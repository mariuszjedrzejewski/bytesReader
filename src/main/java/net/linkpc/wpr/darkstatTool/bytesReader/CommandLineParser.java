package net.linkpc.wpr.darkstatTool.bytesReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineParser {

	private String[] args;

	public CommandLineParser(String[] args) {
		this.args = args;
	}

	public CommandLine parse() {

		DefaultParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();

		Options options = new Options();

		Option url = new Option("u", "url", true, "url");
		url.setRequired(true);
		options.addOption(url);

		Option ip = new Option("i", "ip", true, "ip address");
		ip.setRequired(true);
		ip.setArgs(Option.UNLIMITED_VALUES);
		ip.setValueSeparator(',');
		options.addOption(ip);

		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			formatter.printHelp("darkstat-bytes-reader", options);

			return null;
		}

		return cmd;
	}

}
