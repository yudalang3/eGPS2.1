
package module.heatmap.eheatmap.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.math.matrix.GeneralMatrixOp;
import egps2.utils.common.model.datatransfer.ThreeTuple;
import egps2.utils.common.tablelike.MatrixTableContentBean;
import egps2.UnifiedAccessPoint;
import module.heatmap.eheatmap.data.ValidateMatrixTable;
import module.heatmap.eheatmap.model.transform.CoxBoxTransform;
import module.heatmap.eheatmap.model.transform.DirectlyLog;
import module.heatmap.eheatmap.model.transform.LogPlusMin;
import module.heatmap.eheatmap.model.transform.LogPlusOne;
import module.heatmap.eheatmap.model.transform.MinMaxScale;
import module.heatmap.eheatmap.model.transform.VectorTransform;
import module.heatmap.eheatmap.model.transform.Zscore;

/**
 * 通过三个 set* 方法读入数据： 这分别对应着三种输入的方式： 第一种是输入一个 table like 的表格，一般第一行是Header第二行是二维表
 * 第二种是输入一个 eheatmap格式的文件，可以有各种参数 第三种是直接输入行名、列名和double二维数组
 */

public class DataModel implements Cloneable {

	VectorTransform transformMethod = new LogPlusOne(Math.E);

	double[][] oriDataMatrix;
	double[][] presentDataMatrix;
	double maxValue;
	double minValue;
	double oriMaxValue;
	double oriMinValue;
	String[] rowNames;
	String[] colNames;

	int[] rowOrderMaping;
	int[] colOrderMaping;

	double avgOfLambda = 0;

	boolean ifTransposed = false;

	/**
	 * 数据变换的方式，各种log变换什么的。
	 */
	private int wayOfTransform;

	private Pattern regExPattern = Pattern.compile("\\t|\\||,");

	public DataModel() {
	}

	@Override
	public DataModel clone() throws CloneNotSupportedException {
		DataModel ret = new DataModel();
		ret.transformMethod = this.transformMethod;
		ret.oriDataMatrix = this.oriDataMatrix;
		ret.presentDataMatrix = this.presentDataMatrix;
		ret.maxValue = this.maxValue;
		ret.minValue = this.minValue;
		ret.oriMaxValue = this.oriMaxValue;
		ret.oriMinValue = this.oriMinValue;
		ret.rowNames = this.rowNames;
		ret.colNames = this.colNames;
		ret.rowOrderMaping = this.rowOrderMaping;
		ret.colOrderMaping = this.colOrderMaping;
		ret.avgOfLambda = this.avgOfLambda;
		ret.ifTransposed = this.ifTransposed;

		return ret;
	}

	private void loadDataFromFile(boolean hasColName, boolean hasRowName, String regStr, List<String> validateLines) {
		regExPattern = Pattern.compile(regStr);

		try {
			if (hasColName) {
				if (hasRowName) {
					// both has col name and row name
					loadDataHasColHasRow(validateLines);
				} else {
					// has col name but not row name
					loadDataHasColNotRow(validateLines);
				}
			} else {
				if (hasRowName) {
					// not col name but row name
					loadDataNotColHasRow(validateLines);
				} else {
					// neither
					loadDataNotColNotRow(validateLines);
				}
			}
		} catch (Exception e) {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("Sorry! Cant correct parse the matrix data!\nCurrent parse scheme is:\n");
			sBuilder.append("Has header(column names): ").append(hasColName).append("\n");
			sBuilder.append("Has row names: ").append(hasRowName).append("\n");
			sBuilder.append("Line separator:").append(regStr).append("\n");
			sBuilder.append("Please change parsing scheme in the information area in Data Panel!");
			SwingDialog.showErrorMSGDialog("Parsing error", sBuilder.toString());

			Logger.getGlobal().severe(sBuilder.toString());
		}

		presentDataMatrix = oriDataMatrix;

		oriMaxValue = maxValue;
		oriMinValue = minValue;
	}

