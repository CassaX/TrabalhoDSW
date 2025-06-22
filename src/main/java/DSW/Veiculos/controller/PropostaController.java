package DSW.Veiculos.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.security.ClienteDetails;
import DSW.Veiculos.service.spec.IPropostaService;
import DSW.Veiculos.service.spec.IVeiculoService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
@RequestMapping("/proposta")
public class PropostaController {
    @Autowired
    private IPropostaService propostaService;

    @Autowired
    private IVeiculoService veiculoService;

    @GetMapping("/cadastrar")
    public String cadastrar(Proposta proposta){
        String data = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        proposta.setCliente(this.getCliente());
        proposta.setData(data);
        return "proposta/cadastro";
    }

    private Cliente getCliente(){
        ClienteDetails clienteDetails=(ClienteDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return clienteDetails.getCliente();
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("propostas", service.buscarTodos(this.getCliente()));
        return "proposta/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Proposta proposta, BindingResult result, RedirectAttributes attr) {
        if(result.hasErrors()){
            return "proposta/cadastro";
        }
        service.salvar(proposta);
        attr.addFlashAttribute("sucess", "compra.create.sucess");
        return "redirect:/proposta/listar"
    }

    @ModelAttribute("veiculos")
    public List<Veiculo>lVeiculos(){
        return veiculoService.buscarTodos();
    }
    
    
}
