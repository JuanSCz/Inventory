document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.querySelector('.sidebar');
    const navbarToggler = document.querySelector('.navbar-toggler');

    navbarToggler.addEventListener('click', function () {
        sidebar.classList.toggle('show');
    });

    window.addEventListener('resize', function () {
        if (window.innerWidth >= 768 && window.innerWidth <= 1024) {
            sidebar.classList.add('tablet-sidebar');
        } else {
            sidebar.classList.remove('tablet-sidebar');
        }
    });

    // Trigger resize event on load to set initial state
    window.dispatchEvent(new Event('resize'));
});