package module.evolview.gfamily.work.gui.browser;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.colorscheme.DialogFrame;

@SuppressWarnings("serial")
public class LeftCustomizedBrowserTrackPanel extends JPanel {

    private GeneFamilyController controller;

    /**
     * Create the panel.
     *
     * @param controller
     */
    public LeftCustomizedBrowserTrackPanel(GeneFamilyController controller) {

    	Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
    	
        this.controller = controller;

        setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JLabel lblNewLabel = new JLabel("Key domains");
        lblNewLabel.setFont(globalFont);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        add(lblNewLabel, gbc_lblNewLabel);

        JButton btnCustomized = new JButton("Customize");
        btnCustomized.setFont(globalFont);
        btnCustomized.setFocusable(false);
        btnCustomized.addActionListener(e -> {
            DialogFrame dialog = DialogFrame.obetainCustomizedKeyDoMainsDialog(controller);
            dialog.setVisible(true);
        });
        GridBagConstraints gbc_btnCustomized = new GridBagConstraints();
        gbc_btnCustomized.insets = new Insets(0, 0, 5, 0);
        gbc_btnCustomized.gridx = 2;
        gbc_btnCustomized.gridy = 0;
        add(btnCustomized, gbc_btnCustomized);

        JSeparator separator = new JSeparator();
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.insets = new Insets(0, 0, 5, 0);
        gbc_separator.fill = GridBagConstraints.HORIZONTAL;
        gbc_separator.gridwidth = 3;
        gbc_separator.gridx = 0;
        gbc_separator.gridy = 1;
        add(separator, gbc_separator);

        JLabel lblPrimerSets = new JLabel("Primer sets");
        lblPrimerSets.setFont(globalFont);
        GridBagConstraints gbc_lblPrimerSets = new GridBagConstraints();
        gbc_lblPrimerSets.anchor = GridBagConstraints.WEST;
        gbc_lblPrimerSets.insets = new Insets(0, 0, 5, 5);
        gbc_lblPrimerSets.gridx = 0;
        gbc_lblPrimerSets.gridy = 2;
        add(lblPrimerSets, gbc_lblPrimerSets);

        JButton btnCustomize = new JButton("Customize");
        btnCustomize.addActionListener(e -> {

            DialogFrame dialog = DialogFrame.obetainCustomizedPrimerDialog(controller);
            dialog.setVisible(true);
        });
        btnCustomize.setFocusable(false);
        btnCustomize.setFont(globalFont);
        GridBagConstraints gbc_btnCustomize = new GridBagConstraints();
        gbc_btnCustomize.insets = new Insets(0, 0, 5, 0);
        gbc_btnCustomize.gridx = 2;
        gbc_btnCustomize.gridy = 2;
        add(btnCustomize, gbc_btnCustomize);

        JSeparator separator_1 = new JSeparator();
        GridBagConstraints gbc_separator_1 = new GridBagConstraints();
        gbc_separator_1.gridwidth = 3;
        gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_separator_1.insets = new Insets(0, 0, 0, 5);
        gbc_separator_1.gridx = 0;
        gbc_separator_1.gridy = 3;
        add(separator_1, gbc_separator_1);

    }

}
