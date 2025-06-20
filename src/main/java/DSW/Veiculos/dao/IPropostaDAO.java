package DSW.Veiculos.dao;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Cliente;

@SuppressWarnings("unchecked")
public interface IPropostaDAO extends CrudRepository<Proposta, Long> {
    
    Proposta findById(long id);

    List<Proposta> findAll();

    Proposta save(Proposta proposta);

    void deleteById(Long id);

    List<Proposta> findByCliente(Cliente cliente);
    
    List<Proposta> findByStatus(String status);
    
}
