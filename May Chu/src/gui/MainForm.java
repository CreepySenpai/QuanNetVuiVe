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
		mainFrame.setTitle("Ch??? Qu??n");
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
		tongQuanTableModel.addColumn("T??n M??y");
		tongQuanTableModel.addColumn("T??n T??i Kho???n");
		tongQuanTableModel.addColumn("T???ng S??? Ti???n ???? N???p");
		danhSachMayTable = new JTable(danhSachMayTableModel);
		danhSachMayTableModel.addColumn("ID");
		danhSachMayTableModel.addColumn("M?? M??y");
		danhSachMayTableModel.addColumn("T??nh Tr???ng");
		danhSachMayTableModel.addColumn("Lo???i M??y");
		danhSachMayTableModel.addColumn("S??? Ti???n");
		danhSachKhachTable = new JTable(danhSachKhachTableModel);
		danhSachKhachTableModel.addColumn("ID");
		danhSachKhachTableModel.addColumn("T??n T??i Kho???n");
		danhSachKhachTableModel.addColumn("T???ng S??? Ti???n ???? N???p");
	}
	
	private void setButton() {
		killproccessButton = new JButton("Kill Proccess");
		killproccessButton.addActionListener(this);
		
		themMayButton = new JButton("Th??m M??y", add_icon);
		themMayButton.addActionListener(this);
		themMayButton.setBackground(new Color(242, 210, 242));
		
		xoaMayButton = new JButton("Xo?? M??y", delete_icon);
		xoaMayButton.addActionListener(this);
		xoaMayButton.setBackground(new Color(242, 210, 242));
		
		suaThongTinMayButton = new JButton("S???a Th??ng Tin M??y", update_icon);
		suaThongTinMayButton.addActionListener(this);
		suaThongTinMayButton.setBackground(new Color(242, 210, 242));
		
		timMayButton = new JButton("T??m M??y", find_icon);
		timMayButton.addActionListener(this);
		timMayButton.setBackground(new Color(242, 210, 242));
		
		themTaiKhoanButton = new JButton("Th??m T??i Kho???n", add_icon);
		themTaiKhoanButton.addActionListener(this);
		themTaiKhoanButton.setBackground(new Color(242, 210, 242));
		
		xoaTaiKhoanButton = new JButton("Xo?? T??i Kho???n", delete_icon);
		xoaTaiKhoanButton.addActionListener(this);
		xoaTaiKhoanButton.setBackground(new Color(242, 210, 242));
		
		timTaiKhoanButton = new JButton("T??m T??i Kho???n", find_icon);
		timTaiKhoanButton.addActionListener(this);
		timTaiKhoanButton.setBackground(new Color(242, 210, 242));
		
		clearMayButton = new JButton("Clear", reset_icon);
		clearMayButton.addActionListener(this);
		clearMayButton.setBackground(new Color(242, 210, 242));
		
		clearKhachButton = new JButton("Clear", reset_icon);
		clearKhachButton.addActionListener(this);
		clearKhachButton.setBackground(new Color(242, 210, 242));
		
		nopTienButton = new JButton("N???p Ti???n", nop_tien_icon);
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
		tenMayLabel = new JLabel("T??n M??y");
		loaiMayLabel = new JLabel("Lo???i M??y");
		soTienLabel = new JLabel("S??? Ti???n/Gi???");
		tenTaiKhoanLabel = new JLabel("T??n T??i Kho???n");
		matKhauLabel = new JLabel("M???t Kh???u");
		
		tenTaiKhoanNopTienLabel = new JLabel("T??n T??i Kho???n");
		soTienCanNopLabel = new JLabel("S??? Ti???n C???n N???p");
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
		mayThuongCheckBox = new JCheckBox("M??y Th?????ng");
		mayThuongCheckBox.addActionListener(this);
		mayVIPCheckBox = new JCheckBox("M??y VIP");
		mayVIPCheckBox.addActionListener(this);
		
		loaiMayButtonGroup.add(mayThuongCheckBox);
		loaiMayButtonGroup.add(mayVIPCheckBox);
	}
	
	private void setBorder() {
		nopTienBorder = BorderFactory.createTitledBorder("N???p Ti???n");
		danhSachMayDangOnlineBorder = BorderFactory.createTitledBorder("M??y ??ang Ch??i");
		danhSachMayBorder = BorderFactory.createTitledBorder("Danh S??ch M??y");
		themMayBorder = BorderFactory.createTitledBorder("Th??m M??y");
		danhSachTaiKhoanBorder = BorderFactory.createTitledBorder("Danh S??ch T??i Kho???n");
		themTaiKhoanBorder = BorderFactory.createTitledBorder("Th??m T??i Kho???n");
		chatVoiUsers = BorderFactory.createTitledBorder("Chat V???i Kh??ch H??ng");
		
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
		mainTabbedPane.add(tongQuanPanel, "T???ng Quan");
		mainTabbedPane.add(themMayPanel, "M??y");
		mainTabbedPane.add(themKhachPanel, "T??i Kho???n");
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
				JOptionPane.showMessageDialog(mainFrame, "B???n Kh??ng Th??? Chat V?? Ch??a M??? K???t N???i!!!");
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
				JOptionPane.showMessageDialog(mainFrame, "B???n ??ang M??? K???t N???i!!!");
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
					
					JOptionPane.showMessageDialog(mainFrame, "Host Th??nh C??ng!");
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
			JOptionPane.showMessageDialog(mainFrame, "Kh??ng ???????c ????? Tr???ng!");
		}
		else if (!SQLStatments.checkTenTaiKhoan(tenNguoiNopTien)) {
			JOptionPane.showMessageDialog(mainFrame, "T??n T??i Kho???n Kh??ng T???n T???i!");
		}
		else {
			if(isMoney(moneyString) == false) {
				JOptionPane.showMessageDialog(mainFrame, "S??? Ti???n Nh???p Kh??ng H???p L???!");
			}
			else {
				int realMoney = Integer.parseInt(moneyString);
				SQLStatments.nopTien(tenNguoiNopTien, realMoney);
				
				JOptionPane.showMessageDialog(mainFrame, "N???p Ti???n Th??nh C??ng!");
					
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
			JOptionPane.showMessageDialog(mainFrame, "Kh??ng ???????c ????? Tr???ng!");
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
			JOptionPane.showMessageDialog(mainFrame, "Kh??ng ???????c ????? Tr???ng!");
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
			
			int check = JOptionPane.showConfirmDialog(mainFrame, "B???n C?? Ch???c Mu???n Xo?? M??y!");
			
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
			
			int check = JOptionPane.showConfirmDialog(mainFrame, "B???n C?? Mu???n Xo?? T??i Kho???n!");
			
			if (check == 0) {
				SQLStatments.DeleteUser(users.getUserName());
				showTaiKhoan();
			}
		}
	}
	
	//find
	
	private void timTaiKhoan() {
		String taiKhoan = JOptionPane.showInputDialog("Nh???p T??n T??i Kho???n C???n T??m:");
		
		if ((taiKhoan.length() > 0) && (taiKhoan != null)) {
			
			danhSachTaiKhoan = SQLStatments.findUser(taiKhoan);
			
			danhSachKhachTableModel.setRowCount(0);
			
			for(Users users : danhSachTaiKhoan) {
				danhSachKhachTableModel.addRow(new Object[] {danhSachKhachTableModel.getRowCount() + 1, users.getUserName(), users.getMoney()});
			}
		}
		else {
			JOptionPane.showMessageDialog(mainFrame, "Ch??a Nh???p T??n T??i Kho???n!");
			showTaiKhoan();
		}
	}
	
	private void timMay() {
		String tenMay = JOptionPane.showInputDialog("Nh???p M?? M??y C???n T??m:");
		
		if ((tenMay.length() > 0) && (tenMay != null)) {
			
			danhSachMay = SQLStatments.findComputer(tenMay);
			
			danhSachMayTableModel.setRowCount(0);
			
			for(Computers computer : danhSachMay) {
				danhSachMayTableModel.addRow(new Object[] {danhSachMayTableModel.getRowCount() + 1, computer.getComputerName(), computer.getTinhTrang(), computer.getComputerType(), computer.getMoneyPerHour()});
			}
			
		}
		
		else {
			JOptionPane.showMessageDialog(mainFrame, "Ch??a Nh???p T??n M??y!");
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
		
		String tenMayCu = JOptionPane.showInputDialog("Nh???p T??n M??y B???n Mu???n C???p Nh???t:");
		
		String tenMayMoi = tenMayTextField.getText();
		String loaiMay = loaiMay();
		String money = isMoney(soTienTextField.getText()) ? soTienTextField.getText() : "";
		
		if ((tenMayMoi.length() == 0) || (loaiMay.length() == 0) || (money.length() == 0)) {
			if (money.length() == 0) {
				JOptionPane.showMessageDialog(mainFrame, "?????nh D???ng Ti???n Kh??ng H???p L???!");
			}
			else {
				JOptionPane.showMessageDialog(mainFrame, "Kh??ng ???????c ????? Tr???ng!");
			}
		}
		else {
			
			int curentMoney = Integer.parseInt(money);
			
			if (SQLStatments.checkTenMay(tenMayCu) == false) {
				JOptionPane.showMessageDialog(mainFrame, "M??y Kh??ng N???m Trong Danh S??ch!");
			}
			else {
				SQLStatments.updateComputer(tenMayCu, tenMayMoi, loaiMay, curentMoney);
				JOptionPane.showMessageDialog(mainFrame, "C???p Nh???t Th??nh C??ng!");
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
			JOptionPane.showMessageDialog(mainFrame, "Vui L??ng Ch???n Lo???i M??y!");
		}
		return loaiMay;
	}
	
//	public static void main(String[] args) {
//		new MainForm("anh", "123");
//	}
//	
}
