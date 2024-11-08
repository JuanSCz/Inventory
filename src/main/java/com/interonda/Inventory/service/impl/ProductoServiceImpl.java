package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.HistorialStockRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.repository.UsuarioRepository;
import com.interonda.Inventory.service.ProductoService;
import com.interonda.Inventory.service.ProveedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private HistorialStockRepository historialStockRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProveedorService proveedorService;

    @Transactional(readOnly = true)
    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El producto numero " + id + " no fue encontrado con éxito!"));
    }

    @Transactional
    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.buscarPorNombre(nombre);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> buscarPorCategoria(Long idCategoria) {
        return productoRepository.buscarPorCategoria(idCategoria);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> buscarPorProveedor(Long idProveedor) {
        return productoRepository.buscarPorProveedor(idProveedor);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> productosBajoStockMinimo() {
        return productoRepository.productosBajoStockMinimo();
    }

    @Transactional
    @Override
    public Producto actualizarStock(Long idProducto, int cantidad, String motivo, String tipoMovimiento, Long idUsuario) {

        Producto producto = findById(idProducto);
        int stockAnterior = producto.getStockActual();
        int stockNuevo = stockAnterior + cantidad;

        producto.setStockActual(stockNuevo);
        productoRepository.save(producto);

        HistorialStock historial = new HistorialStock();
        historial.setProducto(producto);
        historial.setCantidadAnterior(stockAnterior);
        historial.setCantidadNueva(stockNuevo);
        historial.setMotivo(motivo);
        historial.setTipoMovimiento(tipoMovimiento);
        historial.setFecha(LocalDateTime.now());
        historial.setUsuario(usuarioRepository.findById(idUsuario).orElse(null));

        historialStockRepository.save(historial);

        return producto;
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal calcularMargenDeGanancia(Long idProducto) {
        Producto producto = findById(idProducto);
        return producto.getPrecio().subtract(producto.getCosto());
    }

    @Transactional
    @Override
    public Producto actualizarPrecio(Long idProducto, BigDecimal nuevoPrecio) {
        if (nuevoPrecio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El nuevo precio debe ser mayor a cero");
        }
        Producto producto = findById(idProducto);
        producto.setPrecio(nuevoPrecio);
        return productoRepository.save(producto);
    }

    @Transactional
    @Override
    public Producto actualizarCosto(Long idProducto, BigDecimal nuevoCosto) {
        if (nuevoCosto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El nuevo costo debe ser mayor a cero");
        }
        Producto producto = findById(idProducto);
        producto.setCosto(nuevoCosto);
        return productoRepository.save(producto);
    }

    @Transactional
    @Override
    public Producto crearProducto(Producto producto, List<Long> idsProveedores) {

        if (producto.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser un valor positivo");
        }

        List<Proveedor> proveedores = idsProveedores.stream().map(id -> proveedorService.findById(id)).collect(Collectors.toList());

        producto.setProveedores(proveedores);
        return productoRepository.save(producto);
    }
}
