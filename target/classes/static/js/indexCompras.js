document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModal();
    initializeAddDetalleButton();
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
    const productoSelectHTML = document.querySelector('select[name="detallesCompra[0].productoId"]').innerHTML;

    newRow.innerHTML = `
        <div class="col-md-4 mb-3">
            <label for="producto${currentIndex}" class="form-label">Producto</label>
            <select class="form-control form-control-detalle" name="detallesCompra[${currentIndex}].productoId" id="producto${currentIndex}" required>
                ${productoSelectHTML}
            </select>
        </div>
        <div class="col-md-4 mb-3">
            <label for="cantidad${currentIndex}" class="form-label">Cantidad</label>
            <input type="number" class="form-control form-control-detalle" name="detallesCompra[${currentIndex}].cantidad" id="cantidad${currentIndex}" placeholder="Ingrese la cantidad..." required>
        </div>
        <div class="col-md-4 mb-3">
            <label for="precioUnitario${currentIndex}" class="form-label">Precio Unitario</label>
            <input type="number" class="form-control form-control-detalle" name="detallesCompra[${currentIndex}].precioUnitario" id="precioUnitario${currentIndex}" placeholder="Ingrese el precio unitario..." required>
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
            updateModalElement.querySelector('#total').value = data.total.toFixed(2);
            updateModalElement.querySelector('#metodoPago').value = data.metodoPago;
            updateModalElement.querySelector('#estado').value = data.estado;
            updateModalElement.querySelector('#impuestos').value = data.impuestos.toFixed(2);
            updateModalElement.querySelector('#proveedor').value = data.proveedorId;

            const detalleContainer = document.getElementById('detalleContainerUpdate');
            detalleContainer.innerHTML = ''; // Limpiar detalles existentes

            data.detallesCompra.forEach((detalle, index) => {
                const newRow = document.createElement('div');
                newRow.classList.add('row', 'detalle-row');

                newRow.innerHTML = `
                    <div class="col-md-4 mb-3">
                        <label for="producto${index}" class="form-label">Producto</label>
                        <select class="form-control form-control-detalle" name="detallesCompra[${index}].productoId" id="producto${index}" required>
                            ${document.querySelector('select[name="detallesCompra[0].productoId"]').innerHTML}
                        </select>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="cantidad${index}" class="form-label">Cantidad</label>
                        <input type="number" class="form-control form-control-detalle" name="detallesCompra[${index}].cantidad" id="cantidad${index}" placeholder="Ingrese la cantidad..." required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="precioUnitario${index}" class="form-label">Precio Unitario</label>
                        <input type="number" step="0.01" class="form-control form-control-detalle" name="detallesCompra[${index}].precioUnitario" id="precioUnitario${index}" placeholder="Ingrese el precio unitario..." required>
                    </div>
                `;

                detalleContainer.appendChild(newRow);

                // Set values
                newRow.querySelector(`#producto${index}`).value = detalle.productoId;
                newRow.querySelector(`#cantidad${index}`).value = detalle.cantidad;
                newRow.querySelector(`#precioUnitario${index}`).value = detalle.precioUnitario.toFixed(2);
            });
        })
        .catch(error => console.error('Error al cargar los datos de la compra:', error));
}

// Utilidad: Función de debounce para limitar solicitudes frecuentes
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}