package com.example.julio.activities.gerencial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.julio.management.ConnectionDataBaseSQLServer;
import com.example.julio.objects.MirrorServer;
import com.example.julio.objects.Server;

import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

public class AddWindowsServer extends AppCompatActivity {

    private Intent i;

    private EditText username;

    private EditText password;

    private EditText port;

    private EditText ip;

    private EditText serverName;

    private EditText ipMirror;

    private ConnectionDataBaseSQLServer connectionDataBaseSQLServer;

    //private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_windows_server);
        username = findViewById(R.id.editext_username_w);
        password = findViewById(R.id.editext_password_w);
        port = findViewById(R.id.editext_port_w);
        ip = findViewById(R.id.editext_serverip_w);
        serverName = findViewById(R.id.editext_serverName_w);
        ipMirror = findViewById(R.id.editext_ip_mirror_w);
        connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

    }


    public void addServer(View v){

        i = new Intent(AddWindowsServer.this, Gerencial.class);

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        int port = Integer.parseInt(this.port.getText().toString());
        String ipServer = this.ip.getText().toString();
        String serverName = this.serverName.getText().toString();
        String ipMirror = this.ipMirror.getText().toString();


        Server server = new Server(4, serverName, username, password, port, ipServer, ipMirror);

        String result = connectionDataBaseSQLServer.insertServer(server);

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        startActivity(i);

    }

    public void cancel(View v){

        username.setText("");
        password.setText("");
        port.setText("");
        ip.setText("");
        ipMirror.setText("");

    }

}
