package com.roosevelt.backend.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "eventos")
@EqualsAndHashCode(exclude = "eventos")

// SWAGGER
@Schema(description = "Modelo de Ruta", name="Ruta")

// JPA
@Entity
@Table(name = "Rutas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Ruta implements Serializable{

    private static final long serialVersionUID = 1L;
  ;
    
    @Schema(description = "ID único de la Ruta", example = "0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true) 
    private Integer id;

    @Schema(description = "El nombre de la Ruta", example = "Cookies")
    @NotBlank(message = "El nombre de la Ruta es obligatorio")
    @Size(min=1, max=50, message = "El nombre de la Ruta no puede tener más de 50 caracteres")
    @Column(name = "nombre_ruta", nullable = false, unique = true) 
    private String nombreRuta;

    @Schema(description = "JSON de MapBox de la Ruta", example = "Ruta de Cookies")
    @NotBlank(message = "JSON de MapBox de la Ruta es obligatoria")
    @Size(min=1, max=300, message = "JSON de MapBox de la Ruta no puede tener más de 300 caracteres")
    @Column(name = "mapbox_json", nullable = false, unique = false) 
    private String mapboxJSON;

    @Schema(description = "La descripcion de la Ruta", example = "Ruta de Cookies")
    @NotBlank(message = "La descripcion de la Ruta es obligatoria")
    @Size(min=1, max=200, message = "La descripcion de la Ruta no puede tener más de 200 caracteres")
    @Column(name = "descripcion", nullable = false, unique = false) 
    private String descripcion;

    @Schema(description = "La fecha de la publicación", example = "2015-01-11")
    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha_pub", nullable = false, unique = false)
    private LocalDate fecha_pub;

    @Schema(description = "La cantidad de likes", example = "0")
    @NotNull(message = "La cantidad de likes es obligatoria")
    @Column(name = "likes_count", nullable = false, unique = false) 
    private Integer likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona", referencedColumnName = "id")
    @JsonIgnoreProperties("rutas")  
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_autor", referencedColumnName = "id")
    
    @JsonIgnoreProperties({"misRutas", "eventos", "password"}) // para contrasena  
    private Usuario usuario_autor;

   
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"eventos", "misRutas", "zona", "rutas"})
    private List<Evento> eventos = new ArrayList<>();
}
