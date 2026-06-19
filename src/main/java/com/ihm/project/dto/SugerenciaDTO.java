package com.ihm.project.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SugerenciaDTO {
    private Long id;
    private String tipo;
    private String mensaje;
    private String estado;
    private LocalDateTime fechaCreacion;
    private Long usuarioId; // Optional, can be null for anonymous suggestions
}
