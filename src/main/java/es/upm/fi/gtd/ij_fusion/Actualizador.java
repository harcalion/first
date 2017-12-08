package es.upm.fi.gtd.ij_fusion;

/*
 * Actualizador.java
 *
 * Created on 20 de noviembre de 2007, 18:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import ij.plugin.PlugIn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
// Hash md5
import java.security.*;
// Show status IJ
import ij.*;

/**
 * Clase que permite la actualización automática de los
 * plugins utilizando un servidor web.
 *
 * @author Francisco Javier Merino Guardiola
 */
public class Actualizador implements PlugIn {
    
    public static final String UPDATE_HOST = "http://ip.cavefish.org/~cavefish/ijfusion/";
    public static final String UPDATE_VERSION_FILE = "update.txt";
    public static final String UPDATE_JAR_FILE = "ij_fusion.jar";
    
    private String urlbase;
    private String version_file;
    private String jar_file;
    private String jar_hash;
    private String tmpdir;
    // Current dialog
    private JFrame cdialog;
    
    public void run(String string) {
        this.urlbase = UPDATE_HOST;
        this.version_file = UPDATE_VERSION_FILE;
        this.jar_file = UPDATE_JAR_FILE;
        try { this.tmpdir = System.getProperty("java.io.tmpdir"); } catch(Exception e) { e.printStackTrace(); }
        IniciarActualizacion();
    }
    
    /** Creates a new instance of Actualizador
     * public Actualizador() {
     * this.urlbase = UPDATE_HOST;
     * this.version_file = UPDATE_VERSION_FILE;
     * this.jar_file = UPDATE_JAR_FILE;
     * try { this.tmpdir = System.getProperty("java.io.tmpdir"); }
     * catch(Exception e) { e.printStackTrace(); }
     * }
     *
     *
     * public Actualizador(String url, String verfile, String jarfile) {
     * if(url.endsWith("/")) this.urlbase = url;
     * else this.urlbase = url + "/";
     * this.version_file = verfile;
     * this.jar_file = jarfile;
     * try { this.tmpdir = System.getProperty("java.io.tmpdir"); }
     * catch(Exception e) { e.printStackTrace(); }
     * }
     */
    
    
    
