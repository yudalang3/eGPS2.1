package module.heatmap.eheatmap.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import module.heatmap.eheatmap.HeatmapController;

/**
* @author YFQ,YDL
* @date 2019-09-11 14:27:18
* @version 1.0
* <p>Description:</p>
*/
public class EheatmapGenerateAnnoDialog extends JDialog {

	private static final long serialVersionUID = 7520499775750910756L;
	
	private Font font = new Font(Font.DIALOG,Font.PLAIN,12);
	final private HeatmapController heamapCont;
	
	public EheatmapGenerateAnnoDialog(HeatmapController heamapCont, String rowOrcol) {
		super(UnifiedAccessPoint.getInstanceFrame(), "Generate new "+rowOrcol+" annotation", false);
		this.heamapCont = heamapCont;
		
		setSize(330, 300);
		setContentPane(getJContentPane());
		//add(getJContentPane());
		setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
		//setResizable(false);
		setIconImage(EGPSShellIcons.get("eGPS_logo16x16.png").getImage());
	}

	private JComponent getJContentPane() {
		return new GenerateAnnoDialog(font,heamapCont,this);
	}
	
//	public static void main(String[] args) {
//		// create our jframe as usual
//		JFrame jframe = new JFrame("JFrame Background Color");
//		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		jframe.setBackground(Color.white);
//		jframe.getContentPane().add( new GenerateAnnoDialog());
//		// set the jframe size and location, and make it visible
//		//jframe.setPreferredSize(new Dimension(400, 300));
//		jframe.pack();
//		jframe.setLocationRelativeTo(null);
//		jframe.setVisible(true);
//	}
	
}

class GenerateAnnoDialog extends JPanel {
	
	final HeatmapController heamapCont;
	final EheatmapGenerateAnnoDialog dialog;
	private Random random = new Random(123);

