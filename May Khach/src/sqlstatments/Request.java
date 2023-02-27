package sqlstatments;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connect.Connecting;

public class Request {
//	public static ArrayList<String> getDanhSachMayOffline(){
//		ArrayList<String> danhSach = new ArrayList<String>();
//		
//		Connection connection = Connecting.getConnection();
//		
//		Statement statement = null;
//		
//		try {
//			
//			String sql = "SELECT COMPUTERNAME FROM COMPUTER WHERE TINHTRANG = /'Offline/'";
//			
//			statement = connection.createStatement();
//			
//			ResultSet resultSet = statement.executeQuery(sql);
//			
//			while(resultSet.next()) {
//				danhSach.add(resultSet.getString("COMPUTERNAME"));
//			}
//			
//		} catch (Exception e) {
//			e.getMessage();
//		}
//		
//		Connecting.closeConnection(connection);
//		Connecting.closeStament(statement);
//		
//		return danhSach;
//	}
}
