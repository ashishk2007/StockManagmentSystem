package com.masai.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBUtils {
	static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		ResourceBundle rs = ResourceBundle.getBundle("dbdetails");
		return DriverManager.getConnection(rs.getString("url"), rs.getString("username"), rs.getString("password"));
	}
	
	static void  closeConnection(Connection con) throws SQLException {
		if(con != null) {
			con.close();
		}
	}
	
	static boolean isResulSetEmpty(ResultSet set) throws SQLException {
		if(!set.isBeforeFirst() && set.getRow() == 0) 
			return true;
		return false;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection con = getConnection();
		System.out.println("ok");
		closeConnection(con);
	}
}
