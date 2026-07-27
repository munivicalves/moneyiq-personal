package com.moneyiqpersonal.backend.service;

import com.moneyiqpersonal.backend.dto.request.LoginRequest;
import com.moneyiqpersonal.backend.dto.request.RegisterRequest;
import com.moneyiqpersonal.backend.dto.response.AuthResponse;
import com.moneyiqpersonal.backend.exception.BusinessException;
import com.moneyiqpersonal.backend.model.Usuario;
import com.moneyiqpersonal.backend.repository.UsuarioRepository;
import com.moneyiqpersonal.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            throw new BusinessException("E-mail já cadastrado.");
        }
        Usuario usuario = Usuario.builder()
                .nome(req.getNome())
                .email(req.getEmail())
                .senhaHash(passwordEncoder.encode(req.getSenha()))
                .build();
        usuarioRepository.save(usuario);

        UserDetails ud = toUserDetails(usuario);
        return AuthResponse.builder()
                .token(jwtService.generateToken(ud))
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .build();
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getSenha()));
        Usuario usuario = usuarioRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        UserDetails ud = toUserDetails(usuario);
        return AuthResponse.builder()
                .token(jwtService.generateToken(ud))
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .build();
    }

    private UserDetails toUserDetails(Usuario u) {
        return User.withUsername(u.getEmail())
                .password(u.getSenhaHash())
                .roles("USER")
                .build();
    }
}