	private void loadDataNotColHasRow(List<String> validateLines) {
		int numOfValidateLines = validateLines.size();

		oriDataMatrix = new double[numOfValidateLines][];
		rowNames = new String[numOfValidateLines];
		String[] candidateOfColNames = regExPattern.split(validateLines.get(0), 0);
		int numOfCols = candidateOfColNames.length - 1;
		colNames = new String[numOfCols];
		for (int j = 0; j < numOfCols; j++) {
			colNames[j] = "ColName" + (j + 1);
		}
		for (int i = 0; i < numOfValidateLines; i++) {
			String string = validateLines.get(i);
			String[] split = regExPattern.split(string, 0);
			// System.out.println(validateLines.get(i));
			double[] eachRow = new double[numOfCols];

			for (int j = 1; j < numOfCols; j++) {
				double parseDouble = 0;
				if (split[j].equalsIgnoreCase("NA")) {
					parseDouble = Double.NaN;
				} else {
					parseDouble = Double.parseDouble(split[j]);
				}
				if (parseDouble > maxValue) {
					maxValue = parseDouble;
				} else if (parseDouble < minValue) {
					minValue = parseDouble;
				}
				eachRow[j - 1] = parseDouble;
			}
			oriDataMatrix[i] = eachRow;
			rowNames[i] = "RowName_" + (i + 1);
		}

	}

	private void loadDataNotColNotRow(List<String> validateLines) {
		int numOfValidateLines = validateLines.size();

		oriDataMatrix = new double[numOfValidateLines][];
		rowNames = new String[numOfValidateLines];
		String[] candidateOfColNames = regExPattern.split(validateLines.get(0), 0);
		int numOfCols = candidateOfColNames.length;
		colNames = new String[numOfCols];
		for (int j = 0; j < numOfCols; j++) {
			colNames[j] = "ColName" + (j + 1);
		}
		for (int i = 0; i < numOfValidateLines; i++) {
			String string = validateLines.get(i);
			String[] split = regExPattern.split(string, 0);
			// System.out.println(validateLines.get(i));
			double[] eachRow = new double[numOfCols];

			for (int j = 0; j < numOfCols; j++) {
				double parseDouble = 0;
				if (split[j].equalsIgnoreCase("NA")) {
					parseDouble = Double.NaN;
				} else {
					parseDouble = Double.parseDouble(split[j]);
				}
				if (parseDouble > maxValue) {
					maxValue = parseDouble;
				} else if (parseDouble < minValue) {
					minValue = parseDouble;
				}
				eachRow[j] = parseDouble;
			}
			oriDataMatrix[i] = eachRow;
			rowNames[i] = "RowName_" + (i + 1);
		}

	}

	private void loadDataHasColNotRow(List<String> validateLines) {
		int numOfValidateLines = validateLines.size();

		oriDataMatrix = new double[numOfValidateLines - 1][];
		rowNames = new String[numOfValidateLines - 1];
		String[] candidateOfColNames = regExPattern.split(validateLines.get(0), 0);
		int numOfCols = candidateOfColNames.length;
		colNames = new String[numOfCols];
		for (int j = 0; j < numOfCols; j++) {
			colNames[j] = candidateOfColNames[j];
		}
		for (int i = 1; i < numOfValidateLines; i++) {
			String string = validateLines.get(i);
			String[] split = regExPattern.split(string, 0);
			// System.out.println(validateLines.get(i));
			double[] eachRow = new double[numOfCols];

			for (int j = 0; j < numOfCols; j++) {
				double parseDouble = 0;
				if (split[j].equalsIgnoreCase("NA")) {
					parseDouble = Double.NaN;
				} else {
					parseDouble = Double.parseDouble(split[j]);
				}
				if (parseDouble > maxValue) {
					maxValue = parseDouble;
				} else if (parseDouble < minValue) {
					minValue = parseDouble;
				}
				eachRow[j] = parseDouble;
			}
			oriDataMatrix[i - 1] = eachRow;
			rowNames[i - 1] = "RowName_" + i;
		}

	}

	private void loadDataHasColHasRow(List<String> validateLines) {
		int numOfValidateLines = validateLines.size();

		oriDataMatrix = new double[numOfValidateLines - 1][];
		rowNames = new String[numOfValidateLines - 1];
		String[] candidateOfColNames = regExPattern.split(validateLines.get(0), 0);
		int numOfCols = candidateOfColNames.length;
		colNames = new String[numOfCols - 1];
		for (int j = 1; j < numOfCols; j++) {
			colNames[j - 1] = candidateOfColNames[j];
		}
		for (int i = 1; i < numOfValidateLines; i++) {
			String string = validateLines.get(i);
			String[] split = regExPattern.split(string, 0);
			// System.out.println(validateLines.get(i));
			double[] eachRow = new double[numOfCols - 1];

			for (int j = 1; j < numOfCols; j++) {
				double parseDouble = 0;
				if (split[j].equalsIgnoreCase("NA")) {
					parseDouble = Double.NaN;
				} else {
					parseDouble = Double.parseDouble(split[j]);
				}
				if (parseDouble > maxValue) {
					maxValue = parseDouble;
				} else if (parseDouble < minValue) {
					minValue = parseDouble;
				}
				eachRow[j - 1] = parseDouble;
			}
			oriDataMatrix[i - 1] = eachRow;
			rowNames[i - 1] = split[0];
		}

	}

