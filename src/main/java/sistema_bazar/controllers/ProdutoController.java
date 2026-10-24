
package sistema_bazar.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sistema_bazar.model.Lote;
import sistema_bazar.model.OrgaoFiscalizador;
import sistema_bazar.model.Produto;
import sistema_bazar.repositories.LoteRepository;
import sistema_bazar.repositories.ProdutoRepository;
import java.sql.SQLException;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository =
            new ProdutoRepository();

    private final LoteRepository loteRepository =
            new LoteRepository();

    @GetMapping("/lote/{id}")
    public String listar(@PathVariable int id,
                         HttpSession session,
                         Model model) {

        String tipo = (String) session.getAttribute("tipoUsuario");

        if (session.getAttribute("tipoUsuario") == null) {
            return "redirect:/login";
        }

        try {
            Lote lote = loteRepository.buscarPorId(id);
            if (lote == null) {
                return "redirect:/lotes";
            }
            if ("FISCALIZADOR".equals(tipo)) {
                OrgaoFiscalizador orgao =
                        (OrgaoFiscalizador) session.getAttribute(
                                "orgaoFiscalizador");

                if (orgao == null ||
                        lote.getOrgaoFiscalizadorId() != orgao.getId()) {
                    return "redirect:/lotes";
                }
            } else if ("DONATARIO".equals(tipo)) {
                sistema_bazar.model.OrgaoDonatario orgao =
                        (sistema_bazar.model.OrgaoDonatario)
                                session.getAttribute("orgaoDonatario");

                if (orgao == null ||
                        lote.getOrgaoDonatarioId() != orgao.getId()) {
                    return "redirect:/lotes";
                }
            } else {
                return "redirect:/login";
            }

            model.addAttribute("lote", lote);
            model.addAttribute("produtos", produtoRepository.listarPorLote(id));

            return "produtos";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao consultar o lote ou seus produtos.");
            return "produtos";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto,
                         @RequestParam int loteId,
                         HttpSession session) {

        if (!"FISCALIZADOR".equals(
                session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }

        try {
            OrgaoFiscalizador orgao =
                    (OrgaoFiscalizador) session.getAttribute(
                            "orgaoFiscalizador");

            Lote lote = loteRepository.buscarPorId(loteId);

            if (lote == null
                    || lote.getOrgaoFiscalizadorId() != orgao.getId()) {
                return "redirect:/lotes";
            }

            produto.setLoteId(loteId);
            produtoRepository.cadastrar(produto);

            return "redirect:/produtos/lote/" + loteId;
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/produtos/lote/" + loteId + "?erro";
        }
    }

    @PostMapping("/excluir")
    public String excluir(
            @RequestParam int codigo,
            @RequestParam int loteId,
            HttpSession session) throws SQLException {

        if (!"FISCALIZADOR".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }

        OrgaoFiscalizador orgao =
                (OrgaoFiscalizador) session.getAttribute("orgaoFiscalizador");

        if (orgao == null) {
            return "redirect:/login";
        }

        Lote lote = loteRepository.buscarPorId(loteId);

        if (lote == null || lote.getOrgaoFiscalizadorId() != orgao.getId()) {
            return "redirect:/lotes";
        }

        Produto produto = produtoRepository.buscarPorCodigo(codigo);

        if (produto == null || produto.getLoteId() != loteId) {
            return "redirect:/produtos/lote/" + loteId;
        }

        produtoRepository.excluir(codigo);

        return "redirect:/produtos/lote/" + loteId;
    }

}
