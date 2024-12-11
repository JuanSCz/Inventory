document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModal();
    initializeDetalleModal();
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
        searchInput.addEventListener('input', debounce(filterTable, 300));
    }
}

function filterTable() {
    const input = document.querySelector('.search-input');
    const filter = input.value;
    const tableBody = document.querySelector('.custom-table tbody');

    tableBody.innerHTML = '<tr><td colspan="8" class="text-center">Cargando...</td></tr>';

    fetch(`/tableCompras/search?fecha=${filter}&page=0&size=15`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al buscar compras');
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTableBody = doc.querySelector('.custom-table tbody');
            const pagination = doc.querySelector('.pagination');
            tableBody.innerHTML = newTableBody ? newTableBody.innerHTML : '<tr><td colspan="8" class="text-center">No se encontraron resultados</td></tr>';
            document.querySelector('.pagination').innerHTML = pagination ? pagination.innerHTML : '';
            initializeUpdateModal();
        })
        .catch(error => {
            console.error('Error al realizar la búsqueda:', error);
            tableBody.innerHTML = '<tr><td colspan="8" class="text-center text-danger">Error al cargar los datos</td></tr>';
        });
}

// Manejo del modal "Crear Compra"
function initializeCreateModal() {
    const createCompraModal = new bootstrap.Modal(document.getElementById('createCompraModal'), {
        keyboard: false
    });

    const createButton = document.querySelector('.buttonPersistCompra');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createCompraModal.show();
        });
    }
}

// Manejo del modal de detalle
function initializeDetalleModal() {
    const createDetalleModal = new bootstrap.Modal(document.getElementById('createDetalleModal'), {
        keyboard: false
    });

    const generarDetalleButton = document.querySelector('.buttonPersistDetalle');
    if (generarDetalleButton) {
        generarDetalleButton.addEventListener('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            createDetalleModal.show();
        });
    }

    // Ajustar z-index de los modales y sus fondos
    document.getElementById('createDetalleModal').addEventListener('shown.bs.modal', function () {
        const modals = document.querySelectorAll('.modal');
        const tier = modals.length - 1;
        const backdrop = document.querySelector('.modal-backdrop');
        if (backdrop) {
            backdrop.style.zIndex = 1030 + tier * 30;
        }
        this.style.zIndex = 1040 + tier * 30;
    });
}

// Inicializar modal de actualización para compras
function initializeUpdateModal() {
    const updateModal = new bootstrap.Modal(document.getElementById('updateCompraModal'));

    const updateButtons = document.querySelectorAll('.buttonUpdateCompra[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            populateUpdateModal(id);
            updateModal.show();
        });
    });

    const updateModalElement = document.getElementById('updateCompraModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        document.getElementById('id').value = '';
        document.getElementById('fecha').value = '';
        document.getElementById('total').value = '';
        document.getElementById('metodoPago').value = '';
        document.getElementById('estado').value = '';
        document.getElementById('impuestos').value = '';
        document.getElementById('proveedor').value = '';

        const form = updateModalElement.querySelector('form');
        if (form) form.reset();
    });
}

function populateUpdateModal(id) {
    fetch(`/tableCompras/${id}`)
        .then(response => response.json())
        .then(data => {
            const updateModalElement = document.getElementById('updateCompraModal');
            updateModalElement.querySelector('#id').value = data.id;
            updateModalElement.querySelector('#fecha').value = data.fecha;
            updateModalElement.querySelector('#total').value = data.total;
            updateModalElement.querySelector('#metodoPago').value = data.metodoPago;
            updateModalElement.querySelector('#estado').value = data.estado;
            updateModalElement.querySelector('#impuestos').value = data.impuestos;
            updateModalElement.querySelector('#proveedor').value = data.proveedorId;
        })
        .catch(error => console.error('Error al cargar los datos de la compra:', error));
}

function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}