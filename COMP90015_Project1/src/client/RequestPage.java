package client;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.json.simple.JSONObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author christina xu (#945756)
 * Request Page: after the client logged in, 
 * the client can do query/add/remove on the page.
 *
 */
public class RequestPage extends JFrame {

	private JFrame frame;
	private JTextField wordField;
	private JTextField defField;
	private Client client;
	private JButton btnQuery;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnDisconnect;

	/**
	 * Create the application.
	 */
	public RequestPage(Client client) {
		initialize(client);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(Client client) {
		this.client = client;
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextPane txtpnDictionaryClient = new JTextPane();
		txtpnDictionaryClient.setText("Dictionary Client");
		txtpnDictionaryClient.setForeground(Color.WHITE);
		txtpnDictionaryClient.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		txtpnDictionaryClient.setBackground(new Color(25, 25, 112));
		txtpnDictionaryClient.setBounds(0, 0, 231, 62);
		frame.getContentPane().add(txtpnDictionaryClient);
		
		JTextPane author = new JTextPane();
		author.setText("                   created by: Christina Xu");
		author.setForeground(Color.LIGHT_GRAY);
		author.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		author.setBackground(new Color(25, 25, 112));
		author.setBounds(230, 0, 220, 62);
		frame.getContentPane().add(author);
		
		JTextPane txtpnWord = new JTextPane();
		txtpnWord.setBackground(SystemColor.window);
		txtpnWord.setText("Word");
		txtpnWord.setBounds(10, 74, 42, 22);
		frame.getContentPane().add(txtpnWord);
		
		//input word
		wordField = new JTextField();
		wordField.setBounds(20, 95, 253, 26);
		frame.getContentPane().add(wordField);
		wordField.setColumns(10);
		
		//input definition
		defField = new JTextField();
		defField.setBounds(20, 153, 253, 97);
		frame.getContentPane().add(defField);
		defField.setColumns(10);
		
		JTextPane txtpnDefinition = new JTextPane();
		txtpnDefinition.setText("Definition");
		txtpnDefinition.setBackground(SystemColor.window);
		txtpnDefinition.setBounds(10, 133, 73, 22);
		frame.getContentPane().add(txtpnDefinition);
		
		//When query button pressed
		btnQuery = new JButton("Query");
		btnQuery.addActionListener(new QueryActionListener()); 
		btnQuery.setBounds(311, 95, 117, 29);
		frame.getContentPane().add(btnQuery);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new AddActionListener());
		btnAdd.setBounds(311, 139, 117, 29);
		frame.getContentPane().add(btnAdd);
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new RemoveActionListener());
		btnRemove.setBounds(311, 180, 117, 29);
		frame.getContentPane().add(btnRemove);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new DisconnectActionListener());
		btnDisconnect.setBounds(311, 221, 117, 29);
		frame.getContentPane().add(btnDisconnect);
		
		frame.setVisible(true);
		
	}

	/**
	 * QueryActionListener: handles when query button is pushed
	 */
	private class QueryActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnQuery) {
				JSONObject request = new JSONObject();
				request.put("Task","Query");
				request.put("Key", wordField.getText());
				try {
					JSONObject reply = client.request(request);
					defField.setText(reply.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * AddActionListener: handles when add button is pushed
	 */
	private class AddActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == btnAdd) {
				JSONObject request = new JSONObject();
				request.put("Task","Add");
				request.put("Key", wordField.getText());
				request.put("Value", defField.getText());
				try {
					JSONObject reply = client.request(request);
					JOptionPane.showMessageDialog(frame, reply.toJSONString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}		
				wordField.setText("");
				defField.setText("");

			}
		}
	}
	
	/**
	 * RemoveActionListener: handles when remove button is pushed.
	 */
	private class RemoveActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnRemove) {
				JSONObject request = new JSONObject();
				request.put("Task","Remove");
				request.put("Key", wordField.getText());
				try {
					JSONObject reply = client.request(request);
					JOptionPane.showMessageDialog(frame, reply.toJSONString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				wordField.setText("");

			}
		}
	}
	
	/**
	 * DisconnectActionListener: handles when disconnect button is pushed.
	 */
	private class DisconnectActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnDisconnect) {
		
				try {
					client.terminate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally {
					frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
					frame.setVisible(false);
				}
			}
		}
	}
}
