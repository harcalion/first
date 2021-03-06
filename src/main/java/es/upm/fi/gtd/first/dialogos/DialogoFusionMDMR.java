/*
 * DialogoFusionMDMR.java
 *
 * Created on 4 de mayo de 2008, 12:24
 */

package es.upm.fi.gtd.first.dialogos;

import es.upm.fi.gtd.first.Accesorio;
import es.upm.fi.gtd.first.Atrous;
import es.upm.fi.gtd.first.CargaAux;
import es.upm.fi.gtd.first.Dialogos;
import es.upm.fi.gtd.first.DialogosAux;
import es.upm.fi.gtd.first.ExampleFileFilter;
import es.upm.fi.gtd.first.FusionAux;
import es.upm.fi.gtd.first.GeoImg;
import es.upm.fi.gtd.first.ParseXML;
import es.upm.fi.gtd.ij.Fusion_mediante_MDMRMod;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.Image;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.PlanarImage;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

/**
 *
 * @author  Alvar
 */
public class DialogoFusionMDMR extends javax.swing.JDialog {

    Atrous principal;
    ParseXML ic;
      private static final long serialVersionUID = 1L;
    
    File [] files_multi = new File[4];
    File file_pan;
    boolean multi = false;
    
    
    /** Creates new form DialogoFusionMDMR
 * @param parent Cuadro padre en el que se dibujar� el di�logo
 * @param modal true si es modal, false en caso contrario
 * @param princ Objeto principal de estado y GUI de la aplicaci�n
 */
    public DialogoFusionMDMR(java.awt.Frame parent, boolean modal, Atrous princ) {
        super(parent, modal);
        principal = princ;
        ic = principal.getIC();
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField10 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextField11 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fusi�n medante MDMR");

        jButton8.setText("Examinar");
        jButton8.setActionCommand("6");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel10.setText("Pancrom�tica");

        jButton1.setText("Examinar");
        jButton1.setActionCommand("0");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Examinar");
        jButton2.setActionCommand("1");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Examinar");
        jButton3.setActionCommand("2");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Examinar");
        jButton4.setActionCommand("3");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("Banda 4");

        jLabel3.setText("Banda 3");

        jLabel2.setText("Banda 2");

        jLabel1.setText("Banda 1");

        jButton7.setText("Examinar");
        jButton7.setActionCommand("5");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel9.setText("Multibanda");

        jLabel5.setText("Filtro");

        jLabel6.setText("Filtro");

        jLabel7.setText("Filtro");

        jLabel8.setText("Filtro");

        jTextField8.setText("1.0");

        jTextField7.setText("1.0");

        jTextField6.setText("1.0");

        jTextField5.setText("1.0");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton5.setText("Aceptar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Cancelar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName(""); // NOI18N

        jTextField11.setText("8");

        jLabel15.setText("Niveles de degradaci�n PAN (�ngulos)");

        jTextField13.setText("8");

        jLabel16.setText("Niveles de degradaci�n MULTI (�ngulos)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel11.setText("Valores para la fusi�n");

        jLabel12.setText("a:");

        jLabel13.setText("b:");

        jTextField12.setText("1.0");
        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });

        jLabel14.setText("a:");

        jLabel17.setText("a:");

        jLabel18.setText("a:");

        jTextField14.setText("1.0");

        jLabel19.setText("b:");

        jTextField15.setText("1.0");

        jLabel20.setText("b:");

        jTextField16.setText("1.0");

        jLabel21.setText("b:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(206, 206, 206))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                        .addGap(285, 285, 285))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel11)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(jTextField2)
                                                            .addComponent(jTextField3)
                                                            .addComponent(jTextField4)
                                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jButton1)
                                                            .addComponent(jButton2)
                                                            .addComponent(jButton3)
                                                            .addComponent(jButton4))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel4)
                                                            .addComponent(jLabel3)
                                                            .addComponent(jLabel2)
                                                            .addComponent(jLabel1))))
                                                .addGap(18, 78, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel5)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel12))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel14))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel7)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel17))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel18)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField8)
                                                    .addComponent(jTextField7)
                                                    .addComponent(jTextField6)
                                                    .addComponent(jTextField5))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel21)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel20)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel13)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel19)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addGap(31, 31, 31)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(jButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)
                                .addGap(36, 36, 36)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1)
                                    .addComponent(jLabel1)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(53, 53, 53)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton7)
                                    .addComponent(jLabel9)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel19)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel20)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel21)
                                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton6))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        jButton1ActionPerformed(evt);
}//GEN-LAST:event_jButton8ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int entrada = Integer.parseInt(evt.getActionCommand());
        
        principal.getFglobal().setMultiSelectionEnabled(false);
        if (entrada == 6)
        {
            principal.getFglobal().setDialogTitle(ic.buscar(103));  // Tag 103
        }
        else
        {
            principal.getFglobal().setDialogTitle(ic.buscar(104));  // Tag 104
        }
        
        principal.getFglobal().setDialogType(JFileChooser.OPEN_DIALOG);
        ExampleFileFilter filter =
                new ExampleFileFilter(
                new String [] {"tiff",
                "tif",
                "jpg",
                "jpeg",
                "jpe",
                "bil"},
                ic.buscar(88)); // Tag 88
        principal.getFglobal().resetChoosableFileFilters();
        principal.getFglobal().addChoosableFileFilter(filter);
        
        Accesorio acc = new Accesorio(principal.getFglobal(), principal.getT(), principal.getIC());
        principal.getFglobal().setAccessory(acc);
        acc.esDialogoAbrir();
        acc.setProfundidad(false);
        
        

        int retval = principal.getFglobal().showDialog(principal.getT(), null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            acc = (Accesorio) principal.getFglobal().getAccessory();
            boolean separadas = acc.getSeparadas();
            File file = principal.getFglobal().getSelectedFile();
            
            switch (entrada)
            {
                case 0:jTextField1.setText(file.getPath());files_multi[entrada] = file;break;
                case 1:jTextField2.setText(file.getPath());files_multi[entrada] = file;break;                
                case 2:jTextField3.setText(file.getPath());files_multi[entrada] = file;break;
                case 3:jTextField4.setText(file.getPath());files_multi[entrada] = file;break;
                case 5: jTextField9.setText(file.getPath());
                    files_multi[0] = file;
                    multi = true;
                    break;
                case 6:jTextField10.setText(file.getPath());
                    file_pan = file;
                    break;
                default: break;
            }
            
            this.pack();
            
        } else {
            Dialogos.dibujarMensajesError(ic,retval);
        }
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        jButton1ActionPerformed(evt);
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

                jButton1ActionPerformed(evt);
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

                jButton1ActionPerformed(evt);
}//GEN-LAST:event_jButton4ActionPerformed

