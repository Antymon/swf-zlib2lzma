package sb.zlib2lzma;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

public class BasicFileIO
{
	public String getFileNameFromPath(String path)
	{
		File file = new File(path);
		
		return file.getName();
	}
	
	public byte[] readFile(String inputPath)
	{
		try
		{
			File inputSwf = new File(inputPath);
			FileInputStream swfFileInputStream = new FileInputStream(inputSwf);
            ByteArrayOutputStream swfBytesOutputStream = new ByteArrayOutputStream();
            
            int inputSwfTempByte = swfFileInputStream.read();

            while (inputSwfTempByte != -1)
            {
            	swfBytesOutputStream.write(inputSwfTempByte);
            	inputSwfTempByte = swfFileInputStream.read();
            }

            swfFileInputStream.close();
            swfBytesOutputStream.close();
            
            return swfBytesOutputStream.toByteArray();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public void writeFile(String outputPath, byte[] allBytes)
	{
		try
		{
			File outputSwf = new File(outputPath);
 
			outputSwf.createNewFile();
			
			FileOutputStream swfFileOutputStream = new FileOutputStream(outputSwf);

			BufferedOutputStream swfOutputStream = new BufferedOutputStream(swfFileOutputStream);

			swfOutputStream.write(allBytes);
			
			swfOutputStream.close();
			swfFileOutputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
