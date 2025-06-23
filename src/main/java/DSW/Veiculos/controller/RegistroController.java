package DSW.Veiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importe o PasswordEncoder
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
    private PasswordEncoder passwordEncoder; // Injetar o PasswordEncoder

    @GetMapping
    public String registro(ModelMap model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("loja", new Loja());
        return "registro"; // Aponta para o novo arquivo registro.html
    }

    @PostMapping("/cliente")
    public String registrarCliente(@Valid Cliente cliente, BindingResult result, 
                                 RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("loja", new Loja()); // Garante que o objeto Loja ainda esteja no modelo
            model.addAttribute("activeTab", "cliente"); // Para manter a aba de cliente ativa
            return "registro"; // Retorna para o template registro.html
        }

        // Codifica a senha antes de salvar
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setRole("CLIENTE");
        cliente.setEnabled(true);
        clienteService.salvar(cliente); // Este salvar pode precisar de refatoração para validações de unicidade
        attr.addFlashAttribute("success", "usuario.create.sucess"); // Mensagem internacionalizada
        attr.addFlashAttribute("successLogin", "Cliente registrado com sucesso! Faça login para continuar."); // Mensagem específica para login
        return "redirect:/login";
    }

    @PostMapping("/loja")
    public String registrarLoja(@Valid Loja loja, BindingResult result, 
                               RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", new Cliente()); // Garante que o objeto Cliente ainda esteja no modelo
            model.addAttribute("activeTab", "loja"); // Para manter a aba de loja ativa
            return "registro"; // Retorna para o template registro.html
        }

        // Codifica a senha antes de salvar
        loja.setPassword(passwordEncoder.encode(loja.getPassword()));
        loja.setRole("LOJA");
        loja.setEnabled(true);
        lojaService.salvar(loja); // Este salvar pode precisar de refatoração para validações de unicidade
        attr.addFlashAttribute("success", "editora.create.sucess"); // Mensagem internacionalizada
        attr.addFlashAttribute("successLogin", "Loja registrada com sucesso! Faça login para continuar."); // Mensagem específica para login
        return "redirect:/login";
    }
}