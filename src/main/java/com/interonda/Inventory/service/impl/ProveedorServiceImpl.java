package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.exceptions.BadRequestException;
import com.interonda.Inventory.exceptions.ConflictException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.repository.ProveedorRepository;
import com.interonda.Inventory.service.ProveedorService;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final CompraRepository compraRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository, ProductoRepository productoRepository, CompraRepository compraRepository) {
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.compraRepository = compraRepository;
    }

    @Override
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor findById(Long id) {
        return proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El proveedor no existe."));
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void deleteById(Long id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Proveedor crearProveedor(Proveedor proveedor) {

        if (proveedor == null) {
            throw new ResourceNotFoundException("El proveedor no puede ser nulo");
        }

        if (proveedorRepository.existsByNombre(proveedor.getNombre())) {
            throw new ConflictException("Ya existe un proveedor con el nombre: " + proveedor.getNombre());
        }

        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return proveedorGuardado;
    }

    @Transactional
    @Override
    public Proveedor actualizarProveedor(Long id, Proveedor proveedorActualizado) {
        // Validación de entrada
        if (proveedorActualizado == null) {
            throw new IllegalArgumentException("El proveedor a actualizar no puede ser nulo");
        }

        // Buscar el proveedor existente en la base de datos
        Proveedor proveedorExistente = proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el ID: " + id));

        if (!proveedorExistente.getNombre().equals(proveedorActualizado.getNombre()) && proveedorRepository.existsByNombre(proveedorActualizado.getNombre())) {
            throw new ConflictException("Ya existe un proveedor con el nombre: " + proveedorActualizado.getNombre());
        }

        // Actualizar los datos del proveedor
        proveedorExistente.setNombre(proveedorActualizado.getNombre());
        proveedorExistente.setContacto(proveedorActualizado.getContacto());
        proveedorExistente.setDireccion(proveedorActualizado.getDireccion());
        proveedorExistente.setPais(proveedorActualizado.getPais());

        // Actualizar productos si es necesario
        if (proveedorActualizado.getProductos() != null && !proveedorActualizado.getProductos().isEmpty()) {
            proveedorExistente.setProductos(proveedorActualizado.getProductos());
        }

        // Guardar el proveedor actualizado en la base de datos
        Proveedor proveedorActualizadoFinal = proveedorRepository.save(proveedorExistente);

        return proveedorActualizadoFinal;
    }

    @Transactional
    @Override
    public void eliminarProveedor(Long id) {
        // Verificar si el proveedor existe, si no lanzar una excepción
        Proveedor proveedorExistente = proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el ID: " + id));

        // Verificar si el proveedor tiene compras asociadas y lanzar una excepción si es necesario
        if (proveedorExistente.getCompras() != null && !proveedorExistente.getCompras().isEmpty()) {
            throw new ConflictException("No se puede eliminar el proveedor con ID: " + id + " porque tiene compras asociadas.");
        }

        // Verificar si el proveedor tiene productos asociados
        if (proveedorExistente.getProductos() != null && !proveedorExistente.getProductos().isEmpty()) {
            // Desvincular los productos (esto depende de si quieres mantener los productos sin un proveedor)
            proveedorExistente.getProductos().clear();
            proveedorRepository.save(proveedorExistente);
        }

        // Eliminar el proveedor
        proveedorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Proveedor> listarProveedores(Pageable pageable) {

        // Obtener la página de proveedores
        Page<Proveedor> proveedores = proveedorRepository.findAll(pageable);

        return proveedores;
    }

    @Transactional(readOnly = true)
    @Override
    public Proveedor buscarProveedorPorId(Long id) {

        // Buscar el proveedor por ID y lanzar excepción si no se encuentra
        Proveedor proveedor = proveedorRepository.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("Proveedor no encontrado con el ID: " + id);
        });

        return proveedor;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Proveedor> buscarProveedoresPorNombre(String nombre) {
        // Validación de entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            logger.warn("El nombre proporcionado es inválido: {}", nombre);
            throw new IllegalArgumentException("El nombre del proveedor no puede ser nulo o vacío.");
        }

        // Buscar proveedores por nombre
        List<Proveedor> proveedores = proveedorRepository.buscarProveedoresPorNombre(nombre);

        // Manejo de caso en que no se encuentren resultados
        if (proveedores.isEmpty()) {
            logger.info("No se encontraron proveedores con el nombre: {}", nombre);
            throw new ResourceNotFoundException("No se encontraron proveedores con el nombre: " + nombre);
        }

        // Logging de éxito
        logger.info("Se encontraron {} proveedores con el nombre: {}", proveedores.size(), nombre);

        return proveedores;
    }

    @Transactional
    @Override
    public void vincularProductoAProveedor(Long proveedorId, Long productoId) {
        // 1. Verificar si el proveedor y el producto existen
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el ID: " + proveedorId));

        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + productoId));

        // 2. Verificar si el producto ya está vinculado al proveedor
        if (proveedor.getProductos().contains(producto)) {
            throw new ConflictException("El producto ya está vinculado al proveedor con ID: " + proveedorId);
        }

        // 3. Vincular el producto al proveedor
        proveedor.getProductos().add(producto);
        producto.getProveedores().add(proveedor);  // Mantener la relación bidireccional

        // 4. Guardar los cambios en la base de datos
        proveedorRepository.save(proveedor);
    }

    @Transactional
    @Override
    public void desvincularProductoDeProveedor(Long proveedorId, Long productoId) {
        // Verificar si el proveedor existe en la base de datos
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el ID: " + proveedorId));

        // Verificar si el producto existe en la base de datos
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + productoId));

        // Verificar si el producto está vinculado al proveedor
        if (!proveedor.getProductos().contains(producto)) {
            throw new ConflictException("El producto con ID: " + productoId + " no está vinculado al proveedor con ID: " + proveedorId);
        }

        // Desvincular el producto del proveedor
        proveedor.getProductos().remove(producto);

        // Guardar los cambios en la base de datos
        proveedorRepository.save(proveedor);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> obtenerProductosDeProveedor(Long proveedorId) {
        // Verificar si el proveedor existe en la base de datos
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el ID: " + proveedorId));

        // Retornar la lista de productos asociados al proveedor
        return proveedor.getProductos();
    }

    @Transactional
    @Override
    public Compra registrarCompra(Long proveedorId, Compra compra) {
        // Validar que la compra no sea nula
        if (compra == null) {
            throw new BadRequestException("La compra no puede ser nula");
        }

        // Verificar si el proveedor existe en la base de datos
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el ID: " + proveedorId));

        // Asignar el proveedor a la compra
        compra.setProveedor(proveedor);

        // Guardar la compra en la base de datos
        Compra compraRegistrada = compraRepository.save(compra);

        // Log de registro de la compra
        logger.info("Compra registrada con éxito para el proveedor con ID: {}. ID de la compra: {}", proveedorId, compraRegistrada.getId());

        // Retornar la compra registrada
        return compraRegistrada;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Compra> obtenerComprasDeProveedor(Long proveedorId) {
        // 1. Validar que el ID del proveedor no sea nulo
        if (proveedorId == null) {
            throw new IllegalArgumentException("El ID del proveedor no puede ser nulo");
        }

        // 2. Buscar el proveedor en la base de datos
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el ID: " + proveedorId));

        // 3. Obtener la lista de compras asociadas al proveedor
        List<Compra> compras = proveedor.getCompras();

        return compras;
    }
}
