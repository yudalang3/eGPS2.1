package module.batchpepo.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import fasta.io.FastaReader;

/**
 * 动机： 发现有蛋白质序列是一模一样的，所以在计算同源基因的时候要注意一下。发现人类的参考基因组 GRCH38有scaffold竟然和染色体一模一样。
 * 奇怪为什么会有这些东西
 */
public class RemoveRedundantPeps {

	static class PepEntry implements Comparable<PepEntry> {
		static StringBuilder sBuilder = new StringBuilder();
		String pepName;
		String assembly;
		String chromosome;
		String start;
		String end;
		String strand;
		String geneName;
		String transcript;

		@Override
		public int compareTo(PepEntry o) {
			int ret = this.chromosome.length() - o.chromosome.length();
			if (ret == 0) {
				ret = this.chromosome.compareTo(o.chromosome);
			}
			if (ret == 0) {
				ret = this.start.compareTo(o.start);
			}
			
			return ret;
		}

		@Override
		public String toString() {
			sBuilder.setLength(0);
			sBuilder.append(geneName);
			sBuilder.append("\t");
			sBuilder.append(pepName);
			sBuilder.append("\t");
			sBuilder.append(transcript);
			sBuilder.append("\t");
			sBuilder.append(assembly);
			sBuilder.append("\t");
			sBuilder.append(chromosome);
			sBuilder.append("\t");
			sBuilder.append(start);
			sBuilder.append("\t");
			sBuilder.append(end);
			sBuilder.append("\t");
			sBuilder.append(strand);

			return sBuilder.toString();
		}

		public static String getHeaderLine() {
			sBuilder.setLength(0);
			sBuilder.append("geneName");
			sBuilder.append("\t");
			sBuilder.append("pepName");
			sBuilder.append("\t");
			sBuilder.append("transcript");
			sBuilder.append("\t");
			sBuilder.append("assembly");
			sBuilder.append("\t");
			sBuilder.append("chromosome");
			sBuilder.append("\t");
			sBuilder.append("start");
			sBuilder.append("\t");
			sBuilder.append("end");
			sBuilder.append("\t");
			sBuilder.append("strand");

			return sBuilder.toString();
		}

	}

	private File inputFastaPath;
	private File outputFastaPath;
	private BufferedWriter writer4info;
	private BufferedWriter writer4intraSpeciesSamePep;

	private BufferedWriter bufferedWriter;

	public static void main(String[] args) throws IOException {
		// 创建一个 Stopwatch 实例
		Stopwatch stopwatch = Stopwatch.createStarted();

		new RemoveRedundantPeps().run();

		// 停止 Stopwatch 并获取经过的时间
		stopwatch.stop();

		// 打印经过的时间
		System.out.println("执行耗时: " + stopwatch.elapsed(TimeUnit.SECONDS) + " 秒");

	}

	public void setInputFastaPath(File file) {
		this.inputFastaPath = file;
	}

	public void setOutputFastaPath(File file) {
		this.outputFastaPath = file;
	}

	public void setWriter4info(BufferedWriter writer4info) {
		this.writer4info = writer4info;
	}

	public void setWriter4intraSpeciesSamePep(BufferedWriter writer4intraSpeciesSamePep) {
		this.writer4intraSpeciesSamePep = writer4intraSpeciesSamePep;
	}

	public void run() throws IOException {

		if (inputFastaPath == null || outputFastaPath == null || writer4info == null
				|| writer4intraSpeciesSamePep == null) {
			throw new InputMismatchException("Please set the correct properties.");
		}

		bufferedWriter = EGPSFileUtil.getBufferedWriterFromFileMaybeCompressed(outputFastaPath.getAbsolutePath());

		Map<String, List<PepEntry>> ret = Maps.newLinkedHashMap();

		FastaReader.readAndProcessFastaPerEntry(inputFastaPath.getAbsolutePath(), (name, seq) -> {
			String[] splits = EGPSStringUtil.split(name, ' ');

			// 前五个是一样的
			// ENSAPOP00000010247.1 pep
			// primary_assembly:ASM210954v1:MVNR01007420.1:817:873:1
			// gene:ENSAPOG00000012658.1 transcript:ENSAPOT00000000162.1
			List<PepEntry> list = ret.get(seq);

			PepEntry pepEntry = new PepEntry();
			pepEntry.pepName = splits[0];
			pepEntry.geneName = splits[3];
			pepEntry.transcript = splits[4];
			String thirdStr = splits[2];
			splits = EGPSStringUtil.split(thirdStr, ':');
			pepEntry.assembly = splits[1];
			pepEntry.chromosome = splits[2];
			pepEntry.start = splits[3];
			pepEntry.end = splits[4];
			pepEntry.strand = splits[5];

			try {
				writer4info.write(pepEntry.toString());
				writer4info.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (list == null) {
				ArrayList<PepEntry> value2 = new ArrayList<>();
				value2.add(pepEntry);
				ret.put(seq, value2);
			} else {
				list.add(pepEntry);
			}

			return false;
		});

		for (Entry<String, List<PepEntry>> entry : ret.entrySet()) {
			List<PepEntry> value = entry.getValue();
			if (value.size() > 1) {
				Collections.sort(value);
				Iterator<PepEntry> iterator = value.iterator();
				writer4intraSpeciesSamePep.write(iterator.next().pepName);
				while (iterator.hasNext()) {
					writer4intraSpeciesSamePep.write("\t");
					writer4intraSpeciesSamePep.write(iterator.next().pepName);
				}
				writer4intraSpeciesSamePep.write("\n");
			}

			// output to fasta file
			bufferedWriter.write(">");
			bufferedWriter.write(value.get(0).pepName);
			bufferedWriter.write("\n");
			bufferedWriter.write(entry.getKey());
			bufferedWriter.write("\n");
		}

		bufferedWriter.close();
	}

	private void printResults(String currAssemblyName, Map<String, List<PepEntry>> ret) throws IOException {
		Set<Entry<String, List<PepEntry>>> entrySet = ret.entrySet();
		if (entrySet.size() > 0) {
			bufferedWriter.write("----------This is assembly: " + currAssemblyName);
			bufferedWriter.write("\n");
		}

		for (Entry<String, List<PepEntry>> entry : entrySet) {
			List<PepEntry> value = entry.getValue();
			if (value.size() > 1) {
				bufferedWriter
						.write("---------------------------------------------------------------------------------\n");
				for (PepEntry string : value) {
					bufferedWriter.write(string.toString());
					bufferedWriter.write("\n");
				}
			}
		}
	}
}
