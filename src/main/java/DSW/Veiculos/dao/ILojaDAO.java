package DSW.Veiculos.dao;

import java.util.List;

import DSW.Veiculos.domain.Loja;

public interface ILojaDAO extends CrudRepository<Cliente, Long>{

    void deleteById(Long id);

    Loja findById(long id);

    Loja findByCNPJ(String CNPJ);

    List<Loja> findAll();

    Loja save(Loja loja);

    Cliente findByEmail(String email);
    
}
