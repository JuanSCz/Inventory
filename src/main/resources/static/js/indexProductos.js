document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModal();
    initializeDeleteModal();
    initializeAddStockButton();
    initializeCostoInput();
    initializePrecioInput();
});

function initializeCostoInput() {
    const costoInput = document.getElementById('costo');
    if (costoInput) {
        costoInput.addEventListener('input', function () {
            this.value = formatNumber(this.value);
        });
    }
}

function initializePrecioInput() {
    const precioInput = document.getElementById('precio');
    if (precioInput) {
        precioInput.addEventListener('input', function () {
            this.value = formatNumber(this.value);
        });
    }
}

function formatNumber(value) {
    value = value.replace(/[^\d,]/g, ''); // Eliminar cualquier carácter que no sea dígito o coma
    const parts = value.split(',');
    if (parts.length > 2) {
        value = parts[0] + ',' + parts.slice(1).join('');
    }
    return value.replace(/\B(?=(\d{3})+(?!\d))/g, '.'); // Agregar puntos como separadores de miles
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

// Manejo de la búsqueda en tiempo real para Productos
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

// Manejo del modal "Crear Producto"
function initializeCreateModal() {
    const createProductoModal = new bootstrap.Modal(document.getElementById('createProductoModal'), {
        keyboard: false
    });

    const createButton = document.querySelector('.buttonPersistProducto');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createProductoModal.show();
        });
    }
}

// Inicializar modal de actualización para productos
function initializeUpdateModal() {
    const updateModal = new bootstrap.Modal(document.getElementById('updateProductoModal'));
    const updateButtons = document.querySelectorAll('.buttonUpdateProducto[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            populateUpdateModal(id);
            updateModal.show();
        });
    });

    const updateModalElement = document.getElementById('updateProductoModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        document.getElementById('id').value = '';
        document.getElementById('nombre').value = '';
        document.getElementById('descripcion').value = '';
        document.getElementById('precio').value = '';
        document.getElementById('costo').value = '';
        document.getElementById('codigoBarras').value = '';
        document.getElementById('numeroDeSerie').value = '';
        document.getElementById('stockActual').value = '';
        document.getElementById('stockMinimo').value = '';
        document.getElementById('macAddress').value = '';
        document.getElementById('categoriaId').value = '';

        const form = updateModalElement.querySelector('form');
        if (form) form.reset();
    });
}

function populateUpdateModal(id) {
    fetch(`/tableProductos/${id}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('id').value = data.id;
            document.getElementById('nombre').value = data.nombre;
            document.getElementById('descripcion').value = data.descripcion;
            document.getElementById('precio').value = data.precioString;
            document.getElementById('costo').value = data.costoString;
            document.getElementById('stockActual').value = data.stockActual;
            document.getElementById('stockMinimo').value = data.stockMinimo;
            document.getElementById('categoriaId').value = data.categoriaId;

            const stockContainer = document.getElementById('stockContainerUpdate');
            stockContainer.innerHTML = '';

            data.stocks.forEach((stock, index) => {
                const newRow = document.createElement('div');
                newRow.classList.add('row', 'stock-row');

                newRow.innerHTML = `
                    <div class="col-md-6 mb-3">
                        <label for="depositoId${index}" class="form-label form-labelForms">Depósito</label>
                        <select class="form-control form-control-sm form-control-sm form-control-detalle" name="stocks[${index}].depositoId" id="depositoId${index}" required>
                            ${document.querySelector('select[name="stocks[0].depositoId"]').innerHTML}
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="cantidad${index}" class="form-label form-labelForms">Cantidad</label>
                        <input type="number" class="form-control form-control-sm form-control-sm form-control-detalle" name="stocks[${index}].cantidad" id="cantidad${index}" placeholder="Ingrese la cantidad" required>
                    </div>
                `;

                stockContainer.appendChild(newRow);

                newRow.querySelector(`#depositoId${index}`).value = stock.depositoId;
                newRow.querySelector(`#depositoId${index} option[value="${stock.depositoId}"]`).text = stock.depositoNombre;
                newRow.querySelector(`#cantidad${index}`).value = stock.cantidad;
            });
        })
        .catch(error => console.error('Error al cargar los datos del producto:', error));
}

function initializeDeleteModal() {
    const deleteButtons = document.querySelectorAll('.delete-button');
    const confirmDeleteButton = document.getElementById('confirmDeleteButton');
    let deleteId = null;

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            deleteId = this.getAttribute('data-id');
            const advertenciaMessage = '¿Está seguro que desea eliminar este producto?';
            const modalBody = document.querySelector('#advertenciaModalGlobal .modal-body p');
            modalBody.textContent = advertenciaMessage;

            const modal = new bootstrap.Modal(document.getElementById('advertenciaModalGlobal'));
            modal.show();
        });
    });

    if (confirmDeleteButton) {
        confirmDeleteButton.addEventListener('click', function () {
            if (deleteId) {
                fetch(`/tableProductos/${deleteId}`, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        alert('Error al eliminar el producto.');
                    }
                });
            }
        });
    }
}

function initializeAddStockButton() {
    const addStockButton = document.getElementById("addStockButton");
    if (addStockButton) {
        addStockButton.addEventListener("click", agregarFilaStock);
    }
}

function agregarFilaStock() {
    const stockContainer = document.getElementById("stockContainer");
    const newRow = document.createElement("div");
    newRow.classList.add("row", "stocks-row");
    const currentIndex = stockContainer.querySelectorAll('.stocks-row').length;

    // Obtener el HTML del select de depósitos del primer stock
    const depositoSelectHTML = document.querySelector('select[name="stocks[0].depositoId"]').innerHTML;

    newRow.innerHTML = `
        <div class="col-md-6 mb-3">
            <label for="depositoId${currentIndex}" class="form-label form-labelForms">Depósito</label>
            <select class="form-control form-control-sm form-control-detalle" name="stocks[${currentIndex}].depositoId" id="depositoId${currentIndex}" required>
                ${depositoSelectHTML}
            </select>
        </div>
        <div class="col-md-6 mb-3">
            <label for="cantidad${currentIndex}" class="form-label form-labelForms">Cantidad</label>
            <input type="number" class="form-control form-control-sm form-control-detalle" name="stocks[${currentIndex}].cantidad" id="cantidad${currentIndex}" placeholder="Ingrese la cantidad" required>
        </div>
    `;

    stockContainer.appendChild(newRow);
}

// Utilidad: Función de debounce para limitar solicitudes frecuentes
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}