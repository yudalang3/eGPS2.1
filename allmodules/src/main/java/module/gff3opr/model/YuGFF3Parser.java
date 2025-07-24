package module.gff3opr.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import egps2.frame.gui.comp.search.SearchItem;
import module.genome.GenomicRange;

public class YuGFF3Parser {

	private static final Logger logger = LoggerFactory.getLogger(YuGFF3Parser.class);

	FeatureList featureList = new FeatureList();


	public FeatureList getFeatureList() {
		return featureList;
	}

	public FeatureList searchFeaturesWithKeywordsByPartialMatch(SearchItem keywords, String path)
			throws IOException {

		FeatureList featureListLocal = new FeatureList();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				if (readLine.startsWith("#")) {
					continue;
				}
				GFF3Feature theFeature = getTheFeature(readLine);
				
				boolean shouldKept = keywords.search(readLine);
				if (shouldKept) {
					featureListLocal.add(theFeature);
				}
				
				/**
				 * 这还是需要的，因为后面要搜索
				 */
				featureList.add(theFeature);
			}
		}
		return featureListLocal;
	}

	public FeatureList parseGFF3File(String path) throws IOException {
		logger.trace("Start to reading the gff3 files.");
		featureList = new FeatureList();

		int sz = 100 * 1024 * 1024;

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(path);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				if (readLine.startsWith("#")) {
					continue;
				}
				GFF3Feature theFeature = getTheFeature(readLine);
				featureList.add(theFeature);
			}
		}

		logger.trace("Finish reading , the count meet is {} \n", featureList.size());

		return featureList;
	}

	public void parseGff3LineByLine(String path, Predicate<GFF3Feature> featureOp) throws IOException {

		int sz = 100 * 1024 * 1024;

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(path);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				if (readLine.startsWith("#")) {
					continue;
				}
				GFF3Feature theFeature = getTheFeature(readLine);

				boolean test = featureOp.test(theFeature);
				if (test) {
					break;
				}
			}
		}

	}

	public void parseGff3LineByLine(String path, BiPredicate<String, GFF3Feature> featureOp) throws IOException {

		int sz = 100 * 1024 * 1024;

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(path);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				GFF3Feature theFeature = null;
				if (readLine.length() > 0 && readLine.charAt(0) == '#') {

				} else {
					theFeature = getTheFeature(readLine);
				}


				boolean test = featureOp.test(readLine, theFeature);
				if (test) {
					break;
				}
			}
		}

	}

	/**
	 * 注意如果没有score的话，用 -1 代替
	 * 
	 * @param line
	 * @return
	 */
	public GFF3Feature getTheFeature(String line) {
		String[] strings = EGPSStringUtil.split(line, '\t', 9);
		String attrStr = strings[FieldConstant.ATTR_FIELD_INDEX];

		String seqname = strings[FieldConstant.SEQID_FIELD_INDEX];
		String source = strings[FieldConstant.SOURCE_FIELD_INDEX];
		String type = strings[FieldConstant.TYPE_FIELD_INDEX];
		String startStr = strings[FieldConstant.START_FIELD_INDEX];
		String endStr = strings[FieldConstant.END_FIELD_INDEX];
		String strand = strings[FieldConstant.STRAND_FIELD_INDEX];
		String score = strings[FieldConstant.SCORE_FIELD_INDEX];
		String frame = strings[FieldConstant.PHASE_FIELD_INDEX];
		GenomicRange loc = GenomicRange.fromBio(Integer.parseInt(startStr), Integer.parseInt(endStr), strand.charAt(0));

		Double scoreValue = Doubles.tryParse(score);
		if (scoreValue == null) {
			scoreValue = FieldConstant.MISSING_SCORE;
		}

		Integer frameValue = Ints.tryParse(frame);
		if (frameValue == null) {
			frameValue = -1;
		}

		GFF3Feature feature = new GFF3Feature(seqname, source, type, loc, scoreValue, frameValue, attrStr);

		return feature;
	}

}