    public void IniciarActualizacion() {
        String line;
        String newversion;
        String []toks;
        BufferedReader buf;
        try {
            cdialog = DialogoConexion();
            cdialog.setVisible(true);
            URL url = new URL(this.urlbase+this.version_file);
            buf = new BufferedReader(new InputStreamReader(url.openStream()));
            line = buf.readLine();
            cdialog.dispose();
            // Comprobamos que el fichero de la URL sea un fichero de actualización
            if (!line.matches("CURRENT_VERSION\\s*=\\s*[0-9]+\\.[0-9]+\\.[0-9]+$")) {
                JOptionPane.showMessageDialog(null,"No se pudo determinar la última version!","Error de actualización",JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Comprobamos si la versión de la web difiere de la instalada
            toks = line.split("=");
            newversion = toks[1].trim();
            if (newversion.equals(Version.IJFUSION_VERSION)) {
                JOptionPane.showMessageDialog(null,"Usted ya dispone de la versión más reciente.","Actualización",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Comprobamos que exista un hash de comprobación y sea válido
            line = buf.readLine();
            if (!line.matches("MD5\\s*=\\s*[a-fA-F0-9]+$")) {
                JOptionPane.showMessageDialog(null,"No se pudo obtener la suma de comprobación","Error de actualización",JOptionPane.ERROR_MESSAGE);
                return;
            }
            toks = line.split("=");
            jar_hash = toks[1].trim().toLowerCase();
            
            String changelog="";
            while((line=buf.readLine())!=null)
                changelog = changelog + line + "\n";
            DialogoActualizacion(Version.IJFUSION_VERSION, newversion, changelog);
            
        } catch (MalformedURLException e) {
            cdialog.dispose();
            JOptionPane.showMessageDialog(null,"URL mal formada!","Error conectando al servidor",JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException e) {
            cdialog.dispose();
            JOptionPane.showMessageDialog(null,"URL de actualización inválida o no disponible!","Error al actualizar",JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    
    
    private void DescargarActualizacion() {
        try {
            IJ.showStatus("Descargando archivo de actualizacion IJFusion...");
            URL url = new URL(this.urlbase+this.jar_file);
            // MD5 message digest
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            // Buffers del fichero
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.tmpdir+"/ij~fusion.jar"));
            InputStream in = url.openConnection().getInputStream();
            byte[] buffer = new byte[4*1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                // Escribiendo en fichero
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                md.update(buffer, 0, numRead);
            }
            // Escribe el fichero al cerrarlo
            out.close();
            IJ.showStatus("Comprobando hash de actualizacion IJFusion...");
            // Comprueba los MD5
            byte hash[] = md.digest();
            if (!this.jar_hash.toLowerCase().equals(ByteToHex(hash).toLowerCase())) {
                JOptionPane.showMessageDialog(null, "Fallo en la suma de comprobación (MD5).\nPor favor inténtelo de nuevo.", "Actualización fallida", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Copia el JAR para actualizarlo
            
            FileInputStream fis  = new FileInputStream(this.tmpdir+"/ij~fusion.jar");
            FileOutputStream fos = new FileOutputStream(ij.Menus.getPlugInsPath()+"ij_fusion.jar");
            byte[] buf = new byte[1024];
            int i = 0;
            while((i=fis.read(buf))!=-1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
            IJ.showStatus("Actualizacion IJFusion completada!");
            JOptionPane.showMessageDialog(null, "Para que los cambios surtan efecto reinicie ImageJ","Actualización Completada",JOptionPane.INFORMATION_MESSAGE);
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"No se ha encontrado el fichero de actualización!","Error actualizando",JOptionPane.ERROR_MESSAGE);
            return;
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error de entrada y salida!","Error actualizando",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private static String ByteToHex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i)
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1,3));
        return sb.toString();
    }
    
    
    
    private JFrame DialogoConexion() {
        JFrame ventana = new JFrame("Actualizando IJFusion");
        ventana.setBounds(300,300,250,50);
        JLabel eti = new JLabel("Iniciando conexión con el servidor...");
        ventana.setContentPane(eti);
        return ventana;
    }
    
    private void DialogoActualizacion(String actual, String nueva, String cambios) {
        JFrame ventana = new JFrame("Actualización disponible!");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setBounds(100,100,500,350);
        ventana.setResizable(false);
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
        JLabel eti = new JLabel("Actualizar IJFusion v"+actual+" a la version v"+nueva);
        panel.add("North", eti);
        JTextArea texto = new JTextArea(cambios);
        texto.setEditable(false);
        panel.add("Center", new JScrollPane(texto));
        JButton boton = new JButton("Aceptar condiciones y proceder con la actualización.");
        String urljar = this.urlbase+this.jar_file;
        Descargar a = new Descargar(this);
        boton.addMouseListener(a);
        panel.add("South", boton);
        ventana.setContentPane(panel);
        ventana.setVisible(true);
    }
    
    
    /**
     * Clase para la escucha del evento del raton al hacer click
     * en el boton de actualizar.
     */
    static class Descargar extends MouseAdapter{
        Actualizador act;
        public Descargar(Actualizador act){
            super();
            this.act = act;
        }
        public void mouseClicked(MouseEvent e) {
            // Cerramos la ventana
            Component c = e.getComponent();
            while(c.getClass().getName() != "javax.swing.JFrame")
                c = c.getParent();
            ((JFrame)c).dispose();
            // Descargamos la actualizacion
            act.DescargarActualizacion();
        }
    }
    
    
    public static void main(String args[]) {
        Actualizador act = new Actualizador();
        act.IniciarActualizacion();
    }
}
