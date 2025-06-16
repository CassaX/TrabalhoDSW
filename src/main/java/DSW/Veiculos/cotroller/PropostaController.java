package DSW.Veiculos.cotroller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import DSW.Veiculos.service.PropostaService;

@Controller
@RequestMapping("/propostas")
public class PropostaController {
    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @GetMapping
    public String listarPropostasCliente(ModelMap  model, 
                                       @AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = (Cliente) userDetails;
        model.addAttribute("propostas", propostaService.listarPorCliente(cliente));
        return "propostas/listar";
    }

}
