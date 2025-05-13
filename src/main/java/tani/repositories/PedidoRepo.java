package tani.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tani.model.entities.Pedido;
import tani.model.entities.Usuario;
import tani.model.enums.ESTADO;

import java.util.List;

@Repository
public interface PedidoRepo extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuario(Usuario usuario); // Obtener pedidos de un usuario

    @Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.detalles")
    List<Pedido> findAllWithDetalles();

    @Query("SELECT p FROM Pedido p WHERE p.usuario = :usuario AND p.estado = :estado")
    Pedido findByUsuarioAndEstado(@Param("usuario") Usuario usuario, @Param("estado") ESTADO estado);

}
