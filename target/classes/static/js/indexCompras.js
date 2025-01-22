document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModal();
    initializeDeleteModal();
    initializeAddDetalleButton();
    initializeTotalInput();
    initializeFormSubmit();
});

function initializeTotalInput() {
    const totalInput = document.getElementById('total');
    if (totalInput) {
        totalInput.addEventListener('input', function () {
            this.value = formatNumber(this.value);
        });
    }
}

function formatNumber(value) {
    value = value.replace(/[^\d.,]/g, '');
    value = value.replace(/,/g, '.');
    const parts = value.split('.');
    if (parts.length > 2) {
        value = parts[0] + '.' + parts.slice(1).join('');
    }
    return value;
}

function initializeFormSubmit() {
    const form = document.getElementById('createCompraForm');
    if (form) {
        form.addEventListener('submit', function (event) {
            const totalInput = document.getElementById('total');
            if (totalInput) {
                totalInput.value = formatNumber(totalInput.value);
            }
        });
    }
}

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

function agregarFilaDetalle() {
    const detalleContainer = document.getElementById("detalleContainer");
    const newRow = document.createElement("div");
    newRow.classList.add("row", "detalle-row");
    const currentIndex = detalleContainer.querySelectorAll('.detalle-row').length;

    // Obtener el HTML del select de productos y depósitos del primer detalle
    const productoSelectHTML = document.querySelector('select[name="detallesCompra[0].productoId"]').innerHTML;
    const depositoSelectHTML = document.querySelector('select[name="detallesCompra[0].depositoId"]').innerHTML;

    newRow.innerHTML = `
        <div class="col-md-3 mb-3">
            <label for="producto${currentIndex}" class="form-label form-labelForms">Producto</label>
            <select class="form-control form-control-sm form-control-detalle" name="detallesCompra[${currentIndex}].productoId" id="producto${currentIndex}" required>
                ${productoSelectHTML}
            </select>
        </div>
        <div class="col-md-3 mb-3">
            <label for="deposito${currentIndex}" class="form-label form-labelForms">Depósito</label>
            <select class="form-control form-control-sm form-control-detalle" name="detallesCompra[${currentIndex}].depositoId" id="deposito${currentIndex}" required>
                ${depositoSelectHTML}
            </select>
        </div>
        <div class="col-md-3 mb-3">
            <label for="cantidad${currentIndex}" class="form-label form-labelForms">Cantidad</label>
            <input type="number" class="form-control form-control-sm form-control-detalle" name="detallesCompra[${currentIndex}].cantidad" id="cantidad${currentIndex}" placeholder="Ingrese la cantidad..." required>
        </div>
        <div class="col-md-3 mb-3">
            <label for="precioUnitario${currentIndex}" class="form-label form-labelForms">Precio unitario del producto</label>
            <input type="number" class="form-control form-control-sm form-control-detalle" name="detallesCompra[${currentIndex}].precioUnitario" id="precioUnitario${currentIndex}" placeholder="Ingrese el precio unitario..." required>
        </div>
    `;

    detalleContainer.appendChild(newRow);
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
    const filter = input.value;
    const tableBody = document.querySelector('.custom-table tbody');

    tableBody.innerHTML = '<tr><td colspan="8" class="text-center">Cargando...</td></tr>';

    fetch(`/tableCompras/search?nombreProveedor=${filter}&page=0&size=15`)
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
            initializeUpdateModal(); // Re-inicializar los eventos de los botones de edición
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
            updateModalElement.querySelector('#total').value = data.totalString;
            updateModalElement.querySelector('#metodoPago').value = data.metodoPago;
            updateModalElement.querySelector('#estado').value = data.estado;
            updateModalElement.querySelector('#impuestos').value = data.impuestos;
            updateModalElement.querySelector('#proveedor').value = data.proveedorId;

            const detalleContainer = document.getElementById('detalleContainerUpdate');
            detalleContainer.innerHTML = '';

            data.detallesCompra.forEach((detalle, index) => {
                const newRow = document.createElement('div');
                newRow.classList.add('row', 'detalle-row');

                newRow.innerHTML = `
                    <div class="col-md-3 mb-3">
                        <label for="producto${index}" class="form-label form-labelForms">Producto</label>
                        <select class="form-control form-control-sm form-control-detalle" name="detallesCompra[${index}].productoId" id="producto${index}" required>
                            ${document.querySelector('select[name="detallesCompra[0].productoId"]').innerHTML}
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="deposito${index}" class="form-label form-labelForms">Depósito</label>
                        <select class="form-control form-control-sm form-control-detalle" name="detallesCompra[${index}].depositoId" id="deposito${index}" required>
                            ${document.querySelector('select[name="detallesCompra[0].depositoId"]').innerHTML}
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="cantidad${index}" class="form-label form-labelForms">Cantidad</label>
                        <input type="number" class="form-control form-control-sm form-control-detalle" name="detallesCompra[${index}].cantidad" id="cantidad${index}" placeholder="Ingrese la cantidad..." required>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="precioUnitario${index}" class="form-label form-labelForms">Precio unitario del producto</label>
                        <input type="text" class="form-control form-control-sm form-control-detalle" name="detallesCompra[${index}].precioUnitarioString" id="precioUnitario${index}" placeholder="Ingrese el precio unitario..." required>
                    </div>
                `;

                detalleContainer.appendChild(newRow);

                newRow.querySelector(`#producto${index}`).value = detalle.productoId;
                newRow.querySelector(`#deposito${index}`).value = detalle.depositoId;
                newRow.querySelector(`#cantidad${index}`).value = detalle.cantidad;
                newRow.querySelector(`#precioUnitario${index}`).value = detalle.precioUnitarioString;
            });
        })
        .catch(error => console.error('Error al cargar los datos de la compra:', error));
}

function initializeAddDetalleButton() {
    const addDetalleButton = document.getElementById("addDetalleButton");
    if (addDetalleButton) {
        addDetalleButton.addEventListener("click", agregarFilaDetalle);
    }
}

function showDetalleCompraModal(button) {
    const compraId = button.getAttribute('data-id');
    fetch(`/detallesCompra/${compraId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los detalles de la compra');
            }
            return response.json();
        })
        .then(data => {
            if (!Array.isArray(data)) {
                throw new Error('La respuesta no es un array');
            }
            const modalBody = document.querySelector('#createDetalleComprasModal .modal-body tbody');
            modalBody.innerHTML = '';

            data.forEach(detalle => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${detalle.id}</td>
                    <td>${detalle.productoNombre}</td>
                    <td>${detalle.cantidad}</td>
                    <td>${detalle.precioUnitarioString}</td>
                    <td>${detalle.totalFormatted}</td>
                `;
                modalBody.appendChild(row);
            });

            const detalleModal = new bootstrap.Modal(document.getElementById('createDetalleComprasModal'));
            detalleModal.show();
        })
        .catch(error => {
            console.error('Error al cargar los detalles de la compra:', error);
            const modalBody = document.getElementById('errorModalBody');
            modalBody.innerHTML = error.message;

            const errorModal = new bootstrap.Modal(document.getElementById('errorModalGlobal'));
            errorModal.show();
        });
}

// Inicializar modal de eliminación para compras
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
                fetch(`/tableCompras/${deleteId}`, {
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

// Utilidad: Función de debounce para limitar solicitudes frecuentes
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}