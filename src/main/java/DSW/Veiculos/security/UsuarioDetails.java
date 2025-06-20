package DSW.Veiculos.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String role;
    private final boolean enabled;

    public UsuarioDetails(String username, String password, String role, boolean enabled) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override public String getPassword() {
         return password; 
        }

    @Override public String getUsername() { 
        return username;
    }
    @Override public boolean isAccountNonExpired() {
         return true; 
        }
    @Override public boolean isAccountNonLocked() { 
        return true; 
    }
    @Override public boolean isCredentialsNonExpired() { 
        return true; 
    }
    @Override public boolean isEnabled() { 
        return enabled;
     }
}

