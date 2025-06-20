package DSW.Veiculos.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import DSW.Veiculos.domain.Veiculo;

public interface IVeiculoDAO extends JpaRepository<Veiculo, Long> {
    Veiculo findByPlaca(String placa);
}
