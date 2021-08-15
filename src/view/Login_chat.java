/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import Servidor.Servidor_Chat;
import Utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author evert
 */
public class Login_chat extends JFrame {
    
    
    
    private JButton bt_login;
    private JLabel lb_usua, lb_port, lb_titulo;
    private JTextField tf_user, tf_port;
    private String apelido;
    int port = 1000;
    
        


    public Login_chat(){
        super("APS CHAT");
        initComponents();
        insertActions();
        start();
        configComponents();
        insertComponents();
      
        
    }
    
    private void initComponents(){
        bt_login = new JButton();
        lb_usua = new JLabel("Apelido", SwingConstants.CENTER);
        lb_port = new JLabel ("Porta", SwingConstants.CENTER);
        lb_titulo = new JLabel(); 
        tf_user = new JTextField();
        tf_port = new JTextField();
    }
    
    private void configComponents(){
        this.setLayout(null);
        this.setMinimumSize(new Dimension(400, 350));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(Color.yellow);
        
        
        lb_titulo.setBounds(5, 5, 380, 135);
        ImageIcon icon = new ImageIcon("src\\imagens\\login.png");
      lb_titulo.setIcon(icon);
      
       bt_login.setBounds(235, 260, 150, 60);
        bt_login.setIcon(new ImageIcon("src\\imagens\\Capturar3.png"));

        lb_usua.setBounds(10, 160, 100, 40);
        lb_usua.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        lb_port.setBounds(10, 210, 100, 40);
        lb_port.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        tf_user.setBounds(120, 160, 265, 40);
        tf_port.setBounds(120, 210, 265, 40);
        // campo para digitar a porta 
        tf_port.setVisible(false);
        lb_port.setVisible(false);
    }
    private void insertComponents(){
        this.add(bt_login);
        this.add(lb_usua);
        this.add(lb_port);
        this.add(lb_titulo);
        this.add(tf_user);
        this.add(tf_port);
    }
    
    private void insertActions(){
        
        bt_login.addActionListener(event -> {
            if(tf_user.getText().equals("")==false){
            for(int i =0; i<40; i++){
            try{
            String nickname = tf_user.getText();
            apelido = nickname;

            /*int port  = Integer.parseInt(tf_port.getText());
            tf_port.setText("");*/
            
            Socket conexao = new Socket (Servidor_Chat.HOST, Servidor_Chat.PORT);
            String info_conex = (nickname + ":" + conexao.getLocalAddress().getHostAddress() + ":" + port);
            Utils.enviaMsg(conexao, info_conex);
            if(Utils.recebeMsg(conexao).equals("SUCESS")){
                Home home = new Home(conexao, info_conex);
                this.dispose();
                i=50;
            } else{
               // 
                port +=1;
                if(i==39){
                    JOptionPane.showMessageDialog(null, "Algum usuário já está conectado com este apelido. Tente novamente!");
                }
               // tf_user.setText(""); 
            }
            }catch (IOException ex){
          JOptionPane.showMessageDialog(null, "Erro ao conectar. Verifique se o servidor está em execução!");

            }
            
            }
            }else{
                JOptionPane.showMessageDialog(null, "Digite um apelido!");
            }
        });
        
        
        
        
    }
    private void start(){
        this.pack();
        this.setVisible(true);
    }
  public String getApelido(){
      return apelido;
  }
}
