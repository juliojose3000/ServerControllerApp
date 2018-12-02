package com.example.julio.activities.gerencial;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.julio.management.ConnectionDataBaseSQLServer;
import com.example.julio.objects.Server;
import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

import java.util.ArrayList;

public class Gerencial extends AppCompatActivity {

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

        setContentView(R.layout.activity_gerencial);

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

    //metodo para mostrar y ocultar el menu
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_gerencial, menu);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == R.id.item_add_server){
            Intent i = new Intent(Gerencial.this, AddServer.class);
            startActivity(i);
        }else if(id == R.id.item_ftp_transfer){
            Intent i = new Intent(Gerencial.this, FTPTransfer.class);
            i.putExtra("username",username);
            i.putExtra("password",password);
            i.putExtra("port",port);
            i.putExtra("ip",ip);

            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
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

    public void seeFiles(View v){

        Intent i = new Intent(Gerencial.this, ListServerFiles.class);

        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("port",port);
        i.putExtra("ip",ip);

        startActivity(i);


    }

    public void seeProcess(View v){

        Intent i = new Intent(Gerencial.this, ListServerProcess.class);

        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("port",port);
        i.putExtra("ip",ip);

        startActivity(i);


    }

    public void editServer(View v){

        Intent i = new Intent (Gerencial.this, EditServers.class);

        i.putExtra("idServer", server.getIdServer());

        startActivity(i);

    }

    public void executeCommands(View v){


    }



}
