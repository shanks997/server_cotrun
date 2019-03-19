package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
	
	ServerSocket ss;
	int port;
	
	public ServerThread(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			ss = new ServerSocket(port);
			System.out.println("start on " + port);
			// ʹ ServerAgentThread �е� run() ��������
			// ���϶�ȡ aCotrun �� pcCotrun �е� UTF ������
			// �ٸ��� DBUtil �еĺ���������Ӧ�� UTF �� Boolean ������
			while (true) {
				// ����� socket ���Ǵ� Android �˴����� socket
				Socket sk = ss.accept();
				new ServerAgentThread(sk).start();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void main (String args[]) {
		new ServerThread(9997).start();
		new ServerThread(9998).start();
		new ServerThread(9999).start();
	}
	
}
