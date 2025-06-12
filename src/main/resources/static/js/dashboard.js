document.addEventListener("DOMContentLoaded", () => {
    const body = document.body;
    const toggleModeBtn = document.getElementById("toggle-mode");
    const toggleVisibilityBtn = document.getElementById("toggle-visibility");
    const balanceElement = document.getElementById("balance");
    const eyeIcon = document.getElementById("eye-icon");

    const token = localStorage.getItem("JWT");
    const refreshToken = localStorage.getItem("refreshToken");

    if (!token) {
        window.location.href = "/PreLogin";
        return;
    }


    ///FUNCION QUE TRAE LOS DATOS DEL USUARIO AUTENTICADO (LA LLAMAMOS CUANDO REFRESCAMOS EL ACCESS TOKEN Y EN LA FUNCION LOADUSERDATE())
    function fetchUserData(tokenToUse) {
        return fetch("/api/user/data", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + tokenToUse
            }
        });
    }

    async function tryRefreshToken() {
        if (!refreshToken) return false;
        const response = await fetch("/api/auth/refresh", {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ refreshToken })
        });
        if (response.ok) {
            const data = await response.json();
            localStorage.setItem("JWT", data.accessToken);
            return data.accessToken;
        }
        return false;
    }

    async function loadUserData() {
        let currentToken = token;
        let response = await fetchUserData(currentToken);

        if (!response.ok) {
            // Si el token expiró o fue revocado, intenta refrescarlo
            const newToken = await tryRefreshToken();
            if (newToken) {
                response = await fetchUserData(newToken);
                if (!response.ok) {
                    localStorage.removeItem("JWT");
                    window.location.href = "/PreLogin";
                    return;
                }
            } else {
                localStorage.removeItem("JWT");
                window.location.href = "/PreLogin";
                return;
            }
        }

        const data = await response.json();
        localStorage.setItem("userData", JSON.stringify(data));
        const nombre = data.name;
        const userData = JSON.parse(localStorage.getItem("userData"));

        if (userData) {
            const welcomeSpan = document.querySelector(".welcome span");
            if (welcomeSpan) {
                welcomeSpan.textContent = nombre;
            }
        }
    }

    loadUserData();

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