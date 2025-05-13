package tani.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tani.model.entities.Usuario;
import tani.model.enums.TIPO_USUARIO;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByTipoUsuario(TIPO_USUARIO tipo_usuario);
}
