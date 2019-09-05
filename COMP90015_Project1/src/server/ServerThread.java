package server;


import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import server.Dictionary;

public class ServerThread implements Runnable{
	public Dictionary dict;
	private Socket socket;
	
	public ServerThread(Dictionary dict,Socket socket) {
		this.dict = dict;
		this.socket = socket;		
	}
	
	/**
	 * parseRequest:parse the request to result
	 * @param request
	 * @return
	 */
	public JSONObject parseRequest(JSONObject request) {
		JSONObject result = null;
		String task = (String) request.get("Task");
		if(task.equals("Query") ){
			result = dict.query((String) request.get("Key"),(String)request.get("Value"));
		}else if(task.equals("Add")) {
			result = dict.add((String)request.get("Key"),(String)request.get("Value"));
		}else if(task.equals("Remove")) {
			result = dict.remove((String)request.get("Key"),(String)request.get("Value"));
		}
		return result;
	}

	public void run() {
		try {BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			OutputStreamWriter wr = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
			String content;
			JSONParser jsonParser = new JSONParser();
			while((content = br.readLine()) != null) {
				JSONObject request = (JSONObject) jsonParser.parse(content);	
				JSONObject result = parseRequest(request);
				wr.write(result.toString()+'\n');
				wr.flush();
			}
			br.close();
			wr.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}