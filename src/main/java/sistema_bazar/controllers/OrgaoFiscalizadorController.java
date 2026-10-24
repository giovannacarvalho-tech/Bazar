package sistema_bazar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sistema_bazar.model.OrgaoFiscalizador;
import sistema_bazar.repositories.OrgaoFiscalizadorRepository;
import java.sql.SQLException;

@Controller
@RequestMapping("/fiscalizadores")
public class OrgaoFiscalizadorController {

    private final OrgaoFiscalizadorRepository repository =
            new OrgaoFiscalizadorRepository();

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    @GetMapping
    public String listar(Model model) {
        try {
            model.addAttribute(
                    "fiscalizadores",
                    repository.listarTodos()
            );
            return "fiscalizadores";

        } catch (SQLException e) {
            model.addAttribute(
                    "erro",
                    "Erro ao consultar os órgãos fiscalizadores."
            );
            return "fiscalizadores";
        }
    }

    @GetMapping("/novo")
    public String formulario() {
        return "cadastro-fiscalizador";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute OrgaoFiscalizador orgao,
            @RequestParam String senha) {

        try {
            orgao.setSenhaHash(encoder.encode(senha));
            repository.cadastrar(orgao);

            return "redirect:/fiscalizadores";

        } catch (SQLException e) {
            return "redirect:/fiscalizadores/novo?erro";
        }
    }
}
