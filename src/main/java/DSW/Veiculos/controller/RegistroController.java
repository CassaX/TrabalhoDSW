package DSW.Veiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.service.spec.IClienteService;
import DSW.Veiculos.service.spec.ILojaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private IClienteService clienteService;
    
    @Autowired
    private ILojaService lojaService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public String registro(ModelMap model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("loja", new Loja());
        return "registro";
    }
    
    @PostMapping("/cliente")
    public String registrarCliente(@Valid Cliente cliente, BindingResult result, 
                                 RedirectAttributes attr, ModelMap model) { // Adicionado ModelMap
        if (result.hasErrors()) {
            model.addAttribute("loja", new Loja()); 
            model.addAttribute("cliente", cliente); // Garante que o cliente submetido está no modelo
            model.addAttribute("activeTab", "cliente"); 
            return "registro";
        }
        
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setRole("CLIENTE");
        cliente.setEnabled(true);
        clienteService.salvar(cliente);
        attr.addFlashAttribute("success", "usuario.create.sucess"); 
        attr.addFlashAttribute("successLogin", "Cliente registrado com sucesso! Faça login para continuar.");
        return "redirect:/login";
    }
    
    @PostMapping("/loja")
    public String registrarLoja(@Valid Loja loja, BindingResult result, 
                               RedirectAttributes attr, ModelMap model) { // Adicionado ModelMap
        if (result.hasErrors()) {
            model.addAttribute("cliente", new Cliente()); 
            model.addAttribute("loja", loja); // Garante que a loja submetida está no modelo
            model.addAttribute("activeTab", "loja"); 
            return "registro";
        }
        
        loja.setPassword(passwordEncoder.encode(loja.getPassword()));
        loja.setRole("LOJA");
        loja.setEnabled(true);
        lojaService.salvar(loja);
        attr.addFlashAttribute("success", "editora.create.sucess");
        attr.addFlashAttribute("successLogin", "Loja registrada com sucesso! Faça login para continuar.");
        return "redirect:/login";
    }
}