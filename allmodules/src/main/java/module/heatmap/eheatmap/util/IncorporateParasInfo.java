package module.heatmap.eheatmap.util;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.distance.AvgDotProduct;
import module.heatmap.eheatmap.model.distance.EuclideanDist;
import module.heatmap.eheatmap.model.distance.ManhattanDist;
import module.heatmap.eheatmap.model.distance.PearsonsCor;
import module.heatmap.eheatmap.model.distance.SpearmansCor;
import module.heatmap.eheatmap.tree.ClusterParameter;

public class IncorporateParasInfo {

	final String dataTranformation_NoTrans = "$dataTrans:noTrans=1";
	final String dataTranformation_zScoreTrans = "$dataTrans:zScoreTrans=1";
	final String dataTranformation_minMaxTrans = "$dataTrans:minMaxTrans=1";
	final String dataTranformation_logTrans = "$dataTrans:logTrans=";
	final String dataTranformation_coxBox = "$dataTrans:coxBox=";

	final String cluster_general = "$cluster:generalClusterOption=";
	final String cluster_hierarchicalCluster = "$cluster:hierarchicalCluster=";
	final String cluster_hierarchicalCluster_keepOriginalOrder = "$cluster:hierarchicalCluster:keepOriginalOrder=1";
	final String cluster_hierarchicalCluster_distMetric = "$cluster:hierarchicalCluster:distMetric=";
	final String cluster_hierarchicalCluster_linkageMethod = "$cluster:hierarchicalCluster:linkageMethod=";
	final String cluster_line_color = "$cluster:line:color=";
	final String cluster_line_width = "$cluster:line:width=";

	final String display_dataValue_status = "$display:dataValue:status=";
	final String display_dataValue_color = "$display:dataValue:color=";
	final String display_dataValue_font = "$display:dataValue:font=";
	final String display_dataValue_numOfdecimals = "$display:dataValue:numOfdecimals=";
	final String display_border = "$display:border=";
	final String display_border_color = "$display:border:color=";

	final String display_legend = "$display:legend=";
	final String display_legend_width = "$display:legend:width=";
	final String display_legend_height = "$display:legend:height=";

	final String display_rowNames = "$display:rowNames=";
	final String display_rowNames_color = "$display:rowNames:color=";
	final String display_rowNames_font = "$display:rowNames:font=";
	final String display_rowNames_roation = "$display:rowNames:roation=";

	final String display_colNames = "$display:colNames=";
	final String display_colNames_color = "$display:colNames:color=";
	final String display_colNames_font = "$display:colNames:font=";
	final String display_colNames_roation = "$display:colNames:roation=";

	final String display_cell_colorScheme = "$display:cell:colorScheme=";
	final String display_cell_shape = "$display:cells:shape=";

	final String display_annotation_font = "$display:annotation:font=";
	final String display_rowAnnotation = "$display:rowAnnotation=1";
	final String display_colAnnotation = "$display:colAnnotation=1";
	final String display_rowAnnotationSize = "$display:rowAnnotationSize=";
	final String display_colAnnotationSize = "$display:colAnnotationSize=";
	final String display_rowAnnotations_input1 = "$display:rowAnnotation:input=";
	final String display_colAnnotations_input1 = "$display:colAnnotation:input=";

	final String layout_general = "$layout:general=";
	final String layout_rectanglar_rowClusterAllocation = "$layout:rectanglar:rowClusterAllocation=";
	final String layout_rectanglar_colClusterAllocation = "$layout:rectanglar:colClusterAllocation=";
	final String layout_rectanglar_cellWidth = "$layout:rectanglar:cellWidth=";
	final String layout_rectanglar_cellHeight = "$layout:rectanglar:cellHeight=";

	final String horizontalGaps = "$horizontalGaps=";
	final String veriticalGaps = "$veriticalGaps=";
	final String gapSize = "$gapSize=";

	int indexOf;

	public void readAndAssign(ParameterModel paraModel, DataModel dataModel, File file) {
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		readAndAssign(paraModel, dataModel, lines);
	}

