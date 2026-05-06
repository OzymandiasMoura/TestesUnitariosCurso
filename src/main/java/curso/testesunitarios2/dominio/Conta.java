package curso.testesunitarios2.dominio;


import curso.testesunitarios2.dominio.exceptions.ValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Getter
public class Conta
{
    private Long id;
    private String nome;
    private Usuario usuario;

    public Conta(Long id, String nome, Usuario usuario)
    {
        if (nome == null || nome.isEmpty()) throw new ValidationException("Nome da conta é obrigatório");
        if (usuario == null) throw new ValidationException("Usuário da conta é obrigatório");

        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conta conta = (Conta) o;
        return Objects.equals(id, conta.id) && Objects.equals(nome, conta.nome) && Objects.equals(usuario, conta.usuario);
    }

    public int hashCode()
    {
        return Objects.hash(id, nome, usuario);
    }
}
