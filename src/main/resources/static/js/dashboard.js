
    document.addEventListener("DOMContentLoaded", () => {
        // Mostrar el botón de admin si el rol es ADMIN
        const role = localStorage.getItem("role");
        const JWT = localStorage.getItem("JWT");
        const adminPanelLink = document.getElementById("admin-panel-link");
        if (role === "ADMIN" && adminPanelLink) {
            adminPanelLink.style.display = "block";

            // Agregar evento click con depuración
            adminPanelLink.addEventListener('click', async (e) => {
                e.preventDefault();

                // Fragmento relevante de dashboard.js
                try {
                    const response = await fetch('/api/admin/check-access', {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem("JWT")}`
                        }
                    });

                    if (response.ok) {
                        window.location.href = "/adminDashboard";
                    } else {
                        let errorMsg = "No tienes permisos para acceder al panel de administración";
                        try {
                            const error = await response.json();
                            errorMsg = error.message || errorMsg;
                        } catch {
                            // Si no es JSON, ignora y usa el mensaje por defecto
                        }
                        console.error("Access denied:", errorMsg);
                        alert(errorMsg);
                    }
                } catch (error) {
                    console.error("Error checking access:", error);
                    alert("Error al verificar permisos");
                }
            });
        }
        actualizarSaldo();
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

                const userNameEl = document.getElementById("user-name");
                const userEmailEl = document.getElementById("user-email");
                const userAliasEl = document.getElementById("user-alias");
                const userBalanceEl = document.getElementById("user-balance");
                const userNameTopbar = document.getElementById("user-name-topbar");

                if (userNameEl) userNameEl.textContent = `${data.name}`;
                if (userEmailEl) userEmailEl.textContent = data.email;
                if (userAliasEl) userAliasEl.textContent = `Alias: ${data.alias}`;
                if (userBalanceEl) userBalanceEl.textContent = data.balance.toFixed(2);
                if (userNameTopbar) userNameTopbar.textContent = data.username;
                document.querySelector(".dashboard").classList.add("loaded");
            })
            .catch(err => {
                console.error("Error al validar token:", err);
                localStorage.removeItem("JWT");
                window.location.href = "/PreLogin";
            });

        // Ingresar Dinero
        const ingresarModal = document.getElementById("ingresar-modal");
        const ingresarButton = document.querySelector(".ingresar");
        const closeIngresar = document.querySelector("#ingresar-modal .close-button");
        const ingresarForm = document.getElementById("ingresar-form");

        if (ingresarButton) {
            ingresarButton.addEventListener("click", () => {
                ingresarModal.classList.remove("hidden");
            });
        }

        if (closeIngresar) {
            closeIngresar.addEventListener("click", () => {
                ingresarModal.classList.add("hidden");
                ingresarForm.reset();
            });
        }

        window.addEventListener("click", (e) => {
            if (e.target === ingresarModal) {
                ingresarModal.classList.add("hidden");
                ingresarForm.reset();
            }
        });

        if (ingresarForm) {
            ingresarForm.addEventListener("submit", async (e) => {
                e.preventDefault();

                const monto = parseFloat(document.getElementById("ingresar-monto").value);
                if (isNaN(monto) || monto <= 0) {
                    showToast("Por favor ingrese un monto válido.", "error");
                    return;
                }

                const token = localStorage.getItem("JWT");
                const accountId = localStorage.getItem("accountId");

                try {
                    const response = await fetch(`/api/accounts/${accountId}/balance`, {
                        method: "PUT",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + token
                        },
                        body: JSON.stringify({ balance: monto })
                    });

                    const result = await response.json();

                    if (response.ok && result.success) {
                        showToast("Ingreso exitoso: " + result.message, "success");
                        ingresarForm.reset();
                        ingresarModal.classList.add("hidden");
                        actualizarSaldo();
                    } else {
                        showToast("Error: " + result.message, "error");
                    }
                } catch (err) {
                    showToast("Error: " + result.message, "error");
                    console.error(err);
                }
            });
        }

        ///ACTUALIZAR DINERO DE LA CUENTA
        function actualizarSaldo() {
            const token = localStorage.getItem("JWT");
            const accountId = localStorage.getItem("accountId");

            if (!token || !accountId) return;

            fetch(`/api/accounts/${accountId}/showBalance`, {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            })
                .then(res => {
                    if (!res.ok) throw new Error("No autorizado o cuenta no encontrada");
                    return res.json();
                })
                .then(data => {
                    const saldoEl = document.getElementById("balance");
                    if (saldoEl) saldoEl.textContent = `${data.balance.toFixed(2)}`;

                    localStorage.setItem("alias", data.alias);
                    localStorage.setItem("cvu", data.cvu);

                })
                .catch(err => {
                    console.error("Error al obtener el saldo:", err);
                });
        }

        // Transferencia
        const modal = document.getElementById("transfer-modal");
        const openButton = document.querySelector(".tranferir");
        const closeButton = document.querySelector("#transfer-modal .close-button");
        const transferForm = document.getElementById("transfer-form");

        if (openButton) {
            openButton.addEventListener("click", () => {
                modal.classList.remove("hidden");
            });
        }

        if (closeButton) {
            closeButton.addEventListener("click", () => {
                modal.classList.add("hidden");
                if (transferForm) transferForm.reset();
            });
        }

        window.addEventListener("click", (e) => {
            if (e.target === modal) {
                modal.classList.add("hidden");
                if (transferForm) transferForm.reset();
            }
        });

        if (transferForm) {
            transferForm.addEventListener("submit", (e) => {
                e.preventDefault();

                const montoInput = document.getElementById("monto");
                const valor = parseFloat(montoInput.value);

                if (isNaN(valor) || valor <= 0) {
                    showToast('Por favor ingrese un monto válido', 'error');
                    montoInput.focus();
                } else {
                    showToast('Transferencia realizada con éxito', 'success');
                    transferForm.reset();
                    modal.classList.add("hidden");
                }
            });
        }



        // Alias Modal
        const aliasModal = document.getElementById("alias-modal");
        const aliasButton = document.querySelector(".Alias-CVU");
        const closeAlias = document.querySelector(".close-alias");
        const copyAliasButton = document.getElementById("copy-alias");
        const copyCvuButton = document.getElementById("copy-cvu");

        if (aliasButton) {
            aliasButton.addEventListener("click", () => {
                const alias = localStorage.getItem("alias") || "No disponible";
                const cvu = localStorage.getItem("cvu") || "No disponible";

                document.getElementById("alias-value").textContent = alias;
                document.getElementById("cvu-value").textContent = cvu;
                aliasModal.classList.remove("hidden");
            });
        }

        // Función para copiar texto
        async function copyText(text, button) {
            try {
                await navigator.clipboard.writeText(text);

                // Feedback visual
                button.classList.add('success');
                showToast('Copiado al portapapeles', 'success');
                setTimeout(() => {
                    button.classList.remove('success');
                }, 2000);
            } catch (err) {
                showToast('No se pudo copiar el texto', 'error');
            }
        }

        // Copiar Alias
        if (copyAliasButton) {
            copyAliasButton.addEventListener("click", () => {
                const aliasText = document.getElementById("alias-value").textContent;
                copyText(aliasText, copyAliasButton);
            });
        }

        // Copiar CVU
        if (copyCvuButton) {
            copyCvuButton.addEventListener("click", () => {
                const cvuText = document.getElementById("cvu-value").textContent;
                copyText(cvuText, copyCvuButton);
            });
        }

        if (closeAlias) {
            closeAlias.addEventListener("click", () => {
                aliasModal.classList.add("hidden");
            });
        }

        window.addEventListener("click", (e) => {
            if (e.target === aliasModal) {
                aliasModal.classList.add("hidden");
            }
        });

        // Sidebar toggle
        const menuButton = document.getElementById("menu-toggle");
        const sidebar = document.getElementById("sidebar");

        if (menuButton && sidebar) {
            menuButton.addEventListener("click", () => {
                sidebar.classList.toggle("hidden");
            });
        }

        // Calculadora Impuestos
        const abrirCalculadora = document.getElementById('abrir-calculadora');
        const taxModal = document.getElementById('tax-modal');
        const closeTax = document.querySelector(".close-tax");

        if (abrirCalculadora) {
            abrirCalculadora.addEventListener("click", (e) => {
                e.preventDefault();
                taxModal.classList.remove("hidden");
            });
        }

        let selectedCurrency = "ARS";

        const currencyButtons = document.querySelectorAll(".currency-button");
        const taxForm = document.getElementById("tax-form");
        const selectedCurrencyLabel = document.getElementById("selected-currency-label");
        const calcularBtn = document.getElementById("calcular-tax");
        const taxResult = document.getElementById("tax-result");
        const taxMonto = document.getElementById("tax-monto");

        currencyButtons.forEach(button => {
            button.addEventListener("click", () => {
                selectedCurrency = button.getAttribute("data-currency");
                selectedCurrencyLabel.textContent = selectedCurrency;
                taxForm.classList.remove("hidden");
                taxResult.textContent = "";
                taxMonto.value = "";
            });
        });

        if (closeTax) {
            closeTax.addEventListener("click", () => {
                taxModal.classList.add("hidden");
                taxForm.classList.add("hidden");
            });
        }

        calcularBtn.addEventListener("click", async () => {
            const monto = parseFloat(taxMonto.value);

            if (isNaN(monto) || monto <= 0) {
                showToast('Por favor ingrese un monto válido', 'error');
                return;
            }

            let url = "";
            if (selectedCurrency === "ARS") {
                url = `/api/impuestos/calculateARS?montoARS=${monto}`;
            } else if (selectedCurrency === "USD") {
                url = `/api/impuestos/calculateUSD?montoUSD=${monto}`;
            } else {
                showToast("Moneda no válida.", "error");
                return;
            }

            try {
                const response = await fetch(url);
                if (!response.ok) {
                    const errorText = await response.text();
                    showToast("Error al calcular impuestos: " + errorText, "error");
                    return;
                }

                const data = await response.json();
                if (data && typeof data.totalFinal === "number") {
                    let detalle = `<p><strong class="label">Monto sin impuestos:</strong> <span class="value">$${data.montoOriginal.toFixed(2)} ARS</span></p>`;

                    if (selectedCurrency === "ARS") {
                        detalle += `<p><strong class="label">IVA 21%:</strong> <span class="value">$${data.iva.toFixed(2)} ARS</span></p>`;
                    } else if (selectedCurrency === "USD") {
                        detalle += `<p><strong class="label">IVA 21%:</strong> <span class="value">$${data.iva.toFixed(2)} ARS</span></p>`;
                        detalle += `<p><strong class="label">Percepción Ganancias 30%:</strong> <span class="value">$${data.percepcionGanancias.toFixed(2)} ARS</span></p>`;
                        detalle += `<p><strong class="label">Cotización dólar oficial:</strong> <span class="value">$${data.precioDolar.toFixed(2)} ARS</span></p>`;
                    }

                    detalle += `<p><strong class="label">Total con impuestos:</strong> <span class="value strong">$${data.totalFinal.toFixed(2)} ARS</span></p>`;

                    taxResult.innerHTML = detalle;
                } else {
                    taxResult.textContent = "No se pudo obtener el resultado del cálculo.";
                }

            } catch (error) {
                showToast("Error al conectar con el servidor.", "error");
                console.error(error);
            }
        });

        // Perfil dropdown
        const profilePhoto = document.querySelector('.profile-photo');
        const profileMenu = document.getElementById('profileMenu');

        if (profilePhoto && profileMenu) {
            profilePhoto.addEventListener("click", (e) => {
                e.stopPropagation();
                profileMenu.style.display = (profileMenu.style.display === 'flex') ? 'none' : 'flex';
            });

            document.addEventListener("click", (e) => {
                if (!profilePhoto.contains(e.target) && !profileMenu.contains(e.target)) {
                    profileMenu.style.display = 'none';
                }
            });
        }

        // Variables para almacenar datos de las cuentas
        let cuentaOrigenId = localStorage.getItem('accountId'); // ID de la cuenta del usuario actual
        let cuentaDestinoId = null;
        let cuentaDestinoData = null;

        // Elementos del DOM
        const searchAccountStep = document.getElementById('search-account-step');
        const confirmAccountStep = document.getElementById('confirm-account-step');
        const amountStep = document.getElementById('amount-step');
        const buscarCuentaBtn = document.getElementById('buscar-cuenta');
        const confirmarCuentaBtn = document.getElementById('confirmar-cuenta');
        const cancelarBusquedaBtn = document.getElementById('cancelar-busqueda');
        const confirmarTransferenciaBtn = document.getElementById('confirmar-transferencia');
        const volverBusquedaBtn = document.getElementById('volver-busqueda');
        const accountDetails = document.getElementById('account-details');

        // Función para buscar cuenta
        buscarCuentaBtn.addEventListener('click', async () => {
            const input = document.getElementById('destinatario').value;
            if (!input) {
                showToast('Por favor ingrese un Alias o CVU', 'error');
                return;
            }

            try {
                const response = await fetch(`/api/transactions/search/${input}`, {
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('JWT')
                    }
                });

                if (!response.ok) {
                    throw new Error('Cuenta no encontrada');
                }

                const data = await response.json();
                cuentaDestinoId = data.idaccount;

                if (cuentaDestinoId === cuentaOrigenId) {
                    showToast('No puedes transferir dinero a tu misma cuenta', 'error');
                    return;
                }

                cuentaDestinoData = data;

                accountDetails.innerHTML = `
                <p><strong>Alias:</strong> ${data.alias}</p>
                <p><strong>CVU:</strong> ${data.cvu}</p>
                <p><strong>Titular:</strong> ${data.user.nombre} ${data.user.apellido}</p>
                <p><strong>DNI:</strong> ${data.user.dni}</p>
            `;

                searchAccountStep.classList.add('hidden');
                confirmAccountStep.classList.remove('hidden');

            } catch (error) {
                showToast('Error: ' + error.message, 'error');
            }
        });

        // Confirmar cuenta y mostrar paso de monto
        confirmarCuentaBtn.addEventListener('click', () => {
            confirmAccountStep.classList.add('hidden');
            amountStep.classList.remove('hidden');
        });

        // Volver a búsqueda
        cancelarBusquedaBtn.addEventListener('click', () => {
            confirmAccountStep.classList.add('hidden');
            searchAccountStep.classList.remove('hidden');
            document.getElementById('destinatario').value = '';
        });

        volverBusquedaBtn.addEventListener('click', () => {
            amountStep.classList.add('hidden');
            searchAccountStep.classList.remove('hidden');
            document.getElementById('destinatario').value = '';
            document.getElementById('monto').value = '';
        });

        // Realizar transferencia
        confirmarTransferenciaBtn.addEventListener('click', async () => {
            const monto = parseFloat(document.getElementById('monto').value);

            if (isNaN(monto) || monto <= 0) {
                showToast('Por favor ingrese un monto válido', 'error');
                return;
            }

            if (!cuentaOrigenId || !cuentaDestinoId) {
                showToast('Error: Información de cuentas incompleta', 'error');
                return;
            }

            try {
                const response = await fetch(`/api/transactions/${cuentaOrigenId}/transfer/${cuentaDestinoId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('JWT')
                    },
                    body: JSON.stringify({ balance: monto })
                });

                const result = await response.json();

                if (response.ok) {
                    showToast('Transferencia realizada con éxito', 'success');
                    document.getElementById('destinatario').value = '';
                    document.getElementById('monto').value = '';
                    searchAccountStep.classList.remove('hidden');
                    amountStep.classList.add('hidden');
                    confirmAccountStep.classList.add('hidden');
                    modal.classList.add('hidden');

                    actualizarSaldo();

                    if (window.cargarMovimientos) {
                        cargarMovimientos();
                    }
                } else {
                    showToast('Error: ' + result.message, 'error');
                }

            } catch (error) {
                showToast('Error al realizar la transferencia', 'error');
                console.error(error);
            }
        });

        // Busca el enlace de cerrar sesión
        const logoutLink = document.querySelector('a[href="/logout"]');

        if (logoutLink) {
            logoutLink.addEventListener('click', async (e) => {
                e.preventDefault();

                try {
                    const token = localStorage.getItem('JWT');
                    if (!token) {
                        window.location.href = '/PreLogin';
                        return;
                    }

                    await fetch('/api/auth/logout', {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        },
                        credentials: 'include'
                    });

                localStorage.clear()
                    showToast('Sesión cerrada con éxito', 'success');
                    setTimeout(() => {
                        window.location.href = '/home';
                    }, 1800);

                } catch (error) {
                    console.error('Error durante el logout:', error);
                    localStorage.clear()
                    window.location.href = '/PreLogin';
                }
            });
        }

        // --- Abrir modal de perfil y cargar datos ---

        const profileModal = document.getElementById('profile-modal');
        const openProfileBtn = document.getElementById('open-profile-modal');
        const closeProfileBtn = profileModal.querySelector('.close-profile');
        const aliasInput = document.getElementById('profile-alias-input');
        const aliasDisplay = document.getElementById('profile-alias-display');
        const nameDisplay = document.getElementById('profile-name');
        const lastNameDisplay = document.getElementById('profile-lastName');
        const emailDisplay = document.getElementById('profile-email');
        const dniDisplay = document.getElementById('profile-dni');
        const editAliasBtn = document.getElementById('edit-alias-btn');
        const cvuDisplay = document.getElementById('profile-cvu');
        const usernameDisplay = document.getElementById('profile-username-display');
        const usernameInput = document.getElementById('profile-username-input');
        const editUsernameBtn = document.getElementById('edit-username-btn');

        openProfileBtn.addEventListener('click', async (e) => {
            e.preventDefault();
            const token = localStorage.getItem("JWT");
            if (!token) {
                window.location.href = "/PreLogin";
                return;
            }
            try {
                const response = await fetch("/api/user/data", {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token
                    }
                });
                if (!response.ok) {
                    showToast("No se pudo obtener la información del usuario", "error");
                    return;
                }
                const data = await response.json();

                // Actualiza estos campos según la nueva estructura
                nameDisplay.textContent = data.name || "";
                lastNameDisplay.textContent = data.lastName || "";
                dniDisplay.textContent = data.dni || "";
                emailDisplay.textContent = data.email || "";
                usernameDisplay.textContent = data.username || ""; // Nombre de usuario para login
                aliasDisplay.textContent = data.alias || "";      // Alias de la cuenta
                cvuDisplay.textContent = data.cvu || "";
                localStorage.setItem("accountId", data.idAccount);
                profileModal.classList.remove('hidden');
                // Oculta inputs y muestra displays por si quedaron abiertos antes
                aliasInput.classList.add('hidden');
                aliasDisplay.classList.remove('hidden');
                editAliasBtn.classList.remove('hidden');
                usernameInput.classList.add('hidden');
                usernameDisplay.classList.remove('hidden');
                editUsernameBtn.classList.remove('hidden');
            } catch (err) {
                showToast("Error al cargar datos de usuario", "error");
                console.error(err);
            }
        });

