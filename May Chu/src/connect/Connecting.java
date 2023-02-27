package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connecting {

	public static Connection getConnection() {
		
		Connection connection= null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionURL = "jdbc:sqlserver:// \\ :1433;encrypt=true;databaseName=\ ;integratedSecurity=true;trustServerCertificate=true";
			
			final String USER_NAME = " ";
			final String PASSWORD = " ";
			connection = DriverManager.getConnection(connectionURL, USER_NAME, PASSWORD);
			
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage() +"\n" + e.getClass() + "\n" + e.getCause());
		}
		  catch (SQLException e) {
			e.getMessage();
		}
		
		return connection;
	}
	
	public static void closeConnection(Connection connection) {
		try {
			if(!(connection == null)) connection.close();
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	
	public static void closeStament(Statement statement) {
		if (!(statement == null))
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}
