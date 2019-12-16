package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import util.Thumbanils;

import util.ImageUtil;

import database.DBUtil;
import util.Constant;

public class ServerAgentThread extends Thread {
	
	Socket sk;
	DataInputStream dis;
	DataOutputStream dos;
	Boolean flag;
	String picPath;
	File file;
	FileInputStream fis;
	byte[] bb;
	String thumbnailpath;
	
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
			
			// 是否是用户
			if (msg.startsWith(Constant.IS_USER)) {
				// 传给 DBUtil.isUser 的参数为 name + "<#>" + password
				flag = DBUtil.isUser(msg.substring(
						Constant.IS_USER.length(), msg.length()));
				dos.writeBoolean(flag);
			} // 是否是管理员
			else if (msg.startsWith(Constant.IS_MANAGER)) {
				flag = DBUtil.isManager(msg.substring(
						Constant.IS_MANAGER.length(), msg.length()));
				dos.writeBoolean(flag);
			} // 初始化信息
			else if (msg.startsWith(Constant.INIT_INFO)) {
				String str = "进入 PC 端登陆界面";
				dos.writeUTF(str);
			} // 注册
			else if (msg.startsWith(Constant.REGIST)) {
				String info = msg.substring(Constant.REGIST.length(),
						msg.length());
				String name = info.split("<#>")[0];
				String passward = info.split("<#>")[1];
				String avatar = info.split("<#>")[2];
				String sex = info.split("<#>")[3];
				flag = DBUtil.register(name, passward, avatar, sex);
				dos.writeBoolean(flag);

			} // 按编号获取图片 android pc
			else if (msg.startsWith(Constant.GET_IMAGE)) {
				String remsg = msg.substring(Constant.GET_IMAGE.length(),
						msg.length());
				File fileResource = new File("resource"); // 创建文件流
				picPath = fileResource.getAbsolutePath() + "/IMAGE/"; // 文件路径
				file = new File(picPath + remsg);
				System.out.println("file "+file.getAbsolutePath());

				fis = new FileInputStream(file);
				byte[] bb = new byte[fis.available()];
				fis.read(bb);
				dos.writeInt(bb.length);
				dos.write(bb);
				fis.close();
			} // 按名称获取图片 缩略图 pc
			else if (msg.startsWith(Constant.GET_THUMBNAIL)) {
				String picName = msg.substring(Constant.GET_THUMBNAIL.length(),
						msg.length());
				File fileResource = new File("resource"); // 创建文件流
				picPath = fileResource.getAbsolutePath() + "/IMAGE/"; // 文件路径
				thumbnailpath = fileResource.getAbsolutePath()
						+ "\\IMAGE\\thumbnail\\";
				file = new File(thumbnailpath + picName);
				if (!file.exists()) {
					Thumbanils.saveImageAsJpg(picPath + picName, thumbnailpath
							+ picName, 200, 200);
					file = new File(thumbnailpath + picName);
				}
				fis = new FileInputStream(file);
				byte[] bb = new byte[fis.available()];
				fis.read(bb);
				dos.writeInt(bb.length);
				dos.write(bb);
				fis.close();
			} // 插入图片 android gck
			else if (msg.startsWith(Constant.INSERT_PIC)) {
				bb = ImageUtil.streamTobyte(dis);
				File fileResource = new File("resource"); // 创建文件流
				picPath = fileResource.getAbsolutePath() + "/IMAGE/"; // 文件路径
				File parentFile = new File(picPath);
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				String picName = System.currentTimeMillis() + ".jpeg";
				ImageUtil.saveByte(bb, picPath + picName);
				dos.writeUTF(picName);
			} // 获取个人的各项信息
			else if (msg.startsWith(Constant.GET_UID_MESSAGE)) {
				String mes = msg.substring(Constant.GET_UID_MESSAGE.length(),
						msg.length());
				String rmes = DBUtil.getUidMessage(mes);
				dos.writeUTF(rmes);
			}
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			try { dis.close(); } catch (Exception e) { e.printStackTrace(); }
			try { dos.flush(); } catch (Exception e) { e.printStackTrace(); }
			try { sk.close(); } catch (Exception e) { e.printStackTrace(); }
		}
	}

}
