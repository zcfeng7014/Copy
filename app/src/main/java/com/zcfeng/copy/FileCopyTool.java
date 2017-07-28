package com.zcfeng.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Iterator;

public class FileCopyTool {
	CopyCallBack ccb;
	File from;
	File to;
	double size=0;
	double copysize;
	public static FileCopyTool create()
	{
		return new FileCopyTool();
	}
	private FileCopyTool(){}
	public FileCopyTool setFrom(File from)
	{
		this.from=from;
		return this;
	}
	public FileCopyTool setFrom(String from)
	{
		this.from=new File(from);
		System.out.println("-------------------"+from);
		return this;
	}
	public FileCopyTool setFrom(URI from)
	{
		this.from=new File(from);

		return this;
	}
	public FileCopyTool setTo(File todir)
	{
		this.to=todir;
		return this;
	}
	public FileCopyTool setTo(String to)
	{
		this.to=new File(to);
		return this;
	}
	public FileCopyTool setTo(URI to)
	{
		this.to=new File(to);
		return this;
	}
	public FileCopyTool setProgressCallBack(CopyCallBack copycallback)
	{
		this.ccb=copycallback;
		return this;
	}

	public FileCopyTool start()
	{
		if(check())
		{
			size=0;
			copysize=0;
			ccb.ShowStata(CopyStata.SCAN);
			scanFiles(from);
			ccb.showFilesSize(size);
			ccb.ShowStata(CopyStata.COPY);
			startCopy(from,to);
			ccb.OnFinish();
		}

		return this;
	}
	private void startCopy(File a, File b)
	{
		if(a.isFile())
		{
			File fb=new File(b.getAbsolutePath()+File.separator+a.getName());
			if(copy(a, fb))
			{
				check(a,fb);
			}
		}
		else
		{
			for (File temp : a.listFiles()) {
				startCopy(temp,new File(b.getAbsolutePath()+File.separator+a.getName()));
			}
		}
	}
	private void check(File fa, File fb) {
		try {

			String a=MD5Utils.getMd5ByFile(fa);
			String b=MD5Utils.getMd5ByFile(fb);
			if (a.equals(b))
			{
				ccb.ShowCheckResult(fa.getName(),true);
			}
			else
			{
				ccb.ShowCheckResult(fa.getName(),false);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ccb.ShowCheckResult(fa.getName(),false);
		}
	}
	private boolean check() {
		if(from==null)
		{
			ccb.OnError("源文件异常，请正确设置源地址");
			return false;
		}
		if(to==null){
			ccb.OnError("目标异常，请正确设置目标地址");
			return false;
		}
		if(to.isFile())
		{
			ccb.OnError("不能以文件作为目标地址");
			return false;
		}
		return true;
	}


	private void scanFiles(File file)
	{
		if(file.isDirectory())
			for (File temp : file.listFiles())
			{
				scanFiles(temp);
			}
		else
		{
			size+=file.length();
		}
	}


	//将文件c从from copy到 to
	private boolean copy(File fa,File fb)
	{
		//判断文件是否存在
		if(!fb.exists()){
			if(!fb.getParentFile().exists())
			{
				fb.getParentFile().mkdirs();
			}
			try {
				fb.createNewFile();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				ccb.OnError(fb.getName()+" 文件创建失败");
				return false;
			}
		}
		else
		{
			ccb.OnError(fb.getName()+" 已存在");
			return false;
		}
		try
		{
			byte[] b=new byte[1024*1024*32];                   //复制所使用的字节缓存数组
			FileInputStream fis=new FileInputStream(fa);
			FileOutputStream fos=new FileOutputStream(fb);
			int len;
			float copySize=0;
			while((len=fis.read(b))>0)
			{
				fos.write(b, 0, len);
				copySize+=len;
				ccb.ShowProcess(fa.getAbsolutePath(),copySize/fa.length()*100);
			}
			this.copysize+=fa.length();
			ccb.ShowTotalProcess((int)(this.copysize/this.size*100));
			fis.close();
			fos.close();
			return true;
		}
		catch (FileNotFoundException e)
		{
			ccb.OnError(""+e.getMessage());
			return false;
		}
		catch (IOException e)
		{
			ccb.OnError(""+e.getMessage());
		}
		return false;
	}
}
