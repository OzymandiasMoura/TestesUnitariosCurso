package curso.testesunitarios2.services;

import curso.testesunitarios2.dominio.Usuario;
import curso.testesunitarios2.dominio.builders.UsuarioBuilder;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import curso.testesunitarios2.repositories.UsuarioRepository;
import curso.testesunitarios2.services.exceptions.UserNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest
{
    //Essas anotações permitem que você tire aquela configuração de dentro do before each.
    @Mock
    UsuarioRepository repository;
    @InjectMocks
    private UsuarioService service;

//    @BeforeEach
//    void setUp()
//    {
//        //Se não fosse por notations, dessas são as linhas que deveriam ser digitadas
//        //repository = Mockito.mock(UsuarioRepository.class);
//        //service = new UsuarioService(repository);
//
//        //Mas para liberar as notations é necessário fazer isso. Pois isso vai realizar a ação de instanciar um service com o repository com o mock
//        MockitoAnnotations.openMocks(this);
//    }

    @AfterEach
    void tearDown()
    {
        //Apenas se for do interesse garantir a verificação, e amarrar o código inteiro
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando retornar vazia a busca por email")
    void deveLancarExcecaoQuandoEmailNaoExistir()
    {

        Mockito.when(repository.getUserByEmail("mail@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> service.getUserByEmail("mail@email.com"));

        Mockito.verify(repository).getUserByEmail("mail@email.com");

    }

    @Test
    @DisplayName("Deve retornar usuário com email valido")
    void deveRetornarUsuarioComEmailValido()
    {
        Mockito.when(repository.getUserByEmail("user@email.com")).thenReturn(Optional.of(UsuarioBuilder.novoUsuario().criar()));

        Usuario response = service.getUserByEmail("user@email.com");

        assertNotNull(response);
        assertEquals("user@email.com", response.getEmail());

        //Esse method permite que verifique quantas vezes algum outro method foi invocado.
        Mockito.verify(repository, Mockito.times(1)).getUserByEmail("user@email.com");
        //Pelo menos uma certa quantidade de vezes.
        //Mockito.verify(usuarioRepository, Mockito.atLeast(1)).getUserByEmail("user@email.com");
        //Garantir que essa chamada nunca ocorreu
        //Mockito.verify(usuarioRepository, Mockito.never()).getUserByEmail("user1@email.com");
        //Verifica se não houve nenhuma interação a mais do esperado
        //Mockito.verifyNoMoreInteractions(usuarioRepository);

        //Repetições em Mock
        //Mockito.when(usuarioRepository.getUserByEmail("user@email.com")).thenReturn(primeira@email.com).thenReturn(outras@email.com);
        //Usuario response = service.getUserByEmail("user@email.com"); retorna primeira@email.com
        //Usuario response = service.getUserByEmail("user@email.com"); retorna outras@email.com
        //Usuario response = service.getUserByEmail("user@email.com"); retorna outras@email.com
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void deveSalvarUsuarioComSucesso()
    {
        Usuario usuario = UsuarioBuilder.novoUsuario().setId(null).criar();

        Mockito.when(repository.getUserByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        Mockito.when(repository.save(usuario)).thenReturn(UsuarioBuilder.novoUsuario().criar());

        Usuario response = service.salvar(usuario);

        assertNotNull(response.getId());

        //Usar dessa forma garante que os metodos foram chamados e usados dentro do código. É importante para garantir a integridade do código, forçando a realizar a validação. Se o metodo não for chamado, ele vai falhar o teste
        Mockito.verify(repository).getUserByEmail(usuario.getEmail());
        Mockito.verify(repository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção quanto email estiver no sistema.")
    void excecaoQuandoEmailEstiverNoSistema()
    {
        //Tentando criar usuario novo
        Usuario usuario = UsuarioBuilder.novoUsuario().setId(null).criar();
        //Mock da busca do repositório. Criei o retorno de um usuario com o mesmo email
        Mockito.when(repository.getUserByEmail(usuario.getEmail())).thenReturn(Optional.of(UsuarioBuilder.novoUsuario().criar()));

        //Confirmei se a exceção foi lançada, e a classe dela.
        ValidationException exception = assertThrows(ValidationException.class, () -> service.salvar(usuario));

        //Confirmei se a exceção tem a mensagem correta.
        assertEquals("Email já cadastrado no sistema.", exception.getMessage());

        //Verifiquei que o metodo que realmente salva o usuario nunca foi chamado
        Mockito.verify(repository, Mockito.never()).save(usuario);
    }
}