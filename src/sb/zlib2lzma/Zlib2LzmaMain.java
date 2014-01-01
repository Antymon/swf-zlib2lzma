package sb.zlib2lzma;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Zlib2LzmaMain
{
	public static void main(String[] args)
	{
		Options options = new Options();

		options.addOption("o", "output", true, "Output LZMA compressed swf file");
		options.addOption("i", "input", true, "Input zlib compressed or uncompressed swf file");

		CommandLineParser parser = new BasicParser();

		try
		{
			CommandLine line = parser.parse(options, args);

			if (isCLIInputValid(line))
			{
				new Zlib2LzmaConverter(line.getOptionValue("input"), line.getOptionValue("output"));
			}
		}
		catch (ParseException exp)
		{
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
	}

	private static boolean isCLIInputValid(CommandLine line)
	{
		if (!line.hasOption("output"))
		{
			System.err.println("Output path was not provided");
		}
		else if (!line.hasOption("input"))
		{
			System.err.println("Input path was not provided");
		}
		else if (line.getArgs().length > 0)
		{
			System.err.println("Expected two io parameters only. Got " + line.getArgs().length + " unrecognized arguments.");
		}
		else
		{
			return true;
		}

		return false;
	}
}
