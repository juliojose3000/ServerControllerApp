package com.example.julio.activities.gerencial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.julio.management.ConnectionDataBaseSQLServer;
import com.example.julio.objects.Server;

import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;


public class AddServer extends AppCompatActivity {

    private Intent i;

    private EditText username;

    private EditText password;

    private EditText port;

    private EditText ip;

    private EditText serverName;

    private ConnectionDataBaseSQLServer connectionDataBaseSQLServer;

    //private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_server);
        username = findViewById(R.id.editext_username2);
        password = findViewById(R.id.editext_password2);
        port = findViewById(R.id.editext_port2);
        ip = findViewById(R.id.editext_serverip2);
        serverName = findViewById(R.id.editext_serverName2);
        connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

    }


    public void addServer(View v){

        //server = new Server ();

        i = new Intent(AddServer.this, Gerencial.class);

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        int port = Integer.parseInt(this.port.getText().toString());
        String ipServer = this.ip.getText().toString();
        String serverName = this.serverName.getText().toString();


        Server server = new Server(4, serverName, username, password, port, ipServer);

        String result = connectionDataBaseSQLServer.insertServer(server);

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        startActivity(i);

    }

    public void cancel(View v){

        username.setText("");
        password.setText("");
        port.setText("");
        ip.setText("");
        
    }

}
