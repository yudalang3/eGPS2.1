package module.multiseq.aligner;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import module.multiseq.aligner.gui.MafftGUIPanel;
import egps2.modulei.CreditBean;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

public class MultipleSeqAlignerMain extends ComputationalModuleFace {

	private static final long serialVersionUID = 6401202253955992441L;

	private static int alignmentViewIndex = 0;

	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();;

	protected JTabbedPane tabbedPane;

	private String[] info = new String[3];

	protected MultipleSeqAlignerMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		add(getTabbedPanel(), BorderLayout.CENTER);
	}
	
	protected void addTabbedPanelContents() {
		// tabbedPane.add("Muscle", new MusclePanel(this));
		tabbedPane.add("MAFFT", new MafftGUIPanel(this));
		//tabbedPane.add("ClustalW", new ClustalwGUIPanel(this));
	}

	private JTabbedPane getTabbedPanel() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setTabPlacement(SwingConstants.LEFT);
			tabbedPane.setFont(defaultFont);
			addTabbedPanelContents();
			
			info[0] = "MAFFT ";
			info[1] = "kazutaka et al.";
			info[2] = "https://mafft.cbrc.jp/alignment/software/";
			tabbedPane.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {

//					JTabbedPane source = (JTabbedPane) e.getSource();
//					int selectedIndex = source.getSelectedIndex();
//					if (selectedIndex == 1) {
//						info[0] = "ClustalW" ;
//						info[1] = "Higgins et al.";
//						info[2] = "http://www.clustal.org/clustal2/";
//						String[] authors = info[1].split(",");
//						statusBarPanel.setAuthors(info[0] + ":", authors, info[2]);
//						statusBarPanel.switchToAuthorsComponents();
//					} else {
//						info[0] = "MAFFT ";
//						info[1] = "kazutaka et al.";
//						info[2] = "https://mafft.cbrc.jp/alignment/software/";
//						String[] authors = info[1].split(",");
//						statusBarPanel.setAuthors(info[0] + ":", authors, info[2]);
//						statusBarPanel.switchToAuthorsComponents();
//					}
				}
			});

		}
		return tabbedPane;
	}


	public void openGesture() {

		tabbedPane.getSelectedComponent().setEnabled(false);
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	public void closeGesture() {
		tabbedPane.getSelectedComponent().setEnabled(true);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public int getAlignmentIndex() {
		return ++alignmentViewIndex;
	}

	@Override
	public boolean closeTab() {
		alignmentViewIndex = 0;
		return false;
	}

	public MultipleSeqAlignerMain getController() {
		return this;
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
		return new String[] { "Align multiple sequences" };
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
				return "The functionality is powered by the MAFFT program and eGPS software.<br> Please browser https://mafft.cbrc.jp/alignment/software/ for citation first.";
			}
		};
		return iInformation;
	}

	@Override
	public CreditBean getDevTeam() {
		CreditBean creditBean = new CreditBean();
		creditBean.setWebSite("https://mafft.cbrc.jp/alignment/software/");
		creditBean.setTeam("MAFFT Team");

		String evolGeneDevs = creditBean.getDevelopers();
		creditBean.setDevelopers("The MAFFT development team | " + evolGeneDevs);
		return creditBean;
	}
}
