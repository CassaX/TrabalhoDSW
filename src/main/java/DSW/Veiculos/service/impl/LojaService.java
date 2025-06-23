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
        return lojaDAO.findById(id.longValue());
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
        validarDadosUnicos(loja);
        
        loja.setPassword(passwordEncoder.encode(loja.getPassword()));
        loja.setEnabled(true);
        loja.setRole("LOJA");
        
        return lojaDAO.save(loja);
    }

    @Override
    public Loja editar(Loja loja, String novaSenha) {
        Loja lojaExistente = buscarPorId(loja.getId());

        if (lojaExistente == null) {
            throw new IllegalArgumentException("Loja não encontrada para edição.");
        }

        if (!lojaExistente.getCNPJ().equals(loja.getCNPJ())) {
            throw new IllegalArgumentException("CNPJ não pode ser alterado");
        }

        if (!lojaExistente.getEmail().equals(loja.getEmail())) {
            Loja lojaEmail = lojaDAO.findByEmail(loja.getEmail());
            if (lojaEmail != null && !lojaEmail.getId().equals(loja.getId())) { 
                throw new IllegalArgumentException("Email já em Uso");
            }
        }

        lojaExistente.setNome(loja.getNome());
        lojaExistente.setEmail(loja.getEmail());

        // Lógica para atualização da senha
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            lojaExistente.setPassword(passwordEncoder.encode(novaSenha));
        }

        return lojaDAO.save(lojaExistente); // Salvar as alterações
    }

    @Override
    public void excluir(Long id) {
        Loja loja = buscarPorId(id);
        lojaDAO.delete(loja);
    }

    private void validarDadosUnicos(Loja loja) {
        Loja lojaExistente = lojaDAO.findByCNPJ(loja.getCNPJ());
        if (lojaExistente != null) {
            throw new IllegalArgumentException("CNPJ Já Cadastrado");
        }
        
        Loja lojaEmail = lojaDAO.findByEmail(loja.getEmail());
        if (lojaEmail != null) {
            throw new IllegalArgumentException("Email Já em Uso");
        }
    }
}