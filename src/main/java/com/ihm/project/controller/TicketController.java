package com.ihm.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ihm.project.dto.ticket.AsignacionRequestDto;
import com.ihm.project.dto.ticket.TicketCreateRequestDto;
import com.ihm.project.dto.ticket.TicketResponseDto;
import com.ihm.project.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Controlador para la gestión completa de tickets del sistema")
public class TicketController {

    private final TicketService ticketServ;

    // PARA ADMINISTRADORES
    @Operation(summary = "Obtener todos los tickets")
    @GetMapping()
    ResponseEntity<List<TicketResponseDto>> findAllTickets() {
        return ResponseEntity.ok(ticketServ.findAll());
    }

    // PARA ADMINISTRADORES
    @Operation(summary = "Obtener ticket por ID", description = "Busca un ticket utilizando su identificador único")
    @GetMapping("/{id}")
    ResponseEntity<Optional<TicketResponseDto>> findTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketServ.findById(id));
    }

    // PARA ADMINISTRADORES
    @Operation(summary = "Obtener tickets por ID de Usuario", description = "Busca tickets utilizando el identificador único de un usuario")
    @GetMapping("/usuario/{userId}")
    ResponseEntity<List<TicketResponseDto>> findTicketsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketServ.findByIdUsuarioAsignado(userId));
    }

    // PARA EL USUARIO AUTENTICADO
    @Operation(summary = "Obtener mis tickets", description = "Mis tickets asignados")
    @GetMapping("/my-tickets")
    ResponseEntity<List<TicketResponseDto>> findMyTicket() {
        return ResponseEntity.ok(ticketServ.findMyTickets());
    }

    // PARA EL USUARIO AUTENTICADO
    @Operation(summary = "Crear Ticket")
    @PostMapping()
    ResponseEntity<TicketResponseDto> createTicket(@RequestBody @Valid TicketCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketServ.save(requestDto));
    }

    // PARA ADMINISTRADORES y USUARIOS, PERO SOLO PARA SUS PROPIOS TICKETS, FALTA IMPLEMENTAR VALIDACION
    @Operation(summary = "Eliminar Ticket por ID")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTicketById(@PathVariable Long id) {
        ticketServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PARA ADMINISTRADORES
    @Operation(summary = "Asignacion de los tickets", description = "Permite asignar un ticket a un usuario y establecer su prioridad")
    @PutMapping("/{id}/asignacion")
    public ResponseEntity<String> asignarTicket(
            @PathVariable Long id,
            @RequestBody AsignacionRequestDto requestDto) {
        ticketServ.asignacionTicket(requestDto, id);
        return ResponseEntity.ok("Ticket asignado correctamente.");
    }

    // PARA USUARIO CON ROL TECNICO
    @Operation(summary = "Culminar ticket", description = "Permite culminar un ticket")
    @PutMapping("/{id}/culminar")
    public ResponseEntity<String> culminarTicket(
            @PathVariable Long id) {
        ticketServ.culminarTicket(id);
        return ResponseEntity.ok("Ticket culminado correctamente.");
    }

    // @Operation(summary = "Modificar Ticket")
    // @PutMapping("/{id}")
    // ResponseEntity<TicketResponseDto> updatedTicket(@PathVariable Long id,
    // @RequestBody @Valid TicketUpdateRequestDto requestDto) {
    // return ResponseEntity.ok(ticketServ.update(id, requestDto));
    // }
}
