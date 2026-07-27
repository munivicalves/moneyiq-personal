package com.sentinelprime.backend.service;

import com.sentinelprime.backend.dto.request.LoginRequest;
import com.sentinelprime.backend.dto.request.RegisterRequest;
import com.sentinelprime.backend.dto.response.AuthResponse;
import com.sentinelprime.backend.exception.BusinessException;
import com.sentinelprime.backend.model.Usuario;
import com.sentinelprime.backend.repository.UsuarioRepository;
import com.sentinelprime.backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService – TDD")
class AuthServiceTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @Mock AuthenticationManager authenticationManager;

    @InjectMocks AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setNome("João Silva");
        registerRequest.setEmail("joao@example.com");
        registerRequest.setSenha("senha123");
    }

    @Test
    @DisplayName("Deve registrar usuário com sucesso e retornar token")
    void register_success() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashSenha");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            return u;
        });
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getEmail()).isEqualTo("joao@example.com");
        assertThat(response.getNome()).isEqualTo("João Silva");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando e-mail já cadastrado")
    void register_emailJaCadastrado() {
        when(usuarioRepository.existsByEmail("joao@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("E-mail já cadastrado");
    }

    @Test
    @DisplayName("Deve fazer login com sucesso")
    void login_success() {
        LoginRequest req = new LoginRequest();
        req.setEmail("joao@example.com");
        req.setSenha("senha123");

        Usuario usuario = Usuario.builder()
                .email("joao@example.com")
                .nome("João Silva")
                .senhaHash("hashSenha")
                .build();

        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthResponse response = authService.login(req);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
