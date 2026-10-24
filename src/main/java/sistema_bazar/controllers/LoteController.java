
package sistema_bazar.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sistema_bazar.model.Lote;
import sistema_bazar.model.OrgaoFiscalizador;
import sistema_bazar.model.OrgaoDonatario;
import sistema_bazar.repositories.LoteRepository;
import sistema_bazar.repositories.OrgaoDonatarioRepository;
import java.sql.SQLException;

@Controller
@RequestMapping("/lotes")
public class LoteController {

    private final LoteRepository repository = new LoteRepository();

    private final OrgaoDonatarioRepository donatarioRepo =
            new OrgaoDonatarioRepository();

    @GetMapping
    public String listar(HttpSession session, Model model) {

        String tipo = (String) session.getAttribute("tipoUsuario");

        if (tipo == null) {
            return "redirect:/login";
        }

        try {
            if ("FISCALIZADOR".equals(tipo)) {
                OrgaoFiscalizador orgao =
                        (OrgaoFiscalizador) session.getAttribute("orgaoFiscalizador");

                model.addAttribute("lotes",
                        repository.listarPorOrgaoFiscalizador(orgao.getId()));
            } else if ("DONATARIO".equals(tipo)){
                OrgaoDonatario orgao =
                        (OrgaoDonatario) session.getAttribute("orgaoDonatario");

                model.addAttribute("lotes",
                        repository.listarPorOrgaoDonatario(orgao.getId()));
            } else {

                return "redirect:/login";

            } return "lotes";

        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute(
                    "erro", "Erro ao tentar consultar os lotes."
            );
            return "lotes";
        }
    }

    @GetMapping("/novo")
    public String formulario(HttpSession session, Model model) {

        if (!"FISCALIZADOR".equals(
                session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        try{
            model.addAttribute("donatarios", donatarioRepo.listarTodos());
            return "cadastro-lote";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute(
                    "erro", "Erro ao carregar os órgãos donatários."
            );
            return "cadastro-lote";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Lote lote,
                         HttpSession session) {

        if (!"FISCALIZADOR".equals(
                session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }

        try {
            OrgaoFiscalizador orgao =
                    (OrgaoFiscalizador) session.getAttribute(
                            "orgaoFiscalizador");

            lote.setOrgaoFiscalizadorId(orgao.getId());
            lote.setStatus("DISPONIVEL");

            repository.cadastrar(lote);

            return "redirect:/lotes";

        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/lotes/novo?erro";
        }
    }
}
