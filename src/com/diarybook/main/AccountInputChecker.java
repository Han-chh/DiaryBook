package com.diarybook.main;

import java.awt.Window;
import java.io.IOException;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AccountInputChecker<T extends Window>{
	private T parent;
	
	public AccountInputChecker(T parent){
		this.parent = parent;
	}
	
	public boolean registerCheck(JTextField username,JPasswordField password //注册检查及注册 返回User对象
			,JPasswordField password_again) {
		return(usernameAndPasswordValidate(username, password)&&
		passwordDoubleCheck(password, password_again)&&
		usernameCheck(username.getText())); 
	}

	public boolean usernameCheck(String username) {
		if (DiaryBook.Users_Map.containsKey(username)) {
			new TempMessageBox<T>(TempMessageBox.REPEAT_NAME, this.parent);
			return false;
		}
		return true;
	}

	public boolean usernameAndPasswordValidate(JTextField username,JPasswordField password) {
		if(username.getText().strip().equals("")||  //用户名或密码为空白
				new String(password.getPassword()).strip().equals("")) {
			new TempMessageBox<T>(TempMessageBox.INVALID, this.parent);
			return false;
		}else if(password.getPassword().length < 6 ){ //密码长度不足或包含空格) 
			new TempMessageBox<T>(TempMessageBox.PASSWORD_INVALID, this.parent);
			return false;
		}else {
			for(char c:password.getPassword()) {
				if(Character.isWhitespace(c)) {
					new TempMessageBox<T>(TempMessageBox.INVALID, this.parent);
					return false;
				}
			}
		}
		return true;
	}
	
	//检查两次密码是否一致
	public boolean passwordDoubleCheck(JPasswordField password , JPasswordField password_again) {
		if(!new String(password.getPassword()).equals
				(new String(password_again.getPassword()))){
			new TempMessageBox<T>(TempMessageBox.PASSWORD_NOT_SAME, this.parent);
			return false;
		}
		return true;
	}
	
	private boolean passwordCheck(JTextField username,JPasswordField password) throws IOException, ClassNotFoundException {
		if(DiaryBook.Users_Map.containsKey(username.getText())) { //从User set读取user对象
			char[]correct_password = DiaryBook.Users_Map.get(username.getText()).getPassword();
			if (!new String(password.getPassword()).equals(new String(correct_password))) { //检查密码
				new TempMessageBox<T>(TempMessageBox.PASSWORD_INCORRECT, this.parent);
				password.setText("");
				password.requestFocus();
				return false;
			}else {
				return true;
				
			}
		}else {
			new TempMessageBox<T>(TempMessageBox.NO_SUCH_NAME, this.parent);
			return false;
		}
	}
	public boolean signUpCheck(JTextField username,JPasswordField password) throws ClassNotFoundException, IOException {
		
		return(usernameAndPasswordValidate(username, password)&&
		passwordCheck(username, password));
	}
}
