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

    window.logout = function() {
        const token = localStorage.getItem('JWT');
        if (!token) {
            window.location.href = '/PreLogin';
            return;
        }

        fetch('/api/auth/logout', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
            .then(() => {
                localStorage.clear();
                showToast('Sesión cerrada con éxito', 'success');
                setTimeout(() => {
                    window.location.href = '/home';
                }, 1800);
            })
            .catch(error => {
                console.error('Error durante el logout:', error);
                localStorage.clear();
                window.location.href = '/PreLogin';
            });
    };

    document.querySelectorAll('.close-button').forEach(closeBtn => {
        const modalId = closeBtn.getAttribute('data-modal-id');
        closeBtn.addEventListener('click', () => closeModal(modalId));
    });

    document.querySelectorAll('.modal-content').forEach(modal => {
        modal.addEventListener('click', function (e) {
            if (e.target === modal) {
                closeModal(modal.id);
            }
        });
    });


});