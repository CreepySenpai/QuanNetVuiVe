package gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sqlstatments.RequestAdmin;

public class MayChuLoginForm implements ActionListener{
	
	private String adminName;
	
	private JFrame loginFrame;
	
	private JButton loginButton;
	
	private JLabel userNameLabel, passwordLabel;
	
	private JTextField userNameTextField;
	
	private JPasswordField passwordField;
	
	private JCheckBox showPasswordBox;
	
	private ImageIcon hide_icon, view_icon, login_icon;
	
	private final int WIDTH = 600;
	private final int HEIGHT = 700;
	
	public MayChuLoginForm() {
		loginFrame = new JFrame();
		hide_icon = new ImageIcon("res/hide_icon.png");
		view_icon = new ImageIcon("res/view_icon.png");
		login_icon = new ImageIcon("res/login_icon.png");
		
		//set
		setFrame();
		setLabel();
		setTextField();
		setButton();
		
		//add
		addLabel();
		addTextField();
		addButton();
		
		//pack
		loginFrame.pack();
	}
	
	private void setFrame() {
		loginFrame.setTitle("Đăng Nhập");
		loginFrame.setSize(WIDTH, HEIGHT);
		loginFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
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
	
	private void addLabel() {
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			signIn();
		}
		if (e.getSource() == showPasswordBox) {
			if (showPasswordBox.isSelected()) {
				passwordField.setEchoChar((char)0);
			}else {
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
			if(RequestAdmin.isAdmin(userName, password) == true) {
				JOptionPane.showMessageDialog(loginFrame, "Đăng Nhập Thành Công");
				loginFrame.setVisible(false);
				new MainForm(userName, password);
			}
			else {
				JOptionPane.showMessageDialog(loginFrame, "Đăng Nhập Thất Bại");
			}
		}
	}
	
	
	public static void main(String[] args) {
		new MayChuLoginForm();
	}
}
