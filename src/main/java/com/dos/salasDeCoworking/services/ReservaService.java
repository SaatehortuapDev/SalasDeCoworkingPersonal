package com.dos.salasDeCoworking.service;

import com.example.demo_basic.model.entity.Usuario;
import com.example.demo_basic.model.entity.Sala;
import com.example.demo_basic.model.entity.reserva;
import com.example.demo_basic.model.enums.EstadoSala;
import com.example.demo_basic.model.enums.Estadoreserva;
import com.example.demo_basic.model.enums.CapacidadSala;
import com.example.demo_basic.repository.reservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
        
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private SalaService salaService;

    @Autowired
    private AdoptanteService adoptanteService;

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Reserva findById(Long id) {
        return findEntity(id);
    }

    public List<Reserva> findByEstado(EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

    public List<Reserva> findByAdoptante(Long adoptanteId) {
        return reservaRepository.findByAdoptanteId(adoptanteId);
    }

    @Transactional
    public Reserva crearreserva(Reserva request) {
        Sala sala = salaService.findEntity(request.getSala().getId());
        Adoptante adoptante = adoptanteService.findEntity(request.getAdoptante().getId());

        validarsalaDisponible(sala);
        validarMayorEdad(adoptante);
        validarMaximoReservaesActivas(adoptante);
        validarPatioSiEsGrande(sala, adoptante);

        request.setsala(sala);
        request.setAdoptante(adoptante);
        request.setEstado(EstadoReserva.PENDIENTE);

        sala.setEstado(Estadosala.EN_PROCESO);
        salaService.save(sala);

        return reservaRepository.save(request);
    }

    @Transactional
    public Reserva cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        Reserva reserva = findEntity(id);
        sala sala = salaService.findEntity(reserva.getsala().getId());

        reserva.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoReserva.APROBADA) {
            sala.setEstado(Estadosala.ADOPTADO);
            salaService.save(sala);
        }

        if (nuevoEstado == EstadoReserva.RECHAZADA) {
            sala.setEstado(Estadosala.DISPONIBLE);
            salaService.save(sala);
        }

        return reservaRepository.save(reserva);
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        reservaRepository.deleteById(id);
    }

    private void validarsalaDisponible(sala sala) {
        if (sala.getEstado() != Estadosala.DISPONIBLE) {
            throw new IllegalArgumentException("La sala debe estar DISPONIBLE para crear la reserva.");
        }
    }

    private void validarMayorEdad(Adoptante adoptante) {
        if (adoptante.getEdad() == null || adoptante.getEdad() <= 18) {
            throw new IllegalArgumentException("El adoptante debe ser mayor de 18 años.");
        }
    }

    private void validarMaximoReservasActivas(Adoptante adoptante) {
        long activas = reservaRepository.countByAdoptanteIdAndEstado(adoptante.getId(), EstadoReserva.PENDIENTE);
        if (activas >= 2) {
            throw new IllegalArgumentException("El adoptante ya tiene 2 reservaes activas.");
        }
    }

    private void validarPatioSiEsGrande(sala sala, Adoptante adoptante) {
        if (sala.getTamano() == Tamanosala.GRANDE && !Boolean.TRUE.equals(adoptante.getTienePatio())) {
            throw new IllegalArgumentException("Para una sala GRANDE el adoptante debe tener patio.");
        }
    }

    private reserva findEntity(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("reserva no encontrada con id: " + id));
    }
}}
