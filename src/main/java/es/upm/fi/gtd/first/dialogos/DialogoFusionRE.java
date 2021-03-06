/*
 * DialogoFusionRE.java
 *
 * Created on 4 de mayo de 2008, 1:25
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
import es.upm.fi.gtd.ij.Fusion_mediante_REMod;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.Image;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

/**
 *
 * @author  Alvar
 */
public class DialogoFusionRE extends javax.swing.JDialog {
private Atrous principal;
ParseXML ic;
private static final long serialVersionUID = 1L;
File [] files_multi = new File[4];
boolean multi = false;
File file_pan;
/** Creates new form DialogoFusionRE
 * @param parent Cuadro padre en el que se dibujar� el di�logo
 * @param modal true si es modal, false en caso contrario
 * @param princ Objeto principal de estado y GUI de la aplicaci�n
 */
    public DialogoFusionRE(java.awt.Frame parent, boolean modal, Atrous princ) {        
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
        jSeparator1 = new javax.swing.JSeparator();
        jTextField9 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fusi�n mediante Respuesta Espectral");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                                    .addGap(120, 120, 120))
                                .addGroup(layout.createSequentialGroup()
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
                                        .addComponent(jLabel1))
                                    .addGap(12, 12, 12))))
                        .addGap(233, 233, 233))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton6))
                                    .addComponent(jLabel9))))
                        .addGap(145, 145, 145))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap(19, Short.MAX_VALUE))
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

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        // Comprobar valores de las ponderaciones
        

        
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
        // Wavelet
        
      
        List<GeoImg> melt = new ArrayList<GeoImg>();
        Image img = pan.get(0).getImage().getAsBufferedImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT);
        
        this.setVisible(false);
        ImagePlus imp = new ImagePlus(file_pan.getName(),img);
        ImageProcessor ip_pan = imp.getProcessor();
        ImageProcessor [] ip_multi = new ImageProcessor [4];
        
        int nbits = pan.get(0).getImage().getSampleModel().getSampleSize(0);
        if (!multi)
        {
            
            List<GeoImg> imagesMulti = new ArrayList<GeoImg>();
            int i = 0;
            for (JTextField c : campos)
            {
                if (c.getText().equalsIgnoreCase(""))
                {
                    return;
                }
                File file = new File(c.getText());
                
                imagesMulti = CargaAux.cargarArchivo(file, da, principal.getT(), ic);
                Image mul = imagesMulti.get(0).getImage().getAsBufferedImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT);
        
       
                ImagePlus imaplus = new ImagePlus(file.getName(),mul);
                ip_multi[i] = imaplus.getProcessor();
                i++;
                
            
            }
            // Pasarlas a ImagePlus
            
        }
        else
        { // Se ha colocado una imagen TIFF multibanda
            List<GeoImg> imagesMulti = new ArrayList<GeoImg>();
            File file = new File (jTextField9.getText());
            imagesMulti.addAll(CargaAux.cargarArchivo(file, da, principal.getT(), ic));
            for (int i = 0; i < 3; i++)
            {
                Image mul = imagesMulti.get(i).getImage().getAsBufferedImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT);
        
       
                ImagePlus imaplus = new ImagePlus(file.getName(),mul);
                ip_multi[i] = imaplus.getProcessor();
            }
            
            
        }
        // Tengo los ImageProcessor
         
        
        Fusion_mediante_REMod fpp = new Fusion_mediante_REMod (ip_multi,nbits,ip_pan);
        ImageProcessor [] resultado = fpp.ejecutar_plugin(fpp.panip);
        
        for (int i = 0; i < 3; i++)
        {
            
           
           //Image ima = res[i].getImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT);
           PlanarImage pi = FusionAux.transformarDesdeImagePlus(resultado[i].createImage(), pan.get(0).getImage());
 
           
           System.out.println(pi.getColorModel().toString());
            melt.add(
                new GeoImg(null, 
                            pi, 
                            new File ("Fusi�n Respuesta Espectral de "+file_pan.getName()), 
                            null, 
                            null));
        }
        ParameterBlock pb = new ParameterBlock();
		pb.addSource(melt.get(0).getImage());
		pb.addSource(melt.get(1).getImage());
		pb.addSource(melt.get(2).getImage());

		GeoImg[] resul = {new GeoImg(
                            pan.get(0).getData(), 
                            JAI.create("bandmerge", pb), 
                            new File(ic.buscar(163)
                                +pan.get(0).getFile().getName()
                                +"&"
                                +campos[0].getText()),
                            pan.get(0).getStreamMetadata(), 
                            pan.get(0).getImageMetadata())}; // Tag 163
			
        
        principal.manejoIMGS(resul, 0);
        //principal.manejoIMGS(melt, 0);
}//GEN-LAST:event_jButton5ActionPerformed

private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

    this.setVisible(false);
}//GEN-LAST:event_jButton6ActionPerformed



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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

}
