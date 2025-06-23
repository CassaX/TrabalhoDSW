package DSW.Veiculos.service.spec;

import java.util.List;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;

public interface IPropostaService {
    void salvar(Proposta proposta);
    Proposta buscarPorId(Long id);
    List<Proposta> findByCliente(Cliente c);
    List<Proposta> buscarPorLoja(Loja loja); 
    void editar(Proposta propostaExistente);
    
    List<Proposta> findByVeiculo(Veiculo veiculo); 
    boolean existePropostaAbertaParaClienteEVeiculo(Cliente cliente, Veiculo veiculo);
}