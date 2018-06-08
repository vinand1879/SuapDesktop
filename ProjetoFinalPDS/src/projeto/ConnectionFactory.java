/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Solange
 */
public class ConnectionFactory {
   public Connection getConnection(){
        //System.out.println("Conectado ao banco");
        try{
            return DriverManager.getConnection("jdbc:mysql://localhost/projeto", "root", "");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    } 
}
