package DSW.Veiculos.dao;

public class ClienteDAO {

<<<<<<< Updated upstream:src/main/java/DSW/Veiculos/dao/ClienteDAO.java
}
=======

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Cliente;

@SuppressWarnings("unchecked")
public interface IClienteDAO extends CrudRepository<Cliente, Long> {
	
	Cliente findById(long id);

	Cliente findByCPF(String CPF);

    @Override
	List<Cliente> findAll();
	
	@Override
	Cliente save(Cliente cliente);

	@Override
	void deleteById(Long id);
	
    Cliente findByEmail(String email);

}
>>>>>>> Stashed changes:src/main/java/DSW/Veiculos/dao/IClienteDAO.java
