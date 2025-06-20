package DSW.Veiculos.DAO;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Proposta;

@SuppressWarnings("unchecked")
public interface IPropostaDAO extends CrudRepository<Proposta, Long> {
    
    Proposta findById(long id);

    @SuppressWarnings("null")
    @Override
    List<Proposta> findAll();

    @SuppressWarnings("null")
    @Override
    Proposta save(Proposta proposta);

    @Override
    void deleteById(Long id);

    List<Proposta> findByCliente(Cliente cliente);
    
    List<Proposta> findByStatus(String status);
    
}
