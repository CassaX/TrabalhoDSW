package DSW.Veiculos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.DAO.IPropostaDAO;
import DSW.Veiculos.DAO.IVeiculoDAO;
import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.service.spec.IPropostaService;
import DSW.Veiculos.service.spec.IVeiculoService;

public abstract class VeiculoService implements IVeiculoService{
    @Autowired
    IVeiculoDAO dao;

    public void salvar(Veiculo veiculo) {
		dao.save(veiculo);
	}

	public void excluir(Long id) {
		dao.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Veiculo> buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Transactional(readOnly = true)
	public List<Veiculo> buscarTodos() {
		return dao.findAll();
	}

}
