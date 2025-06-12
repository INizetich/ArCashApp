document.addEventListener("DOMContentLoaded", () => {
    const body = document.body;
    const toggleModeBtn = document.getElementById("toggle-mode");
    const toggleVisibilityBtn = document.getElementById("toggle-visibility");
    const balanceElement = document.getElementById("balance");
    const eyeIcon = document.getElementById("eye-icon");

    let balanceVisible = true;

    toggleModeBtn.addEventListener("click", () => {
        body.classList.toggle("dark-mode");
    });

    toggleVisibilityBtn.addEventListener("click", () => {
        if (balanceVisible) {
            balanceElement.textContent = "$******";
            eyeIcon.classList.replace("fa-eye", "fa-eye-slash");
        } else {
            balanceElement.textContent = "$2.960,34"; // o obtener dinámicamente el valor
            eyeIcon.classList.replace("fa-eye-slash", "fa-eye");
        }
        balanceVisible = !balanceVisible;
    });
});
