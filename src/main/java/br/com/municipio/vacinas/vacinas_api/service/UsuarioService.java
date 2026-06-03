package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.model.enums.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import br.com.municipio.vacinas.vacinas_api.dto.LoginRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LoginResponseDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterResponseDTO;
import br.com.municipio.vacinas.vacinas_api.dto.UserRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.UsuarioResponseDTO;
import br.com.municipio.vacinas.vacinas_api.exception.UserRegisterException;
import br.com.municipio.vacinas.vacinas_api.exception.UserUpdateException;
import br.com.municipio.vacinas.vacinas_api.mapper.UsuarioMapper;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;
import br.com.municipio.vacinas.vacinas_api.repository.UsuarioRepository;
import br.com.municipio.vacinas.vacinas_api.security.TokenConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UserRegisterException(
                    "Já existe um usuário cadastrado com este e-mail.");
        }
        request.setSenha(passwordEncoder.encode(request.getSenha()));
        request.setRole(Role.USER);
        Usuario usuario = mapper.toEntity(request);
        try {
            usuarioRepository.save(usuario);
        } catch (DuplicateKeyException exception) {
            throw new UserRegisterException(
                    "Já existe um usuário cadastrado com este e-mail.");
        }
        return mapper.toRegisterResponseDTO(usuario);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getSenha());
        Authentication authentication = authenticationManager.authenticate(authToken);
        // Gerar token JWT usando as informações do usuário autenticado

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenConfig.generateToken(usuario);

        return mapper.toLoginResponseDTO(token); // Retornar um LoginResponseDTO com o token JWT
    }

    public UsuarioResponseDTO getUserByEmail(String email) {
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado"));

        return mapper.toDTO(usuario);
    }

    public void updateUserByEmail(UserRequestDTO user) {
        Usuario userModel = (Usuario) usuarioRepository.findByEmail(user.email()).orElseThrow(
            () -> new UserUpdateException("Usuário não encontrado")
        );
        userModel.setLocalId(user.local());
        userModel.setNome(user.nome());
        userModel.setDataNscto(user.dataNscto());
        try {
            usuarioRepository.save(userModel);
        } catch (Exception exception){
            throw new UserUpdateException(exception.toString());
        }
    }

}
