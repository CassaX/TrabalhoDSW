package DSW.Veiculos.dao;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Loja;

public interface ILojaDAO extends CrudRepository<Loja, Long>{

    @Override
    void deleteById(Long id);

    Loja findById(long id);

    Loja findByCNPJ(String CNPJ);

    @SuppressWarnings("unchecked")
    @Override
    Loja save(Loja loja);

    Loja findByEmail(String email);
    
}
