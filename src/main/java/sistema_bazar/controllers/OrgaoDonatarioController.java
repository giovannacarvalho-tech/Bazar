package sistema_bazar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sistema_bazar.model.OrgaoDonatario;
import sistema_bazar.repositories.OrgaoDonatarioRepository;
import java.sql.SQLException;

@Controller
@RequestMapping("/donatarios")
public class OrgaoDonatarioController {

    private final OrgaoDonatarioRepository repository =
            new OrgaoDonatarioRepository();

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    @GetMapping
    public String listar(Model model) {
        try {
            model.addAttribute(
                    "donatarios",
                    repository.listarTodos()
            );
            return "donatarios";

        } catch (SQLException e) {
            model.addAttribute(
                    "erro",
                    "Erro ao consultar os órgãos donatarios."
            );
            return "donatarios";
        }
    }
    @GetMapping("/novo")
    public String formulario() {
        return "cadastro-donatario";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute OrgaoDonatario orgao,
            @RequestParam String senha) {

        try {
            orgao.setSenhaHash(encoder.encode(senha));
            repository.cadastrar(orgao);

            return "redirect:/donatarios";

        } catch (SQLException e) {
            return "redirect:/donatarios/novo?erro";
        }
    }
}

