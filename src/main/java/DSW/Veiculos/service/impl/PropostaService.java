package DSW.Veiculos.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.DAO.IPropostaDAO;
import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.service.spec.IPropostaService;

@Service
@Transactional(readOnly = false)
public class PropostaService implements IPropostaService {

	@Autowired
	IPropostaDAO dao;
	
	public void salvar(Proposta proposta) {
		dao.save(proposta);
	}

	@Transactional(readOnly = true)
	public Proposta buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Transactional(readOnly = true)
	public List<Proposta> findByCliente(Cliente c) {
        return dao.findByCliente(c);
    }
}