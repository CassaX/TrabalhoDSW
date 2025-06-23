package DSW.Veiculos.service.spec;

import java.util.List;

import org.springframework.stereotype.Service;

import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Veiculo;

@Service
public interface IVeiculoService {

    void salvar(Veiculo veiculo);

    List<Veiculo> buscarTodos();

    Veiculo buscarPorId(Long id);

    void excluir(Long id);

    void editar(Veiculo veiculo);

    Object buscarPorLoja(Loja loja);

    List<Veiculo> buscarPorModelo(String modelo);
    
}
