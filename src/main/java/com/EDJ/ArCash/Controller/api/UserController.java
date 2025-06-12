package com.EDJ.ArCash.Controller.api;

import com.EDJ.ArCash.DTO.NonAuthDTO.RegisterResponse;
import com.EDJ.ArCash.DTO.NonAuthDTO.RegistrerRequest;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario registrado correctamente",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"success\": false, \"message\": \"Datos inválidos\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"success\": false, \"message\": \"Error interno del servidor\"}"
                            )
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegistrerRequest dto) throws MessagingException, UnsupportedEncodingException {

        if (dto.getName() == null || dto.getEmail() == null || dto.getPassword() == null) {
            return ResponseEntity.badRequest().body(new RegisterResponse(false, "Datos inválidos"));
        }
        try {
            User user = new User(dto.getName(), dto.getLastName(), dto.getDni(), dto.getEmail(), dto.getAlias());
            userService.insertarUsuario(user, dto.getPassword());
            return ResponseEntity.ok(new RegisterResponse(true, "Usuario registrado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new RegisterResponse(false, "Error interno del servidor"));
        }
    }
}