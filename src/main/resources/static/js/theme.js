document.addEventListener('DOMContentLoaded', () => {
    const html = document.documentElement;
    const switcher = document.getElementById('theme-switcher');

    // Verificar preferencias del usuario
    const savedTheme = localStorage.getItem('nv-theme');
    const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

    if (savedTheme === 'dark' || (!savedTheme && systemPrefersDark)) {
        html.setAttribute('data-theme', 'dark');
    } else {
        html.setAttribute('data-theme', 'light');
    }

    // Cambiar tema si hay botón
    if (switcher) {
        switcher.addEventListener('click', () => {
            const isDark = html.getAttribute('data-theme') === 'dark';

            if (isDark) {
                html.setAttribute('data-theme', 'light');
                localStorage.setItem('nv-theme', 'light');
            } else {
                html.setAttribute('data-theme', 'dark');
                localStorage.setItem('nv-theme', 'dark');
            }
        });
    }
});
