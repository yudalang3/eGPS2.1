package module.benchensdownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import tsv.io.KitTable;
import tsv.io.TSVReader;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.MyFrame;

/**
 * 使用写一个新的模块Tab Name： URL List Downloader<br>
 * Description：Automatic download the files in URL list with progress indicator.
 * 创新点：为用户创建一个方便下载的小工具，拥有指示条，可以估计还需要多少时间。
 * 还需要改装的地方：需要openStream的时候获取整个文件的大小，然后更具已经给出的指示条计算当前进度。问题在于有些服务器它不会返回文件有多大。所以还得增加一个判定。
 */
public class URLDirectDownloader {

	private File outputDir = new File(
			"C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\DB\\protein\\Ensembl");

	private File inputURLsFile = new File(
			"C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\DB\\SpeciesColl\\Ensembl_taxon\\20240514\\species_protein_download.tsv");

	private int startIndexOneBased = 8;

	private boolean hasHeaderLine = false;

	private MyFrame instanceFrame;

	public URLDirectDownloader() {
		boolean guIlaucnched = UnifiedAccessPoint.isGULaunched();
		if (guIlaucnched) {
			instanceFrame = UnifiedAccessPoint.getInstanceFrame();
		}
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public void setInputURLsFile(File inputURLsFile) {
		this.inputURLsFile = inputURLsFile;
	}

	public void setStartIndexOneBased(int startIndexOneBased) {
		this.startIndexOneBased = startIndexOneBased;
	}

	public void setHasHeaderLine(boolean hasHeaderLine) {
		this.hasHeaderLine = hasHeaderLine;
	}

	public static void main(String[] args) throws IOException {
		new URLDirectDownloader().run();
	}

	private void run() throws IOException {

		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		KitTable tsvTextFile = TSVReader.readTsvTextFile(inputURLsFile.getAbsolutePath(), hasHeaderLine);

		List<List<String>> contents = tsvTextFile.getContents();
		int size = contents.size();

		int curr = 0;
		// 第13个下完了
		for (List<String> list : contents) {
			curr++;

			if (curr < startIndexOneBased) {
				continue;
			}
			String downloadURL = list.get(0);
			downloadContent(downloadURL);
			System.out.printf("Current is %d, total is %d\n", curr, size);
		}

	}

	public String downloadContent(String urlStr) throws IOException {
		return downloadContent(null, urlStr);
	}

	public String downloadContent(ComputationalModuleFace computationalModuleFace, String urlString)
			throws IOException {

		///////////////////////////////////////////////////////////

		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 获取文件大小
		long totalFileSize = connection.getContentLengthLong();
		long downloadedSize = 0;

		// 创建输出流以保存文件
		String fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
		FileOutputStream fos = new FileOutputStream(new File(outputDir, fileName));

		// 创建输入流以读取文件
		InputStream is = connection.getInputStream();

		byte[] buffer = new byte[409600000];
		int bytesRead;

		StringBuilder stringBuilder = new StringBuilder();
		while ((bytesRead = is.read(buffer)) != -1) {
			fos.write(buffer, 0, bytesRead);
			downloadedSize += bytesRead;

			if (computationalModuleFace != null && instanceFrame != null) {
				stringBuilder.setLength(0);
				stringBuilder.append(downloadedSize).append(" / ");
				stringBuilder.append(totalFileSize).append(" = ");
				double percentage = (double) downloadedSize / totalFileSize * 100;
				stringBuilder.append((int) percentage);
				stringBuilder.append(" %");
				instanceFrame.onlyRefreshButtomStatesBar(computationalModuleFace, stringBuilder.toString(), 100);
			}
		}

		fos.close();
		is.close();
		connection.disconnect();

		return fileName;

	}

}
