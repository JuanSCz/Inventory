document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModal();
    initializeDeleteModal();
    initializeAddDetalleButton();
    initializeTotalInput();
    initializeSubtotalInput(); // Nuevo método para inicializar los inputs de subtotal
    initializeFormSubmit();
});

function initializeFormSubmit() {
    const form = document.getElementById('createVentaForm');
    if (form) {
        form.addEventListener('submit', function (event) {
            const totalInput = document.getElementById('total');
            if (totalInput) {
                totalInput.value = formatNumber(totalInput.value);
            }
            const subtotalInputs = document.querySelectorAll('input[name^="detallesVenta"][name$=".subtotalFormatted"]');
            subtotalInputs.forEach(input => {
                input.value = formatNumber(input.value);
            });
        });
    }
}

function initializeTotalInput() {
    const totalInput = document.getElementById('total');
    if (totalInput) {
        totalInput.addEventListener('input', function () {
            this.value = formatNumber(this.value);
        });
    }
}

function initializeFormSubmit() {
    const form = document.getElementById('createVentaForm');
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

function initializeAddDetalleButton() {
    const addDetalleButton = document.getElementById("addDetalleButton");
    if (addDetalleButton) {
        addDetalleButton.addEventListener("click", agregarFilaDetalle);
    }
}

function agregarFilaDetalle() {
    const detalleContainer = document.getElementById("detalleContainer");
    const newRow = document.createElement("div");
    newRow.classList.add("row", "detalle-row");
    const currentIndex = detalleContainer.querySelectorAll('.detalle-row').length;

    // Obtener el HTML del select de productos del primer detalle
    const productoSelectHTML = document.querySelector('select[name="detallesVenta[0].productoId"]').innerHTML;

    newRow.innerHTML = `
        <div class="col-md-4 mb-3">
            <label for="producto${currentIndex}" class="form-label">Producto</label>
            <select class="form-control form-control-detalle" name="detallesVenta[${currentIndex}].productoId" id="producto${currentIndex}">
                ${productoSelectHTML}
            </select>
        </div>
        <div class="col-md-4 mb-3">
            <label for="cantidad${currentIndex}" class="form-label">Cantidad</label>
            <input type="number" step="1" class="form-control form-control-detalle" name="detallesVenta[${currentIndex}].cantidad" id="cantidad${currentIndex}" placeholder="Ingrese la cantidad...">
        </div>
        <div class="col-md-4 mb-3">
            <label for="precioUnitario${currentIndex}" class="form-label">Precio unitario del producto</label>
            <input type="number" class="form-control form-control-detalle" name="detallesVenta[${currentIndex}].precioUnitario" id="precioUnitario${currentIndex}" placeholder="Ingrese el precio unitario...">
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

    fetch(`/tableVentas/search?nombreCliente=${filter}&page=0&size=15`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al buscar ventas');
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

// Manejo del modal "Crear Venta"
function initializeCreateModal() {
    const createVentaModal = new bootstrap.Modal(document.getElementById('createVentaModal'), {
        keyboard: false
    });

    const createButton = document.querySelector('.buttonPersistVenta');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createVentaModal.show();
        });
    }
}

// Inicializar modal de actualización para ventas
function initializeUpdateModal() {
    const updateModal = new bootstrap.Modal(document.getElementById('updateVentaModal'));
    const updateButtons = document.querySelectorAll('.buttonUpdateVenta[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            populateUpdateModal(id);
            updateModal.show();
        });
    });

    const updateModalElement = document.getElementById('updateVentaModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        document.getElementById('id').value = '';
        document.getElementById('fecha').value = '';
        document.getElementById('total').value = '';
        document.getElementById('metodoPago').value = '';
        document.getElementById('estado').value = '';
        document.getElementById('impuestos').value = '';
        document.getElementById('cliente').value = '';
        const form = updateModalElement.querySelector('form');
        if (form) form.reset();
    });
}

function populateUpdateModal(id) {
    fetch(`/tableVentas/${id}`)
        .then(response => response.json())
        .then(data => {
            const updateModalElement = document.getElementById('updateVentaModal');
            updateModalElement.querySelector('#id').value = data.id;
            updateModalElement.querySelector('#fecha').value = data.fecha;
            updateModalElement.querySelector('#total').value = data.totalString;
            updateModalElement.querySelector('#metodoPago').value = data.metodoPago;
            updateModalElement.querySelector('#estado').value = data.estado;
            updateModalElement.querySelector('#impuestos').value = data.impuestos;
            updateModalElement.querySelector('#cliente').value = data.clienteId;

            const detalleContainer = document.getElementById('detalleContainerUpdate');
            detalleContainer.innerHTML = ''; // Limpiar detalles existentes

            data.detallesVenta.forEach((detalle, index) => {
                const newRow = document.createElement('div');
                newRow.classList.add('row', 'detalle-row');

                newRow.innerHTML = `
                    <div class="col-md-4 mb-3">
                        <label for="producto${index}" class="form-label">Producto</label>
                        <select class="form-control form-control-detalle" name="detallesVenta[${index}].productoId" id="producto${index}">
                            ${document.querySelector('select[name="detallesVenta[0].productoId"]').innerHTML}
                        </select>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="cantidad${index}" class="form-label">Cantidad</label>
                        <input type="number" class="form-control form-control-detalle" name="detallesVenta[${index}].cantidad" id="cantidad${index}" placeholder="Ingrese la cantidad...">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="precioUnitario${index}" class="form-label">Precio unitario del producto</label>
                        <input type="text" class="form-control form-control-detalle" name="detallesVenta[${index}].precioUnitarioString" id="precioUnitario${index}" placeholder="Ingrese el precio unitario...">
                    </div>
                `;

                detalleContainer.appendChild(newRow);

                // Set values
                newRow.querySelector(`#producto${index}`).value = detalle.productoId;
                newRow.querySelector(`#cantidad${index}`).value = detalle.cantidad;
                newRow.querySelector(`#precioUnitario${index}`).value = detalle.precioUnitarioString;
            });
        })
        .catch(error => console.error('Error al cargar los datos de la venta:', error));
}

