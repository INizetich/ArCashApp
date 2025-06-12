package com.EDJ.ArCash.Controller.api;

import com.EDJ.ArCash.DTO.*;
import com.EDJ.ArCash.Models.Imp.LogoutStatus;
import com.EDJ.ArCash.Security.JwtUtils;
import com.EDJ.ArCash.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth", produces = "application/json")
@Tag(name = "Usuarios Autenticados", description = "Operaciones para usuarios autenticados")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Iniciar sesión",
            description = "Verifica las credenciales del usuario y retorna tokens de acceso y refresh si son válidas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión exitoso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authService.login(loginRequest);

            if (loginResponse.isSuccess()) {
                String refreshToken = loginResponse.getRefreshToken();
                if (refreshToken != null) {
                    ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                            .httpOnly(true)
                            .path("/")
                            .maxAge(7 * 24 * 60 * 60)
                            .build();
                    response.addHeader("Set-Cookie", cookie.toString());
                }
                return ResponseEntity.ok(loginResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "Error interno del servidor", null, null, null, null));
        }
    }

    @Operation(
            summary = "Cerrar sesión",
            description = "Cierra la sesión del usuario y elimina el refresh token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sesión cerrada correctamente",
                    content = @Content(schema = @Schema(implementation = LogoutStatus.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido o no proporcionado",
                    content = @Content(schema = @Schema(implementation = LogoutStatus.class))
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response,
            @Parameter(description = "Token JWT de autenticación", required = true, example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        LogoutStatus status = authService.logout(token);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Verificar sesión",
            description = "Verifica si el token JWT enviado es válido y la sesión está activa."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sesión activa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"status\": \"ACTIVE\", \"message\": \"Sesión activa\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Sesión inactiva o token inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"status\": \"INACTIVE\", \"message\": \"No hay sesión activa\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"status\": \"ERROR\", \"message\": \"Error al verificar la sesión\"}"
                            )
                    )
            )
    })
    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(
            @Parameter(description = "Token JWT de autenticación", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                boolean isValidSession = authService.isValidSession(token);

                if (isValidSession) {
                    return ResponseEntity.ok()
                            .body(Map.of(
                                    "status", "ACTIVE",
                                    "message", "Sesión activa"
                            ));
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", "INACTIVE",
                            "message", "No hay sesión activa"
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "ERROR",
                            "message", "Error al verificar la sesión"
                    ));
        }
    }

    @Operation(
            summary = "Enviar correo de recuperación",
            description = "Envía un correo electrónico para recuperar la contraseña del usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Correo enviado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Correo enviado correctamente\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al enviar el correo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Error interno al enviar el correo\"}"
                            )
                    )
            )
    })
    @PostMapping("/send-recover-mail")
    public ResponseEntity<?> sendRecoverEmail(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            boolean enviado = authService.enviarCorreoRecuperacion(email);
            return ResponseEntity.ok(Map.of("message", "Correo enviado correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error interno al enviar el correo"));
        }
    }

    @Operation(
            summary = "Cambiar nombre de usuario",
            description = "Permite al usuario autenticado cambiar su nombre de usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre de usuario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = UsernameResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = UsernameResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token no proporcionado o inválido",
                    content = @Content(schema = @Schema(implementation = UsernameResponse.class))
            )
    })
    @PutMapping("/changeUsername")
    public ResponseEntity<?> changeUsername(
            @RequestBody UsernameRequest usernameRequest,
            HttpServletRequest request
    ) {
        String newUsername = usernameRequest.getNewUsername();
        if (newUsername == null || newUsername.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new UsernameResponse(false, "El nombre de usuario no puede estar vacio"));
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new UsernameResponse(false, "Token no proporcionado"));
        }
        String token = authHeader.substring(7);

        String userIdStr = JwtUtils.extractUserId(token);
        if (userIdStr == null) {
            return ResponseEntity.status(401).body(new UsernameResponse(false, "Token invalido"));
        }

        boolean result = authService.cambiarAliasYUsername(Long.parseLong(userIdStr), newUsername.trim());
        if (result) {
            return ResponseEntity.ok(new UsernameResponse(true, "Nombre de usuario actualizado correctamente"));
        } else {
            return ResponseEntity.badRequest().body(new UsernameResponse(false ,"No se pudo actualizar el nombre de usuario. Puede que ya exista."));
        }
    }
}