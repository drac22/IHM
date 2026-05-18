package com.ihm.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihm.project.dto.UsuarioCreateRequestDto;
import com.ihm.project.dto.UsuarioResponseDto;
import com.ihm.project.dto.UsuarioUpdateRequestDto;
import com.ihm.project.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Controlador para la gestión completa de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioServ;

    @GetMapping()
    ResponseEntity<List<UsuarioResponseDto>> findAllUsuarios() {
        return ResponseEntity.ok(usuarioServ.findAll());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario en la base de datos utilizando su identificador único y devuelve sus datos detallados.")
    @GetMapping("/{id}")
    ResponseEntity<Optional<UsuarioResponseDto>> findUsuarioById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioServ.findById(id));
    }

    @PostMapping()
    ResponseEntity<UsuarioResponseDto> createUsuario(@RequestBody UsuarioCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioServ.save(requestDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUsuarioById(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<UsuarioResponseDto> updatedUsuario(@PathVariable Long id,
            @RequestBody UsuarioUpdateRequestDto requestDto) {
        return ResponseEntity.ok(usuarioServ.update(id, requestDto));
    }

}
