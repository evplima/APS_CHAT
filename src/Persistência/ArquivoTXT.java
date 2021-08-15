/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistência;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author evert
 */
public class ArquivoTXT {
    
    
    public static void salvarTexto(String caminhoArquivo, String texto){
        try(
            FileWriter criadordeArquivos = new FileWriter(caminhoArquivo,true);
            BufferedWriter buffer = new BufferedWriter(criadordeArquivos);
            PrintWriter escritordeArquivos = new PrintWriter(buffer);
            
                ){        
                    escritordeArquivos.append(texto);
                    
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void apagarTexto(String caminhoArquivo){
        try(
            FileWriter criadordeArquivos = new FileWriter(caminhoArquivo,true);
            BufferedWriter buffer = new BufferedWriter(criadordeArquivos);
            PrintWriter escritordeArquivos = new PrintWriter(buffer);
            
                ){        
                    escritordeArquivos.append("");
                    
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
