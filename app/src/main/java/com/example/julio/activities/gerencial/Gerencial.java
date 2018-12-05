package com.example.julio.activities.gerencial;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.julio.management.ConnectionDataBaseSQLServer;

import com.example.julio.management.MyUserInfo;
import com.example.julio.objects.Server;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

import java.io.InputStream;
import java.util.ArrayList;

import javax.crypto.ShortBufferException;

public class Gerencial extends AppCompatActivity {

    private String username;

    private String password;

    private int port;

    private String ip;

    private Server server;

    private ConnectionDataBaseSQLServer connectionDataBaseSQLServer;

    private ArrayList<String> listServers;

    private ListView listViewServers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gerencial);

        listViewServers = findViewById(R.id.listview_servers);



        listViewServers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String serverName = (String) parent.getItemAtPosition(position);

                connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

                server = connectionDataBaseSQLServer.getServerByName(serverName);

                ip = server.getIp();

                username = server.getUsername();

                password = server.getPassword();

                port = server.getPort();
            }

        });

        fillServerInListView();

    }

    //metodo para mostrar y ocultar el menu
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_gerencial, menu);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == R.id.item_add_server){
            Intent i = new Intent(Gerencial.this, AddServer.class);
            startActivity(i);
        }else if(id == R.id.item_ftp_transfer){
            Intent i = new Intent(Gerencial.this, FTPTransfer.class);
            i.putExtra("username",username);
            i.putExtra("password",password);
            i.putExtra("port",port);
            i.putExtra("ip",ip);

            startActivity(i);
        }else if(id == R.id.item_async_task){

            ShowNotifications showNotifications = new ShowNotifications();
            showNotifications.setContext(Gerencial.this);
            showNotifications.execute(username, ip, ""+port, password);

        }else if(id == R.id.item_show_notifications){



        }

        return super.onOptionsItemSelected(item);
    }

    //cargo al listview la lista de los servidores
    public void fillServerInListView() {
        connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();// Creo una nueva conexion
        listServers = connectionDataBaseSQLServer.getServersUser();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                listServers );

        listViewServers.setAdapter(arrayAdapter);
        listViewServers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    public void seeFiles(View v){

        Intent i = new Intent(Gerencial.this, ListServerFiles.class);

        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("port",port);
        i.putExtra("ip",ip);

        startActivity(i);


    }

    public void seeProcess(View v){

        Intent i = new Intent(Gerencial.this, ListServerProcess.class);

        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("port",port);
        i.putExtra("ip",ip);

        startActivity(i);


    }

    public void editServer(View v){

        Intent i = new Intent (Gerencial.this, EditServers.class);

        i.putExtra("idServer", server.getIdServer());

        startActivity(i);

    }



    /*public void showNotifications(Context mContext){
        String tittle="";
        String subject="Saturacion de memoria";
        String body="Se ha llegado al limite de la memoria";

        NotificationManager notif=(NotificationManager)getSystemService(mContext.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(mContext)
                .setContentTitle(subject).setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background).getNotification();

        notification.flags |= notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notification);
    }*/



    //--------------------------------------------------------------------------------------------//


    //--------------------------------------------------------------------------------------------//

    class ShowNotifications extends AsyncTask<String, Void, Void> {

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

                String subject="Saturacion de memoria";
                String body="Se ha llegado al limite de la memoria";

                NotificationManager notif=(NotificationManager)getSystemService(mComtext.NOTIFICATION_SERVICE);

                Notification notification = new Notification.Builder(mComtext)
                        .setContentTitle(subject).setContentText(body)
                        .setSmallIcon(R.drawable.ic_launcher_background).getNotification();

                notification.flags |= notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notification);


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




}
