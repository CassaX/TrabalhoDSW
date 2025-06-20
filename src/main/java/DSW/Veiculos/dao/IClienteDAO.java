package DSW.Veiculos.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Cliente;

@SuppressWarnings("unchecked")
public interface IClienteDAO extends CrudRepository<Cliente, Long> {
	
	Cliente findById(long id);

	Cliente findByCPF(String CPF);

    @SuppressWarnings("null")
	@Override
	List<Cliente> findAll();
	
	@SuppressWarnings("null")
	@Override
	Cliente save(Cliente cliente);

	@Override
	void deleteById(Long id);
	
    Cliente findByEmail(String email);

}
