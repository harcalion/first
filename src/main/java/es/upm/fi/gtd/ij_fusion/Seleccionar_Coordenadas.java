package es.upm.fi.gtd.ij_fusion;



import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;

/**
 * Plugin de seleccion de region de interes rectangular
 * @author Francisco Javier Merino Guardiola
 */
public class Seleccionar_Coordenadas implements PlugInFilter {
	
	/**
	 * Inicializacion del plugin
	 */
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}

	/**
	 * Codigo del plugin
	 */
	public void run(ImageProcessor ip) {
		// Interfaz
		GenericDialog gd = new GenericDialog("Seleccione origen y datos de la region");
		gd.addNumericField("Origen X:",0,10,10,"");
		gd.addNumericField("Origen Y:",0,10,10,"");
		gd.addNumericField("Anchura:",0,10,10,"pixeles");
		gd.addNumericField("Altura:",0,10,10,"pixeles");
		gd.showDialog();
		if(gd.wasCanceled()) return;
		
		// Seleccion de coordenadas
		WindowManager.getCurrentWindow().getImagePlus().setRoi((int)gd.getNextNumber(),(int)gd.getNextNumber(),(int)gd.getNextNumber(),(int)gd.getNextNumber());
	}
	
}
