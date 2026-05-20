package curso.testesunitarios2.services;

import curso.testesunitarios2.dominio.Conta;
import curso.testesunitarios2.dominio.Usuario;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import curso.testesunitarios2.events.ContaEvent;
import curso.testesunitarios2.repositories.ContaRepository;
import java.util.List;

public class ContaService
{
    private ContaRepository repository;
    private ContaEvent event;

    public ContaService(ContaRepository repository, ContaEvent event)
    {
        this.repository = repository;
        this.event = event;
    }

    public Conta salvar(Conta conta)
    {
        List<Conta> c = repository.obterContasPorUsuario(conta.getUsuario().getId());
        if (!(c.isEmpty()))
        {
            throw new ValidationException("Usuário já possui uma conta.");
        }
        Conta contaSalva = repository.salvar(conta);
        try{
            event.dispatch(contaSalva, ContaEvent.EventType.CREATED);
        }
        catch (Exception ex){
            repository.delete(contaSalva);
            throw new ValidationException("Conta não foi salva com sucesso.");

        }

        return contaSalva;
    }
}
