package rest.ensembl.proteins;

import analysis.AbstractAnalysisAction;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.string.EGPSStringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;

/**
 *
 * How to use:
 * set the input file path: a tsv file, if you just want to query one protein, then directly invoke the restGetProteinDomains.doOnceRest(protID);
 * set the header and the columnIndex
 * set the output file path. Note: it is a dir.
 *
 * @author yudal
 */
public class RestGetProteinDomains extends AbstractAnalysisAction {
	
	private static final Logger logger = LoggerFactory.getLogger(RestGetProteinDomains.class);

	private MessageFormat formatter = new MessageFormat("/overlap/translation/{0}?");
	private String server = "https://rest.ensembl.org";
	
	/**
	 * 0-based index
	 */
	private int columnIndex = 8;
	private boolean hasHeader = true;

	@Override
	public void doIt() throws Exception {
		List<String> allLines = Files.readLines(new File(inputPath), StandardCharsets.UTF_8);
		if (hasHeader) {
			allLines = allLines.subList(1, allLines.size());
		}
		for (String line : allLines) {
			String[] split = EGPSStringUtil.split(line, '\t', columnIndex + 1);
			String string = split[columnIndex];
			
			logger.info("Start to do the Rest request...");
			doOnceRest(string);
			logger.info("Finish the request process.");
			sleepRandomly();
		}

	}
	
	private void sleepRandomly() {
	    try {
	        Thread.sleep(getMillis());
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	        throw new RuntimeException(e);
	    }
	}

	private long getMillis() {
	    return (long) (Math.random() * 1_000 + 3_000);
	}

	public void doOnceRest(String protID) throws Exception {
		String ext = formatter.format( new Object[]{protID});
		logger.info(ext);
		URL url = new URL(server + ext);

		URLConnection connection = url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) connection;

		httpConnection.setRequestProperty("Content-Type", "application/json");

		InputStream response = connection.getInputStream();
		int responseCode = httpConnection.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
		}

		String output;
		Reader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			output = builder.toString();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}

		File dir = new File(outputPath);
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		Files.write(output.getBytes(StandardCharsets.US_ASCII), new File(dir, protID.concat(".json")));
	}
	
	
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}
}
