document.addEventListener("DOMContentLoaded", () => {
    window.openModal = function (modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'flex';
            void modal.offsetWidth; // Forzar reflow
            modal.classList.add('open');
        }
    };

    window.closeModal = function (modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.remove('open');
            modal.addEventListener('transitionend', function handler(e) {
                if (e.propertyName === 'opacity') {
                    modal.style.display = 'none';
                    modal.removeEventListener('transitionend', handler);
                }
            });
        }
    };

    document.querySelectorAll('.close-button').forEach(closeBtn => {
        const modalId = closeBtn.getAttribute('data-modal-id');
        closeBtn.addEventListener('click', () => closeModal(modalId));
    });

    document.querySelectorAll('.modal-content').forEach(modal => {
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                closeModal(modal.id);
            }
        });
    });
});