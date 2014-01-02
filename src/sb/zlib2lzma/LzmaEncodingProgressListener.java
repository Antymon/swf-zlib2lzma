package sb.zlib2lzma;

import SevenZip.ICodeProgress;

public class LzmaEncodingProgressListener implements ICodeProgress
{
	long uncompressedBodySize;

	public LzmaEncodingProgressListener(long numberOfUncompressedBytes)
	{
		this.uncompressedBodySize = numberOfUncompressedBytes;
	}
	
	public void SetProgress(long numberOfProcessedBytes, long currentCompressedSize) 
	{
		printProgress((float)numberOfProcessedBytes/uncompressedBodySize);
	}
	
	public void setAsFinished()
	{
		printProgress(1f);
	}
	
	private void printProgress(float normalizedProgress)
	{
		float percentage = 100f * normalizedProgress;
		
		System.out.format("Compressed: %.2f%%%n", percentage);
		
		if(normalizedProgress == 1f)
		{
			System.out.println("Compression successfully completed! :)");
		}
	}
}
