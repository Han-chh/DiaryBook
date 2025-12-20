package com.diarybook.main;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2412963632651246239L;
	/**
	 * 
	 */
	private String Username; 
	private char[] Password;
	private TreeSet<Diary> Diaries = new TreeSet<>();
	private boolean isChange = false;
	
	public User(String Username,char[] Password) {
		this.Username = Username;
		this.Password = Password;
	}
	
	public String getUsername() {
		return Username;
	}
	
	public char[] getPassword() {
		return Password;
	}
	
	public void setUsername(String username) {
		Username = username;
	}
	
	@Override
	public String toString() {
		return "User [Username=" + Username + ", Password=" + Arrays.toString(Password) + "]";
	}
	
	public TreeSet<Diary> getDiaries() {
		return Diaries;
	}
	
	public void setDiaries(TreeSet<Diary> diaries) {
		this.Diaries = diaries;
	}

	public void setPassword(char[] password) {
		Password = password;
	}

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(Password);
		result = prime * result + Objects.hash(Diaries, Username);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(Diaries, other.Diaries) && Arrays.equals(Password, other.Password)
				&& Objects.equals(Username, other.Username);
	}
	
	public void addDiary(Diary diary) {
		this.Diaries.add(diary);
	}
	
}
