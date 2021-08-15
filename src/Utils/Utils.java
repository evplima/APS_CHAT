/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author evert
 */
public class Utils {
    public static boolean enviaMsg(Socket conexao, String mensagem){

try{
ObjectOutputStream output = new ObjectOutputStream(conexao.getOutputStream());
output.flush();
output.writeObject(mensagem);
return true;
} catch(IOException ex){
System.err.println("[ERROR:sendMessage] -> " + ex.getMessage());
}
return false;
}

public static String recebeMsg(Socket conexao){
String response = null;
try{
ObjectInputStream input = new ObjectInputStream(conexao.getInputStream());
response = (String) input.readObject();
}catch(IOException ex){
System.err.println("ERROR:receiveMessage] -> " + ex.getMessage());
} catch (ClassNotFoundException ex){
System.err.println("[ERROR:receiveMessage] -> " + ex.getMessage());
}
return response;
}

    
}
