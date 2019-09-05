package server;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author christina xu (#945756)
 * Dictionary: read a json dictionary 
 * that supports query/add/remove functions.
 */

public class Dictionary{
	private String filePath;
	private JSONObject dictionary;
	
	public Dictionary(String filePath) 
			throws FileNotFoundException, IOException, ParseException{
		this.filePath = filePath;
		this.dictionary = new JSONObject();
		this.dictionary = readFile(filePath);
	}
		
	/**
	 * read dictionary file
	 * @param filePath
	 * @return dictionary - json object
	 * @throws ParseException 
	 */
	public synchronized JSONObject readFile(String filePath) 
			throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		//read file and return the json object
		try{File f = new File(filePath);
			FileReader fr = new FileReader(f);
			JSONObject jasonObject = (JSONObject) jsonParser.parse(fr);	
			return jasonObject;
		
		//handle exceptions
		}catch(FileNotFoundException e) {
			throw new FileNotFoundException("Please give valid dictionary path");
		}catch(IOException e) {
			throw new IOException("File type error. Please make sure it's JSON file.");
		}catch(ParseException e) {
			throw new ParseException(0, "File type error. Please make sure it's JSON file.");
		}
	}
		
		
	/**
	 * query for a given word
	 * @param word
	 * @return response message
	 */
	public synchronized JSONObject query(String word, String meaning) {
		JSONObject message = new JSONObject();
		if(isInDict(word) && meaning==null) {
			message.put("Query", "Success");
			message.put(word, dictionary.get(word));
			return message;
		}else if (isInDict(word) && meaning!=null){
			message.put("Query", "Failure");
			message.put(word, "Word in dictionary. But please clear the definition field.");
			return message;
		}else if (!isInDict(word) && meaning!=null) {
			message.put("Query", "Failure");
			message.put(word, "Word not in dictionary. Please clear the definition field. ");
			return message;
		}else{
			//clearly indicate if the word was not found
			message.put("Query", "Failure");
			message.put(word,"Word does not exist in the dictionary" );
			return message;
		}
	}

	
	/**
	 * remove a given word from the dictionary
	 * @param word
	 * @return response message
	 */
	public synchronized JSONObject remove(String word, String meaning){
		JSONObject message = new JSONObject();
		if(isInDict(word) && meaning==null) {
			dictionary.remove(word);
			message.put("Remove","Success" );
			message.put(word,": has been removed" );
			return message;
		}else if (isInDict(word) && meaning!=null) {
			message.put("Remove","Failure" );
			message.put(word,"Word is in the dictionary. But please clear the definition field." );
			return message;
		}else {
			message.put("Remove", "Failure");
			message.put(word,"Word does not exist" );
			return message;
		}
	}

	/**
	 * Add a given word to the dictionary
	 * @param word
	 * @param meaning
	 * @return response message
	 */
	public synchronized JSONObject add(String word,String meaning) {
		JSONObject message = new JSONObject();
		if(isInDict(word)) {
			message.put("Add", "Failure");
			message.put(word,"Word already exists in the dictionary" );
			return message;
		}else if (!isInDict(word) && meaning != null){
			dictionary.put(word, meaning);
			message.put("Add", "Success");
			message.put(word,"Word has been added." );
			return message;
		}else{
			message.put("Add", "Failure");
			message.put(word,"Input cannot be null." );
			return message;
		}
	}

	public synchronized JSONObject getDictionary() {
		return dictionary;
	}

	public synchronized void setDictionary(JSONObject dictionary) {
		this.dictionary = dictionary;
	}
	
	/**
	 * check if a word is in the dictionary
	 * @param word
	 * @return boolean
	 */
	public synchronized boolean isInDict(String word) {
		return dictionary.get(word)!=null;
	}
	
}
	
	
	

