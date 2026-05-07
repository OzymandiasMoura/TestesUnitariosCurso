package curso.testesunitarios2.dominio.builders;

import curso.testesunitarios2.dominio.Conta;
import curso.testesunitarios2.dominio.Transacao;

import java.time.LocalDate;

public class TransacaoBuilder
{
    private Long id;
    private String descricao;
    private Double valor;
    private Conta conta;
    private LocalDate data;
    private Boolean status;

    private TransacaoBuilder(){}

    public static TransacaoBuilder novaTransacao()
    {
        TransacaoBuilder builder = new TransacaoBuilder();
        builder.id = 1L;
        builder.descricao = "Transação Criada";
        builder.valor = 0.0;
        builder.conta = ContaBuilder.novaConta().criar();
        builder.data = LocalDate.now();
        builder.status = false;

        return builder;
    }

    public TransacaoBuilder setId(Long id)
    {
        this.id = id;
        return this;
    }

    public TransacaoBuilder setDescricao(String descricao)
    {
        this.descricao = descricao;
        return this;
    }

    public TransacaoBuilder setValor(Double valor)
    {
        this.valor = valor;
        return this;
    }

    public TransacaoBuilder setConta(Conta conta)
    {
        this.conta = conta;
        return this;
    }

    public TransacaoBuilder setData(LocalDate data)
    {
        this.data = data;
        return this;
    }

    public TransacaoBuilder setStatus(Boolean status)
    {
        this.status = status;
        return this;
    }

    public Transacao criar()
    {
        Transacao transacao = new Transacao(id, descricao, valor, conta, data, status);
        return  transacao;
    }
}
