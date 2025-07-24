package module.evolview.gfamily.work.gui.tree;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import egps2.UnifiedAccessPoint;
import module.evoltre.mutation.IMutation;
import module.evoltre.mutation.MutationOperator;
import egps2.frame.gui.dialog.DialogMoreInfoGenerator;

@SuppressWarnings("serial")
public class TreeDialogMoreInfoGenerator extends DialogMoreInfoGenerator {

	public TreeDialogMoreInfoGenerator(List<String> contentLists, List<IMutation> allMutationsFromRoot) {
		super(UnifiedAccessPoint.getInstanceFrame(), true);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(getContentPanel(contentLists), BorderLayout.CENTER);

		JPanel buttonJPanel = new JPanel();
		buttonJPanel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
		buttonJPanel.setLayout(new BoxLayout(buttonJPanel, BoxLayout.X_AXIS));
		JButton oKButton = new JButton("OK");
		buttonJPanel.add(oKButton);
		contentPane.add(buttonJPanel, BorderLayout.SOUTH);

		buttonJPanel.add(Box.createHorizontalGlue());
		JCheckBox chckbxNewCheckBox = new JCheckBox("Detailed mutation");
		buttonJPanel.add(chckbxNewCheckBox);

		// 需要移除一个
		contentLists.remove(contentLists.size() - 1);
		chckbxNewCheckBox.addActionListener(e -> {
			JCheckBox source = (JCheckBox) e.getSource();

			if (source.isSelected()) {

				sBuilder.setLength(0);
				for (String string : contentLists) {
					sBuilder.append(string).append("\n");
				}

				sBuilder.append(MutationOperator.mutationList2FullString(allMutationsFromRoot));
				jTextPane.setText(sBuilder.toString());

			} else {

				sBuilder.setLength(0);
				for (String string : contentLists) {
					sBuilder.append(string).append("\n");
				}

				sBuilder.append(MutationOperator.mutationList2AbbrString(allMutationsFromRoot));
				jTextPane.setText(sBuilder.toString());
			}
		});

		oKButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		setSize(400, 400);
		setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
	}

}
