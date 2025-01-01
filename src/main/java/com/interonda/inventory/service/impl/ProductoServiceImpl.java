package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.dto.StockDTO;
import com.interonda.inventory.entity.*;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.ProductoMapper;
import com.interonda.inventory.repository.CategoriaRepository;
import com.interonda.inventory.repository.DepositoRepository;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.repository.StockRepository;
import com.interonda.inventory.service.ProductoService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaRepository categoriaRepository;
    private final DepositoRepository depositoRepository;
    private final StockRepository stockRepository;
    private final Validator validator;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper, CategoriaRepository categoriaRepository, DepositoRepository depositoRepository, StockRepository stockRepository, Validator validator) {
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
    public ProductoDTO createProducto(ProductoDTO productoDTO) {
        ValidatorUtils.validateEntity(productoDTO, validator);
        try {
            logger.info("Creando nuevo Producto");
            Producto producto = productoMapper.toEntity(productoDTO);

            // Asignar la categoría al producto
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);

            // Asignar los stocks al producto
            producto.setStocks(productoDTO.getStocks().stream().map(stockDTO -> {
                Stock stock = new Stock();
                stock.setCantidad(stockDTO.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("CREACIÓN");
                stock.setProducto(producto);
                stock.setDeposito(depositoRepository.findById(stockDTO.getDepositoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + stockDTO.getDepositoId())));
                return stock;
            }).collect(Collectors.toList()));

            // Establecer los atributos como null
            producto.setCodigoBarras(null);
            producto.setNumeroDeSerie(null);
            producto.setMacAddress(null);

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
            Producto producto = productoRepository.findById(productoDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + productoDTO.getId()));

            // Actualizar los campos del producto
            producto.setNombre(productoDTO.getNombre());
            producto.setDescripcion(productoDTO.getDescripcion());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setCosto(productoDTO.getCosto());

            // Establecer los atributos como null
            producto.setCodigoBarras(null);
            producto.setNumeroDeSerie(null);
            producto.setMacAddress(null);

            producto.setStockActual(productoDTO.getStockActual());
            producto.setStockMinimo(productoDTO.getStockMinimo());

            // Asignar la categoría al producto
            if (productoDTO.getCategoriaId() != null) {
                Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id: " + productoDTO.getCategoriaId()));
                producto.setCategoria(categoria);
            } else {
                producto.setCategoria(null);
            }

            // Actualizar los stocks del producto
            producto.getStocks().clear();
            producto.getStocks().addAll(productoDTO.getStocks().stream().map(stockDTO -> {
                Stock stock = new Stock();
                stock.setCantidad(stockDTO.getCantidad());
                stock.setFechaActualizacion(LocalDateTime.now());
                stock.setOperacion("ACTUALIZACIÓN");
                stock.setProducto(producto);
                stock.setDeposito(depositoRepository.findById(stockDTO.getDepositoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + stockDTO.getDepositoId())));
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
    @Transactional
    public boolean deleteProducto(Long id) {
        try {
            logger.info("Eliminando Producto con id: {}", id);
            if (!productoRepository.existsById(id)) {
                throw new ResourceNotFoundException("Producto no encontrado con el id: " + id);
            }
            productoRepository.deleteById(id);
            logger.info("Producto eliminado exitosamente con id: {}", id);
            return true;
        } catch (ResourceNotFoundException e) {
            logger.warn("Producto no encontrado: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error eliminando Producto", e);
            return false;
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
            stockDTO.setDepositoNombre(stock.getDeposito().getNombre()); // Asignar el nombre del depósito
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
}