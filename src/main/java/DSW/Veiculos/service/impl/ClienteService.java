package DSW.Veiculos.service.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DSW.Veiculos.DAO.IClienteDAO;
import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.service.spec.IClienteService;

@Service
@Transactional(readOnly = false)
public class ClienteService implements IClienteService {

	@Autowired
	IClienteDAO dao;

	@Override
	public void salvar(Cliente cliente) {
		dao.save(cliente);
	}

	@Override
	public void excluir(Long id) {
		dao.deleteById(id);
	}

	@Transactional(readOnly = true)
    @Override
	public Cliente buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Transactional(readOnly = true)
    @Override
	public List<Cliente> buscarTodos() {
		return dao.findAll();
	}
}