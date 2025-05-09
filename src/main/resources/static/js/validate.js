// Obtener el token desde la URL
const params = new URLSearchParams(window.location.search);
const token = params.get("token");

if (token) {
    fetch(`https://arcash.ddns.net/validate?token=${token}`)
        .then(response => response.json())
} else {
    document.getElementById("status").innerText = "Token no proporcionado en la URL.";
}
