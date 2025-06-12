function cargarMovimientos() {
    const lista = document.querySelector(".movimientos-lista");
    const userID = Number(localStorage.getItem("accountId"));
    const modal = document.getElementById("modalTransferencia");
    const cerrarModal = document.getElementById("cerrarModal");
    const token = localStorage.getItem("JWT");

    fetch(`/api/transactions/${userID}/getTransactions`, {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .then(response => response.json())
        .then(data => {
            lista.innerHTML = "";

            data.forEach(mov => {
                const li = document.createElement("li");
                const esSalida = mov.idOrigin === userID;
                const esFallida = mov.state === "FAILED";
                let tipo = "";
                let signo = "";

                if (esFallida) {
                    tipo = "FAILED";
                    signo = "";
                } else {
                    tipo = esSalida ? "negativo" : "positivo";
                    signo = esSalida ? "-" : "+";
                }

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

                li.addEventListener("click", function() {
                    document.getElementById("modalOperacion").textContent = mov.idOperation;
                    document.getElementById("modalOrigen").textContent =
                        `${mov.originUsername} (${mov.originAlias})`;
                    document.getElementById("modalDestino").textContent =
                        `${mov.destinationUsername} (${mov.destinationAlias})`;
                    document.getElementById("modalMonto").textContent = "$" + mov.amount.toLocaleString("es-AR");
                    document.getElementById("modalEstado").textContent = mov.state;

                    // Quita clases previas
                    const modalEstado = document.getElementById("modalEstado");
                    modalEstado.classList.remove("estado-completed", "estado-failed");

                    // Aplica color según estado
                    if (mov.state === "FAILED") {
                        modalEstado.classList.add("estado-failed");
                    } else if (mov.state === "COMPLETED") {
                        modalEstado.classList.add("estado-completed");
                    } else {
                        modalEstado.style.color = "";
                    }

                    const fechaModal = fechaObj.toLocaleString("es-AR", {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    });
                    document.getElementById("modalFecha").textContent = fechaModal;
                    modal.classList.remove("hidden");
                });

                lista.appendChild(li);
            });
        })
        .catch(err => {
            console.error("Error al cargar los movimientos:", err);
            lista.innerHTML = "<li>Error al cargar los movimientos</li>";
        });

    cerrarModal.addEventListener("click", function() {
        modal.classList.add("hidden");
    });

    modal.addEventListener("click", function(event) {
        if (event.target === modal) {
            modal.classList.add("hidden");
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    cargarMovimientos();
});