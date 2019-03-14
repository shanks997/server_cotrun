package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	
//	�������ݿ�
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/scotrun?useSSL=false&serverTimezone=UTC",
					"root", "Cxg19990707"
					);
		} catch (Exception e) { e.printStackTrace(); }
		return con;
	}
	
//	�ж��Ƿ����û�
	public static Boolean isUser(String info) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String[] str = info.split("<#>");
		try {
			con = getConnection();
			st = con.createStatement();
			String sql = "select * from user where uid=" + str[0]
					+ " and pwd=" + str[1] + ";";
			rs = st.executeQuery(sql);
			while (rs.next()) return true;
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return false;
	}
	
}
