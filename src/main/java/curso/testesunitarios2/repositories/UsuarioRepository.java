package curso.testesunitarios2.repositories;

import curso.testesunitarios2.dominio.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository
{
    Usuario salvar(Usuario usuario);
    Optional<Usuario> getUserByEmail(String email);
}