	private void assignClusterHierCluLinkageMethod(String line, ClusterParameter clusterPara) {
		int index = cluster_hierarchicalCluster_linkageMethod.length();
		String str = line.substring(index);

		// "Average", "Complete", "Single"
		// final String persCor = "Average";
		final String complete = "Complete";
		final String single = "Single";

		switch (str) {
		case complete:
			clusterPara.setClusterLinkageType(2);
			break;
		case single:
			clusterPara.setClusterLinkageType(1);
			break;
		default:
			clusterPara.setClusterLinkageType(0);
			break;
		}

	}

	private void assignClusterHierCluDistMetric(String line, ClusterParameter clusterPara) {
		int index = cluster_hierarchicalCluster_distMetric.length();
		String str = line.substring(index);

		// final String dotProduct = "average dot product";
		final String persCor = "pearson correlation";
		final String eucliDist = "euclidean distance";
		final String manhattanDist = "manhattan distance";
		final String spearRankCor = "spearman rank correlation";

		switch (str) {
		case persCor:
			clusterPara.setPairwiseDistance(new PearsonsCor());
			break;
		case eucliDist:
			clusterPara.setPairwiseDistance(new EuclideanDist());
			break;
		case manhattanDist:
			clusterPara.setPairwiseDistance(new ManhattanDist());
			break;
		case spearRankCor:
			clusterPara.setPairwiseDistance(new SpearmansCor());
			break;
		default:
			clusterPara.setPairwiseDistance(new AvgDotProduct());
			break;
		}
	}

	private void assignClusterGeneral(String line, ParameterModel paraModel, DataModel dataModel) {
		final String col = "col";
		final String row = "row";
		final String both = "both";
		// final String nuString = "null";

		int index = cluster_general.length();
		String str = line.substring(index);
		boolean ifPaintCol = false;
		boolean ifPaintRow = false;

		switch (str) {
		case col:
			ifPaintCol = true;
			break;
		case row:
			ifPaintRow = true;
			break;
		case both:
			ifPaintCol = true;
			ifPaintRow = true;
			break;
		default:
			break;
		}
		paraModel.setIfPaintRowTree(ifPaintRow);
		paraModel.setIfPaintColTree(ifPaintCol);
	}

	public void writeAndOutputParameters(ParameterModel paraModel, DataModel dataModel, File file) {

	}

