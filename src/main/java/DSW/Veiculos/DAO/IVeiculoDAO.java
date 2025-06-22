package DSW.Veiculos.DAO;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;

@SuppressWarnings("unchecked")
public interface IVeiculoDAO extends CrudRepository<Veiculo, Long>{

	Optional<Veiculo> findById(long id);

	List<Veiculo> findAll();
	
	Veiculo save(Veiculo veiculo);

	void deleteById(Long id);
}