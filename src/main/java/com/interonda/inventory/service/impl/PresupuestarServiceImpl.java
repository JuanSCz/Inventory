package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.entity.Cliente;
import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.entity.Venta;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.ClienteMapper;
import com.interonda.inventory.mapper.DetalleVentaMapper;
import com.interonda.inventory.mapper.VentaMapper;
import com.interonda.inventory.repository.ClienteRepository;
import com.interonda.inventory.repository.DetalleVentaRepository;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.repository.VentaRepository;
import com.interonda.inventory.service.PresupuestarService;
import com.interonda.inventory.utils.ValidatorUtils;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresupuestarServiceImpl implements PresupuestarService {
    private static final Logger logger = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final Validator validator;
    private final VentaMapper ventaMapper;
    private final ClienteMapper clienteMapper;
    private final DetalleVentaMapper detalleVentaMapper;
    private final TemplateEngine templateEngine; // Añadir este campo

    @Autowired
    public PresupuestarServiceImpl(VentaRepository ventaRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository, DetalleVentaRepository detalleVentaRepository, Validator validator, VentaMapper ventaMapper, ClienteMapper clienteMapper, DetalleVentaMapper detalleVentaMapper, TemplateEngine templateEngine) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.validator = validator;
        this.ventaMapper = ventaMapper;
        this.clienteMapper = clienteMapper;
        this.detalleVentaMapper = detalleVentaMapper;
        this.templateEngine = templateEngine; // Inicializar el campo
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
    public byte[] generatePdf(Long id) {
        try {
            VentaDTO ventaDTO = getVenta(id);
            if (ventaDTO == null) {
                throw new ResourceNotFoundException("Venta no encontrada con id: " + id);
            }

            // Formatear el valor del impuesto
            ventaDTO.setImpuestos(formatImpuestos(ventaDTO.getImpuestos()));

            Context context = new Context();
            context.setVariable("venta", ventaDTO);
            logger.info("VentaDTO para PDF: {}", ventaDTO);
            String htmlContent = templateEngine.process("presupuestar", context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, new ClassPathResource("/templates/").getURL().toString());
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Error generando el PDF para la venta con id: {}", id, e);
            throw new DataAccessException("Error generando el PDF", e);
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

    private BigDecimal formatTotal(BigDecimal total) {
        return total.setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO getVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));
        VentaDTO ventaDTO = ventaMapper.toDto(venta);
        ventaDTO.setCliente(clienteMapper.toDto(venta.getCliente()));
        ventaDTO.setClienteNombre(venta.getCliente().getNombre());
        ventaDTO.setDetallesVenta(venta.getDetallesVenta().stream().map(detalleVenta -> {
            DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO();
            detalleVentaDTO.setId(detalleVenta.getId());
            detalleVentaDTO.setCantidad(detalleVenta.getCantidad());
            detalleVentaDTO.setPrecioUnitario(detalleVenta.getPrecioUnitario());
            detalleVentaDTO.setSubtotal(detalleVenta.getPrecioUnitario().multiply(new BigDecimal(detalleVenta.getCantidad())));
            detalleVentaDTO.setProductoId(detalleVenta.getProducto().getId());
            detalleVentaDTO.setProductoNombre(detalleVenta.getProducto().getNombre());

            // Formatear los valores
            detalleVentaDTO.setPrecioUnitarioFormatted(formatDecimal(detalleVenta.getPrecioUnitario()));
            detalleVentaDTO.setSubtotalFormatted(formatDecimal(detalleVentaDTO.getSubtotal()));

            return detalleVentaDTO;
        }).collect(Collectors.toList()));

        // Calcular subtotal correctamente
        BigDecimal total = venta.getTotal();
        BigDecimal impuestos = new BigDecimal(venta.getImpuestos());
        BigDecimal subtotal = total.divide(BigDecimal.ONE.add(impuestos), 2, RoundingMode.HALF_UP);
        ventaDTO.setSubtotal(subtotal);

        // Formatear los valores
        ventaDTO.setTotalFormatted(getTotalFormatted(total));
        ventaDTO.setSubtotalFormatted(getSubtotalFormatted(subtotal));

        return ventaDTO;
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
                dto.setTotal(formatTotal(venta.getTotal())); // Formatear total
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

    public String getTotalFormatted(BigDecimal total) {
        return formatDecimal(total);
    }

    public String getSubtotalFormatted(BigDecimal subtotal) {
        return formatDecimal(subtotal);
    }

    private String formatDecimal(BigDecimal value) {
        DecimalFormat df = new DecimalFormat("#,##0.000");
        return df.format(value);
    }
}

