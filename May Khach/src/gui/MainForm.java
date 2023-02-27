package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import connect.SocketLib;
import sqlstatments.RequestUser;



public class MainForm implements ActionListener{
	
	private JFrame mainFrame;
	
	private JTabbedPane mainTabbedPane;
	
	private JPanel tongQuanPanel, chatPanel, resetPasswordPanel;
	
	private JLabel maMayLabel, tenTaiKhoanLabel, soGioChoiConLaiLabel, soTienDaNopLabel;
	private JLabel sendMessageLabel, userNameLabel, currentPasswordLabel, newPasswordLabel;
	
	private JTextField maMayTextField, tenTaiKhoanTextField, soGioChoiConLaiTextField, soTienDaNopTextField;
	private JTextField sendMessaggeField, userNameTextField;
	
	private JButton logOutButton, sendMessageButton, connectingButton, resetPasswordButton;
	
	private JPasswordField currentPasswordField, newPasswordField;
	
	private JTextArea khungChatTextPane;
	
	private JScrollPane scrollPane;
	
	private JCheckBox showBox_1, showBox_2;
	
	private Border chatBorder, tongQuanBorder, resetPasswordBorder;
	
	private JPasswordField oldPassword, newPassword;
	
	private final int nHEIGHT = 900;
	private final int nWIDTH = 1000;
	private final int PORT = 9888;
	
	private ImageIcon icon, connect_icon, send_icon, logout_icon, hide_icon, view_icon;
	
	private String computerName, userName;
	
	private Socket socket;
	
	private String hostName = "127.0.0.1";
	
	private SocketLib socketLib;
	
	private boolean bendTime = false;
	
	private int soTienCuaMay;
	
	private String currentPassword;
	
	public MainForm(String computerName, String userName, String currentPassword, int soTienCuaMay) {
		this.computerName = computerName;
		this.userName = userName;
		this.currentPassword = currentPassword;
		this.soTienCuaMay = soTienCuaMay;
		
		mainFrame = new JFrame();
		khungChatTextPane = new JTextArea();
		khungChatTextPane.setEditable(false);
		khungChatTextPane.setBackground(new Color(240, 255, 255));
		icon = new ImageIcon("res/user_icon.png");
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(khungChatTextPane);
		
		//set
		setFrame();
		setIcon();
		setBorder();
		setPanel();
		setLabel();
		setTextField();
		setButton();
		setCheckBox();
		setPasswordField();
		
		
		//add
		addPanelToTabbedPanel();
		addItemToTongQuanPanel();
		addItemToChatPanel();
		addItemToOptionPanel();
		
		
		//connect();
		
		mainFrame.pack();
	}
	
