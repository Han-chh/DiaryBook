package com.diarybook.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DiaryBook {
	public static User current_User;
	public static HashMap<String,User> Users_Map = new HashMap<>();
	
	public static void main(String[] args) throws IOException {
		
		getUsers();
		new UserEnterUI();	
	}
	
	public static void getUsers() {
	
		Path users_directory = Paths.get("Users");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(users_directory)) {
			for (Path f : stream) {
				try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(f))) {
					User user_item = (User) ois.readObject();
					Users_Map.put(user_item.getUsername(), user_item);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveData() throws FileNotFoundException, IOException, ClassNotFoundException{
		Path usersDir = Paths.get("Users");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(usersDir)) {
			ArrayList<Path> files = new ArrayList<>();
			for (Path file : stream) {
				files.add(file);
			}
			Set <String>usernames = Users_Map.keySet();
			ArrayList<User> changeAndAdd = new ArrayList<>();
			ArrayList<User> deleted = new ArrayList<>();
			if (!files.isEmpty()) {
				ArrayList<User>saved_users = new ArrayList<>();
				for(Path f : files) {
					try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(f))) {
						User saved_user = (User)ois.readObject();
						saved_users.add(saved_user);
						if(!usernames.contains(saved_user.getUsername())) {
							deleted.add(saved_user);
						}
					}
				}
				for(String s:usernames) {
					User user = Users_Map.get(s);
					if(user.isChange()|| saved_users.indexOf(user) == -1) {
						changeAndAdd.add(user);
					}
				}	
			}else {
				changeAndAdd.addAll(Users_Map.values());
			}
			for(User u: changeAndAdd) {
				Path userFile = usersDir.resolve(u.getUsername());
				try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(userFile))) {
					oos.writeObject(u);
				}
			}
			for (User u: deleted) {
				Path userFile = usersDir.resolve(u.getUsername());
				Files.deleteIfExists(userFile);
			}
		}
	}
}

