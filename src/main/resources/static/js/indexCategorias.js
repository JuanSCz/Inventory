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

// Manejo de la búsqueda en tiempo real
function initializeSearch() {
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(filterTable, 300)); // Agregamos debounce para evitar múltiples solicitudes
    }
}

function filterTable() {
    const input = document.querySelector('.search-input');
    const filter = input.value.toLowerCase();
    const tableBody = document.querySelector('.custom-table tbody');

    tableBody.innerHTML = '<tr><td colspan="7" class="text-center">Cargando...</td></tr>';

    fetch(`/tableCategorias/search?name=${filter}&page=0&size=15`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al buscar categorías');
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTableBody = doc.querySelector('.custom-table tbody');
            const pagination = doc.querySelector('.pagination');
            tableBody.innerHTML = newTableBody ? newTableBody.innerHTML : '<tr><td colspan="7" class="text-center">No se encontraron resultados</td></tr>';
            document.querySelector('.pagination').innerHTML = pagination ? pagination.innerHTML : '';
            initializeUpdateModal(); // Re-inicializar los eventos de los botones de edición
        })
        .catch(error => {
            console.error('Error al realizar la búsqueda:', error);
            tableBody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Error al cargar los datos</td></tr>';
        });
}

// Manejo del modal "Crear Categoria"
function initializeCreateModal() {
    const createCategoriaModal = new bootstrap.Modal(document.getElementById('createCategoriaModal'), {
        keyboard: false
    });

    const createButton = document.querySelector('.buttonPersistCategoria');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createCategoriaModal.show();
        });
    }

    const createModalElement = document.getElementById('createCategoriaModal');
    createModalElement.addEventListener('hidden.bs.modal', function () {
        const form = createModalElement.querySelector('form');
        if (form) {
            form.reset();
            // Limpiar mensajes de error
            form.querySelectorAll('.is-invalid').forEach(element => element.classList.remove('is-invalid'));
            form.querySelectorAll('.invalid-feedback').forEach(element => element.remove());
        }
    });
}

// Inicializar modal de actualización para categorías
function initializeUpdateModal() {
    const updateModal = new bootstrap.Modal(document.getElementById('updateCategoriaModal'));

    const updateButtons = document.querySelectorAll('.buttonUpdateCategoria[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            populateUpdateModal(id);
            updateModal.show();
        });
    });

    const updateModalElement = document.getElementById('updateCategoriaModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        const form = updateModalElement.querySelector('form');
        if (form) {
            form.reset();
            // Limpiar mensajes de error
            form.querySelectorAll('.is-invalid').forEach(element => element.classList.remove('is-invalid'));
            form.querySelectorAll('.invalid-feedback').forEach(element => element.remove());
        }
    });
}

function populateUpdateModal(id) {
    fetch(`/tableCategorias/${id}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('id').value = data.id;
            document.getElementById('nombre').value = data.nombre;
            document.getElementById('descripcion').value = data.descripcion;
        })
        .catch(error => console.error('Error al cargar los datos de la categoría:', error));
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
                fetch(`/tableCategorias/${deleteId}`, {
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
