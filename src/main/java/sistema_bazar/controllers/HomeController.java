package sistema_bazar.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        String tipo = (String) session.getAttribute("tipoUsuario");

        if (tipo == null) {
            return "redirect:/login";
        }

        model.addAttribute("tipoUsuario", tipo);

        if ("FISCALIZADOR".equals(tipo)) {
            model.addAttribute(
                    "orgao",
                    session.getAttribute("orgaoFiscalizador"));
        } else {
            model.addAttribute(
                    "orgao",
                    session.getAttribute("orgaoDonatario"));
        }

        return "home";
    }
}
