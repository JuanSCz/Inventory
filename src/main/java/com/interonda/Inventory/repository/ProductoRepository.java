package com.interonda.Inventory.repository;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // consulta JPQL que busca productos cuyo nombre contenga una coincidencia parcial, sin importar mayúsculas o minúsculas.
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);

    // consulta JPQL que busca productos asociados a una categoría específica mediante su ID.
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :idCategoria")
    List<Producto> buscarPorCategoria(@Param("idCategoria") Long idCategoria);

    //consulta JPQL que filtra aquellos productos que estén vinculados a un proveedor específico.
    @Query("SELECT p FROM Producto p JOIN p.proveedores prov WHERE prov.id = :idProveedor")
    List<Producto> buscarPorProveedor(@Param("idProveedor") Long idProveedor);

    //consulta JPQL que obtiene todos los productos cuyo stockActual esté por debajo de stockMinimo
    @Query("SELECT p FROM Producto p WHERE p.stockActual < p.stockMinimo")
    List<Producto> productosBajoStockMinimo();

    // Obtener productos por ID de categoría
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :idCategoria")
    List<Producto> obtenerProductosPorCategoria(@Param("idCategoria") Long idCategoria);

    // Contar productos por ID de categoría
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria.id = :idCategoria")
    Long contarProductosPorCategoria(@Param("idCategoria") Long idCategoria);

    // Obtener categorías con productos bajo stock mínimo
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.stockActual < p.stockMinimo")
    List<Categoria> categoriasConProductosBajoStockMinimo();
}