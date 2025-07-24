package module.evolview.treebarplot.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.tuple.Pair;

import analysis.math.DoubleListUtils;
import module.evolknow.GeologicTimeScale;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator.LongestRoot2leafBean;
import module.evolview.phylotree.visualization.layout.RectangularLayout;
import graphic.engine.AxisTickCalculator;
import graphic.engine.colors.EGPSColors;
import graphic.engine.guicalculator.BlankArea;

@SuppressWarnings("serial")
public class PaintingPanel extends PhylogeneticTreePanel {
	
	private final AxisTickCalculator axisTickCalculator = new AxisTickCalculator();
	private final  Rectangle2D.Double rectDrawUtil = new Rectangle2D.Double();
	private final  GeologicTimeScale geologicTimeScale = new GeologicTimeScale();
	
	List<GraphicsNode> leaves;
	
	int rightBlank = 40;
	int tree2barPlotGap = 10;
	int tipLength = 6;
	int halfgapOfPlotedBar = 1;
	int treeToGeologicTimeScaleLegendGap = 45;
	int localGeologicTimeScaleLegendGapHeight = 100;
	
	List<String> titles = Arrays.asList("aging rate","mlres(%)","aw(g)","ml(year)");
	List<double[]> values;
	List<Color> colors4plotRegion = Arrays.asList(EGPSColors.getPredinedCellChatColors());
	

	public PaintingPanel(TreeLayoutProperties properties) {
		super(properties);
		
	}

	@Override
	protected void drawProcess(Graphics2D g2d) {
		super.drawProcess(g2d);
		g2d.setFont(layoutProp.getGlobalFont());
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int fontHeight = fontMetrics.getHeight();
		
		int numberOfPlotRegion = titles.size();
		
		int yGapLength = 0;
		
		if (layout instanceof RectangularLayout) {
			double eachYGapLength = ((RectangularLayout) layout).getEachYGapLength();
			yGapLength = (int) eachYGapLength;
		}
		int halfYGap = yGapLength / 2;
		List<GraphicsNode> leaves2 = getLeaves();
		
		int rightMostXAxis = 0;
		int minYaxisValue = 500000;
		for (GraphicsNode node : leaves2) {
			double xSelf = node.getXSelf();
			double ySelf = node.getYSelf();
			int xx = (int) xSelf;
			int yy = (int) ySelf;
			if (xx > rightMostXAxis) {
				rightMostXAxis = xx;
			}
			if (yy < minYaxisValue) {
				minYaxisValue = yy;
			}
		}
		
		BlankArea blankArea = layout.getBlankArea();
		int plotRegion_width = blankArea.getWorkHeight(getHeight()) + yGapLength;
		
		String maxLengthLeafName = layout.getMaxLengthLeafName();
		int maxLeafNameSpace = fontMetrics.stringWidth(maxLengthLeafName);
		int plotRegion_height = ( blankArea.getRight() - maxLeafNameSpace- rightBlank) / numberOfPlotRegion;
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(1f));
		
		g2d.setFont(layoutProp.getAxisFont());
		
		
		for (int i = 0; i < numberOfPlotRegion; i++) {
			int xx = rightMostXAxis  + maxLeafNameSpace + tree2barPlotGap + i * plotRegion_height ;
			// 绘制边框
			g2d.drawRect(xx, minYaxisValue - halfYGap, plotRegion_height, plotRegion_width);
			
			// 绘制 每个 plotRegion的 title
			String string = titles.get(i);
			int stringWidth = fontMetrics.stringWidth(string);
			int yAxis4tipStart = minYaxisValue - halfYGap + plotRegion_width;
			int titleYAxisValue = yAxis4tipStart + fontHeight + tipLength;
			titleYAxisValue += 20;
			g2d.drawString(string, xx + (plotRegion_height - stringWidth) / 2,  titleYAxisValue);
			
			// 绘制坐标轴刻度线
			axisTickCalculator.clear();
			double[] ds = values.get(i);
			Pair<Double, Double> minMax = DoubleListUtils.getMinMax(ds);
			Pair<Double, Double> of = Pair.of(0.0, minMax.getRight());
			axisTickCalculator.setMinAndMaxPair(of);
			axisTickCalculator.setWorkingSpace(plotRegion_height);
			axisTickCalculator.setWorkSpaceRatio(1f);
			axisTickCalculator.determineAxisTick();
			
			List<String> tickLabels = axisTickCalculator.getTickLabels();
			List<Integer> tickLocations = axisTickCalculator.getTickLocations();
			Iterator<String> iterator = tickLabels.iterator();
			for (Integer integer : tickLocations) {
				int x4paint = xx + integer;
				String next = iterator.next();
				
				int stringWidth1 = fontMetrics.stringWidth(next);
				g2d.drawString(next, x4paint - stringWidth1 / 2 , yAxis4tipStart + fontHeight + tipLength + 5);
				g2d.drawLine(x4paint, yAxis4tipStart, x4paint, yAxis4tipStart + tipLength);
			}
		}
		

