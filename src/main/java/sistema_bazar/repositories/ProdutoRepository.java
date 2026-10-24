
package sistema_bazar.repositories;

import sistema_bazar.config.ConnectionManager;
import sistema_bazar.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public void cadastrar(Produto produto) throws SQLException {

        String sql = """
                INSERT INTO produto (nome, descricao, lote_id)
                VALUES (?, ?, ?)
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(
                             sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, produto.getNome());
            comando.setString(2, produto.getDescricao());
            comando.setInt(3, produto.getLoteId());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    produto.setCodigo(resultado.getInt(1));
                }
            }
        }
    }

    public void excluir(int codigo) throws SQLException {
        String sql = "DELETE FROM produto WHERE codigo = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarPorLote(int loteId)
            throws SQLException {

        List<Produto> produtos = new ArrayList<>();

        String sql = """
                SELECT codigo, nome, descricao, lote_id
                FROM produto
                WHERE lote_id = ?
                ORDER BY nome
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setInt(1, loteId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    produtos.add(mapearProduto(resultado));
                }
            }
        }

        return produtos;
    }

    public Produto buscarPorCodigo(int codigo) throws SQLException {

        String sql = """
                SELECT codigo, nome, descricao, lote_id
                FROM produto
                WHERE codigo = ?
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setInt(1, codigo);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return mapearProduto(resultado);
                }
            }
        }

        return null;
    }

    private Produto mapearProduto(ResultSet resultado)
            throws SQLException {

        Produto produto = new Produto();

        produto.setCodigo(resultado.getInt("codigo"));
        produto.setNome(resultado.getString("nome"));
        produto.setDescricao(resultado.getString("descricao"));
        produto.setLoteId(resultado.getInt("lote_id"));

        return produto;
    }
}
