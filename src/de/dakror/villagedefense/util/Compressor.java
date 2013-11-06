package de.dakror.villagedefense.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compressor
{
	public static void compressFile(File f, String s)
	{
		compressFile(f, (s + ((s.length() < 18) ? "                 " : "")).getBytes());
	}
	
	public static void compressFile(File f, byte[] input)
	{
		setFileContent(f, compress(input));
	}
	
	public static byte[] compress(byte[] b)
	{
		byte[] length = ByteBuffer.allocate(4).putInt(b.length).array();
		byte[] buffer = new byte[b.length];
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
		deflater.setInput(b);
		deflater.finish();
		int len = deflater.deflate(buffer);
		byte[] compr = Arrays.copyOf(buffer, len);
		byte[] output = new byte[compr.length + 4];
		System.arraycopy(length, 0, output, 0, length.length);
		System.arraycopy(compr, 0, output, 4, compr.length);
		
		return output;
	}
	
	public static byte[] decompress(byte[] b)
	{
		try
		{
			int length = ByteBuffer.wrap(Arrays.copyOf(b, 4)).getInt();
			Inflater inflater = new Inflater();
			inflater.setInput(Arrays.copyOfRange(b, 4, b.length));
			byte[] buf = new byte[length];
			inflater.inflate(buf);
			inflater.end();
			return buf;
		}
		catch (DataFormatException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String decompressFile(File f)
	{
		byte[] decompressed = decompress(getFileContentAsByteArray(f));
		String text = new String(decompressed);
		return text;
	}
	
	public static void setFileContent(File f, byte[] b)
	{
		try
		{
			f.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(b);
			fos.close();
		}
		catch (Exception e)
		{}
	}
	
	public static byte[] getFileContentAsByteArray(File f)
	{
		try
		{
			byte[] fileData = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(fileData);
			dis.close();
			return fileData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] compressRow(byte[] b)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte active = b[0];
			byte same = (byte) -127;
			for (int i = 1; i < b.length; i++)
			{
				if (b[i] == active && same < 127) same += 1;
				else
				{
					baos.write(new byte[] { same, active });
					same = -127;
					active = b[i];
				}
			}
			
			baos.write(new byte[] { same, active });
			
			return baos.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] decompressRow(byte[] b)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < b.length; i += 2)
		{
			for (int j = 0; j < b[i] + 128; j++)
			{
				baos.write(b[i + 1]);
			}
		}
		return baos.toByteArray();
	}
}