// Cerrar modal con la X
        closeProfileBtn.addEventListener('click', () => {
            profileModal.classList.add('hidden');
        });

// Cerrar modal clickeando afuera del contenido
        profileModal.addEventListener('click', (e) => {
            if (e.target === profileModal) {
                profileModal.classList.add('hidden');
            }
        });

// --- Editar alias con ícono de lápiz ---
        editAliasBtn.addEventListener('click', () => {
            aliasInput.value = aliasDisplay.textContent;
            aliasDisplay.classList.add('hidden');
            editAliasBtn.classList.add('hidden');
            aliasInput.classList.remove('hidden');
            aliasInput.focus();
        });

// Validar y actualizar alias al presionar Enter
        aliasInput.addEventListener('keydown', async (e) => {
            if (e.key === 'Enter') {
                const newAlias = aliasInput.value.trim();
                const aliasRegex = /^(?=.*[A-Za-z])(?=^[A-Za-z0-9]+(\.[A-Za-z0-9]+)+$)(?!.*\.\.)[A-Za-z0-9.]{4,25}$/;
                if (!aliasRegex.test(newAlias)) {
                    showToast('Formato de alias inválido. Debe tener entre 4 y 25 caracteres, solo letras, números y puntos, al menos un punto en el medio, no puede ser solo números ni tener "..".', 'error');
                    return;
                }
                const token = localStorage.getItem("JWT");
                const accountId = localStorage.getItem("accountId");
                try {
                    const response = await fetch(`/api/accounts/${accountId}/changeAlias`, {
                        method: "PUT",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + token
                        },
                        body: JSON.stringify({ newAlias })
                    });
                    const result = await response.json();
                    if (response.ok && result.success) {
                        aliasDisplay.textContent = newAlias;
                        showToast('Alias actualizado correctamente', 'success');
                        aliasInput.classList.add('hidden');
                        aliasDisplay.classList.remove('hidden');
                        editAliasBtn.classList.remove('hidden');
                        // Actualiza en tiempo real en otros lugares
                        const userAliasEl = document.getElementById("user-alias");
                        if (userAliasEl) userAliasEl.textContent = `Alias: ${newAlias}`;
                        const aliasValueEl = document.getElementById("alias-value");
                        if (aliasValueEl) aliasValueEl.textContent = newAlias;
                        localStorage.setItem("alias", newAlias);
                    } else {
                        showToast(result.message || 'No se pudo actualizar el alias', 'error');
                    }
                } catch (err) {
                    showToast('Error al actualizar el alias', 'error');
                }
            } else if (e.key === 'Escape') {
                aliasInput.classList.add('hidden');
                aliasDisplay.classList.remove('hidden');
                editAliasBtn.classList.remove('hidden');
            }
        });

