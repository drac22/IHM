package com.ihm.project.dto.usuario;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioResponseDto {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String celular;
}