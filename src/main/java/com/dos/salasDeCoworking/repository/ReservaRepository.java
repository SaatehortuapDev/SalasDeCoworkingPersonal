package com.dos.salasDeCoworking.repository;

import com.example.demo_basic.model.entity.Reserva;
import com.example.demo_basic.model.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    long countByUsuarioIdAndEstado(Long Id, EstadoReserva estado);

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByEstado(EstadoReserva estado);
}
