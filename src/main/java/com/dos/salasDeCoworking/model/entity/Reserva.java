package com.dos.salasDeCoworking.model.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Reserva extends BaseEntity {

    @Column(name = "fecha", nullable = false, length = 80)
    private Date fecha;

    @Column(name = "horaInicio", nullable = false, length = 80)
    private String horaInicio;

    @Column(name = "horaFin", nullable = false, length = 80)
    private String horaFin;

    @Column(name = "totalAPagar", nullable = false)
    private Double totalAPagar;
}
