package client;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author christina xu (#945756)
 * Client: client can connect to the dictionary server.
 */
public class Client {
	private Socket socket;
	private OutputStreamWriter wr;
	private BufferedReader br;
	private double time;
	public final int TIME_LIMIT = 100000;
	
	
	/**
	 * start: start the client server
	 * @param address
	 * @param port
	 * @throws Exception
	 */
	public void start(String address,int port) throws Exception {
		try{Socket socket = new Socket(address,port);
			this.socket = socket;
			this.time = System.currentTimeMillis();
		}catch(Exception e) {
			throw new Exception("Error: invalid address.");
		}
		
		try {this.wr = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
		this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(Exception e1) {
			throw new Exception("Error: connection error.");
		}
	}
	
	/**
	 * terminate the client
	 * @throws Exception
	 */
	public void terminate() throws Exception {
		try {
			wr.close();
			br.close();
			socket.close();
		}catch(Exception e) {
			throw new Exception("Error: socket cannot be closed.");
		}
	}
	
	/**
	 * Client process request
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JSONObject request(JSONObject request) throws Exception {
		try {if((System.currentTimeMillis() - this.time)<= TIME_LIMIT) {
				this.time = System.currentTimeMillis();
				wr.write(request.toString()+'\n');
				wr.flush();
				String content =br.readLine();	
				JSONParser jsonParser = new JSONParser();
				JSONObject result = (JSONObject) jsonParser.parse(content);
				return result;
			}else {
				this.terminate();
				throw new Exception("Error: connection error.");
			}
		}catch(Exception e) {
			this.terminate();
			throw new Exception("Error: connection error.");
		}
	}
	



//Testing the server only:
//  public static void main(String arg[]) throws Exception{
//
//  	for (int i =0; i<8; i++) {
//  		Client client = new Client();
//  		client.launch("localhost", 8000);
//  		if(i%7 == 0) {
//  			JSONObject request = new JSONObject();
//  			request.put("Task","Add");
//  			request.put("Key","Apple");
//  			request.put("Value","Fruit,MAC");
//  			System.out.println(client.request(request).toString());
//  		}
//  		else if(i%7==1) {
//  			JSONObject request = new JSONObject();
//  			request.put("Task","Query");
//  			request.put("Key","Apple");
//  			System.out.println(client.request(request).toString());
//  			
//  		}
//          else if(i%7==2) {
//              JSONObject request = new JSONObject();
//              request.put("Task","Remove");
//              request.put("Key","Apple");
//              System.out.println(client.request(request).toString());
//
//          }
//          else if(i%7==3) {
//              JSONObject request = new JSONObject();
//              request.put("Task","Add");
//              request.put("Key","Pear");
//              request.put("Value","Fruit,OKKKKKKKK");
//              System.out.println(client.request(request).toString());
//
//          }
//          else if(i%7==4) {
//              JSONObject request = new JSONObject();
//              request.put("Task","Query");
//              request.put("Key","Mad");
//              System.out.println(client.request(request).toString());
//
//          }
//          else if(i%7==5) {
//              JSONObject request = new JSONObject();
//              request.put("Task","Query");
//              request.put("Key","Pear");
//              System.out.println(client.request(request).toString());
//
//          }
//  		else {
//  			JSONObject request = new JSONObject();
//  			request.put("Task","Remove");
//  			request.put("Key","Pear");
//  			System.out.println(client.request(request).toString());
//  		}
//  	} 
//	}
}