package util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
	
	public static void saveByte(byte[] data,String path) throws IOException{//保存文件		
		File file = new File(path);							//创建文件
		FileOutputStream fos = new FileOutputStream(file);	//将File实例放入输出流
		fos.write(data);				//将实例数据写入输入流
		fos.flush();					//清空缓存区数据
		fos.close();					//关闭文件流
	}
	//读取图片数据
	public static byte[] streamTobyte(DataInputStream din)
	{
		byte[]  data=null;						//声明图片比特数组
		//创建新的缓冲输出流，指定缓存区为4096Byte
		ByteArrayOutputStream out= new ByteArrayOutputStream(4096); 
		try 
		{
			int length=0,temRev =0,size;		//定义int类型
			length=din.readInt();				//获取输入流长度
			byte[] buf=new byte[length-temRev];	//定义byte数组
			while ((size = din.read(buf))!=-1)	//若有内容读出
			{ 	
				temRev+=size;					//记录缓存长度
				out.write(buf, 0, size);		//写入小于size的比特数组
				if(temRev>=length)				//如果缓存的长度大于文件的总长
				{
						break;					//终止写入
				}
				buf = new byte[length-temRev];	//写入比特数组
			}
			data=out.toByteArray();  			//将图片信息以比特数组形式读出并赋值给图片比特数组
			out.close();						//关闭输出流
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;							//返回比特数组
	}

}
