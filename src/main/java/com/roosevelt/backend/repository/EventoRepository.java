package com.roosevelt.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.roosevelt.backend.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    
    @Query(value = "SELECT * FROM eventos", nativeQuery = true)
    List<Evento> findSqlAll();

    @Query(value = "SELECT * FROM eventos WHERE id_evento = :id", nativeQuery = true)
    Evento findSqlById(@Param("id") Integer id);

    @Query(value = "SELECT * FROM eventos WHERE id_ruta = :id_ruta", nativeQuery = true)
    List<Evento> findSqlByIdRuta(@Param("id_ruta") Integer id_ruta);

    @Query(value = "SELECT COUNT(*) FROM eventos", nativeQuery = true)
    Long countSql();
    
}
