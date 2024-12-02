document.addEventListener('DOMContentLoaded', function () {
    const errorMessage = /*[[${errorMessage}]]*/ '';
    if (errorMessage) {
        const modal = new bootstrap.Modal(document.getElementById('errorModal'));
        modal.show();
    }

    const submitBtn = document.getElementById('submitBtn');
    const form = document.getElementById('formLogin');

    submitBtn.addEventListener('click', function (event) {
        event.preventDefault(); // Prevenir el envío del formulario

        // Validar el formulario manualmente
        if (form.checkValidity() === false) {
            event.stopPropagation();
            form.classList.add('was-validated');
        } else {
            // Mostrar el modal de error si hay errores
            const modal = new bootstrap.Modal(document.getElementById('errorModal'));
            modal.show();
        }
    });
});