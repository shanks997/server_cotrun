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
			// �� aCotrun �� pcCotrun �� dos.writeUTF() ��ȡ UTF ������
			String msg = dis.readUTF();
			System.out.println("msg " + msg);
			
			if (msg.startsWith(Constant.IS_USER)) { // �Ƿ����û�
				// ���� DBUtil.isUser �Ĳ���Ϊ name + "<#>" + password
				flag = DBUtil.isUser(msg.substring(
						Constant.IS_USER.length(), msg.length()));
				dos.writeBoolean(flag);
			} else if (msg.startsWith(Constant.IS_MANAGER)) { // �Ƿ��ǹ���Ա
				flag = DBUtil.isManager(msg.substring(
						Constant.IS_MANAGER.length(), msg.length()));
				dos.writeBoolean(flag);
			} else if (msg.startsWith(Constant.INIT_INFO)) { // ��ʼ����Ϣ
				String str = "���� PC �˵�½����";
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
