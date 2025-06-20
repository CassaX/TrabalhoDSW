package DSW.Veiculos.dao;

import org.springframework.data.repository.CrudRepository;

import DSW.Veiculos.domain.Loja;

<<<<<<< Updated upstream:src/main/java/DSW/Veiculos/DAO/ILojaDAO.java
public interface ILojaDAO {
=======
public interface ILojaDAO extends CrudRepository<Loja, Long>{

    @Override
    void deleteById(Long id);

>>>>>>> Stashed changes:src/main/java/DSW/Veiculos/dao/ILojaDAO.java
    Loja findById(long id);

    Loja findByCNPJ(String CNPJ);

    @SuppressWarnings("unchecked")
    @Override
    Loja save(Loja loja);

<<<<<<< Updated upstream:src/main/java/DSW/Veiculos/DAO/ILojaDAO.java
    void deleteById(Long id);
=======
    Loja findByEmail(String email);
    
>>>>>>> Stashed changes:src/main/java/DSW/Veiculos/dao/ILojaDAO.java
}
