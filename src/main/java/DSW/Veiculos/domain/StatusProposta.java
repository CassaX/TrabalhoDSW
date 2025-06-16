package DSW.Veiculos.domain;

public enum StatusProposta {
    ABERTO("Em análise"),
    ACEITO("Aceito"),
    NAO_ACEITO("Não aceito");

    private final String descricao;

    StatusProposta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
        
    }
}
