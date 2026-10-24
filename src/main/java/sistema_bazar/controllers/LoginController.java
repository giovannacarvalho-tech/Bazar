
package sistema_bazar.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sistema_bazar.model.OrgaoFiscalizador;
import sistema_bazar.model.OrgaoDonatario;
import sistema_bazar.repositories.OrgaoFiscalizadorRepository;
import sistema_bazar.repositories.OrgaoDonatarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.SQLException;

@Controller
public class LoginController {

    private final OrgaoFiscalizadorRepository fiscalizadorRepo =
            new OrgaoFiscalizadorRepository();
    private final OrgaoDonatarioRepository donatarioRepo =
            new OrgaoDonatarioRepository();

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String tipo,
            HttpSession session,
            Model model) {

        try {
            if ("FISCALIZADOR".equals(tipo)) {
                OrgaoFiscalizador orgao =
                        fiscalizadorRepo.buscarPorEmail(email);

                if (orgao != null && encoder.matches(
                        senha, orgao.getSenhaHash())) {
                    session.setAttribute("orgaoFiscalizador", orgao);
                    session.setAttribute("tipoUsuario", tipo);
                    return "redirect:/home";
                }

            } else if ("DONATARIO".equals(tipo)) {
                OrgaoDonatario orgao =
                        donatarioRepo.buscarPorEmail(email);

                if (orgao != null && encoder.matches(
                        senha, orgao.getSenhaHash())) {
                    session.setAttribute("orgaoDonatario", orgao);
                    session.setAttribute("tipoUsuario", tipo);
                    return "redirect:/home";
                }
            } else {

                model.addAttribute("erro", "E-mail ou senha inválidos.");
                return "login";
            }
        } catch (SQLException e) {
            model.addAttribute(
                    "erro", "Erro ao acessar o banco de dados.");
            return "login";
        }
        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

