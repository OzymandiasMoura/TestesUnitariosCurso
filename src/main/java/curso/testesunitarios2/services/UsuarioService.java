package curso.testesunitarios2.services;

import curso.testesunitarios2.dominio.Usuario;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import curso.testesunitarios2.repositories.UsuarioRepository;
import curso.testesunitarios2.services.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService
{
    UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository)
    {
        this.repository = repository;
    }

    public Usuario salvar(Usuario usuario)
    {
        repository.getUserByEmail(usuario.getEmail()).ifPresent(user ->
        {
            throw new ValidationException("Email já cadastrado no sistema.");
        });

        return repository.salvar(usuario);
    }

    public Usuario getUserByEmail(String email)
    {
        return repository.getUserByEmail(email).orElseThrow(() -> new UserNotFound("Usuário não encontrado."));
    }
}
