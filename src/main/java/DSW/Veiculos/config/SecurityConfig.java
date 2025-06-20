package DSW.Veiculos.config;

@Configuration
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/cliente/**").hasRole("CLIENTE")
                .requestMatchers("/loja/**").hasRole("LOJA")
                .anyRequest().permitAll()
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
