package messeges;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class Message {
	public int    id;
	public String nom;
	public String login;
	public String prenom;
	public String password;

	public ArrayList<String> messages;
	
	public Message(int id, String nom, String prenom,String login, String password) {
		this.id = id;
		this.nom = nom;
		this.login = login;
		this.prenom = prenom;
		this.password = password;
		messages = new ArrayList<String>();
	}

	public int getId() {
		return id;
	}
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void addMessage(String themessage) {
		messages.add(themessage);
	}
	public String toString() {
		return String.format("id = %s, login = %s , password = %s , messages = %s", id, login, password, Arrays.toString(messages.toArray()));
	}
	
	public void save() {
		File repository = new File("sauvgardes");
		if (!repository.exists()) {
			repository.mkdir();
		}
		File tosave = new File("sauvgardes/" + id + "_" + login + ".json");
		BufferedWriter bof = null;
		try {
			bof = new BufferedWriter(new FileWriter(tosave));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
		final Gson gson = builder.create();
		final String json = gson.toJson(this);
		try {
			if (bof != null) {
				bof.write(json);
				bof.flush();
				bof.close();
			} else {
				System.err.println("WARNING Serialisation problem has occuerd!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Message getMessage(String id,String login) {
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
		final Gson gson = builder.create();
		File repository = new File("sauvgardes");
		if (!repository.exists()) {
			repository.mkdir();
		}
		File tosave = new File("sauvgardes/" + id + "_" + login + ".json");
		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader(tosave));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Message theMessageObject = gson.fromJson(reader, Message.class);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return theMessageObject;
	}
}
