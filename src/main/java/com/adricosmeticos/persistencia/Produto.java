/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adricosmeticos.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Gabriel
 */
public class Produto {

    public Map<String, List<com.adricosmeticos.modelo.Produto>> obterLista() throws SQLException {
        Map<String, List<com.adricosmeticos.modelo.Produto>> listaProdutos = new HashMap<>();
        listaProdutos.put("lash", new ArrayList<>());
        listaProdutos.put("salao", new ArrayList<>());
        listaProdutos.put("manicurePedicure", new ArrayList<>());

        String textSql = "SELECT * FROM public.produto";

        // Pegando a conexão da ConnectionFactory
        try (Connection conexao = ConnectionFactory.obterConexao(); PreparedStatement getLista = conexao.prepareStatement(textSql); ResultSet resultado = getLista.executeQuery()) {

            while (resultado.next()) {
                com.adricosmeticos.modelo.Produto produto = new com.adricosmeticos.modelo.Produto();
                produto.setId(resultado.getString("id"));
                produto.setColecao(resultado.getString("colecao"));
                produto.setDescricao(resultado.getString("descricao"));
                produto.setEstoque(resultado.getInt("estoque"));
                produto.setPreco(resultado.getBigDecimal("preco"));
                produto.setNome(resultado.getString("nome"));
                produto.setImagem(resultado.getString("imagem"));

                String colecao = produto.getColecao();

                // Adiciona somente se a coleção for uma das esperadas
                if (listaProdutos.containsKey(colecao)) {
                    listaProdutos.get(colecao).add(produto);
                }
            }

        } catch (SQLException e) {
            // Você pode tratar o erro ou lançar de novo
            e.printStackTrace();
            throw e;
        }

        return listaProdutos;
    }

    public boolean inserirProduto(com.adricosmeticos.modelo.Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (id, nome, descricao, preco, estoque, imagem, colecao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getId());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setInt(5, produto.getEstoque());
            stmt.setString(6, produto.getImagem());
            stmt.setString(7, produto.getColecao());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new SQLException("Produto já cadastrado!", e);
            } else {
                throw new SQLException("Erro ao inserir produto: " + e.getMessage(), e);
            }
        }
    }
    
    public boolean editarProduto(com.adricosmeticos.modelo.Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, estoque = ?, preco = ? WHERE id = ? AND colecao = ?";
        
        try (Connection conexao = ConnectionFactory.obterConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getEstoque());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setString(5, produto.getId());
            stmt.setString(6, produto.getColecao());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new SQLException("Erro ao editar produto: " + e.getMessage(), e);
        }
    }
    
    public Optional<com.adricosmeticos.modelo.Produto> obterProduto(String id, String colecao){
        String sql = "select * from produto where id = ? and colecao = ?";
        
        try(Connection conexao = ConnectionFactory.obterConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)){
            com.adricosmeticos.modelo.Produto produto = new com.adricosmeticos.modelo.Produto();
            
            stmt.setString(1, id);
            stmt.setString(2, colecao);
            ResultSet resultado = stmt.executeQuery();
            
            if(resultado.next()){
                produto.setId(resultado.getString("id"));
                produto.setColecao(resultado.getString("colecao"));
                produto.setImagem(resultado.getString("imagem"));
                produto.setNome(resultado.getString("nome"));
                produto.setPreco(resultado.getBigDecimal("preco"));
                produto.setEstoque(resultado.getInt("estoque"));
                produto.setDescricao(resultado.getString("descricao"));
                return Optional.of(produto);
            } 
            
        } catch(SQLException e){
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    public Boolean excluirProduto(String id, String colecao) {
    String sql = "DELETE FROM produto WHERE id = ? AND colecao = ?";

    try (Connection conexao = ConnectionFactory.obterConexao(); 
         PreparedStatement stmt = conexao.prepareStatement(sql)) {

        stmt.setString(1, id);
        stmt.setString(2, colecao);

        int linhasAfetadas = stmt.executeUpdate();
        return linhasAfetadas > 0;

    } catch (SQLException e) {
        e.printStackTrace(); // ou use um logger
        return false;
    }
}

}

class ConnectionFactory {

    private static final String URL = "jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres";
    private static final String USUARIO = "postgres.qkzcqadlzgjfbxhosshi";
    private static final String SENHA = "74115987";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // registra o driver (opcional no Java moderno)
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do PostgreSQL não encontrado", e);
        }
    }

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