	private void setFrame() {
		mainFrame.setTitle("Quán Net Vui Vẻ");
		mainFrame.setSize(nWIDTH, nHEIGHT);
		mainFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setIconImage(icon.getImage());
		
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				Thread thread = new Thread() {
					@Override
					public void run() {
						RequestUser.setComputerOffline(computerName);
					}
				};
				
				thread.start();
				
				try {
					if(socket != null) SocketLib.CloseConnect(socket);
					if(socketLib != null) socketLib.CloseConnect();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}
	
	private void setPanel() {
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.setBackground(new Color(255, 105, 180));
		tongQuanPanel = new JPanel();
		tongQuanPanel.setLayout(null);
		tongQuanPanel.setBorder(tongQuanBorder);
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setBorder(chatBorder);
		chatPanel.setBorder(chatBorder);
		resetPasswordPanel = new JPanel();
		resetPasswordPanel.setLayout(null);
		resetPasswordPanel.setBorder(resetPasswordBorder);
	}
	
	private void setLabel() {
		maMayLabel = new JLabel("Tên Máy");
		maMayLabel.setBounds(100, 30, 100, 50);
		
		tenTaiKhoanLabel = new JLabel("Tên Tài Khoản");
		tenTaiKhoanLabel.setBounds(100, 60, 100, 50);
		
		soTienDaNopLabel = new JLabel("Số Tiền Đã Nộp");
		soTienDaNopLabel.setBounds(100, 90, 100, 50);
		
//		soGioChoiConLaiLabel = new JLabel("Số Giờ Chơi Còn Lại");
//		soGioChoiConLaiLabel.setBounds(100, 120, 200, 50);
//		
		sendMessageLabel = new JLabel("Message:");
		userNameLabel = new JLabel("Tên Đăng Nhập");
		currentPasswordLabel = new JLabel("Mật Khẩu Hiện Tại");
		newPasswordLabel = new JLabel("Mật Khẩu Mới");
	}
	
	private void setTextField() {
		maMayTextField = new JTextField();
		maMayTextField.setText(computerName);
		maMayTextField.setEditable(false);
		maMayTextField.setBounds(250, 30, 200, 30);
		
		tenTaiKhoanTextField = new JTextField();
		tenTaiKhoanTextField.setText(userName);
		tenTaiKhoanTextField.setEditable(false);
		tenTaiKhoanTextField.setBounds(250, 60, 200, 30);
		
//		soGioChoiConLaiTextField = new JTextField();
//		soGioChoiConLaiTextField.setEditable(false);
//		soGioChoiConLaiTextField.setBounds(250, 90, 200, 30);
//		
		soTienDaNopTextField = new JTextField();
		soTienDaNopTextField.setText(Integer.toString(soTienCuaMay));
		soTienDaNopTextField.setEditable(false);
		soTienDaNopTextField.setBounds(250, 90, 200, 30);
		
		sendMessaggeField = new JTextField();
		userNameTextField = new JTextField();
	}
	
	private void setButton() {
//		logOutButton = new JButton("Đăng Xuất", logout_icon);
//		logOutButton.addActionListener(this);
//		logOutButton.setBounds(300, 180, 150, 30);
//		logOutButton.setBackground(new Color(255, 240, 245));
		
		sendMessageButton = new JButton("Send", send_icon);
		sendMessageButton.addActionListener(this);
		sendMessageButton.setBackground(new Color(255, 240, 245));
		
		connectingButton = new JButton("Connect", connect_icon);
		connectingButton.addActionListener(this);
		connectingButton.setBackground(new Color(255, 240, 245));
		
		resetPasswordButton = new JButton("Thay Đổi");
		resetPasswordButton.setBounds(340, 240, 100, 20);
		resetPasswordButton.addActionListener(this);
		resetPasswordButton.setBackground(new Color(255, 240, 245));
	}
	
	private void setBorder() {
		chatBorder = BorderFactory.createTitledBorder("Chat Với Chủ Quán");
		tongQuanBorder = BorderFactory.createTitledBorder("Tổng Quan");
		resetPasswordBorder = BorderFactory.createTitledBorder("Thiết Lập Lại Mật Khẩu");
	}
	
	private void setIcon() {
		connect_icon = new ImageIcon("res/connect_icon.png");
		send_icon = new ImageIcon("res/send_icon.png");
		logout_icon = new ImageIcon("res/logout_icon.png");
		hide_icon = new ImageIcon("res/hide_icon.png");
		view_icon = new ImageIcon("res/view_icon.png");
	}
	
	private void setCheckBox() {
		showBox_1 = new JCheckBox();
		showBox_1.setIcon(hide_icon);
		showBox_1.setSelectedIcon(view_icon);
		showBox_1.addActionListener(this);
		showBox_2 = new JCheckBox();
		showBox_2.setIcon(hide_icon);
		showBox_2.setSelectedIcon(view_icon);
		showBox_2.addActionListener(this);
		
		showBox_1.setBounds(450, 150, 100, 20);
		showBox_2.setBounds(450, 200, 100, 20);
	}
	
	private void setPasswordField() {
		oldPassword = new JPasswordField();
		newPassword = new JPasswordField();
		
		oldPassword.setBounds(240, 150, 200, 20);
		
		newPassword.setBounds(240, 200, 200, 20);
	}
	
	private void addPanelToTabbedPanel() {
		mainTabbedPane.add(tongQuanPanel, "Tổng Quan");
		mainTabbedPane.add(chatPanel, "Chat");
		mainTabbedPane.add(resetPasswordPanel, "Cài Đặt");
		
		mainFrame.add(mainTabbedPane);
	}
	
	private void addItemToTongQuanPanel() {
		tongQuanPanel.add(maMayLabel);
		tongQuanPanel.add(maMayTextField);
		tongQuanPanel.add(tenTaiKhoanLabel);
		tongQuanPanel.add(tenTaiKhoanTextField);
//		tongQuanPanel.add(soGioChoiConLaiLabel);
//		tongQuanPanel.add(soGioChoiConLaiTextField);
		tongQuanPanel.add(soTienDaNopLabel);
		tongQuanPanel.add(soTienDaNopTextField);
		
//		tongQuanPanel.add(logOutButton);
	}
	
	private void addItemToChatPanel() {
		chatPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel miniPanel = new JPanel(new GridLayout(1, 3));
		
		JPanel superMiniPanel = new JPanel(new GridLayout(1, 2)); 
		
		superMiniPanel.add(connectingButton);
		superMiniPanel.add(sendMessageLabel);
		
		miniPanel.add(superMiniPanel);
		miniPanel.add(sendMessaggeField);
		miniPanel.add(sendMessageButton);
		
		chatPanel.add(miniPanel, BorderLayout.SOUTH);
	}
	
	private void addItemToOptionPanel() {
		
		JLabel ten = new JLabel("Tên Tài Khoản:");
		ten.setBounds(100, 100, 220, 20);
		JLabel oldPass = new JLabel("Mật Khẩu Hiện Tại:");
		oldPass.setBounds(100, 150, 220, 20);
		JLabel newPass = new JLabel("Mật Khẩu Mới:");
		newPass.setBounds(100, 200, 220, 20);
		
		JTextField tenTaiKhoan = new JTextField(userName);
		tenTaiKhoan.setEditable(false);
		tenTaiKhoan.setBounds(240, 100, 200, 20);
		
		resetPasswordPanel.add(ten);
		resetPasswordPanel.add(oldPass);
		resetPasswordPanel.add(newPass);
		resetPasswordPanel.add(tenTaiKhoan);
		resetPasswordPanel.add(oldPassword);
		resetPasswordPanel.add(newPassword);
		resetPasswordPanel.add(showBox_1);
		resetPasswordPanel.add(showBox_2);
		resetPasswordPanel.add(resetPasswordButton);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == logOutButton) {
//			mainFrame.setVisible(false);
//			new MayKhachLoginForm();
//		}
		
		if(e.getSource() == sendMessageButton) {
			sendMessage();
		}
		
		if (e.getSource() == connectingButton) {
			connect();
		}
		
		if (e.getSource() == showBox_1) {
			showPassOld();
		}
		
		if (e.getSource() == showBox_2) {
			showPassNew();
		}
		
		if (e.getSource() == resetPasswordButton) {
			resetPassword();
		}
	}
	
	//Condition Functions
	
	private void connect() {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(hostName, PORT);
					
					socketLib = new SocketLib(socket, khungChatTextPane);
					
					JOptionPane.showMessageDialog(mainFrame, "Kết Nối Thành Công!");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		thread.start();
	}
	
	private void sendMessage() {
		
		String messageOut = sendMessaggeField.getText().trim();
		if (messageOut.equals("")) {
			return;
		}
		messageOut = userName + ": " + messageOut;
		khungChatTextPane.append(messageOut + "\n");
		socketLib.Send(messageOut);
		
		sendMessaggeField.setText("");
	}
	
//	private void thoiGianChoiConLai(int thoiGian) {
//		Thread thread = new Thread() {
//			@Override
//			public void run() {
//				long startTime = System.currentTimeMillis();
//				long endTime = startTime + (60000*thoiGian);
//				
//				for(int i = 0; System.currentTimeMillis() < endTime; i++) {
//					
//					try {
//						Thread.sleep(6000);
//					} catch (Exception e) {
//						
//					}
//					long timeLeft = ((endTime - System.currentTimeMillis()) / 60000) + 1;
//					
//					//ghi gia tri time left vao textfield
//				}
//				
//				bendTime = true;
//				
//			}
//		};
//		
//		thread.start();
//	}
//	
//	private void kiemTraHetGio() {
//		Thread thread = new Thread() {
//			@Override
//			public void run() {
//				while(true) {
//					if (bendTime == true) {
//						JOptionPane.showMessageDialog(mainFrame, "Hết Giờ!");
//						break;
//					}
//				}
//			}
//		};
//		
//		thread.start();
//	}
//	
	//show
	private void showPassOld() {
		if (showBox_1.isSelected()) {
			oldPassword.setEchoChar((char)0);
		}
		else {
			oldPassword.setEchoChar('*');
		}
	}
	
	private void showPassNew() {
		if (showBox_2.isSelected()) {
			newPassword.setEchoChar((char)0);
		}
		else {
			newPassword.setEchoChar('*');
		}
	}
	
	//reset
	
	private void resetPassword() {
		String getCurrentPass = new String(oldPassword.getPassword());
		String getNewPass = new String(newPassword.getPassword());
		
		int currentPassLength = getCurrentPass.length();
		int newPassLength = getNewPass.length();
		
		if ((currentPassLength <= 0) || (newPassLength <= 0)) {
			JOptionPane.showMessageDialog(mainFrame, "Không Được Để Trống!");
		}
		else if (!getCurrentPass.equals(currentPassword)) {
			JOptionPane.showMessageDialog(mainFrame, "Mật Khẩu Hiện Tại Không Chính Xác!");
		}
		else {
			RequestUser.capNhatMatKhau(userName, getNewPass);
			JOptionPane.showMessageDialog(mainFrame, "Cập Nhật Thành Công!");
		}
	}
	
}