	public void readAndAssign(ParameterModel paraModel, DataModel dataModel, List<String> inputContents) {
		Iterator<String> iterator = inputContents.iterator();

		while (iterator.hasNext()) {
			String line = iterator.next();
			if (line.startsWith("###")) {
				break;
			} else {
				continue;
			}
		}
		// This is the start of real parameters

		/**
		 * 哦！！ 我的天呐，这是什么时候写的代码啊，这简直带恶心了。。。。。 yudalang.
		 */
		while (iterator.hasNext()) {
			String line = iterator.next();

			if (line.startsWith("#")) {
				continue;
			} else {
				if (line.trim().length() == 0) {
					continue;
				}

				if ((indexOf = line.indexOf(dataTranformation_NoTrans)) != -1) {
					dataModel.transformData(1, 1, 1);
				} else if ((indexOf = line.indexOf(dataTranformation_logTrans)) != -1) {
					int index = dataTranformation_logTrans.length();
					int way = Integer.parseInt(line.substring(index, index + 1));
					String baseStr = line.substring(index + 2, index + 3);
					double base;
					if ("e".equalsIgnoreCase(baseStr)) {
						base = Math.E;
					} else {
						base = Double.parseDouble(baseStr);
					}
					dataModel.transformData(2, way, base);
				} else if ((indexOf = line.indexOf(dataTranformation_minMaxTrans)) != -1) {
					dataModel.transformData(4, 1, 1);
				} else if ((indexOf = line.indexOf(dataTranformation_coxBox)) != -1) {
					dataModel.transformData(5, 1, 1);
				} else if ((indexOf = line.indexOf(dataTranformation_zScoreTrans)) != -1) {
					dataModel.transformData(3, 1, 1);
				} else if ((indexOf = line.indexOf(cluster_general)) != -1) {
					assignClusterGeneral(line, paraModel, dataModel);
				} else if ((indexOf = line.indexOf(cluster_hierarchicalCluster)) != -1) {
					int index = cluster_hierarchicalCluster.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					if (parseInt == 0) {
						paraModel.setIfPaintRowTree(false);
						paraModel.setIfPaintColTree(false);
					}
				} else if ((indexOf = line.indexOf(cluster_hierarchicalCluster_distMetric)) != -1) {
					ClusterParameter clusterPara = paraModel.getClusterPara();
					assignClusterHierCluDistMetric(line, clusterPara);

				} else if ((indexOf = line.indexOf(cluster_hierarchicalCluster_keepOriginalOrder)) != -1) {
					ClusterParameter clusterPara = paraModel.getClusterPara();
				} else if ((indexOf = line.indexOf(cluster_hierarchicalCluster_linkageMethod)) != -1) {
					ClusterParameter clusterPara = paraModel.getClusterPara();
					assignClusterHierCluLinkageMethod(line, clusterPara);

				} else if ((indexOf = line.indexOf(cluster_line_color)) != -1) {
					ClusterParameter clusterPara = paraModel.getClusterPara();
					int index = cluster_line_color.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					int r = Integer.parseInt(split[0]);
					int g = Integer.parseInt(split[1]);
					int b = Integer.parseInt(split[2]);
					clusterPara.setColor(r, g, b);
				} else if ((indexOf = line.indexOf(cluster_line_width)) != -1) {
					ClusterParameter clusterPara = paraModel.getClusterPara();
					int index = cluster_line_width.length();
					String str = line.substring(index);
					float b = Float.parseFloat(str);
					clusterPara.setLineWidth(b);
				} else if ((indexOf = line.indexOf(display_dataValue_status)) != -1) {
					int index = display_dataValue_status.length();
					String str = line.substring(index);

					final String orignal = "Original";
					// final String noValue = "No value";
					final String present = "Present";

					switch (str) {
					case orignal:
						paraModel.setDataValueStatus(1);
						break;
					case present:
						paraModel.setDataValueStatus(2);
						break;
					default:
						paraModel.setDataValueStatus(0);
						break;
					}

				} else if ((indexOf = line.indexOf(display_dataValue_color)) != -1) {
					int index = display_dataValue_color.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					int r = Integer.parseInt(split[0]);
					int g = Integer.parseInt(split[1]);
					int b = Integer.parseInt(split[2]);
					paraModel.setDataValueColor(new Color(r, g, b));
				} else if ((indexOf = line.indexOf(display_dataValue_font)) != -1) {
					int index = display_dataValue_font.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					String fam = split[0];
					int style = Integer.parseInt(split[1]);
					int size = Integer.parseInt(split[2]);
					paraModel.setDataValueFont(new Font(fam, style, size));
				} else if ((indexOf = line.indexOf(display_border)) != -1) {
					int index = display_border.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					if (parseInt == 0) {
						paraModel.setIfShowBorder(false);
					} else {
						paraModel.setIfShowBorder(true);
					}
				} else if ((indexOf = line.indexOf(display_border_color)) != -1) {
					int index = display_border_color.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					int r = Integer.parseInt(split[0]);
					int g = Integer.parseInt(split[1]);
					int b = Integer.parseInt(split[2]);
					paraModel.setBorderColor(new Color(r, g, b));
				} else if ((indexOf = line.indexOf(display_dataValue_numOfdecimals)) != -1) {
					int index = display_dataValue_numOfdecimals.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setDecimalFormat(parseInt);
				} else if ((indexOf = line.indexOf(display_rowNames)) != -1) {
					int index = display_rowNames.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					if (parseInt == 0) {
						paraModel.setIfPaintRowNames(false);
					} else {
						paraModel.setIfPaintRowNames(true);
					}
				} else if ((indexOf = line.indexOf(display_rowNames_font)) != -1) {
					int index = display_rowNames_font.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					String fam = split[0];
					int style = Integer.parseInt(split[1]);
					int size = Integer.parseInt(split[2]);
					paraModel.setRowNameFont(new Font(fam, style, size));
				} else if ((indexOf = line.indexOf(display_rowNames_roation)) != -1) {
					int index = display_rowNames_roation.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setRowNamesRotaionAngle(parseInt);
				} else if ((indexOf = line.indexOf(display_colNames)) != -1) {
					int index = display_colNames.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					if (parseInt == 0) {
						paraModel.setIfPaintColNames(false);
					} else {
						paraModel.setIfPaintColNames(true);
					}
				} else if ((indexOf = line.indexOf(display_colNames_font)) != -1) {
					int index = display_colNames_font.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					String fam = split[0];
					int style = Integer.parseInt(split[1]);
					int size = Integer.parseInt(split[2]);
					paraModel.setColNameFont(new Font(fam, style, size));
				} else if ((indexOf = line.indexOf(display_colNames_roation)) != -1) {
					int index = display_colNames_roation.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setColNamesRotaionAngle(parseInt);
				} else if ((indexOf = line.indexOf(display_legend)) != -1) {
					int index = display_legend.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					if (parseInt == 0) {
						paraModel.setIfPaintMapLegend(false);
					} else {
						paraModel.setIfPaintMapLegend(true);
					}
				} else if ((indexOf = line.indexOf(display_legend_width)) != -1) {
					int index = display_legend_width.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setMapLegnedWidth(parseInt);
				} else if ((indexOf = line.indexOf(display_legend_height)) != -1) {
					int index = display_legend_height.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setMapLegendHeight(parseInt);
				} else if ((indexOf = line.indexOf(display_cell_colorScheme)) != -1) {
					int index = display_cell_colorScheme.length();
					String str = line.substring(index);
					final String green_black_red = "green-black-red";
					final String blue_yellow_black = "blue-yellow-black";
					final String blue_white_red = "blue-white-red";
					// final String deepblue_yelllow_red="deepblue-yelllow-red";
					final String white_red = "white-red";
					final String pheatmap = "pheatmap default scheme";
					final String customed_scheme = "customed scheme";

					float[] dist = new float[] { 0.0f, 0.5f, 1.0f };
					Color[] colors = new Color[] { new Color(44, 88, 175), new Color(240, 234, 174),
							new Color(210, 47, 42) };

					switch (str) {
					case green_black_red:
						dist = new float[] { 0.0f, 0.5f, 1.0f };
						colors = new Color[] { Color.green, Color.black, Color.red };
						break;
					case blue_yellow_black:
						dist = new float[] { 0.0f, 0.5f, 1.0f };
						colors = new Color[] { Color.blue, Color.yellow, Color.black };
						break;
					case blue_white_red:
						dist = new float[] { 0.0f, 0.5f, 1.0f };
						colors = new Color[] { Color.blue, Color.white, Color.red };
						break;
					case white_red:
						dist = new float[] { 0.0f, 1.0f };
						colors = new Color[] { Color.white, Color.red };
						break;
					case pheatmap:
						final int numOfColors = 7;
						dist = new float[numOfColors];
						double interval = 1.0 / numOfColors;
						for (int i = 0; i < numOfColors; i++) {
							dist[i] = (float) (interval * i);
						}

						colors = new Color[] { new Color(69, 117, 180), new Color(145, 191, 219),
								new Color(224, 243, 248), new Color(255, 255, 191), new Color(254, 224, 144),
								new Color(252, 141, 89), new Color(215, 48, 39) };
						break;
					case customed_scheme:

						break;

					default:
						break;
					}
					paraModel.getgColorHolder().setColorScheme(dist, colors);

				} else if ((indexOf = line.indexOf(display_cell_shape)) != -1) {
					int index = display_cell_shape.length();
					String str = line.substring(index);

					final String ellipse = "ellipse";
					final String triangle = "triangle";

					switch (str) {
					case ellipse:
						// paraModel.setMapShape(1);
						break;
					case triangle:
						// paraModel.setMapShape(2);
						break;
					default:
						// paraModel.setMapShape(0);
						break;
					}
				} else if ((indexOf = line.indexOf(display_rowAnnotation)) != -1) {
					paraModel.setIfPaintRowAnnotation(true);
					paraModel.setIfPaintAnnotationLegend(true);
				} else if ((indexOf = line.indexOf(display_colAnnotation)) != -1) {
					paraModel.setIfPaintColAnnotation(true);
					paraModel.setIfPaintAnnotationLegend(true);
				} else if ((indexOf = line.indexOf("fefe")) != -1) {

				} else if ((indexOf = line.indexOf(display_rowAnnotationSize)) != -1) {
					int index = display_rowAnnotationSize.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setRowAnnotationCaseWidth(parseInt);
				} else if ((indexOf = line.indexOf(display_colAnnotationSize)) != -1) {
					int index = display_colAnnotationSize.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setColAnnotationCaseWidth(parseInt);
				} else if ((indexOf = line.indexOf(horizontalGaps)) != -1) {
					int index = horizontalGaps.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					int length = split.length;
					int[] tt = new int[length];
					for (int i = 0; i < length; i++) {
						int parseInt = Integer.parseInt(split[i]);
						tt[i] = parseInt;
					}
					paraModel.sethGapLocations(tt);
				} else if ((indexOf = line.indexOf(veriticalGaps)) != -1) {
					int index = veriticalGaps.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					int length = split.length;
					int[] tt = new int[length];
					for (int i = 0; i < length; i++) {
						int parseInt = Integer.parseInt(split[i]);
						tt[i] = parseInt;
					}
					paraModel.setvGapLocations(tt);
				} else if ((indexOf = line.indexOf(gapSize)) != -1) {
					int index = gapSize.length();
					String str = line.substring(index);
					int parseInt = Integer.parseInt(str);
					paraModel.setGapSize(parseInt);
				} else if ((indexOf = line.indexOf(display_rowAnnotations_input1)) != -1) {
					int index = display_rowAnnotations_input1.length();
					String str = line.substring(index);
					String[] split = str.split("#");

					byte[] bytes = split[1].getBytes();
					for (int i = 0; i < bytes.length; i++) {
						bytes[i] = (byte) (bytes[i] - 49);
					}

					AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
					if (split.length == 3 || split.length == 4) {
						String[] splitRGB = split[2].split("\\|");
						int len = splitRGB.length;
						Color[] colors = new Color[len];
						for (int i = 0; i < len; i++) {
							String[] split3 = splitRGB[i].split(",");
							int r = Integer.parseInt(split3[0]);
							int g = Integer.parseInt(split3[1]);
							int b = Integer.parseInt(split3[2]);
							colors[i] = new Color(r, g, b);
						}
						if (split.length == 4) {
							String[] splitCaseName = split[3].split("\\|");
							annotaionParaObj.addARowAnnotation(split[0], colors, bytes, splitCaseName);
						} else {
							annotaionParaObj.addARowAnnotation(split[0], colors, bytes);
						}

					} else {
						annotaionParaObj.addARowAnnotation(split[0], bytes);
					}

				} else if ((indexOf = line.indexOf(display_annotation_font)) != -1) {
					int index = display_annotation_font.length();
					String str = line.substring(index);
					String[] split = str.split(",");
					String fam = split[0];
					int style = Integer.parseInt(split[1]);
					int size = Integer.parseInt(split[2]);
					AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
					annotaionParaObj.setFont(new Font(fam, style, size));
				} else if ((indexOf = line.indexOf(display_colAnnotations_input1)) != -1) {
					int index = display_colAnnotations_input1.length();
					String str = line.substring(index);

					String[] split = str.split("#");
					byte[] bytes = split[1].getBytes();
					for (int i = 0; i < bytes.length; i++) {
						bytes[i] = (byte) (bytes[i] - 49);
					}
					AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
					if (split.length == 3 || split.length == 4) {
						String[] splitRGB = split[2].split("\\|");
						int len = splitRGB.length;
						Color[] colors = new Color[len];
						for (int i = 0; i < len; i++) {
							String[] split3 = splitRGB[i].split(",");
							int r = Integer.parseInt(split3[0]);
							int g = Integer.parseInt(split3[1]);
							int b = Integer.parseInt(split3[2]);
							colors[i] = new Color(r, g, b);
						}

						if (split.length == 4) {
							String[] splitCaseName = split[3].split("\\|");
							annotaionParaObj.addAColAnnotation(split[0], colors, bytes, splitCaseName);
						} else {
							annotaionParaObj.addAColAnnotation(split[0], colors, bytes);
						}
					} else {
						annotaionParaObj.addAColAnnotation(split[0], bytes);
					}

				} else if ((indexOf = line.indexOf("gg")) != -1) {
					int index = display_rowAnnotations_input1.length();
					String str = line.substring(index);
					/**
					 * For additional usage!
					 */
				}
			}
		}

	}

}
