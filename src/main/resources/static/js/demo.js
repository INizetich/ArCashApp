document.addEventListener('DOMContentLoaded', () => {
    const themeSwitcher = document.getElementById('theme-switcher');
    const html = document.documentElement;

    // Verificar preferencias
    const savedTheme = localStorage.getItem('nv-theme');
    const systemDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

    if (savedTheme === 'dark' || (!savedTheme && systemDark)) {
        html.setAttribute('data-theme', 'dark');
    }

    // Alternar tema
    themeSwitcher.addEventListener('click', () => {
        const isDark = html.getAttribute('data-theme') === 'dark';

        if (isDark) {
            html.removeAttribute('data-theme');
            localStorage.setItem('nv-theme', 'light');
        } else {
            html.setAttribute('data-theme', 'dark');
            localStorage.setItem('nv-theme', 'dark');
        }
    });
});