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
		float percentage = Math.round(1000f * normalizedProgress)/10f;
		
		System.out.println("Compressed: " + percentage + "%");
		
		if(normalizedProgress == 1f)
		{
			System.out.println("Compression successfully completed :)");
		}
	}
}
