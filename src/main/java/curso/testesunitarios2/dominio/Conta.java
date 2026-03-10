package curso.testesunitarios2.dominio;


import curso.testesunitarios2.dominio.exceptions.ValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Conta
{
    private Long id;
    private String nome;
    private Usuario usuario;

    public Conta(Long id, String nome, Usuario usuario)
    {
        if(nome == null || nome.isEmpty()) throw new ValidationException("Nome da conta é obrigatório");
        if(usuario == null) throw new ValidationException("Usuário da conta é obrigatório");

        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
    }
}
