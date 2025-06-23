package DSW.Veiculos.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.service.impl.NotificacaoPropostaService;
import DSW.Veiculos.service.spec.IClienteService;
import DSW.Veiculos.service.spec.ILojaService;
import DSW.Veiculos.service.spec.IPropostaService;
import DSW.Veiculos.service.spec.IVeiculoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/propostas")
public class PropostaController {
    
    @Autowired
    private IPropostaService propostaService;
    
    @Autowired
    private IVeiculoService veiculoService;

    @Autowired
    private ILojaService lojaService;
    
    @Autowired
    private IClienteService clienteService;
    
    @Autowired
    private NotificacaoPropostaService notificacaoService;
    
    @GetMapping("/criar/{idVeiculo}")
    public String criarProposta(@PathVariable("idVeiculo") Long idVeiculo, ModelMap model) {
        Veiculo veiculo = veiculoService.buscarPorId(idVeiculo);
        Proposta proposta = new Proposta();
        proposta.setVeiculo(veiculo);
        proposta.setValor(veiculo.getValor());
        
        model.addAttribute("proposta", proposta);
        return "proposta/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvarProposta(@Valid Proposta proposta, BindingResult result, 
                               @RequestParam("idVeiculo") Long idVeiculo,
                               RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "proposta/cadastro";
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email);
        
        Veiculo veiculo = veiculoService.buscarPorId(idVeiculo);
        
        proposta.setCliente(cliente);
        proposta.setVeiculo(veiculo);
        proposta.setStatus("ABERTO");
        proposta.setData(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        
        propostaService.salvar(proposta);
        notificacaoService.notificarProposta(proposta, false);
        
        attr.addFlashAttribute("success", "Proposta enviada com sucesso!");
        return "redirect:/propostas/minhas-propostas";
    }
    
    @GetMapping("/minhas-propostas")
    public String listarMinhasPropostas(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email);
        
        model.addAttribute("propostas", propostaService.findByCliente(cliente));
        return "proposta/lista-cliente";
    }
    
    @GetMapping("/loja")
    public String listarPropostasLoja(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email);
        
        model.addAttribute("propostas", propostaService.buscarPorLoja(loja));
        return "proposta/lista-loja";
    }
    
    @GetMapping("/editar-status/{id}")
    public String preEditarStatus(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("proposta", propostaService.buscarPorId(id));
        return "proposta/editar-status";
    }
    
    @PostMapping("/editar-status")
    public String editarStatus(@Valid Proposta proposta, BindingResult result,
                             @RequestParam("contraproposta") String contraproposta,
                             @RequestParam("linkReuniao") String linkReuniao,
                             RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "proposta/editar-status";
        }
        
        Proposta propostaExistente = propostaService.buscarPorId(proposta.getId());
        propostaExistente.setStatus(proposta.getStatus());
        
        if ("NÃO ACEITO".equals(proposta.getStatus())) {
            propostaExistente.setCondicoesPagamento(contraproposta);
        } else if ("ACEITO".equals(proposta.getStatus())) {
            propostaExistente.setCondicoesPagamento("Link da reunião: " + linkReuniao);
        }
        
        propostaService.editar(propostaExistente);
        notificacaoService.notificarProposta(propostaExistente, true);
        
        attr.addFlashAttribute("success", "Status da proposta atualizado com sucesso!");
        return "redirect:/propostas/loja";
    }
}