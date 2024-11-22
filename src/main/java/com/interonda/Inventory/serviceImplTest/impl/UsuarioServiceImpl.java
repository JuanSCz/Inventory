package com.interonda.Inventory.serviceImplTest.impl;

import com.interonda.Inventory.dto.UsuarioDTO;
import com.interonda.Inventory.entity.Rol;
import com.interonda.Inventory.entity.Usuario;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.RolRepository;
import com.interonda.Inventory.repository.UsuarioRepository;
import com.interonda.Inventory.serviceImplTest.UsuarioService;
import com.interonda.Inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final Validator validator;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository, Validator validator) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.validator = validator;
    }

    @Override
    public UsuarioDTO convertToDto(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setContrasenia(usuario.getContrasenia());
        usuarioDTO.setImagenUsuario(usuario.getImagenUsuario());
        usuarioDTO.setContacto(usuario.getContacto());
        usuarioDTO.setRolId(usuario.getRol().getId());
        return usuarioDTO;
    }

    @Override
    public Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setContrasenia(usuarioDTO.getContrasenia());
        usuario.setImagenUsuario(usuarioDTO.getImagenUsuario());
        usuario.setContacto(usuarioDTO.getContacto());

        Optional<Rol> rol = rolRepository.findById(usuarioDTO.getRolId());
        if (rol.isPresent()) {
            usuario.setRol(rol.get());
        } else {
            throw new ResourceNotFoundException("Rol no encontrado con el id: " + usuarioDTO.getRolId());
        }

        return usuario;
    }

    @Override
    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        ValidatorUtils.validateEntity(usuarioDTO, validator);
        try {
            logger.info("Creando nuevo Usuario");
            Usuario usuario = convertToEntity(usuarioDTO);
            Usuario savedUsuario = usuarioRepository.save(usuario);
            logger.info("Usuario creado exitosamente con id: {}", savedUsuario.getId());
            return convertToDto(savedUsuario);
        } catch (Exception e) {
            logger.error("Error guardando Usuario", e);
            throw new DataAccessException("Error guardando Usuario", e);
        }
    }

    @Override
    @Transactional
    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        ValidatorUtils.validateEntity(usuarioDTO, validator);
        try {
            logger.info("Actualizando Usuario con id: {}", id);
            Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + id));
            usuario = convertToEntity(usuarioDTO);
            usuario.setId(id);
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            logger.info("Usuario actualizado exitosamente con id: {}", updatedUsuario.getId());
            return convertToDto(updatedUsuario);
        } catch (ResourceNotFoundException e) {
            logger.warn("Usuario no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Usuario", e);
            throw new DataAccessException("Error actualizando Usuario", e);
        }
    }

    @Override
    @Transactional
    public void deleteUsuario(Long id) {
        try {
            logger.info("Eliminando Usuario con id: {}", id);
            if (!usuarioRepository.existsById(id)) {
                throw new ResourceNotFoundException("Usuario no encontrado con el id: " + id);
            }
            usuarioRepository.deleteById(id);
            logger.info("Usuario eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Usuario no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Usuario", e);
            throw new DataAccessException("Error eliminando Usuario", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuario(Long id) {
        try {
            logger.info("Obteniendo Usuario con id: {}", id);
            Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + id));
            return convertToDto(usuario);
        } catch (ResourceNotFoundException e) {
            logger.warn("Usuario no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Usuario", e);
            throw new DataAccessException("Error obteniendo Usuario", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> getAllUsuarios(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Usuarios con paginación");
            Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
            return usuarios.map(this::convertToDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Usuarios con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Usuarios con paginación", e);
        }
    }
}
