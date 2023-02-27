package sqlstatments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import connect.Connecting;

public class RequestAdmin {
	public static boolean isAdmin(String userName, String password) {
		boolean isAdmin = false;
		Connection connection = Connecting.getConnection();
		
		Statement statement = null;
		
		try {
			String findAdminSQL = "SELECT USERNAME, PASSWORD FROM ADMIN";
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
	
	public static String getNameAdminPassword(String userName) {
		String passWord = "";
		
		
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			String getAdminPasswordSQL = "SELECT PASSWORD FROM ADMIN WHERE USERNAME = ?";
			
			preparedStatement = connection.prepareStatement(getAdminPasswordSQL);
			
			preparedStatement.setString(1, userName);
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		
		return passWord;
	}
}
