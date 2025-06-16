package DSW.Veiculos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DSW.Veiculos.DAO.IPropostaDAO;
import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.StatusProposta;

@Service
public class PropostaService {
    private final IPropostaDAO PropostaDAO = null;
    // private final EmailService


    @Transactional
    public Proposta criarProposta(Proposta proposta) {
        if (PropostaDAO.existsByClienteAndVeiculoAndStatus(
            proposta.getCliente(), proposta.getVeiculo(), StatusProposta.ABERTO)) {
            throw new IllegalStateException("Já existe uma proposta em aberto para este veículo");
        }
        return PropostaDAO.save(proposta);
    }

    public List<Proposta> listarPorCliente(Cliente cliente) {
        return PropostaDAO.findByCliente(cliente);
    }

    @Transactional
    public Proposta atualizarStatus(Long propostaId, StatusProposta status, String contraproposta, String linkReuniao) {
        Proposta proposta = PropostaDAO.findById(propostaId)
            .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada"));
        
        proposta.setStatus(status);
        if (contraproposta != null)
            proposta.setContraproposta(contraproposta);

        if (linkReuniao != null)
            proposta.setLinkReuniao(linkReuniao);
        
        Proposta propostaAtualizada = PropostaDAO.save(proposta);
        //emailService enviar notificação
        
        return propostaAtualizada;
    }
}