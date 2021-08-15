/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Utils.Utils;
import java.io.IOException;
import java.net.Socket;
import view.Chat;
import view.Home;

/**
 *
 * @author evert
 */
public class ClientListener implements Runnable{

    private boolean rodando;
    private boolean chatOpen;
    private Socket conexao;
    private Home home;
    private String info_conex;
    private Chat chat;
    
    public ClientListener(Home home, Socket conexao){
        this.chatOpen = false;
        this.rodando = false;
        this.home = home;
        this.conexao = conexao;
        this.info_conex = null;
        this.chat = null;
    }

    public boolean isRunning() {
        return rodando;
    }

    public void setRunning(boolean running) {
        this.rodando = running;
    }

    public boolean isChatOpen() {
        return chatOpen;
    }

    public void setChatOpen(boolean chatOpen) {
        this.chatOpen = chatOpen;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
    
    
    @Override
    public void run() {
       rodando = true;
       String mensagem;
       while(rodando){
           mensagem = Utils.recebeMsg(conexao);
           if (mensagem == null || mensagem.equals("CHAT_CLOSE")){
           if(chatOpen){   
           home.getOpened_chats().remove(info_conex);
           home.getConnected_listeners().remove(info_conex);
           chatOpen = false;
           try{
               conexao.close();
           }catch(IOException ex){
               System.err.println("[ClientListener:run] -> " + ex.getMessage());
           }
           chat.dispose();
           }
          
           rodando = false;
       } 
           else{
               String[] fields = mensagem.split(";");
               if(fields.length > 1){
                   if(fields[0].equals("OPEN_CHAT")){
                       String[] splited = fields[1].split(":");
                       info_conex = fields[1];
                       if(!chatOpen){
                           System.out.println("Abriu o chat...");
                           home.getOpened_chats().add(info_conex);
                           home.getConnected_listeners().put(info_conex, this);
                           chatOpen = true;
                           chat = new Chat(home, conexao, info_conex, home.getInfo_conex().split(":")[0]);
                       }
                   }else if (fields[0].equals("MESSAGE")){
                       String msg = "";
                       for(int i=1; i<fields.length;i++){ 
                           msg += fields[i];
                           if(i>1) msg += ";";
                       }
                       chat.append_message(msg);
                       
                   }
               }
               }
           System.out.println(">> Mensagem: " +  mensagem);
       }
    }
    
}
