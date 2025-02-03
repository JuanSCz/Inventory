package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.dto.StockDTO;
import com.interonda.inventory.entity.*;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.ProductoMapper;
import com.interonda.inventory.repository.*;
import com.interonda.inventory.service.ProductoService;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaRepository categoriaRepository;
    private final DepositoRepository depositoRepository;
    private final StockRepository stockRepository;
    private final HistorialStockRepository historialStockRepository;
    private final Validator validator;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper, CategoriaRepository categoriaRepository, DepositoRepository depositoRepository, StockRepository stockRepository, HistorialStockRepository historialStockRepository, Validator validator) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.categoriaRepository = categoriaRepository;
        this.depositoRepository = depositoRepository;
        this.stockRepository = stockRepository;
        this.historialStockRepository = historialStockRepository;
        this.validator = validator;
    }

    @Override
    public ProductoDTO convertToDto(Producto producto) {
        return productoMapper.toDto(producto);
    }

    @Override
    public Producto convertToEntity(ProductoDTO productoDTO) {
        return productoMapper.toEntity(productoDTO);
    }

    @Override
    @Transactional
    public ProductoDTO createProducto(ProductoDTO productoDTO) {
        ValidatorUtils.validateEntity(productoDTO, validator);
        try {
            logger.info("Creando nuevo Producto");
            Producto producto = productoMapper.toEntity(productoDTO);

            // Asignar la categoría al producto
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);

            // Asignar los stocks al producto y crear historial de stock
            producto.setStocks(productoDTO.getStocks().stream().map(stockDTO -> {
                Stock stock = stockRepository.findByProductoIdAndDepositoId(producto.getId(), stockDTO.getDepositoId()).orElse(new Stock());
                stock.setCantidad(stock.getCantidad() == null ? stockDTO.getCantidad() : stock.getCantidad() + stockDTO.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("CREACIÓN");
                stock.setProducto(producto);
                stock.setDeposito(depositoRepository.findById(stockDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + stockDTO.getDepositoId())));

                // Crear historial de stock
                HistorialStock historialStock = new HistorialStock();
                historialStock.setCantidadAnterior(0); // Asumiendo que es un nuevo stock
                historialStock.setCantidadNueva(stock.getCantidad());
                historialStock.setFechaActualizacion(stock.getFechaActualizacion());
                historialStock.setMotivo("Creación de producto");
                historialStock.setTipoMovimiento("CREACIÓN");
                historialStock.setProducto(producto);
                historialStock.setDeposito(stock.getDeposito());
                historialStock.setStock(stock);

                stock.getHistorialStocks().add(historialStock);

                return stock;
            }).collect(Collectors.toList()));

            // Asignar el depósito al producto
            if (!producto.getStocks().isEmpty()) {
                producto.setDeposito(producto.getStocks().get(0).getDeposito());
                producto.setActivo(true);
            }

            Producto savedProducto = productoRepository.save(producto);
            logger.info("Producto creado exitosamente con id: {}", savedProducto.getId());
            return productoMapper.toDto(savedProducto);
        } catch (Exception e) {
            logger.error("Error guardando Producto", e);
            throw new DataAccessException("Error guardando Producto", e);
        }
    }

    @Override
    @Transactional
    public ProductoDTO updateProducto(ProductoDTO productoDTO) {
        ValidatorUtils.validateEntity(productoDTO, validator);
        try {
            logger.info("Actualizando Producto con id: {}", productoDTO.getId());
            Producto producto = productoRepository.findById(productoDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + productoDTO.getId()));

            // Actualizar los campos del producto
            producto.setNombre(productoDTO.getNombre());
            producto.setDescripcion(productoDTO.getDescripcion());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setCosto(productoDTO.getCosto());
            producto.setStockActual(productoDTO.getStockActual());
            producto.setStockMinimo(productoDTO.getStockMinimo());

            // Asignar la categoría al producto
            if (productoDTO.getCategoriaId() != null) {
                Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id: " + productoDTO.getCategoriaId()));
                producto.setCategoria(categoria);
            } else {
                producto.setCategoria(null);
            }

            // Actualizar los stocks del producto y crear historial de stock
            producto.getStocks().clear();
            producto.getStocks().addAll(productoDTO.getStocks().stream().map(stockDTO -> {
                Stock stock = stockRepository.findByProductoIdAndDepositoId(producto.getId(), stockDTO.getDepositoId()).orElse(new Stock());
                Integer cantidadAnterior = stock.getCantidad() == null ? 0 : stock.getCantidad();
                stock.setCantidad(stockDTO.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("ACTUALIZACIÓN");
                stock.setProducto(producto);
                stock.setDeposito(depositoRepository.findById(stockDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + stockDTO.getDepositoId())));

                // Crear historial de stock
                HistorialStock historialStock = new HistorialStock();
                historialStock.setCantidadAnterior(cantidadAnterior);
                historialStock.setCantidadNueva(stock.getCantidad());
                historialStock.setFechaActualizacion(stock.getFechaActualizacion());
                historialStock.setMotivo("Actualización de producto");
                historialStock.setTipoMovimiento("ACTUALIZACIÓN");
                historialStock.setProducto(producto);
                historialStock.setDeposito(stock.getDeposito());
                historialStock.setStock(stock);

                stock.getHistorialStocks().add(historialStock);

                return stock;
            }).collect(Collectors.toList()));

            // Asignar el depósito al producto
            if (!producto.getStocks().isEmpty()) {
                producto.setDeposito(producto.getStocks().get(0).getDeposito());
            }

            Producto updatedProducto = productoRepository.save(producto);
            logger.info("Producto actualizado exitosamente con id: {}", updatedProducto.getId());
            return productoMapper.toDto(updatedProducto);
        } catch (ResourceNotFoundException e) {
            logger.warn("Producto no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Producto", e);
            throw new DataAccessException("Error actualizando Producto", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteProducto(Long id) {
        try {
            logger.info("Desactivando Producto con id: {}", id);
            Producto producto = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + id));

            // Desactivar el producto y establecer la fecha de baja en el DTO
            producto.setActivo(false);
            producto.setFechaBaja(LocalDateTime.now());
            producto.setStockActual(0);

            // Desactivar los stocks relacionados
            for (Stock stock : producto.getStocks()) {
                stock.setCantidad(0); // O cualquier otra lógica de desactivación que necesites
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("ELIMINACIÓN");
                stockRepository.save(stock);

                // Crear historial de stock para la desactivación
                HistorialStock historialStock = new HistorialStock();
                historialStock.setCantidadAnterior(stock.getCantidad());
                historialStock.setCantidadNueva(0);
                historialStock.setFechaActualizacion(stock.getFechaActualizacion());
                historialStock.setMotivo("Eliminación de producto");
                historialStock.setTipoMovimiento("ELIMINACIÓN");
                historialStock.setProducto(producto);
                historialStock.setDeposito(stock.getDeposito());
                historialStock.setStock(stock);
                historialStockRepository.save(historialStock);
            }

            productoRepository.save(producto);
            logger.info("Producto desactivado exitosamente con id: {}", id);
            return true;
        } catch (ResourceNotFoundException e) {
            logger.warn("Producto no encontrado: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error desactivando Producto", e);
            return false;
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Ejecutar todos los días a medianoche
    @Transactional
    public void eliminarProductosDadosDeBaja() {
        LocalDateTime haceDosMeses = LocalDateTime.now().minusMonths(2);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE); // Ajusta el tamaño de la página según sea necesario
        Page<Producto> productosParaEliminar = productoRepository.findByActivoFalseAndFechaBajaBefore(haceDosMeses, pageable);

        for (Producto producto : productosParaEliminar) {
            productoRepository.delete(producto);
            logger.info("Producto eliminado permanentemente con id: {}", producto.getId());
        }
    }

    public String formatPrecio(BigDecimal precio) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(precio);
    }

    public String formatCosto(BigDecimal costo) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat formatter = new DecimalFormat("#,###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(costo);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO getProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + id));
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        if (producto.getCategoria() != null) {
            productoDTO.setCategoriaId(producto.getCategoria().getId());
            productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        productoDTO.setStocks(producto.getStocks().stream().map(stock -> {
            StockDTO stockDTO = new StockDTO();
            stockDTO.setId(stock.getId());
            stockDTO.setCantidad(stock.getCantidad());
            stockDTO.setFechaActualizacion(stock.getFechaActualizacion());
            stockDTO.setOperacion(stock.getOperacion());
            stockDTO.setProductoId(stock.getProducto().getId());
            stockDTO.setDepositoId(stock.getDeposito().getId());
            stockDTO.setDepositoNombre(stock.getDeposito().getNombre()); // Asignar el nombre del depósito
            return stockDTO;
        }).collect(Collectors.toList()));
        return productoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> getAllProductos(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Productos activos con paginación");
            Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
            Page<Producto> productos = productoRepository.findByActivoTrue(sortedByIdDesc);
            return productos.map(producto -> {
                ProductoDTO productoDTO = productoMapper.toDto(producto);
                if (producto.getCategoria() != null) {
                    productoDTO.setCategoriaId(producto.getCategoria().getId());
                    productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
                } else {
                    productoDTO.setCategoriaId(null);
                    productoDTO.setCategoriaNombre(null);
                }
                // Formatear precio y costo
                productoDTO.setPrecioString(productoDTO.getPrecioString());
                productoDTO.setCostoString(productoDTO.getCostoString());
                return productoDTO;
            });
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Productos activos con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Productos activos con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countProductos() {
        try {
            long total = productoRepository.count();
            logger.info("Total de productos: {}", total); // Log para verificar el resultado
            return total;
        } catch (Exception e) {
            logger.error("Error contando todos los Productos", e);
            throw new DataAccessException("Error contando todos los Productos", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        try {
            logger.info("Obteniendo todos los productos");
            return productoRepository.findAll();
        } catch (Exception e) {
            logger.error("Error obteniendo todos los productos", e);
            throw new DataAccessException("Error obteniendo todos los productos", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> searchProductosByName(String nombre, Pageable pageable) {
        try {
            logger.info("Buscando Productos activos por nombre: {}", nombre);
            Page<Producto> productos = productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre, pageable);
            return productos.map(productoMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Productos activos por nombre", e);
            throw new DataAccessException("Error buscando Productos activos por nombre", e);
        }
    }

    @Override
    public Page<Map<String, Object>> getAllProductosAsMap(Pageable pageable) {
        Page<Producto> productos = productoRepository.findByActivoTrue(pageable);
        return productos.map(producto -> Map.of("id", producto.getId(), "nombre", producto.getNombre(), "descripción", producto.getDescripcion(), "precio", producto.getPrecio(), "costo", producto.getCosto(), "categoría", producto.getCategoria().getNombre()));
    }
}