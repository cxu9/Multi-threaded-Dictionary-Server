package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;

import server.ServerThread;

/**
 * @author christina xu (#945756)
 * Server: Dictionary Server that is available for clients to use.
 *
 */
public class Server implements Runnable {

	private Dictionary dict;
	private ServerSocket socket;
	private ArrayList<ServerThread> clients;

	
	public Server(ServerSocket socket, String filepath) throws FileNotFoundException, IOException, ParseException {
		this.dict = new Dictionary(filepath);
		this.socket = socket;
		this.clients = new ArrayList<ServerThread>();
	}

	/**
	 * run the server
	 */
	public void run() {
		try {
			System.out.println("Server is running now...");
			//server continues to wait and accept a connection
			while(true) {	
				try {
					Socket client = socket.accept();
					ServerThread server = new ServerThread(dict,client);
					clients.add(server);
					new Thread(server).start();
				}catch(Exception e) {
					throw new Exception("Error: client lost.");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * terminate the server
	 * @throws Exception
	 */
	public void terminate() throws Exception {
		try {
			for(ServerThread s:clients){
				s.getSocket().close();
		}
		socket.close();
		}catch(IOException g) {
			throw new Exception("Error: Server cannot be closed.");
		}
	}
	
	
	
//Testing for server only:
//	public static void main(String arg[]) throws IOException, ParseException {
//        ServerSocket s = new ServerSocket(8000);
//        Server sc = new Server(s, "/Users/christina/Desktop/COMP90015_DS/COMP90015_Project1/dictionary.json");
//        new Thread(sc).start();
//    }

}