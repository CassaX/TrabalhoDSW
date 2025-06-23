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
	private BCryptPasswordEncoder encoder; // Mantenha aqui se precisar para o AdminConfig ou RegistroController

    @GetMapping("/cadastrar")
    public String cadastrar(Cliente cliente, ModelMap model) { // Adicionado ModelMap
        model.addAttribute("cliente", cliente); // Garante que a instância está no modelo
        return "cliente/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr, ModelMap model) { // Adicionado ModelMap
        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente); // Retorna o objeto para exibir erros
            return "cliente/cadastro";
        }
        
        // A codificação da senha já deve estar no ClienteService.salvar ou RegistroController
        // Se a senha for codificada aqui, remova de lá para evitar dupla codificação.
        // cliente.setSenha(encoder.encode(cliente.getSenha())); // Provavelmente já no RegistroController
        
        cliente.setRole("CLIENTE");
        cliente.setEnabled(true);
        clienteService.salvar(cliente);
        attr.addFlashAttribute("success", "usuario.create.sucess");
        return "redirect:/cliente/listar";
    }
    
    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "cliente/cadastro";
    }
    
    @PostMapping("/editar")
	public String editar(@Valid Cliente cliente, String novoPassword, BindingResult result, RedirectAttributes attr, ModelMap model) { // Adicionado ModelMap
		
		if (result.hasErrors()) {
            model.addAttribute("cliente", cliente); // Retorna o objeto para exibir erros
			return "cliente/cadastro";
		}

        // A lógica de senha deve ser movida para o ClienteService.editar
		// if (novoPassword != null && !novoPassword.trim().isEmpty()) {
		// 	cliente.setSenha(encoder.encode(novoPassword));
		// } else {
		// 	System.out.println("Senha não foi editada");
		// }
        
		clienteService.editar(cliente, novoPassword); // Chama o service que agora aceita novoPassword
		attr.addFlashAttribute("success", "usuario.edit.sucess"); // Ajustei para 'success' e chave i18n
		return "redirect:/cliente/listar";
	}
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        clienteService.excluir(id);
        attr.addFlashAttribute("success", "usuario.delete.sucess"); // Ajustei para 'success' e chave i18n
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