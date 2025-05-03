document.addEventListener('DOMContentLoaded', () => {
    // ==== FORMULARIO ====
    const formulario = document.getElementById('formulario');
    const respuesta = document.getElementById('respuesta');
    const MAX_LENGTH = 50;

    const validarTexto = (texto) => /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,}$/.test(texto);
    const validarEmail = (email) => /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email);

    formulario.addEventListener('submit', async (e) => {
        e.preventDefault();
        mostrarMensaje('Procesando...', 'blue');
        
        const formData = new FormData(formulario);
        const campos = {
            nombre: formData.get('nombre').trim(),
            apellido: formData.get('apellido').trim(),
            dni: formData.get('dni').trim(),
            email: formData.get('email').trim(),
            alias: formData.get('alias').trim()
        };

        // Validaciones
        if (Object.values(campos).some(campo => !campo)) {
            mostrarMensaje('Todos los campos son obligatorios.', 'red');
            return;
        }

        if (!validarTexto(campos.nombre) || !validarTexto(campos.apellido)) {
            mostrarMensaje('Nombre y apellido solo deben contener letras.', 'red');
            return;
        }

        if (!/^\d{7,10}$/.test(campos.dni)) {
            mostrarMensaje('El DNI debe tener entre 7 y 10 dígitos numéricos.', 'red');
            return;
        }

        if (!validarEmail(campos.email)) {
            mostrarMensaje('Ingrese un email válido.', 'red');
            return;
        }

        if (Object.values(campos).some(campo => campo.length > MAX_LENGTH)) {
            mostrarMensaje(`Ningún campo puede exceder ${MAX_LENGTH} caracteres.`, 'red');
            return;
        }

        try {
            const response = await fetch('/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-Token': document.querySelector('meta[name="csrf-token"]')?.content
                },
                body: JSON.stringify({
                    name: campos.nombre,
                    lastName: campos.apellido,
                    dni: campos.dni,
                    email: campos.email,
                    alias: campos.alias
                })
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Error en el servidor');
            }

            mostrarMensaje('Usuario registrado correctamente', 'green');
            formulario.reset();
        } catch (error) {
            mostrarMensaje(
                error.message === 'Failed to fetch' 
                    ? 'Error de conexión. Por favor, intente más tarde.' 
                    : `Error: ${error.message}`, 
                'red'
            );
        }
    });

    function mostrarMensaje(mensaje, color) {
        respuesta.textContent = mensaje;
        respuesta.style.color = color;
    }

    // ==== CAMBIO DE TEMA ====
    const inicializarTema = () => {
        const themeSwitcher = document.getElementById('theme-switcher');
        const html = document.documentElement;
        
        const savedTheme = localStorage.getItem('nv-theme');
        const systemDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

        if (savedTheme === 'dark' || (!savedTheme && systemDark)) {
            html.setAttribute('data-theme', 'dark');
        } else {
            html.removeAttribute('data-theme');
        }

        if (themeSwitcher) {
            themeSwitcher.addEventListener('click', () => {
                const isDark = html.getAttribute('data-theme') === 'dark';
                html.setAttribute('data-theme', isDark ? '' : 'dark');
                localStorage.setItem('nv-theme', isDark ? 'light' : 'dark');
            });
        }
    };

    inicializarTema();
});