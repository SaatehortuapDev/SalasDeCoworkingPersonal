package com.dos.salasDeCoworking.services;

import com.dos.salasDeCoworking.model.entity.Reserva;
import com.dos.salasDeCoworking.model.entity.Sala;
import com.dos.salasDeCoworking.model.entity.Usuario;
import com.dos.salasDeCoworking.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private SalaService salaService;

    @Autowired
    private UsuarioService usuarioService;

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @Transactional
    public Reserva crearReserva(Reserva request) {
        // Cargar entidades desde la base de datos para asegurar datos reales
        Sala sala = salaService.findEntity(request.getSala().getId());
        Usuario usuario = usuarioService.findEntity(request.getUsuario().getId());

        // 5. Impedir reservas para fechas pasadas
        validarFecha(request.getFecha());

        // 2. Validar duración (Max 4h si no es Premium)
        validarDuracion(request, usuario);

        // 1. Verificar disponibilidad de horario (Sin SQL manual)
        validarDisponibilidad(request, sala);

        // 3 y 4. Calcular total con descuento automático
        double total = calcularPrecioFinal(request, sala, usuario);

        // Seteo de valores finales antes de guardar
        request.setSala(sala);
        request.setUsuario(usuario);
        request.setTotalPagar(total);

        return reservaRepository.save(request);
    }

    // --- Métodos de Validación Separados (Clean Code) ---

    private void validarFecha(LocalDate fecha) {
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se permiten reservas en fechas pasadas.");
        }
    }

    private void validarDuracion(Reserva reserva, Usuario usuario) {
        double horas = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin()).toMinutes() / 60.0;
        if (horas <= 0) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la de inicio.");
        }
        
        if (!Boolean.TRUE.equals(usuario.getEsPremium()) && horas > 4) {
            throw new IllegalArgumentException("Usuarios no Premium no pueden reservar más de 4 horas.");
        }
    }

    private void validarDisponibilidad(Reserva req, Sala sala) {
        // Llamamos al método largo que definimos en el Repository sin SQL
        boolean ocupado = reservaRepository.existsBySalaIdAndFechaAndHoraInicioBeforeAndHoraFinAfter(
            sala.getId(),
            req.getFecha(),
            req.getHoraFin(), // finSolicitado
            req.getHoraInicio() // inicioSolicitado
        );

        if (ocupado) {
            throw new IllegalArgumentException("La sala ya está ocupada en ese horario.");
        }
    }

    private double calcularPrecioFinal(Reserva reserva, Sala sala, Usuario usuario) {
        double horas = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin()).toMinutes() / 60.0;
        double total = horas * sala.getPrecioHora();

        // Aplicar 15% de descuento si es Premium
        if (Boolean.TRUE.equals(usuario.getEsPremium())) {
            total = total * 0.85; 
        }
        
        return total;
    }

    public Reserva findEntity(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reserva no encontrada con id: " + id));
    }
}
