package com.roosevelt.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roosevelt.backend.model.Evento;
import com.roosevelt.backend.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Eventos", description = "API para gestión de Eventos")
@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "/*")
public class EventoController {
     @Autowired
    private EventoService eventoService;

    // ***************************************************************************
    // CONSULTAS
    // ***************************************************************************
    // http://localhost:8080/roosevelt/api/eventos
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener todos los eventos",
            description = "Retorna una lista con todos los eventos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Eventos obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("")
    public ResponseEntity<List<Evento>> showEventos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.findAll());
    }

    // http://localhost:8080/roosevelt/api/eventos/2
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener evento por ID",
            description = "Retorna un evento especifico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/{id}")
    public ResponseEntity<Evento> detailsEvento(@PathVariable Integer id) {
        Evento evento = eventoService.findById(id);

        if (evento == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found
        }  return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(evento);  
    }
    // http://localhost:8080/roosevelt/api/eventos/count
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener el número de eventos existentes",
            description = "Retorna la cantidad de eventos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de eventos obtenidos con éxito", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countEventos() {

        ResponseEntity<Map<String, Object>> response = null;

        Map<String, Object> map = new HashMap<>();
        map.put("Eventos", eventoService.count());

        response = ResponseEntity
                .status(HttpStatus.OK)
                .body(map);

        return response;
    }

    // ***************************************************************************
    // ACTUALIZACIONES
    // ***************************************************************************
    // ****************************************************************************
    // INSERT (POST)    
    // http://localhost:8080/roosevelt/api/eventos
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Crear un nuevo evento",
            description = "Registra un nuevo evento en el sistema con las datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evento creado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    // ***************************************************************************
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createEvento(
            @Valid @RequestBody Evento evento) {

        ResponseEntity<Map<String, Object>> response;

        if (evento == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            if (evento.getTitulo() == null || evento.getTitulo().trim().isEmpty() ||
            evento.getDescripcion() == null || evento.getDescripcion().trim().isEmpty() ||
            evento.getTipoEvento() == null || evento.getFechaEvento() == null ||
            evento.getRuta() == null) {

                Map<String, Object> map = new HashMap<>();
                map.put("error", "las campos 'titulo', 'descripcion', 'tipoEvento', 'fechaEvento' y 'ruta' son obligatorios");

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else {
                System.out.println(evento);
                Evento eventoPost = eventoService.save(evento);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Evento creado con éxito");
                map.put("insertEvento", eventoPost);

                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(map);
            }
        }
        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT)
    // http://localhost:8080/roosevelt/api/eventos
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Actualizar un evento existente",
            description = "Reemplaza completamente las datos de un evento identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Evento no encontrada", content = @Content())
    })
    // ***************************************************************************    
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> updateEvento(
             @RequestBody Evento eventoUpdate) {

        ResponseEntity<Map<String, Object>> response;
        System.out.println("DEBUG: Recibido evento con titulo: " + eventoUpdate.getTitulo());

        if (eventoUpdate == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            Integer id = eventoUpdate.getId();
            Evento existingEvento = eventoService.findById(id);

            if (existingEvento == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Evento no encontrada");
                map.put("id", id);

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {

                // Actualizar campos si están presentes
               if (eventoUpdate.getTitulo() != null) existingEvento.setTitulo(eventoUpdate.getTitulo());
        if (eventoUpdate.getDescripcion() != null) existingEvento.setDescripcion(eventoUpdate.getDescripcion());
        if (eventoUpdate.getTipoEvento() != null) existingEvento.setTipoEvento(eventoUpdate.getTipoEvento());
        if (eventoUpdate.getFechaEvento() != null) existingEvento.setFechaEvento(eventoUpdate.getFechaEvento());
        if (eventoUpdate.getRuta() != null) existingEvento.setRuta(eventoUpdate.getRuta());            

                Evento eventoPut = eventoService.save(existingEvento);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Evento actualizado con éxito");
                map.put("updatedEvento", eventoPut);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }

        return response;
    }


    // ****************************************************************************
    // DELETE
    // http://localhost:8080/roosevelt/api/eventos/16
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Eliminar Evento por ID",
            description = "Elimina un evento  especifica del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento eliminado con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Evento no encontrada", content = @Content())
    })
    // ***************************************************************************    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEvento(@PathVariable Integer id) {

        ResponseEntity<Map<String, Object>> response;

        Evento existingEvento = eventoService.findById(id);
        if (existingEvento == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Evento no encontrado");
            map.put("id", id);

            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {

            eventoService.deleteById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Evento eliminado con éxito");
           // map.put("deletedEvento", existingEvento);
           

            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }
    
}
