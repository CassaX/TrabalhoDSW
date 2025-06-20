package DSW.Veiculos.dao;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import DSW.Veiculos.domain.Cliente;

@SuppressWarnings("unchecked")
public interface ClienteDAO extends CrudRepository<Cliente, Long> {
	
	Cliente findById(long id);

	Client findByCPF(String CPF);

	List<Cliente> findAll();
	
	Cliente save(Cliente cliente);

	void deleteById(Long id);
	
    Cliente findByEmail(String email);

}