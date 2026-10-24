package sistema_bazar.repositories;

import sistema_bazar.config.ConnectionManager;
import sistema_bazar.model.OrgaoFiscalizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrgaoFiscalizadorRepository {

    public void cadastrar(OrgaoFiscalizador orgao)
            throws SQLException {

        String sql = """
                INSERT INTO orgao_fiscalizador
                (nome, descricao, endereco, telefone,
                 horario_funcionamento, email, senha_hash)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(
                             sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, orgao.getNome());
            comando.setString(2, orgao.getDescricao());
            comando.setString(3, orgao.getEndereco());
            comando.setString(4, orgao.getTelefone());
            comando.setString(5, orgao.getHorarioFuncionamento());
            comando.setString(6, orgao.getEmail());
            comando.setString(7, orgao.getSenhaHash());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    orgao.setId(resultado.getInt(1));
                }
            }
        }
    }

    public OrgaoFiscalizador buscarPorEmail(String email)
            throws SQLException {

        String sql = """
                SELECT id, nome, descricao, endereco, telefone,
                       horario_funcionamento, email, senha_hash
                FROM orgao_fiscalizador
                WHERE email = ?
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(1, email);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return mapearOrgao(resultado);
                }
            }
        }

        return null;
    }

    public List<OrgaoFiscalizador> listarTodos()
            throws SQLException {

        List<OrgaoFiscalizador> orgaos = new ArrayList<>();

        String sql = """
                SELECT id, nome, descricao, endereco, telefone,
                       horario_funcionamento, email
                FROM orgao_fiscalizador
                ORDER BY nome
                """;

        try (Connection conexao = ConnectionManager.getConnection();
             PreparedStatement comando =
                     conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                orgaos.add(mapearOrgaoSemSenha(resultado));
            }
        }

        return orgaos;
    }

    private OrgaoFiscalizador mapearOrgao(ResultSet resultado)
            throws SQLException {

        OrgaoFiscalizador orgao = new OrgaoFiscalizador();

        orgao.setId(resultado.getInt("id"));
        orgao.setNome(resultado.getString("nome"));
        orgao.setDescricao(resultado.getString("descricao"));
        orgao.setEndereco(resultado.getString("endereco"));
        orgao.setTelefone(resultado.getString("telefone"));
        orgao.setHorarioFuncionamento(
                resultado.getString("horario_funcionamento"));
        orgao.setEmail(resultado.getString("email"));
        orgao.setSenhaHash(resultado.getString("senha_hash"));

        return orgao;
    }
    private OrgaoFiscalizador mapearOrgaoSemSenha(ResultSet resultado)
            throws SQLException {

        OrgaoFiscalizador orgao = new OrgaoFiscalizador();

        orgao.setId(resultado.getInt("id"));
        orgao.setNome(resultado.getString("nome"));
        orgao.setDescricao(resultado.getString("descricao"));
        orgao.setEndereco(resultado.getString("endereco"));
        orgao.setTelefone(resultado.getString("telefone"));
        orgao.setHorarioFuncionamento(
                resultado.getString("horario_funcionamento"));
        orgao.setEmail(resultado.getString("email"));

        return orgao;
    }
}

