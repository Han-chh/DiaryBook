package com.diarybook.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class UserEnterUI extends JFrame{
	private final int DEFALT_WIDTH = 300;   //默认注册登录窗体位置
	private final int DEFALT_HEIGHT = 300;
	private final int DEFALT_X = 500;
	private final int DEFALT_Y = 180;
	private JPanel title_panel = new JPanel();
	private JPanel input_panel = new JPanel();
	private JPanel button_panel = new JPanel();
	private JCheckBox remember = new JCheckBox("记住密码");
	private JTextField username_text = new JTextField(15);
	private JPasswordField password_text = new JPasswordField(15);
	
	public UserEnterUI() throws IOException {
	  //初始化注册登录窗体
		super();
		DiaryBook.current_User = null;
		setTitle("用户注册/登录");
		setPreferredSize(new Dimension(DEFALT_WIDTH,DEFALT_HEIGHT));
		setLocation(DEFALT_X,DEFALT_Y);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		userSignUpInit();
		setVisible(true);
	}
	
	public UserEnterUI(String username) throws IOException { //构造同时带有用户名，更改密码时调用
		  //初始化注册登录窗体
			super();
			username_text.setText(username);
			DiaryBook.current_User = null;
			setTitle("用户注册/登录");
			setPreferredSize(new Dimension(DEFALT_WIDTH,DEFALT_HEIGHT));
			setLocation(DEFALT_X,DEFALT_Y);
			setLayout(new BorderLayout());
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
			userSignUpInit();
			setVisible(true);
		}
	
	private void userSignUpInit() throws IOException {
		Path lastUserFile = Paths.get("LASTUSER");
		if(!Files.exists(lastUserFile)) {
			Files.createFile(lastUserFile);
			try (FileOutputStream fos = new FileOutputStream(lastUserFile.toFile());
				 ObjectOutputStream ois = new ObjectOutputStream(fos)) {
				ois.writeObject(new User(null,null));
			}
		}
		setPanels();
		addThings();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int option = new TempMessageBox<>().confirmDialog(UserEnterUI.this,
						"您确定要关闭应用吗？", "CONFIRMLEAVING");
				if (option == TempMessageBox.OK_OPTION) {
					try {
						DiaryBook.saveData();
					} catch (FileNotFoundException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					setDefaultCloseOperation(EXIT_ON_CLOSE);
				}else {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			};
		});
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(Files.newInputStream(Paths.get("LASTUSER")));
			try {
				User user = (User)ois.readObject();
				if (user.getUsername() != null &&user.getPassword()!=null) {
					username_text.setText(user.getUsername());
					password_text.setText(new String(user.getPassword()));
					remember.setSelected(true);
				}else {
					remember.setSelected(false);
				}
				
			} catch (ClassNotFoundException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			} catch (EOFException e1){
				
			}
		} catch (FileNotFoundException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}finally {
			try {
				ois.close();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}
	
	private void setPanels() {
		add(title_panel,BorderLayout.NORTH);
		input_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(input_panel,BorderLayout.CENTER);
		button_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(button_panel,BorderLayout.SOUTH);
	}
	private void addThings() {
		var title = new JLabel("欢迎使用DiaryBook!");
		title.setFont(new Font("Sancerif",Font.ITALIC,18));
		title_panel.add(title);
		var username = new JLabel("请输入用户名：");
		input_panel.add(username);
		
		username_text.setFont(new Font("Sancerif", Font.BOLD, 12));;
		input_panel.add(username_text);
		var password = new JLabel("请输入密码：");
		input_panel.add(password);
		input_panel.add(password_text);
		var signup = new JButton("登录");
		input_panel.add(signup);
		var new_user = new JButton("新建用户");
		button_panel.add(new_user);
		
		button_panel.add(remember);
		
		new_user.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new UserCreateUI(UserEnterUI.this,true);
				
			}
		});
		
		signup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (new AccountInputChecker<>(UserEnterUI.this)//若登录成功
					.signUpCheck(username_text, password_text)) {
						new TempMessageBox<>(UserEnterUI.this);
						DiaryBook.current_User = DiaryBook.Users_Map.get(username_text.getText());
						DiaryBook.current_User.setChange(true);
						if(remember.isSelected()) {
							//需要记住密码
							try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("LASTUSER")))) {
								oos.writeObject(new User(username_text.getText(),password_text.getPassword()));
							}
						}else {
							try (FileOutputStream fos = new FileOutputStream(Paths.get("LASTUSER").toFile());
								 ObjectOutputStream ois = new ObjectOutputStream(fos)) {
								ois.writeObject(new User(null,null));
							}
						}
						setVisible(false);
						new DiaryBookUI();
					}
				} catch (ClassNotFoundException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				};
			}
		});
	}
	
}


