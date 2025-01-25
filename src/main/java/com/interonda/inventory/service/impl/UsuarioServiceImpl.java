package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.UsuarioDTO;
import com.interonda.inventory.entity.Rol;
import com.interonda.inventory.entity.Usuario;
import com.interonda.inventory.entity.Venta;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.UsuarioMapper;
import com.interonda.inventory.repository.RolRepository;
import com.interonda.inventory.repository.UsuarioRepository;
import com.interonda.inventory.service.UsuarioService;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolRepository rolRepository;
    private final Validator validator;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, RolRepository rolRepository, Validator validator) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.rolRepository = rolRepository;
        this.validator = validator;
    }

    @Override
    public UsuarioDTO convertToDto(Usuario usuario) {
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        return usuarioMapper.toEntity(usuarioDTO);
    }

    @Override
    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        ValidatorUtils.validateEntity(usuarioDTO, validator);
        try {
            logger.info("Creando nuevo Usuario");
            Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

            // Asignar el rol al usuario
            if (usuarioDTO.getRolId() != null) {
                Rol rol = rolRepository.findById(usuarioDTO.getRolId()).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id: " + usuarioDTO.getRolId()));
                usuario.setRol(rol);
            }

            Usuario savedUsuario = usuarioRepository.save(usuario);
            logger.info("Usuario creado exitosamente con id: {}", savedUsuario.getId());
            return usuarioMapper.toDto(savedUsuario);
        } catch (Exception e) {
            logger.error("Error guardando Usuario", e);
            throw new DataAccessException("Error guardando Usuario", e);
        }
    }

    @Override
    @Transactional
    public UsuarioDTO updateUsuario(UsuarioDTO usuarioDTO) {
        ValidatorUtils.validateEntity(usuarioDTO, validator);
        try {
            logger.info("Actualizando Usuario con id: {}", usuarioDTO.getId());
            Usuario usuario = usuarioRepository.findById(usuarioDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + usuarioDTO.getId()));

            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setContrasenia(usuarioDTO.getContrasenia());
            usuario.setContacto(usuarioDTO.getContacto());

            if (usuarioDTO.getRolId() != null) {
                Rol rol = rolRepository.findById(usuarioDTO.getRolId()).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id: " + usuarioDTO.getRolId()));
                usuario.setRol(rol);
            } else {
                usuario.setRol(null);
            }

            Usuario updatedUsuario = usuarioRepository.save(usuario);
            logger.info("Usuario actualizado exitosamente con id: {}", updatedUsuario.getId());
            return usuarioMapper.toDto(updatedUsuario);
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
    public boolean deleteUsuario(Long id) {
        try {
            logger.info("Eliminando Usuario con id: {}", id);
            if (!usuarioRepository.existsById(id)) {
                throw new ResourceNotFoundException("Usuario no encontrado con el id: " + id);
            }
            usuarioRepository.deleteById(id);
            logger.info("Usuario eliminado exitosamente con id: {}", id);
            return true;
        } catch (ResourceNotFoundException e) {
            logger.warn("Usuario no encontrado: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error eliminando Usuario", e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + id));
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
        if (usuario.getRol() != null) {
            usuarioDTO.setRolId(usuario.getRol().getId());
            usuarioDTO.setRolNombre(usuario.getRol().getNombre());
        }
        return usuarioDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> getAllUsuarios(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Usuarios con paginación");
            Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
            Page<Usuario> usuarios = usuarioRepository.findAll(sortedByIdDesc);
            return usuarios.map(usuario -> {
                UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
                if (usuario.getRol() != null) {
                    usuarioDTO.setRolId(usuario.getRol().getId());
                    usuarioDTO.setRolNombre(usuario.getRol().getNombre());
                } else {
                    usuarioDTO.setRolId(null);
                    usuarioDTO.setRolNombre(null);
                }
                return usuarioDTO;
            });
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Usuarios con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Usuarios con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsuarios() {
        try {
            long total = usuarioRepository.count();
            logger.info("Total de proveedores: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error contando todas los Proveedores", e);
            throw new DataAccessException("Error contando todos los Proveedores", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> searchUsuariosByName(String nombre, Pageable pageable) {
        try {
            logger.info("Buscando Usuarios por nombre: {}", nombre);
            Page<Usuario> usuarios = usuarioRepository.findByNombreContainingIgnoreCase(nombre, pageable);
            return usuarios.map(usuarioMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Usuarios por nombre", e);
            throw new DataAccessException("Error buscando Usuarios por nombre", e);
        }
    }

    @Override
    public Page<Map<String, Object>> getAllUsuariosAsMap(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(usuario -> Map.of("id", usuario.getId(), "nombre", usuario.getNombre(), "contraseña", usuario.getContrasenia(), "contacto", usuario.getContacto(), "rol", usuario.getRol().getNombre()
        ));
    }
}
