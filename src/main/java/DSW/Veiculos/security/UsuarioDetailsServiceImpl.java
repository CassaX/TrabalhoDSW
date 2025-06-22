package DSW.Veiculos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import DSW.Veiculos.DAO.IClienteDAO;
import DSW.Veiculos.DAO.ILojaDAO;
import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Loja;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IClienteDAO clienteDAO;

    @Autowired
    private ILojaDAO lojaDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteDAO.findByEmail(email);
        if (cliente != null) {
            return new ClienteDetails(cliente.getEmail(), cliente.getSenha(), cliente.getRole(), cliente.isEnabled());
        }

        Loja loja = lojaDAO.findByEmail(email);
        if (loja != null) {
            return new ClienteDetails(loja.getEmail(), loja.getPassword(), loja.getRole(), loja.isEnabled());
        }

        throw new UsernameNotFoundException("Usuário com email não encontrado");
    }
}

