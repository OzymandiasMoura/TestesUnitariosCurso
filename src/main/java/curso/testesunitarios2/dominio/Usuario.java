package curso.testesunitarios2.dominio;

import curso.testesunitarios2.dominio.exceptions.ValidationException;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

//Isso é um dominio rico por ter níveis maiores de abstração como a capacidade de se validar.
//A classe está imutável por isso apenas o Getter sem o Setter
@NoArgsConstructor
@Getter
public class Usuario
{
    private Long id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(Long id, String nome, String email, String senha)
    {
        //Validação aqui faz com que essa entidade seja validável.
        if (nome == null || nome.isEmpty()) throw new ValidationException("Nome é obrigatório");
        if (email == null || email.isEmpty()) throw new ValidationException("Email é obrigatório");
        if (senha == null || senha.isEmpty()) throw new ValidationException("Senha é obrigatória");

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public boolean equals(Object usuario)
    {
        if(this == usuario) return true;
        if(usuario == null || getClass() != usuario.getClass()) return false;

        Usuario outro = (Usuario) usuario;
        return Objects.equals(email, outro.email) && Objects.equals(senha, outro.senha) && Objects.equals(nome, outro.nome);
    }

    public int hashCode()
    {
        return Objects.hash(email, senha, nome);
    }
}
