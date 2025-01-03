package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.entity.Cliente;
import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.entity.Venta;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.VentaMapper;
import com.interonda.inventory.repository.ClienteRepository;
import com.interonda.inventory.repository.DetalleVentaRepository;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.repository.VentaRepository;
import com.interonda.inventory.service.VentaService;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {
    private static final Logger logger = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final Validator validator;
    private final VentaMapper ventaMapper;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository, DetalleVentaRepository detalleVentaRepository, Validator validator, VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.validator = validator;
        this.ventaMapper = ventaMapper;
    }

    @Override
    public VentaDTO convertToDto(Venta venta) {
        return ventaMapper.toDto(venta);
    }

    @Override
    public Venta convertToEntity(VentaDTO ventaDTO) {
        return ventaMapper.toEntity(ventaDTO);
    }

    @Override
    @Transactional
    public VentaDTO createVenta(VentaDTO ventaDTO) {
        ValidatorUtils.validateEntity(ventaDTO, validator);
        try {
            logger.info("Creando nueva Venta");
            Venta venta = ventaMapper.toEntity(ventaDTO);

            // Asignar el cliente a la venta
            Cliente cliente = clienteRepository.findById(ventaDTO.getClienteId()).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con el id: " + ventaDTO.getClienteId()));
            venta.setCliente(cliente);

            // Asignar los detalles de venta
            venta.setDetallesVenta(ventaDTO.getDetallesVenta().stream().map(detalleDTO -> {
                DetalleVenta detalle = ventaMapper.toDetalleEntity(detalleDTO);
                detalle.setVenta(venta);
                detalle.setProducto(productoRepository.findById(detalleDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleDTO.getProductoId())));
                return detalle;
            }).collect(Collectors.toList()));

            // Establecer el total directamente desde el DTO
            venta.setTotal(ventaDTO.getTotal());
            venta.setImpuestos(ventaDTO.getImpuestos());

            Venta savedVenta = ventaRepository.save(venta);
            logger.info("Venta creada exitosamente con id: {}", savedVenta.getId());

            // Formatear el total
            ventaDTO.setTotalString(formatTotal(savedVenta.getTotal()));

            return ventaMapper.toDto(savedVenta);
        } catch (Exception e) {
            logger.error("Error guardando Venta", e);
            throw new DataAccessException("Error guardando Venta", e);
        }
    }

    @Override
    @Transactional
    public VentaDTO updateVenta(VentaDTO ventaDTO) {
        ValidatorUtils.validateEntity(ventaDTO, validator);
        try {
            logger.info("Actualizando Venta con id: {}", ventaDTO.getId());
            Venta venta = ventaRepository.findById(ventaDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el id: " + ventaDTO.getId()));

            // Actualizar los campos de la venta
            venta.setFecha(ventaDTO.getFecha());
            venta.setTotal(ventaDTO.getTotal());
            venta.setMetodoPago(ventaDTO.getMetodoPago());
            venta.setEstado(ventaDTO.getEstado());
            venta.setImpuestos(ventaDTO.getImpuestos());

            // Asignar el cliente a la venta
            Cliente cliente = clienteRepository.findById(ventaDTO.getClienteId()).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con el id: " + ventaDTO.getClienteId()));
            venta.setCliente(cliente);

            // Eliminar detalles existentes
            detalleVentaRepository.deleteAll(venta.getDetallesVenta());

            // Asignar los nuevos detalles de venta
            List<DetalleVenta> nuevosDetalles = ventaDTO.getDetallesVenta().stream().map(detalleDTO -> {
                DetalleVenta detalle = ventaMapper.toDetalleEntity(detalleDTO);
                detalle.setVenta(venta);
                detalle.setProducto(productoRepository.findById(detalleDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleDTO.getProductoId())));
                return detalle;
            }).collect(Collectors.toList());
            venta.setDetallesVenta(nuevosDetalles);

            Venta updatedVenta = ventaRepository.save(venta);
            logger.info("Venta actualizada exitosamente con id: {}", updatedVenta.getId());

            // Formatear el total
            ventaDTO.setTotalString(formatTotal(updatedVenta.getTotal()));

            return ventaMapper.toDto(updatedVenta);
        } catch (ResourceNotFoundException e) {
            logger.warn("Venta no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Venta", e);
            throw new DataAccessException("Error actualizando Venta", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteVenta(Long id) {
        try {
            logger.info("Eliminando Venta con id: {}", id);
            if (!ventaRepository.existsById(id)) {
                throw new ResourceNotFoundException("Venta no encontrada con el id: " + id);
            }
            ventaRepository.deleteById(id);
            logger.info("Venta eliminada exitosamente con id: {}", id);
            return true;
        } catch (ResourceNotFoundException e) {
            logger.warn("Venta no encontrada: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error eliminando Venta", e);
            return false;
        }
    }

    private String formatImpuestos(String impuestos) {
        switch (impuestos) {
            case "0.10":
                return "IVA: 10%";
            case "0.20":
                return "IVA: 20%";
            case "0.30":
                return "IVA: 30%";
            default:
                return "IVA: " + (new BigDecimal(impuestos).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString()) + "%";
        }
    }

    public String formatTotal(BigDecimal total) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(total);
    }

    public String formatPrecioUnitario(BigDecimal precioUnitario) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(precioUnitario);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO getVenta(Long id) {
        try {
            logger.info("Obteniendo Venta con id: {}", id);
            Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el id: " + id));
            VentaDTO ventaDTO = ventaMapper.toDto(venta);
            ventaDTO.setClienteId(venta.getCliente().getId());
            ventaDTO.setClienteNombre(venta.getCliente().getNombre());
            ventaDTO.setDetallesVenta(venta.getDetallesVenta().stream().map(detalle -> {
                DetalleVentaDTO detalleDTO = ventaMapper.toDetalleDto(detalle);
                detalleDTO.setProductoId(detalle.getProducto().getId());
                detalleDTO.setProductoNombre(detalle.getProducto().getNombre());
                detalleDTO.setPrecioUnitarioString(formatPrecioUnitario(detalle.getPrecioUnitario())); // Formatear el precio unitario
                detalleDTO.setSubtotalFormatted(formatSubtotal(detalle.getSubtotal())); // Formatear el subtotal
                return detalleDTO;
            }).collect(Collectors.toList()));
            ventaDTO.setTotalString(formatTotal(venta.getTotal())); // Formatear el total
            return ventaDTO;
        } catch (ResourceNotFoundException e) {
            logger.warn("Venta no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Venta", e);
            throw new DataAccessException("Error obteniendo Venta", e);
        }
    }

    public String formatSubtotal(BigDecimal subtotal) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(subtotal);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> getAllVentas(Pageable pageable) {
        try {
            logger.info("Obteniendo todas las Ventas con paginación");
            Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
            Page<Venta> ventas = ventaRepository.findAll(sortedByIdDesc);
            return ventas.map(venta -> {
                VentaDTO dto = ventaMapper.toDto(venta);
                dto.setClienteNombre(venta.getCliente().getNombre());
                dto.setImpuestos(formatImpuestos(venta.getImpuestos())); // Formatear impuestos
                dto.setTotal(venta.getTotal()); // Establecer el total como BigDecimal
                dto.setTotalString(formatTotal(venta.getTotal())); // Formatear total como String para visualización
                return dto;
            });
        } catch (Exception e) {
            logger.error("Error obteniendo todas las Ventas con paginación", e);
            throw new DataAccessException("Error obteniendo todas las Ventas con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countVentas() {
        try {
            long total = ventaRepository.count();
            logger.info("Total de ventas: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error contando todas los Ventas", e);
            throw new DataAccessException("Error contando todos los Ventas", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> searchVentasByFecha(LocalDate fecha, Pageable pageable) {
        try {
            logger.info("Buscando Ventas por fecha: {}", fecha);
            Page<Venta> ventas = ventaRepository.findByFecha(fecha, pageable);
            return ventas.map(ventaMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Ventas por fecha", e);
            throw new DataAccessException("Error buscando Ventas por fecha", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> searchVentasByClienteNombre(String nombreCliente, Pageable pageable) {
        try {
            logger.info("Buscando Ventas por nombre de cliente: {}", nombreCliente);
            Page<Venta> ventas = ventaRepository.findByClienteNombre(nombreCliente, pageable);
            return ventas.map(venta -> {
                VentaDTO dto = ventaMapper.toDto(venta);
                dto.setClienteNombre(venta.getCliente().getNombre());
                return dto;
            });
        } catch (Exception e) {
            logger.error("Error buscando Ventas por nombre de cliente", e);
            throw new DataAccessException("Error buscando Ventas por nombre de cliente", e);
        }
    }
}
