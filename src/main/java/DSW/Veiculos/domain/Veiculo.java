package DSW.Veiculos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("rawtypes")
@Entity
@Table(name = "Veiculo")
public class Veiculo extends AbstractEntity {

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

}
