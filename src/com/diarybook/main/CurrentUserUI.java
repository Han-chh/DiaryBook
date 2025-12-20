package com.diarybook.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CurrentUserUI extends JDialog{
	private int sum_diaries = 0; //总日记篇目
	private final int DEFALT_WIDTH = 300;   //默认注册登录窗体位置
	private final int DEFALT_HEIGHT = 250;
	private final int DEFALT_X = 500;
	private final int DEFALT_Y = 180;
	private JPanel title_panel = new JPanel();
	private JPanel button_panel = new JPanel();
	private DiaryBookUI parent;
	private JLabel current_user_label = new JLabel("当前用户：" + DiaryBook.current_User.getUsername());
	
	
	public CurrentUserUI(DiaryBookUI frame) {
		super(frame,true);
		parent = frame;
		sum_diaries = DiaryBook.current_User.getDiaries().size();
		setTitle("用户");
		setPreferredSize(new Dimension(DEFALT_WIDTH,DEFALT_HEIGHT));
		setLocation(DEFALT_X,DEFALT_Y);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		pack();
		currentUserUIInit();
		setVisible(true);
	}
	
	private void currentUserUIInit() {
		setPanels();
		addThings();
	}
	
	private void setPanels() {
		add(title_panel,BorderLayout.NORTH);
		title_panel.setPreferredSize(new Dimension(300,50));
		add(button_panel,BorderLayout.SOUTH);
		button_panel.setPreferredSize(new Dimension(300,150));
		button_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
	}
	
	
	private void addThings() {
		
		current_user_label.setFont(new Font("Sancerif", Font.BOLD, 15));
		title_panel.add(current_user_label);
		title_panel.add(new JLabel("               "));
		var sum = new JLabel("共 " +sum_diaries +" 篇日记");
		sum.setFont(new Font("Sancerif", Font.BOLD, 15));
		title_panel.add(sum);
		var download_all = new JButton("下载所有日记至本地");
		button_panel.add(download_all);
		var change_username = new JButton("修改用户名");
		button_panel.add(change_username);
		var change_password = new JButton("更改密码");
		button_panel.add(change_password);
		var exit = new JButton("退出登录");
		button_panel.add(exit);
		var delete_user = new JButton("注销用户");
		button_panel.add(delete_user);
		
		exit.addActionListener((e)->{
			int option = new TempMessageBox<>().confirmDialog(CurrentUserUI.this,
					"您确定要退出登录吗？", "CONFIRMLEAVING");

			if (option == TempMessageBox.OK_OPTION) {
				try {
					exitSign();
					new UserEnterUI();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		
		change_username.addActionListener((e)-> {
			try {
				changeUsername();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		});
		
		delete_user.addActionListener((e)->{
			try {
				deleteUser();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		});
		
		download_all.addActionListener((e)->{
			try {
				downloadAll();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			});
		
		change_password.addActionListener((e)->changePassword());
	}
	public void exitSign() throws IOException {
		setVisible(false);
		parent.setVisible(false);
		resetLastuser();
	}
	
	
	private void resetLastuser() throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("LASTUSER")))) {
			oos.writeObject(new User(null,null));
		}
	}
	private void deleteUser() throws IOException {
		if(new TempMessageBox<>().confirmDialog(this, 
				"您确定要将用户名“ "+DiaryBook.current_User.getUsername()+" ”的用户注销吗？（将清除所有日记数据）", "注销用户") ==
				TempMessageBox.OK_OPTION) {
			DiaryBook.Users_Map.remove(DiaryBook.current_User.getUsername());
			new TempMessageBox<>(this);
			resetLastuser();
			this.setVisible(false);
			parent.setVisible(false);
			new UserEnterUI();
		}
	}
	
	private void changeUsername() throws IOException {
		String old_name = DiaryBook.current_User.getUsername();
		String new_name = TempMessageBox.showInputDialog(CurrentUserUI.this, "请输入新的用户名");
		if(new_name!= null ) {
			if(!new_name.strip().equals("")) {
				if(new TempMessageBox<>().confirmDialog(this, 
						"您确定要将用户名“ "+old_name+" ”改为“ "+new_name+" ”吗？", "更改用户名") ==
						TempMessageBox.OK_OPTION) {
					AccountInputChecker<CurrentUserUI> aic = new AccountInputChecker<>(CurrentUserUI.this);
					if(aic.usernameCheck(new_name)) {
						for(String name:DiaryBook.Users_Map.keySet()) {
							if(name.equals(old_name)) {
								DiaryBook.current_User.setUsername(new_name);
								DiaryBook.Users_Map.put(new_name, DiaryBook.current_User);
								DiaryBook.Users_Map.remove(old_name);
								parent.refreshPage();
								current_user_label.setText("当前用户：" + DiaryBook.current_User.getUsername());
								resetLastuser();
								new TempMessageBox<>(this);
							}
						}
					}
				}
			}else {
				new TempMessageBox<CurrentUserUI>(TempMessageBox.INVALID, CurrentUserUI.this);
			}
		}
	}
	
	private void downloadAll() throws IOException {
		int option = new TempMessageBox<CurrentUserUI>().confirmDialog(this, "您将保存“ "+
				DiaryBook.current_User.getUsername()+" ”用户的所有日记至本地（将保存为文件夹“ "+
				DiaryBook.current_User.getUsername()+"的日记本” ）",
				"保存确认");
		if (option == JOptionPane.OK_OPTION) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// 不设置默认目录，使用跨平台方式
			int option2 = fc.showSaveDialog(this);
			if (option2 == JFileChooser.APPROVE_OPTION) {
				
				Path selectedDir = Paths.get(fc.getSelectedFile().getPath());
				Path diaryDir = selectedDir.resolve(DiaryBook.current_User.getUsername() + "的日记本");
				Files.createDirectories(diaryDir);
				TreeSet<Diary> diaries = DiaryBook.current_User.getDiaries();
				for(Diary d: diaries) {
					Path diaryFile = diaryDir.resolve(d.toString());
					try (OutputStreamWriter osw = new OutputStreamWriter(Files.newOutputStream(diaryFile))) {
						osw.write(d.toString());
						osw.append('\r');
						osw.append('\r');
						osw.append("正文：");
						osw.append("\r");
						osw.append(d.getContent());
						osw.close();
					}
				}
				new TempMessageBox<>(this);
			} 
			
		}
	}
	
	private void changePassword() {
		new passwordChangeUI(this);
	}
}
