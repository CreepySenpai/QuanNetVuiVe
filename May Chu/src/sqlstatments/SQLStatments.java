package sqlstatments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connect.Connecting;
import something.Computers;
import something.Online;
import something.Users;

public class SQLStatments {
	public static void nopTien(String userName, int money) {
		
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement_1 = null;
		PreparedStatement preparedStatement_2 = null;
		
		try {
			
			String nopTienString = "SELECT TIENNAP, TONGSOTIENDANAP FROM KHACHHANG WHERE USERNAME = ?";
			preparedStatement_1 = connection.prepareStatement(nopTienString);
			preparedStatement_1.setString(1, userName);
			
			ResultSet resultSet = preparedStatement_1.executeQuery();
			
			int newMoney = 0;
			int newTotalMoney = 0;
			while(resultSet.next()) {
				newMoney = resultSet.getInt("TIENNAP") + money;
				newTotalMoney = resultSet.getInt("TONGSOTIENDANAP") + money;
			}
			
			String updateMoney = "UPDATE KHACHHANG SET TIENNAP = ?, TONGSOTIENDANAP = ? WHERE USERNAME = ?";
			
			preparedStatement_2 = connection.prepareStatement(updateMoney);
			preparedStatement_2.setInt(1, newMoney);
			preparedStatement_2.setInt(2, newTotalMoney);
			preparedStatement_2.setString(3, userName);
			
			preparedStatement_2.executeUpdate();
			
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement_1);
		Connecting.closeStament(preparedStatement_2);
	}
	
	public static ArrayList<Computers> getDanhSachMay(){
		ArrayList<Computers> danhSachMay = new ArrayList<Computers>();
		
		Connection connection = Connecting.getConnection();
		
		Statement statement = null;
		
		try {
			String sql = "SELECT * FROM COMPUTER";
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()) {
				danhSachMay.add(new Computers(resultSet.getString("COMPUTERNAME"), resultSet.getString("TINHTRANG"), resultSet.getString("LOAIMAY"), resultSet.getInt("TIEN")));
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(statement);
		return danhSachMay;
	}
	
	public static ArrayList<Users> getDanhSachTaiKhoan(){
		ArrayList<Users> danhSachTaiKhoan = new ArrayList<Users>();
		
		Connection connection = Connecting.getConnection();
		
		Statement statement = null;
		
		try {
			String sql = "SELECT USERNAME, TIENNAP FROM KHACHHANG";
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()) {
				danhSachTaiKhoan.add(new Users(resultSet.getString("USERNAME"), resultSet.getInt("TIENNAP")));
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(statement);
		return danhSachTaiKhoan;
	}
	
	public static ArrayList<Online> getDanhSachMayVaNguoiChoiOnline(){
		ArrayList<Online> danhSachDangOnline = new ArrayList<Online>();
		
		Connection connection = Connecting.getConnection();
		
		Statement statement = null;
		
		try {
			
			String sql = "SELECT COMPUTERNAME, USERNAME, TONGSOTIENDANAP FROM COMPUTER, KHACHHANG WHERE TINHTRANG = 'Online' AND MAKHACHHANGDANGCHOI = USERNAME";
			
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()) {
				danhSachDangOnline.add(new Online(resultSet.getString("COMPUTERNAME"), resultSet.getString("USERNAME"), resultSet.getInt("TONGSOTIENDANAP")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return danhSachDangOnline;
	}
	
	public static boolean checkTenMay(String computerName){
		boolean tonTai = false;
		Connection connection = Connecting.getConnection();
		Statement statement = null;
		
		try {
			String sqlString = "SELECT COMPUTERNAME FROM COMPUTER";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlString);
			while(resultSet.next()) {
				String name = resultSet.getString("COMPUTERNAME");
				if (name.equals(computerName)) {
					tonTai = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Connecting.closeConnection(connection);
		Connecting.closeStament(statement);
		return tonTai;
	}
	
	public static boolean checkTenTaiKhoan(String userName) {
		boolean tonTai = false;
		Connection connection = Connecting.getConnection();
		Statement statement = null;
		
		try {
			String sqlString = "SELECT USERNAME FROM KHACHHANG";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlString);
			while(resultSet.next()) {
				String name = resultSet.getString("USERNAME");
				if(name.equals(userName)) {
					tonTai = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(statement);
		return tonTai;
	}
	
	public static void themMay(String computerName, String loaiMay, int money) {
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String themSQL = "INSERT INTO COMPUTER(COMPUTERNAME, LOAIMAY, TIEN) VALUES(?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(themSQL);
			
			preparedStatement.setString(1, computerName);
			preparedStatement.setString(2, loaiMay);
			preparedStatement.setInt(3, money);
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);;
		Connecting.closeStament(preparedStatement);
	}
	
	public static void themTaiKhoan(String userName, String password) {
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String themSQL = "INSERT INTO KHACHHANG(USERNAME, PASSWORD) VALUES(?, ?)";
			preparedStatement = connection.prepareStatement(themSQL);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
	}
	
	public static ArrayList<Users> findUser(String userName) {
		ArrayList<Users> user = new ArrayList<Users>();
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			String findSQL = "SELECT USERNAME, TONGSOTIENDANAP FROM KHACHHANG WHERE USERNAME LIKE ?";
			preparedStatement = connection.prepareStatement(findSQL);
			
			preparedStatement.setString(1, "%" + userName + "%");
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				user.add(new Users(resultSet.getString("USERNAME"), resultSet.getInt("TONGSOTIENDANAP")));
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		
		return user;
	}
	
	public static ArrayList<Computers> findComputer(String computerName){
		ArrayList<Computers> computer = new ArrayList<Computers>();
		
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String findSQL = "SELECT COMPUTERNAME, TINHTRANG, LOAIMAY, TIEN FROM COMPUTER WHERE COMPUTERNAME LIKE ?";
			
			preparedStatement = connection.prepareStatement(findSQL);
			
			preparedStatement.setString(1, "%" + computerName + "%");
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				computer.add(new Computers(resultSet.getString("COMPUTERNAME"), resultSet.getString("TINHTRANG"), resultSet.getString("LOAIMAY"), resultSet.getInt("TIEN")));
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		
		return computer;
	}
	
	public static void updateComputer(String oldComputerName, String newComputerName, String computerType, int money) {
		
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String updateSQL = "UPDATE COMPUTER SET COMPUTERNAME = ?, LOAIMAY = ?, TIEN = ? WHERE COMPUTERNAME = ?";
			
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, newComputerName);
			preparedStatement.setString(2, computerType);
			preparedStatement.setInt(3, money);
			preparedStatement.setString(4, oldComputerName);
			preparedStatement.execute();
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
	}
	
	public static void DeleteComputer(String computerName) {
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String delete = "DELETE FROM COMPUTER WHERE COMPUTERNAME = ?";
			
			preparedStatement = connection.prepareStatement(delete);
			preparedStatement.setString(1, computerName);
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
	}
	
	public static void DeleteUser(String userName) {
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String delete = "DELETE FROM KHACHHANG WHERE USERNAME = ?";
			
			preparedStatement = connection.prepareStatement(delete);
			
			preparedStatement.setString(1, userName);
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
	}
}
