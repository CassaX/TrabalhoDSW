package DSW.Veiculos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DSW.Veiculos.DAO.ILojaDAO;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.service.spec.ILojaService;

@Service
@Transactional
public class LojaService implements ILojaService {

    @Autowired
    private ILojaDAO lojaDAO;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    
	@Transactional(readOnly = true)
    @Override
    public Loja buscarPorId(Long id) {
        return lojaDAO.findById(id).orElse(null); // Usando orElse(null) para Optional
    }

    @Override
    public Loja buscarPorCNPJ(String cnpj) {
        return lojaDAO.findByCNPJ(cnpj);
    }

    @Override
    public Loja buscarPorEmail(String email) {
        return lojaDAO.findByEmail(email);
    }

    @Override
    public List<Loja> buscarTodos() {
        return lojaDAO.findAll();
    }

    @Override
    public Loja salvar(Loja loja) {

        if (loja.getId() == null || (loja.getPassword() != null && !loja.getPassword().startsWith("$2a$"))) {
            loja.setPassword(passwordEncoder.encode(loja.getPassword())); // Codifica aqui
        }
        loja.setEnabled(true);
        loja.setRole("LOJA"); // Certifique-se que o role é definido aqui ou no controller de registro
        
        return lojaDAO.save(loja);
    }

    @Override
    public Loja editar(Loja loja, String novaSenha) {
        Loja lojaExistente = buscarPorId(loja.getId());
        
        if (lojaExistente == null) {
            throw new IllegalArgumentException("Loja não encontrada.");
        }
        
        // Validação de unicidade para EMAIL (se alterado)
        if (!lojaExistente.getEmail().equals(loja.getEmail())) {
            Loja lojaEmailConflito = lojaDAO.findByEmail(loja.getEmail());
            if (lojaEmailConflito != null && !lojaEmailConflito.getId().equals(loja.getId())) {
                throw new IllegalArgumentException("Email Já em Uso.");
            }
        }

        // Validação: CNPJ NÃO PODE SER ALTERADO.
        // Se o CNPJ que veio do formulário for diferente do CNPJ original do banco, lance erro.
        if (!lojaExistente.getCNPJ().equals(loja.getCNPJ())) {
            throw new IllegalArgumentException("CNPJ não pode ser alterado.");
        }
        
        // ATUALIZAÇÃO DA SENHA:
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            lojaExistente.setPassword(passwordEncoder.encode(novaSenha)); // Codifica a nova senha
        }
        // Se novaSenha é nula/vazia, a senha existente (já hashed) de lojaExistente é mantida.

        // ATUALIZAÇÃO DOS OUTROS CAMPOS DA LOJA
        lojaExistente.setNome(loja.getNome());
        lojaExistente.setEmail(loja.getEmail());
        lojaExistente.setDescricao(loja.getDescricao());
        // IMPORTANTE: NÃO ATUALIZE O CNPJ AQUI, MANTENHA O CNPJ ORIGINAL DO BANCO
        // lojaExistente.setCNPJ(loja.getCNPJ()); // <--- REMOVA OU COMENTE ESTA LINHA

        return lojaDAO.save(lojaExistente);
    }

    @Override
    public void excluir(Long id) {
        Loja loja = buscarPorId(id);
        if (loja == null) {
            throw new IllegalArgumentException("Loja não encontrada para exclusão.");
        }
        // TODO: Adicionar verificação de veículos vinculados antes de excluir
        lojaDAO.delete(loja);
    }

    // Método auxiliar para validação de unicidade (para novos cadastros)
    private void validarDadosUnicos(Loja loja) {
        Loja lojaExistenteCNPJ = lojaDAO.findByCNPJ(loja.getCNPJ());
        if (lojaExistenteCNPJ != null) {
            throw new IllegalArgumentException("CNPJ Já Cadastrado.");
        }
        
        Loja lojaExistenteEmail = lojaDAO.findByEmail(loja.getEmail());
        if (lojaExistenteEmail != null) {
            throw new IllegalArgumentException("Email Já em Uso.");
        }
    }
}