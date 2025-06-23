package DSW.Veiculos.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;

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

    List<Proposta> findByVeiculoLoja(Loja loja);

    List<Proposta> findByVeiculoAndVeiculo_Loja(Veiculo veiculo, Loja loja);

    long countByClienteAndVeiculoAndStatus(Cliente cliente, Veiculo veiculo, String status);

    List<Proposta> findByVeiculo(Veiculo veiculo);

}