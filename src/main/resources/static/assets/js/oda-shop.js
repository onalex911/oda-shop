console.log(document.querySelectorAll('[data-toggle="password"]').length);

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
//
// document.getElementById('togglePassword').addEventListener('click', changeIcon('login-password'));
// document.getElementById('toggleRegPassword').addEventListener('click', changeIcon('reg-password'));
// document.getElementById('toggleRegPassword2').addEventListener('click', changeIcon('reg-password2'));
//
// function changeIcon(fieldId) {
//     console.log("changing: "+ fieldId);
//     const passwordInput = document.getElementById(fieldId);
//     const icon = passwordInput.querySelector('i');
//     const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
//     passwordInput.setAttribute('type', type);
//
//     // Меняем иконку
//     icon.classList.toggle('ion ion-md-eye');
//     icon.classList.toggle('ion ion-md-eye-off');
// }
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