function showDetalleVentaModal(button) {
    const ventaId = button.getAttribute('data-id');
    fetch(`/detallesVentas/${ventaId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los detalles de la venta');
            }
            return response.json();
        })
        .then(data => {
            if (!Array.isArray(data)) {
                throw new Error('La respuesta no es un array');
            }
            const modalBody = document.querySelector('#createDetalleVentasModal .modal-body tbody');
            modalBody.innerHTML = '';

            data.forEach(detalle => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${detalle.id}</td>
                    <td>${detalle.productoNombre}</td>
                    <td>${detalle.cantidad}</td>
                    <td>${detalle.precioUnitarioString}</td>
                    <td>${detalle.subtotalFormatted}</td>
                `;
                modalBody.appendChild(row);
            });

            const detalleModal = new bootstrap.Modal(document.getElementById('createDetalleVentasModal'));
            detalleModal.show();
        })
        .catch(error => console.error('Error al cargar los detalles de la venta:', error));
}

// Inicializar modal de eliminación para ventas
function initializeDeleteModal() {
    const deleteButtons = document.querySelectorAll('.delete-button');
    const confirmDeleteButton = document.getElementById('confirmDeleteButton');
    let deleteId = null;

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Evita el envío automático del formulario
            deleteId = this.getAttribute('data-id');
            const advertenciaMessage = '¿Está seguro que desea eliminar esta venta?'; // Mensaje predeterminado
            const modalBody = document.querySelector('#advertenciaModalGlobal .modal-body p');
            modalBody.textContent = advertenciaMessage;

            const modal = new bootstrap.Modal(document.getElementById('advertenciaModalGlobal'));
            modal.show();
        });
    });

    if (confirmDeleteButton) {
        confirmDeleteButton.addEventListener('click', function () {
            if (deleteId) {
                fetch(`/tableVentas/${deleteId}`, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        alert('Error al eliminar la venta.');
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

function downloadPdf(ventaId) {
    fetch(`/tablePresupuestar/generatePdf/${ventaId}`)
        .then(response => {
            if (response.ok) {
                return response.blob();
            } else {
                throw new Error('Error al generar el PDF');
            }
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'presupuesto.pdf';
            document.body.appendChild(a);
            a.click();
            a.remove();
        })
        .catch(error => {
            console.error('Error:', error);
        });
}