package curso.testesunitarios2.dominio;

import curso.testesunitarios2.dominio.builders.ContaBuilder;
import curso.testesunitarios2.dominio.builders.UsuarioBuilder;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class ContaTest
{
    @Test
    @DisplayName("Deve criar uma conta valida")
    public void deveCriarContaValida()
    {
        //Criar conta
        Conta conta = ContaBuilder.novaConta().criar();
        //Assertivas para conta
        assertAll("Conta",
            () -> assertEquals(1L, conta.getId()),
            () -> assertEquals("Conta", conta.getNome()),
            () -> assertSame(UsuarioBuilder.novoUsuario().criar().getId(), conta.getUsuario().getId())
        );
    }

    //methodSource referencia um method para a entrada de dados.
    @DisplayName("Deve gerar um ValidationException")
    @ParameterizedTest(name = "{index} - {3}")
    @MethodSource(value = "dataProvider")
    public void deveCriarContaInvalida(Long id, String nome, Usuario usuario, String message)
    {
        ValidationException e = assertThrows(ValidationException.class, () ->
        {
            Conta conta = ContaBuilder.novaConta().setId(id).setNome(nome).setUsuario(usuario).criar();
        });

        assertEquals(message, e.getMessage());
    }

    //Method para permitir a entrada de dados. Ele deve ser static.
    private static Stream<Arguments> dataProvider()
    {
        return Stream.of(
            Arguments.of(1L, null, UsuarioBuilder.novoUsuario().criar(), "Nome da conta é obrigatório"),
            Arguments.of(1L, "Conta", null, "Usuário da conta é obrigatório")
        );
    }
}
