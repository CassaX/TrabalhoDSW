package DSW.Veiculos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importe
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
    private PasswordEncoder passwordEncoder; // Injete o PasswordEncoder

    @Override
    public void salvar(Cliente cliente) {
        if (cliente.getId() == null || (cliente.getSenha() != null && !cliente.getSenha().startsWith("$2a$"))) {
             cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        }
        dao.save(cliente);
    }

    @Override
    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
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

    // O método editar agora deve receber o BindingResult para adicionar erros de unicidade
    @Override
    public void editar(Cliente cliente, String novoPassword) {
        Cliente clienteExistente = dao.findById(cliente.getId()).orElse(null);

        if (clienteExistente == null) {
            throw new IllegalArgumentException("Cliente não encontrado para edição.");
        }

        // Validação de unicidade para EMAIL (se alterado)
        if (!clienteExistente.getEmail().equals(cliente.getEmail())) {
            Cliente clienteEmail = dao.findByEmail(cliente.getEmail());
            // Se encontrou outro cliente com o mesmo email
            if (clienteEmail != null && !clienteEmail.getId().equals(cliente.getId())) {
                throw new IllegalArgumentException("Email já em Uso."); // Lançar exceção
            }
        }

        // VALIDAÇÃO DE UNICIDADE PARA CPF (se alterado)
        if (!clienteExistente.getCPF().equals(cliente.getCPF())) {
             Cliente clienteCPF = dao.findByCPF(cliente.getCPF());
             // Se encontrou outro cliente com o mesmo CPF
             if (clienteCPF != null && !clienteCPF.getId().equals(cliente.getId())) {
                 throw new IllegalArgumentException("CPF já em Uso."); // Lançar exceção
             }
        }

        // ATUALIZAÇÃO DOS CAMPOS NÃO-SENHA
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setTelefone(cliente.getTelefone());
        clienteExistente.setSexo(cliente.getSexo());
        clienteExistente.setDataNascimento(cliente.getDataNascimento());
        clienteExistente.setCPF(cliente.getCPF());

        // Lógica para atualização da nova senha (se fornecida)
        if (novoPassword != null && !novoPassword.trim().isEmpty()) {
            clienteExistente.setSenha(passwordEncoder.encode(novoPassword)); // Codifica a nova senha
        }
        // Se novoPassword é nulo/vazio, a senha existente (já hashed) de clienteExistente é mantida.

        dao.save(clienteExistente);
    }
}