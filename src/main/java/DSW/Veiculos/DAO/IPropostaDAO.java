package DSW.Veiculos.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.StatusProposta;

public interface IPropostaDAO extends CrudRepository<Proposta, Long> {

    boolean existsByClienteAndVeiculoAndStatus(Cliente cliente, Veiculo veiculo, StatusProposta status);
    List<Proposta> findByCliente(Cliente cliente);
    List<Proposta> findByVeiculo(Veiculo veiculo);
    
}
