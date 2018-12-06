package com.example.julio.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.julio.activities.gerencial.Gerencial;
import com.example.julio.activities.sysadmin.SysAdmin;
import com.example.julio.activities.tecnico.Tecnico;
import com.example.julio.management.ConnectionDataBaseSQLServer;
import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

//Comentario de prueba
//comentario de juan XD

public class MainActivity extends AppCompatActivity {

    EditText username;

    EditText password;

    ConnectionDataBaseSQLServer connectionDataBaseSQLServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);

        password = findViewById(R.id.password);

    }

    public void logon(View v){
        String username = "'"+this.username.getText().toString()+"'";

        String password = "'"+this.password.getText().toString()+"'";

        connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

        String rolName = connectionDataBaseSQLServer.verifyUserInBD(username, password);

        Intent i = null;

        if(rolName.equals("Gerencial")){

            i = new Intent(MainActivity.this, Gerencial.class);

        } else if (rolName.equals("SysAdmin")){

            i = new Intent(MainActivity.this, SysAdmin.class);

        } else if (rolName.equals("Tecnico")){

            i = new Intent(MainActivity.this, Tecnico.class);

        }else{
            Toast.makeText(this, rolName, Toast.LENGTH_LONG).show();
        }

        if(i!=null){
            startActivity(i);
        }

    }

    public void Cancel(View v){
        password.setText("");
        username.setText("");
    }


}
