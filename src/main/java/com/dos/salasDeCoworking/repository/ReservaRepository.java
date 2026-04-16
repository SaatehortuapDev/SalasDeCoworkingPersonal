package com.dos.salasDeCoworking.repository;

import com.dos.salasDeCoworking.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findBySalaId(Long salaId);

    boolean existsBySalaIdAndFechaAndHoraInicioBeforeAndHoraFinAfter(
        Long salaId,
        LocalDate fecha,
        LocalTime finSolicitado,
        LocalTime inicioSolicitado
    );
}
