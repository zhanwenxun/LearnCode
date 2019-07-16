package com.jiang.learncode2.HomeModule.textwarrior.simpleC.util;

import android.content.Context;
import android.content.res.AssetManager;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IO
{
	/**
	 * 读取assets
	 * @param context
	 * @param fileName
     * @return
     */
	public static String getFromAssets(Context context, String fileName)
	{ 
		StringBuilder result=new StringBuilder();
		InputStreamReader inputReader=null;
		BufferedReader bufReader=null;
		try
		{ 
			 inputReader= new InputStreamReader(context.getResources().getAssets().open(fileName));
			 bufReader = new BufferedReader(inputReader);
			String line="";

			while ((line = bufReader.readLine()) != null)
				result.append(line + "\r");

		}
		catch (Exception e)
		{ 
			e.printStackTrace(); 
		}
		finally {
			if(inputReader!=null)
				try {
					inputReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result.toString();
    }

	/**
	 *
	 * @param context
	 * @param path
     * @return
     */
	public  static  InputStream getInputSteamFromAssets(Context context,String path)
	{
		AssetManager assetManager=context.getAssets();
		InputStream inputStream=null;
		try {
			inputStream=assetManager.open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputStream;

	}
	/**
	 * 读取文件
	 * @param file
	 * @return
     */
	public static String readFile(File file)
	{
		BufferedReader bufr = null;
		StringBuilder sb = null;
		try
		{
			bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String temp=null;
			sb = new StringBuilder();
			for (;;)
			{
				temp = bufr.readLine() ;
				if (temp != null)
					sb.append(temp + "\r\n");
				else
				{
					break;
				}
			}
		}
		catch (IOException e)
		{}
		finally
		{
			try
			{
				if (bufr != null)
					bufr.close();
			}
			catch (IOException e)
			{}
		}
		return sb.toString();
	}

	/**
	 * 写入文本文件
	 * @param file
	 * @param data
     */
	public static String writeFile(File file, String data)
	{
		BufferedWriter bw=null;
		String msg=null;
		try
		{
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(data, 0, data.length());
			bw.flush();
		}
		catch (IOException e)
		{
			msg=e.toString();
		}
		finally
		{
			try
			{
				bw.close();
			}
			catch (IOException e)
			{
				msg=e.toString();
			}
		}
		return msg;
	}

	/**
	 * markdown转成html
	 * @param text
	 * @return
     */
	public static String md2html(String text)
	{

		ByteArrayInputStream bis=null;
		ByteArrayOutputStream bos = null;
		InputStreamReader isr=null;
		OutputStreamWriter osw = null;
		String str=null;
		try
		{
			bis = new ByteArrayInputStream(text.getBytes());
			bos = new ByteArrayOutputStream();
			isr = new InputStreamReader(bis);
			osw = new OutputStreamWriter(bos);
			Markdown md=new Markdown();
			md.transform(isr, osw);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		try
		{
			isr.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			osw.close();
		}
		catch (IOException e)
		{e.printStackTrace();}

		str = bos.toString();


		return str;
	}
}
