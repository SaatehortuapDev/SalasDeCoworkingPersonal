package com.dos.salasDeCoworking.service;

import com.example.demo_basic.model.entity.Sala;
import com.example.demo_basic.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    public Sala findById(Long id) {
        return findEntity(id);
    }

    @Transactional
    public Sala save(Sala request) {
        return salaRepository.save(request);
    }

    @Transactional
    public Sala update(Long id, Sala request) {
        Sala sala = findEntity(id);
        sala.setNombre(request.getNombre());
        sala.setEspecie(request.getEspecie());
        sala.setEdad(request.getEdad());
        sala.setTamano(request.getTamano());
        sala.setEstado(request.getEstado());
        return salaRepository.save(sala);
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        salaRepository.deleteById(id);
    }

    public sala findEntity(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("sala no encontrada con id: " + id));
    }
}