package utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static void main(String[] args) {
		System.out.println(DBConnection.connectDB());
	}
	
	public static Connection connectDB() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_management_system", "root", "");
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			conn = null;
		}
		return conn;
	}
}
