/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Utils.Utils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author evert
 */
public class Servidor_Chat {
    
public static final String HOST = "127.0.0.1";
public static final int PORT = 4444;



private ServerSocket server;
private Map<String, ClientListener> clients;


public Servidor_Chat(){
try{

String info_conex;
clients = new HashMap<String, ClientListener>();
server = new ServerSocket(PORT);
System.out.println("Servidor iniciado no host: " + HOST + " e porta: " + PORT);
while (true){
Socket conexao = server.accept();
info_conex = Utils.recebeMsg(conexao);
if(checkLogin(info_conex)){
    ClientListener cl = new ClientListener(info_conex, conexao, this);
    clients.put(info_conex, cl);
    Utils.enviaMsg(conexao, "SUCESS");
    new Thread(cl).start();
}
else{
    Utils.enviaMsg(conexao, "ERROR");
}
}
} catch(IOException ex){
System.err.println("[ERROR:Server] -> " + ex.getMessage());
}
}


public Map<String, ClientListener> getClients(){
    return clients;
}





private boolean checkLogin(String connection_info){


String[] splited = connection_info.split(":");
for(Map.Entry<String, ClientListener> pair: clients.entrySet()){
    String[] parts = pair.getKey().split(":");
    if(parts[0].toLowerCase().equals(splited[0].toLowerCase())){
        return false;
    }
else if ((parts[1] + parts[2]).equals(splited[1] + splited[2])){
        return false;
        }

}
return true;
}


}
