/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transferência_de_Arquivos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;



/**
 *
 * @author evert
 */
public class Servidor_Transf {
    
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();

    public Servidor_Transf() {
        try{
            serverSocket = new ServerSocket(5555);
            System.out.println("Servidor on!");
            
            while(true){
                socket = serverSocket.accept();
                new Thread(new ListenerSocket(socket)).start();
                
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private class ListenerSocket implements Runnable {
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;
        public ListenerSocket(Socket socket) throws IOException {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        
        public void run() {
            FileMessage message = null;
            try{
            
                while((message = (FileMessage) inputStream.readObject()) != null){
                streamMap.put(message.getCliente(), outputStream);
                if(message.getFile() !=null){
                    for (Map.Entry<String, ObjectOutputStream> kv : streamMap.entrySet()){
                        if(!message.getCliente().equals(kv.getKey())){
                            kv.getValue().writeObject(message);
                        }
                    }
                }
            }
            }catch (IOException ex){
                streamMap.remove(message.getCliente());
                System.out.println(message.getCliente() + " desconectou!");
            }catch(ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }
    }
    }
    

