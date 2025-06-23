package DSW.Veiculos.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Importe BigDecimal

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat; // Importe esta anotação
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

import DSW.Veiculos.domain.Cliente;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Proposta;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.service.impl.NotificacaoPropostaService;
import DSW.Veiculos.service.spec.IClienteService;
import DSW.Veiculos.service.spec.ILojaService;
import DSW.Veiculos.service.spec.IPropostaService;
import DSW.Veiculos.service.spec.IVeiculoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/propostas")
public class PropostaController {
    
    @Autowired
    private IPropostaService propostaService;
    
    @Autowired
    private IVeiculoService veiculoService;

    @Autowired
    private ILojaService lojaService;
    
    @Autowired
    private IClienteService clienteService;
    
    @Autowired
    private NotificacaoPropostaService notificacaoService;
    
    @GetMapping("/criar/{idVeiculo}")
    public String criarProposta(@PathVariable("idVeiculo") Long idVeiculo, ModelMap model) {
        Veiculo veiculo = veiculoService.buscarPorId(idVeiculo);
        if (veiculo == null) {
            // Tratar caso o veículo não seja encontrado
            model.addAttribute("fail", "Veículo não encontrado para criar proposta.");
            return "redirect:/veiculos/listar";
        }
        Proposta proposta = new Proposta();
        proposta.setVeiculo(veiculo); // Define o veículo na proposta
        
        model.addAttribute("proposta", proposta);
        model.addAttribute("veiculo", veiculo); // Adiciona o veículo ao modelo para exibição no HTML
        return "proposta/cadastro";
    }
    
