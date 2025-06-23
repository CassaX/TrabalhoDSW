package DSW.Veiculos.controller;

import java.util.List;

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

import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.service.spec.ILojaService;
import DSW.Veiculos.service.spec.IVeiculoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {
    
    @Autowired
    private IVeiculoService veiculoService;
    
    @Autowired
    private ILojaService lojaService;
    
    @GetMapping("/cadastrar")
    public String cadastrar(Veiculo veiculo, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email); 
        model.addAttribute("loja", loja);
        return "veiculo/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid Veiculo veiculo, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "veiculo/cadastro";
        }
        
        veiculoService.salvar(veiculo);
        attr.addFlashAttribute("success", "Veículo cadastrado com sucesso.");
        return "redirect:/veiculos/meus-veiculos";
    }
    
    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("veiculo", veiculoService.buscarPorId(id));
        return "veiculo/cadastro";
    }
    
    @PostMapping("/editar")
    public String editar(@Valid Veiculo veiculo, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "veiculo/cadastro";
        }
        
        veiculoService.editar(veiculo);
        attr.addFlashAttribute("success", "Veículo editado com sucesso.");
        return "redirect:/veiculos/meus-veiculos";
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        veiculoService.excluir(id);
        attr.addFlashAttribute("success", "Veículo removido com sucesso.");
        return "redirect:/veiculos/meus-veiculos";
    }
    
    @GetMapping("/meus-veiculos")
    public String listarMeusVeiculos(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email);
        model.addAttribute("veiculos", veiculoService.buscarPorLoja(loja));
        return "veiculo/lista-loja";
    }
    
    @GetMapping("/listar")
    public String listarTodos(@RequestParam(value = "modelo", required = false) String modelo, ModelMap model) {
        List<Veiculo> veiculos;
        if (modelo != null && !modelo.isEmpty()) {
            veiculos = veiculoService.buscarPorModelo(modelo);
        } else {
            veiculos = veiculoService.buscarTodos();
        }
        model.addAttribute("veiculos", veiculos);
        return "veiculo/lista-publica";
    }
}