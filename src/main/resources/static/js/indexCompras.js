document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeUpdateModal();
    initializeCreateCompraModal();
});

function handleErrorModal() {
    const errorMessage = document.body.getAttribute('data-error-message');

    if (errorMessage) {
        const modalBody = document.getElementById('errorModalBody');
        modalBody.innerHTML = errorMessage;

        const errorModal = new bootstrap.Modal(document.getElementById('errorModalGlobal'));
        errorModal.show();
    }
}

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

function initializeCreateCompraModal() {
    const addDetalleButton = document.getElementById('addDetalleButton');
    const generateDetalleButton = document.querySelector('button[data-bs-target="#createDetalleModal"]');

    if (generateDetalleButton) {
        generateDetalleButton.addEventListener('click', function (event) {
            if (!validateCreateCompraForm()) {
                event.preventDefault();
                alert('Por favor, complete todos los campos requeridos antes de generar detalles.');
            }
        });
    }

    if (addDetalleButton) {
        addDetalleButton.addEventListener('click', function (event) {
            event.preventDefault();
            const detalleVentaForm = document.getElementById('detalleVentaForm');
            const formData = new FormData(detalleVentaForm);

            fetch(detalleVentaForm.action, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Detalle guardado:', data);
                updateDetalleTable(data);
                const detalleModal = bootstrap.Modal.getInstance(document.getElementById('createDetalleModal'));
                detalleModal.hide();
            })
            .catch(error => {
                console.error('Error al guardar el detalle:', error);
            });
        });
    }
}

function validateCreateCompraForm() {
    const requiredFields = ['fecha', 'total', 'metodoPago', 'estado', 'impuestos', 'proveedor'];
    let isValid = true;

    requiredFields.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (!field.value) {
            isValid = false;
            field.classList.add('is-invalid');
        } else {
            field.classList.remove('is-invalid');
        }
    });

    return isValid;
}

function updateDetalleTable(data) {
    const detalleVentaBody = document.getElementById('detalleVentaBody');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>${data.productoNombre}</td>
        <td>${data.cantidad}</td>
        <td>${data.precioUnitario}</td>
        <td>${data.total}</td>
    `;
    detalleVentaBody.appendChild(newRow);
}

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