package com.diarybook.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class passwordChangeUI extends JDialog{
	private final int DEFALT_WIDTH = 300;   //默认注册登录窗体位置
	private final int DEFALT_HEIGHT = 230;
	private final int DEFALT_X = 500;
	private final int DEFALT_Y = 180;
	private JPanel title_panel = new JPanel();
	private JPanel input_panel = new JPanel();
	private JPanel button_panel = new JPanel();
	private CurrentUserUI parent ;
	
	public  passwordChangeUI(CurrentUserUI parent){
	  //初始化注册登录窗体
		super(parent,true);
		this.parent = parent;
		setTitle("更改密码");
		setPreferredSize(new Dimension(DEFALT_WIDTH,DEFALT_HEIGHT));
		setLocation(DEFALT_X,DEFALT_Y);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		pack();
		userSignUpInit();
		setVisible(true);
	}
	
	private void userSignUpInit() {
		setPanels();
		addThings();
	}
	
	private void setPanels() {
		add(title_panel,BorderLayout.NORTH);
		input_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(input_panel,BorderLayout.CENTER);
		button_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(button_panel,BorderLayout.SOUTH);
	}
	private void addThings() {
		var title = new JLabel("DiaryBook更改密码界面");
		title.setFont(new Font("Sancerif",Font.ITALIC,16));
		title_panel.add(title);
		var password = new JLabel("请输入原密码：");
		input_panel.add(password);
		var password_text = new JPasswordField(15);
		input_panel.add(password_text);
		var new_password = new JLabel("请输入新密码：");
		input_panel.add(new_password);
		var new_password_text = new JPasswordField(15);
		input_panel.add(new_password_text);
		var new_password_again = new JLabel("请再次输入新密码：");
		input_panel.add(new_password_again);
		var new_password_again_text = new JPasswordField(15);
		input_panel.add(new_password_again_text);
		var apply = new JButton("应用");
		input_panel.add(apply);
		
		
		apply.addActionListener((e)-> {
			char[]correct_password = DiaryBook.current_User.getPassword();
			
			if (!Arrays.equals(password_text.getPassword(),(((correct_password))))) { //检查密码
				new TempMessageBox<>(TempMessageBox.PASSWORD_INCORRECT, this);
				password_text.setText("");
				password_text.requestFocus();
			}else {
				if(new String(new_password_text.getPassword()).strip().equals("")) { //用户名或密码为空白
					new TempMessageBox<>(TempMessageBox.INVALID, this);
				}else if(new_password_text.getPassword().length < 6){ //密码长度不足或包含空格
					
					new TempMessageBox<>(TempMessageBox.PASSWORD_INVALID, this);
				
				}else if(!testSpace(new_password_text.getPassword())) {
					new TempMessageBox<>(TempMessageBox.INVALID, this);
				}else if(new AccountInputChecker<>(this).passwordDoubleCheck
					(new_password_text, new_password_again_text)) {
					if(new TempMessageBox<>().confirmDialog
							(this, "确定要应用新密码吗？您将需要退出重新登录", "应用新密码")==TempMessageBox.OK_OPTION) {
						char[] newPassword = new_password_text.getPassword();
						DiaryBook.Users_Map.get(DiaryBook.current_User.getUsername()).setPassword(newPassword);
						new TempMessageBox<>(this);
						setVisible(false);
						try {
							parent.exitSign();
							new UserEnterUI(DiaryBook.current_User.getUsername());
						} catch (IOException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
						
					}
				}
			}
		});
	}
	
	private boolean testSpace(char[] password) {
		for(char c:password) {
			if(Character.isWhitespace(c)) return false;
		}
		return true;
	}
}

