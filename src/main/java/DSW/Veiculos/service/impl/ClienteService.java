package DSW.Veiculos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	@Autowired
	private BCryptPasswordEncoder encoder; // Injetar o PasswordEncoder

	@Override
	public void editar(Cliente cliente, String novoPassword) {
		Cliente clienteExistente = dao.findById(cliente.getId()).orElse(null); // Buscar cliente existente

		if (clienteExistente == null) {
			throw new IllegalArgumentException("Cliente não encontrado para edição.");
		}

		// Atualizar os campos que podem ser editados (nome, email, telefone, sexo, dataNascimento, CPF)
		clienteExistente.setNome(cliente.getNome());
		clienteExistente.setEmail(cliente.getEmail());
		clienteExistente.setTelefone(cliente.getTelefone());
		clienteExistente.setSexo(cliente.getSexo());
		clienteExistente.setDataNascimento(cliente.getDataNascimento()); // Use o nome correto do campo
		clienteExistente.setCPF(cliente.getCPF()); // Se o CPF pode ser alterado, valide a unicidade aqui

		// Lógica para atualização da senha
		if (novoPassword != null && !novoPassword.trim().isEmpty()) {
			clienteExistente.setSenha(encoder.encode(novoPassword));
		}

		dao.save(clienteExistente); // Salvar as alterações
	}

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

	@Override
	public Cliente buscarPorEmail(String email) {
		return dao.findByEmail(email);
	}

}