package com.interonda.inventory.service;

import com.interonda.inventory.dto.UsuarioDTO;
import com.interonda.inventory.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioDTO convertToDto(Usuario usuario);

    Usuario convertToEntity(UsuarioDTO usuarioDTO);

    UsuarioDTO createUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO updateUsuario(UsuarioDTO usuarioDTO);

    boolean deleteUsuario(Long id);

    UsuarioDTO getUsuario(Long id);

    Page<UsuarioDTO> getAllUsuarios(Pageable pageable);

    Page<UsuarioDTO> searchUsuariosByName(String nombre, Pageable pageable);

    long countUsuarios();
}