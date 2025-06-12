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
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({refreshToken})
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
        const balanceFloat = parseFloat(data.balance);

        const balance = new Intl.NumberFormat('es-ES', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(balanceFloat);
        const userData = JSON.parse(localStorage.getItem("userData"));

        if (userData) {
            const welcomeSpan = document.querySelector(".welcome span");
            if (welcomeSpan) {
                welcomeSpan.textContent = nombre;
            }
        }
        balanceElement.textContent = "$"+balance;
        toggleVisibilityBtn?.addEventListener("click", () => {
            if (balanceVisible) {
                balanceElement.textContent = "$******";
                eyeIcon.classList.replace("fa-eye", "fa-eye-slash");
            } else {
                balanceElement.textContent = "$" + balance; // obtener dinámicamente si querés
                eyeIcon.classList.replace("fa-eye-slash", "fa-eye");
            }
            balanceVisible = !balanceVisible;
        });
    }

    async function loadTransactions() {
        const lista = document.querySelector('.movements-list');
        const token = localStorage.getItem('JWT');
        const userData = JSON.parse(localStorage.getItem('userData'));
        const userID = userData.idAccount;

        try {
            const response = await fetch(`/api/transactions/${userID}/getTransactions`, {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });
            const data = await response.json();
            lista.innerHTML = "";

            data.forEach(mov => {
                const li = document.createElement("li");
                const esSalida = mov.idOrigin === userID;
                const esFallida = mov.state === "FAILED";
                let tipo = esFallida ? "FAILED" : (esSalida ? "negativo" : "positivo");
                let signo = esFallida ? "" : (esSalida ? "-" : "+");

                const montoFormateado = esFallida
                    ? `$${Math.abs(mov.amount).toLocaleString("es-AR")}`
                    : `${signo}$${Math.abs(mov.amount).toLocaleString("es-AR")}`;

                const usuarioRelacionado = esSalida ? mov.destinationUsername : mov.originUsername;
                const fechaObj = new Date(mov.date);
                const fechaFormateada = fechaObj.toLocaleDateString("es-AR", {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric'
                });

                li.innerHTML = `
                <span class="fecha">${fechaFormateada}</span>
                <span class="descripcion">
                    transferencia con ${usuarioRelacionado}
                    ${esFallida ? '<span class="estado-fallido"> (Fallida)</span>' : ''}
                </span>
                <span class="monto ${tipo}">${montoFormateado}</span>
            `;

                li.addEventListener("click", () => {
                    document.getElementById("modalOperacion").textContent = mov.idOperation;
                    document.getElementById("modalOrigen").textContent =
                        `${mov.originUsername} (${mov.originAlias})`;
                    document.getElementById("modalDestino").textContent =
                        `${mov.destinationUsername} (${mov.destinationAlias})`;
                    document.getElementById("modalMonto").textContent = "$" + mov.amount.toLocaleString("es-AR");
                    document.getElementById("modalEstado").textContent = mov.state;

                    const modalEstado = document.getElementById("modalEstado");
                    modalEstado.classList.remove("estado-completed", "estado-failed");
                    modalEstado.classList.add(mov.state === "FAILED" ? "estado-failed" : "estado-completed");

                    const fechaModal = fechaObj.toLocaleString("es-AR", {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    });
                    document.getElementById("modalFecha").textContent = fechaModal;
                    document.getElementById("modal3").classList.remove("hidden");
                });

                lista.appendChild(li);
            });
        } catch (err) {
            console.error("Error al cargar los movimientos:", err);
            lista.innerHTML = "<li>Error al cargar los movimientos</li>";
        }
    }

    async function init() {
        try {
            await loadUserData();
            await loadTransactions();
        } finally {
            document.getElementById("loader").style.display = "none";
        }
    }

    init();


    let balanceVisible = true;

    toggleModeBtn?.addEventListener("click", () => {
        body.classList.toggle("dark-mode");
    });


});