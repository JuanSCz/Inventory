document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeUpdateModal();
    initializeDeleteModal();
    initializeAddStockButton();
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

    tableBody.innerHTML = '<tr><td colspan="12" class="text-center">Cargando...</td></tr>';

    fetch(`/tableProductos/search?name=${filter}&page=0&size=15`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al buscar productos');
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTableBody = doc.querySelector('.custom-table tbody');
            const pagination = doc.querySelector('.pagination');
            tableBody.innerHTML = newTableBody ? newTableBody.innerHTML : '<tr><td colspan="12" class="text-center">No se encontraron resultados</td></tr>';
            document.querySelector('.pagination').innerHTML = pagination ? pagination.innerHTML : '';
            initializeUpdateModal(); // Re-inicializar los eventos de los botones de edición
        })
        .catch(error => {
            console.error('Error al realizar la búsqueda:', error);
            tableBody.innerHTML = '<tr><td colspan="12" class="text-center text-danger">Error al cargar los datos</td></tr>';
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
    // Crear una única instancia del modal
    const updateModal = new bootstrap.Modal(document.getElementById('updateProductoModal'));

    // Seleccionar todos los botones relacionados con la actualización
    const updateButtons = document.querySelectorAll('.buttonUpdateProducto[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id'); // Obtener el ID del producto
            populateUpdateModal(id); // Llenar los datos del modal con el ID del producto
            updateModal.show(); // Mostrar el modal usando la misma instancia
        });
    });

    // Limpiar el modal cuando se cierra
    const updateModalElement = document.getElementById('updateProductoModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        // Limpiar los campos del formulario
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

        // Opcional: limpiar clases de validación, si usas alguna
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
            document.getElementById('precio').value = data.precio;
            document.getElementById('costo').value = data.costo;
            document.getElementById('codigoBarras').value = data.codigoBarras;
            document.getElementById('numeroDeSerie').value = data.numeroDeSerie;
            document.getElementById('stockActual').value = data.stockActual;
            document.getElementById('stockMinimo').value = data.stockMinimo;
            document.getElementById('macAddress').value = data.macAddress;
            document.getElementById('categoriaId').value = data.categoriaId;
        })
        .catch(error => console.error('Error al cargar los datos del producto:', error));
}

// Inicializar modal de eliminación para productos
function initializeDeleteModal() {
    const deleteButtons = document.querySelectorAll('.delete-button');
    const confirmDeleteButton = document.getElementById('confirmDeleteButton');
    let deleteId = null;

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Evita el envío automático del formulario
            deleteId = this.getAttribute('data-id');
            const advertenciaMessage = '¿Está seguro que desea eliminar este producto?'; // Mensaje predeterminado
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
    newRow.classList.add("row", "stock-row");
    const currentIndex = stockContainer.querySelectorAll('.stock-row').length;

    // Verificar si el select de depósitos existe antes de acceder a su innerHTML
    const depositoSelect = document.querySelector('select[name="stocks[0].depositoId"]');
    if (!depositoSelect) {
        console.error('El select de depósitos no se encontró en el DOM.');
        return;
    }

    const depositoSelectHTML = depositoSelect.innerHTML;

    newRow.innerHTML = `
        <div class="col-md-6 mb-3">
            <label for="depositoId${currentIndex}" class="form-label">Depósito</label>
            <select class="form-control form-control-producto" name="stocks[${currentIndex}].depositoId" id="depositoId${currentIndex}" required>
                ${depositoSelectHTML}
            </select>
        </div>
        <div class="col-md-6 mb-3">
            <label for="cantidad${currentIndex}" class="form-label">Cantidad</label>
            <input type="number" class="form-control form-control-producto" name="stocks[${currentIndex}].cantidad" id="cantidad${currentIndex}" placeholder="Ingrese la cantidad" required>
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