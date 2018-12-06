package com.example.julio.management;

import com.example.julio.objects.Audit;
import com.example.julio.objects.Command;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class ConnectServerWindows implements Runnable {
    private ArrayList<String> lista1 = new ArrayList<>();

    private ArrayList<String> lista0 = new ArrayList<>();

    private ConnectionDataBaseSQLServer connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

    private Command command;

    private Session session;

    private Session mirrorSession;

    private Audit audit;

    public ArrayList<String> getLista1(){
        return this.lista1;
    }

    public ArrayList<String> getLista0(){
        return this.lista0;
    }

    private String commandToExecute; // enter any command you need to execute

    private String userServer; // enter any command you need to execute

    private String userServerMirror;

    private String ipServer; // enter any command you need to execute

    private String mirrorIpServer;

    private int portServer; // enter any command you need to execute

    private String password;

    private String mirrorPassword;

    public ConnectServerWindows(String commandToExecute, String userServer, String ipServer,String mirrorIpServer, int portServer, String password,
                                String userServerMirror, String mirrorPassword){
        this.commandToExecute = commandToExecute;
        this.userServer = userServer;
        this.ipServer = ipServer;
        this.mirrorIpServer=mirrorIpServer;
        this.portServer = portServer;
        this.password = password;
        this.userServerMirror=userServerMirror;
        this.mirrorPassword=mirrorPassword;
    }

    public ConnectServerWindows(String commandToExecute){
        this.commandToExecute = commandToExecute;
    }

    public ConnectServerWindows(String userServer, String ipServer,String mirrorIpServer, int portServer, String password,
                                String userServerMirror, String mirrorPassword){
        this.userServer = userServer;
        this.ipServer = ipServer;
        this.mirrorIpServer=mirrorIpServer;
        this.portServer = portServer;
        this.password = password;
        this.userServerMirror=userServerMirror;
        this.mirrorPassword=mirrorPassword;
    }

    public void setCommandToExecute(String commandToExecute){
        this.commandToExecute = commandToExecute;
    }



    @Override
    public void run() {

        this.command = new Command(commandToExecute);

        Calendar currenttime = Calendar.getInstance();
        Date sqldate = new Date((currenttime.getTime()).getTime());

        this.audit = new Audit(connectionDataBaseSQLServer.getUserLogged(), connectionDataBaseSQLServer.getServerNameByIP(ipServer), sqldate , sqldate);

        try {
            JSch jsch = new JSch();

            String host = userServer+"@"+ipServer;// enter username and ipaddress for machine you need to connect

            String mirrorHost=  userServerMirror+"@"+mirrorIpServer;

            String user = host.substring(0, host.indexOf('@'));

            String mirrorUser= mirrorHost.substring(0, mirrorHost.indexOf('@'));

            host = host.substring(host.indexOf('@') + 1);

            mirrorHost=mirrorHost.substring(mirrorHost.indexOf('@')+1);

            session = jsch.getSession(user, host, portServer);

            mirrorSession = jsch.getSession(mirrorUser, mirrorHost, portServer);

            // username and password will be given via UserInfo interface.
            MyUserInfo ui = new MyUserInfo();
            ui.setPasswd(password);
            session.setUserInfo(ui);
            session.connect();

            MyUserInfo mirrorUI = new MyUserInfo();
            mirrorUI.setPasswd(mirrorPassword);
            mirrorSession.setUserInfo(mirrorUI);
            mirrorSession.connect();

            Channel canalServidor=session.openChannel("exec");
            ((ChannelExec)canalServidor).setCommand(commandToExecute);

            canalServidor.setInputStream(null);

            ((ChannelExec)canalServidor).setErrStream(System.err);

            InputStream informacionDelServidor =canalServidor.getInputStream();


            Channel canalServidorMirror=mirrorSession.openChannel("exec");
            ((ChannelExec)canalServidorMirror).setCommand(commandToExecute);

            canalServidorMirror.setInputStream(null);

            ((ChannelExec)canalServidorMirror).setErrStream(System.err);

            InputStream informacionDelServidorMirror =canalServidorMirror.getInputStream();

            //aqui conectar al servidor espejo
            try {
                canalServidor.connect();
                if(!commandToExecute.equals("ps -A")){//solo se insertan en la base los procesos listados de un usuario especifico
                    connectionDataBaseSQLServer.insertAudit(this.audit, this.command);
                }

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

            }catch(Exception e){
                canalServidorMirror.connect();

                if(!commandToExecute.equals("ps -A")){//solo se insertan en la base los procesos listados de un usuario especifico
                    connectionDataBaseSQLServer.insertAudit(this.audit, this.command);
                }

                byte[] arregloBytes=new byte[1024];
                while(true){
                    while (true) {
                        while (informacionDelServidorMirror.available() > 0) {
                            int i = informacionDelServidorMirror.read(arregloBytes, 0, 1024);
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

                        }
                        if (canalServidor.isClosed()) {
                            System.out.println("exit-status: " + canalServidorMirror.getExitStatus());
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }


                    }
                    if(canalServidorMirror.isClosed()){
                        System.out.println("exit-status: "+canalServidorMirror.getExitStatus());
                        break;
                    }
                    try{Thread.sleep(1000);}catch(Exception ee){}
                }
                canalServidorMirror.disconnect();
                mirrorSession.disconnect();
            }




        } catch (Exception e) {
            System.out.print(e);
        }

    }
}