    /**
     * Creates new form GenerateAnnoDialog
     * @param font 
     * @param heamapCont 
     * @param dialog 
     */
    public GenerateAnnoDialog(Font font, HeatmapController heamapCont, EheatmapGenerateAnnoDialog dialog) {
    	this.heamapCont = heamapCont;
    	this.dialog = dialog;
    	initComponents(font);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents(Font font) {

        jLabel1 = new javax.swing.JLabel();
        jLabel1.setFont(font);
        jTextField1AnnotationName = new javax.swing.JTextField();
        jTextField1AnnotationName.setFont(font);
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setFont(font);
        jLabel3 = new javax.swing.JLabel();
        jLabel3.setFont(font);
        jLabel4 = new javax.swing.JLabel();
        jLabel4.setFont(font);
        jLabel5 = new javax.swing.JLabel();
        jLabel5.setFont(font);
        jTextField2NameCat1 = new javax.swing.JTextField();
        jTextField2NameCat1.setFont(font);
        jTextField3NameCat2 = new javax.swing.JTextField();
        jTextField3NameCat2.setFont(font);
        jButtonCat1Color = new javax.swing.JButton();
        jButtonCat1Color.setFont(font);
        jButtonCat2Color = new javax.swing.JButton();
        jButtonCat2Color.setFont(font);
        jButtonGenerate = new javax.swing.JButton();
        jButtonGenerate.setFont(font);

        setPreferredSize(new java.awt.Dimension(200, 170));

        jLabel1.setText("annotation name:");

        jTextField1AnnotationName.setFont(new Font("Dialog", 0, 12)); // NOI18N
        jTextField1AnnotationName.setText("name    ");
        jTextField1AnnotationName.setToolTipText("enter an annotaion name");
        jTextField1AnnotationName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setText("category1");

        jLabel3.setText("category2");

        jLabel4.setText("color");

        jLabel5.setText("name");

        jTextField2NameCat1.setFont(new Font("Dialog", 0, 12)); // NOI18N
        jTextField2NameCat1.setText("cat1       ");
        jTextField2NameCat1.setToolTipText("enter an category name");
        //jTextField2NameCat1.setMinimumSize(new java.awt.Dimension(30, 24));
        jTextField2NameCat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3NameCat2.setFont(new Font("Dialog", 0, 12)); // NOI18N
        jTextField3NameCat2.setText("cat2       ");
        jTextField3NameCat2.setToolTipText("enter an category name");
        //jTextField3NameCat2.setPreferredSize(new java.awt.Dimension(30, 24));
        jTextField3NameCat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButtonCat1Color.setText("set");
        Color randomColor = randomColor();
        jButtonCat1Color.setBackground(randomColor);
        jButtonCat1Color.setForeground(randomColor);
        jButtonCat1Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButtonCat2Color.setText("set");
        randomColor = randomColor();
        jButtonCat2Color.setBackground(randomColor);
        jButtonCat2Color.setForeground(randomColor);
        jButtonCat2Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButtonGenerate.setText("Generate");
        jButtonGenerate.setToolTipText("");
        jButtonGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        //javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(10, 5, 10, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		
		this.add(jLabel1,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		this.add(jTextField1AnnotationName,gridBagConstraints);
		
		gridBagConstraints.gridy = 2;
		this.add(jButtonGenerate,gridBagConstraints);
		
		JPanel leftPanel = new JPanel(new GridBagLayout());
		JPanel rightPanel = new JPanel(new GridBagLayout());
		
		leftPanel.add(jButtonCat1Color,gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		leftPanel.add(jLabel4,gridBagConstraints);
		rightPanel.add(jButtonCat2Color,gridBagConstraints);
		
		gridBagConstraints.gridy = 1;
		leftPanel.add(jLabel5,gridBagConstraints);
		rightPanel.add(jTextField3NameCat2,gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(jTextField2NameCat1,gridBagConstraints);
		
		gridBagConstraints.gridy = 0;
		leftPanel.add(jLabel2,gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		rightPanel.add(jLabel3,gridBagConstraints);
		
		gridBagConstraints.gridy = 1;
		this.add(leftPanel,gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.insets = new Insets(10, 0, 10, 5);
		this.add(rightPanel,gridBagConstraints);
		
        /*layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonCat2Color))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(35, 35, 35)
                                .addComponent(jTextField1AnnotationName, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2NameCat1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jButtonCat1Color)
                            .addComponent(jTextField3NameCat2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonGenerate)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1AnnotationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2NameCat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3NameCat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jButtonCat1Color)
                    .addComponent(jButtonCat2Color))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jButtonGenerate)
                .addContainerGap())
        );*/
    }// </editor-fold>     
    
    private Color randomColor() {
        int r = random.nextInt(150);
        int g = random.nextInt(200);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
    }                                           

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {                                            
    }                                           

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {                                            
    }                                           

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	SwingUtilities.invokeLater(() ->{
    		EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButtonCat1Color);
    		dialog.setVisible(true);
    	});
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	SwingUtilities.invokeLater(() ->{
    		EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButtonCat2Color);
    		dialog.setVisible(true);
    	});
    }                                        

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
//    	heamapCont.displayAnnotaions(jTextField1.getText());
    	Color[] colors = {jButtonCat1Color.getBackground(),jButtonCat2Color.getBackground()};
    	String[] caseNames = {jTextField2NameCat1.getText(),jTextField3NameCat2.getText()};
    	heamapCont.displayAnnotaions(jTextField1AnnotationName.getText(),colors,caseNames);
    	
    	dialog.dispose();
    }                                        


    // Variables declaration - do not modify                     
    private javax.swing.JButton jButtonCat1Color;
    private javax.swing.JButton jButtonCat2Color;
    private javax.swing.JButton jButtonGenerate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1AnnotationName;
    private javax.swing.JTextField jTextField2NameCat1;
    private javax.swing.JTextField jTextField3NameCat2;
    // End of variables declaration                   
}

