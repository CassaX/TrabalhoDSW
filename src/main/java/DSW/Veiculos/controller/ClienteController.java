package DSW.Veiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.service.spec.IClienteService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private IClienteService clienteService;

	@Autowired
	private BCryptPasswordEncoder encoder;
    
    @GetMapping("/cadastrar")
    public String cadastrar(Cliente cliente) {
        return "cliente/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "cliente/cadastro";
        }
        
        cliente.setRole("CLIENTE");
        cliente.setEnabled(true);
        clienteService.salvar(cliente);
        attr.addFlashAttribute("success", "Cliente cadastrado com sucesso.");
        return "redirect:/cliente/listar";
    }
    
    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "cliente/cadastro";
    }
    
    @PostMapping("/editar")
    public String editar(@Valid Cliente cliente, @RequestParam(value = "novoPassword", required = false) String novoPassword, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "cliente/cadastro";
        }
        // A chamada para o serviço agora passará o novoPassword
        clienteService.editar(cliente, novoPassword); // Modifique o service para aceitar novoPassword
        attr.addFlashAttribute("sucess", "usuario.edit.sucess");
        return "redirect:/cliente/listar";
    }
        
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        clienteService.excluir(id);
        attr.addFlashAttribute("success", "Cliente removido com sucesso.");
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