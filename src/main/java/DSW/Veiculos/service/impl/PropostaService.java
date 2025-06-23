package DSW.Veiculos.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DSW.Veiculos.DAO.IPropostaDAO;
import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.service.spec.IPropostaService;

@Service
@Transactional(readOnly = false)
public class PropostaService implements IPropostaService {

	@Autowired
	IPropostaDAO dao;
	
	@Override
	public void salvar(Proposta proposta) {
		dao.save(proposta);
	}

	@Override
	@Transactional(readOnly = true)
	public Proposta buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Proposta> findByCliente(Cliente c) {
        return dao.findByCliente(c);
    }

	@Transactional(readOnly = true)
    @Override
    public List<Proposta> buscarPorLoja(Loja loja) {
        return dao.findByVeiculoLoja(loja);
    }

	@Override
    @Transactional(readOnly = true)
    public List<Proposta> findByVeiculo(Veiculo veiculo) {
        return dao.findByVeiculo(veiculo);
    }

	@Transactional
    @Override
    public void editar(Proposta propostaExistente) {
        // Verifica se a proposta existe
        if (!dao.existsById(propostaExistente.getId())) {
            throw new IllegalArgumentException("Proposta não encontrada com ID: " + propostaExistente.getId());
        }
        
        // Atualiza a proposta no banco de dados
        dao.save(propostaExistente);
    }

	@Override
    @Transactional(readOnly = true)
    public boolean existePropostaAbertaParaClienteEVeiculo(Cliente cliente, Veiculo veiculo) {
        // Assume que "ABERTO" é o status de uma proposta em análise
        return dao.countByClienteAndVeiculoAndStatus(cliente, veiculo, "ABERTO") > 0;
    }
	
}