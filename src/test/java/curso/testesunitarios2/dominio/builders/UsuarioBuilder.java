package curso.testesunitarios2.dominio.builders;

import curso.testesunitarios2.dominio.Usuario;

public class UsuarioBuilder
{
    //Atributos do usuário.
    private Long id;
    private String nome;
    private String email;
    private String senha;

    //Construtor deve ser privado para não ser usado;
    private UsuarioBuilder(){}

    //Essa é a construção de um padrão de design chamado builder. Ele tem a função de realizar o intermédio na criação de uma entidade de maneira legível e fácil. E dessa vez irei usar como facilitador para criar os testes.
    public static UsuarioBuilder novoUsuario()
    {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.id = 1L;
        builder.nome = "Usuário";
        builder.email = "user@email.com";
        builder.senha = "123456";

        return builder;
    }

    //O retorno da própria entidade vai permitir que eu construa de maneira encadeada os valores. Facilitando a leitura. Devo fazer o mesmo para todos os atributos de usuário.
    public UsuarioBuilder setId(Long param)
    {
        id = param;
        return this;
    }

    public UsuarioBuilder setNome(String param)
    {
        nome = param;
        return this;
    }

    public UsuarioBuilder setEmail(String param)
    {
        email = param;
        return this;
    }

    public UsuarioBuilder setSenha(String param)
    {
        senha = param;
        return this;
    }

    // Este method final vai permitir o retorno de um usuário. Então dessa vez eu retorno a entidade.
    public Usuario criar()
    {
        return new Usuario(id, nome, email, senha);
    }
}
