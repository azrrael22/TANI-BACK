package tani.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tani.model.entities.Producto;
import tani.model.entities.ProductoTalla;

import java.util.List;

@Repository
public interface ProductoTallaRepo extends JpaRepository<ProductoTalla, Integer> {
    List<ProductoTalla> findByProducto(Producto producto);
    List<ProductoTalla> findByTalla(String talla);

}