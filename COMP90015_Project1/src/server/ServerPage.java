package server;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.awt.event.ActionEvent;

/**
 * @author christina Xu (#945756)
 * Server Page (contains server GUI)
 *
 */
public class ServerPage extends JFrame {
	private JFrame frame;
	private JTextField addressField;
	private JTextField portField;
	private JTextField fileField;
	private String filePath;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private int port;
	private String address;
	private Server server;
	public final int PORT_MAX = 10000;
	public final String PATH = "/Users/christina/Desktop/COMP90015_DS/COMP90015_Project1/dictionary.json";

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerPage window = new ServerPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public ServerPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextPane txtpnDictionaryServer = new JTextPane();
		txtpnDictionaryServer.setText("Dictionary Server");
		txtpnDictionaryServer.setForeground(Color.WHITE);
		txtpnDictionaryServer.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		txtpnDictionaryServer.setBackground(new Color(25, 25, 112));
		txtpnDictionaryServer.setBounds(0, 0, 231, 62);
		frame.getContentPane().add(txtpnDictionaryServer);
		
		JTextPane author = new JTextPane();
		author.setText("                   created by: Christina Xu");
		author.setForeground(Color.LIGHT_GRAY);
		author.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		author.setBackground(new Color(25, 25, 112));
		author.setBounds(230, 0, 220, 62);
		frame.getContentPane().add(author);
		
		JTextPane addressPane = new JTextPane();
		addressPane.setText("address:");
		addressPane.setBackground(SystemColor.window);
		addressPane.setBounds(25, 90, 56, 21);
		frame.getContentPane().add(addressPane);
		
		addressField = new JTextField();
		addressField.setColumns(10);
		addressField.setBounds(81, 85, 130, 26);
		frame.getContentPane().add(addressField);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setText("port:");
		textPane_2.setBackground(SystemColor.window);
		textPane_2.setBounds(264, 90, 37, 21);
		frame.getContentPane().add(textPane_2);
		
		portField = new JTextField();
		portField.setColumns(10);
		portField.setBounds(297, 85, 130, 26);
		frame.getContentPane().add(portField);
		
		//when the connect button is pressed, the server will be ready.
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ConnectActionListener());
		btnConnect.setBounds(62, 152, 149, 29);
		frame.getContentPane().add(btnConnect);
		
		//when the disconnect button is pressed, the server will be unavailable.
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new DisconnectActionListener()); 
		btnDisconnect.setBounds(264, 152, 149, 29);
		frame.getContentPane().add(btnDisconnect);
		
		JTextPane filePane = new JTextPane();
		filePane.setText("filePath:");
		filePane.setBackground(SystemColor.window);
		filePane.setBounds(25, 200, 56, 21);
		frame.getContentPane().add(filePane);
		
		fileField = new JTextField();
		//set default path of the dictionary
		fileField.setText(PATH);
		fileField.setColumns(10);
		fileField.setBounds(81, 200, 300, 26);
		frame.getContentPane().add(fileField);
		
		
	}
	
	
	/**
	 * ConnectActionListener: handles when connect button is presseda
	 *
	 */
	private class ConnectActionListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
    		 		
        	if(e.getSource() == btnConnect) {
        		//assign dictionary path to the dictionary
        		filePath = fileField.getText();
        		//server connection becomes available
				//if the address and port number are valid
				address = addressField.getText();
	          	if(!portField.getText().equals("")) {
	          		port = Integer.parseInt(portField.getText());
	          	}
	          
	      	  	//server accpets "localhost" and ip addresses
	          	if (!address.equals("localhost") && (!address.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"))){
	          		JOptionPane.showMessageDialog(frame, "Error: Invalid address.");
	          	//port number must be digits in [0-10000]
	          	}else if(!portField.getText().matches("^[0-9]+$")){
	          		JOptionPane.showMessageDialog(frame, "Error: port number must be digits.");
	          	}else if(port<0 || port>PORT_MAX) {
	          		JOptionPane.showMessageDialog(frame, "Error: port number must be [0-10000].");
	    		}else {       
	    			try {ServerSocket socket = new ServerSocket(port);
	        		     Server server = new Server(socket, filePath);
	        		     new Thread(server).start();
	        		     JOptionPane.showMessageDialog(frame, "Server is running...");
	    		}catch (Exception e1) {	
	    			JOptionPane.showMessageDialog(frame, "Error: invalid file path.");
				}
	    	}
        	}
    	}
	}
	
	/**
	 * DisconnectActionListener: handles when disconnect button is pressed.
	 *
	 */
	private class DisconnectActionListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
        	if(e.getSource() == btnDisconnect) {	
        		try {
        			if (server!=null){
        				server.terminate();
        				JOptionPane.showMessageDialog(frame, "Server is closed.");
        			}else {
        				JOptionPane.showMessageDialog(frame, "Server not availiable.");
        			}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(frame, "Error occurs when disconnecting the server.");
				}
        		finally {
					frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
					frame.setVisible(false);
				}
        }
	}
	}

}
