/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utils.Utils;
import java.io.IOException;
import java.net.Socket;
import java.util.*;



/**
 *
 * @author evert
 */
public class ClientListener implements Runnable {
private String info_conex;
private Socket conexao;
private Servidor_Chat server;
private boolean rodando;

public ClientListener(String info_conex, Socket conexao, Servidor_Chat server){
this.info_conex = info_conex;
this.conexao = conexao;
this.server = server;
this.rodando = false;
}

public boolean isRunning(){
return rodando;
}

public void setRunning(boolean running){
this.rodando = running;
}


public void run(){
rodando = true;
String mensagem;
while(rodando){
mensagem = Utils.recebeMsg(conexao);
if(mensagem.equals("QUIT")){
    server.getClients().remove(info_conex);
    try {
        conexao.close();
    } catch (IOException ex) {
        System.out.println("[ClientListener:Run] -> " + ex.getMessage());
    }
rodando = false;
}else if(mensagem.equals("GET_CONNECTED_USERS")){
    System.out.println("Solicitação de atualizar lista de contatos...");
    String resposta = "";
    for(Map.Entry<String, ClientListener> pair: server.getClients().entrySet()){
        resposta += (pair.getKey() + ";");
    }
    Utils.enviaMsg(conexao, resposta);
}

else{   
    System.out.println("Recebido: " + mensagem);
}
}
}
}
