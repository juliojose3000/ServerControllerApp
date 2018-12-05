/*package com.example.julio.management;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;

import com.example.julio.activities.gerencial.Gerencial;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.ArrayList;

import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

public class ShowNotifications extends AsyncTask<String, Void, Void> {

    private ArrayList<String> lista1 = new ArrayList<>();

    private Session session;

    private Context mComtext;

    public void setContext(Context mComtext){
        this.mComtext = mComtext;
    }


    @Override
    protected Void doInBackground(String... params) {

        while(true){

            connectWithServerUbuntu(params);

            System.out.println("Hola a todos");

            try{Thread.sleep(5000);}catch(Exception ee){}

            getPercentMemory();

        }//fin del ciclo infinito

    }


    private void connectWithServerUbuntu(String[] params){
        String userServer =  params[0]; // enter any command you need to execute
        String ipServer =  params[1]; // enter any command you need to execute
        int portServer =  Integer.parseInt(params[2]); // enter any command you need to execute
        String password = params[3];

        try {
            JSch jsch = new JSch();

            String host = userServer+"@"+ipServer;// enter username and ipaddress for machine you need to connect

            String user = host.substring(0, host.indexOf('@'));
            host = host.substring(host.indexOf('@') + 1);

            session = jsch.getSession(user, host, portServer);

            // username and password will be given via UserInfo interface.
            MyUserInfo ui = new MyUserInfo();
            ui.setPasswd(password);
            session.setUserInfo(ui);
            session.connect();

            Channel canalServidor=session.openChannel("exec");
            ((ChannelExec)canalServidor).setCommand("free -m -h");

            canalServidor.setInputStream(null);

            ((ChannelExec)canalServidor).setErrStream(System.err);

            InputStream informacionDelServidor =canalServidor.getInputStream();

            canalServidor.connect();

            byte[] arregloBytes=new byte[1024];
            while(true){
                while (true) {
                    while (informacionDelServidor.available() > 0) {
                        int i = informacionDelServidor.read(arregloBytes, 0, 1024);
                        if (i < 0) {
                            break;
                        }
                        //meto el resultado de la ejecucion del comando en text
                        String text = new String(arregloBytes, 0, i);
                        String[] rowsText = text.split("\n");
                        //este for lo que hace es omiter el encabezado de las consultas que lo tienen
                        for (int j = 1; j<2; j++) {
                            lista1.add(rowsText[j]);
                        }
                    }
                    if (canalServidor.isClosed()) {
                        System.out.println("exit-status: " + canalServidor.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ee) {
                    }


                }
                if(canalServidor.isClosed()){
                    System.out.println("exit-status: "+canalServidor.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            canalServidor.disconnect();
            session.disconnect();


        } catch (Exception e) {

        }

    }

    private void getPercentMemory(){
        String[] rowParts=null;
        for(String row: lista1){
            rowParts = row.split("\\s+");
        }

        double avaibleMemory = isInMB(rowParts[6]);

        if(avaibleMemory<800){

        }


    }

    private double isInMB(String data){

        double x = Double.parseDouble(data.substring(0, data.length()-1));

        if(data.charAt(data.length()-1)=='G'){

            return x*1024;

        }else{

            return x;

        }

    }

}
*/