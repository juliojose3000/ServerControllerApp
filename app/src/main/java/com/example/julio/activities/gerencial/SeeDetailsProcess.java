package com.example.julio.activities.gerencial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julio.management.ConectionServer;
import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;
import java.util.concurrent.ExecutionException;

public class SeeDetailsProcess extends AppCompatActivity {

    private String processDetails;

    private  String pidSelected;

    private TextView textView;

    private String username;

    private String password;

    private int port;

    private String ip;

    private ListServerProcess listServerProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_details_process);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("username");

        password = extras.getString("password");

        port = extras.getInt("port");

        ip = extras.getString("ip");

        processDetails = extras.getString("processDetails");

        pidSelected = extras.getString("pidProcess");

        textView = findViewById(R.id.textView_processDetails);

        textView.setText(processDetails);

    }

    public void StopProcess(View v){
        String pid = pidSelected;
        System.out.println("kill -STOP "+pid);
        try{
            ConectionServer conectionServer= new ConectionServer();
            conectionServer.execute("kill -STOP"+" "+pid, username, ip, ""+port, password).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(this,"Se detuvo con exito",Toast.LENGTH_LONG).show();

    }

    public void ContinueProcess(View v){
        String pid = pidSelected;
        try{
            ConectionServer conectionServer= new ConectionServer();
            conectionServer.execute("kill -CONT"+" "+pid, username, ip, ""+port, password).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(this,"Se reanudó con éxito",Toast.LENGTH_LONG).show();

    }


    public void createSon(View v){
        try{
            ConectionServer conectionServer= new ConectionServer();
            conectionServer.execute("/home/"+username+"/Desktop/Son/", username, ip, ""+port, password).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(this,"Se creó un proceso hijo",Toast.LENGTH_LONG).show();


    }




}

