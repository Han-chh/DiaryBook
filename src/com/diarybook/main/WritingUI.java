package com.diarybook.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class WritingUI extends JDialog{
		
	
	private final int DEFALT_WIDTH = 430;   //默认日记写入窗体位置数据
	private final int DEFALT_HEIGHT = 620;
	private final int DEFALT_X = 500;
	private final int DEFALT_Y = 80;
	private JPanel info_panel = new JPanel();
	private JPanel content_panel = new JPanel();
	private Diary diary = null;
	private JTextArea content_area;
	private String content_init = "在此键入您的日记内容...";
	private String location_init = "当前地点...";
	private String mood_init = "您的心情...";
	private String weather_init = "键入天气...";
	private String theme_init = "无";
	private DiaryBookUI parent;
	private JComboBox<String> year = new JComboBox<>();
	private JComboBox<String> month = new JComboBox<>();
	private JComboBox<String> day = new JComboBox<>();
	private JTextField location_text = new JTextField(9);
	private JTextField mood_text = new JTextField(7);
	private JTextField weather_text = new JTextField(7);
	private JTextField theme_text = new JTextField(15);
	private JLabel weekday = new JLabel("星期__");
	private int word_count = 0;
	private JLabel words = new JLabel("字数：" + word_count);
	
	public WritingUI(Diary diary,JFrame parent) {//初始化日记本写入窗口
		super(parent,true);
		this.parent = (DiaryBookUI) parent;
		this.diary = diary;
		setTitle("Diary");
		setPreferredSize(new Dimension(DEFALT_WIDTH,DEFALT_HEIGHT));
		setLocation(DEFALT_X,DEFALT_Y);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		writingInterfaceInit();
		setVisible(true);
	}
	
	public WritingUI(JFrame parent) {//初始化日记本写入窗口
		super(parent,true);
		this.parent = (DiaryBookUI) parent;
		setTitle("Diary");
		setPreferredSize(new Dimension(DEFALT_WIDTH,DEFALT_HEIGHT));
		setLocation(DEFALT_X,DEFALT_Y);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		writingInterfaceInit();
		setVisible(true);
	}
	
	private void writingInterfaceInit() {
		setPanels();
		addWritingArea();
		addInfoComponents();
		this.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				var temp_diary = new Diary();
				temp_diary = setUpDiary(temp_diary); 
				if(checkIfChanged(temp_diary)) {
					
					// 未选择日期
					int option = new TempMessageBox<>().confirmDialog(WritingUI.this,
							"需要保存您的日记吗？", "CONFIRMSAVING");
					if (option == TempMessageBox.CLOSED_OPTION) {
						setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					
					}else if(option == TempMessageBox.CANCEL_OPTION) {
						setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					
					}else if(option == TempMessageBox.OK_OPTION) {
						save();
						setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					}
				
				}
			}
				
		});
		new Thread(new WordCount()).start();
	}
	private void setPanels() {  //向日记写入窗口中加入其他组件
			//初始化所有面板
			//信息面板设置
			info_panel .setLayout(new FlowLayout(FlowLayout.LEFT));
			info_panel.setPreferredSize(new Dimension(300,200));  // 增加高度以容纳所有组件
			info_panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			add(info_panel,BorderLayout.NORTH);
			
			//日记正文面板设置
			content_panel.setLayout(new FlowLayout()); //设置边界布局对齐左边
			content_panel.setPreferredSize(new Dimension(300,50));
			content_panel.setBorder(BorderFactory.createTitledBorder("CONTENT"));
			add(content_panel,BorderLayout.CENTER);
			
	}
	
	private void addInfoComponents() {
		var date = new JLabel("日期：");
		var year_label = new JLabel("年");
		var month_label = new JLabel("月");
		var day_label = new JLabel("日");
		var now_date = new JButton("使用今日日期");
		var location = new JLabel("地点：");
		var mood = new JLabel("心情：");
		var weather = new JLabel("天气：");
		var theme = new JLabel("主题：");
		var save = new JButton("保存");
		theme.setFont(new Font("Sancerif",Font.BOLD,16));
		
		theme_text.setFont(new Font("Sancerif",Font.ITALIC,16));
		weekday.setFont(new Font("Sancerif", Font.BOLD, 12));
		theme_text.addFocusListener(addTextTips(theme_init, theme_text));
		weather_text.addFocusListener(addTextTips(weather_init, weather_text));
		mood_text.addFocusListener(addTextTips(mood_init, mood_text));
		location_text.addFocusListener(addTextTips(location_init, location_text));
		// 添加年月日复选框
		for (int i = 0;i<101;i++) {
			int num_year = 2000 + i;
			String y = new String(Integer.toString(num_year));
			year.addItem(y);
		}
		for (int i = 0;i<12;i++) {
			int num_month = 1 + i;
			String m = new String(Integer.toString(num_month));
			month.addItem(m);
		}
		
		//根据不同年月生成日
		day.addFocusListener(new FocusListener() {
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO 自动生成的方法存根
				calculateDayAmount();
			}
			@Override
			public void focusLost(FocusEvent e) {
				// TODO 自动生成的方法存根
				
				//根据日期计算出周几
				weekday.setText(calculateWeekday((String)year.getSelectedItem(), 
						(String)month.getSelectedItem(), (String)day.getSelectedItem()));
			}
		});
		now_date.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[]now_date_ymd = getNowDate();
				year.setSelectedItem(now_date_ymd[0]);
				month.setSelectedItem(now_date_ymd[1]);
				calculateDayAmount();
				day.setSelectedItem(now_date_ymd[2]);
				weekday.setText(calculateWeekday((String)year.getSelectedItem(), 
						(String)month.getSelectedItem(), (String)day.getSelectedItem()));
				
			}
		});
		
		info_panel.add(date);
		info_panel.add(year);
		info_panel.add(year_label);
		info_panel.add(month);
		info_panel.add(month_label);
		info_panel.add(day);
		info_panel.add(day_label);
		info_panel.add(weekday);
		info_panel.add(now_date);
		
		info_panel.add(weather);
		info_panel.add(weather_text);
		info_panel.add(mood);
		info_panel.add(mood_text);
		info_panel.add(location);
		info_panel.add(location_text);
		
		info_panel.add(theme);
		info_panel.add(theme_text);
		info_panel.add(save);
		info_panel.add(words);		
		
		//加载指定日记
		loadCertainDiary();
		
		if (diary == null) {
			// 设置默认日期为今天
			String[] now_date_ymd = getNowDate();
			year.setSelectedItem(now_date_ymd[0]);
			month.setSelectedItem(now_date_ymd[1]);
			calculateDayAmount();
			day.setSelectedItem(now_date_ymd[2]);
			weekday.setText(calculateWeekday(now_date_ymd[0], now_date_ymd[1], now_date_ymd[2]));
		}
		
		//添加保存功能
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
	}
	
	private void loadCertainDiary() {
		if (diary != null) {
			for(Diary d: DiaryBook.current_User.getDiaries()) {
				if (d.getUid().equals(diary.getUid())) {
					diary = d;
				}
			}
			String[] y_m_d = diary.getDate().toString().split("-");
			year.setSelectedItem(y_m_d[0]);
			month.setSelectedItem(y_m_d[1]);
			calculateDayAmount();
			day.setSelectedItem(y_m_d[2]);
			weekday.setText(calculateWeekday(y_m_d[0],y_m_d[1] ,y_m_d[2]));
			weather_text.setText(diary.getWeather());
			mood_text.setText(diary.getMood());
			theme_text.setText(diary.getTheme());
			location_text.setText(diary.getLocation());
			content_area.setText(diary.getContent());
		}
	}

	private void save(){
		if (day.getSelectedItem() == null) { 
			// 未选择日期
			calculateDayAmount();
			weekday.setText(calculateWeekday((String)year.getSelectedItem(), (String)month.getSelectedItem(),
					(String)day.getSelectedItem()));
			day.setSelectedItem("1");
		} 
		diary = setUpDiary(diary);
		//检测是否已有当前日记
		if(checkRepeatedDiary()) {
			//写入日记
			DiaryBook.current_User.addDiary(diary);
			parent.addOrSaveDiary(DiaryBookUI.current_page,diary);
			new TempMessageBox<>(WritingUI.this);
		}else {
			new TempMessageBox<>(TempMessageBox.OVERSAVED, WritingUI.this);
		
		}
		
	}
	
	private Diary setUpDiary(Diary diary) {
		LocalDate date;
		date = LocalDate.of(Integer.parseInt((String)(year.getSelectedItem())),
				Integer.parseInt((String)(month.getSelectedItem())), 
				Integer.parseInt((String)(day.getSelectedItem()))); //解析为date
		if(diary == null) {		
			diary = new Diary();
		}
		diary.setAll(date,weekday.getText(), theme_text.getText(),mood_text.getText(),
				weather_text.getText(), location_text.getText(),content_area.getText());
		voidCheck(diary);
		return diary;
	
		
	}
	
	private boolean checkIfChanged(Diary diary_now) {
		if (diary == null) return true;
		return !diary.equals(diary_now);
	}
	
	private boolean checkRepeatedDiary() {
		return parent.checkIfRepeatedDiary(diary);
	}

	private void voidCheck(Diary diary) {
		
		if (diary.getContent().strip().equals("")||diary.getContent().strip().
				equals(content_init)) diary.setContent("无");
		
		if (diary.getLocation().strip().equals("")||diary.getLocation().strip().
				equals(location_init))  diary.setLocation("无");
		
		if (diary.getMood().strip().equals("")||diary.getMood().strip().
				equals(mood_init))  diary.setMood("无");
		
		if (diary.getTheme().strip().equals("")||diary.getTheme().strip().
				equals(theme_init))  diary.setTheme("无");
		
		if (diary.getWeather().strip().equals("")||diary.getWeather().strip().
				equals(weather_init))  diary.setWeather("无");
		
	}

	
	private FocusListener addTextTips(String text,JTextField text_field) {
		//文本域提示
		text_field.setText(text);
		FocusListener listener = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO 自动生成的方法存根				
					if (text_field.getText().equals("")) {
						text_field.setText(text);
					}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO 自动生成的方法存根
				if (text_field.getText().equals(text)||text_field.getText().equals("无")){
					text_field.setText("");
				}
			}
		};
		return listener;
	}
	
	private void calculateDayAmount() {
		day.removeAllItems();
		if (!(month.getSelectedIndex()==3||month.getSelectedIndex()==5||
				month.getSelectedIndex()==8||month.getSelectedIndex()==10
				||month.getSelectedIndex()==1)) {
			for (int i = 0;i<31;i++) {
				int num_day = 1 + i;
				String d = new String(Integer.toString(num_day));
				day.addItem(d);
			}
		}else if (month.getSelectedIndex()==1) {
			Integer year_num = Integer.parseInt((String)year.getSelectedItem());
			for (int i = 0;i<28;i++) {
				int num_day = 1 + i;
				String d = new String(Integer.toString(num_day));
				day.addItem(d);
			}
			if (year_num %4 == 0 || (year_num%100 == 0 && year_num % 400 == 0)) {
				day.addItem("29");
			}
		}else {
			for (int i = 0;i<30;i++) {
				int num_day = 1 + i;
				String d = new String(Integer.toString(num_day));
				day.addItem(d);
			}
		}
	}
	private String calculateWeekday(String y,String m,String d) {
		String []weekdays = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		LocalDate date = LocalDate.of(Integer.parseInt(y), 
				Integer.parseInt(m), Integer.parseInt(d));
		return (weekdays[date.getDayOfWeek().ordinal()]);
	}
	
	private String[] getNowDate() {
		LocalDate date = LocalDate.now();
		String now_date[] = date.toString().split("-");
		return now_date;
	}
	private void addWritingArea() {
		content_area = new JTextArea("在此键入您的日记内容...",20,30);
		content_area.setFont(new Font("Sancerif",Font.PLAIN,15));
		content_area.setLineWrap(true); //自动换行
		content_area.setWrapStyleWord(true); // 剩余距离不够单词换行
		content_area.setTabSize(2);
		content_area.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO 自动生成的方法存根
				if (content_area.getText().equals("")){
					content_area.setText("在此键入您的日记内容...");
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO 自动生成的方法存根
				if (content_area.getText().equals(content_init) ||content_area.getText().equals("无")){
					content_area.setText("");
				}
			}
		});
		content_panel.add(content_area);
		var content_scroll = new JScrollPane(content_area); //滚动条
		content_panel.add(content_scroll);
		
		
	}
	private int wordCount(String text) { //返回现在总字数
		if(content_area.getText().equals(content_init)) return 0;
		var word_count = 0;
		for(int i = 0; i< text.length();i++) {
			char c = text.charAt(i);
			if (c == '\r'||c == '\t'|| Character.isWhitespace(c)) continue;
			if(Character.toString(c).matches
					("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】|\\、|\\·]")) {
				word_count++; //汉字及汉字符号
			}else if(Character.isLetterOrDigit(c)) {
				if(i != 0 ){
					if(Character.toString(text.charAt(i - 1)).matches
							("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】|\\\\、|\\\\·]")||
							Character.isWhitespace(text.charAt(i - 1))) {
						word_count++; // 检测至已输入单词
					}
				}else{
					word_count++; // 首个字符
				}
			}else {
				word_count++;  //英文符号
			}
		}
		return word_count;
	}
	
	class WordCount implements Runnable{ //统计字数

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while(true) {
				word_count = WritingUI.this.wordCount(content_area.getText());
				words.setText("字数：" + word_count);
				
			}
		}
		
	}
}
