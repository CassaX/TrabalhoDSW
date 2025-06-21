package DSW.Veiculos.service.spec;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import DSW.Veiculos.domain.Veiculo;

@Service
public interface IVeiculoService {

    void salvar(Veiculo veiculo);

    List<Veiculo> buscarTodos();

    Optional<Veiculo> buscarPorId(Long id);

    void excluir(Long id);
    
}
