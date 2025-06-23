package DSW.Veiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult; // Importe
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String cadastrar(Loja loja, ModelMap model) {
        model.addAttribute("loja", loja);
        model.addAttribute("org.springframework.validation.BindingResult.loja", new BeanPropertyBindingResult(loja, "loja")); //
        return "loja/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid Loja loja, BindingResult result, RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("loja", loja);
            model.addAttribute("org.springframework.validation.BindingResult.loja", result); //
            return "loja/cadastro";
        }
        
        try {
            loja.setRole("LOJA"); // Define a role aqui
            loja.setEnabled(true); // Define enabled aqui
            // O serviço (LojaService.salvar) já cuida da codificação da senha
            lojaService.salvar(loja); 
            attr.addFlashAttribute("success", "editora.create.sucess");
            return "redirect:/loja/listar";
        } catch (IllegalArgumentException e) {
            // Captura a exceção de unicidade do serviço (Email ou CNPJ)
            if (e.getMessage().contains("Email Já em Uso")) {
                result.rejectValue("email", null, e.getMessage());
            } else if (e.getMessage().contains("CNPJ Já Cadastrado")) {
                result.rejectValue("CNPJ", null, e.getMessage());
            } else {
                result.reject("globalError", null, e.getMessage()); // Erro geral se não for específico
            }
            model.addAttribute("loja", loja);
            model.addAttribute("org.springframework.validation.BindingResult.loja", result);
            return "loja/cadastro";
        }
    }
    
    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Loja loja = lojaService.buscarPorId(id);
        model.addAttribute("loja", loja);
        model.addAttribute("org.springframework.validation.BindingResult.loja", new BeanPropertyBindingResult(loja, "loja")); //
        return "loja/cadastro";
    }
    
    @PostMapping("/editar")
    public String editar(@Valid Loja loja, String novaSenha, BindingResult result, RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("loja", loja);
            model.addAttribute("org.springframework.validation.BindingResult.loja", result); //
            return "loja/cadastro";
        }
        
        try {
            lojaService.editar(loja, novaSenha); // O serviço cuida da atualização e validação
            attr.addFlashAttribute("success", "editora.edit.sucess");
            return "redirect:/loja/listar";
        } catch (IllegalArgumentException e) {
            // Captura a exceção de unicidade (Email ou CNPJ)
            if (e.getMessage().contains("Email Já em Uso")) {
                result.rejectValue("email", null, e.getMessage());
            } else if (e.getMessage().contains("CNPJ não pode ser alterado")) { // Mensagem específica para CNPJ não poder ser alterado
                result.rejectValue("CNPJ", null, e.getMessage());
            } else {
                result.reject("globalError", null, e.getMessage());
            }
            model.addAttribute("loja", loja);
            model.addAttribute("org.springframework.validation.BindingResult.loja", result);
            return "loja/cadastro";
        }
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        lojaService.excluir(id);
        attr.addFlashAttribute("success", "Loja removida com sucesso.");
        return "redirect:/loja/listar";
    }
    
    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("lojas", lojaService.buscarTodos());
        return "loja/lista";
    }
    
    @GetMapping("/perfil")
    public String perfil(ModelMap model, RedirectAttributes attr) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email);
        
        if (loja == null || !"LOJA".equals(loja.getRole())) { // Salvaguarda para garantir que é uma loja
            System.err.println("Erro: Usuário logado como " + email + " tentou acessar perfil de loja, mas não foi encontrado ou não tem ROLE_LOJA.");
            attr.addFlashAttribute("fail", "Seu perfil de loja não foi encontrado ou você não tem permissão para acessá-lo.");
            // Redireciona para o perfil apropriado ou home
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
                return "redirect:/cliente/perfil";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/home";
        }

        model.addAttribute("loja", loja);
        return "loja/perfil";
    }
}