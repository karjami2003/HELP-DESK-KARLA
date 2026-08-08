package com.utm.backend_help.controller;

import com.utm.backend_help.model.Ticket;
import com.utm.backend_help.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    // GET /api/tickets: Listar todos los incidentes
    @GetMapping
    public List<Ticket> listarTodos() {
        return ticketRepository.findAll();
    }

    // GET /api/tickets/{id}: Buscar un ticket específico
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> buscarPorId(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return new ResponseEntity<>(ticket.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST /api/tickets: Registrar un nuevo incidente
    @PostMapping
    public ResponseEntity<Ticket> crearTicket(@RequestBody Ticket ticket) {
        Ticket nuevoTicket = ticketRepository.save(ticket);
        return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
    }

    // PUT /api/tickets/{id}: Actualizar el estado o detalles de un ticket
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @RequestBody Ticket ticketDetalles) {
        Optional<Ticket> ticketExistente = ticketRepository.findById(id);

        if (ticketExistente.isPresent()) {
            Ticket ticket = ticketExistente.get();

            ticket.setTitulo(ticketDetalles.getTitulo());
            ticket.setDescripcion(ticketDetalles.getDescripcion());
            ticket.setCategoria(ticketDetalles.getCategoria());
            ticket.setPrioridad(ticketDetalles.getPrioridad());
            ticket.setEstado(ticketDetalles.getEstado());

            Ticket ticketActualizado = ticketRepository.save(ticket);
            return new ResponseEntity<>(ticketActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE /api/tickets/{id}: Eliminar un registro
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> eliminarTicket(@PathVariable Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}