// --- Editar username con ícono de lápiz ---
        if (editUsernameBtn && usernameDisplay && usernameInput) {
            editUsernameBtn.addEventListener('click', () => {
                usernameInput.value = usernameDisplay.textContent;
                usernameDisplay.classList.add('hidden');
                editUsernameBtn.classList.add('hidden');
                usernameInput.classList.remove('hidden');
                usernameInput.focus();
            });

            usernameInput.addEventListener('keydown', async (e) => {
                if (e.key === 'Enter') {
                    const newUsername = usernameInput.value.trim();
                    // Regex igual al backend
                    const regex = /^(?=.*[A-Za-z])[A-Za-z\d]{4,25}$/;
                    if (
                        !newUsername ||
                        !regex.test(newUsername) ||
                        /^\d+$/.test(newUsername) // solo números
                    ) {
                        showToast('Formato inválido. Solo letras y números, al menos una letra, sin caracteres especiales ni solo números.', 'error');
                        return;
                    }
                    const token = localStorage.getItem("JWT");
                    try {
                        const response = await fetch('/api/auth/changeUsername', {
                            method: "PUT",
                            headers: {
                                "Content-Type": "application/json",
                                "Authorization": "Bearer " + token
                            },
                            body: JSON.stringify({ newUsername })
                        });
                        const result = await response.json();
                        if (response.ok && result.success) {
                            usernameDisplay.textContent = newUsername;
                            const userNameTopbar = document.getElementById("user-name-topbar");
                            if (userNameTopbar) userNameTopbar.textContent = newUsername;
                            showToast('Nombre de usuario actualizado correctamente', 'success');
                            usernameInput.classList.add('hidden');
                            usernameDisplay.classList.remove('hidden');
                            editUsernameBtn.classList.remove('hidden');
                        } else {
                            showToast(result.message || 'No se pudo actualizar el nombre de usuario', 'error');
                        }
                    } catch (err) {
                        showToast('Error al actualizar el nombre de usuario', 'error');
                    }
                } else if (e.key === 'Escape') {
                    usernameInput.classList.add('hidden');
                    usernameDisplay.classList.remove('hidden');
                    editUsernameBtn.classList.remove('hidden');
                }
            });
        }

    });



