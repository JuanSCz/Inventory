package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.entity.Usuario;
import com.interonda.Inventory.exceptions.BadRequestException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.HistorialStockRepository;
import com.interonda.Inventory.repository.StockRepository;
import com.interonda.Inventory.repository.UsuarioRepository;
import com.interonda.Inventory.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.interonda.Inventory.util.ValidationUtil.validarCantidadPositiva;

public class StockServiceImpl implements StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialStockRepository historialStockRepository;

    public StockServiceImpl(StockRepository stockRepository, UsuarioRepository usuarioRepository, HistorialStockRepository historialStockRepository) {
        this.stockRepository = stockRepository;
        this.usuarioRepository = usuarioRepository;
        this.historialStockRepository = historialStockRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Stock findById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El stock N " + id + " no fue encontrado!"));
    }

    @Transactional
    @Override
    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        stockRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void actualizarCantidad(Long stockId, Integer cantidadNueva, String operacion, Long usuarioId, String observacion) {
        // Buscar el stock actual
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con ID: " + stockId));

        validarCantidadPositiva(cantidadNueva);

        Integer cantidadAnterior = stock.getCantidad(); // Guardamos la cantidad anterior
        Integer nuevaCantidad = cantidadAnterior + cantidadNueva; // Calculamos la nueva cantidad

        // Validación: no puede ser menor que 0
        if (nuevaCantidad < 0) {
            throw new BadRequestException("No hay suficiente stock para la operación.");
        }

        // Actualizamos la cantidad de stock
        stock.setCantidad(nuevaCantidad);
        stock.setFechaActualizacion(LocalDateTime.now());
        stockRepository.save(stock); // Guardamos el stock actualizado

        // Obtener el usuario que realiza la operación
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // Crear un nuevo registro en HistorialStock
        HistorialStock historialStock = new HistorialStock();
        historialStock.setStock(stock); // Asociamos al stock actualizado
        historialStock.setCantidadAnterior(cantidadAnterior); // Cantidad anterior
        historialStock.setCantidadNueva(nuevaCantidad); // Nueva cantidad
        historialStock.setFecha(LocalDateTime.now()); // Fecha de la operación
        historialStock.setMotivo(operacion); // Motivo de la operación (ajuste, compra, venta, etc.)
        historialStock.setTipoMovimiento("Actualización de stock"); // Tipo de movimiento (por ejemplo, "entrada", "salida")
        historialStock.setObservacion(observacion); // Cualquier observación adicional
        historialStock.setProducto(stock.getProducto()); // Producto asociado con el stock
        historialStock.setDeposito(stock.getDeposito()); // Depósito asociado con el stock
        historialStock.setUsuario(usuario); // Usuario que realizó la operación

        // Guardamos el historial de stock
        historialStockRepository.save(historialStock);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean verificarStockSuficiente(Long stockId, int cantidadRequerida) {
        // Validamos si la cantidad requerida es válida

        validarCantidadPositiva(cantidadRequerida);

        // Buscamos el stock en la base de datos
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con ID: " + stockId));

        // Verificamos si hay suficiente stock
        if (stock.getCantidad() >= cantidadRequerida) {
            return true; // Hay suficiente stock
        } else {
            return false; // No hay suficiente stock
        }
    }

    @Transactional
    @Override
    public void registrarMovimiento(Long stockId, int cantidadMovida, String operacion, Long usuarioId, String tipoOperacion) {
        // Validamos que la cantidad movida sea positiva

        validarCantidadPositiva(cantidadMovida);

        // Buscamos el stock en la base de datos
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con ID: " + stockId));

        // Verificamos si el movimiento es válido
        if (tipoOperacion.equals("retiro") && stock.getCantidad() < cantidadMovida) {
            throw new BadRequestException("No hay suficiente stock para realizar el retiro.");
        }

        // Si es una "entrada", aumentamos el stock
        // Si es un "retiro", disminuimos el stock
        int cantidadAnterior = stock.getCantidad();
        int cantidadNueva = cantidadAnterior;

        if ("entrada".equalsIgnoreCase(tipoOperacion)) {
            cantidadNueva += cantidadMovida;
        } else if ("retiro".equalsIgnoreCase(tipoOperacion)) {
            cantidadNueva -= cantidadMovida;
        } else {
            throw new BadRequestException("Operación no válida. Use 'entrada' o 'retiro'.");
        }

        // Actualizamos la cantidad de stock
        stock.setCantidad(cantidadNueva);
        stock.setFechaActualizacion(LocalDateTime.now());

        // Guardamos el movimiento en el historial de stock
        HistorialStock historialStock = new HistorialStock();
        historialStock.setCantidadAnterior(cantidadAnterior);
        historialStock.setCantidadNueva(cantidadNueva);
        historialStock.setFecha(LocalDateTime.now());
        historialStock.setMotivo("Movimiento de stock: " + tipoOperacion);
        historialStock.setTipoMovimiento(tipoOperacion);
        historialStock.setProducto(stock.getProducto());
        historialStock.setDeposito(stock.getDeposito());

        // Relacionamos el historial con el stock y el usuario
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));
        historialStock.setUsuario(usuario);
        historialStock.setStock(stock);

        // Guardamos el historial de stock en la base de datos
        historialStockRepository.save(historialStock);

        // Finalmente, guardamos los cambios en el stock
        stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public Page<HistorialStock> obtenerHistorialDeMovimientos(Long stockId, Pageable pageable) {
        // Verificamos que el stock existe
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con ID: " + stockId));

        // Obtenemos el historial de movimientos para el stock solicitado con paginación
        return historialStockRepository.findByStockId(stockId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public void validarStockAntesDeMovimiento(int cantidadRequerida, Stock stock) {
        // Verificamos que la cantidad requerida sea positiva
        validarCantidadPositiva(cantidadRequerida);

        // Verificamos que haya suficiente stock
        if (stock.getCantidad() < cantidadRequerida) {
            throw new BadRequestException("No hay suficiente stock para realizar el movimiento. Stock disponible: " + stock.getCantidad());
        }
    }

    @Transactional
    @Override
    public void actualizarFechaDeActualizacion(Long stockId) {
        // Buscamos el stock en la base de datos
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con ID: " + stockId));

        // Actualizamos la fecha de actualización al momento actual
        stock.setFechaActualizacion(LocalDateTime.now());

        // Guardamos el cambio en la base de datos
        stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    @Override
    public Stock obtenerStockDeProducto(Long productoId, Long depositoId) {
        // Validamos que los parámetros no sean nulos
        if (productoId == null || depositoId == null) {
            throw new BadRequestException("El ID del producto y el ID del depósito son obligatorios.");
        }

        // Buscamos el stock en la base de datos basado en producto y depósito
        Stock stock = stockRepository.obtenerStockPorProductoYDeposito(productoId, depositoId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado para el producto con ID: "
                        + productoId + " en el depósito con ID: " + depositoId));

        // Retornamos el stock encontrado
        return stock;
    }

    public Usuario obtenerUsuarioPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));
    }

    @Transactional
    @Override
    public void ajustarStock(Long productoId, Long depositoId, int cantidadAjustada, Long usuarioId) {
        // Validamos que la cantidad ajustada no sea 0
        
        validarCantidadPositiva(cantidadAjustada);

        // Buscamos el stock correspondiente al producto y depósito
        Stock stock = stockRepository.obtenerStockPorProductoYDeposito(productoId, depositoId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el stock para el producto con ID: "
                        + productoId + " en el depósito con ID: " + depositoId));

        // Calculamos la nueva cantidad
        int cantidadAnterior = stock.getCantidad();
        int cantidadNueva = cantidadAnterior + cantidadAjustada; // Ajustamos la cantidad según el valor

        // Verificamos si la nueva cantidad es válida (por ejemplo, no puede ser negativa)
        if (cantidadNueva < 0) {
            throw new BadRequestException("La cantidad ajustada no puede dejar el stock en un valor negativo. Stock actual: " + cantidadAnterior);
        }

        // Actualizamos la cantidad de stock
        stock.setCantidad(cantidadNueva);
        stock.setFechaActualizacion(LocalDateTime.now());

        // Guardamos el ajuste en el historial de movimientos de stock
        HistorialStock historialStock = new HistorialStock();
        historialStock.setCantidadAnterior(cantidadAnterior);
        historialStock.setCantidadNueva(cantidadNueva);
        historialStock.setFecha(LocalDateTime.now());
        historialStock.setMotivo("Ajuste de stock");
        historialStock.setTipoMovimiento("ajuste");
        historialStock.setProducto(stock.getProducto());
        historialStock.setDeposito(stock.getDeposito());

        // Relacionamos el historial con el stock y el usuario
        Usuario usuario = obtenerUsuarioPorId(usuarioId);  // Ahora este método funciona correctamente
        historialStock.setUsuario(usuario);
        historialStock.setStock(stock);

        // Guardamos el historial de stock
        historialStockRepository.save(historialStock);

        // Finalmente, guardamos los cambios en el stock
        stockRepository.save(stock);
    }
}

