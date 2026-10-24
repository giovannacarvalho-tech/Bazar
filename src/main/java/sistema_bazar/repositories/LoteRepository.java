package sistema_bazar.repositories;

import sistema_bazar.config.ConnectionManager;
import sistema_bazar.model.Lote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoteRepository {

    public void cadastrar(Lote lote) throws SQLException {

        String sql = """
                INSERT INTO lote
                (data_entrega, observacao, status,
                 orgao_fiscalizador_id, orgao_donatario_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(
                             sql, Statement.RETURN_GENERATED_KEYS)) {

            if (lote.getDataEntrega() == null) {
                comando.setNull(1, java.sql.Types.DATE);
            } else {
                comando.setDate(
                        1, java.sql.Date.valueOf(lote.getDataEntrega()));
            }

            comando.setString(2, lote.getObservacao());
            comando.setString(3, lote.getStatus());
            comando.setInt(4, lote.getOrgaoFiscalizadorId());
            comando.setInt(5, lote.getOrgaoDonatarioId());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    lote.setId(resultado.getInt(1));
                }
            }
        }
    }

    public List<Lote> listarPorOrgaoFiscalizador(int orgaoFiscalizadorId)
            throws SQLException {

        List<Lote> lotes = new ArrayList<>();

        String sql = """
                SELECT id, data_entrega, observacao, status,
                       orgao_fiscalizador_id, orgao_donatario_id
                FROM lote
                WHERE orgao_fiscalizador_id = ?
                ORDER BY data_entrega DESC, id DESC
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setInt(1, orgaoFiscalizadorId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    lotes.add(mapearLote(resultado));
                }
            }
        }

        return lotes;
    }

    public List<Lote> listarPorOrgaoDonatario(int orgaoDonatarioId)
            throws SQLException {

        List<Lote> lotes = new ArrayList<>();

        String sql = """
                SELECT id, data_entrega, observacao, status,
                       orgao_fiscalizador_id, orgao_donatario_id
                FROM lote
                WHERE orgao_donatario_id = ?
                ORDER BY data_entrega DESC, id DESC
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setInt(1, orgaoDonatarioId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    lotes.add(mapearLote(resultado));
                }
            }
        }

        return lotes;
    }

    public Lote buscarPorId(int id) throws SQLException {

        String sql = """
                SELECT id, data_entrega, observacao, status,
                       orgao_fiscalizador_id, orgao_donatario_id
                FROM lote
                WHERE id = ?
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return mapearLote(resultado);
                }
            }
        }

        return null;
    }

    private Lote mapearLote(ResultSet resultado) throws SQLException {

        Lote lote = new Lote();

        lote.setId(resultado.getInt("id"));

        java.sql.Date data = resultado.getDate("data_entrega");
        if (data != null) {
            lote.setDataEntrega(data.toLocalDate());
        }

        lote.setObservacao(resultado.getString("observacao"));
        lote.setStatus(resultado.getString("status"));
        lote.setOrgaoFiscalizadorId(
                resultado.getInt("orgao_fiscalizador_id"));
        lote.setOrgaoDonatarioId(
                resultado.getInt("orgao_donatario_id"));

        return lote;
    }
}
