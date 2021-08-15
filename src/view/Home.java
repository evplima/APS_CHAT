/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import Utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import model.ClientListener;


/**
 *
 * @author evert
 */
public class Home extends JFrame{
    
    
    
private ArrayList<String> chats_abertos;    
private Map<String, ClientListener> conex_listeners;
private ArrayList<String> usua_conectados;
private String info_conex;
private Socket conexao;
private ServerSocket server;
private boolean rodando;

private JLabel lb_title;
private JButton bt_get_conectado, bt_inicia_conv;
private JList jlist;
private JScrollPane scroll;





public Home(Socket conexao, String info_conex){
super("CHAT APS - Home");

this.conexao = conexao;
this.info_conex = info_conex;
initComponents();
configComponents();
insertComponents();
insertActions();
start();
}

private void initComponents()
{
rodando = false;
server = null;
conex_listeners = new HashMap<String, ClientListener>();   
chats_abertos = new ArrayList<String>();   
usua_conectados = new ArrayList<String>();
lb_title = new JLabel("< Usuário : " + info_conex.split(":")[0] +" >", SwingConstants.CENTER);
bt_get_conectado = new JButton ("Atualizar Contatos");
bt_inicia_conv = new JButton ("Abrir Conversa");
jlist = new JList ();
scroll = new JScrollPane(jlist);
}


private void configComponents(){
this.setLayout(null);
this.setMinimumSize(new Dimension(600, 480));
this.setResizable(false);
this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
this.getContentPane().setBackground(Color.white);

lb_title.setBounds(10, 10, 370, 40);
lb_title.setBorder(BorderFactory.createLineBorder(Color.GRAY));

bt_get_conectado.setBounds(400, 10, 180, 40);
bt_get_conectado.setFocusable(false);

bt_inicia_conv.setBounds(10, 400, 575, 40);
bt_inicia_conv.setFocusable(false);

jlist.setBorder(BorderFactory.createTitledBorder("Usuários Online"));
jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

scroll.setBounds(10, 60, 575, 335);
scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
scroll.setBorder(null);
}

private void insertComponents(){
this.add(lb_title);
this.add(bt_get_conectado);
this.add(scroll);
this.add(bt_inicia_conv);
}

private void insertActions(){
this.addWindowListener(new WindowListener() {
    @Override
    public void windowOpened(WindowEvent we) {
        
    }

    @Override
    public void windowClosing(WindowEvent we) {
        rodando = false;
        Utils.enviaMsg(conexao, "QUIT");
        System.out.println("> Conexão encerrada!");
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

bt_get_conectado.addActionListener(event -> getConnectedUsers());
bt_inicia_conv.addActionListener(event -> openChat());

}

private void start(){
this.pack();
this.setVisible(true);
startServer(this, Integer.parseInt(info_conex.split(":")[2]));
}

private void getConnectedUsers(){
    Utils.enviaMsg(conexao, "GET_CONNECTED_USERS");
    String response = Utils.recebeMsg(conexao);
    jlist.removeAll();
    usua_conectados.clear();
    for(String info: response.split(";")){
        if(!info.equals(info_conex)){
            usua_conectados.add(info);
            
        }
    }
    jlist.setListData(usua_conectados.toArray());
}

    public ArrayList<String> getOpened_chats() {
        return chats_abertos;
    }

    public Map<String, ClientListener> getConnected_listeners() {
        return conex_listeners;
    }

    public String getInfo_conex() {
        return info_conex;
    }

    private void openChat(){
        int index = jlist.getSelectedIndex();
        if(index != -1){
            String info_conex = jlist.getSelectedValue().toString();
            String splited[] = info_conex.split(":");
            if(!chats_abertos.contains(info_conex)){
                try {
                    Socket conexao = new Socket(splited[1], Integer.parseInt(splited[2]));
                    Utils.enviaMsg(conexao, "OPEN_CHAT;" + this.info_conex);
                    ClientListener cl = new ClientListener(this, conexao);
                    cl.setChat(new Chat(this, conexao, info_conex, this.info_conex.split(":")[0]));
                    cl.setChatOpen(true);
                    conex_listeners.put(info_conex, cl);
                    chats_abertos.add(info_conex);
                    new Thread(cl).start();
                } catch (IOException ex) {
                    System.err.println("[Home:openChat] -> " + ex.getMessage());
                }
            }
        }
    }

private void startServer(Home home, int port){
    new Thread(){
        @Override
        public void run(){
            rodando = true;
            try{
                server = new ServerSocket(port);
                System.out.println("Servidor cliente iniciado na porta: " + port + "...");
                while(rodando){
                    Socket conexao = server.accept();
                    ClientListener cl = new ClientListener(home, conexao);
                    new Thread(cl).start();
                }
            }catch(IOException ex){
                System.err.println("[Home:startServer] -> " + ex.getMessage());
            }
        }
    }.start();
}
}
