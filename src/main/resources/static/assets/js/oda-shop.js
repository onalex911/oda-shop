//console.log(document.querySelectorAll('[data-toggle="password"]').length);

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

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('[data-toggle="password"]').forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.getAttribute('data-target');
            const passwordInput = document.getElementById(targetId);
            const icon = this.querySelector('i');

            if (!passwordInput) return;

            // Переключаем тип поля
            passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';

            // Переключаем иконку (для ionicons)
            icon.classList.toggle('ion-md-eye');
            icon.classList.toggle('ion-md-eye-off');
        });
    });
});
