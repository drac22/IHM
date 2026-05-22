package com.ihm.project.dto.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}