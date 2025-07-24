package module.heatmap.eheatmap.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.tuple.Pair;

import egps2.panels.dialog.AllocatorPanel;
import egps2.panels.dialog.ColorCapture;
import egps2.panels.dialog.EGPSColorChooser;
import egps2.panels.dialog.GradientPanel;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.model.ParameterModel;

@SuppressWarnings("serial")
public class GradientColorChooserWorkPanel extends EGPSColorChooser implements ChangeListener {

	protected HeatmapController heamapCont;
	protected EHeatmapGradientColorChooser jDialog;

	protected AllocatorPanel allocator;
	protected GradientPanel gradientPanel;

	public GradientColorChooserWorkPanel(EHeatmapGradientColorChooser jDialog, HeatmapController heamapCont,
			float[] dist, Color[] colors) {
		this.jDialog = jDialog;
		this.heamapCont = heamapCont;
		this.initializedDist = dist;
		this.initializedColors = colors;

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(intalnalWidth, 650));

		setPreviewAndAllocator();
		setColorChooser();
		setButtomButtons();
	}

	public GradientColorChooserWorkPanel(EHeatmapGradientColorChooser jDialog, HeatmapController heamapCont) {
		this(jDialog, heamapCont, new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { Color.blue, Color.white, Color.red });
	}

	public GradientColorChooserWorkPanel() {
		this(null, null, new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { Color.blue, Color.white, Color.red });
	}

	protected void setButtomButtons() {
		Border eBorder = BorderFactory.createEmptyBorder(10, 10, 10, 20);

		JPanel jPanel = new JPanel();
		jPanel.setBorder(eBorder);
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));

		JButton jButton = new JButton("Screen color picker");
		jButton.setFont(defauotFont);
		jButton.addActionListener((e) -> {
			ColorCapture colorCapture = new ColorCapture(this);

			SwingUtilities.invokeLater(() -> {
				colorCapture.run();
				// new Thread(colorCapture).start();
			});
		});

		jPanel.add(jButton);
		pickedColorJLabel = new JTextField();
		pickedColorJLabel.setPreferredSize(new Dimension(50, 20));
		pickedColorJLabel.setSize(50, 20);

		pickedColorJLabel.setBackground(Color.white);
		pickedColorJLabel.setEditable(false);
		jPanel.add(pickedColorJLabel);

		jPanel.add(Box.createHorizontalGlue());
		JButton jButton2 = new JButton("OK");
		jButton2.setFont(defauotFont);
		jButton2.addActionListener((e) -> {

			ParameterModel paraModel = heamapCont.getParaModel();
			float[] distances = allocator.getDistances();
			Color[] colors = allocator.getColors();
			
			paraModel.setCustomizedColorScheme(Pair.of(distances, colors));
			
			paraModel.getgColorHolder().setColorScheme(distances, colors);
			heamapCont.weakestRefreshHeatmap();
			
			if (calBackInstance != null) {
				calBackInstance.doAfterCorrectClick();
			}
			
			jDialog.dispose();
		});
		jPanel.add(jButton2);
		JButton jButton3 = new JButton("Cancel");
		jButton3.setFont(defauotFont);
		jButton3.addActionListener((e) -> {
			jDialog.dispose();
		});
		jPanel.add(jButton3);
		add(jPanel, BorderLayout.SOUTH);
	}

	private void setPreviewAndAllocator() {
		JPanel bannerPanel = new JPanel(new BorderLayout());

		TitledBorder titledBorder = BorderFactory.createTitledBorder("Gradient colors preview");
		titledBorder.setTitleFont(titleFont);
		gradientPanel = new GradientPanel(initializedDist, initializedColors);
		gradientPanel.setBorder(titledBorder);
		gradientPanel.setPreferredSize(new Dimension(intalnalWidth, 150));

		bannerPanel.add(gradientPanel, BorderLayout.NORTH);
		bannerPanel.setPreferredSize(new Dimension(intalnalWidth, 300));

		allocator = new AllocatorPanel(this, initializedColors);
		TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Color allocator");
		titledBorder2.setTitleFont(titleFont);
		allocator.setBorder(titledBorder2);
		// allocator.setSize(new Dimension(intalnalWidth, 20));
		bannerPanel.add(allocator, BorderLayout.CENTER);

		add(bannerPanel, BorderLayout.NORTH);

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Color newColor = tcc.getColor();
		colorChangedAgain(newColor);
	}

	@Override
	public void colorChangedAgain(Color c) {
		allocator.setSelectedButtonColor(c);
		super.colorChangedAgain(c);
	}

	@Override
	public void updateAll() {
		gradientPanel.reSetSchame(allocator.getDistances(), allocator.getColors());
		updateUI();
	}

}
