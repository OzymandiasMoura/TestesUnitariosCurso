package curso.testesunitarios2.services;

import curso.testesunitarios2.dominio.Conta;
import curso.testesunitarios2.dominio.builders.ContaBuilder;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import curso.testesunitarios2.events.ContaEvent;
import curso.testesunitarios2.repositories.ContaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest
{
    @InjectMocks
    private ContaService service;
    @Mock
    private ContaRepository repository;
    @Mock
    private ContaEvent event;

    @Test
    @DisplayName("Deve salvar conta com sucesso.")
    void deveSalvarComSucesso()
    {
        Conta contaToSave = ContaBuilder.novaConta().setId(null).criar();

        Mockito.when(repository.obterContasPorUsuario(contaToSave.getUsuario().getId())).thenReturn(List.of());
        Mockito.when(repository.salvar(contaToSave)).thenReturn(ContaBuilder.novaConta().criar());
        Mockito.doNothing().when(event).dispatch(ContaBuilder.novaConta().criar(), ContaEvent.EventType.CREATED);

        Conta response = service.salvar(contaToSave);

        assertNotNull(response.getId());
    }

    @Test
    @DisplayName("Deve rejeitar conta repetida.")
    void deveRejeitarContaRepetido()
    {
        Conta contaToSave = ContaBuilder.novaConta().setId(null).criar();

        Mockito.when(repository.obterContasPorUsuario(contaToSave.getUsuario().getId())).thenReturn(List.of(ContaBuilder.novaConta().criar()));

        ValidationException exception = assertThrows(ValidationException.class, () -> service.salvar(contaToSave));

        assertEquals("Usuário já possui uma conta.", exception.getMessage());

    }

    @Test
    @DisplayName("Deve deletar ao falhar o evento.")
    void deveDeletarSeEventoFalha()
    {
        Conta contaToSave = ContaBuilder.novaConta().setId(null).criar();
        Conta contaSalva  = ContaBuilder.novaConta().criar();

        Mockito.when(repository.obterContasPorUsuario(contaToSave.getUsuario().getId())).thenReturn(List.of());
        Mockito.when(repository.salvar(Mockito.any(Conta.class))).thenReturn(contaSalva);
        Mockito.doThrow(new ValidationException("Conta não foi salva com sucesso.")).when(event).dispatch(contaSalva, ContaEvent.EventType.CREATED);

        String message = assertThrows(ValidationException.class, () -> service.salvar(contaToSave)).getMessage();

        assertEquals("Conta não foi salva com sucesso.",  message);

        Mockito.verify(repository, Mockito.times(1)).delete(contaSalva);
    }
}