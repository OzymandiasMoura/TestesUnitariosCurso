package curso.testesunitarios2.services;

import curso.testesunitarios2.dominio.Usuario;
import curso.testesunitarios2.dominio.builders.UsuarioBuilder;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import curso.testesunitarios2.infra.UsuarioMemoryRepository;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

//Essa notação permite que você ordene os testes, isso pode prevenir alguns erros. O ideal é não precisar disso.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioServiceTest
{
    private final UsuarioService service = new UsuarioService(new UsuarioMemoryRepository());

    //Notação Order define em que posição os teste fica na ordem de execução.
    @Test
    @Order(1)
    @DisplayName("Deve salvar um usuário valido")
    void deveSalvarUsuarioValido()
    {
        Usuario response = service.salvar(UsuarioBuilder.novoUsuario().setId(null).criar());

        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(2L, response.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Deve retornar exceção quando usuário existir no sistema.")
    void deveRejeitarUsuarioExistente()
    {
        ValidationException e = Assertions.assertThrows(ValidationException.class, () ->
        {
            Usuario response = service.salvar(UsuarioBuilder.novoUsuario().setId(null).setEmail("user1@email.com").criar());
        });

        Assertions.assertEquals("Email já cadastrado no sistema.", e.getMessage());
    }
}