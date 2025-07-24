package module.brutemapping;

import java.util.InputMismatchException;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;

public class Importer4BruteMappingAbstract extends AbstractParamsAssignerAndParser4VOICE {

	public Importer4BruteMappingAbstract() {
		addKeyValueEntryBean("genome.fasta.path", "", "Input the genome fasta file, gzip format is supportted.");
		addKeyValueEntryBean("query.fasta.path", "", "Input the query sequence fasta file, gzip format is supportted.");
		addKeyValueEntryBean("only.include.name.key", "chromosome",
				"Only include sequence with name contains the key (case insensitive). This is usefull to exclude the scafford.");
		addKeyValueEntryBean("exclude.name.key", "scaffold",
				"Exclude sequence with name contains the key (case insensitive). This is usefull to exclude the scafford. If both include and exclude setting, include has priority.");
		addKeyValueEntryBean("out.file.path", "",
				"Output results with bed file, default is not output. If you want to get results, please entery the file path.");

		addKeyValueEntryBean("need.reverse.complement", "F",
				"Whether need the reverse complement results, default false.");
	}

	public ImportDataBean getImportDataInformation(String str) {
		ImportDataBean importDataBean = new ImportDataBean();

		Map<String, String> keyValueStringMap = getKeyValueStringMap(str);
		String string = keyValueStringMap.get("$query.fasta.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new InputMismatchException("You need to input $query.fasta.path parameter.");
		}
		importDataBean.queryFastaPath = string;

		string = keyValueStringMap.get("$genome.fasta.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new InputMismatchException("You need to input $genome.fasta.path parameter.");
		}
		importDataBean.genomeFastaPath = string;

		string = keyValueStringMap.get("$only.include.name.key");
		if (string != null) {
			importDataBean.onlyIncludeKeySeqeunce = string;
		}
		string = keyValueStringMap.get("$exclude.name.key");
		if (string != null) {
			importDataBean.excludeSequenceName = string;
		}
		string = keyValueStringMap.get("$out.file.path");
		if (string != null) {
			importDataBean.outputBedPath = string;
		}
		string = keyValueStringMap.get("$need.reverse.complement");
		if (string != null) {
			boolean boolean1 = BooleanUtils.toBoolean(string);
			importDataBean.needReverseComplement = boolean1;
		}

		return importDataBean;
	}

}
