package gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import sqlstatments.RequestUser;

public class MayKhachLoginForm implements ActionListener{

	private JFrame loginFrame;
	
	private JButton loginButton;
	
	private JLabel userNameLabel, passwordLabel, computerNameLabel;
	
	private JTextField userNameTextField;
	
	private JPasswordField passwordField;
	
	private JList<String> danhSachMay;
	
	private DefaultListModel<String> danhSachMayModel;
	
	private ArrayList<String> danhSachMayOffline;
	
	private JScrollPane scrollPane;
	
	private JCheckBox showPasswordBox;
	
	private ImageIcon hide_icon, view_icon, login_icon;
	
	private final int WIDTH = 600;
	private final int HEIGHT = 700;
	
	
	public MayKhachLoginForm() {
		loginFrame = new JFrame();
		danhSachMayModel = new DefaultListModel<String>();
		danhSachMayOffline = new ArrayList<String>();
		hide_icon = new ImageIcon("res/hide_icon.png");
		view_icon = new ImageIcon("res/view_icon.png");
		login_icon = new ImageIcon("res/login_icon.png");
		
		//set
		setFrame();
		setLabel();
		setTextField();
		setButton();
		setList();
		setScrollPane();
		
		//add
		addLabel();
		addTextField();
		addButton();
		addList();
		
		
		//func
		
		//pack
		loginFrame.pack();
	}
	
	private void setFrame() {
		loginFrame.setTitle("Đăng Nhập");
		loginFrame.setSize(WIDTH, HEIGHT);
		loginFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2); //Xuat hien giua man hinh
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLayout(null);
		loginFrame.setIconImage(login_icon.getImage());
	}
	
	private void setLabel() {
		userNameLabel = new JLabel("Tên Đăng Nhập");
		userNameLabel.setBounds(50, 100, 100, 25);
		passwordLabel = new JLabel("Mật Khẩu");
		passwordLabel.setBounds(50, 150, 100, 25);
		computerNameLabel = new JLabel("Chọn Máy");
		computerNameLabel.setBounds(50, 30, 100, 25);
	}
	
	private void setTextField() {
		userNameTextField = new JTextField();
		userNameTextField.setBounds(150, 100, 200, 25);
		passwordField = new JPasswordField();
		passwordField.setBounds(150, 150, 200, 25);
	}
	
	private void setButton() {
		loginButton = new JButton("Đăng Nhập");
		loginButton.addActionListener(this);
		loginButton.setBounds(250, 190, 100, 25);
		loginButton.setFocusable(false);
		
		showPasswordBox = new JCheckBox();
		showPasswordBox.addActionListener(this);
		showPasswordBox.setIcon(hide_icon);
		showPasswordBox.setSelectedIcon(view_icon);
		showPasswordBox.setFocusable(false);
		showPasswordBox.setBounds(350, 150, 200, 25);
	}
	
	private void setList() {
		showListComputerOffline();
		danhSachMay = new JList<String>(danhSachMayModel);
		danhSachMay.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		danhSachMay.setSelectionBackground(new Color(204, 255, 255));
		danhSachMay.setLayoutOrientation(JList.VERTICAL);
	}
	
	private void setScrollPane() {
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(danhSachMay);
		scrollPane.setBounds(150, 30, 200, 40);
	}
	
	private void addLabel() {
		loginFrame.add(computerNameLabel);
		loginFrame.add(userNameLabel);
		loginFrame.add(passwordLabel);
	}
	
	private void addTextField() {
		loginFrame.add(userNameTextField);
		loginFrame.add(passwordField);
	}
	
	private void addButton() {
		loginFrame.add(loginButton);
		loginFrame.add(showPasswordBox);
	}
	
	private void addList() {
		loginFrame.add(scrollPane);
	}
	
	private void showListComputerOffline() {
		danhSachMayOffline = RequestUser.getComputerOffline();
		danhSachMayModel.add(0, "__Chọn Máy__");
		for(String computer : danhSachMayOffline) {
			danhSachMayModel.addElement(computer);
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			signIn();
		}
		if (e.getSource() == showPasswordBox) {
			if (showPasswordBox.isSelected()) {
				passwordField.setEchoChar((char)0);
			}
			else {
				passwordField.setEchoChar('*');
			}
		}
	}
	
	private void signIn() {
		String userName = userNameTextField.getText();
		int userNameLength = userName.length();
		String password = new String(passwordField.getPassword());
		int passwordLength = password.length();
		
		if((userNameLength == 0) || (passwordLength == 0)) {
			JOptionPane.showMessageDialog(loginFrame, "Không Được Để Trống");
		}
		else {
			if(RequestUser.isUsers(userName, password)) {
				if (RequestUser.getSoTienConLai(userName) <= 0) {
					JOptionPane.showMessageDialog(loginFrame, "Không Đủ Tiền Chơi!");
				}
				else {
					JOptionPane.showMessageDialog(loginFrame, "Đăng Nhập Thành Công");
					loginFrame.setVisible(false);
					String computerName = getComputerName();
					int tongSoTien = RequestUser.getTongSoTien(userName);
					RequestUser.setComputerOnline(computerName, userName);
					new MainForm(computerName, userName, password, tongSoTien);
				}
			}
			else {
				JOptionPane.showMessageDialog(loginFrame, "Đăng Nhập Thất Bại");
			}
		}
	}
	
//	private String checkNull(String str) {
//		String hehe = "";
//		if (str != "") {
//			hehe = str;
//		}
//		else {
//			JOptionPane.showMessageDialog(loginFrame, "Something Wrong!");
//		}
//		return hehe;
//	}
	
	private String getComputerName() {
		String computerName = "";
		if (checkSelect() == false) {
			JOptionPane.showMessageDialog(loginFrame, "Vui Lòng Chọn Máy");
		}
		else {
			computerName = danhSachMay.getSelectedValue();
		}
		return computerName;
	}
	
	private boolean checkSelect() {
		boolean select = false;
		
		if (danhSachMay.getSelectedValue() != "__Chọn Máy__") {
			select = true;
		}
		
		return select;
	}
	
	public static void main(String[] args) {
		new MayKhachLoginForm();
	}
}
