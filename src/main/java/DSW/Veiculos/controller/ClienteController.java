package DSW.Veiculos.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import DSW.Veiculos.service.spec.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService service;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Cliente cliente) {
		return "cliente/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("clientes",service.buscarTodos());
		return "cliente/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			return "cliente/cadastro";
		}

		System.out.println("password = " + cliente.getSenha());
		cliente.setSenha(encoder.encode(cliente.getSenha()));
		service.salvar(cliente);
		attr.addFlashAttribute("sucess", "cliente.create.sucess");
		return "redirect:/cliente/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("cliente", service.buscarPorId(id));
		return "cliente/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Cliente cliente, String novoPassword, BindingResult result, RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			return "cliente/cadastro";
		}

		if (novoPassword != null && !novoPassword.trim().isEmpty()) {
			cliente.setSenha(novoPassword);(encoder.encode(novoPassword));
		} else {
			System.out.println("Senha não foi editada");
		}
		service.salvar(cliente);
		attr.addFlashAttribute("sucess", "cliente.edit.sucess");
		return "redirect:/cliente/listar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		service.excluir(id);
		model.addAttribute("sucess", "cliente.delete.sucess");
		return listar(model);
	}
}
