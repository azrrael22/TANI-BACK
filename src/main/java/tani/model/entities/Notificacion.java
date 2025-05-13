package tani.model.entities;
import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notificacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id_notificacion;

    @ManyToOne
    @JoinColumn(name = "producto_talla_id")
    private ProductoTalla productoTalla;

    private String mensaje;
    private LocalDateTime fecha;
}
