package br.ufscar.dc.dsw.domain;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Veiculo")
public class Veiculo extends AbstractEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
	private String valor;

  	@Column(nullable = false, unique = true, length = 10)
	private String fotos;

	@OneToOne
	private Loja loja;

    public void setCnpj(Loja cnpj) {
        this.cnpj = cnpj;
    }
}
