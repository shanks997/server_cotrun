package util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
	
	public static void saveByte(byte[] data,String path) throws IOException{//�����ļ�		
		File file = new File(path);							//�����ļ�
		FileOutputStream fos = new FileOutputStream(file);	//��Fileʵ�����������
		fos.write(data);				//��ʵ������д��������
		fos.flush();					//��ջ���������
		fos.close();					//�ر��ļ���
	}
	//��ȡͼƬ����
	public static byte[] streamTobyte(DataInputStream din)
	{
		byte[]  data=null;						//����ͼƬ��������
		//�����µĻ����������ָ��������Ϊ4096Byte
		ByteArrayOutputStream out= new ByteArrayOutputStream(4096); 
		try 
		{
			int length=0,temRev =0,size;		//����int����
			length=din.readInt();				//��ȡ����������
			byte[] buf=new byte[length-temRev];	//����byte����
			while ((size = din.read(buf))!=-1)	//�������ݶ���
			{ 	
				temRev+=size;					//��¼���泤��
				out.write(buf, 0, size);		//д��С��size�ı�������
				if(temRev>=length)				//�������ĳ��ȴ����ļ����ܳ�
				{
						break;					//��ֹд��
				}
				buf = new byte[length-temRev];	//д���������
			}
			data=out.toByteArray();  			//��ͼƬ��Ϣ�Ա���������ʽ��������ֵ��ͼƬ��������
			out.close();						//�ر������
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;							//���ر�������
	}

}
