package br.com.municipio.vacinas.vacinas_api.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.municipio.vacinas.vacinas_api.model.PasswordRecoveryCode;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;
import br.com.municipio.vacinas.vacinas_api.repository.PasswordRecoveryCodeRepository;
import br.com.municipio.vacinas.vacinas_api.repository.UsuarioRepository;

@Service
public class PasswordRecoveryService {

    @Autowired
    private PasswordRecoveryCodeRepository recoveryCodeRepository;

    @Autowired
    private UsuarioRepository userRepository; 

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injetado do Spring Security para criptografar a nova senha

    public void generateAndSendCode(String email) {
        // 1. Verifica se o usuário realmente existe no banco para não enviar e-mail à toa
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // 2. Limpa qualquer código pendente que esse e-mail já tenha solicitado antes
        recoveryCodeRepository.deleteByEmail(email);

        // 3. Gera um código aleatório de 6 dígitos
        String code = String.format("%06d", new Random().nextInt(999999));

        // 4. Salva o registro no MongoDB com validade de 15 min
        PasswordRecoveryCode recoveryCode = new PasswordRecoveryCode(email, code);
        recoveryCodeRepository.save(recoveryCode);

        // 5. Envia o e-mail assincronamente ou de forma simples
        sendEmail(email, code);
    }

    public void resetPassword(String email, String code, String newPassword) {
        // 1. Busca se a combinação de e-mail e código bate e ainda está dentro do prazo (se sumiu por TTL, retorna vazio)
        PasswordRecoveryCode validCode = recoveryCodeRepository.findByEmailAndCode(email, code)
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado"));

        // 2. Busca o usuário para atualizar a senha
        Usuario user = (Usuario) userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 3. Criptografa a nova senha com BCrypt e atualiza o usuário
        user.setSenha(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 4. Apaga o código do banco, pois já foi utilizado
        recoveryCodeRepository.delete(validCode);
    }

    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de Recuperação de Senha");
        message.setText("Olá! Seu código para redefinição de senha é: " + code + "\nEste código expira em 15 minutos.");
        mailSender.send(message);
    }
}

