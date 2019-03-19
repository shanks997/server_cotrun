package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import database.DBUtil;
import util.Constant;

public class ServerAgentThread extends Thread {
	
	Socket sk;
	DataInputStream dis;
	DataOutputStream dos;
	Boolean flag;
	
	public ServerAgentThread(Socket sk) {
		this.sk = sk;
	}
	
	public void run() {
		try {
			dis = new DataInputStream(sk.getInputStream());
			dos = new DataOutputStream(sk.getOutputStream());
			// 从 aCotrun 和 pcCotrun 的 dos.writeUTF() 读取 UTF 数据流
			String msg = dis.readUTF();
			System.out.println("msg " + msg);
			
			if (msg.startsWith(Constant.IS_USER)) { // 是否是用户
				// 传给 DBUtil.isUser 的参数为 name + "<#>" + password
				flag = DBUtil.isUser(msg.substring(
						Constant.IS_USER.length(), msg.length()));
				dos.writeBoolean(flag);
			} else if (msg.startsWith(Constant.IS_MANAGER)) { // 是否是管理员
				flag = DBUtil.isManager(msg.substring(
						Constant.IS_MANAGER.length(), msg.length()));
				dos.writeBoolean(flag);
			} else if (msg.startsWith(Constant.INIT_INFO)) { // 初始化信息
				String str = "进入 PC 端登陆界面";
				dos.writeUTF(str);
			}
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			try { dis.close(); } catch (Exception e) { e.printStackTrace(); }
			try { dos.flush(); } catch (Exception e) { e.printStackTrace(); }
			try { sk.close(); } catch (Exception e) { e.printStackTrace(); }
		}
	}

}
