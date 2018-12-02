package com.example.julio.activities.tecnico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.julio.management.ConectionServer;
import com.example.julio.proyectoso_v2.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListServerFiles extends AppCompatActivity {

    private ListView listViewFiles;

    private int position;

    private ArrayList<String> listaNombreFiles;

    private ConectionServer connectionServer;

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

        connectionServer = new ConectionServer();

        try {

            connectionServer.execute("ls -l /home/"+username+"/Desktop/", username, ip, ""+port, password).get();
            getFileNameFromQuery();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_list_server_files_tecnico);

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

    }

    public void deleteFileTEC(View v){

        String fileName = listaNombreFiles.get(position);

        try {
            connectionServer = new ConectionServer();
            connectionServer.execute("rm -R /home/"+username+"/Desktop/"+fileName, username, ip, ""+port, password).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listaNombreFiles.remove(position);

        fillListViewFileName();

    }

    private void saveDetailsItem(int position){

        this.position = position;

    }



}
