package module.evoldist.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import egps2.utils.common.math.matrix.MatrixTriangleOp;
import utils.EGPSFileUtil;
import module.evoldist.operator.pairdist.DistParameterLevel1;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class BootstrapDistCalculator<E extends DistParameterLevel1> extends DistanceCalculateor {

	/** For producing random number*/
	protected Random random = new Random();
	protected E[][][] preCalSegmentPairwiseDist;
	protected short preCalSegmentPairwiseDistIndex = 0;
	/** For one bootstrap */
	protected E[][] tempWholeSeqsDistanceM;
	
	protected int bootTimes = 50;
	protected int bootstrapRunTimeIndex = 0;
	
	protected EvoPairwiseDistMethod<E> evoPairwiseDistMethod;
	
	private BufferedWriter bWriter;
	
	public BootstrapDistCalculator() {}
	
	public BootstrapDistCalculator(String[] sequences, String[] seqNames) {
		super(sequences,seqNames);
	}
	
	public BootstrapDistCalculator(List<String> seqs, List<String> seq_names) {
		super(seqs,seq_names);
	}
	
	public void setbWriter(File outputFile) {
		try {
			EGPSFileUtil.createFile(outputFile.getAbsolutePath());
			this.bWriter = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeBWriter() {
		try {
			this.bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBootTimes(int bootTimes) {
		this.bootTimes = bootTimes;
	}
	
	public void initialize() throws Exception {
		evoPairwiseDistMethod = (EvoPairwiseDistMethod<E>) alignmentDists.getPairEvoDistance().getEvopairDistMethod();
		preprocessAlignment();
		initWholeTempStoreVariable();
		getOriEvoDistance();
		createPreCalulatedDisMList();
	}
	
	protected void preprocessAlignment() {
		sequences = getProcessedAlignment();
		alignmentDists.reSetSeqs(sequences);
		
		seqLength = sequences[0].length();
	}
	protected void initWholeTempStoreVariable() {
		// new the storage information for whole pairwise distances
		tempWholeSeqsDistanceM = iniTwoDimArray();
	}
	
	protected E[][]	iniTwoDimArray() {
		E[][] ret = (E[][]) new DistParameterLevel1[seqNames.length - 1][];
		for (int i = 0; i < ret.length; i++) {
			E[] aRow = (E[]) new DistParameterLevel1[i + 1];

			for (int j = 0; j <= i; j++) {
				try {
					aRow[j] = (E) alignmentDists.getPairEvoDistance().getKind().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			ret[i] = aRow;
		}
		
		return ret;
	}
	
	protected void getOriEvoDistance() throws Exception {
		double[][] evoDistances = alignmentDists.getEvoDistances();
		validateMatrix(evoDistances);
	}
	
	
	/**
	 * Form seqs to create a list which contains the segment distance matrix. If num
	 * of seqs'site is no more than 1000, we directly divid them into parts(col
	 * 
	 * @author yudalang
	 * @date 2018-7-27
	 */
	protected void createPreCalulatedDisMList() {

		preCalSegmentPairwiseDist = (E[][][]) new DistParameterLevel1[1000][][];
		
		if (seqLength <= 1000) {
			// We just directly use every site for boostrap
			for (int i = 0; i < seqLength; i++) {
				List<String> temp_seqs = new ArrayList<>();
				for (String string : sequences) {
					String str_fregment = string.substring(i, i + 1);
					temp_seqs.add(str_fregment);
				}
				addToPreCalSegments(temp_seqs);
			}
		} else {
			// seqLength is the number of bootstrap resample unit
			int quotient = (int) Math.ceil(seqLength / 1000d);
			// [ pre , next ) start with 0; so the when using string.substring() we could
			// directely use.
			int pre = 0;
			int next = pre + quotient;
			int numOfIteration = 0;
			while (next < seqLength) {

				List<String> temp_seqs = new ArrayList<>();
				for (String string : sequences) {
					String str_fregment = string.substring(pre, next);
					temp_seqs.add(str_fregment);
				}

				addToPreCalSegments(temp_seqs);

				next += quotient;
				pre += quotient;
				numOfIteration++;
			}

			// We need to run the last time.
			List<String> temp_seqs = new ArrayList<>();
			for (String string : sequences) {
				String str_fregment = string.substring(pre, seqLength);
				temp_seqs.add(str_fregment);
			}

			addToPreCalSegments(temp_seqs);

			numOfIteration++;
			seqLength = numOfIteration;
		}

	}
	
	protected void addToPreCalSegments(List<String> temp_seqs) {
		String[] sequenceStrings = new String[temp_seqs.size()];
		sequenceStrings = temp_seqs.toArray(sequenceStrings);
		addToPreCalSegments(sequenceStrings);
	}
	
	protected void addToPreCalSegments(String[] temp_seqs) {
		if (preCalSegmentPairwiseDistIndex == 1000) {
			System.err.println(getClass()+"Impossible to get 1000!!");
			return;
		}
		
		String[] processedAlignment = dealWithDeletion(temp_seqs);
		alignmentDists.reSetSeqs(processedAlignment);
		
		E[][] oneSegmentDistOneBS = (E[][]) alignmentDists.getEvoDistParameters();
		
		preCalSegmentPairwiseDist[preCalSegmentPairwiseDistIndex] = oneSegmentDistOneBS;
		preCalSegmentPairwiseDistIndex ++;
	}
	
	
	/**
	 * Run once bootstrap for out class to invoke!
	 * 
	 *  @return whether outer class should invoke again!
	 */
	public boolean runOnceBSIteration() {
		// one bootstrap step
		initialTempDistanceMatrix();
		for (int j = 1; j <= seqLength; j++) {

			int randomValue = random.nextInt(seqLength);

			E[][] oneBootstrapDisMat = preCalSegmentPairwiseDist[randomValue];
			// System.out.println(randomValue);
			addOneBootstrapDisMatToTempDisMat(oneBootstrapDisMat);

		}
		//printTempDisMat();
		//System.out.println("This is bootstrap times\t" + bootstrapRunTimeIndex);
		
		/** YDL: This is the situation when some bootstrap segement, unfortunately be gap, i.e.:
		 *  A T T
		 *  T G G
		 *  - - -
		 *  g t a
		 *  So we will get error!! In this situation we need to run this method again! 
		 */
		double[][] distM = segmentDistToDoubleDist(tempWholeSeqsDistanceM);
		/** 
		 * Output distance matrix if no NaN
		 */
		if (EvoDistanceUtil.evaluateDistM(distM)[1]) {
			int numOfSeqs = sequences.length;
			double[][] matrixToExport = MatrixTriangleOp.downTriangleNoDiag2full(distM);
			try {
				bWriter.write("    ");
				bWriter.write(numOfSeqs+"");
				bWriter.write("\n");
				
				for (int i = 0; i < numOfSeqs; i++) {
					bWriter.write(StringUtils.rightPad(seqNames[i], 14));
					for (int j = 0; j < numOfSeqs; j++) {
						
						if (j != 0) {
							bWriter.write("  ");
						}
						bWriter.write(String.format("%.6f", matrixToExport[i][j]));
					}
					bWriter.write("\n");
				}
				bWriter.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else {
			return true;
		}
		
			
		bootstrapRunTimeIndex++;
		return bootstrapRunTimeIndex < bootTimes;
	}
	
	protected void initialTempDistanceMatrix() {
		for (int i = 0; i < tempWholeSeqsDistanceM.length; i++) {
			for (int j = 0; j <= i; j++) {
				tempWholeSeqsDistanceM[i][j].clear();
			}
		}
	}
	
	protected void addOneBootstrapDisMatToTempDisMat(E[][] oneBootstrapDisMat) {
		for (int i = 0; i < tempWholeSeqsDistanceM.length; i++) {
			for (int j = 0; j <= i; j++) {
				E ttt = (E) oneBootstrapDisMat[i][j];
				tempWholeSeqsDistanceM[i][j].add(ttt);
			}
		}
	}
	
	protected double[][] segmentDistToDoubleDist(E[][] wholePairwiseProps) {
		int len = wholePairwiseProps.length;
		double[][] ret = new double[len][];

		for (int i = 0; i < len; i++) {
			double[] aRow = new double[i + 1];
			for (int j = 0; j <= i; j++) {
				E tt = (E) wholePairwiseProps[i][j];
				aRow[j] = evoPairwiseDistMethod.getPairwiseDist(tt);
			}
			ret[i] = aRow;
		}
		return ret;
	}
	
	
}
