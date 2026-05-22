package com.ihm.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihm.project.dto.auth.AuthResponse;
import com.ihm.project.dto.auth.RefreshTokenRequest;
import com.ihm.project.jwt.JwtService;
import com.ihm.project.model.Usuario;
import com.ihm.project.exceptions.ForbiddenException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        try {
            String email = jwtService.getUsername(requestRefreshToken);

            if (email != null) {
                Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(requestRefreshToken, usuario)) {
                    String newAccessToken = jwtService.generateToken(usuario);
                    AuthResponse authResponse = AuthResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(requestRefreshToken) 
                            .build();

                    return ResponseEntity.ok(authResponse);
                }
            }
        } catch (Exception e) {
            throw new ForbiddenException("El Refresh Token es inválido o ha expirado. Por favor, inicia sesión nuevamente.");
        }

        throw new ForbiddenException("No se pudo refrescar el token.");
    }
}
