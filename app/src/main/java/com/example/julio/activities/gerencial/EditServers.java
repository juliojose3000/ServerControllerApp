package com.example.julio.activities.gerencial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.julio.management.ConnectionDataBaseSQLServer;
import com.example.julio.objects.Server;
import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

public class EditServers extends AppCompatActivity{


        private EditText username;

        private EditText password;

        private EditText port;

        private EditText ip;

        private EditText serverName;

        private int idServer;

        private ConnectionDataBaseSQLServer connectionDataBaseSQLServer;

        private Server server;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_servers);

        Bundle extras = getIntent().getExtras();

        username = findViewById(R.id.editext_username2);
        password = findViewById(R.id.editext_password2);
        port = findViewById(R.id.editext_port2);
        ip = findViewById(R.id.editext_serverip2);
        serverName = findViewById(R.id.editext_serverName2);
        connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

        idServer = extras.getInt("idServer");

        server = connectionDataBaseSQLServer.getServerById(idServer);

        username.setText(server.getUsername());

        password.setText(server.getPassword());

        port.setText(""+server.getPort());

        ip.setText(server.getIp());


        serverName.setText(server.getServerName());

    }


    public void editServerSelected(View v){

            Server ServerToEdit = connectionDataBaseSQLServer.getServerById(this.idServer);

            Server editedServer = new Server(ServerToEdit.getIdServer(), serverName.getText().toString(), username.getText().toString(), password.getText().toString(), Integer.parseInt(port.getText().toString()), ip.getText().toString());

            String result = connectionDataBaseSQLServer.editServer(editedServer);
            if(result.equals("Servidor editado correctamente")){
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                Intent i  =  new Intent(EditServers.this, Gerencial.class);
                startActivity(i);
            }else{
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }

    }

    public void cancel(View v){

        username.setText("");
        password.setText("");
        port.setText("");
        ip.setText("");
        serverName.setText("");

    }



}
