package curso.testesunitarios2.dominio.builders;

import curso.testesunitarios2.dominio.Conta;
import curso.testesunitarios2.dominio.Usuario;

public class ContaBuilder
{
    private Long id;
    private String nome;
    private Usuario usuario;

    private ContaBuilder(){}

    public static ContaBuilder novaConta()
    {
        ContaBuilder builder = new ContaBuilder();
        builder.id = 1L;
        builder.nome = "Conta";
        builder.usuario = UsuarioBuilder.novoUsuario().criar();

        return builder;
    }

    public ContaBuilder setId(Long id)
    {
        this.id = id;
        return this;
    }

    public ContaBuilder setNome(String nome)
    {
        this.nome = nome;
        return this;
    }

    public ContaBuilder setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
        return this;
    }

    public Conta criar()
    {
        return new Conta(id, nome, usuario);
    }


}
