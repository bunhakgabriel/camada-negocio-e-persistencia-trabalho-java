package com.adricosmeticos.negocio;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Gabriel
 */
public class Produto {

    public Map<String, List<com.adricosmeticos.modelo.Produto>> obterLista() throws SQLException {
        com.adricosmeticos.persistencia.Produto produtoPersist = new com.adricosmeticos.persistencia.Produto();
        Map<String, List<com.adricosmeticos.modelo.Produto>> listaProdutos = produtoPersist.obterLista();

        return listaProdutos;
    }

    public boolean salvarProduto(com.adricosmeticos.modelo.Produto produto) throws SQLException {
        com.adricosmeticos.persistencia.Produto produtoPersist = new com.adricosmeticos.persistencia.Produto();
        return produtoPersist.inserirProduto(produto);
    }
    
    public boolean editarProduto(com.adricosmeticos.modelo.Produto produto) throws SQLException {
        com.adricosmeticos.persistencia.Produto produtoPersist = new com.adricosmeticos.persistencia.Produto();
        return produtoPersist.editarProduto(produto);
    }
    
    public Optional<com.adricosmeticos.modelo.Produto> obterProduto(String id, String colecao) throws SQLException {
        com.adricosmeticos.persistencia.Produto produtoPersist = new com.adricosmeticos.persistencia.Produto();
        return produtoPersist.obterProduto(id, colecao);
    }

    public Boolean excluirProduto(String id, String colecao) throws SQLException {
        com.adricosmeticos.persistencia.Produto produtoPersist = new com.adricosmeticos.persistencia.Produto();
        return produtoPersist.excluirProduto(id, colecao);
    }
    
}
