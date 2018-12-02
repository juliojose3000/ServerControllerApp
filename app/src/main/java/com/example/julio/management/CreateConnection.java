package com.example.julio.management;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnection {

    public CreateConnection(){

    }

    public Connection createConnection(String user, String password, String database, String server) throws ClassNotFoundException, SQLException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //politica de connexion

        StrictMode.setThreadPolicy(policy);

        Connection connection = null;

        String connectionUrl = null;

        Class.forName("net.sourceforge.jtds.jdbc.Driver");

        connectionUrl = "jdbc:jtds:sqlserver://"+server+"//"+database+";user="+user+";password="+password+";";

        connection = DriverManager.getConnection(connectionUrl);

        return connection;

    }



}
