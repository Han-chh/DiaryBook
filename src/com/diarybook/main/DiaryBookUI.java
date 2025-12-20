package com.diarybook.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;



@SuppressWarnings("serial")
public class DiaryBookUI extends JFrame{
	
	private final int DEFALT_WIDTH = 500;   //默认日记本窗体位置数据
	private final int DEFALT_HEIGHT = 600;
	private final int DEFALT_X = 550;
	private final int DEFALT_Y = 120;
	private JPanel title_panel = new JPanel();
	private JPanel manage_panel = new JPanel();
	private JPanel diary_panel = new JPanel();
	private JPanel chapters_panel = new JPanel(); //日记选择面板
	private JPanel page_switch_panel = new JPanel(); //页码选择面板
	public static int current_page = 1;
	private int sum_pages = 1; //总页数
	private final int single_page_max = 10; //每页最多多少篇日记
	private JButton pre = new JButton();
	private JButton next = new JButton();
	private JLabel page = new JLabel();
	private ArrayList<ArrayList<Diary>> diaries_pages;
	private JLabel sum = new JLabel();
	private JButton account = new JButton(DiaryBook.current_User.getUsername()+"的日记本");
	
	public DiaryBookUI() {  //初始化日记本窗口
		super();
		setPreferredSize(new Dimension(DEFALT_WIDTH,DEFALT_HEIGHT));
		setLocation(DEFALT_X,DEFALT_Y);
		setTitle("DiaryBook");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		DiaryBookUIINit();
		setVisible(true);
	}
	
