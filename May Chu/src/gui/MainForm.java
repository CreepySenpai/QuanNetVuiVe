package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.module.ModuleDescriptor.Requires;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import javax.print.attribute.standard.RequestingUserName;
import javax.sound.midi.VoiceStatus;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import connect.SocketLib;
import something.Computers;
import something.Online;
import something.Users;
import sqlstatments.SQLStatments;

public class MainForm implements ActionListener{
	
	private JFrame mainFrame;
	
	private JPanel tongQuanPanel, themMayPanel, themKhachPanel, chatPanel, nopTienPanel;
	
	private JTabbedPane mainTabbedPane;
	
	private JTable tongQuanTable, danhSachMayTable, danhSachKhachTable;
	
	private JLabel tenMayLabel, loaiMayLabel, soTienLabel, tenTaiKhoanLabel, matKhauLabel;
	private JLabel tenTaiKhoanNopTienLabel, soTienCanNopLabel;
	private JLabel guiTinLabel;
	
	private JTextField tenMayTextField, soTienTextField, tenTaiKhoanTextField, matKhauTextField;
	private JTextField tenTaiKhoanNopTienTextField, soTienCanNopTextField;
	private JTextField messageTextField;
	
	private JButton killproccessButton, themMayButton, xoaMayButton, suaThongTinMayButton, timMayButton, themTaiKhoanButton, xoaTaiKhoanButton, timTaiKhoanButton;
	private JButton clearMayButton, clearKhachButton, nopTienButton, refreshButton;
	private JButton sendButton, hostButton;
	
	private JCheckBox mayThuongCheckBox, mayVIPCheckBox;
	
	private JTextArea khungChatTextPane;
	
	private ButtonGroup loaiMayButtonGroup;
	
	private JScrollPane scrollPane, danhSachMayScrollPane, danhSachDangOnlineScrollPane, danhSachTaiKhoanScrollPane;
	
	private Border nopTienBorder, danhSachMayDangOnlineBorder, danhSachMayBorder, themMayBorder, danhSachTaiKhoanBorder, themTaiKhoanBorder, chatVoiUsers;
	
	private DefaultTableModel tongQuanTableModel, danhSachMayTableModel, danhSachKhachTableModel;
	
	private ImageIcon icon, add_icon, delete_icon, find_icon, host_icon, nop_tien_icon, reset_icon, send_icon, update_icon, refesh_icon;
	
	private final int nHEIGHT = 900;
	
	private final int nWIDTH = 100;
	
	private final int PORT = 9888;
	
	private ServerSocket serverSocket;
	
	private Socket socket;
	
	private DataInputStream dataInputStream;
	
	private DataOutputStream dataOutputStream;
	
	private SocketLib socketLib;
	
	private ArrayList<Computers> danhSachMay;
	private ArrayList<Users> danhSachTaiKhoan;
	private ArrayList<Online> danhSachMayVaNguoiChoiOnline;
	private ArrayList<String> danhSachTenTaiKhoan;
	
	private String adminName, adminPassword;
	
	private int checkHost = 0;
	