	void setDataOfTableLikeTextFile(List<String> readLines, boolean ifQuery, MatrixTableContentBean mtBean) {
		List<String> validateLines = getValidateLines(readLines, ifQuery);
		loadDataFromFile(mtBean.isHasColumn(), mtBean.isHasRows(), mtBean.getLineSeparator().getRegularExp(),
				validateLines);
	}

	/**
	 * If you pass true it will suspend to query user for dealing with missing data!
	 * If pass false, it will keep the line contains NA string!
	 * 
	 * @param inputFile
	 * @param ifQuery   : If query user to deal with NA data!
	 */
	void setDataOfEHeatmapFormat(List<String> readLines, boolean ifQuery) {

		List<String> validateLines = getValidateLines(readLines, ifQuery);

		int len = validateLines.size();
		oriDataMatrix = new double[len - 1][];
		rowNames = new String[len - 1];
		String[] split1 = regExPattern.split(validateLines.get(0), 0);
		int numOfCols = split1.length;
		colNames = new String[numOfCols - 1];
		for (int j = 1; j < numOfCols; j++) {
			colNames[j - 1] = split1[j];
		}

		for (int i = 1; i < len; i++) {
			String string = validateLines.get(i);
			String[] split = regExPattern.split(string, 0);
			// System.out.println(validateLines.get(i));
			double[] eachRow = new double[numOfCols - 1];

			for (int j = 1; j < numOfCols; j++) {
				double parseDouble = 0;
				if (split[j].equalsIgnoreCase("NA")) {
					parseDouble = Double.NaN;
				} else {
					parseDouble = Double.parseDouble(split[j]);
				}

				if (parseDouble > maxValue) {
					maxValue = parseDouble;
				} else if (parseDouble < minValue) {
					minValue = parseDouble;
				}

				eachRow[j - 1] = parseDouble;
			}
			oriDataMatrix[i - 1] = eachRow;

			rowNames[i - 1] = split[0];
		}

		presentDataMatrix = oriDataMatrix;

		oriMaxValue = maxValue;
		oriMinValue = minValue;

	}

	/**
	 * first is row names; next is col names; last is data matrix!
	 * 
	 * @param element
	 */
	public void setDataFromMatrixWithNames(ThreeTuple<String[], String[], double[][]> element) {
		rowNames = element.first;
		colNames = element.second;

		presentDataMatrix = oriDataMatrix = element.third;

		for (double[] ds : presentDataMatrix) {
			for (double d : ds) {
				if (d > maxValue) {
					maxValue = d;
				} else if (d < minValue) {
					minValue = d;
				}
			}
		}

		oriMaxValue = maxValue;
		oriMinValue = minValue;
	}

	/**
	 * 最基本的导入方式
	 * @param contents 不去除#开头的注释行
	 * @param ifQuery 表示是否要询问用户如何处理NAN数据
	 * @return true 是 eGPS heatmap format
	 */
	public boolean setDataFromTextContens(List<String> contents, boolean ifQuery) {

		final String headerOfEGPSHeatmap = "##fileformat=eGPS heatmap";
		String firstLine = contents.get(0);
		boolean isEHeatmapFormat = Objects.equals(firstLine.substring(0, headerOfEGPSHeatmap.length()),
				headerOfEGPSHeatmap);
		if (isEHeatmapFormat) {
			setDataOfEHeatmapFormat(contents, ifQuery);
		} else {
			// General Table file
			ValidateMatrixTable validateMatrixTable = new ValidateMatrixTable();
			// 这一步是必须的！！否则后面的返回会空
			validateMatrixTable.getFileFormat(contents);
			Optional<MatrixTableContentBean> bean = validateMatrixTable.getBean();
			if (bean.isPresent()) {
				MatrixTableContentBean matrixTableContentBean = bean.get();
				setDataOfTableLikeTextFile(contents, ifQuery, matrixTableContentBean);
			} else {
				throw new IllegalArgumentException("Sorry, the input format is not table like content.");
			}
		}
		
		return isEHeatmapFormat;

	}

