package es.upm.fi.gtd.ij_fusion;

/*
 * Acerca_De.java
 *
 * Created on 14 de noviembre de 2007
 *
 */

import ij.*;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

/**
 *
 * @author Francisco Javier Merino Guardiola
 */
public class Version implements PlugIn {
    public static String IJFUSION_VERSION = "0.0.2";
    private static String ABOUT_IJFUSION = 
            "IJFusion its a collection of tools developed by the remote sensing\n" +
            "group of the Facultad de Informatica UPM, for the image fusion of\n" +
            "multispectral and pancromatic images of diverse sensors.\n \n" +
            "Project coordinator:\n\tConsuelo Gonzalo Martín (chelo@fi.upm.es)\n \n"+
            "Main developer:\n\tFrancisco Javier Merino Guardiola (fjmerinog@gmail.com)\n \n" +
            "version: "+IJFUSION_VERSION;
    
    public void run(String cmd) {
        if(cmd.equals("IJFUSION")) {
            IJ.showMessage("About IJFusion plugins", ABOUT_IJFUSION);
        }

    }
}
