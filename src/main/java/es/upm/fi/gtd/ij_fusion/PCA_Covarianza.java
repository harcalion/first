package es.upm.fi.gtd.ij_fusion;





import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.PCA;
import es.upm.fi.gtd.first.jama.Matrix;


/**
 * Plugin para ImageJ que realiza el analisis de componentes principales
 * haciendo uso de la matriz de covarianza.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class PCA_Covarianza implements PlugInFilter {
	ImagePlus imp;

	/***
	 * Incializacion del plugin
	 */
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL+ STACK_REQUIRED + NO_CHANGES;
	}

	/**
	 * Implementacion del plugin
	 */
	public void run(ImageProcessor ip) {
		ejecutar_plugin();
	}


	/**
	 * Implementacion del paso a matrices y utilizacion de la transformada PCA
	 */
	public void ejecutar_plugin()
	{
		ImageProcessor[] processor = new ImageProcessor[imp.getStackSize()];
		for (int i=0;i<processor.length;i++)
			processor[i] = imp.getStack().getProcessor(i+1);

		// Crea la matriz en la que se almacenaran las imagenes (una por columna)
		IJ.showStatus("Leyendo y creando matrices...");
		int max = imp.getHeight()*imp.getWidth(); // status bar
		Matrix X = new Matrix(imp.getHeight()*imp.getWidth(),imp.getStackSize());
		// Lectura de las imagenes en la matriz de Jama
		for (int y=0, k=0; y<imp.getHeight(); y++)
		{
			for(int x=0; x<imp.getWidth(); x++,k++)
				for (int z=0; z<imp.getStackSize(); z++)
					X.set(k, z, processor[z].getPixelValue(x,y));
			// Actualiza la barra de progreso
			IJ.showProgress(k, max);
		}
		
		// Analisis de Componentes Principales
		PCA pca = new PCA(X);
                X = null;
		IJ.showStatus("Realizando transformada...");
		Matrix res= pca.Transformada(imp.getStackSize());
		IJ.showStatus("PCA Finalizado, mostrando resultados.");

		//res = pca.Transformada_Inversa(res);
		pca.EscalarValores(res, 8);
		
		double[][] transformadas = res.transpose().getArray();


		ImageStack pcastack= imp.createEmptyStack();
		for (int n=0;n<imp.getStackSize();n++) {
			byte[] puntos = new byte[transformadas[0].length];
			for (int i=0; i<transformadas[0].length; i++)
				puntos[i] = (byte) transformadas[n][i];
			pcastack.addSlice("Componente "+n, puntos);
		}
		ImagePlus newim=new ImagePlus("Transformada PCA",pcastack);
		newim.show();
	}
	
}
