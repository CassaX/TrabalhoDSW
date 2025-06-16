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

}
