package com.dos.salasDeCoworking.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Reserva extends BaseEntity {

    @Column(name = "fecha", nullable = false, length = 80)
    private LocalDate fecha;

    @Column(name = "horaInicio", nullable = false, length = 80)
    private LocalTime horaInicio;

    @Column(name = "horaFin", nullable = false, length = 80)
    private LocalTime horaFin;

    @Column(name = "totalPagar", nullable = false)
    private Double totalPagar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

}
