package DSW.Veiculos.controller;

import java.util.List;

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
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.service.spec.ILojaService;
import DSW.Veiculos.service.spec.IVeiculoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {
    
    @Autowired
    private IVeiculoService veiculoService;
    
    @Autowired
    private ILojaService lojaService;

    // REMOVA ESTA INJEÇÃO:
    // @Autowired
    // private FileStorageService fileStorageService;
    
    @GetMapping("/cadastrar")
    public String cadastrar(Veiculo veiculo, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email); 
        model.addAttribute("loja", loja);
        model.addAttribute("veiculo", veiculo);
        return "veiculo/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid Veiculo veiculo, BindingResult result, 
                            // REMOVA ESTE PARÂMETRO: @RequestParam(value = "photos", required = false) MultipartFile[] photos, 
                             RedirectAttributes attr, ModelMap model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email); 
        veiculo.setLoja(loja);

        if (result.hasErrors()) {
            model.addAttribute("loja", loja);
            model.addAttribute("veiculo", veiculo);
            return "veiculo/cadastro";
        }
        
        // REMOVA TODA A LÓGICA DE UPLOAD DE FOTOS:
        // if (photos != null && photos.length > 0) {
        //     try {
        //         List<String> photoUrls = new java.util.ArrayList<>();
        //         for (MultipartFile photo : photos) {
        //             if (!photo.isEmpty()) {
        //                 String url = fileStorageService.saveFile(photo);
        //                 if (url != null) {
        //                     photoUrls.add(url);
        //                 }
        //             }
        //         }
        //         veiculo.setFotos(photoUrls);
        //     } catch (Exception e) {
        //         attr.addFlashAttribute("fail", "Erro ao salvar fotos: " + e.getMessage());
        //         model.addAttribute("loja", loja);
        //         model.addAttribute("veiculo", veiculo);
        //         return "veiculo/cadastro";
        //     }
        // }
        
        veiculoService.salvar(veiculo);
        attr.addFlashAttribute("success", "livro.create.sucess");
        return "redirect:/veiculos/meus-veiculos";
    }
    
    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Veiculo veiculo = veiculoService.buscarPorId(id);
        model.addAttribute("veiculo", veiculo);
        model.addAttribute("loja", veiculo.getLoja());
        return "veiculo/cadastro";
    }
    
    @PostMapping("/editar")
    public String editar(@Valid Veiculo veiculo, BindingResult result, 
                         // REMOVA ESTE PARÂMETRO: @RequestParam(value = "photos", required = false) MultipartFile[] photos, 
                         RedirectAttributes attr, ModelMap model) {
        
        Veiculo veiculoExistente = veiculoService.buscarPorId(veiculo.getId());
        if (veiculoExistente == null) {
            attr.addFlashAttribute("fail", "Veículo não encontrado para edição.");
            return "redirect:/veiculos/meus-veiculos";
        }
        veiculo.setLoja(veiculoExistente.getLoja());

        if (result.hasErrors()) {
            model.addAttribute("veiculo", veiculo);
            model.addAttribute("loja", veiculoExistente.getLoja());
            return "veiculo/cadastro";
        }

        // REMOVA TODA A LÓGICA DE UPLOAD/ATUALIZAÇÃO DE FOTOS:
        // List<String> currentPhotos = veiculoExistente.getFotos();
        // if (currentPhotos == null) currentPhotos = new java.util.ArrayList<>();
        //
        // if (photos != null && photos.length > 0 && !photos[0].isEmpty()) {
        //     try {
        //         for (MultipartFile photo : photos) {
        //             if (!photo.isEmpty()) {
        //                 String url = fileStorageService.saveFile(photo);
        //                 if (url != null && !currentPhotos.contains(url)) {
        //                     currentPhotos.add(url);
        //                 }
        //             }
        //         }
        //     } catch (Exception e) {
        //         attr.addFlashAttribute("fail", "Erro ao salvar fotos: " + e.getMessage());
        //         model.addAttribute("loja", veiculoExistente.getLoja());
        //         model.addAttribute("veiculo", veiculo);
        //         return "veiculo/cadastro";
        //     }
        // }
        // veiculo.setFotos(currentPhotos);

        veiculoService.editar(veiculo);
        attr.addFlashAttribute("success", "livro.edit.sucess");
        return "redirect:/veiculos/meus-veiculos";
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        veiculoService.excluir(id);
        attr.addFlashAttribute("success", "livro.delete.sucess");
        return "redirect:/veiculos/meus-veiculos";
    }
    
    @GetMapping("/meus-veiculos")
    public String listarMeusVeiculos(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email);
        model.addAttribute("veiculos", veiculoService.buscarPorLoja(loja));
        return "veiculo/lista-loja";
    }
    
    @GetMapping("/listar")
    public String listarTodos(@RequestParam(value = "modelo", required = false) String modelo, ModelMap model) {
        List<Veiculo> veiculos;
        if (modelo != null && !modelo.isEmpty()) {
            veiculos = veiculoService.buscarPorModelo(modelo);
        } else {
            veiculos = veiculoService.buscarTodos();
        }
        model.addAttribute("veiculos", veiculos);
        return "veiculo/lista-publica";
    }
}