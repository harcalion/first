package es.upm.fi.gtd.ij_fusion;



import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.HSI;



/**
 * Plugin para ImageJ que realiza la transformacion de IHS a RGB.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class IHS_a_RGB implements PlugInFilter {

	ImagePlus imp;

	
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL+ STACK_REQUIRED + NO_CHANGES;
	}

	
	public void run(ImageProcessor ip) {
		ImageProcessor panip = null;
		panip = imp.getStack().getProcessor(1);
		ejecutar_plugin(panip);
	}


	/**
	 * Ejecuta el plugin de fusion por analisis de componentes principales
	 * @param panip ImageProcessor con la imagen pancromatica
	 */
	public void ejecutar_plugin(ImageProcessor panip)
	{
		IJ.showStatus("Redimensionando imagenes...");
		ImageProcessor[] processor = new ImageProcessor[imp.getStackSize()];
		//ip.setInterpolate(true);
		for (int i=0;i<processor.length;i++)
			processor[i] = imp.getStack().getProcessor(i+1).resize(panip.getWidth(),panip.getHeight());
		
		// Crea la matriz en la que se almacenaran las imagenes (una por columna)
		IJ.showStatus("Leyendo los puntos...");
		int max = panip.getHeight()*panip.getWidth(); // status bar

		// Crea variables utilizadas para la transformada
		double[] punto;
		double[] h = new double[imp.getWidth()*imp.getHeight()];
		double[] s = new double[imp.getWidth()*imp.getHeight()];
		double[] i = new double[imp.getWidth()*imp.getHeight()];
		double r, g, b;
		// Lectura de las imagenes en la matriz de Jama
		for (int y=0, k=0; y<panip.getHeight(); y++)
		{
			for(int x=0; x<panip.getWidth(); x++,k++)
			{
				r = HSI.Normalizar((double)processor[0].getPixelValue(x,y), imp.getBitDepth());
				g = HSI.Normalizar((double)processor[1].getPixelValue(x,y), imp.getBitDepth());
				b = HSI.Normalizar((double)processor[2].getPixelValue(x,y), imp.getBitDepth());
				punto = HSI.toRGB(r, g, b);
				h[k] = HSI.Desnormalizar(punto[0], imp.getBitDepth());
				s[k] = HSI.Desnormalizar(punto[1], imp.getBitDepth());
				i[k] = HSI.Desnormalizar(punto[2], imp.getBitDepth());
			}
					
			// Actualiza la barra de progreso
			IJ.showProgress(k, max);
		}
		
		// Cambiar la primera componente por la pancromatica

		// Crea la pila de imagenes resultantes
		ImageStack pcastack = new ImageStack(panip.getWidth(), panip.getHeight(), panip.getColorModel());

		byte[] puntosh = new byte[h.length];
		byte[] puntoss = new byte[h.length];
		byte[] puntosi = new byte[h.length];
		
		for (int k=0; k<puntosh.length; k++)
		{
			puntosh[k] = (byte) h[k];
			puntoss[k] = (byte) s[k];
			puntosi[k] = (byte) i[k];
		}

		pcastack.addSlice("H", puntosh);
		pcastack.addSlice("S", puntoss);
		pcastack.addSlice("I", puntosi);

		ImagePlus newim=new ImagePlus("RGB -> HSI",pcastack);
		newim.show();
	}
	
}
