package DSW.Veiculos.domain;

import java.util.List;

public interface ILojaDAO {
    void deleteById(Long id);

    Loja findById(long id);

    Loja findByCNPJ(String CNPJ);

    List<Loja> findAll();

    Loja save(Loja loja);
}
