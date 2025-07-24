package module.remnant.datapanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import module.remnant.datapanel.informationArea.AbstactInformationArea;

public class MiddleInformationArea extends JPanel {
	
	private AbstactInformationArea informationArea;

	public MiddleInformationArea() {
		setLayout(new BorderLayout());
		setBorder(new MyBevelBorder(BevelBorder.LOWERED));
		setBackground(Color.white);
	}

	public IDataInformation getDataInfoamtion() {
		return informationArea;
	}

	public void addNewInformationArea(AbstactInformationArea informationArea) {
		this.informationArea = informationArea;
		
		if (getComponentCount() > 0) {
			removeAll();
		}
		
		add(informationArea,BorderLayout.CENTER);
	}

	public void loadingInformation(File inputFile) {
		if (informationArea != null) {
			informationArea.loadingInformation(inputFile);
		}
	}

}
