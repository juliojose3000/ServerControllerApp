package com.example.julio.activities.tecnico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.julio.management.ConectionServer;
import com.example.julio.management.CreateConnection;
import com.example.julio.proyectoso_v2.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListServerProcess extends AppCompatActivity {

    private ListView listViewProcess;

    private ArrayList<String> listPIDNameProcess;

    private ArrayList<String> listPIDProcess;

    private ArrayList<String> listUsers;

    private ConectionServer processServer;

    private ConectionServer usersServer;

    private Spinner spinnerUsers;

    private String username;

    private String password;

    private int port;

    private String ip;

    private String pid_processSelected;

    private ConectionServer conectionServer;

    private Button buttonKill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_server_process_tecnico);

        listViewProcess = findViewById(R.id.listView_process);

        listPIDNameProcess = new ArrayList<>();

        listPIDProcess = new ArrayList<>();

        listUsers = new ArrayList<>();

        processServer = new ConectionServer();

        usersServer = new ConectionServer();

        Bundle extras = getIntent().getExtras();

        username = extras.getString("username");

        password = extras.getString("password");

        port = extras.getInt("port");

        ip = extras.getString("ip");

        spinnerUsers = findViewById(R.id.spinner_users);

        buttonKill = findViewById(R.id.button_killProcess);

        try {
            //usersServer.execute("cut -d: -f1 /etc/passwd", username, ip, ""+port, password).get();
            usersServer.execute("ps -aux", username, ip, ""+port, password).get();
            extractDataFromServerQuery();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listViewProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pid_processSelected = listPIDProcess.get(position);
            }

        });


        spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                refreshProcess(arg1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        fillListViewProcess();
        fillSpinnerUsers();


    }


    public void fillSpinnerUsers() {
        ArrayList<String> list = usersServer.getLista1();
        for (String row:
                list) {
            String[] rowParts = row.split(" ");
            if(!listUsers.contains(rowParts[0])){
                listUsers.add(rowParts[0]);
            }

        }

        listUsers.add(0,"Todos");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listUsers);

        spinnerUsers.setAdapter(arrayAdapter);

    }

    public void fillListViewProcess(){

        ListView lv = findViewById(R.id.listView_process);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                listPIDNameProcess );

        lv.setAdapter(arrayAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    //Aqui extraigo el pid y nombre del proceso de la consulta al servidor del comando ps -aux
    public void extractDataFromServerQuery() {
        listPIDProcess = new ArrayList<>();
        listPIDNameProcess = new ArrayList<>();
        ArrayList<String> list = processServer.getLista1();
        for (int j = 0; j<list.size(); j++) {
            //agrego a la lista solo el nombre del los archivos
            String processName = getProcessName(list.get(j));
            if(!processName.equals("Unknow")){
                String processPID = getProcessPID(list.get(j));
                listPIDNameProcess.add(processPID+" "+processName);
                listPIDProcess.add(processPID);
            }

        }
    }

    private String getProcessName(String row){
        String nombreProceso = "";
        int pivot = 0;
        int i;
        for(i = 0; pivot < 4 && i<row.length(); i++){
            String aux = ""+row.charAt(i);
            if(aux.equals(" ")){
                int k = i;
                if(i<=row.length()-2){
                    String aux2 = ""+row.charAt(++k);
                    if(!aux2.equals(" ")){
                        pivot++;
                    }
                }

            }
        }
        if(pivot==4){
            for(int j=i; j<row.length(); j++){
                nombreProceso+=row.charAt(j);
            }
        }else{
            return "Unknow";
        }

        return nombreProceso;
    }

    private String getProcessPID(String row){
        String nombreProceso = "";
        int pivot = 0;
        int i;
        for(i = 0; pivot < 1 && i<row.length(); i++){
            String aux = ""+row.charAt(i);
            if(aux.equals(" ")){
                int k = i;
                if(i<=row.length()-2){
                    String aux2 = ""+row.charAt(++k);
                    if(!aux2.equals(" ")){
                        pivot++;
                    }
                }

            }
        }
        if(pivot==1){
            for(int j=i; !((""+row.charAt(j)).equals(" ")); j++){
                nombreProceso+=row.charAt(j);
            }
        }else{
            return "Unknow";
        }

        return nombreProceso;
    }

    public void seeDetailsProcess(View v){

        String processDetails = "PID: "+pid_processSelected+"\n";


        ConectionServer connectionServer = new ConectionServer();

        try {

            connectionServer.execute("/home/"+username+"/Desktop/procesos "+pid_processSelected, username, ip, ""+port, password).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<String> lista = connectionServer.getLista0();
        for (String row:
                lista) {
            processDetails+=row+="\n";
        }

        Intent i = new Intent(ListServerProcess.this, SeeDetailsProcess.class);

        i.putExtra("processDetails", processDetails);
        i.putExtra("pidProcess", pid_processSelected);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("ip",ip);
        i.putExtra("port",port);

        startActivity(i);

    }
    public void refreshProcess(View v){

        String userName = spinnerUsers.getSelectedItem().toString();
        processServer = new ConectionServer();
        if(userName.equals("Todos")){

            try {
                processServer.execute("ps -A", username, ip, ""+port, password).get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }else{
            //los procesos que quiero ver son de un usuario especifico
            try {
                processServer.execute("ps -U "+userName+" -u "+userName, username, ip, ""+port, password).get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        extractDataFromServerQuery();

        fillListViewProcess();

    }

    public void insertProcessInDB(ArrayList<String> procesos){
        CreateConnection connectionDataBaseSQLServer = new CreateConnection();
        Connection connection=null;
        try {
            connection = connectionDataBaseSQLServer.createConnection("estudiantesrp","estudiantesrp", "AndroidAppJ","163.178.173.148");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection==null){

            Toast.makeText(ListServerProcess.this, "Error de conección con BD",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            for (int j = 0; j < procesos.size(); j++) {
                String string = procesos.get(j);
                String[] parts = string.split(" ");
                String part1 = parts[0];
                String part2 = parts[1];
                String query= "Insert into   [AndroidAppJ].[dbo].[Server] (PID,name) values ('"+part1+"','"+part2+"')";
                System.out.println(query);
                try {
                    Statement statement= connection.createStatement();
                    //ejecuta la consulta y obtiene resultado
                    statement.executeQuery(query);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void killProcess(View v){
        String pid = pid_processSelected;
        try{
            ConectionServer conectionServer= new ConectionServer();
            conectionServer.execute("kill "+pid, username, ip, ""+port, password).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        refreshProcess(v);

    }




}
