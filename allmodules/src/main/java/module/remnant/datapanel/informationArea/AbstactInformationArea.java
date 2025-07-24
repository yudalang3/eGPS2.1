package module.remnant.datapanel.informationArea;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import module.remnant.datapanel.IDataInformation;

public abstract class AbstactInformationArea extends JPanel implements IDataInformation{

	private static final long serialVersionUID = -7577205206786749011L;
	private AbstactInformationArea informationArea;
	
	public AbstactInformationArea() {
		setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		setBackground(Color.white);
	}
	public void addNewInformationArea(AbstactInformationArea informationArea) {
		if (getComponentCount() != 0) {
			removeAll();
		}
		
		add(informationArea,BorderLayout.CENTER);
		this.informationArea = informationArea;
	}

}
