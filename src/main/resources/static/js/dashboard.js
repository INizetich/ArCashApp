document.addEventListener("DOMContentLoaded", () => {
    const body = document.body;
    const toggleModeBtn = document.getElementById("toggle-mode");
    const toggleVisibilityBtn = document.getElementById("toggle-visibility");
    const balanceElement = document.getElementById("balance");
    const eyeIcon = document.getElementById("eye-icon");

    let balanceVisible = true;

    //--- guarda el tema al recargar la pagina ---//
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme === "dark") {
        body.classList.add("dark-mode");
    }

    toggleModeBtn?.addEventListener("click", () => {
        const isDark = body.classList.toggle("dark-mode");
        localStorage.setItem("theme", isDark ? "dark" : "light");
    });

    toggleVisibilityBtn?.addEventListener("click", () => {
        if (balanceVisible) {
            balanceElement.textContent = "$******";
            eyeIcon.classList.replace("fa-eye", "fa-eye-slash");
        } else {
            balanceElement.textContent = "$2.960,34"; // obtener dinámicamente si querés
            eyeIcon.classList.replace("fa-eye-slash", "fa-eye");
        }
        balanceVisible = !balanceVisible;
    });
});

function toggleTheme() {
    const body = document.body;
    const isDark = body.classList.toggle("dark-mode");
    localStorage.setItem("theme", isDark ? "dark" : "light");
}
