package curso.testesunitarios2.repositories;

import curso.testesunitarios2.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
    Optional<Usuario> getUserByEmail(String email);
}
