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
			// 使 ServerAgentThread 中的 run() 反复调用
			// 不断读取 aCotrun 和 pcCotrun 中的 UTF 数据流
			// 再根据 DBUtil 中的函数返回相应的 UTF 或 Boolean 数据流
			while (true) {
				// 这里的 socket 就是从 Android 端传来的 socket
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
