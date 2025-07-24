package module.gff3opr.myscript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import analysis.AbstractAnalysisAction;
import module.genome.GenomicRange;
import module.gff3opr.model.GFF3Feature;
import module.gff3opr.model.YuGFF3Parser;

public class GetRefFlatFile extends AbstractAnalysisAction implements Predicate<GFF3Feature> {

	BufferedWriter br;

	private Set<String> hashset = new HashSet<>();

	@Override
	public void doIt() throws Exception {
		br = new BufferedWriter(new FileWriter(getOutputPath()));

		YuGFF3Parser yuGFF3Parser = new YuGFF3Parser();

		yuGFF3Parser.parseGff3LineByLine(inputPath, this);

		br.close();

//		System.out.println("All types are:");
//		for (String string : hashset) {
//			System.out.println(string);
//		}
	}

	@Override
	public boolean test(GFF3Feature f) {
//		if (!f.hasAttribute("Parent")) {
//			hashset.add(f.type());
//		}

		if (Objects.equals("gene", f.type()) || Objects.equals("ncRNA_gene", f.type())) {

			try {
				br.write(f.seqname());
				br.write("\t");
				GenomicRange location = f.location();

				br.write(String.valueOf(location.bioStart()));
				br.write("\t");
				br.write(String.valueOf(location.bioEnd()));
				br.write("\t");
				br.write(String.valueOf(location.strand));
				br.write("\t");
				String ID = f.getAttribute("ID");
				String name = f.getAttribute("Name");
				br.write(ID);
				br.write("\t");
				if (name == null) {
					br.write("NovelTranscript");
				} else {
					br.write(name);
				}
				br.write("\t");
				br.write(f.type());
				br.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: program input.file output.file");
			return;
		}
		String inputFile = args[0];
		String outputFile = args[1];
		GetRefFlatFile getRefFlatFile = new GetRefFlatFile();
		getRefFlatFile.setInputPath(inputFile);
		getRefFlatFile.setOutputPath(outputFile);

		getRefFlatFile.doIt();
	}

}
