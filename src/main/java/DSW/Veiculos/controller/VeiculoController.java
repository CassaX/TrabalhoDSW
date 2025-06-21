package DSW.Veiculos.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.service.spec.IPropostaService;
import DSW.Veiculos.service.spec.IVeiculoService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/veiculo")
public class VeiculoController {

	@Autowired
	private IVeiculoService VeiculoService;

	@GetMapping("/cadastrar")
	public String cadastrar(Veiculo veiculo) {
		return "veiculo/cadastro";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("veiculo", VeiculoService.buscarTodos());
		return "veiculo/lista";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Veiculo veiculo, BindingResult result, RedirectAttributes attr) {

		if (result.hasErrors()) {
			return "veiculo/cadastro";
		}

		VeiculoService.salvar(veiculo);
		attr.addFlashAttribute("sucess", "veiculo.create.sucess");
		return "redirect:/veiculo/listar";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("veiculo", VeiculoService.buscarPorId(id));
		return "veiculo/cadastro";
	}

	@PostMapping("/editar")
	public String editar(@Valid Veiculo veiculo, BindingResult result, RedirectAttributes attr) {

		if (result.hasErrors()) {
			return "veiculo/cadastro";
		}

		VeiculoService.salvar(veiculo);
		attr.addFlashAttribute("sucess", "veiculo.edit.sucess");
		return "redirect:/veiculo/listar";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		VeiculoService.excluir(id);
		attr.addFlashAttribute("sucess", "veiculo.delete.sucess");
		return "redirect:/veiculo/listar";
	}
}