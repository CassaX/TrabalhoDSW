package DSW.Veiculos.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Veiculo")
public class Veiculo extends AbstractEntity {


	@Column(nullable = false, unique = true, length = 7)
	private String placa;

	@Column(nullable = false, unique = true, length = 50)
	private String modelo;

	@Column(nullable = false, unique = true, length = 50)
	private String chassi;

	@Column(nullable = false, unique = true, length = 4)
	private String ano;

	@Column(nullable = false, unique = true, length = 6)
	private int quilometragem;

	@Column(nullable = false, unique = true, length = 300)
	private String descricao;

	@Column(nullable = false, unique = true, length = 8)
	private BigDecimal valor;

	@Column(nullable = false, unique = true, length = 10)
	private String fotos[] = new String[10];

	@ManyToOne
	@JoinColumn(name = "loja_id")
	private Loja loja;


	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public int getQuilometragem() {
		return quilometragem;
	}

	public void setQuilometragem(int quilometragem) {
		this.quilometragem = quilometragem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String[] getFotos() {
		return fotos;
	}

	public void setFotos(String[] fotos) {
		this.fotos = fotos;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	
}