private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        jButton1ActionPerformed(evt);
}//GEN-LAST:event_jButton7ActionPerformed

private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed

}//GEN-LAST:event_jTextField5ActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        // Comprobar valores de las ponderaciones
        
        double [] ponderaciones = new double[4];
        ponderaciones[0] = Double.parseDouble(jTextField5.getText());
        ponderaciones[1] = Double.parseDouble(jTextField6.getText());
        ponderaciones[2] = Double.parseDouble(jTextField7.getText());
        ponderaciones[3] = Double.parseDouble(jTextField8.getText());
        for (int i = 0; i < 4; i++)
        {
            if (ponderaciones[i]<0.0 && ponderaciones[i]>10.0)
            {
                Dialogos.informarError(ic.buscar(168)); // Tag 168
            }
        }
        
        // Comprobar valor de PAN
        if (jTextField10.getText().equalsIgnoreCase(""))
        {
            return;
        }
        file_pan = new File(jTextField10.getText());
        DialogosAux da = new DialogosAux(principal);
        List<GeoImg> pan = CargaAux.cargarArchivo(file_pan, da, principal.getT(), ic);
        // Comprobar valores de MULTI
        JTextField[] campos = {jTextField1, jTextField2, jTextField3, jTextField4};
        // Comprobar valores de Fusi�n
               
        // �ngulos
        int angulosPan = Integer.parseInt(jTextField11.getText());
        int angulosMulti = Integer.parseInt(jTextField13.getText());
        // Filtros
        JTextField[] filtrosA = {jTextField5, jTextField6, jTextField7, jTextField8};
        JTextField[] filtrosB = {jTextField12, jTextField14, jTextField15, jTextField16};
        double [] a = new double[4];
        double [] b = new double[4];
        for (int i = 0; i < 4; i++)
        {
            a[i]=Double.parseDouble(filtrosA[i].getText());
            b[i]=Double.parseDouble(filtrosB[i].getText());
        }
        
        this.setVisible(false);
        Image img = pan.get(0).getImage().getAsBufferedImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT);
        ImagePlus imp = new ImagePlus(file_pan.getName(),img);
        ImageProcessor ip_pan = imp.getProcessor();
        
        List<GeoImg> melt = new ArrayList<GeoImg>();
        List<GeoImg> imagesMulti = new ArrayList<GeoImg>();
        
        if (!multi)
        {
            int i = 0;
            for (JTextField c : campos)
            {
                if (!c.getText().equalsIgnoreCase(""))
                {
                    
                
                File file = new File(c.getText());
                imagesMulti.addAll(CargaAux.cargarArchivo(file, da, principal.getT(), ic));
                }
            }
            
        }
        else
        { // Se ha colocado una imagen TIFF multibanda
           
            File file = new File (jTextField9.getText());
            imagesMulti.addAll(CargaAux.cargarArchivo(file, da, principal.getT(), ic));

            
            
        }
        ImageProcessor [] ip_multi = new ImageProcessor [imagesMulti.size()];
        for (int i = 0; i < imagesMulti.size(); i++)
            {
                Image mul = imagesMulti.get(i).getImage().getAsBufferedImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT);
        
       
                ImagePlus imaplus = new ImagePlus("",mul);
                ip_multi[i] = imaplus.getProcessor();
            }
        System.out.println("Estoy antes de la fusi�n");
        Fusion_mediante_MDMRMod fmm = new Fusion_mediante_MDMRMod(ip_pan,ip_multi);
        ImageProcessor [] resultado = fmm.MDMR(fmm.multibanda, fmm.pancromatica, angulosMulti, angulosPan, a, b);
        for (int i = 0; i < imagesMulti.size(); i++)
        {
            
           
          
           PlanarImage pi = FusionAux.transformarDesdeImagePlus(resultado[i].createImage(), pan.get(0).getImage(), DataBuffer.TYPE_FLOAT);
            
           
           System.out.println(pi.getColorModel().toString());
            melt.add(
                new GeoImg(null, 
                            pi, 
                            new File ("Fusi�n MDMR de "+file_pan.getName()+"&"+imagesMulti.get(i).getFile().getName()), 
                            null, 
                            null));
        }
        principal.manejoIMGS(melt, 0);
}//GEN-LAST:event_jButton5ActionPerformed

private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

    this.setVisible(false);
}//GEN-LAST:event_jButton6ActionPerformed

private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed

}//GEN-LAST:event_jTextField12ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

}
