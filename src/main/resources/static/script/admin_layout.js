document.addEventListener('DOMContentLoaded', function() {

    const userMenuButton = document.getElementById('userMenuButton');
    const userMenuDropdown = document.getElementById('userMenuDropdown');

    if (userMenuButton && userMenuDropdown) {
        userMenuButton.addEventListener('click', function(event) {
            event.stopPropagation();
            userMenuDropdown.classList.toggle('show');
        });

        window.addEventListener('click', function(event) {
            if (userMenuDropdown.classList.contains('show')) {
                userMenuDropdown.classList.remove('show');
            }
        });
    }
});