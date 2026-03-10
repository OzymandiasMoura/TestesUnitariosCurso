package curso.testesunitarios2.dominio;

import curso.testesunitarios2.dominio.builders.UsuarioBuilder;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

//Importar de maneira estática nesse caso permite que eu não precise invocar a classe toda vez que ela for instanciada. Assim ela é instanciada juntamente com essa classe e posso chamar suas funções quando quiser.
import static org.junit.jupiter.api.Assertions.*;

//Uma maneira de organizar melhor seus testes com nomes mais compreensíveis para depois na hora de rodar.
@DisplayName("Domínio: Usuário")
class UsuarioTest
{
    @Test
    @DisplayName("Deve criar um usuário válido.")
    public void deveCriarUsuarioValido()
    {
        Usuario usuario = UsuarioBuilder.novoUsuario().criar();

        //Uma estratégia alternativa para adotar os testes. Agrupando e executando eles concomitantemente e não em ordem. Por outro lado, você terá dificuldades de saber em qual das linhas estará o erro, pois precisará identificar de maneira apropriada os erros dentro do terminal.
        assertAll("Usuário",
            () -> assertEquals(1, usuario.getId()),
            () -> assertEquals("Usuário", usuario.getNome()),
            () -> assertEquals("user@email.com", usuario.getEmail()),
            () -> assertEquals("123456", usuario.getSenha())
        );
    }

    @Test
    @DisplayName("Deve rejeitar usuário com nome nulo")
    public void deveRejeitarUsuarioSemNome()
    {
        //Uma vez que eu configuro assim, eu não amarro o teste de maneira apropriada, pois ele passaria mesmo se o email viesse nulo.
        ValidationException e = assertThrows(ValidationException.class, () -> {
            Usuario usuario = UsuarioBuilder.novoUsuario().setNome(null).criar();
        });

        //Para amarrar ele de maneira correta, eu preciso adicionar outra assertiva que vai falhar caso o nome venha preenchida. Como no código o erro é especificado na mensagem eu uso ela para fazer essa amarração.
        assertEquals("Nome é obrigatório", e.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar usuário com email nulo")
    public void deveRejeitarUsuarioComEmailNulo()
    {
        ValidationException e = assertThrows(ValidationException.class, () -> {
            Usuario usuario = UsuarioBuilder.novoUsuario().setEmail(null).criar();
        });

        assertEquals("Email é obrigatório", e.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar usuário com senha nula")
    public void deveRejeitarSenhaComEmailNula()
    {
        ValidationException e = assertThrows(ValidationException.class, () -> {
            Usuario usuario = UsuarioBuilder.novoUsuario().setSenha(null).criar();
        });

        assertEquals("Senha é obrigatória", e.getMessage());
    }

    //Esta é uma maneira de agrupar e reduzir os códigos repetitivos dos testes anteriores. O CSVSource permite que se crie testes com variáveis destacadas nessa notação. Eu também nomeei os testes para facilitar a leitura depois.
    @ParameterizedTest(name = "{index} - {4}")
    @CsvSource(value = {
        "1, NULL, user@email.com, 123456, Nome é obrigatório",
        "1, Usuário, NULL, 123456, Email é obrigatório",
        "1, Usuário, user@email.com, NULL, Senha é obrigatória"
    }, nullValues = "NULL")
    public void deveRejeitarInfosNulas(Long id, String nome, String email, String senha, String message)
    {
        //Repare que passei as informações que vão preencher os argumentos com o CSVSource e com isso eu consegui agrupar todos os testes em um só. Cada linha representa um teste distinto. E logo em seguida defini que a string NULL é, na verdade, valor nulo.
        ValidationException e = assertThrows(ValidationException.class, () ->
        {
            Usuario usuario = UsuarioBuilder.novoUsuario().setId(id).setNome(nome).setEmail(email).setSenha(senha).criar();
        });

        assertEquals(message, e.getMessage());
    }
}