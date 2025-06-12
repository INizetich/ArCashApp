document.addEventListener("DOMContentLoaded", () => {
    const body = document.body;
    const toggleModeBtn = document.getElementById("toggle-mode");
    const toggleVisibilityBtn = document.getElementById("toggle-visibility");
    const balanceElement = document.getElementById("balance");
    const eyeIcon = document.getElementById("eye-icon");

    // COMIENZA LA LOGICA DE VALIDACIÓN DEL TOKEN
    const token = localStorage.getItem("JWT");

    if (!token) {
        window.location.href = "/PreLogin";
        return;
    }

    fetch("/api/user/data", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .then(async response => {
            if (!response.ok) {
                localStorage.removeItem("JWT");
                window.location.href = "/PreLogin";
                return;
            }

            const data = await response.json();
            // Guardar la data en localStorage y la llamamos a necesidad.
            localStorage.setItem("userData", JSON.stringify(data));
        })
        .catch(err => {
            console.error("Error al validar token:", err);
            localStorage.removeItem("JWT");
            window.location.href = "/PreLogin";
        });

    // FINALIZA LA LOGICA DE VALIDACIÓN DEL TOKEN

    const userData = JSON.parse(localStorage.getItem("userData"));

    if(userData){
        const nombre = userData.name;
        const welcomeSpan = document.querySelector(".welcome span");
        if(welcomeSpan){
            welcomeSpan.textContent = nombre;
        }
    }

    let balanceVisible = true;

    toggleModeBtn?.addEventListener("click", () => {
        body.classList.toggle("dark-mode");
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
