/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infox.dal;

import java.sql.*;

/**
 *
 * @author gabri
 */
public class ModuloConexao {

    //metodo para estabelecer conexão
    public static Connection conector() {
        java.sql.Connection conexao = null;
        // a linha abaixo "chama" o driver
        String driver = "com.mysql.cj.jdbc.Driver";
        //armazenar informações do banco
        String url = "jdbc:mysql:// 192.168.0.118:3306/dbinfox?characterEncoding=utf-8"; //ativar linha quando tiver em rede principal
        //String url = "jdbc:mysql://1ocalhost:3306/dbinfox?characterEncoding=utf-8";// ativar linha quando tiver em outra rede
        String user = "dba"; // quando em outra rede o user é root //quando em rede principal o user dba 
        String password = "Gabriel@2801"; // quando em  outra rede a senha é nula// quando em rede principal a senha Gabriel@2801
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            //a linha abaixo serve para o desenvolvedor identificar erros de conexão com banco de dados 
            //System.out.println(e);
            return null;
        }
    }
}
