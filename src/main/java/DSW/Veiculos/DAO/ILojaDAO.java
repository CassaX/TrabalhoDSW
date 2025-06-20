package DSW.Veiculos.DAO;

import java.util.List;

import DSW.Veiculos.domain.Loja;

public interface ILojaDAO {
    void deleteById(Long id);

    Loja findById(long id);

    Loja findByCNPJ(String CNPJ);

    List<Loja> findAll();

    Loja save(Loja loja);

    Loja findByEmail(String email);
}
