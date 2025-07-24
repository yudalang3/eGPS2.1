package module.fastatools.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import fasta.io.FastaReader;
import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import analysis.AbstractAnalysisAction;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.MyFrame;

public class FastaConcatenator extends AbstractAnalysisAction {

	private List<String> inputFiles;
	int totalSequence = 0;
	int keptSequence = 0;
	private List<String> excludeStrings;

	private List<String> outputs = Lists.newArrayList();
	private MyFrame instanceFrame;
	private ComputationalModuleFace computationalModuleFace;

	@Override
	public void doIt() throws Exception {
		List<File> files = Lists.newArrayList();

		for (String filePath : inputFiles) {
			File inputFile = new File(filePath);
			if (inputFile.isDirectory()) {
				File[] listFiles = inputFile.listFiles();

				for (File file : listFiles) {
					if (file.isDirectory()) {
						continue;
					}
					files.add(file);
				}
			} else {
				files.add(inputFile);
			}
		}

		if (excludeStrings != null && !excludeStrings.isEmpty()) {
			Iterator<File> iterator = files.iterator();
			while (iterator.hasNext()) {
				File next = iterator.next();
				String extension = FilenameUtils.getExtension(next.getName());
				boolean exclude = false;
				for (String str : excludeStrings) {
					if (Objects.equal(extension, str)) {
						exclude = true;
					}
				}
				if (exclude) {
					iterator.remove();
				}
			}
		}


		BufferedWriter bufferedOutputStream = new BufferedWriter(new FileWriter(outputPath), 8192 * 100);

		MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();

		int index = 1;
		int size = files.size();
		StringBuilder stringBuilder = new StringBuilder();
		for (File file : files) {
			processOneFile(bufferedOutputStream, file);


			stringBuilder.setLength(0);
			stringBuilder.append("Current file is ");
			stringBuilder.append(index);
			stringBuilder.append(", total is ");
			stringBuilder.append(size);

			if (computationalModuleFace != null) {
				instanceFrame.onlyRefreshButtomStatesBar(computationalModuleFace, stringBuilder.toString(), 100);
			}

			index++;

		}

		bufferedOutputStream.close();

		outputs.clear();
		outputs.add("Total sequence is: " + totalSequence);

	}


	private void outputOneSequence(BufferedWriter bufferedOutputStream, String name, String seq) {
		try {
			bufferedOutputStream.write(">");
			bufferedOutputStream.write(name);
			bufferedOutputStream.write("\n");
			bufferedOutputStream.write(seq);
			bufferedOutputStream.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void processOneFile(BufferedWriter bufferedOutputStream, File file) throws IOException {
		FastaReader.readAndProcessFastaPerEntry(file.getAbsolutePath(), (name, seq) -> {
			outputOneSequence(bufferedOutputStream, name, seq);
			totalSequence++;
			return false;
		});

	}

	public void setInputFiles(List<String> inputFiles) {
		this.inputFiles = inputFiles;
	}


	public void setExcludeStrings(List<String> excludeStrings) {
		this.excludeStrings = excludeStrings;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public void setModuleFace(ComputationalModuleFace computationalModuleFace) {
		this.computationalModuleFace = computationalModuleFace;
		this.instanceFrame = UnifiedAccessPoint.getInstanceFrame();
	}

}
