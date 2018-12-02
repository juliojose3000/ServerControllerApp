package com.example.julio.objects;

public class Server {

    private int idServer;

    private String serverName;

    private String username;

    private String password;

    private int port;

    private String ip;

    public Server(){

    }

    public Server(int idServer, String serverName,String username, String password, int port, String ip) {
        this.idServer=idServer;
        this.serverName=serverName;
        this.username = username;
        this.password = password;
        this.port = port;
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}