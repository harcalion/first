package es.upm.fi.gtd.ij_fusion;



import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;
import java.awt.event.*;
//import ij.plugin.PlugIn;
import java.awt.*;


	/**
	 * Plugin para la representacion grafica de la firma espectral de un punto
	 * Implementado mediante MouseListener
	 * @author Francisco Javier Merino Guardiola
	 *
	 */
	public class Firma_Espectral implements PlugInFilter, MouseListener {
		ImagePlus imp;

	
	/**
	 * Inicializacion del plugin
	 */
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL+STACK_REQUIRED+NO_CHANGES;
	}

	
	/**
	 * Codigo del plugin
	 */
	public void run(ImageProcessor ip) {
		ImageWindow win = imp.getWindow();
		ImageCanvas canvas = win.getCanvas();
		canvas.addMouseListener(this);
		IJ.showStatus("Haga click en el punto deseado de la imagen...");
	}
	
	
	/////////////////////////////////////////////
	// IMPLEMENTACION DE LOS LISTENER DEL RATON
	/////////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		int x, y; /* Pixel */
		float []bandas = new float[imp.getStackSize()];
		float []values = new float[imp.getStackSize()];
		x = e.getPoint().x;
		y = e.getPoint().y;
		for(int i=0;i<imp.getStackSize();i++) {
			values[i] = imp.getStack().getProcessor(i+1).getPixel(x,y);
			bandas[i] = i+1;
		}

		
        Plot plot = new Plot("Firma espectral ("+x+","+y+")","Número de banda","Nivel de gris",bandas,values);
        plot.setColor(Color.blue);
        //plot.setLimits(1, , 0, java.lang.Math.pow (2, imp.getBitDepth()));
        plot.setLimits(1, imp.getStackSize(), imp.getProcessor().getMin(), imp.getProcessor().getMax());
        plot.addPoints(bandas,values,PlotWindow.BOX);
        plot.setLineWidth(2);
        plot.show();
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
}

