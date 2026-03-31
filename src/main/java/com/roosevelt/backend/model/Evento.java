package com.roosevelt.backend.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

//LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Data
// @ToString(exclude = "ruta") // Excluir del toString para evitar recursividad
// @EqualsAndHashCode(exclude = "ruta") // Excluir de equals y hashCode para
// evitar recursividad
@ToString(exclude = { "ruta", "usuarios" })
@EqualsAndHashCode(exclude = { "ruta", "usuarios" })
// SWAGGER
@Schema(description = "Modelo de Eventos", name = "Evento")

// JPA
@Entity
@Table(name = "eventos")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único del Evento", example = "0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @JsonProperty("id")
    @Column(name = "id_evento", nullable = false, unique = true)
    private Integer id;

    @Schema(description = "El titulo del evento", example = "Picnic en la montana")
    @NotBlank(message = "El título es obligatorio")
    @Column(name = "titulo", nullable = false, unique = false)
    @JsonProperty("titulo")
    private String titulo;

    // @Enumerated(EnumType.STRING)
    // @Schema(description = "El tipo de eventoo", example = "marcha nordica")
    // @NotNull(message = "El tipo de evento es obligatorio")
    // @Column(name = "tipo_evento", nullable = false)
    // @JsonProperty("tipoEvento")
    // private TipoEvento tipoEvento;
    @NotBlank(message = "El tipo de evento es obligatorio")
    @Column(name = "tipo_evento", nullable = false)
    @JsonProperty("tipoEvento")

    private String tipoEvento;

    @Schema(description = "descripcion del evento", example = "empesamos a las 12.00 en el centro de la cuidad")
    @NotBlank(message = "descripcion del evento")
    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false, unique = false)
    @JsonProperty("descripcion")
    private String descripcion;

    @Schema(description = "fecha del evento", example = "11.08.2026")
    @Column(name = "fecha_evento")
    @NotNull
    @JsonProperty("fechaEvento")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", referencedColumnName = "id")
    @JsonIgnoreProperties("eventos")
    private Ruta ruta;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "eventos_usuarios", joinColumns = @JoinColumn(name = "id_evento"), inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    @JsonIgnoreProperties("eventos")
    private Set<Usuario> usuarios = new HashSet<>();
}
