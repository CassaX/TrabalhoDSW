package DSW.Veiculos.controller;

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
import DSW.Veiculos.service.spec.ILojaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/loja")
public class LojaController {
    
    @Autowired
    private ILojaService lojaService;
    
    @GetMapping("/cadastrar")
    public String cadastrar(Loja loja) {
        return "loja/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid Loja loja, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "loja/cadastro";
        }
        
        loja.setRole("LOJA");
        loja.setEnabled(true);
        lojaService.salvar(loja);
        attr.addFlashAttribute("success", "Loja cadastrada com sucesso.");
        return "redirect:/loja/listar";
    }
    
    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("loja", lojaService.buscarPorId(id));
        return "loja/cadastro";
    }
    

    @PostMapping("/editar")
    public String editar(@Valid Loja loja, @RequestParam(value = "novaSenha", required = false) String novaSenha, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "loja/cadastro";
        }
        // Passar a nova senha para o service
        lojaService.editar(loja, novaSenha); // Modifique o service para aceitar novaSenha
        attr.addFlashAttribute("success", "editora.edit.sucess");
        return "redirect:/loja/listar";
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        lojaService.excluir(id);
        attr.addFlashAttribute("success", "Loja removida com sucesso.");
        return "redirect:loja/listar";
    }
    
    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("lojas", lojaService.buscarTodos());
        return "loja/lista";
    }
    
    @GetMapping("/perfil")
    public String perfil(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email);
        model.addAttribute("loja", loja);
        return "loja/perfil";
    }
}