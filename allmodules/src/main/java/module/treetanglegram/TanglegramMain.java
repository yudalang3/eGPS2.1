package module.treetanglegram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import egps2.frame.ModuleFace;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class TanglegramMain extends ModuleFace {

	private final TanglegramController controller;
	private JSplitPane jSplitPane;

	private JPanel rightJPanel;

	protected TanglegramMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		controller = new TanglegramController(this);

		setLayout(new BorderLayout());

		jSplitPane = new JSplitPane();

		SimpleLeftControlPanel simpleLeftControlPanel = new SimpleLeftControlPanel(controller);


		jSplitPane.setLeftComponent(simpleLeftControlPanel);
		
		JPanel rightJPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

			}
		};
		rightJPanel.setBackground(Color.white);
		setRightJPanel(rightJPanel);
		
		add(jSplitPane, BorderLayout.CENTER);

	}
	
	public void setRightJPanel(JPanel rightJPanel) {
		this.rightJPanel = rightJPanel;
		jSplitPane.setRightComponent(rightJPanel);
	}
	
	public JPanel getRightJPanel() {
		return rightJPanel;
	}

	public TanglegramController getController() {
		return controller;
	}

	public Dimension getPaintingPanelDim() {
		return rightJPanel.getSize();
	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public void changeToThisTab() {
		
	}


	@Override
	public boolean canImport() {
		return false;
	}

	@Override
	public void importData() {
		
	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {
		
	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Compare the trees" };
	}

	@Override
	protected void initializeGraphics() {
		
	}

	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The data is loading from the import dialog.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The functionality is powered by the eGPS software.";
			}
		};
		return iInformation;
	}

}
