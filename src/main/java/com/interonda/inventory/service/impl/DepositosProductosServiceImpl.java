package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.dto.StockDTO;
import com.interonda.inventory.entity.Categoria;
import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.entity.Stock;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.ProductoMapper;
import com.interonda.inventory.repository.CategoriaRepository;
import com.interonda.inventory.repository.DepositoRepository;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.repository.StockRepository;
import com.interonda.inventory.service.DepositosProductosService;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepositosProductosServiceImpl implements DepositosProductosService {
    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaRepository categoriaRepository;
    private final DepositoRepository depositoRepository;
    private final StockRepository stockRepository;
    private final Validator validator;

    @Autowired
    public DepositosProductosServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper, CategoriaRepository categoriaRepository, DepositoRepository depositoRepository, StockRepository stockRepository, Validator validator) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.categoriaRepository = categoriaRepository;
        this.depositoRepository = depositoRepository;
        this.stockRepository = stockRepository;
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
    public ProductoDTO updateProducto(ProductoDTO productoDTO) {
        ValidatorUtils.validateEntity(productoDTO, validator);
        try {
            logger.info("Actualizando Producto con id: {}", productoDTO.getId());
            Producto producto = productoRepository.findById(productoDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + productoDTO.getId()));

            producto.setNombre(productoDTO.getNombre());
            producto.setDescripcion(productoDTO.getDescripcion());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setCosto(productoDTO.getCosto());
            producto.setStockActual(productoDTO.getStockActual());
            producto.setStockMinimo(productoDTO.getStockMinimo());

            if (productoDTO.getCategoriaId() != null) {
                Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id: " + productoDTO.getCategoriaId()));
                producto.setCategoria(categoria);
            } else {
                producto.setCategoria(null);
            }

            producto.getStocks().clear();
            producto.getStocks().addAll(productoDTO.getStocks().stream().map(stockDTO -> {
                Stock stock = new Stock();
                stock.setCantidad(stockDTO.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("ACTUALIZACIÓN");
                stock.setProducto(producto);
                stock.setDeposito(depositoRepository.findById(stockDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + stockDTO.getDepositoId())));
                return stock;
            }).collect(Collectors.toList()));

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
            stockDTO.setDepositoNombre(stock.getDeposito().getNombre());
            return stockDTO;
        }).collect(Collectors.toList()));
        return productoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> getAllProductos(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Productos con paginación");
            Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
            Page<Producto> productos = productoRepository.findAll(sortedByIdDesc);
            return productos.map(producto -> {
                ProductoDTO productoDTO = productoMapper.toDto(producto);
                if (producto.getCategoria() != null) {
                    productoDTO.setCategoriaId(producto.getCategoria().getId());
                    productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
                } else {
                    productoDTO.setCategoriaId(null);
                    productoDTO.setCategoriaNombre(null);
                }
                return productoDTO;
            });
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Productos con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Productos con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> searchProductosByName(String nombre, Pageable pageable) {
        try {
            logger.info("Buscando Productos por nombre: {}", nombre);
            Page<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
            return productos.map(productoMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Productos por nombre", e);
            throw new DataAccessException("Error buscando Productos por nombre", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countProductos() {
        try {
            long total = productoRepository.count();
            logger.info("Total de productos: {}", total);
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
    public Page<ProductoDTO> getProductosByDeposito(Long depositoId, Pageable pageable) {
        Page<Producto> productos = productoRepository.findByDepositoId(depositoId, pageable);
        return productos.map(producto -> {
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setCosto(producto.getCosto());
            productoDTO.setCodigoBarras(producto.getCodigoBarras());
            productoDTO.setNumeroDeSerie(producto.getNumeroDeSerie());
            productoDTO.setStockActual(producto.getStockActual()); // Asegúrate de incluir el stock actual
            productoDTO.setStockMinimo(producto.getStockMinimo());
            productoDTO.setMacAddress(producto.getMacAddress());
            productoDTO.setDepositoId(producto.getDeposito().getId());
            productoDTO.setCategoriaId(producto.getCategoria().getId());
            productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());

            productoDTO.setStocks(producto.getStocks().stream().map(stock -> {
                StockDTO stockDTO = new StockDTO();
                stockDTO.setId(stock.getId());
                stockDTO.setCantidad(stock.getCantidad());
                stockDTO.setFechaActualizacion(stock.getFechaActualizacion());
                stockDTO.setOperacion(stock.getOperacion());
                stockDTO.setProductoId(stock.getProducto().getId());
                stockDTO.setDepositoId(stock.getDeposito().getId());
                stockDTO.setDepositoNombre(stock.getDeposito().getNombre());
                return stockDTO;
            }).collect(Collectors.toList()));

            return productoDTO;
        });
    }

    @Override
    public Page<Map<String, Object>> getProductosByDepositoAsMap(Long depositoId, Pageable pageable) {
        Page<Producto> productos = productoRepository.findByDepositoId(depositoId, pageable);
        return productos.map(producto -> {
            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("id", producto.getId());
            productoMap.put("nombre", producto.getNombre());
            productoMap.put("descripcion", producto.getDescripcion());
            productoMap.put("stock", producto.getStockActual());
            productoMap.put("macAddress", producto.getMacAddress());
            productoMap.put("numeroDeSerie", producto.getNumeroDeSerie());
            productoMap.put("codigoBarras", producto.getCodigoBarras());
            productoMap.put("categoria", producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoría");
            return productoMap;
        });
    }
}
