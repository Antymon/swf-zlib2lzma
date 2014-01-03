package sb.zlib2lzma;

import SevenZip.ICodeProgress;

public class LzmaEncodingProgressListener implements ICodeProgress
{
	long uncompressedBodySize;
	boolean verbose;
	String fileName;
	
	float normalizedCounter = 0.1f;

	public LzmaEncodingProgressListener(long numberOfUncompressedBytes, String fileName, boolean verbose)
	{
		this.fileName = fileName;
		this.uncompressedBodySize = numberOfUncompressedBytes;
		this.verbose = verbose;
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
		
		if(verbose)
		{
			System.out.format(fileName + " compression progress: %.2f%%%n", percentage);
		}
		
		if(normalizedProgress == 1f)
		{
			System.out.println(fileName + " successfully compressed! :)");
		}
	}
}
