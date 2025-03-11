document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeUpdateModal();
    initializeDepositoSelect();
});

function initializeDepositoSelect() {
    const depositoSelect = document.getElementById('depositoProductoId');
    if (depositoSelect) {
        depositoSelect.addEventListener('change', function () {
            const depositoProductoId = this.value;
            fetchProductoPorDeposito(depositoProductoId);
        });
    }
}

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
    const filter = input.value.toLowerCase();
    const tableBody = document.querySelector('.custom-table tbody');

    tableBody.innerHTML = '<tr><td colspan="12" class="text-center">Cargando...</td></tr>';

    fetch(`/tableDepositoProducto/search?name=${filter}&page=0&size=15`)
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
            initializeUpdateModal();
        })
        .catch(error => {
            console.error('Error al realizar la búsqueda:', error);
            tableBody.innerHTML = '<tr><td colspan="12" class="text-center text-danger">Error al cargar los datos</td></tr>';
        });
}

function initializeUpdateModal() {
    const updateModal = new bootstrap.Modal(document.getElementById('updateDepositoProductoModal'));
    const updateButtons = document.querySelectorAll('.buttonUpdateProducto[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            populateUpdateModal(id);
            updateModal.show();
        });
    });

    const updateModalElement = document.getElementById('updateDepositoProductoModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        document.getElementById('id').value = '';
        document.getElementById('macAddress').value = '';
        document.getElementById('numeroDeSerie').value = '';
        document.getElementById('codigoBarras').value = '';
        const form = updateModalElement.querySelector('form');
        if (form) form.reset();
    });
}

function populateUpdateModal(id) {
    fetch(`/tableProductos/${id}`)
        .then(response => response.json())
        .then(data => {
        document.getElementById('id').value = data.id;
        document.getElementById('macAddress').value = data.macAddress || '';
        document.getElementById('numeroDeSerie').value = data.numeroDeSerie || '';
        document.getElementById('codigoBarras').value = data.codigoBarras || '';

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

function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}