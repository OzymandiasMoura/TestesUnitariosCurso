package curso.testesunitarios2.services;

import curso.testesunitarios2.dominio.Conta;
import curso.testesunitarios2.dominio.Transacao;
import curso.testesunitarios2.dominio.builders.ContaBuilder;
import curso.testesunitarios2.dominio.builders.TransacaoBuilder;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import curso.testesunitarios2.repositories.TransacaoDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest
{
    @InjectMocks private TransacaoService service;
    @Mock private TransacaoDAO dao;

    @Test
    @DisplayName("Deve salvar a transação com sucesso")
    void deveSalvarTransacao()
    {
        Transacao transacaoParaSalvar = TransacaoBuilder.novaTransacao().setId(null).criar();
        Transacao transacaoPersistida = TransacaoBuilder.novaTransacao().criar();
        Mockito.when(dao.salvar(transacaoParaSalvar)).thenReturn(transacaoPersistida);

        Transacao transacaoSalva = service.salvar(transacaoParaSalvar);

        assertNotNull(transacaoSalva.getId());
        assertEquals(transacaoPersistida, transacaoSalva);
        //Encadeamento de assertivas usando o assertAll
        assertAll("Atributos da Transação",
                () -> assertEquals(1L, transacaoSalva.getId()),
                () -> assertEquals("Transação Criada", transacaoSalva.getDescricao()),
                () -> assertEquals(0.0, transacaoSalva.getValor()),
                () -> {
                    assertAll("Atributos da Conta",
                            () -> assertEquals(1l, transacaoSalva.getConta().getId()),
                            () -> assertEquals("Conta", transacaoSalva.getConta().getNome()),
                            () -> {
                                assertAll("Atributos do Usuario",
                                        () -> assertEquals("Usuário", transacaoSalva.getConta().getUsuario().getNome())
                                        );
                            }
                            );
                }
                );
    }

    @ParameterizedTest
    @MethodSource(value = "camposObrigatorios")
    @DisplayName("Deve validar atributos obrigatórios ao salvar")
    public void deveValidarAtributosObrigatoriosSalvar(Long id, String descricao, Double valor, Conta conta, LocalDate data, Boolean status, String message)
    {
        ValidationException exception = assertThrows(ValidationException.class, () ->
        {
            Transacao transacaoParaSalvar =
                    TransacaoBuilder.novaTransacao().setId(id).setDescricao(descricao).setValor(valor).setData(data).setConta(conta).setStatus(status).criar();

            service.salvar(transacaoParaSalvar);
        });

        assertEquals(message, exception.getMessage());
    }

    static Stream<Arguments> camposObrigatorios()
    {
        return Stream.of(
                Arguments.of(1L, null, 10D, ContaBuilder.novaConta().criar(), LocalDate.now(),  true, "Descrição obrigatória"),
                Arguments.of(1L, "Transação Criada", null, ContaBuilder.novaConta().criar(), LocalDate.now(), true, "Valor é obrigatorio"),
                Arguments.of(1L, "Transação Criada", 10D, ContaBuilder.novaConta().criar(), null, true, "Data é obrigatorio"),
                Arguments.of(1L, "Transação Criada", 10D, null, LocalDate.now(), true, "Conta é obrigatoria")
        );
    }
}