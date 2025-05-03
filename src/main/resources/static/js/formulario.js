document.addEventListener('DOMContentLoaded', () => {
    // ==== FORMULARIO ====
    const formulario = document.getElementById('formulario');
    const respuesta = document.getElementById('respuesta');

    formulario.addEventListener('submit', (e) => {
        e.preventDefault();

        const formData = new FormData(formulario);

        const nombre = formData.get('nombre').trim();
        const apellido = formData.get('apellido').trim();
        const dni = formData.get('dni').trim();
        const email = formData.get('email').trim();
        const alias = formData.get('alias').trim();

        if (!nombre || !apellido || !dni || !email || !alias) {
            mostrarMensaje('Todos los campos son obligatorios.', 'red');
            return;
        }

        if (!/^\d{7,10}$/.test(dni)) {
            mostrarMensaje('El DNI debe tener entre 7 y 10 dígitos numéricos.', 'red');
            return;
        }

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            mostrarMensaje('Ingrese un email válido.', 'red');
            return;
        }

        // Crear objeto JSON
        const resultado = {
            name: nombre,
            lastName: apellido,
            dni: dni,
            email: email,
            alias: alias
        };

        // Enviar al backend con fetch
        fetch('/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(resultado)
        })
            .then(response => {
                if (!response.ok) throw new Error("Error al enviar el formulario.");
                return response.text();
            })
            .then(data => {
                mostrarMensaje('Usuario registrado correctamente', 'green');
                formulario.reset(); // limpiar campos
            })
            .catch(error => {
                mostrarMensaje('Error: ' + error.message, 'red');
            });
    });

    function mostrarMensaje(mensaje, color) {
        respuesta.textContent = mensaje;
        respuesta.style.color = color;
    }

    // ==== CAMBIO DE TEMA ====
    const switcher = document.getElementById("theme-switcher");
    const currentTheme = localStorage.getItem("theme");

    if (currentTheme === "dark") {
        document.documentElement.setAttribute("data-theme", "dark");
    }

    if (switcher) {
        switcher.addEventListener("click", () => {
            const theme = document.documentElement.getAttribute("data-theme");
            if (theme === "dark") {
                document.documentElement.removeAttribute("data-theme");
                localStorage.setItem("theme", "light");
            } else {
                document.documentElement.setAttribute("data-theme", "dark");
                localStorage.setItem("theme", "dark");
            }
        });
    }
});
