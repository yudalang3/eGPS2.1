package module.evolview.gfamily.work.gui.tree.control;

import javax.swing.JPanel;

import module.evolview.gfamily.GeneFamilyController;

@SuppressWarnings("serial")
public abstract class CommonSubLayoutPanel extends JPanel {
	protected GeneFamilyController controller;


    public CommonSubLayoutPanel(GeneFamilyController controller) {
        this.controller = controller;
    }


    public void setController(GeneFamilyController controller) {
        this.controller = controller;
    }

}
