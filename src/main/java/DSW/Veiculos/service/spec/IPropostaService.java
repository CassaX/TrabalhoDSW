package DSW.Veiculos.service.spec;


import java.util.List;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Proposta;

public interface IPropostaService {

	Proposta buscarPorId(Long id);

	List<Proposta> findByCliente(Cliente cliente);
	
	void salvar(Proposta loja);
}