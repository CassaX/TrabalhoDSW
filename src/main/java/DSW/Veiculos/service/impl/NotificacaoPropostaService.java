package DSW.Veiculos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DSW.Veiculos.domain.Proposta;
import jakarta.mail.internet.InternetAddress;

@Service
public class NotificacaoPropostaService {

    @Autowired
    private EmailService emailService;

    public void notificarProposta(Proposta proposta, boolean notificarCliente) {
        try {
            String assunto = "Atualização da proposta para o carro " + proposta.getVeiculo().getModelo();
            String corpo = "Olá " + (notificarCliente ? proposta.getCliente().getNome() : proposta.getVeiculo().getLoja().getNome()) + ",\n\n" +
                    "A proposta para o carro \"" + proposta.getVeiculo().getModelo() + "\" no valor de R$ " +
                    String.format("%.2f", proposta.getValor()) + " foi atualizada.\n\n" +
                    "Status atual: " + proposta.getStatus() + "\n\n" +
                    "Atenciosamente,\nEquipe de Propostas";

            InternetAddress from = new InternetAddress("trabalhodswgrupo@gmail.com");
            InternetAddress to = new InternetAddress(
                notificarCliente ? proposta.getCliente().getEmail() : proposta.getVeiculo().getLoja().getEmail()
            );

            emailService.send(from, to, assunto, corpo);

        } catch (Exception e) {
            System.out.println("Erro ao enviar e-mail de notificação:");
            e.printStackTrace();
        }
    }
}