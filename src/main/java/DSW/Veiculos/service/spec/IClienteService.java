package DSW.Veiculos.service.spec;

import java.util.List;

import DSW.Veiculos.domain.Cliente;

public interface IClienteService {

	Cliente buscarPorId(Long id);

	List<Cliente> buscarTodos();

	void salvar(Cliente cliente);

	void excluir(Long id);

	Cliente buscarPorEmail(String email);

	void editar(Cliente cliente, String novoPassword);
}
