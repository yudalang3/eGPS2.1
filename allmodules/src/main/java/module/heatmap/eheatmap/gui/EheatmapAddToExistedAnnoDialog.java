package module.heatmap.eheatmap.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import egps2.utils.common.model.datatransfer.FourTuple;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.NameSelection;

/**
* @author YFQ,YDL
* @date 2019-09-11 14:27:18
* @version 1.0
* <p>Description:</p>
*/
public class EheatmapAddToExistedAnnoDialog extends JDialog {

	private static final long serialVersionUID = 7520499775750910756L;
	
	private Font font = new Font(Font.DIALOG,Font.PLAIN,12);
	final private HeatmapController heamapCont;
	
	public EheatmapAddToExistedAnnoDialog(HeatmapController heamapCont, String rowOrcol) {
		super(UnifiedAccessPoint.getInstanceFrame(), "Add existed "+rowOrcol+" annotation", false);
		this.heamapCont = heamapCont;
		
		setSize(330, 260);
		boolean ifRow = true;
		if (rowOrcol.contains("row")) {
			ifRow = true;
		}else {
			ifRow = false;
		}
		AddToExistedAnnoPanel tt = new AddToExistedAnnoPanel(font,heamapCont,this,ifRow);
		
		
		setContentPane(tt);
		//add(getJContentPane());
		setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
		//setResizable(false);
		setIconImage(EGPSShellIcons.get("eGPS_logo16x16.png").getImage());
	}
	
}

class AddToExistedAnnoPanel extends javax.swing.JPanel {
	
	final HeatmapController heamapCont;
	final EheatmapAddToExistedAnnoDialog dialog;
	private boolean ifRow = true;

    /**
     * Creates new form GenerateAnnoDialog
     * @param font 
     * @param heamapCont 
     * @param dialog 
     * @param ifRow2 
     */
	public AddToExistedAnnoPanel(Font font, HeatmapController heamapCont, EheatmapAddToExistedAnnoDialog dialog,
			boolean ifRow2) {
		this.heamapCont = heamapCont;
		this.dialog = dialog;
		this.ifRow = ifRow2;
		initComponents(font);
	}
    private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	int index = jComboBox1.getSelectedIndex();
    	Color newColor = jButton1.getBackground();
    	String text = jTextFieldCaseName.getText();
    	
    	AnnotaionParaObj annotaionParaObj = heamapCont.getParaModel().getAnnotaionParaObj();
    	ParameterModel paraModel = heamapCont.getParaModel();
    	byte[] oldBa = null;
    	int num = jComboBox1.getSelectedIndex();
    	
    	List<NameSelection> selections = null;
    	if (ifRow) {
    		selections = paraModel.getRowNameSelections();
    		FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> rowAnnoParas = annotaionParaObj.getRowAnnoParas();
    		oldBa = rowAnnoParas.third.get(num);
    		num = oldBa.length;
		}else {
			selections = paraModel.getColNameSelections();
			FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> colAnnoParas = annotaionParaObj.getColAnnoParas();
    		oldBa = colAnnoParas.third.get(num);
    		num = oldBa.length;
		}
    	
    	byte newValue = (byte) (getNewValue(oldBa) + 1);
		for (NameSelection ns : selections) {
			oldBa[ns.getIndex()] = newValue;
		}
		
		annotaionParaObj.updateAnnotations(ifRow, index, newColor, text, oldBa);
    	
    	dialog.dispose();
    }                                        

    private byte getNewValue(byte[] oldBa) {
		byte max = Byte.MIN_VALUE;
		for (byte b : oldBa) {
			if (b > max) {
				max = b;
			}
		}
		return max;
	}
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	SwingUtilities.invokeLater(() ->{
    		EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButton1);
    		dialog.setVisible(true);
    	});
    } 

	  private void initComponents(Font font) {

		  jLabel1 = new javax.swing.JLabel();
	        jLabel1.setFont(font);
	        jComboBox1 = new javax.swing.JComboBox<>();
	        jComboBox1.setFont(font);
	        jLabel2 = new javax.swing.JLabel();
	        jLabel2.setFont(font);
	        jButton1 = new javax.swing.JButton();
	        jButton1.setFont(font);
	        buttonOK = new javax.swing.JButton();
	        buttonOK.setFont(font);
	        jLabel3 = new javax.swing.JLabel();
	        jLabel3.setFont(font);
	        jTextFieldCaseName = new javax.swing.JTextField();
	        jTextFieldCaseName.setFont(font);

	        setPreferredSize(new java.awt.Dimension(300, 200));

	        jLabel1.setText("Choose annotation");

	        AnnotaionParaObj annotaionParaObj = heamapCont.getParaModel().getAnnotaionParaObj();
			String[] eles = null;
			if (ifRow) {
				List<String> first = annotaionParaObj.getRowAnnoParas().first;
				eles = first.toArray(new String[0]);
			}else {
				List<String> first = annotaionParaObj.getColAnnoParas().first;
				eles = first.toArray(new String[0]);
			}
	        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(eles));

	        jLabel2.setText("New color");

	        jButton1.setText("Col");
	        jButton1.setActionCommand("Col");
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });

	        buttonOK.setText("OK");
	        buttonOK.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                buttonOKActionPerformed(evt);
	            }
	        });

	        jLabel3.setText("Case name");

	        jTextFieldCaseName.setText("name");

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGap(56, 56, 56)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jLabel1)
	                    .addComponent(jLabel2)
	                    .addComponent(jLabel3))
	                .addGap(31, 31, 31)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                    .addComponent(buttonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jTextFieldCaseName))
	                .addContainerGap(49, Short.MAX_VALUE))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGap(31, 31, 31)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel1)
	                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2)
	                    .addComponent(jButton1))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel3)
	                    .addComponent(jTextFieldCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
	                .addComponent(buttonOK)
	                .addGap(19, 19, 19))
	        );
	    }// </editor-fold>                        


	    // Variables declaration - do not modify                     
	    private javax.swing.JButton buttonOK;
	    private javax.swing.JButton jButton1;
	    private javax.swing.JComboBox<String> jComboBox1;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JTextField jTextFieldCaseName;
	    // End of variables declaration
	    

}

