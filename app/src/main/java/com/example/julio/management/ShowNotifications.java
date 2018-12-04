package com.example.julio.management;

import android.os.AsyncTask;

import com.example.julio.activities.gerencial.Gerencial;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import java.util.ArrayList;

public class ShowNotifications extends AsyncTask<String, Void, Void> {

    private ArrayList<String> lista1 = new ArrayList<>();

    private ArrayList<String> lista0 = new ArrayList<>();

    ConnectServerUbuntu connectServerUbuntu;

    private String username;

    private String password;

    private int port;

    private String ip;

    private Session session;

    Gerencial gerencial = new Gerencial();


    @Override
    protected Void doInBackground(String... params) {

        String command =  params[0]; // enter any command you need to execute
        String userServer =  params[1]; // enter any command you need to execute
        String ipServer =  params[2]; // enter any command you need to execute
        int portServer =  Integer.parseInt(params[3]); // enter any command you need to execute
        String password = params[4];

        while(true){

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
                ((ChannelExec)canalServidor).setCommand(command);

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
                            for (int j = 1; j<rowsText.length; j++) {
                                lista1.add(rowsText[j]);
                            }
                            //cuando no hay encabezado, se usa este otro ciclo que tome desde la fina uno de resultados
                            for (int j = 0; j<rowsText.length; j++) {
                                lista0.add(rowsText[j]);
                            }

                            if(1>0){
                                gerencial.ShowNotifications();
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

            return null;

        }

    }
}
