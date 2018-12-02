package com.example.julio.activities.gerencial;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julio.management.FTPConnection;
import cr.ac.ucr.teamjjja.sistemasoperativos.servercontroller.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FTPTransfer extends ListActivity {

    private List<String> item;
    private List<String> path;
    private String root ;
    private TextView mPath;
    private File absoluteFile;

    private String username;

    private String password;

    private int port;

    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftptransfer);
        item=null;
        path = null;
        root="/";
        mPath = findViewById(R.id.path);
        getDir(root);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("username");

        password = extras.getString("password");

        port = extras.getInt("port");

        ip = extras.getString("ip");

    }

    private void getDir(String dirPath) {
        mPath.setText("Location: " + dirPath);
        item = new ArrayList<>();
        path = new ArrayList<>();
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if (!dirPath.equals(root)) {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            path.add(file.getPath());
            if (file.isDirectory())
                item.add(file.getName() + "/");
            else
                item.add(file.getName());
        }
        ArrayAdapter<String> fileList = new ArrayAdapter<>(this, R.layout.row, item);
        setListAdapter(fileList);

    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File file = new File(path.get(position));
        absoluteFile=file;
        if (file.isDirectory()) {
            if (file.canRead())
                getDir(path.get(position));

            else {
                Intent intent = new Intent(FTPTransfer.this,FTPConnection.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("file",file);
                intent.putExtras(bundle);
                startActivity(intent);

                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_background)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
            }

        } else {
            final FTPConnection ftp =new FTPConnection();
            ftp.setFile(file);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("FTP");
            builder.setMessage("Desea enviar este archivo?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // TODO: handle the OK

                            ftp.execute(username, ip, ""+port, password);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }
}
