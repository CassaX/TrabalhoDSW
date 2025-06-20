package DSW.Veiculos.security;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private ILojaDAO lojaDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteDAO.findByEmail(email);
        if (cliente != null) {
            return new UsuarioDetails(cliente.getEmail(), cliente.getSenha(), cliente.getRole(), cliente.isEnabled());
        }

        Loja loja = lojaDAO.findByEmail(email);
        if (loja != null) {
            return new UsuarioDetails(loja.getEmail(), loja.getPassword(), loja.getRole(), loja.isEnabled());
        }

        throw new UsernameNotFoundException("Usuário com email não encontrado");
    }
}

