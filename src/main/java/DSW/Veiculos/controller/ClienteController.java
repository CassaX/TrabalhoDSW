package DSW.Veiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller; // Importe (se for usado)
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping; // Importe
import org.springframework.web.bind.annotation.PathVariable; // Importe
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.service.spec.IClienteService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private IClienteService clienteService;

    // Se você injetar o PasswordEncoder aqui, ele pode ser usado para depuração ou para garantir
    // que a senha NÃO seja codificada no controller se o serviço já o fizer.
    // @Autowired
    // private PasswordEncoder passwordEncoder; 

    @GetMapping("/cadastrar")
    public String cadastrar(Cliente cliente, ModelMap model) {
        model.addAttribute("cliente", cliente);
        // Garante que o BindingResult existe mesmo em GET, para o fragmento de validação
        model.addAttribute("org.springframework.validation.BindingResult.cliente", new BeanPropertyBindingResult(cliente, "cliente"));
        return "cliente/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr, ModelMap model) {
        
        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente); 
            model.addAttribute("org.springframework.validation.BindingResult.cliente", result);
            return "cliente/cadastro";
        }
        
        try {
            cliente.setRole("CLIENTE");
            cliente.setEnabled(true);
            // Aqui a senha deve vir do form (texto puro) e ser codificada NO SERVICE (ClienteService.salvar)
            // Se você codificar aqui, certifique-se que o service não codifica de novo.
            // Ex: cliente.setSenha(passwordEncoder.encode(cliente.getSenha())); // Se a codificação for no Controller
            clienteService.salvar(cliente); // Deixa o service cuidar da codificação
            attr.addFlashAttribute("success", "usuario.create.sucess");
            return "redirect:/cliente/listar";
        } catch (IllegalArgumentException e) {
            // Captura a exceção de unicidade (Email ou CPF) do serviço
            if (e.getMessage().contains("Email já em Uso")) {
                result.rejectValue("email", "Unique.cliente.email", e.getMessage()); // Rejeita o campo 'email'
            } else if (e.getMessage().contains("CPF já em Uso")) {
                result.rejectValue("CPF", "Unique.cliente.CPF", e.getMessage()); // Rejeita o campo 'CPF'
            } else {
                result.reject("globalError", e.getMessage()); // Erro geral se mensagem não específica
            }
            model.addAttribute("cliente", cliente);
            model.addAttribute("org.springframework.validation.BindingResult.cliente", result);
            return "cliente/cadastro";
        }
    }
    
    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        model.addAttribute("org.springframework.validation.BindingResult.cliente", new BeanPropertyBindingResult(cliente, "cliente"));
        return "cliente/cadastro";
    }
    
    @PostMapping("/editar")
	public String editar(@Valid Cliente cliente, String novoPassword, BindingResult result, RedirectAttributes attr, ModelMap model) {
		
		if (result.hasErrors()) {
            model.addAttribute("cliente", cliente); 
            model.addAttribute("org.springframework.validation.BindingResult.cliente", result);
			return "cliente/cadastro";
		}
		
		try {
            clienteService.editar(cliente, novoPassword); // Deixa o service lidar com a senha e validações
            attr.addFlashAttribute("success", "usuario.edit.sucess");
            return "redirect:/cliente/listar";
        } catch (IllegalArgumentException e) {
            // Captura a exceção de unicidade (Email ou CPF) do serviço
            if (e.getMessage().contains("Email já em Uso")) {
                result.rejectValue("email", "Unique.cliente.email", e.getMessage());
            } else if (e.getMessage().contains("CPF já em Uso")) {
                result.rejectValue("CPF", "Unique.cliente.CPF", e.getMessage());
            } else {
                result.reject("globalError", e.getMessage());
            }
            model.addAttribute("cliente", cliente);
            model.addAttribute("org.springframework.validation.BindingResult.cliente", result);
            return "cliente/cadastro";
        }
	}
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        clienteService.excluir(id);
        attr.addFlashAttribute("success", "usuario.delete.sucess");
        return "redirect:/cliente/listar";
    }
    
    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("clientes", clienteService.buscarTodos());
        return "cliente/lista";
    }
    
    @GetMapping("/perfil")
    public String perfil(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email);
        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }
}