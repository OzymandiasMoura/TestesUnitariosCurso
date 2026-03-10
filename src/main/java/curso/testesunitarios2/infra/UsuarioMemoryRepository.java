package curso.testesunitarios2.infra;

import curso.testesunitarios2.dominio.Usuario;
import curso.testesunitarios2.repositories.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioMemoryRepository implements UsuarioRepository
{
    private List<Usuario> users;
    private Long currentId;

    public UsuarioMemoryRepository()
    {
        currentId = 0L;
        users = new ArrayList<>();
        salvar(new Usuario(null, "User1", "user1@email.com", "123456"));
    }

    @Override
    public Usuario salvar(Usuario usuario)
    {
        Usuario newUser = new Usuario(nextId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha());
        users.add(newUser);
        return newUser;
    }

    @Override
    public Optional<Usuario> getUserByEmail(String email)
    {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    private Long nextId()
    {
        return ++currentId;
    }
}
