package com.ihm.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ihm.project.dto.ticket.TicketCreateRequestDto;
import com.ihm.project.dto.ticket.TicketResponseDto;
import com.ihm.project.model.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(source = "usuarioAsignado.nombre", target = "usuarioAsignado")
    @Mapping(source = "creadoPor.nombre", target = "creadoPor")
    @Mapping(source = "categoria.nombre", target = "categoria")
    @Mapping(source = "fechaRegistro", target = "fechaRegistro", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "fechaAsignacion", target = "fechaAsignacion", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "fechaCulminacion", target = "fechaCulminacion", dateFormat = "yyyy-MM-dd HH:mm:ss")
    TicketResponseDto toDto(Ticket ticket);

    @Mapping(source = "categoriaId", target = "categoria.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    Ticket toEntity(TicketCreateRequestDto dto);
}