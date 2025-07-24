package module.remnant.mafoperator;

import javax.swing.SwingUtilities;

import module.remnant.datapanel.DefaultPanel;



public class Method4MAFOperator  {


	public void turnToModule() {
		SwingUtilities.invokeLater(() -> {
			DefaultPanel component = new DefaultPanel();
		});

	}

	public String getModuleTabName() {
		return "MAF Operator";
	}

}
