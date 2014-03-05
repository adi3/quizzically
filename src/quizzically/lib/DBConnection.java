package quizzically.lib;

import java.sql.*;
import quizzically.config.MyDBInfo;

public class DBConnection {
	
	private Connection con;
	
	public DBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		 
		con = DriverManager.getConnection("jdbc:mysql://" + MyDBInfo.MYSQL_DATABASE_SERVER,
				 											MyDBInfo.MYSQL_USERNAME,
				 											MyDBInfo.MYSQL_PASSWORD);
	}
	
	public Statement getStatement() throws SQLException {
		Statement stmt = con.createStatement();
		stmt.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
		return stmt;
	}
	
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
