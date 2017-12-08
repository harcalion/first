package es.upm.fi.gtd.ij_fusion;




import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;


public class Rango_Bits implements PlugInFilter {
/**
 * Convierte una imagen de RGB a HSI
*/
	int width, height, size;
	

	
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {
		/*
		GenericDialog gd = new GenericDialog("Seleccione el numero de bits a mostrar");
		gd.addNumericField("Numero bits:",0,0,10,"");
		gd.showDialog();
		if(gd.wasCanceled()) return;
		ip.setMinAndMax(0, Math.pow(2, gd.getNextNumber()));
		*/
		GenericDialog gd = new GenericDialog("Seleccione el rango de valores a mostrar");
		gd.addNumericField("Minimo:",0,0,10,"");
		gd.addNumericField("Maximo:",0,0,10,"");
		gd.showDialog();
		if(gd.wasCanceled()) return;
		ip.setMinAndMax(gd.getNextNumber(),gd.getNextNumber());

	}

}