	public MainForm(String adminName, String adminPassword) {
		this.adminName = adminName;
		this.adminPassword = adminPassword;
		mainFrame = new JFrame();
		tongQuanTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		danhSachKhachTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		danhSachMayTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		khungChatTextPane = new JTextArea();
		khungChatTextPane.setEditable(false);
		khungChatTextPane.setBackground(new Color(240, 248, 255));
		icon = new ImageIcon("res/admin_icon.png");
		
		//set
		setFrame();
		setIcon();
		setBorder();
		setPanel();
		setTable();
		setButton();
		setLabel();
		setTextField();
		setCheckBox();
		setScrollPane();
		
		//add
		addPanelToTabbedPane();
		addItemToTongQuanPanel();
		addItemToDanhSachMayPanel();
		addItemToDanhSachKhachPanel();
		addItemToChatPanel();
		
		//show
		showMay();
		showTaiKhoan();
		showDanhSachMayVaNguoiChoiOnline();
		
		mainFrame.pack();
		
		//checkwindow closing
		
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if(socketLib != null) socketLib.CloseConnect();
					if((serverSocket != null) && (socket != null)) {
					SocketLib.CloseConnect(serverSocket, socket);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}
	
	private void setFrame() {
		mainFrame.setTitle("Chủ Quán");
		mainFrame.setSize(nWIDTH, nHEIGHT);
		mainFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setIconImage(icon.getImage());
	}
	
	private void setPanel() {
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.setBackground(new Color(255, 160, 122));
		tongQuanPanel = new JPanel();
		tongQuanPanel.setLayout(new BorderLayout());
		tongQuanPanel.setBorder(danhSachMayDangOnlineBorder);
		themMayPanel = new JPanel();
		themMayPanel.setLayout(new BorderLayout());
		themMayPanel.setBorder(danhSachMayBorder);
		themKhachPanel = new JPanel();
		themKhachPanel.setLayout(new BorderLayout());
		themKhachPanel.setBorder(danhSachTaiKhoanBorder);
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setBorder(chatVoiUsers);
		
		nopTienPanel = new JPanel();
		nopTienPanel.setBorder(nopTienBorder);
		nopTienPanel.setLayout(new GridLayout(3, 2));
		
	}
	
	private void setTable() {
		tongQuanTable = new JTable(tongQuanTableModel);
		tongQuanTableModel.addColumn("ID");
		tongQuanTableModel.addColumn("Tên Máy");
		tongQuanTableModel.addColumn("Tên Tài Khoản");
		tongQuanTableModel.addColumn("Tổng Số Tiền Đã Nạp");
		danhSachMayTable = new JTable(danhSachMayTableModel);
		danhSachMayTableModel.addColumn("ID");
		danhSachMayTableModel.addColumn("Mã Máy");
		danhSachMayTableModel.addColumn("Tình Trạng");
		danhSachMayTableModel.addColumn("Loại Máy");
		danhSachMayTableModel.addColumn("Số Tiền");
		danhSachKhachTable = new JTable(danhSachKhachTableModel);
		danhSachKhachTableModel.addColumn("ID");
		danhSachKhachTableModel.addColumn("Tên Tài Khoản");
		danhSachKhachTableModel.addColumn("Tổng Số Tiền Đã Nộp");
	}
	
	private void setButton() {
		killproccessButton = new JButton("Kill Proccess");
		killproccessButton.addActionListener(this);
		
		themMayButton = new JButton("Thêm Máy", add_icon);
		themMayButton.addActionListener(this);
		themMayButton.setBackground(new Color(242, 210, 242));
		
		xoaMayButton = new JButton("Xoá Máy", delete_icon);
		xoaMayButton.addActionListener(this);
		xoaMayButton.setBackground(new Color(242, 210, 242));
		
		suaThongTinMayButton = new JButton("Sửa Thông Tin Máy", update_icon);
		suaThongTinMayButton.addActionListener(this);
		suaThongTinMayButton.setBackground(new Color(242, 210, 242));
		
		timMayButton = new JButton("Tìm Máy", find_icon);
		timMayButton.addActionListener(this);
		timMayButton.setBackground(new Color(242, 210, 242));
		
		themTaiKhoanButton = new JButton("Thêm Tài Khoản", add_icon);
		themTaiKhoanButton.addActionListener(this);
		themTaiKhoanButton.setBackground(new Color(242, 210, 242));
		
		xoaTaiKhoanButton = new JButton("Xoá Tài Khoản", delete_icon);
		xoaTaiKhoanButton.addActionListener(this);
		xoaTaiKhoanButton.setBackground(new Color(242, 210, 242));
		
		timTaiKhoanButton = new JButton("Tìm Tài Khoản", find_icon);
		timTaiKhoanButton.addActionListener(this);
		timTaiKhoanButton.setBackground(new Color(242, 210, 242));
		
		clearMayButton = new JButton("Clear", reset_icon);
		clearMayButton.addActionListener(this);
		clearMayButton.setBackground(new Color(242, 210, 242));
		
		clearKhachButton = new JButton("Clear", reset_icon);
		clearKhachButton.addActionListener(this);
		clearKhachButton.setBackground(new Color(242, 210, 242));
		
		nopTienButton = new JButton("Nộp Tiền", nop_tien_icon);
		nopTienButton.addActionListener(this);
		nopTienButton.setBackground(new Color(242, 210, 242));
		
		refreshButton = new JButton("Refesh", refesh_icon);
		refreshButton.addActionListener(this);
		refreshButton.setBackground(new Color(242, 210, 242));
		
		sendButton = new JButton("Send", send_icon);
		sendButton.addActionListener(this);
		sendButton.setBackground(new Color(255, 141, 172));
		
		hostButton = new JButton("Host", host_icon);
		hostButton.addActionListener(this);
		hostButton.setBackground(new Color(255, 141, 172));
	}
	
	private void setLabel() {
		tenMayLabel = new JLabel("Tên Máy");
		loaiMayLabel = new JLabel("Loại Máy");
		soTienLabel = new JLabel("Số Tiền/Giờ");
		tenTaiKhoanLabel = new JLabel("Tên Tài Khoản");
		matKhauLabel = new JLabel("Mật Khẩu");
		
		tenTaiKhoanNopTienLabel = new JLabel("Tên Tài Khoản");
		soTienCanNopLabel = new JLabel("Số Tiền Cần Nộp");
		guiTinLabel = new JLabel("Message:");
	}
	
	private void setTextField() {
		tenMayTextField = new JTextField();
		soTienTextField = new JTextField();
		soTienTextField.setEditable(false);
		tenTaiKhoanTextField = new JTextField();
		matKhauTextField = new JTextField();
		
		tenTaiKhoanNopTienTextField = new JTextField();
		soTienCanNopTextField = new JTextField();
		messageTextField = new JTextField();
	}
	
	private void setCheckBox() {
		loaiMayButtonGroup = new ButtonGroup();
		mayThuongCheckBox = new JCheckBox("Máy Thường");
		mayThuongCheckBox.addActionListener(this);
		mayVIPCheckBox = new JCheckBox("Máy VIP");
		mayVIPCheckBox.addActionListener(this);
		
		loaiMayButtonGroup.add(mayThuongCheckBox);
		loaiMayButtonGroup.add(mayVIPCheckBox);
	}
	
	private void setBorder() {
		nopTienBorder = BorderFactory.createTitledBorder("Nộp Tiền");
		danhSachMayDangOnlineBorder = BorderFactory.createTitledBorder("Máy Đang Chơi");
		danhSachMayBorder = BorderFactory.createTitledBorder("Danh Sách Máy");
		themMayBorder = BorderFactory.createTitledBorder("Thêm Máy");
		danhSachTaiKhoanBorder = BorderFactory.createTitledBorder("Danh Sách Tài Khoản");
		themTaiKhoanBorder = BorderFactory.createTitledBorder("Thêm Tài Khoản");
		chatVoiUsers = BorderFactory.createTitledBorder("Chat Với Khách Hàng");
		
	}
	
	private void setIcon() {
		add_icon = new ImageIcon("res/add_icon.png");
		delete_icon = new ImageIcon("res/delete_icon.png");
		find_icon = new ImageIcon("res/find_icon.png");
		host_icon = new ImageIcon("res/host_icon.png");
		nop_tien_icon = new ImageIcon("res/nop_tien_icon.png");
		reset_icon = new ImageIcon("res/reset_icon.png");
		send_icon = new ImageIcon("res/send_icon.png");
		update_icon = new ImageIcon("res/update_icon.png");
		refesh_icon = new ImageIcon("res/refesh_icon.png");
	}
	
	private void setScrollPane() {
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(khungChatTextPane);
		danhSachMayScrollPane = new JScrollPane();
		danhSachMayScrollPane.setViewportView(danhSachMayTable);
		danhSachDangOnlineScrollPane = new JScrollPane();
		danhSachDangOnlineScrollPane.setViewportView(tongQuanTable);
		danhSachTaiKhoanScrollPane = new JScrollPane();
		danhSachTaiKhoanScrollPane.setViewportView(danhSachKhachTable);
	}
	
	private void addPanelToTabbedPane() {
		mainTabbedPane.add(tongQuanPanel, "Tổng Quan");
		mainTabbedPane.add(themMayPanel, "Máy");
		mainTabbedPane.add(themKhachPanel, "Tài Khoản");
		mainTabbedPane.add(chatPanel, "Chat");
				
		mainFrame.add(mainTabbedPane);
	}
	
	private void addItemToTongQuanPanel() {
		tongQuanPanel.add(tongQuanTable.getTableHeader(), BorderLayout.NORTH);
		tongQuanPanel.add(danhSachDangOnlineScrollPane, BorderLayout.CENTER);
		
		nopTienPanel.add(tenTaiKhoanNopTienLabel);
		nopTienPanel.add(tenTaiKhoanNopTienTextField);
		nopTienPanel.add(soTienCanNopLabel);
		nopTienPanel.add(soTienCanNopTextField);
		nopTienPanel.add(nopTienButton);
		nopTienPanel.add(refreshButton);
		
		tongQuanPanel.add(nopTienPanel, BorderLayout.SOUTH);
	}
	
	private void addItemToDanhSachMayPanel() {
		themMayPanel.add(danhSachMayTable.getTableHeader(),BorderLayout.NORTH);
		themMayPanel.add(danhSachMayScrollPane, BorderLayout.CENTER);
		
		JPanel miniPanel = new JPanel();
		miniPanel.setLayout(new GridLayout(4, 2));
		miniPanel.setBorder(themMayBorder);
		miniPanel.add(tenMayLabel);
		miniPanel.add(tenMayTextField);
		miniPanel.add(loaiMayLabel);
		
		
		JPanel superMiniPanel = new JPanel(new GridLayout(1, 2));
		
		superMiniPanel.add(mayThuongCheckBox);
		superMiniPanel.add(mayVIPCheckBox);
		
		miniPanel.add(superMiniPanel);
		miniPanel.add(soTienLabel);
		miniPanel.add(soTienTextField);
		
		JPanel ultraMiniPanel_1 = new JPanel(new GridLayout(1, 2));
		ultraMiniPanel_1.add(themMayButton);
		ultraMiniPanel_1.add(suaThongTinMayButton);
		
		JPanel ultraMiniPanel_2 = new JPanel(new GridLayout(1, 3));
		ultraMiniPanel_2.add(timMayButton);
		ultraMiniPanel_2.add(xoaMayButton);
		ultraMiniPanel_2.add(clearMayButton);
		
		miniPanel.add(ultraMiniPanel_1);
		miniPanel.add(ultraMiniPanel_2);
		
		themMayPanel.add(miniPanel, BorderLayout.SOUTH);
	}
	
	private void addItemToDanhSachKhachPanel() {
		themKhachPanel.add(danhSachKhachTable.getTableHeader(), BorderLayout.NORTH);
		themKhachPanel.add(danhSachTaiKhoanScrollPane, BorderLayout.CENTER);
		
		JPanel miniPanel = new JPanel();
		miniPanel.setLayout(new GridLayout(3, 2));
		miniPanel.setBorder(themTaiKhoanBorder);
		miniPanel.add(tenTaiKhoanLabel);
		miniPanel.add(tenTaiKhoanTextField);
		miniPanel.add(matKhauLabel);
		miniPanel.add(matKhauTextField);
		
		JPanel superMiniPanel = new JPanel(new GridLayout(1, 2));
		superMiniPanel.add(themTaiKhoanButton);
		superMiniPanel.add(timTaiKhoanButton);
		
		JPanel ultraMiniPanel = new JPanel(new GridLayout(1, 2));
		ultraMiniPanel.add(xoaTaiKhoanButton);
		ultraMiniPanel.add(clearKhachButton);
		
		miniPanel.add(superMiniPanel);
		miniPanel.add(ultraMiniPanel);
		
		themKhachPanel.add(miniPanel, BorderLayout.SOUTH);
	}
	
	private void addItemToChatPanel() {
		chatPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel miniPanel = new JPanel(new GridLayout(1, 3));
		
		JPanel superMiniPanel = new JPanel(new GridLayout(1, 2));
		
		superMiniPanel.add(hostButton);
		superMiniPanel.add(guiTinLabel);
		
		miniPanel.add(superMiniPanel);
		miniPanel.add(messageTextField);
		miniPanel.add(sendButton);
		
		chatPanel.add(miniPanel, BorderLayout.SOUTH);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if((e.getSource() == clearMayButton) || (e.getSource() == clearKhachButton)) {
			resetMayField();
			resetTaiKhoanField();
		}
		
		if(e.getSource() == mayThuongCheckBox) {
			soTienTextField.setText("5000");
		}
		
		if(e.getSource() == mayVIPCheckBox) {
			soTienTextField.setText("10000");
		}
		
		if(e.getSource() == sendButton) {
			if(checkHost == 0) {
				JOptionPane.showMessageDialog(mainFrame, "Bạn Không Thể Chat Vì Chưa Mở Kết Nối!!!");
			}
			else {
				sendMessage();
			}
		}
		
		if(e.getSource() == nopTienButton) {
			nopTien();
		}
		
		if(e.getSource() == themMayButton) {
			themMay();
		}
		
		if (e.getSource() == themTaiKhoanButton) {
			themTaiKhoan();
		}
		
		if (e.getSource() == timTaiKhoanButton) {
			timTaiKhoan();
		}
		
		if (e.getSource() == timMayButton) {
			timMay();
		}
		
		if (e.getSource() == suaThongTinMayButton) {
			suaThongTinMay();
		}
		
		if (e.getSource() == hostButton) {
			if(checkHost == 0) {
				connect();
				checkHost++;
			}
			else {
				JOptionPane.showMessageDialog(mainFrame, "Bạn Đang Mở Kết Nối!!!");
			}
		}
		
		if(e.getSource() == xoaMayButton) {
			xoaMay();
		}
		
		if (e.getSource() == xoaTaiKhoanButton) {
			xoaKhach();
		}
		if (e.getSource() == refreshButton) {
			showDanhSachMayVaNguoiChoiOnline();
		}
	}
	
	//Condition Functions
	
	private void connect() {
		
		Thread thread = new Thread() {
			public void run() {
				try {
					serverSocket = new ServerSocket(PORT);
								
					socket = serverSocket.accept();
							
					socketLib = new SocketLib(socket, khungChatTextPane);
					
					JOptionPane.showMessageDialog(mainFrame, "Host Thành Công!");
					} catch (IOException e) {	
						e.printStackTrace();
				}
			}
		};
		
		thread.start();
	}
	
	
	//Send Message To Client
	private void sendMessage() {
		String messageOut = messageTextField.getText().trim();
		if(messageOut.equals("")) {
			return; //do nothing
		}
		messageOut = "Admin: " + messageOut;
		
		khungChatTextPane.append(messageOut + "\n");
		
		socketLib.Send(messageOut);
		
		messageTextField.setText("");
	}
	
	
	private void nopTien() {
		String tenNguoiNopTien = tenTaiKhoanNopTienTextField.getText();
		
		String moneyString = soTienCanNopTextField.getText();
		
		if ((tenNguoiNopTien.length() == 0) || (moneyString.length() == 0)) {
			JOptionPane.showMessageDialog(mainFrame, "Không Được Để Trống!");
		}
		else if (!SQLStatments.checkTenTaiKhoan(tenNguoiNopTien)) {
			JOptionPane.showMessageDialog(mainFrame, "Tên Tài Khoản Không Tồn Tại!");
		}
		else {
			if(isMoney(moneyString) == false) {
				JOptionPane.showMessageDialog(mainFrame, "Số Tiền Nhập Không Hợp Lệ!");
			}
			else {
				int realMoney = Integer.parseInt(moneyString);
				SQLStatments.nopTien(tenNguoiNopTien, realMoney);
				
				JOptionPane.showMessageDialog(mainFrame, "Nộp Tiền Thành Công!");
					
				resetNopTien();
				
				showDanhSachMayVaNguoiChoiOnline();
			}
		}
	}
	
	//add
	
	private void themMay() {
		String maMay = tenMayTextField.getText();
		String loaiMay = loaiMay();
		String money = soTienTextField.getText();
		
		if ((maMay.length() == 0) || (loaiMay.length() == 0) || (money.length() == 0)) {
			JOptionPane.showMessageDialog(mainFrame, "Không Được Để Trống!");
		}
		else {
			int realMoney = Integer.parseInt(money);
			SQLStatments.themMay(maMay, loaiMay, realMoney);
			
			resetMayField();
			showMay();
		}
		
	}
	
	private void themTaiKhoan() {
		String tenTaiKhoan = tenTaiKhoanTextField.getText();
		String matKhau = matKhauTextField.getText();
		
		if ((tenTaiKhoan.length() == 0) || (matKhau.length() == 0)) {
			JOptionPane.showMessageDialog(mainFrame, "Không Được Để Trống!");
		}
		else {
			SQLStatments.themTaiKhoan(tenTaiKhoan, matKhau);
			
			resetTaiKhoanField();
			showTaiKhoan();
		}
	}
	
	//delete
	
	private void xoaMay() {
		int selectPosition = danhSachMayTable.getSelectedRow();
		
		if (selectPosition > 0) {
			Computers computers = danhSachMay.get(selectPosition);
			
			int check = JOptionPane.showConfirmDialog(mainFrame, "Bạn Có Chắc Muốn Xoá Máy!");
			
			if (check == 0) {
				SQLStatments.DeleteComputer(computers.getComputerName());
				showMay();
			}
		}
	}
	
	private void xoaKhach() {
		int selectPosition = danhSachKhachTable.getSelectedRow();
		
		if (selectPosition > 0) {
			Users users = danhSachTaiKhoan.get(selectPosition);
			
			int check = JOptionPane.showConfirmDialog(mainFrame, "Bạn Có Muốn Xoá Tài Khoản!");
			
			if (check == 0) {
				SQLStatments.DeleteUser(users.getUserName());
				showTaiKhoan();
			}
		}
	}
	
	//find
	
	private void timTaiKhoan() {
		String taiKhoan = JOptionPane.showInputDialog("Nhập Tên Tài Khoản Cần Tìm:");
		
		if ((taiKhoan.length() > 0) && (taiKhoan != null)) {
			
			danhSachTaiKhoan = SQLStatments.findUser(taiKhoan);
			
			danhSachKhachTableModel.setRowCount(0);
			
			for(Users users : danhSachTaiKhoan) {
				danhSachKhachTableModel.addRow(new Object[] {danhSachKhachTableModel.getRowCount() + 1, users.getUserName(), users.getMoney()});
			}
		}
		else {
			JOptionPane.showMessageDialog(mainFrame, "Chưa Nhập Tên Tài Khoản!");
			showTaiKhoan();
		}
	}
	
	private void timMay() {
		String tenMay = JOptionPane.showInputDialog("Nhập Mã Máy Cần Tìm:");
		
		if ((tenMay.length() > 0) && (tenMay != null)) {
			
			danhSachMay = SQLStatments.findComputer(tenMay);
			
			danhSachMayTableModel.setRowCount(0);
			
			for(Computers computer : danhSachMay) {
				danhSachMayTableModel.addRow(new Object[] {danhSachMayTableModel.getRowCount() + 1, computer.getComputerName(), computer.getTinhTrang(), computer.getComputerType(), computer.getMoneyPerHour()});
			}
			
		}
		
		else {
			JOptionPane.showMessageDialog(mainFrame, "Chưa Nhập Tên Máy!");
			showMay();
		}
	}
	
	//show
	
	private void showTaiKhoan() {
		
		danhSachTaiKhoan = SQLStatments.getDanhSachTaiKhoan();
		
		danhSachKhachTableModel.setRowCount(0);
		
		for(Users users : danhSachTaiKhoan) {
			danhSachKhachTableModel.addRow(new Object[] {danhSachKhachTableModel.getRowCount() + 1, users.getUserName(), users.getMoney()});
		}
	}
	
	private void showMay() {
		
		danhSachMay = SQLStatments.getDanhSachMay();
		
		danhSachMayTableModel.setRowCount(0);
		
		for(Computers computers : danhSachMay) {
			danhSachMayTableModel.addRow(new Object[] {danhSachMayTableModel.getRowCount() + 1, computers.getComputerName(), computers.getTinhTrang(), computers.getComputerType(), computers.getMoneyPerHour()});
		}
	}
	
	private void showDanhSachMayVaNguoiChoiOnline() {
		danhSachMayVaNguoiChoiOnline = SQLStatments.getDanhSachMayVaNguoiChoiOnline();
		
		tongQuanTableModel.setRowCount(0);
		
		for(Online online : danhSachMayVaNguoiChoiOnline) {
			tongQuanTableModel.addRow(new Object[] {tongQuanTableModel.getRowCount() + 1, online.getComputerName(), online.getUserName(), online.getTongSoTienDaNap()});
		}
	}
	
	//update
	
	//Can check money la digit
	private void suaThongTinMay() {
		
		String tenMayCu = JOptionPane.showInputDialog("Nhập Tên Máy Bạn Muốn Cập Nhật:");
		
		String tenMayMoi = tenMayTextField.getText();
		String loaiMay = loaiMay();
		String money = isMoney(soTienTextField.getText()) ? soTienTextField.getText() : "";
		
		if ((tenMayMoi.length() == 0) || (loaiMay.length() == 0) || (money.length() == 0)) {
			if (money.length() == 0) {
				JOptionPane.showMessageDialog(mainFrame, "Định Dạng Tiền Không Hợp Lệ!");
			}
			else {
				JOptionPane.showMessageDialog(mainFrame, "Không Được Để Trống!");
			}
		}
		else {
			
			int curentMoney = Integer.parseInt(money);
			
			if (SQLStatments.checkTenMay(tenMayCu) == false) {
				JOptionPane.showMessageDialog(mainFrame, "Máy Không Nằm Trong Danh Sách!");
			}
			else {
				SQLStatments.updateComputer(tenMayCu, tenMayMoi, loaiMay, curentMoney);
				JOptionPane.showMessageDialog(mainFrame, "Cập Nhật Thành Công!");
				showMay();
				resetMayField();
			}
			
		}
	}
	
	//reset
	private void resetMayField() {
		tenMayTextField.setText("");
		soTienTextField.setText("");
		loaiMayButtonGroup.clearSelection();
		showMay();
	}
	
	private void resetTaiKhoanField() {
		tenTaiKhoanTextField.setText("");
		matKhauTextField.setText("");
		showTaiKhoan();
	}
	
	private void resetNopTien() {
		tenTaiKhoanNopTienTextField.setText("");
		soTienCanNopTextField.setText("");
	}
	
	//something
	private boolean isMoney(String money) {
		boolean ismoney = false;
		
		Pattern pattern = Pattern.compile("^\\d+$");
		ismoney = pattern.matcher(money).matches();
		
		return ismoney;
	}
	
	private String loaiMay() {
		String loaiMay = "";
		
		boolean isMayThuong = mayThuongCheckBox.isSelected();
		
		boolean isMayVIP = mayVIPCheckBox.isSelected();
		
		if ((isMayThuong == true) || (isMayVIP == true)) {
			if (isMayThuong == true) {
				loaiMay = "Thuong";
			}
			
			if (isMayVIP == true) {
				loaiMay = "VIP";
			}
		}
		else {
			JOptionPane.showMessageDialog(mainFrame, "Vui Lòng Chọn Loại Máy!");
		}
		return loaiMay;
	}
	
//	public static void main(String[] args) {
//		new MainForm("anh", "123");
//	}
//	
}
