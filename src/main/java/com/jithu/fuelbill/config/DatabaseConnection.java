package com.jithu.fuelbill.config;
import java.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection 
    {
        private static final String url= "jdbc:mysql://localhost:3306/fuelbill",user ="root",password="";
        public static  Connection getConnection() throws SQLException
         {
            return DriverManager.getConnection(url,user,password);
         }
    }
