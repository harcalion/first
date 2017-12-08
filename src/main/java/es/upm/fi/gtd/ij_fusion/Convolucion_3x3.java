package es.upm.fi.gtd.ij_fusion;

import ij.ImageStack;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.Convolucion;
import java.awt.Color;
import java.awt.Image;


public class Convolucion_3x3 implements PlugInFilter {
	ImagePlus imp;
	double[][] banda1;
        
	/**
	 * Inicializacion del plugin
	 */
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}
	
	
	/**
	 * Implementacion del plugin
	 */
	public void run(ImageProcessor arg0) {
		double[][]kernel = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
		//double[][]kernel = {{0,0,0},{0,1,0},{0,0,0}};
		double[][]convolucionada;
		byte[] img;
		
        banda1 = new double[imp.getHeight()][imp.getWidth()];
        imp.getStackSize();
        
        for(int i=0;i<imp.getHeight();i++)
            for(int j=0;j<imp.getWidth();j++)
                banda1[i][j] = imp.getStack().getProcessor(1).getPixel(j,i);

        
        Convolucion a = new Convolucion(banda1,es.upm.fi.gtd.ij_fusion.fi.upm.util.Convolucion.CONVOLUCION_CEROS);
        convolucionada = a.convolucionar(kernel);

        img = new byte[imp.getHeight()*imp.getWidth()];
       
        
        for(int i=0,k=0;i<imp.getHeight();i++)
        	for(int j=0;j<imp.getWidth();j++,k++)
        		img[k] = (byte) convolucionada[i][j];
        
        
        ImageStack stack = new ImageStack(imp.getWidth(), imp.getHeight());
        stack.addSlice("prueba", img);
        
        ImagePlus imagen = new ImagePlus("imagen",stack);
        imagen.show();
	}
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {


	}

}
