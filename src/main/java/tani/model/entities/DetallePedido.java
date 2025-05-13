package tani.model.entities;
import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter @Builder
@NoArgsConstructor
@ToString @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DetallePedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id_detalle;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_talla_id")
    private ProductoTalla productoTalla;

    private int cantidad;
    private float precio_unitario;
    private float subtotal;

}