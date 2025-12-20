package com.diarybook.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class UserCreateUI extends JDialog{
	private final int DEFALT_WIDTH = 300;   //默认注册登录窗体位置
	private final int DEFALT_HEIGHT = 230;
	private final int DEFALT_X = 500;
	private final int DEFALT_Y = 180;
	private JPanel title_panel = new JPanel();
	private JPanel input_panel = new JPanel();
	private JPanel button_panel = new JPanel();
	
	public  UserCreateUI(JFrame parent,boolean model){
	  //初始化注册登录窗体
		super(parent,model);
		setTitle("用户注册");
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
		var title = new JLabel("欢迎使用DiaryBook!(用户注册界面)");
		title.setFont(new Font("Sancerif",Font.ITALIC,16));
		title_panel.add(title);
		var username = new JLabel("请输入用户名：");
		input_panel.add(username);
		var username_text = new JTextField(15);
		username_text.setFont(new Font("Sancerif", Font.BOLD, 12));;
		input_panel.add(username_text);
		var password = new JLabel("请输入密码：");
		input_panel.add(password);
		var password_text = new JPasswordField(15);
		input_panel.add(password_text);
		var password_again = new JLabel("请再次输入密码：");
		input_panel.add(password_again);
		var password_again_text = new JPasswordField(15);
		input_panel.add(password_again_text);
		var register = new JButton("注册");
		input_panel.add(register);
		
		
		register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 if(new AccountInputChecker<JDialog>(UserCreateUI.this).registerCheck //调用注册方法
				(username_text, password_text, password_again_text)) {
											User new_user = new User(username_text.getText(),password_text.getPassword()); 
					
					DiaryBook.Users_Map.put(new_user.getUsername(),new_user);
					new TempMessageBox<JDialog>(UserCreateUI.this);
					setVisible(false);
				 }
			}
		});
	}
}
