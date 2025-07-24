package module.remnant.datapanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.painter.BusyPainter;

import egps2.UnifiedAccessPoint;

public class DataFileLink extends JPanel{
	private static final long serialVersionUID = 608583872411325477L;
	private JXBusyLabel simpleBusyLabel;
	
//	private Border blackBorder = BorderFactory.createLineBorder(Color.lightGray);
	private Border blackBorder = BorderFactory.createEmptyBorder(10, 16, 10, 10);
	private boolean ifAlreadyRedBorder = false;
	private Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
	private JCheckBox chckbxToBeRemoved;
	private JLabel btnFileName;
	private JLabel btnFileSize;
	private JLabel btnTypeString;
	private File inputFile;
	
	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	
	private MyDataEventManager myDataEventManager;
	private String typeString;

	/**
	 * Create the panel.
	 */
	public DataFileLink() {
		Dimension size = new Dimension(230, 120);
		setPreferredSize(size);
		setMaximumSize(size);
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ifAlreadyRedBorder) {
					return;
				}
				myDataEventManager.letLeftClearRedBorderAndUpdateMiddleInformation(inputFile);
				setRedBorder();
				repaint();
			}
		};
		addMouseListener(mouseAdapter);
        setBorder(blackBorder);
        setFocusable(true);
        
		setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		
		chckbxToBeRemoved = new JCheckBox("To be removed");
		chckbxToBeRemoved.setFont(defaultFont);
		topPanel.add(chckbxToBeRemoved,BorderLayout.WEST);
		simpleBusyLabel = createBusyLabel();
		simpleBusyLabel.setVisible(false);
		topPanel.add(simpleBusyLabel,BorderLayout.EAST);
		
		JPanel centerPanel = new JPanel();
		add(centerPanel,BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblName = new JLabel("Name : ");
		lblName.setFont(defaultFont);
		centerPanel.add(lblName);
		
		btnFileName = new JLabel("New label");
		btnFileName.setFont(defaultFont);
		btnFileName.addMouseListener(mouseAdapter);
		centerPanel.add(btnFileName);
		
		JLabel lblSize = new JLabel("Size   :");
		lblSize.setFont(defaultFont);
		centerPanel.add(lblSize);
		
		btnFileSize = new JLabel("New label");
		btnFileSize.setFont(defaultFont);
		btnFileSize.addMouseListener(mouseAdapter);
		centerPanel.add(btnFileSize);
		
		
		JLabel lblType = new JLabel("Type  : ");
		lblType.setFont(defaultFont);
		centerPanel.add(lblType);
		
		btnTypeString = new JLabel("New label");
		btnTypeString.setFont(defaultFont);
		btnTypeString.addMouseListener(mouseAdapter);
		centerPanel.add(btnTypeString);
		
	}
	
	protected void setRedBorder() {
		setBorder(redBorder);
		ifAlreadyRedBorder = true;
	}

	protected void setBlackBorder() {
		setBorder(blackBorder);
		ifAlreadyRedBorder = false;
	}

	public void setMyDataEventManager(MyDataEventManager myDataEventManager) {
		this.myDataEventManager = myDataEventManager;
	}
	
	public JXBusyLabel createBusyLabel(){
		  // this will not work in the 0.9.1 release of 
		  // SwingX (need later builds)
		 
//
//		  JXBusyLabel label = new JXBusyLabel(new Dimension(38, 38));
//		  BusyPainter painter = new BusyPainter(
//		      new Rectangle2D.Float(0, 0, 8.0f, 8.0f),
//		      new Rectangle2D.Float(5.5f, 5.5f, 27.0f, 27.0f));
//		  painter.setTrailLength(4);
//		  painter.setPoints(8);
//		  painter.setFrame(-1);
//		 
//
//		  painter.setBaseColor(Color.blue);
//		  painter.setHighlightColor(Color.orange);
//		 
//
//		  label.setPreferredSize(new Dimension(38, 38));
//		  label.setIcon(new EmptyIcon(38, 38));
//		  label.setBusyPainter(painter);
//		 
//
//		  label.setToolTipText("complex busy label");
		 

		  JXBusyLabel label = new JXBusyLabel();
		  label.setToolTipText("simple busy label");
		  label.setBackground(Color.blue);
		  
		  BusyPainter painter = new BusyPainter();
		  painter.setHighlightColor(Color.blue);
		// painter.setTrailLength(5);
		// painter.setPoints(31);
		// painter.setFrame(1);
	         
		  label.setBusyPainter(painter);
		  
		  return label;
		}
	
		public void setInputFile(File file) {
			this.inputFile = file;
			String name = file.getName();
			btnFileName.setText(name);
			btnFileName.setToolTipText("Name: "+name);
		}
		
		public File getInputFile() {
			return inputFile;
		}
		
		public void setTypeString(String str) {
			btnTypeString.setText(str);
			typeString = "Type: " + str;
			btnTypeString.setToolTipText(typeString);
		}
		
		public void setFileSize(String str) {
			btnFileSize.setText(str);
			btnFileSize.setToolTipText("Size: " + str);;
		}
		
		public boolean ifSelected() {
			return chckbxToBeRemoved.isSelected();
		}
		
		public void setSelected(boolean b) {
			chckbxToBeRemoved.setSelected(b);
		}
		
		public void setCallBackMethodForCheckbox(Runnable method) {
			chckbxToBeRemoved.addChangeListener(e ->{
				SwingUtilities.invokeLater(method);
			});
		}
		
		public void startBusyStatus() {
			SwingUtilities.invokeLater(() ->{
				simpleBusyLabel.setBusy(true);
				simpleBusyLabel.setVisible(true);
				btnTypeString.setText("indexing...");
				simpleBusyLabel.setToolTipText("Creating config file...");
			});
		}
		
	public void stopBusyStatus() {
		SwingUtilities.invokeLater(() -> {
			simpleBusyLabel.setBusy(false);
			simpleBusyLabel.setVisible(false);
			btnTypeString.setText(typeString);
		});
	}

}
