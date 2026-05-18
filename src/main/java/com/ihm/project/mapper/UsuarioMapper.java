package com.ihm.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.ihm.project.dto.UsuarioCreateRequestDto;
import com.ihm.project.dto.UsuarioResponseDto;
import com.ihm.project.dto.UsuarioUpdateRequestDto;
import com.ihm.project.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponseDto toDto(Usuario usuario);

    Usuario toEntity(UsuarioCreateRequestDto dto);

    void updateToEntity(UsuarioUpdateRequestDto dto, @MappingTarget Usuario entity);

}
