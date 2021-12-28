package net.linkpc.wpr.darkstatTool.bytesReader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BytesReader {

	public static void main(String[] args) {
		CommandLineParser clp = new CommandLineParser(args);
		CommandLine cmd = clp.parse();

		if (null == cmd)
			System.exit(1);
		
		StringBuilder sb = new StringBuilder();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("# yyyy-MM-dd HH:mm:ss");

		sb.append(simpleDateFormat.format(new Date())).append(System.getProperty("line.separator"));
		sb.append("ip;in;out;total").append(System.getProperty("line.separator"));

		String url = cmd.getOptionValue("url");
		if (!url.endsWith("/"))
			url = url.concat("/");

		String[] ipList = cmd.getOptionValues("ip");

		for (String ip : ipList) {
			String urlToFetch = url.concat(ip).concat("/");

			// System.err.println("Getting info from url: " + urlToFetch);

			Document doc = null;
			try {
				doc = Jsoup.connect(urlToFetch).get();
			} catch (IOException e) {
				sb.append(getLine(ip, "0", "0", "0", ";"));
			}
			
			if (null == doc)
				continue;

			// String title = doc.title();
			// System.err.println(title);

			Elements links = doc.select("p");
			for (Element e : links) {
				if (e.hasText() && e.text().startsWith("In: ")) {
					// System.err.println(e.text());
					Pattern pattern = Pattern.compile("In: (.*) Out: (.*) Total: (.*)");
					Matcher matcher = pattern.matcher(e.text());

					if (matcher.find()) {
						sb.append(getLine(
								ip,
								matcher.group(1).replace(",", ""),
								matcher.group(2).replace(",", ""),
								matcher.group(3).replace(",", ""),
								";")
						);
					}
				}
			}
		}
		
		System.out.println(sb.toString());
	}

	private static String getLine(String ip, String in, String out, String total, String separator) {
		StringBuilder sb = new StringBuilder();
		sb.append(ip).append(separator);
		sb.append(in).append(separator);
		sb.append(out).append(separator);
		sb.append(total);
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

}
