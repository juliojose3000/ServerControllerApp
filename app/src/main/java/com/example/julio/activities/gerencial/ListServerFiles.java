package com.example.julio.activities.gerencial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.julio.management.ConectionServer;
import com.example.julio.management.ConnectServerUbuntu;
import com.example.julio.proyectoso_v2.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListServerFiles extends AppCompatActivity {

    private ListView listViewFiles;

    private int position;

    private ArrayList<String> listaNombreFiles;

    private ConnectServerUbuntu connectionServer;

    private String username;

    private String password;

    private int port;

    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();

        username = extras.getString("username");

        password = extras.getString("password");

        port = extras.getInt("port");

        ip = extras.getString("ip");

        listaNombreFiles = new ArrayList<>();

        connectionServer = new ConnectServerUbuntu("ls -l /home/"+username+"/Desktop/", username, ip, port, password);
        connectionServer.run();
        getFileNameFromQuery();

        setContentView(R.layout.activity_list_server_files);

        fillListViewFileName();

        listViewFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveDetailsItem(position);
            }

        });

    }


    public void getFileNameFromQuery(){

        ArrayList<String> list = connectionServer.getLista1();
        for (int j = 1; j<list.size(); j++) {
            //divido cada fila del resultado en sus diferentes partes (permisos, dueno,
            String[] fileName = list.get(j).split("\\s+|\n");
            //agrego a la lista solo el nombre del los archivos
            listaNombreFiles.add(fileName[8]);

        }
    }

    public void fillListViewFileName() {

        listViewFiles = findViewById(R.id.listaFiles);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                listaNombreFiles );

        listViewFiles.setAdapter(arrayAdapter);
        listViewFiles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        showError();

    }

    public void deleteFile(View v){

        String fileName = listaNombreFiles.get(position);

        connectionServer.setCommandToExecute("rm -R /home/"+username+"/Desktop/"+fileName);
        connectionServer.run();

        listaNombreFiles.remove(position);

        fillListViewFileName();

    }

    private void saveDetailsItem(int position){

        this.position = position;

    }


    public void seePermitsFile(View v){
        String fileName = listaNombreFiles.get(position);
        connectionServer = new ConnectServerUbuntu("ls -l /home/"+username+"/Desktop/"+fileName, username, ip, port, password);
        connectionServer.run();

        Intent i = new Intent(ListServerFiles.this, ChangePermissionsFiles.class);
        ArrayList<String> row = connectionServer.getLista0();
        String permits = getPermits(row);
        i.putExtra("permitsFiles", permits);
        i.putExtra("fileName", fileName);
        i.putExtra("username",username);
        i.putExtra("password", password);
        i.putExtra("port",port);
        i.putExtra("ip",ip);
        startActivity(i);

    }

    private String getPermits(ArrayList<String> row){
        String[] rowParts = row.get(0).split(" ");
        return rowParts[0];
    }

    public void showError(){
        if(listaNombreFiles.size()==0){
            Toast.makeText(this,"Error al conectar con el servidor "+ip,Toast.LENGTH_LONG).show();
        }

    }



}
