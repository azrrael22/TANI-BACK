package tani.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tani.model.entities.Producto;
import tani.model.entities.Usuario;
import tani.model.enums.TIPO_CALZADO;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByTipoCalzado(TIPO_CALZADO tipoCalzado);

    Optional<Producto> findByNombre(String nombre);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.tallas") // Carga las tallas
    List<Producto> findAllWithTallas();

}