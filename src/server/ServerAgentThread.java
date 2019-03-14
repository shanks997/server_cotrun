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
	byte[] bb;
	Boolean flag;
	
	public ServerAgentThread(Socket sk) {
		this.sk = sk;
	}
	
	public void run() {
		try {
			dis = new DataInputStream(sk.getInputStream());
			dos = new DataOutputStream(sk.getOutputStream());
			String msg = dis.readUTF();
			System.out.println("msg " + msg);
			if (msg.startsWith(Constant.IS_USER)) {
				flag = DBUtil.isUser(msg.substring(
						Constant.IS_USER.length(), msg.length()));
				dos.writeBoolean(flag);
			}
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			try { dis.close(); } catch (Exception e) { e.printStackTrace(); }
			try { dos.close(); } catch (Exception e) { e.printStackTrace(); }
			try { sk.close(); } catch (Exception e) { e.printStackTrace(); }
		}
	}

}
