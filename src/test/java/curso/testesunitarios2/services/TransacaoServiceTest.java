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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// @EnabledIf(value = "isHoraValida") condicionar a execução do teste a alguma condição.
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

        LocalDateTime dataDesejada =  LocalDateTime.of(2026, 1, 1, 4,0);

        //Isso é necessário para criar mock de methods static
        //Poderia criar uma interface entre meu código e o LocalDateTime para que eu possa chamar da maneira normal e usar o mock do jeito normal. Ou até mesmo methods normais, mas que atuem como interface para a função estática
        try(MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class))
        {
            ldt.when(LocalDateTime::now).thenReturn(dataDesejada);

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
    }

    @ParameterizedTest
    @MethodSource(value = "camposObrigatorios")
    @DisplayName("Deve validar atributos obrigatórios ao salvar")
    public void deveValidarAtributosObrigatoriosSalvar(Long id, String descricao, Double valor, Conta conta, LocalDate data, Boolean status, String message)
    {
        LocalDateTime dataDesejada =  LocalDateTime.of(2026, 1, 1, 4,0);

        try(MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class))
        {
            ldt.when(LocalDateTime::now).thenReturn(dataDesejada);

            ValidationException exception = assertThrows(ValidationException.class, () ->
            {
                Transacao transacaoParaSalvar =
                        TransacaoBuilder.novaTransacao().setId(id).setDescricao(descricao).setValor(valor).setData(data).setConta(conta).setStatus(status).criar();

                service.salvar(transacaoParaSalvar);
            });
            //Posso verificar a quantidade de chamadas do static method.
            ldt.verify(LocalDateTime::now);
            assertEquals(message, exception.getMessage());
        }
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
//    É uma maneira de condicionar a execução dos testes
//    public boolean isHoraValida()
//    {
//        return LocalDateTime.now().getHour() < 17;
//    }

    @Test
    @DisplayName("Deve alterar o status para false quando vier nulo")
    public void deveAlterarStatusParaFalseQuandoVinerNulo()
    {
        LocalDateTime dataDesejada =  LocalDateTime.of(2026, 1, 1, 4,0);

        try(MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class)){
            ldt.when(LocalDateTime::now).thenReturn(dataDesejada);

            Transacao t = TransacaoBuilder.novaTransacao().setStatus(null).criar();

            Mockito.when(dao.salvar(Mockito.any())).thenReturn(t);

            Transacao resutado = service.salvar(t);

            assertNotNull(resutado);
            assertEquals(false, resutado.getStatus());
            ldt.verify(LocalDateTime::now);
        }
    }

    @Test
    @DisplayName("Deve rejeitar transação fora de hora")
    public void deveRejeitarTransacaoForaHora()
    {
        LocalDateTime dataDesejada =  LocalDateTime.of(2026, 1, 1, 18,0);

        try(MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class))
        {
            ldt.when(LocalDateTime::now).thenReturn(dataDesejada);

            Transacao t = TransacaoBuilder.novaTransacao().criar();
            RuntimeException resutado = assertThrows(RuntimeException.class, () -> {service.salvar(t);});

            assertEquals("Tente novamente amanhã", resutado.getMessage());
        }
    }
}