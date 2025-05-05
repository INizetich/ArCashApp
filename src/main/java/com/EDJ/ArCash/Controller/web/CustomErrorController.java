package com.EDJ.ArCash.Controller.web;


import com.EDJ.ArCash.DTO.LoginResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        // Obtener el código de error
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = status != null ? (int) status : 500;  // Si no se encuentra el código de error, se asume 500

        // Si es una solicitud de API, devolver un JSON con el error
        if (isApiRequest(request)) {
            return ResponseEntity.status(statusCode)
                    .body(new LoginResponse(false, "Ocurrió un error: " + statusCode, null));
        }

        // Si no es una solicitud de API (por ejemplo, es una vista), devolver un mensaje de error genérico
        return ResponseEntity.status(statusCode)
                .body(new LoginResponse(false, "Ocurrió un error en la solicitud", null));
    }

    // Método para determinar si la solicitud es para una API
    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/");  // Verifica si la URI comienza con "/api/"
    }

}