	private void DiaryBookUIINit() {
		// TODO 自动生成的方法存根
		setPanels();
		
		diaries_pages = getSplitPages();
		addThings();
		loadDiariesToPanel(current_page);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int option = new TempMessageBox<>().confirmDialog(DiaryBookUI.this,
						"您确定要关闭应用吗？", "CONFIRMLEAVING");
				// option 确定为0，取消为2 x为-1；
				if (option == 2 || option == -1) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}else {
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
				}
			};
		});
	}

	private void setPanels() {  //向窗口中加入其他组件
		
		//初始化面板
		//标题面板设置
		title_panel.setLayout(new FlowLayout());
		title_panel.setPreferredSize(new Dimension(300,50));
		add(title_panel,BorderLayout.NORTH);
		
		//管理面板（检索、账户管理）
		manage_panel.setLayout(new FlowLayout(FlowLayout.CENTER)); //设置边界布局对齐左边
		manage_panel.setPreferredSize(new Dimension(300,50));
		add(manage_panel,BorderLayout.CENTER);
		
		//日记篇目面板
		diary_panel.setLayout(new BorderLayout()); //设置边界布局
		diary_panel.setPreferredSize(new Dimension(300,450));
		diary_panel.setBorder(BorderFactory.createLineBorder(Color.white));
		add(diary_panel,BorderLayout.SOUTH);
		
		 //章节选择面板
		chapters_panel.setBorder(BorderFactory.createTitledBorder("日记"));
		chapters_panel.setLayout(new GridLayout(single_page_max,1)); 
		chapters_panel.setPreferredSize(new Dimension(300,400));
		diary_panel.add(chapters_panel,BorderLayout.NORTH);
		
		//页码选择面板
		page_switch_panel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
		page_switch_panel.setPreferredSize(new Dimension(300,50));
		diary_panel.add(page_switch_panel,BorderLayout.SOUTH);
		
		//页码选择面板
	}
	private void addThings() {
		//添加面板内组件
		//标题面板
		var title = new JLabel("Dairy Book");
		title.setFont(new Font("Sancerif",Font.ITALIC,23));
		title.setPreferredSize(new Dimension(150,50));
		title_panel.add(title);
		
		//管理面板
		
		account.addActionListener((e)->new CurrentUserUI(this));

		manage_panel.add(account);
		var new_diary = new JButton("新建日记");
		var instruction = new JLabel("双击日记可进行编辑，右键日记可进行删除或保存至本地");
		instruction.setFont(new Font("Sancerif",Font.ITALIC,12));
		
		manage_panel.add(new_diary);
		manage_panel.add(instruction);
		manage_panel.add(sum);
		
		pre.setText("上一页");
		page_switch_panel.add(pre);
		pre.setEnabled(false);
		page.setText(Integer.toString(current_page)+" / "+Integer.toString(sum_pages)+"页");
		page_switch_panel.add(page);
		next.setText("下一页");
		if(sum_pages == current_page) next.setEnabled(false);
		page_switch_panel.add(next);
		
		pre.addActionListener ((e) -> prePage());
		next.addActionListener ((e) -> nextPage());
		
		// 按钮添加监听
		new_diary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new WritingUI(null,DiaryBookUI.this );
			}
		});
	}
	
	private void syncUserSetAndDiaries(TreeSet<Diary> diaries) {
		DiaryBook.current_User.setDiaries(diaries);;
	}
	
	private TreeSet<Diary> getDiaries() {
		return DiaryBook.current_User.getDiaries();
	}
	
	public void addOrSaveDiary(int current_page, Diary diary) {
		TreeSet<Diary> diaries = getDiaries();
		for (Diary d: diaries) {
			if(d.getUid().equals(diary.getUid())) {
				diaries.remove(d);
				break;
			}
		}
		diaries.add(diary);
		syncUserSetAndDiaries(diaries);
		refreshPage();
	}
	
	public void refreshPage() {
		diaries_pages = getSplitPages();
		loadDiariesToPanel(current_page);
	}
	
	private ArrayList<ArrayList<Diary>> getSplitPages() { //将日记集合按每n个日记为一页分页
		TreeSet <Diary> diaries = getDiaries() ;
		ArrayList<ArrayList<Diary>> diaries_pages = new ArrayList<>();
		int remainder = diaries.size() % single_page_max;
		int remain_num = diaries.size();
		Iterator<Diary> iterator = diaries.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			diaries_pages.add(new ArrayList<>());
			if(remain_num > remainder) {
				for(int j = 0;j<single_page_max;j++) {
					diaries_pages.get(i).add(iterator.next());
				}
				remain_num -= single_page_max;
				i++;
			}else {
				for(int j = 0; j<remainder;j++) {
					diaries_pages.get(i).add(iterator.next());
				}
			}	
		}
		
		sum.setText("共"+diaries.size()+"篇日记");
		sum_pages = diaries_pages.size();
		if (sum_pages == 0) sum_pages = 1;
		next.setEnabled(sum_pages != current_page);
		pre.setEnabled(current_page != 1);
		page.setText(Integer.toString(current_page)+" / "+Integer.toString(sum_pages)+"页");
		account.setText(DiaryBook.current_User.getUsername()+"的日记本");
		return diaries_pages;
	}
	
	public boolean checkIfRepeatedDiary(Diary diary) {
		TreeSet<Diary> diaries = getDiaries();
		for(Diary d:diaries) { 
			if (diary.equals(d)&&(!diary.getUid().equals(d.getUid()))) return false;
		}
		return true;
	}
	
	private void loadDiariesToPanel(int page) {
		//加载所有日记至面板 需指定至哪页
		chapters_panel.removeAll();
		chapters_panel.repaint();
		if (diaries_pages.size()!= 0){
			for(int i = 0;i <diaries_pages.get(page - 1).size();i++) {
				chapters_panel.add(new DisplayedDiary(diaries_pages.get(page - 1).get(i),
						DiaryBookUI.this));
			}
		}
		this.pack();
		
	}
	
	private void prePage() {
		current_page--;
		if(current_page == 1) pre.setEnabled(false);
		next.setEnabled(true);
		loadDiariesToPanel(current_page);
	}
	
	private void nextPage() {
		current_page++;
		if(current_page == sum_pages) next.setEnabled(false);
		pre.setEnabled(true);
		loadDiariesToPanel(current_page);
	}
	 
	class DisplayedDiary extends JButton{
		Diary local_diary;
		public DisplayedDiary(Diary local_diary,DiaryBookUI frame) {
			super();
			this.local_diary = local_diary;
			setText(local_diary.toString());
			setHorizontalAlignment(LEFT);
			setBorder(null);
			
			JPopupMenu popupmenu = new JPopupMenu(); //弹出菜单
			var download = new JMenuItem("保存至指定路径");
			popupmenu.add(download);
			popupmenu.addSeparator();
			var delete = new JMenuItem("删除");
			popupmenu.add(delete);
			this.setComponentPopupMenu(popupmenu);
			delete.addActionListener((e)->{
				int option = new TempMessageBox<>().confirmDialog(DiaryBookUI.this,
						"您确定要删除"+ local_diary.toString() +"吗？", "CONFIRMDELETE");
				// option 确定为0，取消为2 x为-1；
				if (option == 0) {
					try {
						deleteDiary();
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
				}
			});
			
			download.addActionListener((e)->{
				try {
					downloadDiary();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			});
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO 自动生成的方法存根
					
					if(e.getClickCount() == 2) {
						new WritingUI(local_diary, DiaryBookUI.this);
					}
				}
			});
			setVisible(true);
			
		}
		
		private void deleteDiary() throws FileNotFoundException, IOException, ClassNotFoundException {
			TreeSet<Diary> diaries = getDiaries();
			for(Diary d: diaries) {
				if(d.equals(local_diary)) {
					diaries.remove(d);
					break;
				}
			}
			syncUserSetAndDiaries(diaries);
			refreshPage();
		}
		
		private void downloadDiary() throws IOException {
			int option = new TempMessageBox<DiaryBookUI>().confirmDialog(DiaryBookUI.this, "您将保存《"+local_diary.toString()+"》日记至本地"
					, "保存确认");
			if (option == JOptionPane.OK_OPTION) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// 不设置默认目录，使用跨平台方式
				int option2 = fc.showSaveDialog(DiaryBookUI.this);
				if (option2 == JFileChooser.APPROVE_OPTION) {
					Path selectedDir = Paths.get(fc.getSelectedFile().getPath());
					Path diaryFile = selectedDir.resolve(local_diary.toString() + ".txt");
					try (OutputStreamWriter osw = new OutputStreamWriter(Files.newOutputStream(diaryFile))) {
						osw.write(local_diary.toString());
						osw.append('\r');
						osw.append('\r');
						osw.append("正文：");
						osw.append("\r");
						osw.append(local_diary.getContent());
						osw.close();
					}
					new TempMessageBox<>(DiaryBookUI.this);
				} 
				
			}
		}
	}
}