		/**
		 * 绘制从leaf name到第一个bar plot的刻度线
		 */
		g2d.setColor(new Color(5,5,5,55));

		List<String> arrayOfStrings = new ArrayList<>();
		for (GraphicsNode graphicsNode : leaves2) {
			arrayOfStrings.add(graphicsNode.getName());
		}

		int leafTipXLocationInPlotRegion = rightMostXAxis  + maxLeafNameSpace + tree2barPlotGap ;
		for (GraphicsNode node : leaves2) {
//			double xSelf = node.getXSelf();
			double ySelf = node.getYSelf();
//			int xx = (int) xSelf;
			int yy = (int) ySelf;
			g2d.drawLine(leafTipXLocationInPlotRegion - 5 , yy, leafTipXLocationInPlotRegion, yy);
		}
		
		// 绘制bar
		for (int i = 0; i < numberOfPlotRegion; i++) {
			int xx = rightMostXAxis  + maxLeafNameSpace + tree2barPlotGap + i * plotRegion_height ;
			
			double[] ds = values.get(i);
			Color color = colors4plotRegion.get(i);
			double maxValue = DoubleStream.of(ds).max().orElse(Double.NaN);
			
			g2d.setColor(color);
			int index = 0;
			for (GraphicsNode node : leaves2) {
				double xSelf = node.getXSelf();
				double ySelf = node.getYSelf();
				int xOfNode = (int) xSelf;
				int yOfNode = (int) ySelf;
				
				double d = ds[index];
				int barHeight = (int) (d / maxValue * plotRegion_height);
				
				int halfWidth = halfYGap - halfgapOfPlotedBar;
				
				rectDrawUtil.setFrame(xx, yOfNode - halfWidth, barHeight, halfWidth + halfWidth);
				g2d.fill(rectDrawUtil);
				index ++;

			}
			
		}
		
		LongestRoot2leafBean maxLengthOfRoot2LeafBean = GraphicTreePropertyCalculator.getMaxLengthOfRoot2Leaf(rootNode);
		double maxLengthOfRoot2Leaf = maxLengthOfRoot2LeafBean.getLength();
		

		int bottomAxixHeight = treeToGeologicTimeScaleLegendGap;
		int localHeight = localGeologicTimeScaleLegendGapHeight;
		int localYAxis = getHeight() - blankArea.getBottom() + bottomAxixHeight;
		Rectangle rectangle = new Rectangle(
				blankArea.getLeft(), localYAxis, 
				blankArea.getWorkWidth(getWidth()), localHeight);
		geologicTimeScale.paintThe4legend(g2d, rectangle, (int) maxLengthOfRoot2Leaf);
//		g2d.drawRect(rightMostXAxis, minYaxisValue - halfYGap, maxLeafNameSpace, plotRegion_width);
//		g2d.drawString(maxLengthLeafName, 0, 10);
//		g2d.drawRect(blankArea.getLeft(), blankArea.getTop(), blankArea.getWorkWidth(getWidth()),
//				blankArea.getWorkHeight(getHeight()));
		
		g2d.dispose();
	}
	
	
	private List<GraphicsNode> getLeaves() {
		if (leaves == null) {
			leaves = getLayoutProperties().getLeaves();
		}
		return leaves;
	}

	public int getRightBlank() {
		return rightBlank;
	}

	public void setRightBlank(int rightBlank) {
		this.rightBlank = rightBlank;
	}

	public int getTree2barPlotGap() {
		return tree2barPlotGap;
	}

	public void setTree2barPlotGap(int tree2barPlotGap) {
		this.tree2barPlotGap = tree2barPlotGap;
	}

	public int getTipLength() {
		return tipLength;
	}

	public void setTipLength(int tipLength) {
		this.tipLength = tipLength;
	}

	public int getHalfgapOfPlotedBar() {
		return halfgapOfPlotedBar;
	}

	public void setHalfgapOfPlotedBar(int halfgapOfPlotedBar) {
		this.halfgapOfPlotedBar = halfgapOfPlotedBar;
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<double[]> getValues() {
		return values;
	}

	public void setValues(List<double[]> values) {
		this.values = values;
	}
	

}
