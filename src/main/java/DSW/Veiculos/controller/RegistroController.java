package DSW.Veiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult; // Importe
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

    @GetMapping
    public String registro(ModelMap model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("loja", new Loja());
        model.addAttribute("org.springframework.validation.BindingResult.cliente",
                new BeanPropertyBindingResult(new Cliente(), "cliente"));
        model.addAttribute("org.springframework.validation.BindingResult.loja",
                new BeanPropertyBindingResult(new Loja(), "loja")); //
        return "registro";
    }

    @PostMapping("/cliente")
    public String registrarCliente(@Valid Cliente cliente, BindingResult result,
            RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", new Cliente());
            model.addAttribute("cliente", cliente);
            model.addAttribute("org.springframework.validation.BindingResult.cliente", result); //
            model.addAttribute("activeTab", "cliente");
            return "registro";
        }

        try {
            cliente.setRole("CLIENTE");
            cliente.setEnabled(true);
            // A senha do cliente será codificada no ClienteService.salvar()
            clienteService.salvar(cliente);
            attr.addFlashAttribute("success", "usuario.create.sucess");
            attr.addFlashAttribute("successLogin", "Cliente registrado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Email já em Uso")) {
                result.rejectValue("email", null, e.getMessage());
            } else if (e.getMessage().contains("CPF já em Uso")) {
                result.rejectValue("CPF", null, e.getMessage());
            } else {
                result.reject("globalError", null, e.getMessage());
            }
            model.addAttribute("loja", new Loja());
            model.addAttribute("cliente", cliente);
            model.addAttribute("org.springframework.validation.BindingResult.cliente", result); //
            model.addAttribute("activeTab", "cliente");
            return "registro";
        }
    }

    @PostMapping("/loja")
    public String registrarLoja(@Valid Loja loja, BindingResult result,
            RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("loja", new Loja());
            model.addAttribute("loja", loja);
            model.addAttribute("org.springframework.validation.BindingResult.loja", result); //
            model.addAttribute("activeTab", "loja");
            return "registro";
        }

        try {
            loja.setRole("LOJA");
            loja.setEnabled(true);
            // A senha da loja será codificada no LojaService.salvar()
            lojaService.salvar(loja);
            attr.addFlashAttribute("success", "editora.create.sucess");
            attr.addFlashAttribute("successLogin", "Loja registrada com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Email Já em Uso")) {
                result.rejectValue("email", null, e.getMessage());
            } else if (e.getMessage().contains("CNPJ Já Cadastrado")) {
                result.rejectValue("CNPJ", null, e.getMessage());
            } else {
                result.reject("globalError", null, e.getMessage());
            }
            model.addAttribute("loja", new Loja());
            model.addAttribute("loja", loja);
            model.addAttribute("org.springframework.validation.BindingResult.loja", result); //
            model.addAttribute("activeTab", "loja");
            return "registro";
        }
    }
}