	private List<String> getValidateLines(List<String> readLines, boolean ifQuery) {
		boolean ifFirstTimeToDetectNaN = true;
		boolean ifNaNremoved = false;

		// 如果是不询问那么直接保留含有NA的行！
		if (!ifQuery) {
			ifFirstTimeToDetectNaN = false;
			ifNaNremoved = false;
		}

		List<String> validateLines = null;
		validateLines = new ArrayList<>(8192);
		for (String string : readLines) {
			if (string.startsWith("#")) {
				if (string.substring(0, 3).equalsIgnoreCase("###")) {
					break;
				}
				continue;
			} else {
				String trim = string.trim();
				if (trim.length() > 0) {

					String[] split1 = regExPattern.split(trim, 0);
					boolean hasNaN = false;
					for (int i = 1; i < split1.length; i++) {

						if (split1[i].equalsIgnoreCase("NA")) {
							if (ifFirstTimeToDetectNaN) {
								int response = JOptionPane.showConfirmDialog(UnifiedAccessPoint.getInstanceFrame(),
										"Do you want to remove rows contain NaN?\nIf not some clustering or transformation operation can't be accomplished ?",
										"NaN operation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (response == JOptionPane.NO_OPTION) {
									ifNaNremoved = false;
								} else if (response == JOptionPane.YES_OPTION) {
									ifNaNremoved = true;
								} else if (response == JOptionPane.CLOSED_OPTION) {
									ifNaNremoved = true;
								}

								ifFirstTimeToDetectNaN = false;
							}

							hasNaN = true;
						}

					}

					if (ifNaNremoved && hasNaN) {
						continue;
					}

					validateLines.add(string);
				}
			}
		}

		return validateLines;
	}

