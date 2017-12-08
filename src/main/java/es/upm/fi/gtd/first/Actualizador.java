/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first;

import es.upm.fi.gtd.first.dialogos.DialogoActualizador;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Actualizador que recupera desde un sitio Web las versiones más modernas de la herramienta
 * @author Alvar
 */
public class Actualizador {
    String baseURL = "http://usuarios.lycos.es/proyectofirst/";
    String [] jars = {"clibwrapper_jiio.jar",
    "ij.jar",
    "ij_fusion.jar",
    "jai_codec.jar",
    "jai_core.jar",
    "jai_imageio.jar",
    "jcommon-1.0.12.jar",
    "jfreechart-1.0.9.jar",
    "jscience.jar",
    "mlibwrapper_jai.jar"};
    Atrous principal;
    List<String> ficherosActualizables;
    DialogoActualizador da;
    ParseXML verLoc;
    List<String[]> verRemota;
    /**
     * Constructor para el actualizador
     * @param princ Objeto principal de estado y GUI de la herramienta
     */
    public Actualizador(Atrous princ)
    {
        principal = princ;
    }
    
    
    /**
     * Recupera los datos de actualización desde el sitio Web y los muestra en una lista
     */
    public void actualizar()
    {
        da =  new DialogoActualizador(principal.getT(), this, false);
        da.setVisible(true);
        List<String[]> verLocal;
        boolean actualizar = false;
        verLoc = new ParseXML("Versiones.xml");
        verLocal = verLoc.recuperarValores();
        ParseXML verRem = new ParseXML("http://usuarios.lycos.es/proyectofirst/Versiones.xml");
        verRemota = verRem.recuperarValores();
        List<String []> datos = new ArrayList<String []>();
         ficherosActualizables = new ArrayList<String>();
        
        //da.setTexto(texto);
        //javax.swing.SwingUtilities.invokeLater(da);
        for (int i = 0; i < verLocal.size(); i++)            
        {
            String [] res = new String[3];
            String [] l = verLocal.get(i);
            String [] r = verRemota.get(i);
            if (l[1].compareTo(r[1])!=0)
            {
                actualizar = true;
                ficherosActualizables.add(l[0]);
            }
            res[0] = l[0];
            res[1] = l[1];
            res[2] = r[1];
            datos.add(res);
            
            
            
            //da.setTexto(texto);
            //javax.swing.SwingUtilities.invokeLater(da);
        }
        da.setTexto(datos);
        
        if (actualizar)
        {
            da.activarBoton();
        }
        /*
        URI uri = null;
                try {

                    uri = new URI(baseURL+"Versiones.xml");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                URL url = null;
                try {
                    url = uri.toURL();
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
        */
    }

    /**
     * Descarga desde el sitio Web los ficheros actualizados de la herramienta
     */
    public void descargarActualizaciones() {
        for (int i = 0; i < ficherosActualizables.size(); i++)
        {
            FileOutputStream fos = null;
            try {
                String nf = ficherosActualizables.get(i);
                URL dir = new URL(baseURL + nf);
                if (nf.endsWith("jar"))
                {
                    fos = new FileOutputStream("./lib/" + nf);
                }
                else
                {
                    fos = new FileOutputStream(nf);    
                }
                
                InputStream is = dir.openConnection().getInputStream();
                boolean OK = true;
                while (OK)
                {
                    int dato = is.read();
                    if (dato == -1)
                    {
                        OK = false;
                    }
                    else
                    {
                        fos.write(dato);
                    } 
                       
                }
            } catch (Exception ex) {
                Logger.getLogger(Actualizador.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Actualizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        // Actualizar la versión en el archivo XML
        verLoc.sustituirValores(verRemota);
        verLoc.actualizar();
        JOptionPane.showMessageDialog(principal.getT(), "Reinicie FIRST para aplicar los cambios");
        da.setVisible(false);
        
    }
    /*public void actualizar()
    {
        final DialogoActualizador da =  new DialogoActualizador(principal.getT(), false);
          da.setVisible(true);
        
 

        
        String verLocal, verRemota;
        boolean actualizar = false;
        String texto = new String("Versiones de los componentes:\n"+
                                  "Nombre JAR:\t\tVersión local:\t\tVersión remota:\n");
        da.setTexto(texto);
        javax.swing.SwingUtilities.invokeLater(da);
        for (int i = 0; i < jars.length; i++) {
            texto = texto.concat(jars[i]+"\t\t");
            // Obtener versión local
            
            
            try {
                JarFile jfLocal = new JarFile(".\\lib\\"+jars[i]);
                Manifest mf = jfLocal.getManifest();
                Attributes att = mf.getMainAttributes();
                verLocal = att.getValue("Implementation-Version");
                texto = texto.concat(verLocal+"\t\t");
                URI uri = null;
                try {

                    uri = new URI(baseURL+jars[i]+"!/");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                URL url = null;
                try {
                    url = uri.toURL();
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
                JarURLConnection conn = null;
                try {
                    conn = (JarURLConnection) url.openConnection();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //Manifest mf = conn.getManifest();
                JarFile jf = conn.getJarFile();
                mf = jf.getManifest();
                att = mf.getMainAttributes();
                verRemota = att.getValue("Implementation-Version");
                texto = texto.concat(verRemota+"\n");
                if (verRemota.compareTo(verLocal) != 0)
                {
                    actualizar = true;
                }
                da.setTexto(texto);
                javax.swing.SwingUtilities.invokeLater(da);
            //System.out.println(mf.getAttributes("Implementation-Version").toString());
            } catch (IOException ex) {
                Logger.getLogger(Actualizador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }*/

}
