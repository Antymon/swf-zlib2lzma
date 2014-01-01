package sb.zlib2lzma;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BasicFileIO
{
	public byte[] readFile(String inputPath)
	{
		try
		{
			Path path = Paths.get(new URI(inputPath));
			byte[] allBytes = Files.readAllBytes(path);

			return allBytes;
		}
		catch (URISyntaxException e1)
		{
			e1.printStackTrace();
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
			Path path = Paths.get(new URI(outputPath));
			Files.write(path, allBytes);
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
		catch (URISyntaxException e1)
		{

			e1.printStackTrace();
		}
	}

}
