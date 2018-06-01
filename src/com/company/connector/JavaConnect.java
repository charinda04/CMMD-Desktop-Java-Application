/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.connector;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Charinda
 */
public class JavaConnect {
    
    Connection conn = null;
    
    public static Connection ConnectorDb(){
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/company","root","");
            return conn;
        }
        catch(Exception ex){
            return null;
        }
    }
    
}
