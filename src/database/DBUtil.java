package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.Constant;

public class DBUtil {
	
//	连接数据库
	public static Connection getConnection() {
		Connection con = null;
		try {
			String url;
			// 本机
//			url = "jdbc:mysql://localhost:3306/scotrun?useSSL=false&serverTimezone=UTC";
			// 腾讯云
//			url = "jdbc:mysql://cdb-ah23ypr7.gz.tencentcdb.com:10022/scotrun?useSSL=false&serverTimezone=UTC";
			// 腾讯云2
//			url = "jdbc:mysql://sh-cdb-f39noah4.sql.tencentcdb.com:61692/scotrun?useSSL=false&serverTimezone=UTC";
			// 阿里云
			url = "jdbc:mysql://rm-bp1963290wwbi7tleio.mysql.rds.aliyuncs.com:3306/scotrun?useSSL=false&serverTimezone=UTC";
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, "root", "Cxg19990707");
		} catch (Exception e) { e.printStackTrace(); }
		return con;
	}
	
	// 判断是否是用户
	public static Boolean isUser(String info) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String[] str = info.split("<#>");
		try {
			con = getConnection();
			st = con.createStatement();
			String sql = "select * from user where uid='" + str[0]
					+ "' and pwd='" + str[1] + "';";
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

	// 判断是否是管理员
	public static Boolean isManager(String info) {
		Connection con=null;							
		Statement st = null;
		ResultSet rs = null;
		String[] str = info.split("<#>");
		try {
			con = getConnection();
			st = con.createStatement();
			String sql = "select * from manager where uid='" + str[0]
					+ "' and pwd='" + str[1] + "';";
			rs = st.executeQuery(sql);
			while (rs.next()) return true;
		} catch (Exception e) {e.printStackTrace();} 
		finally {
			try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return false;
	}
	
	// 注册
	public static Boolean register(String name, String password,
		String avatar, String sex) {
		Connection con = getConnection();
		Statement st = null;
		try {
			st = con.createStatement();
			String sql = "insert into user(uid,pwd,avatar,sex) values('"
					+ name + "','" + password + "','" + avatar + "','" + sex
					+ "')";
			System.out.println("sql " + sql);
			int v = st.executeUpdate(sql);
			if (v > 0) {
				sql = "insert into attention(uid) values('" + name + "')";
				System.out.println("sql");
				st.executeUpdate(sql);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return false;
	}
	
	// 获取个人的各项信息
	public static String getUidMessage(String info) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {
			st = con.createStatement();
			String sql = "select uid,pwd,avatar from user where uid='"
					+ info + "'";
			rs = st.executeQuery(sql);
			if (rs.next()) {
				for (int i = 1; i <= 9; i++) {
					sb.append(rs.getString(i) + "<#>");
				}
			}
			if (sb.length() > 0)
				return sb.substring(0, sb.length() - 3).toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return Constant.NO_MESSAGE;
	}

}
