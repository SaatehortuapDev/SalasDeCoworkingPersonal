package com.dos.salasDeCoworking.model.entity;

import com.dos.salasDeCoworking.model.enums.CapacidadSala;
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
@Table(name = "Salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Sala extends BaseEntity {

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "capacidad", nullable = false, length = 20)
    private CapacidadSala capacidad;

    @Column(name = "precioHora", nullable = false)
    private Double precioHora;

}
