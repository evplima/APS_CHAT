/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transferência_de_Arquivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.*;
import javax.swing.JFileChooser;


/**
 *
 * @author evert
 */
public class Cliente {
    
    private Socket socket;
    private ObjectOutputStream outputStream;
    private String nick;
    public Cliente(String nome) throws IOException{
        this.socket = new Socket("localhost", 5555);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        
        new Thread(new ListenerSocket(socket)).start();
        nick = nome;
        
        
    }

    public void menu() throws IOException {
        
        
         String nome = nick;
        
        this.outputStream.writeObject(new FileMessage(nome));

            
                enviar(nome);
            
        
    
}
    private void enviar(String nome) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int opt = fileChooser.showOpenDialog(null);
        
        if(opt == JFileChooser.APPROVE_OPTION ){
            File file = fileChooser.getSelectedFile();
            this.outputStream.writeObject(new FileMessage(nome, file));
            
        }
    }

    private class ListenerSocket implements Runnable{
        
        
        private ObjectInputStream inputStream;
        public ListenerSocket(Socket socket) throws IOException {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }
        
        public void run(){
            FileMessage msg = null;
           
            
                try {
                    while((msg = (FileMessage) inputStream.readObject()) != null){
                        System.out.println("\nVocê recebeu um arquivo de " + msg.getCliente());
                        System.out.println("O arquivo é " + msg.getFile().getName());
                        
                        //imprime(message);
                        salvar(msg);

                        
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
    
        
    }
   
        private void imprime(FileMessage message) {
            try {
                FileReader fileReader = new FileReader(message.getFile());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String linha;
                while((linha = bufferedReader.readLine()) !=null ){
                    System.out.println(linha);    
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                
            }
        }

        private void salvar(FileMessage message) {
            try {
                
                Thread.sleep(new Random().nextInt(1000));
                long time = System.currentTimeMillis();
                FileInputStream fileInputStream = new FileInputStream(message.getFile());
                FileOutputStream fileOutputStream = new FileOutputStream("salvos\\salvos" + time + "_" + message.getFile().getName());
                FileChannel fin = fileInputStream.getChannel();
                FileChannel fout = fileOutputStream.getChannel();
                
                long size = fin.size();
                
                fin.transferTo(0, size, fout);
            } catch (FileNotFoundException ex) {
                   ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                    ex.printStackTrace();
            }
        }
    }   
  
}
