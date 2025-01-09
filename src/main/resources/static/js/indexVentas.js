document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeCreateModal();
    initializeDeleteModal();
    initializeTotalInput();
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