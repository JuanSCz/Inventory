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

// Manejo de la búsqueda en tiempo real para Clientes
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

// Manejo del modal "Crear Cliente"
function initializeCreateModal() {
    const createClienteModal = new bootstrap.Modal(document.getElementById('createClienteModal'), {
        keyboard: false
    });

    const createButton = document.querySelector('.buttonPersistCliente');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createClienteModal.show();
        });
    }
}

// Inicializar modal de actualización para clientes
function initializeUpdateModal() {
    // Crear una única instancia del modal
    const updateModal = new bootstrap.Modal(document.getElementById('updateClienteModal'));

    // Seleccionar todos los botones relacionados con la actualización
    const updateButtons = document.querySelectorAll('.buttonUpdateCliente[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id'); // Obtener el ID del cliente
            populateUpdateModal(id); // Llenar los datos del modal con el ID del cliente
            updateModal.show(); // Mostrar el modal usando la misma instancia
        });
    });

    // Limpiar el modal cuando se cierra
    const updateModalElement = document.getElementById('updateClienteModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        // Limpiar los campos del formulario
        document.getElementById('id').value = '';
        document.getElementById('nombre').value = '';
        document.getElementById('email').value = '';
        document.getElementById('contacto').value = '';
        document.getElementById('país').value = '';
        document.getElementById('dirección').value = '';
        document.getElementById('ciudad').value = '';

        // Opcional: limpiar clases de validación, si usas alguna
        const form = updateModalElement.querySelector('form');
        if (form) form.reset();
    });
}

function populateUpdateModal(id) {
    fetch(`/tableClientes/${id}`)
        .then(response => response.json())
        .then(data => {
            console.log(data); // Verificar los datos recibidos
            document.getElementById('id').value = data.id;
            document.getElementById('nombre').value = data.nombre;
            document.getElementById('email').value = data.eMailCliente;
            document.getElementById('contacto').value = data.contactoCliente;
            document.getElementById('pais').value = data.pais;
            document.getElementById('direccion').value = data.direccion;
            document.getElementById('ciudad').value = data.ciudad;
        })
        .catch(error => console.error('Error al cargar los datos del cliente:', error));
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
    });

    if (confirmDeleteButton) {
        confirmDeleteButton.addEventListener('click', function () {
            if (deleteId) {
                fetch(`/tableClientes/${deleteId}`, {
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
        fetch(`/tableClientes/${deleteId}`, {
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
