package com.example.julio.activities.gerencial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.julio.management.ConectionServer;
import com.example.julio.management.ConnectServerUbuntu;
import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

import java.util.ArrayList;

public class ChangePermissionsFiles extends AppCompatActivity {

    private ArrayList<String> permitsFilesForWillApply;

    private String permitsUser;

    private String permitsGroup;

    private String permitsWorld;

    private String fileName;

    //---------------------------------------Checkbox---------------------------------------------//

    private CheckBox userReadPermit;

    private CheckBox userExecutePermit;

    private CheckBox userWritePermit;

    private CheckBox groupReadPermit;

    private CheckBox groupExecutePermit;

    private CheckBox groupWritePermit;

    private CheckBox worldReadPermit;

    private CheckBox worldExecutePermit;

    private CheckBox worldWritePermit;

    //-----------------------------------Server-------------------------------//

    private String username;

    private String password;

    private int port;

    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_permissions_files);
        Bundle bundle = getIntent().getExtras();

        permitsFilesForWillApply = new ArrayList<>();

        String permits = bundle.getString("permitsFiles").subSequence(1, 10).toString();

        permitsUser = permits.substring(0,3);

        permitsGroup = permits.substring(3,6);

        permitsWorld = permits.substring(6,9);

        this.fileName = bundle.getString("fileName");

        username = bundle.getString("username");

        password = bundle.getString("password");

        port = bundle.getInt("port");

        ip = bundle.getString("ip");

        userReadPermit = findViewById(R.id.checkbox_userRead);

        userExecutePermit = findViewById(R.id.checkBox_userExecute);

        userWritePermit = findViewById(R.id.checkBox_userWrite);

        groupReadPermit = findViewById(R.id.checkbox_groupRead);

        groupExecutePermit = findViewById(R.id.checkBox_groupExecute);

        groupWritePermit = findViewById(R.id.checkBox_groupWrite);

        worldReadPermit = findViewById(R.id.checkbox_worldRead);

        worldExecutePermit = findViewById(R.id.checkBox_worldExecute);

        worldWritePermit = findViewById(R.id.checkBox_worldWrite);

        setUserPermitsChecks(permitsUser);

        setGroupPermitsChecks(permitsGroup);

        setWorldPermitsChecks(permitsWorld);
    }

    private void setUserPermitsChecks(String userPermitsFile){

        int amountPermitsOfUser = userPermitsFile.length();

        for(int i = 0; i<amountPermitsOfUser; i++){

            if(userPermitsFile.charAt(i)=='r'){
                userReadPermit.setChecked(true);
            }else if(userPermitsFile.charAt(i)=='w'){
                userWritePermit.setChecked(true);
            }else if(userPermitsFile.charAt(i)=='x'){
                userExecutePermit.setChecked(true);
            }

        }

    }

    private void setGroupPermitsChecks(String userPermitsFile){

        int amountPermitsOfUser = userPermitsFile.length();

        for(int i = 0; i<amountPermitsOfUser; i++){

            if(userPermitsFile.charAt(i)=='r'){
                groupReadPermit.setChecked(true);
            }else if(userPermitsFile.charAt(i)=='w'){
                groupWritePermit.setChecked(true);
            }else if(userPermitsFile.charAt(i)=='x'){
                groupExecutePermit.setChecked(true);
            }

        }

    }

    private void setWorldPermitsChecks(String userPermitsFile){

        int amountPermitsOfUser = userPermitsFile.length();

        for(int i = 0; i<amountPermitsOfUser; i++){

            if(userPermitsFile.charAt(i)=='r'){
                worldReadPermit.setChecked(true);
            }else if(userPermitsFile.charAt(i)=='w'){
                worldWritePermit.setChecked(true);
            }else if(userPermitsFile.charAt(i)=='x'){
                worldExecutePermit.setChecked(true);
            }

        }

    }

    public void applyChages(View v){

        ConectionServer connectServerUbuntu;

        for(int i = 0; i < permitsFilesForWillApply.size(); i++){

            connectServerUbuntu = new ConectionServer();

            connectServerUbuntu.execute(permitsFilesForWillApply.get(i), username, ip, ""+port, password);

        }

        Toast.makeText(this, "Se han actualizado los permisos del archivo excitosamente!", Toast.LENGTH_SHORT).show();

    }

    public void clickOnCheckBox(View v){

        int idCheckbox = v.getId();
        CheckBox checkBox = findViewById(idCheckbox);

        String operando = "";
        String command = "";

        if(checkBox.isChecked()){
            operando="+";
        }else{
            operando="-";
        }

        if(v.getId()==R.id.checkBox_groupExecute){
            command="g"+operando+"x";
        }else if(v.getId()==R.id.checkBox_groupWrite){
            command="g"+operando+"w";
        }else if(v.getId()==R.id.checkbox_groupRead){
            command="g"+operando+"r";
        }else if(v.getId()==R.id.checkBox_userExecute){
            command="u"+operando+"x";
        }else if(v.getId()==R.id.checkBox_userWrite){
            command="g"+operando+"w";
        }else if(v.getId()==R.id.checkbox_userRead){
            command="g"+operando+"w";
        }else if(v.getId()==R.id.checkBox_worldExecute){
            command="g"+operando+"w";
        }else if(v.getId()==R.id.checkBox_worldWrite){
            command="g"+operando+"w";
        }else if(v.getId()==R.id.checkbox_worldRead){
            command="g"+operando+"w";
        }

        permitsFilesForWillApply.add("/home/"+username+"/Desktop/changePermitsFiles "+fileName+" "+command);

    }

    public void changePermits(View v){

        String octalPermits = "";

        int userPermits = 0;

        int groupPermits = 0;

        int worldPermits = 0;

        if(userReadPermit.isChecked()){
            userPermits+=4;
        }
        if(userExecutePermit.isChecked()){
            userPermits+=1;
        }
        if(userWritePermit.isChecked()){
            userPermits+=2;
        }

        if(groupReadPermit.isChecked()){
            groupPermits+=4;
        }
        if(groupExecutePermit.isChecked()){
            groupPermits+=1;
        }
        if(groupWritePermit.isChecked()){
            groupPermits+=2;
        }

        if(worldReadPermit.isChecked()){
            worldPermits+=4;
        }
        if(worldExecutePermit.isChecked()){
            worldPermits+=1;
        }
        if(worldWritePermit.isChecked()){
            worldPermits+=2;
        }

        octalPermits+=userPermits+""+groupPermits+""+worldPermits;

        ConnectServerUbuntu connectServerUbuntu = new ConnectServerUbuntu("chmod "+octalPermits+" /home/julio/Desktop/"+fileName, username, ip, port, password);

        connectServerUbuntu.run();

        Toast.makeText(this, "Se han actualizado los permisos del archivo excitosamente!", Toast.LENGTH_SHORT).show();

    }


}
