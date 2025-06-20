<<<<<<< HEAD:src/main/java/DSW/Veiculos/dao/IClienteDAO.java
package DSW.Veiculos.dao;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
=======
package DSW.Veiculos.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
>>>>>>> origin/main:src/main/java/DSW/Veiculos/DAO/IClienteDAO.java

import DSW.Veiculos.domain.Cliente;

@SuppressWarnings("unchecked")
<<<<<<< HEAD:src/main/java/DSW/Veiculos/dao/IClienteDAO.java
public interface ClienteDAO extends CrudRepository<Cliente, Long> {
	
	Cliente findById(long id);

	List<Cliente> findAll();
	
	Cliente save(Cliente cliente);

	void deleteById(Long id);
	
    @Query("SELECT u FROM Cliente u WHERE u.username = :username")
    public Cliente getUserByUsername(@Param("username") String username);
}
=======
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
>>>>>>> origin/main:src/main/java/DSW/Veiculos/DAO/IClienteDAO.java
