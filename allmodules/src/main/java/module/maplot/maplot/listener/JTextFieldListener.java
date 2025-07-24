package module.maplot.maplot.listener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import module.maplot.MainFace;

public class JTextFieldListener implements DocumentListener {

	private MainFace plotMain;

	public JTextFieldListener(MainFace plotMain) {
		this.plotMain = plotMain;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changedUpdate(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changedUpdate(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		plotMain.getRightTabbedPanel().repaint();
	}

}
