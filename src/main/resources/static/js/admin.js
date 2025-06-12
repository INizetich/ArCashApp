document.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("JWT");
    const role = localStorage.getItem("role");
    if (!token) {
        window.location.href = "/PreLogin";
        return;
    }
    if (role !== "ADMIN") {
        window.location.href = "/dashboard";
        return;
    }
    try {
        const resp = await fetch("/api/admin/check-access", {
            headers: {"Authorization": "Bearer " + token}
        });
        if (!resp.ok) {
            window.location.href = "/dashboard";
            return;
        }
    } catch {
        window.location.href = "/dashboard";
        return;
    }
    const verUsuariosBtn = document.getElementById("ver-usuarios");
    const mostrarFormAdminBtn = document.getElementById("mostrar-form-admin");
    const formAdminDiv = document.getElementById("form-admin");
    const cancelarFormAdminBtn = document.getElementById("cancelar-form-admin");
    const usuariosLista = document.getElementById("usuarios-lista");

    // Mostrar formulario y ocultar lista
    mostrarFormAdminBtn.addEventListener("click", () => {
        formAdminDiv.style.display = "block";
        usuariosLista.style.display = "none";
        mostrarFormAdminBtn.disabled = true;
    });
    // Cancelar formulario y mostrar lista
    cancelarFormAdminBtn.addEventListener("click", () => {
        formAdminDiv.style.display = "none";
        usuariosLista.style.display = "block";
        mostrarFormAdminBtn.disabled = false;
    });

    // Renderiza la lista de usuarios
    function renderUsuarios(usuarios) {
        if (!usuarios || usuarios.length === 0) {
            usuariosLista.innerHTML = "<p>No hay usuarios autenticados.</p>";
            return;
        }
        let html = `<ul class="usuarios-lista">`;
        usuarios.forEach(u => {
            html += `
    <li class="usuario-item">
        <div class="usuario-datos">
            <strong>${u.name} ${u.lastName}</strong>
            <span class="usuario-dni">DNI: ${u.dni}</span>
            <span class="usuario-email">${u.email}</span>
            <span class="usuario-username">@${u.username}</span>
        </div>
        ${
                u.active
                    ? `<button class="inhabilitar-btn" data-id="${u.id}">Inhabilitar</button>`
                    : `<button class="habilitar-btn" data-id="${u.id}" style="background:#27ae60;color:#fff;border:none;border-radius:6px;padding:0.5rem 1rem;font-size:0.95em;cursor:pointer;">Habilitar</button>`
            }
    </li>
`;
        });
        html += "</ul>";
        usuariosLista.innerHTML = html;
    }

    // Carga usuarios desde el backend y guarda en localStorage
    async function cargarUsuarios() {
        usuariosLista.innerHTML = "Cargando...";
        try {
            const response = await fetch("/api/admin/users", {
                headers: {"Authorization": "Bearer " + token}
            });
            if (!response.ok) throw new Error("No autorizado");
            const usuarios = await response.json();
            localStorage.setItem("usuariosAdmin", JSON.stringify(usuarios));
            renderUsuarios(usuarios);
        } catch (err) {
            usuariosLista.innerHTML = "<p>Error al cargar usuarios.</p>";
            console.error(err);
        }
    }

    // Al hacer clic en "Ver usuarios autenticados"
    verUsuariosBtn.addEventListener("click", () => {
        formAdminDiv.style.display = "none";
        usuariosLista.style.display = "block";
        mostrarFormAdminBtn.disabled = false;
        const usuariosGuardados = localStorage.getItem("usuariosAdmin");
        if (usuariosGuardados) {
            renderUsuarios(JSON.parse(usuariosGuardados));
        } else {
            cargarUsuarios();
        }
    });

    // Habilitar/Inhabilitar usuario sin recargar la lista ni doble confirm
    usuariosLista.addEventListener("click", async (e) => {
        const id = e.target.getAttribute("data-id");
        if (!id) return;
        let usuarios = JSON.parse(localStorage.getItem("usuariosAdmin") || "[]");
        const usuario = usuarios.find(u => u.id == id);
        if (!usuario) return;

        if (e.target.classList.contains("inhabilitar-btn")) {
            if (!confirm("¿Seguro que quieres inhabilitar este usuario?")) return;
            try {
                const response = await fetch(`/api/admin/users/${id}/disable`, {
                    method: "PUT",
                    headers: {"Authorization": "Bearer " + token}
                });
                if (response.ok) {
                    usuario.active = false;
                    localStorage.setItem("usuariosAdmin", JSON.stringify(usuarios));
                    showToast("Usuario inhabilitado", "success"); // <-- Primero el toast
                    renderUsuarios(usuarios);
                } else {
                    showToast("Error al inhabilitar usuario", "error");
                }
            } catch {
                showToast("Error de red", "error");
            }
            return;
        }
        if (e.target.classList.contains("habilitar-btn")) {
            if (!confirm("¿Seguro que quieres habilitar este usuario?")) return;
            try {
                const response = await fetch(`/api/admin/users/${id}/enable`, {
                    method: "PUT",
                    headers: {"Authorization": "Bearer " + token}
                });
                if (response.ok) {
                    usuario.active = true;
                    localStorage.setItem("usuariosAdmin", JSON.stringify(usuarios));
                    showToast("Usuario habilitado", "success"); // <-- Primero el toast
                    renderUsuarios(usuarios);
                } else {
                    showToast("Error al habilitar usuario", "error");
                }
            } catch {
                showToast("Error de red", "error");
            }
            return;
        }
    });

    // --- Validación y envío del formulario de admin ---
    const adminForm = document.getElementById('crear-admin-form');
    const adminRespuesta = document.getElementById('admin-respuesta');
    const adminPassword = document.getElementById('admin-password');
    const adminConfirmPassword = document.getElementById('admin-confirmPassword');
    const adminPasswordHelp = document.getElementById('admin-passwordHelp');
    const MAX_LENGTH = 50;

    const validarTexto = texto => /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,}$/.test(texto);
    const validarEmail = email => /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email);

    // Validación en tiempo real de contraseñas
    adminConfirmPassword.addEventListener('input', () => {
        const pass = adminPassword.value;
        const confirm = adminConfirmPassword.value;
        if (pass.length >= 6 && confirm.length >= 6 && pass !== confirm) {
            adminPasswordHelp.classList.add('visible');
            adminPasswordHelp.style.display = 'block';
            adminConfirmPassword.classList.add('input-error');
        } else {
            adminPasswordHelp.classList.remove('visible');
            adminPasswordHelp.style.display = 'none';
            adminConfirmPassword.classList.remove('input-error');
        }
    });

    // Validación al perder foco
    adminConfirmPassword.addEventListener('blur', () => {
        const pass = adminPassword.value;
        const confirm = adminConfirmPassword.value;
        if (pass.length >= 6 && confirm.length >= 6 && pass !== confirm) {
            adminPasswordHelp.classList.add('visible');
            adminPasswordHelp.style.display = 'block';
            adminConfirmPassword.classList.add('input-error');
        } else {
            adminPasswordHelp.classList.remove('visible');
            adminPasswordHelp.style.display = 'none';
            adminConfirmPassword.classList.remove('input-error');
        }
    });

    adminForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        mostrarMensajeAdmin('Procesando...', 'blue');

        const formData = new FormData(adminForm);
        const campos = {
            nombre: formData.get('nombre').trim(),
            apellido: formData.get('apellido').trim(),
            dni: formData.get('dni').trim(),
            email: formData.get('email').trim(),
            alias: formData.get('alias').trim()
        };
        const password = formData.get('password').trim();
        const confirmPassword = formData.get('confirmPassword').trim();

        if (Object.values(campos).some(campo => !campo) || !password || !confirmPassword) {
            mostrarMensajeAdmin('Todos los campos son obligatorios.', 'red');
            return;
        }
        if (!validarTexto(campos.nombre) || !validarTexto(campos.apellido)) {
            mostrarMensajeAdmin('Nombre y apellido solo deben contener letras.', 'red');
            return;
        }
        if (!/^\d{8}$/.test(campos.dni)) {
            mostrarMensajeAdmin('El DNI debe tener 8 dígitos numéricos.', 'red');
            return;
        }
        if (!validarEmail(campos.email)) {
            mostrarMensajeAdmin('Ingrese un email válido.', 'red');
            return;
        }
        if (Object.values(campos).some(campo => campo.length > MAX_LENGTH)) {
            mostrarMensajeAdmin(`Ningún campo puede exceder ${MAX_LENGTH} caracteres.`, 'red');
            return;
        }
        if (password.length < 6) {
            mostrarMensajeAdmin('La contraseña debe tener al menos 6 caracteres.', 'red');
            return;
        }
        if (password !== confirmPassword) {
            mostrarMensajeAdmin('Las contraseñas no coinciden.', 'red');
            adminPasswordHelp.classList.add('visible');
            adminPasswordHelp.style.display = 'block';
            adminConfirmPassword.classList.add('input-error');
            return;
        } else {
            adminPasswordHelp.classList.remove('visible');
            adminPasswordHelp.style.display = 'none';
            adminConfirmPassword.classList.remove('input-error');
        }

        try {
            const response = await fetch('/api/admin/users/create-admin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({
                    name: campos.nombre,
                    lastName: campos.apellido,
                    dni: campos.dni,
                    email: campos.email,
                    username: campos.alias,
                    password: password
                })
            });
            const data = await response.json().catch(() => ({}));
            if (!response.ok) {
                throw new Error(data.mensaje || 'Error en el servidor');
            }
            mostrarMensajeAdmin('Administrador creado correctamente.', 'green');
            adminForm.reset();
            adminPasswordHelp.classList.remove('visible');
            adminPasswordHelp.style.display = 'none';
            adminConfirmPassword.classList.remove('input-error');
        } catch (error) {
            mostrarMensajeAdmin(
                error.message === 'Failed to fetch'
                    ? 'Error de conexión. Por favor, intente más tarde.'
                    : `Error: ${error.message}`,
                'red'
            );
        }
    });

    function mostrarMensajeAdmin(mensaje, color) {
        adminRespuesta.textContent = mensaje;
        adminRespuesta.style.color = color;
        showToast(mensaje, color === 'green' || color === 'blue' ? 'success' : 'error');
    }

    // --- Modal de usuario ---
    const usuarioModal = document.getElementById('usuario-modal');
    const usuarioInfo = document.getElementById('usuario-info');
    const closeModalBtn = usuarioModal.querySelector('.close-modal');

    usuariosLista.addEventListener('click', (e) => {
        // Ignora clicks en los botones de habilitar/inhabilitar
        if (e.target.classList.contains('inhabilitar-btn') || e.target.classList.contains('habilitar-btn')) return;

        const usuarioItem = e.target.closest('.usuario-item');
        if (!usuarioItem) return;

        const index = Array.from(usuarioItem.parentNode.children).indexOf(usuarioItem);
        const usuarios = JSON.parse(localStorage.getItem("usuariosAdmin") || "[]");
        const usuario = usuarios[index];
        if (!usuario) return;

        usuarioInfo.innerHTML = `
        <p><strong>Nombre:</strong> ${usuario.name}</p>
        <p><strong>Apellido:</strong> ${usuario.lastName}</p>
        <p><strong>DNI:</strong> ${usuario.dni}</p>
        <p><strong>Email:</strong> ${usuario.email}</p>
        <p><strong>Username:</strong> ${usuario.username}</p>
        <p><strong>ID Cuenta:</strong> ${usuario.idAccount ?? '-'}</p>
        <p><strong>Activo:</strong> <span class="${usuario.active ? 'estado-si' : 'estado-no'}">${usuario.active ? 'Sí' : 'No'}</span></p>
        <p><strong>Habilitado:</strong> <span class="${usuario.enabled ? 'estado-si' : 'estado-no'}">${usuario.enabled ? 'Sí' : 'No'}</span></p>
    `;

        usuarioModal.classList.add('show');
    });

    // Cerrar modal al hacer click en la X
    closeModalBtn.addEventListener('click', () => {
        usuarioModal.classList.remove('show');
    });

    // Cerrar modal al hacer click fuera del contenido
    usuarioModal.addEventListener('click', (e) => {
        if (e.target === usuarioModal) {
            usuarioModal.classList.remove('show');
        }
    });

});