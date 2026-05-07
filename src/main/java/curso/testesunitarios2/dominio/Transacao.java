package curso.testesunitarios2.dominio;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Transacao
{
    //Esse formato simples chama POJO plain old java object
    private Long id;
    private String descricao;
    private Double valor;
    private Conta conta;
    private LocalDate data;
    private Boolean status;
}
