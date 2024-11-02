package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario save(Usuario usuario);

    void deleteById(Long id);

}
