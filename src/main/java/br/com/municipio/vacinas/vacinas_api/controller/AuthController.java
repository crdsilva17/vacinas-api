package br.com.municipio.vacinas.vacinas_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.dto.LoginRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LoginResponseDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterResponseDTO;
import br.com.municipio.vacinas.vacinas_api.service.PasswordRecoveryService;
import br.com.municipio.vacinas.vacinas_api.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService service;

    @Autowired
    private PasswordRecoveryService recoveryService;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.login(request));
    }

    // PASSO 1: Recebe o e-mail do Flutter (Forgot-Password)
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            recoveryService.generateAndSendCode(email);
            return ResponseEntity.ok().build(); // Retorna 200 OK para o Flutter avançar de tela
        } catch (Exception e) {
            System.out.println("Erro ao gerar código de recuperação: " + e.getMessage() + " para o e-mail: " + email
                    + " - StackTrace: " + e.getStackTrace());
            // Boas práticas de segurança: opcionalmente retorne 200 mesmo se não achar para
            // evitar "user enumeration"
            return ResponseEntity.badRequest().build();
        }
    }

    // PASSO 3: Recebe e-mail, código e nova senha do Flutter (Reset-Password)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        String password = request.get("password"); // Lembra que mapeamos como 'password' no Flutter

        try {
            recoveryService.resetPassword(email, code, password);
            return ResponseEntity.ok("Senha alterada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
