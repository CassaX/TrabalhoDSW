package DSW.Veiculos.DAO;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Veiculo;

@SuppressWarnings("unchecked")
public interface IVeiculoDAO extends CrudRepository<Veiculo, Long>{

	
	Optional<Veiculo> findById(long id);

    @Override
	List<Veiculo> findAll();
	
    @Override
	Veiculo save(Veiculo veiculo);

	@Override
	void deleteById(Long id);

	List<Veiculo> findByLoja(Loja loja);
    
    List<Veiculo> findByModelo(String modelo);
}