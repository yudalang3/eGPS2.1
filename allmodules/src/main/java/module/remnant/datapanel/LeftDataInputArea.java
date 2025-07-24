package module.remnant.datapanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jidesoft.utils.SystemInfo;

import module.remnant.datapanel.data.IDataCenter;
import egps2.UnifiedAccessPoint;


public class LeftDataInputArea extends JPanel {
	private static final long serialVersionUID = 905505083978342607L;
	
	private JButton btnSelectAll;
	private JButton btnDeleteSelected;
	private JPanel fileLinkContainers;
	private boolean ifNextClikBeSelectedAll = true;
	
	DecimalFormat dFormatForFileSize = new DecimalFormat("#.00");

	private MyDataEventManager myDataEventManager;
	private JScrollPane scrollPane;

	/**
	 * Create the panel.
	 */
	public LeftDataInputArea() {
		setMaximumSize(new Dimension(300, 2000));
		setBackground(Color.WHITE);
		
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		btnSelectAll = new JButton("Select All");
		btnSelectAll.setFont(globalFont);
		btnSelectAll.setEnabled(false);
		btnSelectAll.addActionListener(e ->{
			allFileLinksToBe(ifNextClikBeSelectedAll);
			ifNextClikBeSelectedAll = !ifNextClikBeSelectedAll;
			
			if (ifNextClikBeSelectedAll) {
				btnSelectAll.setText("Select All");
			}else {
				btnSelectAll.setText("Unselect All");
			}
		});
		
		btnSelectAll.setPreferredSize(new Dimension(102, 35));
		
		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setFont(globalFont);
		btnDeleteSelected.setEnabled(false);
		btnDeleteSelected.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				removeSelectedFiles();
				refreshUI();
			}
		});
		btnDeleteSelected.setPreferredSize(new Dimension(140, 35));
		setLayout(new BorderLayout(0, 0));
		
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LeftDataInputArea.class.getResource("/resources/images/DropFilesHere.png")));
		add(lblNewLabel,BorderLayout.NORTH);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout(0, 0));
		
		jPanel.add(btnSelectAll,BorderLayout.WEST);
		jPanel.add(btnDeleteSelected,BorderLayout.EAST);
		add(jPanel,BorderLayout.SOUTH);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
		
		fileLinkContainers = new JPanel();
		fileLinkContainers.setBackground(Color.WHITE);
		scrollPane.setViewportView(fileLinkContainers);
		fileLinkContainers.setLayout(new VerticalLayout());
	}

	/**
	 * ydl: 实现 select all 与 un select all方法
	 * 注意实现了之后，改变全局的状态: {@link #ifNextClikBeSelectedAll}
	 * @param b
	 */
	private void allFileLinksToBe(boolean b) {
		Component[] components = fileLinkContainers.getComponents();
		for (Component component : components) {
			DataFileLink dLink = (DataFileLink) component;
			dLink.setSelected(b);
		}
	}
	
	
	public int getNumberOfFileLinks() {
		return fileLinkContainers.getComponentCount();
	}
	
	public void addValidateFiles(List<File> validataFiles) {
		IDataCenter dataCenter = myDataEventManager.getDataCenter();
		String strToShow = dataCenter.getDataFormatNameFromDataFormat(dataCenter.getCurrentDataFormat()) + " format";
		
		for (File file : validataFiles) {
			/**
			 * ydl：注意这些setter 需要一一注入，否则会出现问题！
			 */
			DataFileLink dataFileLink = new DataFileLink();
			dataFileLink.setInputFile(file);
			dataFileLink.setCallBackMethodForCheckbox( () -> refreshTwoButtonStatusInButtom() );
			
			long num = file.length();
			if (num < 1024) {
				dataFileLink.setFileSize(dFormatForFileSize.format((double) num) + "B");
			} else if (num < 1048576) {
				dataFileLink.setFileSize(dFormatForFileSize.format((double) num / 1024) + "KB");
			} else if (num < 1073741824) {
				dataFileLink.setFileSize(dFormatForFileSize.format((double) num / 1048576) + "MB");
			} else {
				dataFileLink.setFileSize(dFormatForFileSize.format((double) num / 1073741824) + "GB");
			}
			
			
			dataFileLink.setTypeString(strToShow);
			dataFileLink.setMyDataEventManager(myDataEventManager);
			
			dataFileLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
			dataFileLink.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					if (SystemInfo.isMacOSX()) {
						return;
					}
					if (event.getClickCount() == 2) {
						try {
							Runtime.getRuntime().exec(
							        "rundll32 SHELL32.DLL,ShellExec_RunDLL " +
							        "Explorer.exe /select," + file.getAbsolutePath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
			});
			
			fileLinkContainers.add(dataFileLink);
		}
		refreshTwoButtonStatusInButtom();
		refreshUI();
	}
	
	private void refreshUI() {
		scrollPane.updateUI();
	}
	
	private void removeSelectedFiles() {
		List<File> filesToRemove = new ArrayList<File>();
		
		Component[] components = fileLinkContainers.getComponents();
		for (Component component : components) {
			DataFileLink dLink = (DataFileLink) component;
			if (dLink.ifSelected()) {
				fileLinkContainers.remove(component);
				filesToRemove.add(dLink.getInputFile());
			}
		}
		
		myDataEventManager.removeFiles(filesToRemove);
		refreshTwoButtonStatusInButtom();
	}
	
	void refreshTwoButtonStatusInButtom() {
		boolean ifhasFileSelected = false;
		
		Component[] components = fileLinkContainers.getComponents();
		for (Component component : components) {
			DataFileLink dLink = (DataFileLink) component;
			if (dLink.ifSelected()) {
				ifhasFileSelected = true;
				break;
			}
		}
		btnDeleteSelected.setEnabled(ifhasFileSelected);
		btnSelectAll.setEnabled(components.length != 0 );
	}

	public void setDataEventManager(MyDataEventManager myDataEventManager) {
		this.myDataEventManager = myDataEventManager;
	}

	public void letFirstButtonToBeSelectAll() {
		btnSelectAll.setText("Select All");
		ifNextClikBeSelectedAll = true;
	}
	
	void letLeftStartBusyStatus(int[] indexes) {
		Component[] components = fileLinkContainers.getComponents();
		int lenMiusOne = components.length - 1;
		for (int i : indexes) {
			if (i > lenMiusOne) {
				continue;
			}
			DataFileLink dLink = (DataFileLink) components[i];
			dLink.startBusyStatus();
		}
	}
	void letLeftBusyStatusShutDown(int[] indexes) {
		Component[] components = fileLinkContainers.getComponents();
		int lenMiusOne = components.length - 1;
		for (int i : indexes) {
			if (i > lenMiusOne) {
				continue;
			}
			DataFileLink dLink = (DataFileLink) components[i];
			dLink.stopBusyStatus();
		}
	}

	public void clearRedBorder() {
		Component[] components = fileLinkContainers.getComponents();
		for (Component component : components) {
			DataFileLink dLink = (DataFileLink) component;
			dLink.setBlackBorder();
		}
	}
	
	
}
