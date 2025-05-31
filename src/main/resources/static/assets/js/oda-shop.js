let form = document.getElementById('profile-edit');
if(form) {
    const button = document.getElementById("refresh-button");
    const inputs = form.querySelectorAll('input, textarea, select');

    inputs.forEach(input => {
        input.addEventListener('input', function() {
            button.classList.replace("button-area", "button-primary");
            button.removeAttribute("disabled");
        });
    });
}