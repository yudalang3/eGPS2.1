package module.evolview.genebrowser;

import java.awt.Font;
import java.io.IOException;
import java.util.Optional;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.gfamily.work.beans.TrackMultipleDomainBean;
import module.evolview.gfamily.work.gui.browser.BrowserPanel;
import module.evolview.gfamily.work.gui.browser.DrawIntermediatedProperties;
import module.evolview.gfamily.work.gui.browser.GeneDrawingLengthCursor;
import module.evolview.gfamily.work.gui.browser.TrackAlignment;
import module.evolview.gfamily.work.gui.browser.TrackGeneStructure;
import module.evolview.gfamily.work.gui.browser.TrackKeyDomains;
import module.evolview.gfamily.work.gui.browser.TrackPrimers;
import module.evolview.gfamily.work.gui.browser.draw.AlignmentWithDerivedStatistics;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.model.AlignmentGetSequence;

@SuppressWarnings("serial")
public class GBBrowserPanel extends BrowserPanel {

	public GBBrowserPanel(GeneFamilyController controller, RequiredGeneData geneData) {
		super(controller, geneData);
	}

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

			TrackAlignment genomerAlignment = new TrackAlignment(this, alignmentStati);
			tracks.add(genomerAlignment);
		}

		Optional<TrackMultipleDomainBean> keyDomains = geneData.getKeyDomains();

		if (keyDomains.isPresent()) {
			TrackMultipleDomainBean drawingPropertyElementModelItem = keyDomains.get();
			TrackKeyDomains elementStructure = new TrackKeyDomains(this, drawingPropertyElementModelItem);
			tracks.add(elementStructure);
		}

		Optional<DrawingPropertyPrimerSet> primerSets = geneData.getPrimerSets();
		if (primerSets.isPresent()) {

			TrackPrimers genomePrimers = new TrackPrimers(this, primerSets.get(), alignmentSequence.get());
			tracks.add(genomePrimers);
		}

		initializeTrackGUI();
	}

}
