/*
 * DialogoEscala.java
 *
 * Created on 20 de junio de 2007, 13:09
 */

package es.upm.fi.gtd.first.dialogos;

import es.upm.fi.gtd.first.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Locale;

/**
 *
 * @author  Harcalion
 */
public class DialogoEscala extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;

    /** Creates new form DialogoEscala
     * @param parent Cuadro padre para dibujar el diálogo
     * @param width Anchura
     * @param modal true si es modal, false en caso contrario
     * @param lis Sumidero de eventos de acción
     * @param loc Idioma actual
     * @param height Altura
     */
    public DialogoEscala(java.awt.Frame parent, boolean modal, ActionListener lis, int width, int height, Locale loc) {
        super(parent, modal);
         ic = new ParseXML(loc.getLanguage()+"_"+loc.getCountry()+".xml");
        initComponents();
        jFormattedTextField1.setText(String.valueOf(width));
        jFormattedTextField2.setText(String.valueOf(height));
        jComboBox1.addActionListener(lis);
        // Modo por defecto
        jRadioButton1.setSelected(true);
        
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(ic.buscar(66)); // Tag 66

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(ic.buscar(67))); // Tag 67

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText(ic.buscar(68)); // Tag 68
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel2.setText(ic.buscar(69)); // Tag 69

        jLabel3.setText(ic.buscar(70)); // Tag 70

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jFormattedTextField2)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)))
                    .addComponent(jRadioButton1))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jRadioButton1)
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(ic.buscar(71))); // Tag 71

        jTextField3.setText("100");

        jTextField4.setText("100");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText(ic.buscar(72)); // Tag 72
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel4.setText(ic.buscar(73)); // Tag 73

        jLabel5.setText(ic.buscar(74)); // Tag 74

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)))
                    .addComponent(jRadioButton2))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jButton1.setText(ic.buscar(23)); // Tag 23
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botones(evt);
            }
        });

        jButton2.setText(ic.buscar(24)); // Tag 24
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botones(evt);
            }
        });

        jComboBox1.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { ic.buscar(75), 
                                   ic.buscar(76), 
                                   ic.buscar(77), 
                                   ic.buscar(78), 
                                   ic.buscar(79) })); // Tag 75, 76, 77, 78, 79 

        jLabel1.setText(ic.buscar(80)); // Tag 80

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setText("8");

        jLabel6.setText(ic.buscar(81)); // Tag 81

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(21, 21, 21)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Obtiene los valores de dimensión final para la imagen
     * @return Valores de dimensión deseados
     */
    public Point2D getValores()
    {
        Point2D p;
        if (absoluto)
        {
            p = new Point(width,height);
        }
        else
        {
            p = new Point2D.Float(perWidth, perHeight);
        }
        return p;
    }
    /**
     * Obtiene si es un cambio de tamaño absoluto
     * @return true si es escala absoluta, false si es relativa
     */
    public boolean getAbsoluto()
    {
        return absoluto;
    }
    /**
     * Obtiene la interpolación que se usará en el remuestreo
     * @return 0 en todo caso
     */
    public int getInterpolacion()
    {
       return 0; // El valor interpolación está en Atrous 
    }
    /**
     * Obtiene la precisión utilizada en los algoritmos de interpolación bicúbica
     * @return Precisión utilizada
     */
    public int getPrecision()
    {
        return precision;
    }
    
    private void botones(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botones

        if (evt.getActionCommand().compareTo(ic.buscar(23))==0) // Tag 23
        { // Acción de aceptar
            if (jRadioButton1.isSelected())
            { // Absoluto
                absoluto = true;
                // Comprobar los valores                
                if (!jFormattedTextField1.getText().equalsIgnoreCase("") && !jFormattedTextField2.getText().equalsIgnoreCase(""))
                { // Hay valores
                    try
                    {
                        String anchura = jFormattedTextField1.getText();
                        width = Integer.parseInt(anchura);
                        String altura = jFormattedTextField2.getText();
                        height = Integer.parseInt(altura);
                        precision = Integer.parseInt(jTextField1.getText());
                        this.setVisible(false);
                    }
                    catch (Exception e)
                    {
                        jFormattedTextField1.setBackground(Color.RED);
                        jFormattedTextField2.setBackground(Color.RED);
                    }
                }
                else if (jFormattedTextField1.getText().equalsIgnoreCase(""))
                {
                    jFormattedTextField1.setBackground(Color.RED);
                    repaint();
                }
                else if (jFormattedTextField2.getText().equalsIgnoreCase(""))
                {
                    jFormattedTextField2.setBackground(Color.RED);
                    repaint();
                }
                else if (jFormattedTextField1.getText().equalsIgnoreCase("") && jFormattedTextField2.getText().equalsIgnoreCase(""))
                {
                    jFormattedTextField1.setBackground(Color.RED);
                    jFormattedTextField2.setBackground(Color.RED);
                    repaint();
                }
            }
            else
            { // Relativo
                absoluto = false;
                if (!jTextField3.getText().equalsIgnoreCase("") && !jTextField4.getText().equalsIgnoreCase(""))
                { // Hay valores
                    try
                    {
                        String anchura = jTextField3.getText();
                        perWidth = Float.parseFloat(anchura);
                        perWidth /= 100;
                        String altura = jTextField4.getText();
                        perHeight = Float.parseFloat(altura);
                        perHeight /= 100;
                        precision = Integer.parseInt(jTextField1.getText());
                        this.setVisible(false);
                    }
                    catch (Exception e)
                    {
                        jTextField3.setBackground(Color.RED);
                        jTextField4.setBackground(Color.RED);
                    }
                }
                else if (jTextField3.getText().equalsIgnoreCase(""))
                {
                    jTextField3.setBackground(Color.RED);
                    repaint();
                }
                else if (jTextField4.getText().equalsIgnoreCase(""))
                {
                    jTextField4.setBackground(Color.RED);
                    repaint();
                }
                else if (jTextField3.getText().equalsIgnoreCase("") && jTextField4.getText().equalsIgnoreCase(""))
                {
                    jTextField3.setBackground(Color.RED);
                    jTextField4.setBackground(Color.RED);
                    repaint();
                }
               
            }
            
        }
        else if (evt.getActionCommand().compareTo(ic.buscar(24))==0) // Tag 24
        {
            this.setVisible(false);
        }
    }//GEN-LAST:event_botones
   


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
    private boolean absoluto;
    private int width;
    private int height;
    private float perWidth;
    private float perHeight;
    private int precision;
    private ParseXML ic;
}