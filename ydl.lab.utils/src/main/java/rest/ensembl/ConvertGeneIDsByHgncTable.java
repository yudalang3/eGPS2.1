package rest.ensembl;

import analysis.AbstractAnalysisAction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableInt;
import tsv.io.KitTable;
import tsv.io.TSVReader;
import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * index: 0 = hgnc_id
index: 1 = symbol
index: 2 = name
index: 3 = locus_group
index: 4 = locus_type
index: 5 = status
index: 6 = location
index: 7 = location_sortable
index: 8 = alias_symbol
index: 9 = alias_name
index: 10 = prev_symbol
index: 11 = prev_name
index: 12 = gene_group
index: 13 = gene_group_id
index: 14 = date_approved_reserved
index: 15 = date_symbol_changed
index: 16 = date_name_changed
index: 17 = date_modified
index: 18 = entrez_id
index: 19 = ensembl_gene_id
index: 20 = vega_id
index: 21 = ucsc_id
index: 22 = ena
index: 23 = refseq_accession
index: 24 = ccds_id
index: 25 = uniprot_ids
index: 26 = pubmed_id
index: 27 = mgd_id
index: 28 = rgd_id
index: 29 = lsdb
index: 30 = cosmic
index: 31 = omim_id
index: 32 = mirbase
index: 33 = homeodb
index: 34 = snornabase
index: 35 = bioparadigms_slc
index: 36 = orphanet
index: 37 = pseudogene.org
index: 38 = horde_id
index: 39 = merops
index: 40 = imgt
index: 41 = iuphar
index: 42 = kznf_gene_catalog
index: 43 = mamit-trnadb
index: 44 = cd
index: 45 = lncrnadb
index: 46 = enzyme_id
index: 47 = intermediate_filament_db
index: 48 = rna_central_ids
index: 49 = lncipedia
index: 50 = gtrnadb
index: 51 = agr
index: 52 = mane_select
index: 53 = gencc
 * </pre>
 * 
 */
public class ConvertGeneIDsByHgncTable extends AbstractAnalysisAction {

	private String hgncFile;
	int indexOfColumn4convert = 0;

	public static void main(String[] args) throws Exception {
		String hgncTableFile = args[0];
		String inputFile = args[1];
		final int indexOfColumn4convert = 0;
		String outputFile = args[2];
		ConvertGeneIDsByHgncTable readHGNCgeneTable = new ConvertGeneIDsByHgncTable();
		readHGNCgeneTable.setInputPath(inputFile);
		readHGNCgeneTable.setOutputPath(outputFile);
		readHGNCgeneTable.setIndexOfColumn4convert(indexOfColumn4convert);
		readHGNCgeneTable.setHgncFile(hgncTableFile);
		readHGNCgeneTable.doIt();
	}

	public void setHgncFile(String hgncFile) {
		this.hgncFile = hgncFile;
	}

	public void setIndexOfColumn4convert(int indexOfColumn4convert) {
		this.indexOfColumn4convert = indexOfColumn4convert;
	}

	@Override
	public void doIt() throws Exception {

		if (hgncFile == null){
			throw new Exception("hgncFile is null");
		}
		check();

//		System.out.println(kitTable.toString());

//		List<String> headerNames = kitTable.getHeaderNames();
//		int index = 0;
//		for (String string : headerNames) {
//			System.out.printf("index: %d = %s\n", index, string);
//			index++;
//		}

		final int indexOfHGNC = 0;
		final int indexOfSymbol = 1;
		final int indexOfNCBI = 18;
		final int indexOfEns = 19;

		Map<String, String> symbol2Ens = Maps.newHashMap();
		EGPSFileUtil.forLoopToFileMaybeCompressed(hgncFile, line -> {
			String[] strings = EGPSStringUtil.split(line, '\t');
			symbol2Ens.put(strings[indexOfSymbol], strings[indexOfEns]);
			return false;
		});

		KitTable kitTable = TSVReader.readTsvTextFile(inputPath, false);
		MutableInt totalSymbolNeedToConvert = new MutableInt();
		List<String> symbolCanNotConvert = Lists.newArrayList();

		List<String> outputList = Lists.newLinkedList();
		kitTable.getContents().forEach(list -> {
			String key = list.get(indexOfColumn4convert);
			String convertedValue = symbol2Ens.get(key);
			totalSymbolNeedToConvert.increment();
			if (convertedValue == null){
				symbolCanNotConvert.add(key);
			}

			outputList.add( key + "\t" + convertedValue);
		});

		System.out.println("Total: " + totalSymbolNeedToConvert.intValue() );
		System.out.println("Can not convert: " + symbolCanNotConvert.size());
		System.out.println(symbolCanNotConvert.toString());

		Files.write(Path.of(outputPath), outputList);
	}

}
