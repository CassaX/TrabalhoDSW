package DSW.Veiculos.cotroller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.StatusProposta;
import DSW.Veiculos.service.PropostaService;

@Controller
@RequestMapping("/propostas")
public class PropostaController {
    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @GetMapping
    public String listarPropostasCliente(Model model, 
                                       @AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = (Cliente) userDetails;
        model.addAttribute("propostas", propostaService.listarPorCliente(cliente));
        return "propostas/listar";
    }

    @PostMapping
    public String criarProposta(@ModelAttribute Proposta proposta,
                              @AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = (Cliente) userDetails;
        proposta.setCliente(cliente);
        propostaService.criarProposta(proposta);
        return "redirect:/propostas?success";
    }

    //@PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                @RequestParam StatusProposta status,
                                @RequestParam(required = false) String contraproposta,
                                @RequestParam(required = false) String linkReuniao) {
        propostaService.atualizarStatus(id, status, contraproposta, linkReuniao);
        //return "redirect:/loja/propostas?success";
    }    
}