	public void outputDataMatrix(File file) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter(file));
		String[] colNames2 = getColNames();
		int numOfCols = colNames2.length;
		for (int i = 0; i < numOfCols; i++) {
			br.write("\t");
			br.write(colNames2[i]);
		}
		String[] rowNames2 = getRowNames();
		double[][] dataMatrix = getDataMatrix();
		int length = rowNames2.length;
		for (int i = 0; i < length; i++) {
			br.newLine();
			br.write(rowNames2[i]);

			for (int j = 0; j < numOfCols; j++) {
				br.write("\t");
				br.write(dataMatrix[i][j] + "");
			}

		}

		br.close();
	}

	/**
	 * 1: No transformation <br>
	 * 2: Log transformation <br>
	 * p1 is the log method: 0 is log(x)/ 1 is log(x+1); 1 is log (x+min) <br>
	 * p2 is the log base 3: Z-score <br>
	 * 4: Min-Max scaling <br>
	 * 5: Cox-Box <br>
	 * p1 is the Cox-Box method;
	 * 
	 * @param int wayOfTransform
	 */
	public void transformData(int wayOfTransform, int p1, double e) {

		switch (wayOfTransform) {
		case 2:
			if (p1 == 1) {
				transformMethod = new LogPlusOne(e);
			} else if (p1 == 2) {
				transformMethod = new DirectlyLog(e);
			} else {
				transformMethod = new LogPlusMin(e, oriMinValue);
			}
			transformRowByRow();
			break;
		case 3:
			transformMethod = new Zscore();
			transformRowByRow();
			break;
		case 4:
			transformMethod = new MinMaxScale();
			transformRowByRow();
			break;
		case 5:
			transformMethod = new CoxBoxTransform();
			transformRowByRow();
			break;

		default:
			// Also is the case 1
			maxValue = Double.MIN_VALUE;
			minValue = Double.MAX_VALUE;
			int len = oriDataMatrix.length;
			presentDataMatrix = oriDataMatrix;
			for (int i = 0; i < len; i++) {
				int tt = presentDataMatrix[i].length;
				for (int j = 0; j < tt; j++) {
					double tmp = presentDataMatrix[i][j];
					if (tmp > maxValue) {
						maxValue = tmp;
					} else if (tmp < minValue) {
						minValue = tmp;
					}
				}
			}
			break;
		}

		this.wayOfTransform = wayOfTransform;
	}

	public int getWayOfTransform() {
		return wayOfTransform;
	}

	private void transformRowByRow() {
		maxValue = Double.MIN_VALUE;
		minValue = Double.MAX_VALUE;

		avgOfLambda = 0;
		int len = presentDataMatrix.length;

		presentDataMatrix = new double[len][];
		for (int i = 0; i < len; i++) {
			presentDataMatrix[i] = transformMethod.transform(oriDataMatrix[i]);

			if (transformMethod instanceof CoxBoxTransform) {
				avgOfLambda += ((CoxBoxTransform) transformMethod).getChoosedLambda();
			}
			int tt = presentDataMatrix[i].length;

			for (int j = 0; j < tt; j++) {
				double parseDouble = presentDataMatrix[i][j];
				if (parseDouble > maxValue) {
					maxValue = parseDouble;
				} else if (parseDouble < minValue) {
					minValue = parseDouble;
				}
			}
		}

		avgOfLambda = avgOfLambda / len;
	}

	public double[][] getDataMatrix() {
		return presentDataMatrix;
	}

	public int getNumOfCols() {
		return presentDataMatrix[presentDataMatrix.length - 1].length;
	}

	public int getNumOfRows() {
		return presentDataMatrix.length;
	}

	public double[][] getOriDataMatrix() {
		return oriDataMatrix;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getOriMaxValue() {
		return oriMaxValue;
	}

	public double getOriMinValue() {
		return oriMinValue;
	}

	public String[] getColNames() {
		if (colNames == null) {
			int len = oriDataMatrix[0].length;
			colNames = new String[len];
			for (int i = 0; i < len; i++) {
				colNames[i] = "Col_" + (i + 1);
			}
		}
		return colNames;
	}

	public String[] getRowNames() {
		if (rowNames == null) {
			int len = oriDataMatrix.length;
			rowNames = new String[len];
			for (int i = 0; i < len; i++) {
				rowNames[i] = "Row_" + (i + 1);
			}
		}

		return rowNames;
	}

	public double getAvgOfLambda() {
		return avgOfLambda;
	}

	public double[][] getRowDistanceMatrix(PairwiseDistance pairwiseDistance) {
		int numOfRowMinusOne = presentDataMatrix.length - 1;
		double[][] ret = new double[numOfRowMinusOne][];

		for (int i = 0; i < numOfRowMinusOne; i++) {
			int ll = i + 1;
			double[] tmp = new double[ll];
			for (int j = 0; j < ll; j++) {
				tmp[j] = pairwiseDistance.getPairwiseDist(presentDataMatrix[i + 1], presentDataMatrix[j]);
			}
			ret[i] = tmp;
//			System.out.println(Arrays.toString(tmp));
		}

		return ret;
	}

	public double[][] getColDistanceMatrix(PairwiseDistance pairwiseDistance) {
		double[][] transposedMatrix = GeneralMatrixOp.transpose(presentDataMatrix);

		int numOfRowMinusOne = transposedMatrix.length - 1;
		double[][] ret = new double[numOfRowMinusOne][];

		for (int i = 0; i < numOfRowMinusOne; i++) {
			int ll = i + 1;
			double[] tmp = new double[ll];
			for (int j = 0; j < ll; j++) {
				tmp[j] = pairwiseDistance.getPairwiseDist(transposedMatrix[i + 1], transposedMatrix[j]);

				if (Double.isNaN(tmp[j])) {
					// System.out.println(Arrays.toString(transposedMatrix[i+1]));
					// System.out.println(Arrays.toString(transposedMatrix[j]));
				}
			}
			ret[i] = tmp;
		}

		return ret;
	}

	public void transposeData() {
		presentDataMatrix = GeneralMatrixOp.transpose(presentDataMatrix);
		oriDataMatrix = GeneralMatrixOp.transpose(oriDataMatrix);
		// Swap data
		String[] temp = rowNames;
		rowNames = colNames;
		colNames = temp;

	}

	public int[] getRowOrderMaping() {
		if (rowOrderMaping == null) {
			int length = getRowNames().length;
			rowOrderMaping = new int[length];
			for (int i = 0; i < length; i++) {
				rowOrderMaping[i] = i;
			}
		}
		return rowOrderMaping;
	}

	public void setRowOrderMaping(int[] rowOrderMaping) {
		this.rowOrderMaping = rowOrderMaping;
	}

	public int[] getColOrderMaping() {
		if (colOrderMaping == null) {
			int length = getColNames().length;
			colOrderMaping = new int[length];
			for (int i = 0; i < length; i++) {
				colOrderMaping[i] = i;
			}
		}
		return colOrderMaping;
	}

	public void setColOrderMaping(int[] colOrderMaping) {
		this.colOrderMaping = colOrderMaping;
	}

	public int getOriginalIndexForRows(String leafName) {
		int length = rowNames.length;
		for (int i = 0; i < length; i++) {
			String string = rowNames[i];
			if (string.equalsIgnoreCase(leafName)) {
				return i;
			}
		}
		return -1;
	}

}
