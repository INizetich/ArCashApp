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
    public Object handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = status != null ? (int) status : 500;

        if (isApiRequest(request)) {
            return ResponseEntity.status(statusCode)
                    .body(new LoginResponse(false, "Ocurrió un error: " + statusCode, null));
        }

        // Renderiza la vista Thymeleaf "error.html"
        return "error-404"; // <-- Esto busca templates/error.html
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contentType = request.getHeader("Content-Type");
        String acceptHeader = request.getHeader("Accept");

        return uri.startsWith("/api/")
                || (contentType != null && contentType.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"));
    }
}
