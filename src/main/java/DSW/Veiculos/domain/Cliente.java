package DSW.Veiculos.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@SuppressWarnings("serial")
@Entity
@Table(name = "Cliente")
public class Cliente extends AbstractEntity<Long> {

	@NotBlank
	@Column(nullable = false, length = 100, unique = true)
	private String email;

	@NotBlank
	@Column(nullable = false, length = 64)
	private String senha;

	@NotBlank
	@Column(nullable = false, length = 14)
	private String CPF;

	@NotBlank
	@Column(nullable = false, length = 14)
	private String nome;

	@NotBlank
	@Column(nullable = false, length = 11)
	private String telefone;

	@NotBlank
	@Column(nullable = false, length = 10)
	private String sexo;

	@NotBlank
	@Column(nullable = false, length = 10)
	private Date data_nasc;

	@NotBlank
	@Column(nullable = false, length = 10)
	private String role;

	@Column(nullable = false)
	private boolean enabled;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Date getData_nasc() {
		return data_nasc;
	}

	public void setData_nasc(Date data_nasc) {
		this.data_nasc = data_nasc;
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
}