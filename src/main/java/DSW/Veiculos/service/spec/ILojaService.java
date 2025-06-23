package DSW.Veiculos.service.spec;

import java.util.List;

import DSW.Veiculos.domain.Loja;

public interface  ILojaService {

    Loja buscarPorId(Long id);
    Loja buscarPorCNPJ(String cnpj);
    Loja buscarPorEmail(String email);
    List<Loja> buscarTodos();
    Loja salvar(Loja loja);
    Loja editar(Loja loja);
    void excluir(Long id);
}
