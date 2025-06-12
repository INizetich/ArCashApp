package com.EDJ.ArCash.Controller.api;

import com.EDJ.ArCash.Models.Account;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.AccountRepository;
import com.EDJ.ArCash.Security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json")
@Tag(name = "Datos de usuario", description = "Operaciones para obtener datos del usuario autenticado")
public class UserDataController {

    @Autowired
    private AccountRepository accountRepository;

    @Operation(
            summary = "Obtener datos del usuario autenticado",
            description = "Devuelve información personal y de la cuenta asociada al usuario autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cuenta no encontrada para el usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"error\": \"Cuenta no encontrada para el usuario\"}"
                            )
                    )
            )
    })
    @GetMapping("/data")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Optional<Account> optionalAccount =  accountRepository.findByUser_Id(user.getId());
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("error", "Cuenta no encontrada para el usuario"));
        }
        Account account = optionalAccount.get();
        return ResponseEntity.ok(new UserDTO(
                user.getName(),
                user.getLastName(),
                user.getDni(),
                user.getEmail(),
                user.getAlias(),
                account.getAccountNickname(),
                account.getIdAccount(),
                account.getAccountCvu(),
                account.getBalance()
        ));
    }

    @Schema(name = "UserDTO", description = "Datos del usuario y su cuenta asociada")
    public record UserDTO(
            String name,
            String lastName,
            String dni,
            String email,
            String username,
            String alias,
            Long idAccount,
            String cvu,
            double balance
    ) {}
}