package com.EDJ.ArCash.Controller.api;

import com.EDJ.ArCash.DTO.AuthDTO.AccountRequest;
import com.EDJ.ArCash.DTO.AuthDTO.AccountResponse;
import com.EDJ.ArCash.DTO.AuthDTO.AliasRequest;
import com.EDJ.ArCash.DTO.AuthDTO.AliasResponse;
import com.EDJ.ArCash.Models.Account;
import com.EDJ.ArCash.Security.JwtUtils;
import com.EDJ.ArCash.Service.AccountService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/accounts", produces = "application/json")
@Tag(name = "Cuentas", description = "Operaciones sobre cuentas del usuario")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(
            summary = "Actualizar balance de la cuenta",
            description = "Permite ingresar dinero a la cuenta del usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Balance actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Monto negativo o inválido",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cuenta no encontrada",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "498",
                    description = "Usuario inválido o no autorizado",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))
            )
    })
    @PutMapping("/{id}/balance")
    public ResponseEntity<AccountResponse> updateBalance(
            @PathVariable Long id,
            @RequestBody AccountRequest accountRequest,
            HttpServletRequest request) {

        if (accountRequest.getBalance() < 0) {
            return ResponseEntity.badRequest()
                    .body(new AccountResponse(false, "El monto a ingresar no puede ser negativo.", accountRequest.getBalance()));
        }

        Object responseEntity = jwtUtils.validateAccessToken(request).getBody();

        if(responseEntity == null){
            return ResponseEntity.status(498).body(new AccountResponse(false ,"Usuario invalido",0));
        }

        Optional<Account> optionalAccount = accountService.findAccountByID(id);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(404).body(new AccountResponse(false, "La cuenta no existe.",0));
        } else {
            Account account = optionalAccount.get();

            if (account.getUser().getId().equals(responseEntity)) {
                boolean success = accountService.updateBalance(accountRequest.getBalance(), id);
                if (!success) {
                    return ResponseEntity.status(404)
                            .body(new AccountResponse(false, "No se pudo actualizar el balance. Verifique el ID ingresado.",0));
                }

                return ResponseEntity.ok(new AccountResponse(true, "Ingreso de dinero realizado correctamente.", account.getBalance()));
            } else {
                return ResponseEntity.status(498).body(new AccountResponse(false, "El usuario no es propietario legitimo de la cuenta", 0));
            }
        }
    }

    @Operation(
            summary = "Obtener información de la cuenta",
            description = "Devuelve el balance, alias y CVU de la cuenta si el usuario es propietario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos de la cuenta obtenidos correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"balance\": 1000.0, \"alias\": \"mi.alias.cuenta\", \"cvu\": \"0001234567890123456789\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token no proporcionado o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"error\": \"Token no proporcionado o inválido\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario no es propietario de la cuenta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"error\": \"El usuario no es propietario de la cuenta\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}/showBalance")
    public ResponseEntity<?> getAccount(@PathVariable Long id, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Token no proporcionado o inválido"));
        }

        String token = authHeader.substring(7);
        Claims claims = JwtUtils.getClaimJWT(token);
        String userIdStr = claims.get("userID", String.class);
        Long userId = userIdStr != null ? Long.parseLong(userIdStr) : null;

        Optional<Account> optionalAccount = accountService.findAccountByID(id);
        if (optionalAccount.isPresent() && optionalAccount.get().getUser().getId().equals(userId)) {
            Account account = optionalAccount.get();

            Map<String, Object> response = new HashMap<>();
            response.put("balance", account.getBalance());
            response.put("alias", account.getAccountNickname());
            response.put("cvu", account.getAccountCvu());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(403)
                .body(Map.of("error", "El usuario no es propietario de la cuenta"));
    }

    @Operation(
            summary = "Cambiar alias de la cuenta",
            description = "Permite al usuario cambiar el alias de su cuenta."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Alias cambiado correctamente",
                    content = @Content(schema = @Schema(implementation = AliasResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Alias inválido",
                    content = @Content(schema = @Schema(implementation = AliasResponse.class))
            ),
            @ApiResponse(
                    responseCode = "498",
                    description = "Usuario inválido o no autorizado",
                    content = @Content(schema = @Schema(implementation = AliasResponse.class))
            )
    })
    @PutMapping("/{id}/changeAlias")
    public ResponseEntity<AliasResponse> changeAlias(
            @PathVariable Long id,
            @RequestBody AliasRequest aliasRequest,
            HttpServletRequest request){
        Object responseEntity = jwtUtils.validateAccessToken(request).getBody();

        if(responseEntity == null){
            return ResponseEntity.status(498).body(new AliasResponse(false, "Usuario invalido."));
        }

        return accountService.changeAlias(aliasRequest.getNewAlias(), id, responseEntity);
    }
}