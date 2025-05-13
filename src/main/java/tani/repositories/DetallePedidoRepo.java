package tani.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tani.dto.pedido.InformacionPedidoDTO;
import tani.model.entities.DetallePedido;
import tani.model.entities.Pedido;
import tani.model.entities.ProductoTalla;

import java.util.List;

@Repository
public interface DetallePedidoRepo extends JpaRepository<DetallePedido, Integer> {
    List<DetallePedido> findByPedido(Pedido pedido); // Obtener detalles de un pedido

    @Query("SELECT pt FROM DetallePedido pt WHERE pt.pedido.id_pedido = :idPedido")
    List<DetallePedido> buscarPorIdPedido(@Param("idPedido") Integer idPedido);



}
