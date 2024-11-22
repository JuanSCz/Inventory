package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.Usuario;
import com.interonda.Inventory.dto.UsuarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioDTO convertToDto(Usuario usuario);

    Usuario convertToEntity(UsuarioDTO usuarioDTO);

    UsuarioDTO createUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO);

    void deleteUsuario(Long id);

    UsuarioDTO getUsuario(Long id);

    Page<UsuarioDTO> getAllUsuarios(Pageable pageable);
}