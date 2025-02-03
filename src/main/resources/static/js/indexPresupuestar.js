document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeSearch();
    initializeFormSubmit();
    initializeTotalInput();
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

// Manejo de la búsqueda en tiempo real para Ventas
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
    tableBody.innerHTML = '<tr><td colspan="8" class="text-center">Cargando...</td></tr>';

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
                tableBody.innerHTML = '<tr><td colspan="8" class="text-center">No se encontraron resultados</td></tr>';
            }

            // Actualizar la paginación
            if (pagination) {
                document.querySelector('.pagination').innerHTML = pagination.innerHTML;
            } else {
                document.querySelector('.pagination').innerHTML = '';
            }

        })
        .catch(error => {
            console.error('Error al realizar la búsqueda:', error);
            tableBody.innerHTML = '<tr><td colspan="8" class="text-center text-danger">Error al cargar los datos</td></tr>';
        });
}

// Utilidad: Función de debounce para limitar solicitudes frecuentes
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}

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
