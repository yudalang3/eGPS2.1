package operator.sequences;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.InputMismatchException;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import egps2.panels.dialog.SwingDialog;
import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;
import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class OneSeqOperatorPanel extends ModuleFace {
	private JTextArea outputTextArea;
	private JTextArea inputTextArea;

	protected OneSeqOperatorPanel(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new GridLayout(0, 1, 0, 10));

		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();
		Font defaultFont = launchProperty.getDefaultFont();
		Font defaultTitleFont = launchProperty.getDefaultTitleFont();

		inputTextArea = new JTextArea();
		inputTextArea.setFont(defaultFont);
		inputTextArea.setLineWrap(true);

		add(new JScrollPane(inputTextArea));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		DoSthActionsCollections doSthActionsCollections = new DoSthActionsCollections();
		{

			Function<String, String> uniqueSetBySemicolonAction = doSthActionsCollections.getSequenceLengthAction();
			JButton jButton = new JButton("Get length");
			jButton.addActionListener(e -> {
				String inputContent = getInputContent();
				String apply = uniqueSetBySemicolonAction.apply(inputContent);
				outputTextArea.setText("The text length is:\n" +apply);
			});
			jButton.setFont(defaultTitleFont);
			panel.add(jButton);
		}
		{
			Function<String, String> uniqueSetBySemicolonAction = doSthActionsCollections.getReverseSequenceAction();
			JButton jButton = new JButton("Reverse string");
			jButton.addActionListener(e -> {
				String inputContent = getInputContent();
				String apply = uniqueSetBySemicolonAction.apply(inputContent);
				outputTextArea.setText(apply);
			});
			jButton.setFont(defaultTitleFont);
			panel.add(jButton);
		}


		{
			Function<String, String> uniqueSetBySemicolonAction = doSthActionsCollections.getReverseComplementSequence4DNAAction();
			JButton jButton = new JButton("Reverse complement string for DNA");
			jButton.addActionListener(e -> {
				String inputContent = getInputContent();
				String apply = uniqueSetBySemicolonAction.apply(inputContent);
				outputTextArea.setText(apply);
			});
			jButton.setFont(defaultTitleFont);
			panel.add(jButton);
		}

		{
			Function<String, String> uniqueSetBySemicolonAction = doSthActionsCollections.getSortedUniqueSetBySemicolonAction();
			JButton jButton = new JButton("Sorted unique Set By Semicolon");
			jButton.addActionListener(e -> {
				String inputContent = getInputContent();
				String apply = uniqueSetBySemicolonAction.apply(inputContent);
				outputTextArea.setText(apply);
			});
			jButton.setFont(defaultTitleFont);
			panel.add(jButton);
		}

		outputTextArea = new JTextArea();
		outputTextArea.setFont(defaultFont);
		outputTextArea.setLineWrap(true);
		add(new JScrollPane(outputTextArea));

	}

	private String getInputContent() {
		String text = inputTextArea.getText();
		// Clear the output text area
		outputTextArea.setText("");
		if (text.isEmpty()) {
			SwingDialog.showInfoMSGDialog("No content", "Please input content first.");
			throw new InputMismatchException("Please input content first.");
		}
		return text;
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
	protected void initializeGraphics() {

	}

}
