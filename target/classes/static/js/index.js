document.addEventListener('DOMContentLoaded', function () {
    // Obtener el mensaje de error desde el atributo data-error-message del body
    const errorMessage = document.body.getAttribute('data-error-message');

    if (errorMessage) {
        // Obtener el cuerpo del modal y establecer el mensaje de error
        const modalBody = document.getElementById('errorModalBody');
        modalBody.innerHTML = errorMessage;

        // Mostrar el modal de error
        const errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
        errorModal.show();
    }
});

document.addEventListener('DOMContentLoaded', function () {
    // Vincular el evento input al campo de búsqueda
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        searchInput.addEventListener('input', filterTable);
    }
});

function filterTable() {
    const input = document.querySelector('.search-input');
    const filter = input.value.toLowerCase();

    // Realizar una solicitud AJAX al backend
    fetch(`/tableProveedores/search?name=${filter}`)
        .then(response => response.text())
        .then(html => {
            // Actualizar la tabla con los resultados de búsqueda
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTableBody = doc.querySelector('.custom-table tbody');
            const tableBody = document.querySelector('.custom-table tbody');
            tableBody.innerHTML = newTableBody.innerHTML;
        })
        .catch(error => console.error('Error al realizar la búsqueda:', error));
}
