package com.dos.salasDeCoworking.services;


import com.dos.salasDeCoworking.model.entity.Usuario;
import com.dos.salasDeCoworking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return findEntity(id);
    }

    @Transactional
    public Usuario save(Usuario request) {
        return usuarioRepository.save(request);
    }

    @Transactional
    public Usuario update(Long id, Usuario request) {
        Usuario usuario = findEntity(id);
        usuario.setEmail(request.getEmail());
        usuario.setEsPremium(request.getEsPremium());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        usuarioRepository.deleteById(id);
    }

    public Usuario findEntity(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("usuario no encontrado con id: " + id));
    }
}
