package module.pill.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import module.pill.gui.CtrlPathwayCompoentsPanel;
import module.pill.images.ImageUtils;

public class IntuitiveShapeCreator extends JDialog {

	private static final long serialVersionUID = 1L;
	private final DrawingPanelIntuiShapeCreator contentPanel = new DrawingPanelIntuiShapeCreator();
	private JTextField textField;
	private SkeletonMaker locationPicker;

	/**
	 * Create the dialog.
	 */
	public IntuitiveShapeCreator(Frame owner, boolean modal) {
		super(owner, "Intuitive Shape Creator", modal);

		setBounds(100, 100, 670, 592);

		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.setBackground(Color.WHITE);
		jPanel.setBorder(new EmptyBorder(5, 15, 5, 15));

		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(new FlowLayout());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			jPanel.add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Finished");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						IntuitiveShapeCreator.this.dispose();

						SwingUtilities.invokeLater(() -> {
							CtrlPathwayCompoentsPanel ctrlPathwayCompoentsPanel = locationPicker
									.getCtrlPathwayCompoentsPanel();
							String name = ctrlPathwayCompoentsPanel.defaultNodeShape.name;
							ctrlPathwayCompoentsPanel.loadTheCustomizedShapes(name);
							// 还需要赋值 currentNodeShape
							// 不赋值了用默认的
						});

					}
				});
				buttonPane.add(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						IntuitiveShapeCreator.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			jPanel.add(buttonPane, BorderLayout.NORTH);
			{

				{
					JButton btn_load = new JButton("Load image from clipboard");
					btn_load.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							SwingUtilities.invokeLater(() -> {
								contentPanel.loadImageFrom();
							});
						}
					});
					Icon icon = ImageUtils.getIcon("clipboard-solid.png");
					btn_load.setIcon(icon);
					btn_load.setRequestFocusEnabled(false);

					buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
					buttonPane.add(btn_load);
				}

				buttonPane.add(Box.createHorizontalGlue());

				{
					JButton btnNewButton = new JButton("Clear");
					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							contentPanel.clearAll();
						}
					});
					buttonPane.add(btnNewButton);
					Icon icon = ImageUtils.getIcon("eraser.png");
					;
					btnNewButton.setIcon(icon);
					btnNewButton.setRequestFocusEnabled(false);
				}

			}
			{
				JButton btnNewButton_1 = new JButton("Save and clean");
				btnNewButton_1.setToolTipText(
						"<html>After save the picture, <br>\r\nClick Finished button to refresh the control panel. <br>\r\nOtherwise, the cancel and x button will not refresh");
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new Thread(() -> {
							contentPanel.saveCurrentShape(textField.getText());
						}).start();
					}
				});
				{
					textField = new JTextField();
					textField.setHorizontalAlignment(SwingConstants.CENTER);
					buttonPane.add(textField);
					textField.setColumns(10);
					textField.setMaximumSize(new Dimension(250, 140));
				}
				buttonPane.add(btnNewButton_1);
				Icon icon = ImageUtils.getIcon("save.png");
				btnNewButton_1.setIcon(icon);
				btnNewButton_1.setRequestFocusEnabled(false);
			}
		}

		jPanel.add(contentPanel, BorderLayout.CENTER);
	}

	public void setSkeletonMaker(SkeletonMaker locationPicker) {
		this.locationPicker = locationPicker;
	}

}
