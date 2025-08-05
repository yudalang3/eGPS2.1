package module.evolview.gfamily.work.beans;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;
import module.evolview.model.AlignmentGetSequence;

public class RequiredGeneData {

	protected GeneStructureInfo geneStructureInfo;
	protected AlignmentGetSequence sequenceInforObtainer;

	protected String geneStructInfoPath;
	protected String alignmentFilePath;
	protected String keyDomainsFilePath;
	protected String primersFilePath;
	
	private GeneMetaInfo getGeneStructureInfo() throws IOException {
		GeneMetaInfo geneStruct = GeneMetaInfo.obtainTheGeneMetaInfo(new File(geneStructInfoPath));
		return geneStruct;
	}

	public String getGeneStructInfoPath() {
		return geneStructInfoPath;
	}

	public GeneStructureInfo getGeneStructComputerStruct() throws IOException {
		if (geneStructureInfo == null) {

			GeneMetaInfo geneStruct = null;
			try {
				geneStruct = getGeneStructureInfo();
			} catch (IOException e) {
				e.printStackTrace();
			}
			geneStructureInfo = new GeneStructureInfo(geneStruct);
		}

		return geneStructureInfo;
	}
	
	public Optional<List<Double>> getAlleleFreqScores() {
		List<Double> charScores = null;
		
		ThreadLocalRandom current = ThreadLocalRandom.current();
		try {
			GeneStructureInfo geneStructComputerStruct = getGeneStructComputerStruct();
			int geneLength = geneStructComputerStruct.geneLength;
			
			charScores = new ArrayList<>();
			for (int i = 0; i < geneLength; i++) {
				charScores.add(0.0);
			}
			for (int i = 0; i < geneLength; i+=100) {
				double randomFloat = current.nextDouble();
				charScores.set(i, randomFloat);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Optional.ofNullable(charScores);
	}

	public Optional<AlignmentGetSequence> getAlignmentSequence(){
		File alignmentFile = new File(alignmentFilePath);
		if (alignmentFilePath == null || !alignmentFile.exists()) {
			return Optional.empty();
		}
		if (sequenceInforObtainer == null) {
			sequenceInforObtainer = new AlignmentGetSequence();
			try {
				sequenceInforObtainer.setGetSequenceByLocal(alignmentFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return Optional.of(sequenceInforObtainer);
	}
	
	public Optional<TrackMultipleDomainBean> getKeyDomains() {
		DrawingPropertySequenceModel parseWithLocalFile = null;
		
		ReaderDoMain readerDoMain = new ReaderDoMain();
		File file = new File(keyDomainsFilePath);

		if (keyDomainsFilePath == null || !file.exists()) {
			return Optional.empty();
		}

		try {
			parseWithLocalFile = readerDoMain.parseWithLocalFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		TrackMultipleDomainBean ret = new TrackMultipleDomainBean();
		
		ret.setMultiTracksOfSeqElements(Arrays.asList(parseWithLocalFile));
		return Optional.ofNullable(ret);
	}
	
	public Optional<DrawingPropertyPrimerSet> getPrimerSets() {
		if (primersFilePath == null) {
			return Optional.empty();
		}

		File file = new File(primersFilePath);
		if (!file.exists()) {
			return Optional.empty();
		}

		DrawingPropertyPrimerSet generateNcov2019GenomePaintPrimerSet = null;
		
		ReaderPrimer readerDoMain = new ReaderPrimer();

		List<String> readLines = null;
		try {
			readLines = FileUtils.readLines(file,StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Optional<AlignmentGetSequence> alignmentSequence = getAlignmentSequence();
		
		if (!alignmentSequence.isPresent()) {
			throw new InputMismatchException("You need to provide the alignment first.");
		}
		
		generateNcov2019GenomePaintPrimerSet = readerDoMain.generateNcov2019GenomePaintPrimerSet(alignmentSequence.get(), readLines);
		
		
		return Optional.ofNullable(generateNcov2019GenomePaintPrimerSet);
	}

	public void setGeneStructInfoPath(String geneStructInfoPath) {
		this.geneStructInfoPath = geneStructInfoPath;
	}

	public void setAlignmentFilePath(String alignmentFilePath) {
		this.alignmentFilePath = alignmentFilePath;
	}

	public void setKeyDomainsFilePath(String keyDomainsFilePath) {
		this.keyDomainsFilePath = keyDomainsFilePath;
	}

	public void setPrimersFilePath(String primersFilePath) {
		this.primersFilePath = primersFilePath;
	}

	

	
}
