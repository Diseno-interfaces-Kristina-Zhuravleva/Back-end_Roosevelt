package com.roosevelt.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roosevelt.backend.model.Evento;
import com.roosevelt.backend.repository.EventoRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoService {
    
    @Autowired
    public EventoRepository eventoRepository;

    // ************************
    // CONSULTAS
    // ************************
    @Transactional(readOnly = true)
    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Evento findById(Integer id) {
        return eventoRepository.findSqlById(id);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return eventoRepository.count();
    }

    // ************************
    // ACTUALIZACIONES
    // ************************
    @Transactional
    public Evento save(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento update(Integer id, Evento eventoDetails) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

       if (eventoDetails.getTitulo() != null) {
            evento.setTitulo(eventoDetails.getTitulo());
        }
        if (eventoDetails.getTipoEvento() != null) {
            evento.setTipoEvento(eventoDetails.getTipoEvento());
        }
        if (eventoDetails.getDescripcion() != null) {
            evento.setDescripcion(eventoDetails.getDescripcion());
        }
        if (eventoDetails.getFechaEvento() != null) {
            evento.setFechaEvento(eventoDetails.getFechaEvento());
        }
        if (eventoDetails.getRuta() != null) {
            evento.setRuta(eventoDetails.getRuta());
        }
        if (eventoDetails.getUsuarios() != null) {
            evento.setUsuarios(eventoDetails.getUsuarios());
        }

        return eventoRepository.save(evento);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado");
        }
        eventoRepository.deleteById(id);
    }
}
