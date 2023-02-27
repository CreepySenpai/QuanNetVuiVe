package sqlstatments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connect.Connecting;

public class UserInformation {
	public static String getMoney(String userName){
		String info = null;
		Connection connection = Connecting.getConnection();
		
		PreparedStatement preparedStatement = null;
		
		try {
			
			String getInfo = "SELECT TIENNAP FROM KHACHHANG WHERE USERNAME = ?";
			
			preparedStatement = connection.prepareStatement(getInfo);
			
			preparedStatement.setString(1, userName);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				info = resultSet.getString("TIENNAP");
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		Connecting.closeConnection(connection);
		Connecting.closeStament(preparedStatement);
		
		return info;
	}
}
