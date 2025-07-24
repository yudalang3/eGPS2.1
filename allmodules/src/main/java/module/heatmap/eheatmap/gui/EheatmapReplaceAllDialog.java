package module.heatmap.eheatmap.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import egps2.utils.common.model.filefilter.OpenFilterCsv;
import egps2.utils.common.model.filefilter.OpenFilterTsv;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;

/**
* @author YFQ,YDL
* @date 2019-09-11 14:27:18
* @version 1.0
* <p>Description:</p>
*/
public class EheatmapReplaceAllDialog extends JDialog {

	private static final long serialVersionUID = 7520499775750910756L;
	
	private JPanel jContentPane = null;
	private JButton jButtonReplace;
	private Font font = new Font(Font.DIALOG,Font.PLAIN,12);

	private File inputFile;
	
	public EheatmapReplaceAllDialog() {
		super(UnifiedAccessPoint.getInstanceFrame(), "Replace OTU names", true);
		setSize(350, 150);
		setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
		setContentPane(getJContentPane());
		setResizable(false);
		setIconImage(EGPSShellIcons.get("eGPS_logo16x16.png").getImage());
	}

	/**  
	* @author YFQ
	* @date 2019-09-11 14:34:31
	* @parameter 
	* @description 
	* @return Container
	*/  
	private JPanel getJContentPane() {
		if(jContentPane == null) {
			
			jContentPane = new JPanel(null);
			
			JLabel jLabelInput = new JLabel("Input mapping file( tsv/csv ) :");
			jLabelInput.setFont(font);
			jLabelInput.setBounds(25, 10, 330, 25);
			jContentPane.add(jLabelInput);
			
			JTextField jTextField = new JTextField();
			jTextField.setBounds(25, 42, 200, 25);
			jTextField.setFont(font);
			jContentPane.add(jTextField);
			
			JButton jButtonOpen = new JButton("Open");
			jButtonOpen.setBounds(240, 42, 95, 25);
			jButtonOpen.setFont(font);
			jButtonOpen.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					Preferences pref = Preferences.userNodeForPackage(this.getClass());
					String lastPath = pref.get("lastPath", "");
						
					JFileChooser jfc = null;
					if (lastPath.length() > 0) {
					    jfc = new JFileChooser(lastPath);
					} else{
					    jfc = new JFileChooser();
					}
					
					jfc.setDialogTitle("Open File");
					jfc.setMultiSelectionEnabled(false);
					jfc.setAcceptAllFileFilterUsed(false);

					jfc.addChoosableFileFilter(new OpenFilterCsv());
					jfc.addChoosableFileFilter(new OpenFilterTsv());

					int result = jfc.showOpenDialog(UnifiedAccessPoint.getInstanceFrame());
					
					if (result == JFileChooser.APPROVE_OPTION) {
						File file = jfc.getSelectedFile();

						if (file != null) {
							SwingUtilities.invokeLater(()->{
								jTextField.setText(file.getName());
								jButtonReplace.setEnabled(true);
							} );
							inputFile = file;
							
							jfc.setCurrentDirectory(file.getParentFile());
							pref.put("lastPath",file.getParent());
						}else {
							if (jTextField.getText().length() == 0) {
								SwingUtilities.invokeLater(()->{
									jButtonReplace.setEnabled(false);
								} );
							}
						}
					}
				}
			});
			jContentPane.add(jButtonOpen);
			
			jButtonReplace = new JButton("Replace All");
			jButtonReplace.setBounds(240, 75, 95, 25);
			jButtonReplace.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (inputFile== null) {
						return;
					}

					Map<String, String> mapHM = new HashMap<String, String>();
					try (BufferedReader br = new BufferedReader(new FileReader(inputFile));) {
						String line = null;
						while ((line = br.readLine()) != null) {
							line = line.trim();
							String[] split = line.split("\\s+|,");
							mapHM.put(split[0], split[1]);
						}

					} catch (IOException io) {
						//e.printStackTrace();
					}
					
//					List<Node> leafs = ptp.getTreePro().getLeafs();
//					for (Node node : leafs) {
//						String oriLeafName = node.getLeafName();
//						String destName = mapHM.get(oriLeafName);
//						if (destName != null) {
//							node.setLeafName(destName);
//						}
//					}
					//setVisible(false);
					dispose();

					
				}
			});
			jButtonReplace.setFont(font);
			jButtonReplace.setEnabled(false);
			jContentPane.add(jButtonReplace);
		}
		
		return jContentPane;
	}
	
}
