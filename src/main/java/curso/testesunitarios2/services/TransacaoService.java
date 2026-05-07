package curso.testesunitarios2.services;

import curso.testesunitarios2.dominio.Transacao;
import curso.testesunitarios2.dominio.exceptions.ValidationException;
import curso.testesunitarios2.repositories.TransacaoDAO;

public class TransacaoService
{
    private TransacaoDAO dao;
    public Transacao salvar(Transacao transacao)
    {
        if(transacao.getDescricao() == null || transacao.getDescricao().isEmpty()) throw new ValidationException(
                "Descrição obrigatória");
        if(transacao.getValor() == null) throw new ValidationException("Valor é obrigatorio");
        if(transacao.getData() == null) throw new ValidationException("Data é obrigatorio");
        if(transacao.getConta() == null) throw new ValidationException("Conta é obrigatoria");
        if(transacao.getStatus() == null)
        {
            transacao.setStatus(false);
        }

        return dao.salvar(transacao);
    }
}
