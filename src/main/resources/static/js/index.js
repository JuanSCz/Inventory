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

function filterTable() {
    const input = document.querySelector('.search-input');
    const filter = input.value.toLowerCase();
    const table = document.querySelector('.custom-table tbody');
    const rows = table.querySelectorAll('tr');

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        const nameCell = cells[1]; // Assuming the name is in the second column
        if (nameCell) {
            const name = nameCell.textContent.toLowerCase();
            if (name.includes(filter)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        }
    });
}
