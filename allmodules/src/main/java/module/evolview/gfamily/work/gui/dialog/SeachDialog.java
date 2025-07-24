package module.evolview.gfamily.work.gui.dialog;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;

public class SeachDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the dialog.
	 * 
	 * @param controller
	 */
	public SeachDialog(GeneFamilyController controller) {
		super(UnifiedAccessPoint.getInstanceFrame(), true);
		setBounds(100, 100, 450, 200);
		setLayout(new BorderLayout());
		SearchDialogContent comp = new SearchDialogContent(controller);
		add(comp, BorderLayout.CENTER);

		setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
	}

}
