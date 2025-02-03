document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModal();
    initializeDeleteModal();
});

// Manejo del modal de error
function handleErrorModal() {
    const errorMessage = document.body.getAttribute('data-error-message');

    if (errorMessage) {
        const modalBody = document.getElementById('errorModalBody');
        modalBody.innerHTML = errorMessage;

        const errorModal = new bootstrap.Modal(document.getElementById('errorModalGlobal'));
        errorModal.show();
    }
}

// Manejo de la búsqueda en tiempo real para Proveedores
function initializeSearch() {
    const searchInputs = document.querySelectorAll('.search-input');
    searchInputs.forEach(searchInput => {
        searchInput.addEventListener('input', debounce(filterTable, 300)); // Agregamos debounce para evitar múltiples solicitudes
    });
}

function filterTable(event) {
    const input = event.target;
    const filter = input.value.toLowerCase();
    const table = input.closest('form').querySelector('input[name="table"]').value;
    const tableBody = document.querySelector('.custom-table tbody');

    // Mostrar mensaje de carga
    tableBody.innerHTML = '<tr><td colspan="7" class="text-center">Cargando...</td></tr>';

    fetch(`/main/search?table=${table}&name=${filter}&page=0`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al buscar en la tabla');
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTableBody = doc.querySelector('.custom-table tbody');
            const pagination = doc.querySelector('.pagination');

            // Actualizar el contenido de la tabla
            if (newTableBody) {
                tableBody.innerHTML = newTableBody.innerHTML;
            } else {
                tableBody.innerHTML = '<tr><td colspan="7" class="text-center">No se encontraron resultados</td></tr>';
            }

            // Actualizar la paginación
            if (pagination) {
                document.querySelector('.pagination').innerHTML = pagination.innerHTML;
            } else {
                document.querySelector('.pagination').innerHTML = '';
            }

            initializeUpdateModal(); // Re-inicializar los eventos de los botones de edición
        })
        .catch(error => {
            console.error('Error al realizar la búsqueda:', error);
            tableBody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Error al cargar los datos</td></tr>';
        });
}

// Manejo del modal "Crear Proveedor"
function initializeCreateModal() {
    const createProveedorModal = new bootstrap.Modal(document.getElementById('createProveedorModal'), {
        keyboard: false
    });

    const createButton = document.querySelector('.buttonPersistProveedor');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createProveedorModal.show();
        });
    }
}

// Inicializar modal de actualización para proveedores
function initializeUpdateModal() {
    // Crear una única instancia del modal
    const updateModal = new bootstrap.Modal(document.getElementById('updateProveedorModal'));

    // Seleccionar todos los botones relacionados con la actualización
    const updateButtons = document.querySelectorAll('.buttonUpdateProveedor[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id'); // Obtener el ID del proveedor
            populateUpdateModal(id); // Llenar los datos del modal con el ID del proveedor
            updateModal.show(); // Mostrar el modal usando la misma instancia
        });
    });

    // Limpiar el modal cuando se cierra
    const updateModalElement = document.getElementById('updateProveedorModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        // Limpiar los campos del formulario
        document.getElementById('id').value = '';
        document.getElementById('nombre').value = '';
        document.getElementById('email').value = '';
        document.getElementById('contacto').value = '';
        document.getElementById('pais').value = '';
        document.getElementById('direccion').value = '';

        // Opcional: limpiar clases de validación, si usas alguna
        const form = updateModalElement.querySelector('form');
        if (form) form.reset();
    });
}

function populateUpdateModal(id) {
    fetch(`/tableProveedores/${id}`)
        .then(response => response.json())
        .then(data => {
            console.log(data); // Verificar los datos recibidos
            document.getElementById('id').value = data.id;
            document.getElementById('nombre').value = data.nombre;
            document.getElementById('email').value = data.emailProveedor;
            document.getElementById('contacto').value = data.contactoProveedor;
            document.getElementById('pais').value = data.pais;
            document.getElementById('direccion').value = data.direccion;
        })
        .catch(error => console.error('Error al cargar los datos del proveedor:', error));
}

function initializeDeleteModal() {
    const deleteButtons = document.querySelectorAll('.delete-button');
    const confirmDeleteButton = document.getElementById('confirmDeleteButton');
    let deleteId = null;

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Evita el envío automático del formulario
            deleteId = this.getAttribute('data-id');
            const advertenciaMessage = '¿Está seguro que desea eliminar este dato?'; // Mensaje predeterminado
            const modalBody = document.querySelector('#advertenciaModalGlobal .modal-body p');
            modalBody.textContent = advertenciaMessage;

            const modal = new bootstrap.Modal(document.getElementById('advertenciaModalGlobal'));
            modal.show();
        });
    })

    if (confirmDeleteButton) {
        confirmDeleteButton.addEventListener('click', function () {
            if (deleteId) {
                fetch(`/tableProveedores/${deleteId}`, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        alert('Error al eliminar el dato.');
                    }
                });
            }
        });
    }
}

function confirmDelete(form) {
    const deleteId = form.querySelector('.delete-button').getAttribute('data-id');
    const modal = new bootstrap.Modal(document.getElementById('advertenciaModalGlobal'));
    const confirmDeleteButton = document.getElementById('confirmDeleteButton');

    modal.show();

    confirmDeleteButton.addEventListener('click', function () {
        fetch(`/delete/${deleteId}`, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error al eliminar el dato.');
            }
        });
    });

    return false; // Evita el envío del formulario hasta que se confirme la eliminación
}

// Utilidad: Función de debounce para limitar solicitudes frecuentes
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}
