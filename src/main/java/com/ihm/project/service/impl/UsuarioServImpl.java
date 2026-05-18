package com.ihm.project.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ihm.project.dto.UsuarioCreateRequestDto;
import com.ihm.project.dto.UsuarioResponseDto;
import com.ihm.project.dto.UsuarioUpdateRequestDto;
import com.ihm.project.mapper.UsuarioMapper;
import com.ihm.project.model.Usuario;
import com.ihm.project.repo.UserRepository;
import com.ihm.project.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServImpl implements UsuarioService {

    private final UserRepository usuarioRepo;
    private final UsuarioMapper usuarioMapper;

    @Override
    public List<UsuarioResponseDto> findAll() {
        return usuarioRepo.findAll().stream().map(user -> usuarioMapper.toDto(user)).toList();
    }

    @Override
    public Optional<UsuarioResponseDto> findById(Long id) {
        return usuarioRepo.findById(id).map(user -> usuarioMapper.toDto(user));
    }

    @Override
    public UsuarioResponseDto save(UsuarioCreateRequestDto request) {
        Usuario usuarioCreated = usuarioMapper.toEntity(request);
        Usuario usuarioSave = usuarioRepo.save(usuarioCreated);
        return usuarioMapper.toDto(usuarioSave);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Usuario> usuario = usuarioRepo.findById(id);
        if (usuario.isPresent()) {
            usuarioRepo.deleteById(id);
        }
    }

    @Override
    public UsuarioResponseDto update(Long id, UsuarioUpdateRequestDto request) {
        return usuarioRepo.findById(id).map(usuarioExist -> {
            usuarioMapper.updateToEntity(request, usuarioExist);
            Usuario usuarioSaved = usuarioRepo.save(usuarioExist);
            return usuarioMapper.toDto(usuarioSaved);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
