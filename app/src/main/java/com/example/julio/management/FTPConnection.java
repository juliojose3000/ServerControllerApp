package com.example.julio.management;

import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

public class FTPConnection extends AsyncTask<String, Void, Void> {

    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected Void doInBackground(String... params) {

        String userServer =  params[0]; // enter any command you need to execute
        String ipServer =  params[1]; // enter any command you need to execute
        int portServer =  Integer.parseInt(params[2]); // enter any command you need to execute
        String password = params[3];


        File absoluteFile = this.file;
        FTPClient client = new FTPClient();
        String sFTP = ipServer;
        String sUser = userServer;
        String sPasword = password;
        try {
            client.connect(sFTP, portServer);
            boolean login = client.login(sUser, sPasword);
            client.changeWorkingDirectory("files");
            int reply = client.getReplyCode();
            System.out.println("Reply " + reply);
            if (FTPReply.isPositiveCompletion(reply)) {
                absoluteFile.getAbsolutePath();
                FileInputStream input = new FileInputStream(absoluteFile);
                client.enterLocalPassiveMode();
                System.out.println("Subido correctamente el archivo " + absoluteFile.getName());

                if (!client.storeFile(absoluteFile.getName(), input)) {
                    System.out.println("subida fallida");
                }
                System.out.println("Reply " + client.getReplyCode());
                input.close();
            }
            client.logout();
            client.disconnect();
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
        return null;
    }

}
