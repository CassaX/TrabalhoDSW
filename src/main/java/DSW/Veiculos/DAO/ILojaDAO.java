package DSW.Veiculos.DAO;

import java.util.List;

import DSW.Veiculos.domain.Loja;

public interface ILojaDAO {
    Loja findById(long id);

    Loja findByCNPJ(String CNPJ);

    List<Loja> findAll();

    Loja save(Loja loja);

    void deleteById(Long id);
}
