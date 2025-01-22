document.addEventListener('DOMContentLoaded', function () {
    handleErrorModal();
    initializeCreateModal();
    initializeUpdateModal();
    initializeDeleteModal();
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

// Manejo del modal "Crear Usuario"
function initializeCreateModal() {
    const createUsuarioModal = new bootstrap.Modal(document.getElementById('createUsuarioModal'), {
        keyboard: false
    });

    const createButton = document.querySelector('.buttonPersistUsuario');
    if (createButton) {
        createButton.addEventListener('click', function () {
            createUsuarioModal.show();
        });
    }
}

// Inicializar modal de actualización para usuarios
function initializeUpdateModal() {
    const updateModal = new bootstrap.Modal(document.getElementById('updateUsuarioModal'));

    const updateButtons = document.querySelectorAll('.buttonUpdateUsuario[data-id]');

    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');
            populateUpdateModal(id);
            updateModal.show();
        });
    });

    const updateModalElement = document.getElementById('updateUsuarioModal');
    updateModalElement.addEventListener('hidden.bs.modal', function () {
        const form = updateModalElement.querySelector('form');
        if (form) form.reset();
    });
}

function populateUpdateModal(id) {
    fetch(`/tableUsuarios/${id}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('id').value = data.id;
            document.getElementById('nombre').value = data.nombre;
            document.getElementById('contrasenia').value = data.contrasenia;
            document.getElementById('contacto').value = data.contacto;
            document.getElementById('rol').value = data.rolId;

            // Establecer el nombre del rol seleccionado
            const rolSelect = document.getElementById('rol');
            for (let i = 0; i < rolSelect.options.length; i++) {
                if (rolSelect.options[i].value == data.rolId) {
                    rolSelect.options[i].selected = true;
                    break;
                }
            }
        })
        .catch(error => console.error('Error al cargar los datos del usuario:', error));
}

// Inicializar modal de eliminación para usuarios
function initializeDeleteModal() {
    const deleteButtons = document.querySelectorAll('.delete-button');
    const confirmDeleteButton = document.getElementById('confirmDeleteButton');
    let deleteId = null;

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Evita el envío automático del formulario
            deleteId = this.getAttribute('data-id');
            const advertenciaMessage = '¿Está seguro que desea eliminar este dato?'; // Mensaje predeterminado
            const modalBody = document.querySelector('#advertenciaModalGlobal .modal-body p');
            modalBody.textContent = advertenciaMessage;

            const modal = new bootstrap.Modal(document.getElementById('advertenciaModalGlobal'));
            modal.show();
        });
    });

    if (confirmDeleteButton) {
        confirmDeleteButton.addEventListener('click', function () {
            if (deleteId) {
                fetch(`/tableUsuarios/${deleteId}`, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        alert('Error al eliminar el usuario.');
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