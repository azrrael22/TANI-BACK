package tani.model.entities;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductoTalla implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private String talla;
    private int cantidad;

    @Builder
    public ProductoTalla(Producto producto, String talla, int cantidad) {
        this.producto = producto;
        this.talla = talla;
        this.cantidad = cantidad;
    }
}
