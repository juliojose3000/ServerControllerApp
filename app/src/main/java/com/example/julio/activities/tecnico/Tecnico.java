package com.example.julio.activities.tecnico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.julio.management.ConnectionDataBaseSQLServer;
import com.example.julio.objects.Server;
import com.example.julio.proyectoso_v2.R;

import java.util.ArrayList;

public class Tecnico extends AppCompatActivity {

    private String username;

    private String password;

    private int port;

    private String ip;

    private Server server;

    private ConnectionDataBaseSQLServer connectionDataBaseSQLServer;

    private ArrayList<String> listServers;

    private ListView listViewServers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tecnico);

        listViewServers = findViewById(R.id.listview_servers);

        listViewServers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String serverName = (String) parent.getItemAtPosition(position);

                connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

                server = connectionDataBaseSQLServer.getServerByName(serverName);

                ip = server.getIp();

                username = server.getUsername();

                password = server.getPassword();

                port = server.getPort();
            }

        });

        fillServerInListView();

    }

    //cargo al listview la lista de los servidores
    public void fillServerInListView() {
        connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();// Creo una nueva conexion
        listServers = connectionDataBaseSQLServer.getServersUser();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                listServers );

        listViewServers.setAdapter(arrayAdapter);
        listViewServers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    public void seeFilesTEC(View v){

        Intent i = new Intent(Tecnico.this, ListServerFiles.class);

        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("port",port);
        i.putExtra("ip",ip);

        startActivity(i);


    }

    public void seeProcessTEC(View v){

        Intent i = new Intent(Tecnico.this, ListServerProcess.class);

        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("port",port);
        i.putExtra("ip",ip);

        startActivity(i);


    }

}
