document.addEventListener('DOMContentLoaded', () => {
    const themeSwitcher = document.getElementById('theme-switcher');
    const html = document.documentElement;

    // Verificar preferencias
    const savedTheme = localStorage.getItem('nv-theme');
    const systemDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

    if (savedTheme === 'dark' || (!savedTheme && systemDark)) {
        html.setAttribute('data-theme', 'dark');
    }
});


