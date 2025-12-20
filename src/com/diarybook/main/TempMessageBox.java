package com.diarybook.main;

import java.awt.Window;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class TempMessageBox <T extends Window>extends JOptionPane {
	public static final String INVALID = "INVALID";
	public static final String PASSWORD_INVALID = "PASSWORDINVALID";
	public static final String PASSWORD_NOT_SAME = "PASSWORDNOTSAME";
	public static final String REPEAT_NAME = "REPEATNAME";
	public static final String NO_SUCH_NAME = "NOSUCHNAME";
	public static final String PASSWORD_INCORRECT = "PASSWORDINCORRECT";
	public static final String OVERSAVED = "OVERSAVED";
	public static final String DATE_NOT_SELECTED = "DATENOTSELECTED";
	
	public TempMessageBox() {
		super();
	}
	public TempMessageBox(String type ,T parent) {
		setBounds(530,200,200,200);
		String text = null;
		String title = null;
		int message_type = 0;
		switch (type) {
		case INVALID:
			text = "您输入了空白字符！";
			title = INVALID;
			message_type = WARNING_MESSAGE;
			break;
		case PASSWORD_INVALID:
			text = "请输入六位以上密码！";
			title = PASSWORD_INVALID;
			message_type = WARNING_MESSAGE;
			break;
		case PASSWORD_NOT_SAME:
			text = "您两次输入的密码不一致！";
			title = PASSWORD_NOT_SAME;
			message_type = WARNING_MESSAGE;
			break;
		case REPEAT_NAME:
			text = "用户名已被注册！";
			title = REPEAT_NAME;
			message_type = WARNING_MESSAGE;
			break;
		case NO_SUCH_NAME:
			text = "用户名不存在！";
			title = NO_SUCH_NAME;
			message_type = WARNING_MESSAGE;
			break;
		case PASSWORD_INCORRECT:
			text = "密码错误！";
			title = PASSWORD_INCORRECT;
			message_type = WARNING_MESSAGE;
			break;
		case OVERSAVED:
			text = "您已保存过相同的日记！";
			title = OVERSAVED;
			message_type = WARNING_MESSAGE;
			break;
		case DATE_NOT_SELECTED:
			text = "您尚未选择有效日期！";
			title = DATE_NOT_SELECTED;
			message_type = WARNING_MESSAGE;
			break;
		}
		showMessageDialog(parent,text,title,message_type);
	}
	
	public TempMessageBox(T parent) {
		setBounds(530,200,200,200);
		showMessageDialog(parent,"操作成功！","SUCCESS",DEFAULT_OPTION);
	}
	
	public int confirmDialog(T parent, String text,String title) {
		return showConfirmDialog(parent, text, title, OK_CANCEL_OPTION);
	}
}
