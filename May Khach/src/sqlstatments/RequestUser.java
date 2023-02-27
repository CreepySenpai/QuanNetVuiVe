package sqlstatments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connect.Connecting;

public class RequestUser {
	public static boolean isUsers(String userName, String password) {
		boolean isAdmin = false;
		Connection connection = Connecting.getConnection();
		
		Statement statement = null;
		
		try {
			String findAdminSQL = "SELECT USERNAME, PASSWORD FROM KHACHHANG";
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(findAdminSQL);
			
			while(resultSet.next()) {
				if(resultSet.getString("USERNAME").equals(userName) && resultSet.getString("PASSWORD").equals(password)) {
					isAdmin = true;
				}
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(statement);
		
		return isAdmin;
	}
	
	public static ArrayList<String> getComputerOffline(){
		ArrayList<String> danhSach = new ArrayList<String>();
		
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String offline = "Offline";
			
			String sqlString = "SELECT COMPUTERNAME FROM COMPUTER WHERE TINHTRANG = ?";
			
			preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, offline);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				danhSach.add(resultSet.getString("COMPUTERNAME"));
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		return danhSach;
	}
	
	public static void setComputerOnline(String computerName, String userName) {
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		String tinhTrang = "Online";
		
		try {
			
			String setComputer = "UPDATE COMPUTER SET TINHTRANG = ?, MAKHACHHANGDANGCHOI = ? WHERE COMPUTERNAME = ?";
			
			preparedStatement = connection.prepareStatement(setComputer);
			
			preparedStatement.setString(1, tinhTrang);
			preparedStatement.setString(2, userName);
			preparedStatement.setString(3, computerName);
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
	}
	
	public static void setComputerOffline(String computerName) {
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		String tinhTrang = "Offline";
		
		String newUserName = "";
		
		try {
			
			String setComputer = "UPDATE COMPUTER SET TINHTRANG = ?, MAKHACHHANGDANGCHOI = ? WHERE COMPUTERNAME = ?";
			
			preparedStatement = connection.prepareStatement(setComputer);
			
			preparedStatement.setString(1, tinhTrang);
			preparedStatement.setString(2, newUserName);
			preparedStatement.setString(3, computerName);
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
	}
	
	public static int getSoTienConLai(String userName) {
		int soTienConLai = 0;
		
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String getThoiGian = "SELECT TIENNAP FROM KHACHHANG WHERE USERNAME = ?";
			
			preparedStatement = connection.prepareStatement(getThoiGian);
			preparedStatement.setString(1, userName);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				soTienConLai = resultSet.getInt("TIENNAP");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		
		return soTienConLai;
	}
	
	public static int getTongSoTien(String userName) {
		int tongSoTien = 0;
		
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String getThoiGian = "SELECT TIENNAP FROM KHACHHANG WHERE USERNAME = ?";
			
			preparedStatement = connection.prepareStatement(getThoiGian);
			preparedStatement.setString(1, userName);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				tongSoTien = resultSet.getInt("TIENNAP");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		
		return tongSoTien;
	}
	
	public static int soTienCuaMay(String computerName) {
		int sotTienCuaMay = 0;
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String sql = "SELECT TIEN FROM COMPUTER WHERE COMPUTERNAME = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, computerName);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			sotTienCuaMay = resultSet.getInt("TIEN");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		return sotTienCuaMay;
	}
	
	public static void capNhatMatKhau(String userName, String newPass) {
		Connection connection = Connecting.getConnection();
		PreparedStatement preparedStatement = null;
		
		try {
			
			String sql = "UPDATE KHACHHANG SET PASSWORD = ? WHERE USERNAME = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newPass);
			preparedStatement.setString(2, userName);
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
	}
	
}
