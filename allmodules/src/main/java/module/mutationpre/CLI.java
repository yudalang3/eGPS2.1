package module.mutationpre;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import module.mutationpre.gui.DrawProperties;
import module.mutationpre.gui.PaintingPanel;
import module.mutationpre.model.InputDataModel;
import module.mutationpre.model.PaintingCalculator;
import egps2.builtin.modules.voice.template.CommandLineArgsInterpreter4VOICE;

public class CLI extends CommandLineArgsInterpreter4VOICE {
	@Override
	protected void process() throws Exception {
		// Crate the properties first
		DrawProperties drawProperties = new DrawProperties();
		PaintingCalculator calculator = new PaintingCalculator();

		Dimension paintingPanelSize = new Dimension(width, height);

		List<String> readLines = FileUtils.readLines(new File(configFilePath), StandardCharsets.UTF_8);
		String[] array = readLines.toArray(new String[0]);

		InputDataModel example = new InputDataModel().configInputDataFromStringArray(array);
		calculator.calculate(example, drawProperties, paintingPanelSize);

		// Set the painting panel property and size first
		PaintingPanel paintingPanel = new PaintingPanel();
		paintingPanel.setProperty(drawProperties);
		paintingPanel.setSize(paintingPanelSize);

		// let the painting panel draw once first
		BufferedImage image = new BufferedImage(700, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = image.createGraphics();
		paintingPanel.paintComponent(graphics2D);

		savePaintingPanel2file(paintingPanel);

	}

	public static void main(String[] args) throws Exception {
		CLI cli = new CLI();
		cli.parseOptions(args);
	}

}
