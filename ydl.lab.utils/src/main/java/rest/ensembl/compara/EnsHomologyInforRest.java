package rest.ensembl.compara;

import analysis.AbstractAnalysisAction;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class EnsHomologyInforRest extends AbstractAnalysisAction {
	private static final Logger logger = LoggerFactory.getLogger(EnsHomologyInforRest.class);

	String geneID = "ENSG00000156076";

	@Override
	public void doIt() throws Exception {
		String server = "https://rest.ensembl.org";
		String ext = "/homology/id/human/";

//		String format = MessageFormat.format("{0}{1}{2}?", server, ext, geneID);

		// vertebrates , metazoa, protists, plants, fungi, pan_homology
		String format = MessageFormat.format("{0}{1}{2}?compara=fungi", server, ext, geneID);
		logger.info(format);

		URL url = new URL(format);

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

		FileUtils.writeStringToFile(new File(outputPath), output, StandardCharsets.US_ASCII);
	}

	public static void main(String[] args) throws Exception {
		EnsHomologyInforRest worker = new EnsHomologyInforRest();
		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\5_WIF\\comparaInfo\\ensembl.fungi.json";
		worker.setOutputPath(outPath);

		worker.doIt();
	}

}
