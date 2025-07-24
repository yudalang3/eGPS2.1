package module.evolview.gfamily.work.gui.browser;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jidesoft.swing.JideSplitPane;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.gfamily.work.beans.TrackMultipleDomainBean;
import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.AlignmentWithDerivedStatistics;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;
import module.evolview.gfamily.work.model.AlignmentGetSequence;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 *
 * @author mhl
 * @Date Created on:2020-04-23 14:26
 */
@SuppressWarnings("serial")
public class BrowserPanel extends JPanel {
	protected int moveLocation;// 鼠标的位置x值

	protected TrackGeneStructure virusGeomeStructure;
	protected GeneDrawingLengthCursor geneDrawingLengthCursor;
	protected DrawIntermediatedProperties calculatorDataForViewer;

	protected boolean whetherDrawMouseTrackLine;
	
	// 存储所有track
	protected List<AbstractTrackPanel> tracks = new ArrayList<>();

	protected final GeneFamilyController controller;

	protected JideSplitPane contextPanel = null;

	protected RequiredGeneData geneData;
	protected GeneStructureInfo geneStructureInfo;
	
	private static final Stroke dashed = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 4 },
			0);
	
	public BrowserPanel(GeneFamilyController controller, RequiredGeneData geneData) {
		this.controller = controller;
		this.geneData = geneData;

		setLayout(new BorderLayout());

		add(new JScrollPane(getContextPanel()), BorderLayout.CENTER);
		setBackground(Color.white);

		intitialTracks();
	}

	public JPanel getContextPanel() {
		if (contextPanel == null) {
			contextPanel = new JideSplitPane(JideSplitPane.VERTICAL_SPLIT);
			
			MouseListernerBrowserMain ncov2019GenomeMainMouseListerner = new MouseListernerBrowserMain(this);
			contextPanel.addMouseMotionListener(ncov2019GenomeMainMouseListerner);
			contextPanel.addMouseListener(ncov2019GenomeMainMouseListerner);

		}
		return contextPanel;
	}

	/**
	 * 初始化默认track
	 *
	 * @Author: mhl
	 * @Date Created on: 2020-07-15 14:36
	 */
	protected void intitialTracks() {

		try {
			geneStructureInfo = geneData.getGeneStructComputerStruct();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		geneDrawingLengthCursor = new GeneDrawingLengthCursor(defaultFont);
		geneDrawingLengthCursor.setDrawStart(1);
		int geneLength = geneStructureInfo.geneLength;
		geneDrawingLengthCursor.setDrawEnd(geneLength + 1);
		geneDrawingLengthCursor.setMinValue(1);
		geneDrawingLengthCursor.setMaxValue(geneLength + 1);

		this.calculatorDataForViewer = new DrawIntermediatedProperties(geneDrawingLengthCursor);

		this.virusGeomeStructure = new TrackGeneStructure(this, geneStructureInfo);
		tracks.add(virusGeomeStructure);

		
		Optional<AlignmentGetSequence> alignmentSequence = geneData.getAlignmentSequence();
		if (alignmentSequence.isPresent()) {
			AlignmentWithDerivedStatistics alignmentStati = new AlignmentWithDerivedStatistics();
			alignmentStati.setSequence(alignmentSequence.get());
			
//			alignmentStati.setSimPlotData(GeneStaticsCalculator.getSimplotData(alignmentSequence.get(), 50, 200));
			TrackAlignment genomerAlignment = new TrackAlignment(this,alignmentStati);
			tracks.add(genomerAlignment);
			//相似性面板暂时去掉吧
//			TrackSeqSimilarity genomePolylinePlot = new TrackSeqSimilarity(this,alignmentStati);
//			tracks.add(genomePolylinePlot);
		}
		
		Optional<TrackMultipleDomainBean> keyDomains = geneData.getKeyDomains();

		if (keyDomains.isPresent()) {
			TrackKeyDomains elementStructure = new TrackKeyDomains(this, keyDomains.get());
			tracks.add(elementStructure);
		}

		// 这个allele freq的面板也先不要放进去
//		Optional<List<Double>> alleleFreqScores = geneData.getAlleleFreqScores();
//		if (alleleFreqScores.isPresent()) {
//			TrackAlleleFreq genomeBarPlot = new TrackAlleleFreq(this,alleleFreqScores.get());
//			tracks.add(genomeBarPlot);
//		}
		
		Optional<DrawingPropertyPrimerSet> primerSets = geneData.getPrimerSets();
		if (primerSets.isPresent()) {

			TrackPrimers genomePrimers = new TrackPrimers(this,primerSets.get(),alignmentSequence.get());
			tracks.add(genomePrimers);
		}
		
		initializeTrackGUI();
	}

	/**
	 * 刷新Tracks Panel界面
	 *
	 * @Author: mhl
	 * @Date Created on: 2020-07-15 14:36
	 */
	public void initializeTrackGUI() {
		
		int height2 = getHeight();
		int size = tracks.size();
		int height = 0;
		Color color = new Color(245, 245, 245);
		for (int indexTrack = 0; indexTrack < size; indexTrack++) {
			AbstractTrackPanel abstractTrack = tracks.get(indexTrack);
			Color trackBackground = indexTrack % 2 == 0 ? Color.white : color;
			abstractTrack.setBackground(trackBackground);
			contextPanel.addPane(abstractTrack);
			height += abstractTrack.getMaximumSize().getHeight();
			
		}
		contextPanel.setBackground(Color.white);

		Dimension preferredSize = new Dimension(getPreferredSize().width, height);
		contextPanel.setPreferredSize(preferredSize);
		
		
		// 很多让他自动调整的方式都失败了
//		contextPanel.setDividerLocations(new int[] {60,60,60});
//		for (AbstractTrackPanel abstractTrackPanel : tracks) {
//			contextPanel.addPane(abstractTrackPanel);
//		}
//		contextPanel.setBackground(Color.white);
//		contextPanel.setDividerLocation(0, 150);
	}


	@Override
	public void paint(Graphics g) {
		boolean reCalculator = calculatorDataForViewer.isReCalculator(getWidth(), getHeight());

		if (reCalculator) {
			initialGenomeBrowser();
		}
		
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		if (whetherDrawMouseTrackLine) {
			// 绘制鼠标跟踪虚线
			Stroke oldStroke = g2.getStroke();
			
			g2.setStroke(dashed);
			g2.setColor(Color.DARK_GRAY);
			g2.drawLine(moveLocation, virusGeomeStructure.getHeight(), moveLocation,
					getHeight() - LocationCalculator.BLINKTOPSPACELENGTH);
			g2.setStroke(oldStroke);
		}
		
//		System.out.println(getSize());

	}

	public void setInTheInterface(boolean isInTheInterface) {
		this.whetherDrawMouseTrackLine = isInTheInterface;
	}

	public void setMoveLocation(int moveLocation) {

		this.moveLocation = moveLocation;
	}

	public GeneDrawingLengthCursor getDrawProperties() {
		return geneDrawingLengthCursor;
	}

	public DrawIntermediatedProperties getCalculatorDataForViewer() {
		return calculatorDataForViewer;
	}

	public GeneFamilyController getController() {
		return controller;
	}

	public TrackAlleleFreq getGenomeBarPlot() {
		for (AbstractTrackPanel abstractTrackPanel : tracks) {
			
			if (abstractTrackPanel instanceof TrackAlleleFreq) {
				TrackAlleleFreq instance = (TrackAlleleFreq) abstractTrackPanel;
				return instance;
			}
		}
		return null;
	}

	public TrackSeqSimilarity getGenomePolylinePlot() {
		for (AbstractTrackPanel abstractTrackPanel : tracks) {
			if (abstractTrackPanel instanceof TrackSeqSimilarity) {
				TrackSeqSimilarity instance = (TrackSeqSimilarity) abstractTrackPanel;
				return instance;
			}
		}
		return null;

	}

	public TrackGeneStructure getVirusGeomeStructure() {
		return virusGeomeStructure;
	}

	public TrackKeyDomains getElementStructure() {
		for (AbstractTrackPanel abstractTrackPanel : tracks) {
			if (abstractTrackPanel instanceof TrackKeyDomains) {
				TrackKeyDomains instance = (TrackKeyDomains) abstractTrackPanel;
				return instance;
			}
		}
		return null;
	}

	public TrackAlignment getGenomerAlignment() {
		for (AbstractTrackPanel abstractTrackPanel : tracks) {
			if (abstractTrackPanel instanceof TrackAlignment) {
				TrackAlignment instance = (TrackAlignment) abstractTrackPanel;
				return instance;
			}
		}
		return null;
	}

	public TrackPrimers getGenomePrimers() {
		for (AbstractTrackPanel abstractTrackPanel : tracks) {
			if (abstractTrackPanel instanceof TrackPrimers) {
				TrackPrimers instance = (TrackPrimers) abstractTrackPanel;
				return instance;
			}
		}
		return null;
	}

	public List<AbstractTrackPanel> getTracks() {
		return tracks;
	}

	public void initialGenomeBrowser() {
		for (AbstractTrackPanel track : tracks) {
			track.initialize();
		}
	}

	public GeneDrawingLengthCursor getGeneDrawingLengthCursor() {
		return geneDrawingLengthCursor;
	}

	public GeneStructureInfo getGeneStructureInfo() {
		return geneStructureInfo;
	}
	
	public RequiredGeneData getGeneData() {
		return geneData;
	}

//	protected void calculateMousePos() {
//		drawProperties.setMousePos((int)(drawProperties.getDrawStart()+(moveLocation-LocationCalculator.BLINKLEFTSPACELENGTH)/(drawProperties.getDrawEnd()-drawProperties.getDrawStart())));
//	}
}
