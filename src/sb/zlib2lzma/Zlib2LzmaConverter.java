package sb.zlib2lzma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import SevenZip.Compression.LZMA.Encoder;

public class Zlib2LzmaConverter
{
	private final int NON_LZMA_HEADER_SIZE = 8;
	private final int LZMA_HEADER_SIZE = 12;
	private final int LZMA_PROPS_SIZE = 5;
	
	private final char COMPRESSED_LZMA = 'Z';
	private final char COMPRESSED_ZLIB = 'C';
	private final char UNCOMPRESSED = 'F';

	private boolean verbose;
	private String fileName;
	
	public Zlib2LzmaConverter(String inputPath, String outputPath, boolean verbose)
	{	
		this.verbose = verbose;
		
		BasicFileIO basicFileIO = new BasicFileIO();
		
		this.fileName = basicFileIO.getFileNameFromPath(inputPath);
		
		byte[] inputSwfBytes = basicFileIO.readFile(inputPath);
		
		byte[] outputSwfBytes = this.convert(inputSwfBytes);
		
		if(outputSwfBytes != null)
		{
			basicFileIO.writeFile(outputPath, outputSwfBytes);
		}
	}
	
	private byte[] convert(byte[] inputSwfBytes)
	{
		if (isSwfValid(inputSwfBytes))
		{
			byte[] outputSwfBytes;
			
			if (isSwfZlibCompressed(inputSwfBytes))
			{
				outputSwfBytes = uncompressZlibCompressedSwf(inputSwfBytes);
				outputSwfBytes = createLzmaCompressedSwf(outputSwfBytes);
			}
			else
			{
				outputSwfBytes = createLzmaCompressedSwf(inputSwfBytes);
			}

			return outputSwfBytes;
		}
		
		return null;
	}

	/*
	 * Format of SWF when LZMA is used:
	 * 
	 * | 4 bytes | 4 bytes | 4 bytes | 5 bytes | n bytes | 6 bytes | |
	 * 'ZWS'+version | scriptLen | compressedLen | LZMA props | LZMA data | LZMA
	 * end marker |
	 * 
	 * scriptLen is the uncompressed length of the SWF data. Includes 4 bytes
	 * SWF header and 4 bytes for scriptLen itself.
	 * 
	 * compressedLen does not include header (4+4+4 bytes) or lzma props (5
	 * bytes) compressedLen does include LZMA end marker (6 bytes);
	 */

	private byte[] createLzmaCompressedSwf(final byte[] uncompressedSwfBytes)
	{
		Encoder lzmaEncoder = new Encoder();

		lzmaEncoder.SetLcLpPb(3, 0, 2);
		lzmaEncoder.SetDictionarySize(1 << 24);
		lzmaEncoder.SetNumFastBytes(128);
		lzmaEncoder.SetEndMarkerMode(true);

		final int uncompressedBodySize = uncompressedSwfBytes.length - NON_LZMA_HEADER_SIZE;
		
		ByteArrayInputStream uncompressedBodyInputStream = new ByteArrayInputStream(uncompressedSwfBytes, NON_LZMA_HEADER_SIZE, uncompressedBodySize);
		ByteArrayOutputStream lzmaBodyAndMarkerOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream lzmaPropsOutputStream = new ByteArrayOutputStream(LZMA_PROPS_SIZE);

		try
		{
			final LzmaEncodingProgressListener lzmaEncodingProgressListener = new LzmaEncodingProgressListener(uncompressedBodySize, fileName, verbose);
			lzmaEncoder.Code(uncompressedBodyInputStream, lzmaBodyAndMarkerOutputStream, uncompressedBodySize, 0, lzmaEncodingProgressListener);
			lzmaEncoder.WriteCoderProperties(lzmaPropsOutputStream);
			lzmaEncodingProgressListener.setAsFinished();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		final int lzmaHeaderAndPropsSize = LZMA_HEADER_SIZE + LZMA_PROPS_SIZE;
		final int lzmaBodyAndMarkerSize = lzmaBodyAndMarkerOutputStream.size();
		
		byte[] outputSwf = new byte[lzmaHeaderAndPropsSize + lzmaBodyAndMarkerSize];

		System.arraycopy(uncompressedSwfBytes, 0, outputSwf, 0, NON_LZMA_HEADER_SIZE);

		outputSwf[8] = (byte) (lzmaBodyAndMarkerSize & 0xFF);
		outputSwf[9] = (byte) ((lzmaBodyAndMarkerSize >> 8) & 0xFF);
		outputSwf[10] = (byte) ((lzmaBodyAndMarkerSize >> 16) & 0xFF);
		outputSwf[11] = (byte) ((lzmaBodyAndMarkerSize >> 24) & 0xFF);

		System.arraycopy(lzmaPropsOutputStream.toByteArray(), 0, outputSwf, LZMA_HEADER_SIZE, LZMA_PROPS_SIZE);
		System.arraycopy(lzmaBodyAndMarkerOutputStream.toByteArray(), 0, outputSwf, lzmaHeaderAndPropsSize, lzmaBodyAndMarkerSize);

		outputSwf[0] = COMPRESSED_LZMA; // altering byte indicating compression type;
		
		return outputSwf;
	}

	public byte[] uncompressZlibCompressedSwf(byte[] zlibSwfBytes)
	{
		Inflater zlibDecoder = new Inflater();
		zlibDecoder.setInput(zlibSwfBytes, NON_LZMA_HEADER_SIZE, zlibSwfBytes.length - NON_LZMA_HEADER_SIZE);

		ByteArrayOutputStream uncompressedBodyOutputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		
		while (!zlibDecoder.finished())
		{
			try
			{
				int count = zlibDecoder.inflate(buffer);
				uncompressedBodyOutputStream.write(buffer, 0, count);
			}
			catch (DataFormatException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			uncompressedBodyOutputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		byte[] outputSwf = new byte[NON_LZMA_HEADER_SIZE + uncompressedBodyOutputStream.size()];
		
		System.arraycopy(zlibSwfBytes, 0, outputSwf, 0, NON_LZMA_HEADER_SIZE);
		System.arraycopy(uncompressedBodyOutputStream.toByteArray(), 0, outputSwf, NON_LZMA_HEADER_SIZE, uncompressedBodyOutputStream.size());
		
		outputSwf[0] = UNCOMPRESSED;

		return outputSwf;
	}

	private boolean isSwfZlibCompressed(byte[] swfBytes)
	{
		return swfBytes[0] == COMPRESSED_ZLIB;
	}

	private boolean isSwfValid(byte[] swfBytes)
	{
		if (swfBytes != null)
		{
			if (swfBytes[1] != 'W' || swfBytes[2] != 'S')
			{
				System.err.println("not a SWF file.");
			}
			else if (swfBytes[3] < 13)
			{
				System.err.println("only SWF version 13 or higher is supported.");
			}
			else if (swfBytes[0] == COMPRESSED_LZMA)
			{
				System.err.println("already LZMA compressed");
			}
			else if (swfBytes[0] == UNCOMPRESSED || isSwfZlibCompressed(swfBytes))
			{
				return true;
			}
			else
			{
				System.err.println("not a swf, unrecognized compression or malformed file");
			}
		}

		return false;
	}
}
