package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.HistorialStockRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.repository.UsuarioRepository;
import com.interonda.Inventory.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private HistorialStockRepository historialStockRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El producto N " + id + " no fue encontrado con exito!"));
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.buscarPorNombre(nombre);
    }

    @Override
    public List<Producto> buscarPorCategoria(Long idCategoria) {
        return productoRepository.buscarPorCategoria(idCategoria);
    }

    @Override
    public List<Producto> buscarPorProveedor(Long idProveedor) {
        return productoRepository.buscarPorProveedor(idProveedor);
    }

    @Override
    public List<Producto> productosBajoStockMinimo() {
        return productoRepository.productosBajoStockMinimo();
    }

    @Override
    public Producto actualizarStock(Long idProducto, int cantidad, String motivo, String tipoMovimiento, Long idUsuario) {

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        int stockAnterior = producto.getStockActual();
        int stockNuevo = stockAnterior + cantidad;

        producto.setStockActual(stockNuevo);
        productoRepository.save(producto);

        // Crear el historial de stock
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

    @Override
    public BigDecimal calcularMargenDeGanancia(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        return producto.getPrecio().subtract(producto.getCosto());
    }

    @Override
    public Producto actualizarPrecio(Long idProducto, BigDecimal nuevoPrecio) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        producto.setPrecio(nuevoPrecio);
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarCosto(Long idProducto, BigDecimal nuevoCosto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        producto.setCosto(nuevoCosto);
        return productoRepository.save(producto);
    }

}