    @PostMapping("/salvar")
    public String salvarProposta(@Valid Proposta proposta, BindingResult result, 
                               @RequestParam("idVeiculo") Long idVeiculo, // idVeiculo vem do formulário
                               RedirectAttributes attr, ModelMap model) {
        
        // Carrega o veículo e o cliente para revalidar/repopular em caso de erro
        Veiculo veiculo = veiculoService.buscarPorId(idVeiculo);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailCliente = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(emailCliente);

        // Define o veículo e o cliente na proposta antes da validação para que as validações de relacionamento funcionem
        proposta.setVeiculo(veiculo);
        proposta.setCliente(cliente);

        if (result.hasErrors()) {
            model.addAttribute("veiculo", veiculo); // Passe o veículo de volta para a view em caso de erro
            model.addAttribute("proposta", proposta); // Passe a proposta de volta com os erros
            return "proposta/cadastro";
        }
        
        if (propostaService.existePropostaAbertaParaClienteEVeiculo(cliente, veiculo)) {
            attr.addFlashAttribute("fail", "Você já tem uma proposta em aberto para este veículo!");
            return "redirect:/veiculos/listar"; // Ou para a página de propostas do cliente
        }
        
        proposta.setStatus("ABERTO"); // Define o status inicial
        proposta.setData(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))); // Define a data atual
        
        propostaService.salvar(proposta);
        notificacaoService.notificarProposta(proposta, false); // Notifica a loja

        attr.addFlashAttribute("success", "Proposta enviada com sucesso!");
        return "redirect:/propostas/minhas-propostas";
    }
    
    @GetMapping("/minhas-propostas")
    public String listarMinhasPropostas(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cliente cliente = clienteService.buscarPorEmail(email);
        
        model.addAttribute("propostas", propostaService.findByCliente(cliente));
        return "proposta/lista-cliente";
    }
    
    // Lista propostas para um VEÍCULO ESPECÍFICO DE UMA LOJA (R6, R8)
    @GetMapping("/loja/{idVeiculo}/propostas") // Nova URL para gerenciar propostas de um veículo específico
    public String listarPropostasLoja(@PathVariable("idVeiculo") Long idVeiculo, ModelMap model, RedirectAttributes attr) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Loja loja = lojaService.buscarPorEmail(email);
        
        Veiculo veiculo = veiculoService.buscarPorId(idVeiculo);

        // Garante que o veículo existe e pertence à loja logada
        if (veiculo == null || !veiculo.getLoja().getId().equals(loja.getId())) {
            attr.addFlashAttribute("fail", "Acesso negado ou veículo não encontrado.");
            return "redirect:/veiculos/meus-veiculos"; // Redireciona para a lista de veículos da loja
        }

        model.addAttribute("veiculo", veiculo); // Passa o veículo para o HTML
        model.addAttribute("propostas", propostaService.findByVeiculo(veiculo)); // Busca propostas SOMENTE para este veículo
        return "proposta/lista-loja";
    }
    
    @GetMapping("/editar-status/{idProposta}") // Alterei o nome do path variable para clareza
    public String preEditarStatus(@PathVariable("idProposta") Long idProposta, ModelMap model, RedirectAttributes attr) {
        Proposta proposta = propostaService.buscarPorId(idProposta);
        
        // Verifica se a proposta existe e se a loja logada tem permissão sobre o veículo da proposta
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailLoja = auth.getName();
        Loja loja = lojaService.buscarPorEmail(emailLoja);

        if (proposta == null || !proposta.getVeiculo().getLoja().getId().equals(loja.getId())) {
            attr.addFlashAttribute("fail", "Acesso negado ou proposta não encontrada.");
            return "redirect:/veiculos/meus-veiculos"; // Redireciona para a lista de veículos da loja
        }

        model.addAttribute("proposta", proposta);
        return "proposta/editar-status";
    }
    
    @PostMapping("/editar-status")
    public String editarStatus(@Valid Proposta proposta, BindingResult result,
                             @RequestParam(value = "contrapropostaValor", required = false) BigDecimal contrapropostaValor,
                             @RequestParam(value = "contrapropostaCondicoes", required = false) String contrapropostaCondicoes,
                             @RequestParam(value = "horarioReuniao", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horarioReuniao,
                             @RequestParam(value = "linkReuniao", required = false) String linkReuniao,
                             RedirectAttributes attr, ModelMap model) { // Adicione ModelMap para retornar a view com erros
        
        // Recarrega a proposta existente do banco para não perder dados importantes e evitar manipulação direta do objeto de formulário
        Proposta propostaExistente = propostaService.buscarPorId(proposta.getId());
        if (propostaExistente == null) {
            attr.addFlashAttribute("fail", "Proposta não encontrada para edição.");
            return "redirect:/veiculos/meus-veiculos"; // Redireciona para a lista de veículos da loja
        }

        // Atualiza o status vindo do formulário
        propostaExistente.setStatus(proposta.getStatus());

        // Lógica para setar os novos campos com base no status
        if ("NÃO ACEITO".equals(proposta.getStatus())) {
            propostaExistente.setContrapropostaValor(contrapropostaValor);
            propostaExistente.setContrapropostaCondicoes(contrapropostaCondicoes);
            // Limpa campos de ACEITO
            propostaExistente.setHorarioReuniao(null);
            propostaExistente.setLinkReuniao(null);
        } else if ("ACEITO".equals(proposta.getStatus())) {
            // Validações adicionais para ACEITO: horário e link devem ser fornecidos
            if (horarioReuniao == null || linkReuniao == null || linkReuniao.trim().isEmpty()) {
                result.rejectValue("horarioReuniao", "NotNull.proposta.horarioReuniao", "Horário da reunião é obrigatório para status ACEITO.");
                result.rejectValue("linkReuniao", "NotBlank.proposta.linkReuniao", "Link da reunião é obrigatório para status ACEITO.");
                // Se houver erros de validação específicos para o status ACEITO
                model.addAttribute("proposta", propostaExistente); // Retorna a proposta existente para a view
                return "proposta/editar-status";
            }
            propostaExistente.setHorarioReuniao(horarioReuniao);
            propostaExistente.setLinkReuniao(linkReuniao);
            // Limpa campos de NÃO ACEITO
            propostaExistente.setContrapropostaValor(null);
            propostaExistente.setContrapropostaCondicoes(null);
        } else { // ABERTO ou outros status, limpar campos de resposta
            propostaExistente.setContrapropostaValor(null);
            propostaExistente.setContrapropostaCondicoes(null);
            propostaExistente.setHorarioReuniao(null);
            propostaExistente.setLinkReuniao(null);
        }
        
        // Verifica erros de validação gerais do objeto proposta (se houvessem, ex: valor nulo)
        if (result.hasErrors()) {
            model.addAttribute("proposta", propostaExistente); // Retorna a proposta existente para a view
            return "proposta/editar-status";
        }

        propostaService.editar(propostaExistente); // Salva a proposta atualizada
        notificacaoService.notificarProposta(propostaExistente, true); // Notifica o cliente

        attr.addFlashAttribute("success", "proposta.statusAtualizado");
        return "redirect:/propostas/loja/" + propostaExistente.getVeiculo().getId() + "/propostas"; // Redireciona para as propostas do veículo específico
    }
}