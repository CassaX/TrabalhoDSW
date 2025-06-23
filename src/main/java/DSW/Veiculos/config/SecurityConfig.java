package DSW.Veiculos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import DSW.Veiculos.security.UsuarioDetailsServiceImpl;

@Configuration
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                 .authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers("/", "/home", "/login", "/registro", "/registro/**").permitAll()
                .requestMatchers("/veiculos", "/veiculos/listar").permitAll()
                
                // Admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Cliente
                .requestMatchers("/cliente/**").hasRole("CLIENTE")
                .requestMatchers("/propostas/criar/**", "/propostas/minhas-propostas").hasRole("CLIENTE")
                
                // Loja
                .requestMatchers("/loja/**").hasRole("LOJA")
                .requestMatchers("/veiculos/cadastrar", "/veiculos/meus-veiculos", "/veiculos/editar/**").hasRole("LOJA")
                .requestMatchers("/propostas/loja", "/propostas/editar-status/**").hasRole("LOJA")
                
                // Rotas compartilhadas
                .requestMatchers("/propostas/salvar").hasAnyRole("CLIENTE", "LOJA")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .userDetailsService(usuarioDetailsService);

    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
