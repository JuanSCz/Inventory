package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.entity.*;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.CompraMapper;
import com.interonda.inventory.repository.*;
import com.interonda.inventory.service.CompraService;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final Validator validator;
    private final CompraMapper compraMapper;
    private final DepositoRepository depositoRepository;
    private final StockRepository stockRepository;
    private final HistorialStockRepository historialStockRepository;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, ProveedorRepository proveedorRepository, ProductoRepository productoRepository, DetalleCompraRepository detalleCompraRepository, Validator validator, CompraMapper compraMapper, DepositoRepository depositoRepository, StockRepository stockRepository, HistorialStockRepository historialStockRepository) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.validator = validator;
        this.compraMapper = compraMapper;
        this.depositoRepository = depositoRepository;
        this.stockRepository = stockRepository;
        this.historialStockRepository = historialStockRepository;
    }

    @Override
    public CompraDTO convertToDto(Compra compra) {
        return compraMapper.toDto(compra);
    }

    @Override
    public Compra convertToEntity(CompraDTO compraDTO) {
        return compraMapper.toEntity(compraDTO);
    }

    @Override
    @Transactional
    public CompraDTO createCompra(CompraDTO compraDTO) {
        ValidatorUtils.validateEntity(compraDTO, validator);
        try {
            logger.info("Creando nueva Compra");
            Compra compra = compraMapper.toEntity(compraDTO);

            // Asignar el proveedor a la compra
            Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId()).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + compraDTO.getProveedorId()));
            compra.setProveedor(proveedor);

            // Asignar los detalles de compra
            compra.setDetallesCompra(compraDTO.getDetallesCompra().stream().map(detalleDTO -> {
                DetalleCompra detalle = compraMapper.toDetalleEntity(detalleDTO);
                detalle.setCompra(compra);
                Producto producto = productoRepository.findById(detalleDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleDTO.getProductoId()));
                Deposito deposito = depositoRepository.findById(detalleDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + detalleDTO.getDepositoId()));
                detalle.setProducto(producto);
                detalle.setDeposito(deposito);

                // Actualizar el stock del producto en el depósito
                Stock stock = stockRepository.findByProductoIdAndDepositoId(producto.getId(), deposito.getId()).orElse(new Stock());
                Integer cantidadAnterior = stock.getCantidad() == null ? 0 : stock.getCantidad();
                stock.setCantidad(stock.getCantidad() == null ? detalle.getCantidad() : stock.getCantidad() + detalle.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("COMPRA");
                stock.setProducto(producto);
                stock.setDeposito(deposito);
                stockRepository.save(stock);

                // Crear historial de stock
                HistorialStock historialStock = new HistorialStock();
                historialStock.setCantidadAnterior(cantidadAnterior);
                historialStock.setCantidadNueva(stock.getCantidad());
                historialStock.setFechaActualizacion(stock.getFechaActualizacion());
                historialStock.setMotivo("Compra de producto");
                historialStock.setTipoMovimiento("COMPRA");
                historialStock.setProducto(producto);
                historialStock.setDeposito(deposito);
                historialStock.setStock(stock);
                historialStockRepository.save(historialStock);

                // Actualizar el stock_actual del producto
                producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
                productoRepository.save(producto);

                return detalle;
            }).collect(Collectors.toList()));

            // Establecer el total directamente desde el DTO
            compra.setTotal(compraDTO.getTotal());
            compra.setImpuestos(compraDTO.getImpuestos());

            Compra savedCompra = compraRepository.save(compra);
            logger.info("Compra creada exitosamente con id: {}", savedCompra.getId());
            return compraMapper.toDto(savedCompra);
        } catch (Exception e) {
            logger.error("Error guardando Compra", e);
            throw new DataAccessException("Error guardando Compra", e);
        }
    }

    @Override
    @Transactional
    public CompraDTO updateCompra(CompraDTO compraDTO) {
        ValidatorUtils.validateEntity(compraDTO, validator);
        try {
            logger.info("Actualizando Compra con id: {}", compraDTO.getId());
            Compra compra = compraRepository.findById(compraDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + compraDTO.getId()));

            // Actualizar los campos de la compra
            compra.setFecha(compraDTO.getFecha());
            compra.setTotal(compraDTO.getTotal());
            compra.setMetodoPago(compraDTO.getMetodoPago());
            compra.setEstado(compraDTO.getEstado());
            compra.setImpuestos(compraDTO.getImpuestos());

            // Asignar el proveedor a la compra
            Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId()).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + compraDTO.getProveedorId()));
            compra.setProveedor(proveedor);

            // Actualizar los detalles de compra
            List<DetalleCompra> detallesExistentes = compra.getDetallesCompra();
            List<DetalleCompraDTO> nuevosDetallesDTO = compraDTO.getDetallesCompra();

            // Eliminar detalles que ya no están presentes
            detallesExistentes.removeIf(detalle -> {
                boolean exists = nuevosDetallesDTO.stream().anyMatch(nuevoDetalle -> nuevoDetalle.getId() != null && nuevoDetalle.getId().equals(detalle.getId()));
                if (!exists) {
                    // Restar el stock del detalle eliminado
                    List<Stock> stocks = stockRepository.findByProductoIdAndDepositoIdList(detalle.getProducto().getId(), detalle.getDeposito().getId());
                    for (Stock stock : stocks) {
                        stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
                        stock.setFechaActualizacion(LocalDateTime.now());
                        stock.setOperacion("ACTUALIZACIÓN");
                        stockRepository.save(stock);
                    }

                    // Actualizar el stock_actual del producto
                    Producto producto = detalle.getProducto();
                    producto.setStockActual(producto.getStockActual() - detalle.getCantidad());
                    productoRepository.save(producto);
                }
                return !exists;
            });

            // Actualizar o añadir nuevos detalles
            for (DetalleCompraDTO nuevoDetalleDTO : nuevosDetallesDTO) {
                DetalleCompra detalle = detallesExistentes.stream().filter(d -> nuevoDetalleDTO.getId() != null && d.getId().equals(nuevoDetalleDTO.getId())).findFirst().orElse(new DetalleCompra());

                if (detalle.getId() != null) {
                    // Restar el stock anterior
                    Stock stock = stockRepository.findByProductoIdAndDepositoId(detalle.getProducto().getId(), detalle.getDeposito().getId()).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado"));
                    Integer cantidadAnterior = stock.getCantidad();
                    stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
                    stock.setFechaActualizacion(LocalDateTime.now());
                    stock.setOperacion("ACTUALIZACIÓN");
                    stockRepository.save(stock);

                    // Crear historial de stock
                    HistorialStock historialStock = new HistorialStock();
                    historialStock.setCantidadAnterior(cantidadAnterior);
                    historialStock.setCantidadNueva(stock.getCantidad());
                    historialStock.setFechaActualizacion(stock.getFechaActualizacion());
                    historialStock.setMotivo("Actualización de detalle de compra");
                    historialStock.setTipoMovimiento("ACTUALIZACIÓN");
                    historialStock.setProducto(detalle.getProducto());
                    historialStock.setDeposito(detalle.getDeposito());
                    historialStock.setStock(stock);
                    historialStockRepository.save(historialStock);

                    // Actualizar el stock_actual del producto
                    Producto producto = detalle.getProducto();
                    producto.setStockActual(producto.getStockActual() - detalle.getCantidad());
                    productoRepository.save(producto);
                }

                detalle.setCantidad(nuevoDetalleDTO.getCantidad());
                detalle.setPrecioUnitario(nuevoDetalleDTO.getPrecioUnitario());
                detalle.setProducto(productoRepository.findById(nuevoDetalleDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + nuevoDetalleDTO.getProductoId())));
                detalle.setDeposito(depositoRepository.findById(nuevoDetalleDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + nuevoDetalleDTO.getDepositoId())));
                detalle.setCompra(compra);

                if (detalle.getId() == null) {
                    detallesExistentes.add(detalle);
                }

                // Sumar el nuevo stock
                Stock stock = stockRepository.findByProductoIdAndDepositoId(detalle.getProducto().getId(), detalle.getDeposito().getId()).orElse(new Stock());
                Integer cantidadAnterior = stock.getCantidad() == null ? 0 : stock.getCantidad();
                stock.setCantidad(stock.getCantidad() == null ? detalle.getCantidad() : stock.getCantidad() + detalle.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("ACTUALIZACIÓN");
                stock.setProducto(detalle.getProducto());
                stock.setDeposito(detalle.getDeposito());
                stockRepository.save(stock);

                // Crear historial de stock
                HistorialStock historialStock = new HistorialStock();
                historialStock.setCantidadAnterior(cantidadAnterior);
                historialStock.setCantidadNueva(stock.getCantidad());
                historialStock.setFechaActualizacion(stock.getFechaActualizacion());
                historialStock.setMotivo("Actualización de detalle de compra");
                historialStock.setTipoMovimiento("ACTUALIZACIÓN");
                historialStock.setProducto(detalle.getProducto());
                historialStock.setDeposito(detalle.getDeposito());
                historialStock.setStock(stock);
                historialStockRepository.save(historialStock);

                // Actualizar el stock_actual del producto
                Producto producto = detalle.getProducto();
                producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
                productoRepository.save(producto);
            }

            compra.setDetallesCompra(detallesExistentes);

            Compra updatedCompra = compraRepository.save(compra);
            logger.info("Compra actualizada exitosamente con id: {}", updatedCompra.getId());
            return compraMapper.toDto(updatedCompra);
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Compra", e);
            throw new DataAccessException("Error actualizando Compra", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteCompra(Long id) {
        try {
            logger.info("Eliminando Compra con id: {}", id);
            Compra compra = compraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + id));

            // Revertir el stock de los productos
            for (DetalleCompra detalle : compra.getDetallesCompra()) {
                Stock stock = stockRepository.findByProductoIdAndDepositoId(detalle.getProducto().getId(), detalle.getDeposito().getId()).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado para el producto y depósito especificados"));
                Integer cantidadAnterior = stock.getCantidad();
                stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("Eliminación de compra");
                stockRepository.save(stock);

                // Crear historial de stock
                HistorialStock historialStock = new HistorialStock();
                historialStock.setCantidadAnterior(cantidadAnterior);
                historialStock.setCantidadNueva(stock.getCantidad());
                historialStock.setFechaActualizacion(stock.getFechaActualizacion());
                historialStock.setMotivo("Eliminación de compra");
                historialStock.setTipoMovimiento("ELIMINACIÓN");
                historialStock.setProducto(detalle.getProducto());
                historialStock.setDeposito(detalle.getDeposito());
                historialStock.setStock(stock);
                historialStockRepository.save(historialStock);

                // Actualizar el stock_actual del producto
                Producto producto = detalle.getProducto();
                producto.setStockActual(producto.getStockActual() - detalle.getCantidad());
                productoRepository.save(producto);
            }

            // Eliminar la compra
            compraRepository.delete(compra);
            logger.info("Compra eliminada exitosamente con id: {}", id);
            return true;
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Compra", e);
            throw new DataAccessException("Error eliminando Compra", e);
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
    public CompraDTO getCompra(Long id) {
        try {
            logger.info("Obteniendo Compra con id: {}", id);
            Compra compra = compraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + id));
            CompraDTO compraDTO = compraMapper.toDto(compra);
            compraDTO.setProveedorId(compra.getProveedor().getId());
            compraDTO.setProveedorNombre(compra.getProveedor().getNombre());
            compraDTO.setDetallesCompra(compra.getDetallesCompra().stream().map(detalle -> {
                DetalleCompraDTO detalleDTO = compraMapper.toDetalleDto(detalle);
                detalleDTO.setProductoId(detalle.getProducto().getId());
                detalleDTO.setProductoNombre(detalle.getProducto().getNombre());
                detalleDTO.setDepositoId(detalle.getDeposito().getId()); // Asegúrate de que el valor del depósito se esté estableciendo
                detalleDTO.setDepositoNombre(detalle.getDeposito().getNombre());
                detalleDTO.setPrecioUnitarioString(formatPrecioUnitario(detalle.getPrecioUnitario())); // Formatear el precio unitario
                detalleDTO.setTotalFormatted(formatTotal(detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad())))); // Formatear el total
                return detalleDTO;
            }).collect(Collectors.toList()));
            compraDTO.setTotalString(formatTotal(compra.getTotal())); // Formatear el total
            return compraDTO;
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Compra", e);
            throw new DataAccessException("Error obteniendo Compra", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompraDTO> getAllCompras(Pageable pageable) {
        try {
            logger.info("Obteniendo todas las Compras con paginación");
            Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
            Page<Compra> compras = compraRepository.findAll(sortedByIdDesc);
            return compras.map(compra -> {
                CompraDTO dto = compraMapper.toDto(compra);
                dto.setProveedorNombre(compra.getProveedor().getNombre());
                dto.setImpuestos(formatImpuestos(compra.getImpuestos())); // Formatear impuestos
                dto.setTotal(compra.getTotal()); // Establecer el total como BigDecimal
                dto.setTotalString(formatTotal(compra.getTotal())); // Formatear total como String para visualización
                return dto;
            });
        } catch (Exception e) {
            logger.error("Error obteniendo todas las Compras con paginación", e);
            throw new DataAccessException("Error obteniendo todas las Compras con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countCompras() {
        try {
            long total = compraRepository.count();
            logger.info("Total de compras: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error contando todas las Compras", e);
            throw new DataAccessException("Error contando todas las Compras", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompraDTO> searchComprasByFecha(LocalDate fecha, Pageable pageable) {
        try {
            logger.info("Buscando Compras por fecha: {}", fecha);
            Page<Compra> compras = compraRepository.findByFecha(fecha, pageable);
            return compras.map(compraMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Compras por fecha", e);
            throw new DataAccessException("Error buscando Compras por fecha", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompraDTO> searchComprasByProveedorNombre(String nombreProveedor, Pageable pageable) {
        try {
            logger.info("Buscando Compras por nombre de proveedor: {}", nombreProveedor);
            Page<Compra> compras = compraRepository.findByProveedorNombre(nombreProveedor, pageable);
            return compras.map(compra -> {
                CompraDTO dto = compraMapper.toDto(compra);
                dto.setProveedorNombre(compra.getProveedor().getNombre());
                return dto;
            });
        } catch (Exception e) {
            logger.error("Error buscando Compras por nombre de proveedor", e);
            throw new DataAccessException("Error buscando Compras por nombre de proveedor", e);
        }
    }
}