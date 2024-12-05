document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModals();
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

    fetch(`/tableProveedores/search?name=${filter}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al buscar proveedores');
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTableBody = doc.querySelector('.custom-table tbody');
            tableBody.innerHTML = newTableBody ? newTableBody.innerHTML : '<tr><td colspan="7" class="text-center">No se encontraron resultados</td></tr>';
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

    const createButton = document.querySelector('.buttonPersist');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createProveedorModal.show();
        });
    }
}

// Manejo del modal "Actualizar Proveedor"
function initializeUpdateModals() {
    const updateButtons = document.querySelectorAll('.btn-primary[data-id]');
    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            populateUpdateModal(id); // Llama a la función para cargar datos en el modal
        });
    });
}

// Función para poblar el modal de actualización
function populateUpdateModal(id) {
    fetch(`/tableProveedores/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`No se pudo obtener los datos del proveedor con ID ${id}`);
            }
            return response.json();
        })
        .then(data => {
            // Poblar los campos del modal con los datos recibidos
            document.querySelector('#updateProveedorModal [name="nombre"]').value = data.nombre;
            document.querySelector('#updateProveedorModal [name="contactoProveedor"]').value = data.contactoProveedor;
            document.querySelector('#updateProveedorModal [name="direccion"]').value = data.direccion;
            document.querySelector('#updateProveedorModal [name="pais"]').value = data.pais;
            document.querySelector('#updateProveedorModal [name="emailProveedor"]').value = data.emailProveedor;

            // Mostrar el modal
            const updateModal = new bootstrap.Modal(document.getElementById('updateProveedorModal'));
            updateModal.show();
        })
        .catch(error => console.error('Error al obtener los datos del proveedor:', error));
}

// Utilidad: Función de debounce para limitar solicitudes frecuentes
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}