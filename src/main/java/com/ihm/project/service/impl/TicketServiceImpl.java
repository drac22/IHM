package com.ihm.project.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ihm.project.dto.ticket.AsignacionRequestDto;
import com.ihm.project.dto.ticket.TicketCreateRequestDto;
import com.ihm.project.dto.ticket.TicketResponseDto;
import com.ihm.project.enums.Estado;
import com.ihm.project.enums.Prioridad;
import com.ihm.project.mapper.TicketMapper;
import com.ihm.project.model.Ticket;
import com.ihm.project.model.Usuario;
import com.ihm.project.repo.TicketRepository;
import com.ihm.project.repo.UserRepository;
import com.ihm.project.service.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository usuarioRepository;
    private final TicketMapper ticketMapper;

    @Override
    public List<TicketResponseDto> findAll() {
        return ticketRepository.findAll().stream().map(ticketMapper::toDto).toList();
    }

    @Override
    public List<TicketResponseDto> findByIdUsuarioAsignado(Long userId) {
        return ticketRepository.findByUsuarioAsignadoId(userId).stream().map(ticketMapper::toDto).toList();
    }

    @Override
    public List<TicketResponseDto> findMyTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ticketRepository.findByUsuarioAsignadoId(usuario.getId()).stream().map(ticketMapper::toDto).toList();
    }

    @Override
    public TicketResponseDto save(TicketCreateRequestDto request) {
        Ticket ticketCreated = ticketMapper.toEntity(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        ticketCreated.setCreadoPor(usuario);
        Ticket ticketSave = ticketRepository.save(ticketCreated);
        return ticketMapper.toDto(ticketSave);
    }

    @Override
    public void deleteById(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("No se encontró el ticket con ID: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public Optional<TicketResponseDto> findById(Long id) {
        return ticketRepository.findById(id).map(ticketMapper::toDto);
    }

    @Override
    public void asignacionTicket(AsignacionRequestDto requestDto, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("No se encontró el ticket con ID: " + ticketId));

        Usuario usuario = usuarioRepository.findById(requestDto.getIdUsuarioAsignado())
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el usuario con ID: " + requestDto.getIdUsuarioAsignado()));

        ticket.setUsuarioAsignado(usuario);
        ticket.setEstado(Estado.EN_PROCESO);
        ticket.setFechaAsignacion(LocalDateTime.now());
        try {
            ticket.setPrioridad(Prioridad.valueOf(requestDto.getPrioridad().toUpperCase().trim()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("La prioridad '" + requestDto.getPrioridad() + "' no es válida.");
        }
        ticketRepository.save(ticket);
    }

    @Override
    public void culminarTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("No se encontró el ticket con ID: " + ticketId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario tecnicoLogueado = (Usuario) authentication.getPrincipal();

        if (ticket.getUsuarioAsignado() == null ||
                !ticket.getUsuarioAsignado().getId().equals(tecnicoLogueado.getId())) {

            throw new RuntimeException("No tienes permisos para culminar este ticket porque no te está asignado.");
        }
        ticket.setEstado(Estado.RESUELTO);
        ticket.setFechaCulminacion(LocalDateTime.now());
        ticketRepository.save(ticket);
    }
}