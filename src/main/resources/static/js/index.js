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
