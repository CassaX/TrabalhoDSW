package DSW.Veiculos.domain;

import DSW.Veiculos.validation.UniqueCNPJ;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "Loja")
public class Loja extends AbstractEntity<Long> {

    @UniqueCNPJ(message = "{Unique.editora.CNPJ}")
    @NotBlank
    @Size(min = 18, max = 18, message = "{Size.editora.CNPJ}")
    @Column(nullable = false, unique = true, length = 60)
    private String CNPJ;

    @NotBlank
    @Size(min = 3, max = 60)
    @Column(nullable = false, unique = true, length = 60)
    private String nome;

    @NotBlank
    @Column(nullable = false, length = 10)
    private String role;

    @Column(nullable = false)
    private boolean enabled;

    @NotBlank
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 64)
    private String password;

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
