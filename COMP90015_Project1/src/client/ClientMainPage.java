package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author christina Xu (#945756)
 * Client main (contains login GUI for client)
 */
public class ClientMainPage extends JFrame{
	private Client client = new Client();
	private JFrame frame;
	private JTextField addressField;
	private JTextField portField;
	private JButton btnConnect;
	private String address;
	private int port;
	private final int PORT_MAX = 10000;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ClientMainPage();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Initialize the contents of the frame.
	 */
	public ClientMainPage() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextPane txtpnDictionaryServer = new JTextPane();
		txtpnDictionaryServer.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		txtpnDictionaryServer.setForeground(new Color(255, 255, 255));
		txtpnDictionaryServer.setBackground(new Color(25, 25, 112));
		txtpnDictionaryServer.setText("Dictionary Client");
		txtpnDictionaryServer.setBounds(0, 0, 231, 62);
		frame.getContentPane().add(txtpnDictionaryServer);
		
		JTextPane author = new JTextPane();
		author.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		author.setForeground(new Color(192, 192, 192));
		author.setBackground(new Color(25, 25, 112));
		author.setText("                   created by: Christina Xu");
		author.setBounds(230, 0, 220, 62);
		frame.getContentPane().add(author);
		
		JTextPane txtpnAddress = new JTextPane();
		txtpnAddress.setBackground(SystemColor.window);
		txtpnAddress.setText("address:");
		txtpnAddress.setBounds(17, 112, 56, 21);
		frame.getContentPane().add(txtpnAddress);
		
		//input address
		addressField = new JTextField();
		addressField.setBounds(73, 107, 130, 26);
		frame.getContentPane().add(addressField);
		addressField.setColumns(10);
		
		JTextPane txtpnPort = new JTextPane();
		txtpnPort.setText("port:");
		txtpnPort.setBackground(SystemColor.window);
		txtpnPort.setBounds(256, 112, 37, 21);
		frame.getContentPane().add(txtpnPort);
		
		//input port number
		portField = new JTextField();
		portField.setColumns(10);
		portField.setBounds(289, 107, 130, 26);
		frame.getContentPane().add(portField);
		
		//when connect button is pressed,
		//connect the client to the server
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ConnectActionListener());
		btnConnect.setBounds(139, 182, 206, 29);
		frame.getContentPane().add(btnConnect);
		
		frame.setVisible(true);
	}
	
	/**
	 * ConnectActionListener: react when connect button is pushed.
	 *
	 */
	private class ConnectActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnConnect) {
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
	      	  		try {
						client.start(address, port);
		      	  		String msg = "The client is running";
		      	     	JOptionPane.showConfirmDialog(null, msg, msg, JOptionPane.YES_NO_OPTION);		
		      	     	frame.setVisible(false);
		      	     	new RequestPage(client);  		
	      	  		}catch(Exception invalidInputs) {
	      	  			//Invalid inputs, show errors.
						JOptionPane.showMessageDialog(frame, "Error: Please check the address and port.");
	      	  		}
	    		}
			}
		}
	}
}
