package es.upm.fi.gtd.ij_fusion;


import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.PCA;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.EscaladorRango;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.Escalador;

import es.upm.fi.gtd.first.jama.Matrix;


/**
 * Plugin para ImageJ que realiza fusion mediante el analisis de componentes
 * principales.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Fusion_por_PCA_bak implements PlugInFilter {
	// Contenedor de imagenes multiespectrales
	ImagePlus imp;

	/**
	 * Inicializacion del plugin
	 */
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;

		new ImageConverter(imp).convertToGray16();
		return DOES_ALL+ STACK_REQUIRED + NO_CHANGES;
	}

	/**
	 * Implementacion del plugin
	 */
	public void run(ImageProcessor ip) {
		Multi_Lector imagen = new Multi_Lector();
		ImageProcessor panip = imagen.LeerArchivo("Seleccione el archivo de la pancromatica...").getProcessor();
		ejecutar_plugin(panip);
	}


	/**
	 * Ejecuta el plugin de fusion por analisis de componentes principales
	 * @param panip ImageProcessor con la imagen pancromatica
	 */
	public void ejecutar_plugin(ImageProcessor panip)
	{
		// Variables referidas a la imagen final
		int width = panip.getWidth();		// Ancho final es el de la pancromatica
		int height = panip.getHeight();		// Altura final es la de la pancromatica
		int npixels = width*height;     	// numero de pixeles
		int nbands = imp.getStackSize(); 	// numero de bandas
		
				
		IJ.showStatus("Redimensionando bandas...");
		ImageProcessor[] processor = new ImageProcessor[nbands];
		for (int i=0;i<nbands;i++) {
			imp.getStack().getProcessor(i+1).setInterpolate(true);
			processor[i] = imp.getStack().getProcessor(i+1).resize(width, height);
		}
		
		// Crea la matriz en la que se almacenaran las imagenes (una por columna)
		IJ.showStatus("Leyendo y creando matrices para la PCA...");
		int max = npixels; // status bar
		Matrix X = new Matrix(npixels, nbands);
		// Lectura de las imagenes en la matriz de Jama
		for (int y=0, k=0; y<height; y++)
		{
			for(int x=0; x<width; x++,k++)
				for (int z=0; z<nbands; z++)
					X.set(k, z, processor[z].getPixelValue(x,y));
			// Actualiza la barra de progreso
			IJ.showProgress(k, max);
		}
		
		// Analisis de Componentes Principales
		PCA pca = new PCA(X);
		X = null; // Liberamos algo mas de memoria
		IJ.showStatus("Realizando transformada...");
		Matrix res = pca.Transformada(nbands);
		
		
		// Calcular el cambio de escala
		double[] primeracomponente = new double[npixels];
		for (int i=0, k=0; i<height; i++)
			for (int j=0; j<width; j++,k++)
				primeracomponente[k] = res.get(k, 0);
		
		// Teniendo cuenta la media de todas (HACER ESTO CON MEDIAS-DESVIACIONES ES NEFASTO!)
		/*
		double[] primeracomponente = new double[npixels*nbands];
		for (int z=0; z<nbands; z++)
			for (int i=0, k=0; i<height; i++)
				for (int j=0; j<width; j++,k++)
					primeracomponente[k] = res.get(k, z);
		*/

		double[] pancromatica = new double[npixels];
		for (int i=0, k=0; i<height; i++)
			for (int j=0; j<width; j++,k++)
				pancromatica[k] = panip.getPixel(j,i);
		
		// Metodo escalado de Chelo
		//EscaladorRango rango = new EscaladorRango(primeracomponente, pancromatica);
		// Mi metodo de escalado
		Escalador rango = new Escalador(primeracomponente, pancromatica);
		primeracomponente = null;
		
		// Cambiar la primera componente por la pancromatica
		IJ.showStatus("Cambiando la primera componente por la pancromatica...");

		for (int i=0, k=0; i<height; i++)
			for (int j=0; j<width; j++,k++)
				res.set(k, 0, rango.EscalarPixel(pancromatica[k]));
				//res.set(k, 0, pca.CambiaDeRangoAPCA(panip.getPixel(j, i), imp.getBitDepth()));
		pancromatica = null;

		// Realiza la transformacion inversa
		res = pca.Transformada_Inversa(res);
		
		
		// Compruebo que los pixeles no se salgan de valor
		for (int z=0; z<nbands; z++)
			for (int i=0, k=0; i<height; i++)
				for (int j=0; j<width; j++,k++)
				{
					double valor = res.get(k, z); 
					if(valor < panip.getMin()) res.set(k, z, panip.getMin());
					if(valor > panip.getMax()) res.set(k, z, panip.getMax());
				}
		
			
			
			
		// Crea la pila de imagenes resultantes
		double[][] transformadas = res.transpose().getArray();
		res = null;
		ImageStack pcastack = new ImageStack(width, height);
		
		
		
		
		
		// Creamos la pila con las bandas finales
		switch (imp.getBitDepth())
		{
		case 8: // En caso de ser una imagen de 8 bits
			for (int n=0;n<nbands;n++) {
				byte[] puntos_b = new byte[transformadas[0].length];
				for (int i=0; i<transformadas[0].length; i++)
					puntos_b[i] = (byte) transformadas[n][i];
				pcastack.addSlice("Componente "+n, puntos_b);
			}
			break;
		case 16: // En caso de ser una imagen de 16 bits
			for (int n=0;n<nbands;n++) {
				short[] puntos_s = new short[transformadas[0].length];
				for (int i=0; i<transformadas[0].length; i++)
					puntos_s[i] = (short) transformadas[n][i];
				pcastack.addSlice("Componente "+n, puntos_s);
			}
			break;
		case 32: // En caso de ser una imagen de 32 bits
            for (int n=0;n<nbands;n++) {
				int[] puntos_i = new int[transformadas[0].length];
				for (int i=0; i<transformadas[0].length; i++)
					puntos_i[i] = (int) transformadas[n][i];
				pcastack.addSlice("Componente "+n, puntos_i);
			}
			
			IJ.showMessage("32 bits!");
			break;
		}
		
		// Creamos el contenedor de bandas y lo mostramos
		ImagePlus newim=new ImagePlus("Fusionada PCA",pcastack);
		newim.getProcessor().setMinAndMax(panip.getMin(), panip.getMax());
		newim.show();
	}
	
}
