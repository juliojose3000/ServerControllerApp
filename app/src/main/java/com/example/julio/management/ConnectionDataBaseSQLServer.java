package com.example.julio.management;

import com.example.julio.objects.Audit;
import com.example.julio.objects.Command;
import com.example.julio.objects.MirrorServer;
import com.example.julio.objects.Rol;
import com.example.julio.objects.Server;
import com.example.julio.objects.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectionDataBaseSQLServer {

    //private String dataBaseName = "UATDATABASE_MovilApp";

    private String dataBaseName = "AndroidAppJ";

    private CreateConnection connectionDataBaseSQLServer;

    private Connection connection = null;

    private String notification;

    public ConnectionDataBaseSQLServer(){

        connectionDataBaseSQLServer = new CreateConnection();

        try {
            connection = connectionDataBaseSQLServer.createConnection("estudiantesrp","estudiantesrp", "AndroidAppJ","163.178.173.1484");
            //connection = connectionDataBaseSQLServer.createConnection("SA","Prograamigos1!", "UATDATABASE_MovilApp","192.168.43.248");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            notification = "No se pudo conectar con la base de datos del servidor de ubuntu, por lo que se conectará con la base de datos del servidor espejo";
            try {
                connection = connectionDataBaseSQLServer.createConnection("estudiantesrp","estudiantesrp", "AndroidAppJ","163.178.173.148");
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if(connection==null) {

            return;

        }

    }

    public String getNotification(){
        return this.notification;
    }

    public String verifyUserInBD(String username, String password){

            String tableName = "Users";

            String query = "SELECT* FROM "+tableName+" WHERE username="+username+" AND password="+password;

            try {
                //prepara la conexion;'
                Statement statement = connection.createStatement();
                //ejecuta el query
                ResultSet resultSet = statement.executeQuery(query);

                //pregunta si la consulta trajo resultados.
                if(!resultSet.next()){

                    return "No existe el usuario que intenta ingresar, favor revise los datos e intente de nuevo.";

                }else{
                    String query2 = "select name,username,password,incremental_key,r.id_rol,r.rol_name from dbo.Rol r,dbo.Users u where r.id_rol=u.id_rol and\n" +
                            "username="+username+" and password="+password;

                    ResultSet resultSet2 = statement.executeQuery(query2);


                    if(!resultSet2.next()){

                        return "El usuario no existe en la base de datos";

                    }
                    insertLoggedUser(username, password);

                    //retorno el rol del usuario que intente loguearse
                    String rolName = resultSet2.getObject("rol_name").toString();

                    return rolName;

                }

            } catch (SQLException e) {

                return "Error al conectar con la base de datos. Verifique la coneccion.";

            }



    }

    public ArrayList<String> getServersUser(){

        ArrayList<String> listServers = new ArrayList<>();

        String tableName = "Server";

        String query = "SELECT* FROM "+tableName;

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while(resultSet.next()){
                listServers.add(resultSet.getString("server_name"));
            }
        } catch (SQLException e) {

            System.out.print(e);

        }

        return listServers;
    }

    public Server getServerByName(String serverName) {

        String query = "Select ip, server_user, password,port,id_server, server_name  from  dbo.Server where server_name = '" + serverName+"'";
        Server server = null;
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                server = new Server();
                server.setIp(resultSet.getString("ip"));
                server.setUsername(resultSet.getString("server_user"));
                server.setPassword(resultSet.getString("password"));
                server.setPort(resultSet.getInt("port"));
                server.setIdServer(resultSet.getInt("id_server"));
                server.setServerName(resultSet.getString("server_name"));

            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return server;
    }

    public String deleteServerByName(String serverName) {

        String query="delete Server where server_name='"+serverName+"';";

        try {
            Statement statement= connection.createStatement();
            //ejecuta la consulta y obtiene resultado
            statement.executeUpdate(query);

            return "Servidor ingresado correctamente";

        } catch (SQLException e) {
            System.out.println(e);
            return "Error al ingresar el servidor. Intente de nuevo";
        }
    }

    public Server getServerById(int idServer) {

        String query = "Select ip, server_user, password,port,id_server, server_name  from  dbo.Server where id_server = '" + idServer+"'";
        Server server = null;
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                server = new Server();
                server.setIp(resultSet.getString("ip"));
                server.setUsername(resultSet.getString("server_user"));
                server.setPassword(resultSet.getString("password"));
                server.setPort(resultSet.getInt("port"));
                server.setIdServer(resultSet.getInt("id_server"));
                server.setServerName(resultSet.getString("server_name"));

            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return server;
    }

    public String insertServer(Server server){
        String query= "Insert into   ["+dataBaseName+"].[dbo].[Server] (ip,server_user,password,port,server_name)" +
                " values ('"+server.getIp()+"','"+server.getUsername()+"','"+server.getPassword()+"',"+server.getPort()+",'"+server.getServerName()+"')";

        try {
            Statement statement= connection.createStatement();
            //ejecuta la consulta y obtiene resultado
            statement.executeUpdate(query);

            return "Servidor ingresado correctamente";

        } catch (SQLException e) {
            System.out.println(e);
            return "Error al ingresar el servidor. Intente de nuevo";
        }
    }





    public String insertMirrorServer(MirrorServer server){
        String query= "Insert into   ["+dataBaseName+"].[dbo].[WindowsServer] (ip,server_user,password,port,ip_mirror,server_name)" +
                " values ('"+server.getIp()+"','"+server.getUsername()+"','"+server.getPassword()+"',"+server.getPort()+",'"+server.getIpMirror()+"','"+server.getServerName()+"')";

        try {
            Statement statement= connection.createStatement();
            //ejecuta la consulta y obtiene resultado
            statement.executeUpdate(query);

            return "Servidor ingresado correctamente";

        } catch (SQLException e) {
            System.out.println(e);
            return "Error al ingresar el servidor. Intente de nuevo";
        }
    }

    public String editServer(Server server){
        String query = "Update ["+dataBaseName+"].[dbo].[Server] "+
                " set ip = '"+server.getIp()+"', server_user = '"+server.getUsername()+"', password = '"+server.getPassword()+"',port =  "+server.getPort()+", server_name = '"+server.getServerName()+"'"+
                " where id_server="+server.getIdServer();

        try {
            Statement statement= connection.createStatement();
            //ejecuta la consulta y obtiene resultado
            statement.executeUpdate(query);

            return "Servidor editado correctamente";

        } catch (SQLException e) {
            System.out.println(e);
            return "Error al editar el servidor. Intente de nuevo";
        }
    }

    public void insertAudit(Audit audit, Command command){
        String query1= "Insert into Audit (user_identification,server_name,admission_date,departure_date) " +
                " values ('"+audit.getUser().getIdentification()+"','"+audit.getServerName()+"','"+audit.getAdmissionDate()+"','"+audit.getDepartureDate()+
                "')";

        String query2 = "Insert into ["+dataBaseName+"].[dbo].[Command] (command_line) values ('"+command.getCommandLine()+"')";

        String query3= "Insert into ["+dataBaseName+"].[dbo].[AuditCommand] (user_identification,command_line) values ('"+audit.getUser().getIdentification()+
                "','"+ command.getCommandLine()+"')";

        System.out.println(query1+"\n"+query2+"\n"+query3);

        try {
            Statement statement= connection.createStatement();
            //ejecuta la consulta y obtiene resultado
            statement.executeUpdate(query1);
            statement.executeUpdate(query2);
            statement.executeUpdate(query3);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserLogged(){
        String query = "SELECT TOP 1 * FROM LoggedUsers ORDER BY incremental_key DESC";
        User user = null;
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                user = new User();
                user.setIdentification(resultSet.getString("identification"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRol(getRolUser());


            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return user;
    }

    public Rol getRolUser(){
        String query = "select r.id_rol, rol_name\n" +
                " from Rol r, LoggedUsers l\n" +
                " where r.id_rol = l.id_rol";
        Rol rol = null;
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                rol = new Rol();
                rol.setIdRol(resultSet.getInt("id_rol"));
                rol.setRolName(resultSet.getString("rol_name"));
            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return rol;
    }

    public String getServerNameByIP(String ip){
        String query = "select server_name\n" +
                " from Server\n" +
                " where ip='"+ip+"'";
        String serverName = "";
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                serverName = resultSet.getString("server_name");

            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return serverName;
    }

    public User getUserByUserNameAndPassword(String username, String password){
        String query = "Select identification, name, id_rol, username, password from Users where  username = "+username+" AND password = "+password;
        User user = null;
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                user = new User();
                user.setIdentification(resultSet.getString("identification"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRol(getRolUserByIdRol(resultSet.getString("id_rol")));


            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return user;
    }

    public Rol getRolUserByIdRol(String role){
        String query = "select r.id_rol, rol_name\n" +
                " from Rol r\n" +
                " where r.id_rol = '"+role+"'";
        Rol rol = null;
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                rol = new Rol();
                rol.setIdRol(resultSet.getInt("id_rol"));
                rol.setRolName(resultSet.getString("rol_name"));
            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return rol;
    }

    public String insertLoggedUser(String username, String password){
        User user = getUserByUserNameAndPassword(username, password);
        String query= "Insert into   ["+dataBaseName+"].[dbo].[LoggedUsers] (identification,name,username,id_rol, password)" +
                " values ('"+user.getIdentification()+"','"+user.getName()+"','"+user.getUsername()+"','"+user.getRol().getIdRol()+"','"+user.getPassword()+"')";

        try {
            Statement statement= connection.createStatement();
            //ejecuta la consulta y obtiene resultado
            statement.executeUpdate(query);

            return "Servidor ingresado correctamente";

        } catch (SQLException e) {
            System.out.println(e);
            return "Error al ingresar el servidor. Intente de nuevo";
        }
    }

    public void insertProcessInDB(ArrayList<String> procesos){

        for (int j = 0; j < procesos.size(); j++) {
            String string = procesos.get(j);
            String[] parts = string.split(" ");
            String part1 = parts[0];
            String part2 = parts[1];
            String query= "Insert into   ["+dataBaseName+"].[dbo].[Process] (PID,name) values ('"+part1+"','"+part2+"')";
            System.out.println(query);
            try {
                Statement statement= connection.createStatement();
                //ejecuta la consulta y obtiene resultado
                statement.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public MirrorServer getMirrorServerByIpServer(String ipServer) {

        String query = "Select m.ip, m.server_user, m.password,m.port,m.idServer, m.server_name  from  MirrorServer m, Server s where s.ip = '"+ipServer+"' and s.id_server = m.idServer";
        MirrorServer server = null;
        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //obtengo los datos de la consulta
            while (resultSet.next()) {
                server = new MirrorServer();
                server.setIp(resultSet.getString("ip"));
                server.setUsername(resultSet.getString("server_user"));
                server.setPassword(resultSet.getString("password"));
                server.setPort(resultSet.getInt("port"));
                server.setIdServer(resultSet.getInt("idServer"));
                server.setServerName(resultSet.getString("server_name"));

            }
        } catch (SQLException e) {

            System.out.print(e);

        }
        return server;
    }


}
