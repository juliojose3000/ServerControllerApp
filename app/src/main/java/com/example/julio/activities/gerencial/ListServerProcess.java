package com.example.julio.activities.gerencial;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.julio.management.ConectionServer;
import com.example.julio.management.ConnectServerUbuntu;
import com.example.julio.management.ConnectionDataBaseSQLServer;
import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListServerProcess extends AppCompatActivity {

    private ListView listViewProcess;

    private ArrayList<String> listPIDNameProcess;

    private ArrayList<String> listPIDProcess;

    private ArrayList<String> listUsers;

    private ConnectServerUbuntu processServer;

    private ConnectServerUbuntu usersServer;

    private ConnectServerUbuntu executeCommands;

    private Spinner spinnerUsers;

    private String username;

    private String password;

    private int port;

    private String ip;

    private String pid_processSelected;

    private Button buttonKill;

    private ProgressBar progressBar;

    private Handler handler;

    private int i = 0;

    PopupWindow popupWindow;

    boolean click = true;

    LinearLayout layout;

    LinearLayout.LayoutParams params;

    boolean isShowing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_server_process);

        listViewProcess = findViewById(R.id.listView_process);

        progressBar = new ProgressBar(this);

        popupWindow = new PopupWindow(this);

        layout = new LinearLayout(this);

        listPIDNameProcess = new ArrayList<>();

        handler = new Handler();

        listPIDProcess = new ArrayList<>();

        listUsers = new ArrayList<>();

        Bundle extras = getIntent().getExtras();

        username = extras.getString("username");

        password = extras.getString("password");

        port = extras.getInt("port");

        ip = extras.getString("ip");

        spinnerUsers = findViewById(R.id.spinner_users);

        buttonKill = findViewById(R.id.button_killProcess);

        CountDownTimer timer=new CountDownTimer(1,1) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if(isShowing){
                    //CLOSE
                }
                else{
                    isShowing=true;

                }
            }
        };
        timer.start();

        processServer = new ConnectServerUbuntu("ps -A", username, ip, port, password);
        processServer.run();

        usersServer = new ConnectServerUbuntu("ps -aux", username, ip, port, password);
        usersServer.run();

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ListServerProcess.this, "Cargando procesos, por favor espera.",Toast.LENGTH_SHORT).show();
                    }
                }).start();

                refreshProcess(arg1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        fillSpinnerUsers();

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(progressBar, params);
        popupWindow.setContentView(layout);

    }//onCreate

    //--------------------------------------------------------------------------------------------//

    @Override
    protected void onPause() {
        super.onPause();
        //popupWindow.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }

    public void fillSpinnerUsers() {

        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        ListServerProcess.this,
                        android.R.layout.simple_spinner_item,
                        listUsers);

                spinnerUsers.setAdapter(arrayAdapter);
            }
        }).start();

    }

    public void fillListViewProcess(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectionDataBaseSQLServer connectionDataBaseSQLServer  = new ConnectionDataBaseSQLServer();
                connectionDataBaseSQLServer.insertProcessInDB(listPIDNameProcess); //inserto en la base los procesos listados
            }
        }).start();
        extractDataFromServerQuery();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ListServerProcess.this,
                android.R.layout.simple_list_item_checked,
                listPIDNameProcess );

        listViewProcess.setAdapter(arrayAdapter);
        listViewProcess.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        showError();
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


        ConnectServerUbuntu connectionServer = new ConnectServerUbuntu("/home/"+username+"/Desktop/processDetails "+pid_processSelected, username, ip, port, password);
        connectionServer.run();

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
        if(userName.equals("Todos")){

            processServer = new ConnectServerUbuntu("ps -A", username, ip, port, password);
            processServer.run();

        }else{
            //los procesos que quiero ver son de un usuario especifico
            processServer = new ConnectServerUbuntu("ps -U "+userName+" -u "+userName, username, ip, port, password);
            processServer.run();
        }

        fillListViewProcess();



    }

    public void killProcess(View v){

        String pid = pid_processSelected;

        if(pid!=null){
            executeCommands = new ConnectServerUbuntu("kill "+pid, username, ip, port, password);
            executeCommands.run();

            refreshProcess(v);

            Toast.makeText(this, "Se ha matado al proceso con PID = "+pid, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Seleccione un proceso", Toast.LENGTH_SHORT).show();
        }



    }

    public void showError(){
        if(listPIDNameProcess.size()==0){
            Toast.makeText(this,"Error al conectar con el servidor "+ip,Toast.LENGTH_LONG).show();
        }

    }

    public void showProgressBar(){
        if (click) {
            popupWindow.showAtLocation(layout, Gravity.CENTER, 10, 10);
            //ventanaEmergente.update(50, 50, 300, 80);
            click = false;
        } else {
            popupWindow.dismiss();
            click = true;
        }

    }

    public void refreshProcessButton(View v){

        refreshProcess(v);

        Toast.makeText(this, "Se han actualizado los procesos del servidor", Toast.LENGTH_SHORT).show();

    }



}
