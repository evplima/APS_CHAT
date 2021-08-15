/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import Persistência.ArquivoTXT;
import Persistência.MsgDAO;
import Transferência_de_Arquivos.Cliente;
import Utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author evert
 */
public class Chat extends JFrame{
    
private JLabel lb_titulo;
private JEditorPane mensagens;
private JTextField tf_mensagem;
private JButton bt_mensagem;
private JButton bt_transf;
private JPanel panel;
private JScrollPane scroll;

private Home home;
private Socket conexao;
private ArrayList<String> lista_msg;
private String info_conex;



public Chat(Home home, Socket conexao, String info_conex, String titulo){
super(titulo);
this.conexao = conexao;
this.home = home;
this.info_conex = info_conex;
initComponents();
configComponents();
insertComponents();
insertActions();
start();

}


private void initComponents(){
lista_msg = new ArrayList<String>();
lb_titulo = new JLabel(info_conex.split(":")[0], SwingConstants.CENTER);
mensagens = new JEditorPane();
tf_mensagem = new JTextField();
bt_mensagem = new JButton("Enviar");
bt_transf = new JButton ("Anexar");
panel = new JPanel(new BorderLayout());
scroll = new JScrollPane(mensagens);



}



private void configComponents(){
this.setMinimumSize(new Dimension(480, 720));
this.setLayout(new BorderLayout());
this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
mensagens.setContentType("text/html");
mensagens.setEditable(false);
scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
bt_mensagem.setSize(100, 40);
bt_transf.setSize(100, 40);
}
private void insertComponents(){
this.add(lb_titulo, BorderLayout.NORTH);
this.add(scroll, BorderLayout.CENTER);
this.add(panel, BorderLayout.SOUTH);
panel.add(tf_mensagem, BorderLayout.CENTER);
panel.add(bt_mensagem, BorderLayout.EAST);
panel.add(bt_transf, BorderLayout.WEST);
}



private void insertActions(){
bt_mensagem.addActionListener(event -> send());
bt_transf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
                          new Thread() {
 
                @Override
                public void run() {

                try {
                    Cliente cliente = new Cliente(lb_titulo.getText());
                   cliente.menu();
                      
                     } catch (IOException ex) {
                   ex.printStackTrace();
                                 }
               
       
    }
              
  }.start();
			}
		});
tf_mensagem.addKeyListener(new KeyListener(){
    
    @Override
    public void keyTyped(KeyEvent e){
        
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyChar() == KeyEvent.VK_ENTER){
            send();
        }
    }
    @Override
    public void keyReleased(KeyEvent ke) {
        //To change body of generated methods, choose Tools | Templates.
    }

   
});
this.addWindowListener(new WindowListener(){
    @Override
    public void windowOpened(WindowEvent we) {

    }

    @Override
    public void windowClosing(WindowEvent we) {
        Utils.enviaMsg(conexao, "CHAT_CLOSE");
        home.getOpened_chats().remove(info_conex);
        home.getConnected_listeners().get(info_conex).setChatOpen(false);
        home.getConnected_listeners().get(info_conex).setRunning(false);
        home.getConnected_listeners().remove(info_conex);
        MsgDAO.InsereMsg(mensagens.getText());
    }

    @Override
    public void windowClosed(WindowEvent we) {

    }

    @Override
    public void windowIconified(WindowEvent we) {

    }

    @Override
    public void windowDeiconified(WindowEvent we) {

    }

    @Override
    public void windowActivated(WindowEvent we) {

    }

    @Override
    public void windowDeactivated(WindowEvent we) {

    }
    
});
}


public void append_message(String received){
lista_msg.add(received);
String mensagem = "";
for (String str : lista_msg){
mensagem += str;
}
mensagens.setText(mensagem);
}

private void send(){
if(tf_mensagem.getText().length() > 0) {
Calendar c = Calendar.getInstance();
DateFormat df = new SimpleDateFormat("hh:mm:ss");
Utils.enviaMsg(conexao, "MESSAGE;" + "<b>[" + df.format(new Date()) + "] " + this.getTitle() + ": </b><i>" + tf_mensagem.getText() + "</i><br>");
append_message("<b>[" + df.format(new Date()) + "] Eu: </b><i>" + tf_mensagem.getText() + "</i><br>");
tf_mensagem.setText("");
ArquivoTXT.apagarTexto("TextoChat.html");
ArquivoTXT.salvarTexto("TextoChat.html", mensagens.getText() + "\n Chat salvo do apelido: " + this.getTitle() + "\n"
        + "Data do log: " + c.getTime().toString() );
}
}
private void start(){
this.pack();
this.setVisible(true);
}
    


}
