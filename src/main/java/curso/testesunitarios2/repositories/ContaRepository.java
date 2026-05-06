package curso.testesunitarios2.repositories;

import curso.testesunitarios2.dominio.Conta;
import curso.testesunitarios2.dominio.Usuario;

import java.util.List;

public interface ContaRepository
{
    Conta salvar(Conta conta);
    List<Conta> obterContasPorUsuario(Long usuarioId);
    void delete(Conta conta);
}
