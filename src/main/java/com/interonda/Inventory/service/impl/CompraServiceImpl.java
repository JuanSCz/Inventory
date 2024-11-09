package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.exceptions.BadRequestException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.DetalleCompraRepository;
import com.interonda.Inventory.service.CompraService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;

    public CompraServiceImpl(CompraRepository compraRepository, DetalleCompraRepository detalleCompraRepository) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Compra> findAll() {
        return compraRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Compra findById(Long id) {
        return compraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Transactional
    @Override
    public Compra save(Compra compra) {
        return compraRepository.save(compra);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        compraRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Compra crearCompra(Compra compra, List<DetalleCompra> detalles) {
        // Validar que la lista de detalles no esté vacía
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La compra debe tener al menos un detalle.");
        }

        // Calcular el total de la compra
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCompra detalle : detalles) {
            if (detalle.getCantidad() <= 0) {
                throw new BadRequestException("La cantidad debe ser mayor a cero.");
            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("El precio unitario debe ser mayor a cero.");
            }
            total = total.add(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            detalle.setCompra(compra);  // Asignar la referencia de la compra
        }

        // Establecer el total de la compra
        compra.setTotal(total);

        // Guardar la compra y los detalles
        Compra compraGuardada = compraRepository.save(compra);
        detalleCompraRepository.saveAll(detalles);

        return compraGuardada;
    }

    @Transactional
    @Override
    public Compra actualizarCompra(Long idCompra, Compra compraActualizada, List<DetalleCompra> nuevosDetalles) {
        Compra compraExistente = compraRepository.findById(idCompra).orElseThrow(() -> new ResourceNotFoundException());

        // Eliminar detalles anteriores
        detalleCompraRepository.deleteAll(compraExistente.getDetallesCompra());

        BigDecimal nuevoTotal = BigDecimal.ZERO;
        for (DetalleCompra detalle : nuevosDetalles) {
            if (detalle.getCantidad() <= 0) {
                throw new BadRequestException("La cantidad debe ser mayor a cero.");
            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("El precio unitario debe ser mayor a cero.");
            }

            detalle.setCompra(compraExistente);
            detalleCompraRepository.save(detalle);
            nuevoTotal = nuevoTotal.add(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }

        compraExistente.setTotal(nuevoTotal);
        compraExistente.setFecha(compraActualizada.getFecha());
        compraExistente.setMetodoPago(compraActualizada.getMetodoPago());
        compraExistente.setEstado(compraActualizada.getEstado());
        compraExistente.setImpuestos(compraActualizada.getImpuestos());

        return compraRepository.save(compraExistente);
    }

    @Transactional
    @Override
    public void eliminarCompra(Long idCompra) {
        // Verificar si la compra existe en la base de datos
        Compra compraExistente = compraRepository.findById(idCompra).orElseThrow(() -> new ResourceNotFoundException());

        // Validar el estado de la compra antes de eliminarla
        if ("COMPLETADA".equals(compraExistente.getEstado())) {
            throw new ResourceNotFoundException("No se puede eliminar una compra completada.");
        }

        // Eliminar la compra (los detalles se eliminarán automáticamente debido a orphanRemoval)
        compraRepository.delete(compraExistente);
    }

    @Transactional(readOnly = true)
    @Override
    public Compra buscarPorId(Long idCompra) {
        // Buscar y devolver la compra
        return compraRepository.findById(idCompra).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Transactional(readOnly = true) // Indica que este metodo solo realiza una lectura
    @Override
    public List<Compra> listarCompras() {
        return compraRepository.findAll(); // Retorna todas las compras
    }

    @Transactional(readOnly = true) // Indica que este metodo solo realiza una lectura
    @Override
    public List<Compra> buscarPorProveedor(Long idProveedor) {
        // Buscar compras por el ID del proveedor
        List<Compra> compras = compraRepository.findByProveedorId(idProveedor);
        return compras; // Retornar las compras encontradas
    }

    @Transactional(readOnly = true) // Indica que este metodo solo realiza una lectura
    @Override
    public List<Compra> buscarPorFecha(LocalDate fecha) {
        // Buscar compras por fecha
        List<Compra> compras = compraRepository.findByFecha(fecha);
        return compras; // Retornar las compras encontradas
    }
}
