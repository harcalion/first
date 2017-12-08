package es.upm.fi.gtd.ij;





import es.upm.fi.gtd.ij_fusion.*;
import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.FIHS;



/**
 * Plugin para ImageJ que realiza fusión utilizando la respuesta espectral
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Fusion_mediante_REMod implements PlugInFilter {

	public  ImageProcessor[] imp; // Pila de bandas multi
        public ImageProcessor panip;
        int nbits;
	
        public Fusion_mediante_REMod (ImageProcessor[] multi, int nbits, ImageProcessor pan)
        {
            imp = multi;
            this.nbits = nbits;
            panip = pan;
        }
        
	public int setup(String arg, ImagePlus imp) {
		//this.imp = imp;
		// new ImageConverter(imp).convertToGray16(); 
		return DOES_ALL+ STACK_REQUIRED + NO_CHANGES;
	}

	
	public void run(ImageProcessor ip) {
		// Abrimos la imagen pancromatica
		Multi_Lector imagen = new Multi_Lector();
		//ImageProcessor panip = imagen.LeerArchivo("Seleccione el archivo de la pancromática...").getProcessor();

		// Realizamos la fusion
		ejecutar_plugin(panip);
	}


	/**
	 * Ejecuta el plugin de fusion por analisis de componentes principales
	 * @param panip ImageProcessor con la imagen pancromatica
	 */
	public ImageProcessor[] ejecutar_plugin(ImageProcessor panip)
	{
		// Variables referidas a la imagen final
		int width = panip.getWidth();		// Ancho final es el de la pancromatica
		int height = panip.getHeight();		// Altura final es la de la pancromatica
		int npixels = width*height;     	// numero de pixeles
		int nbands = imp.length; 	// numero de bandas
		//double minv = panip.getMin();		// Minimo valor de pixel
		//double maxv = panip.getMin();		// Maximo valor de pixel
		
		// Redimensiona las bandas de la multi para que tengan el tamaño de la pan
		IJ.showStatus("Redimensionando imagenes...");
		ImageProcessor[] processor = new ImageProcessor[nbands];
		//ip.setInterpolate(true);
		for (int i=0;i<nbands;i++)
			processor[i] = imp[i].resize(width,height);
		
		// Crea la matriz en la que se almacenaran las imagenes (una por columna)
		IJ.showStatus("Realizando transformada FIHS...");
		int max = npixels; // status bar

		// Crea variables utilizadas para la transformada
		double[] r = new double[npixels];
		double[] g = new double[npixels];
		double[] b = new double[npixels];
		double[] ir = new double[npixels];
		double nr, ng, nb, nir, np;
		
		// Fusion mediante FIHS
		for (int y=0, k=0; y<height; y++)
		{
			for(int x=0; x<width; x++,k++)
			{
				nr = processor[0].getPixelValue(x,y);
				ng = processor[1].getPixelValue(x,y);
				nb = processor[2].getPixelValue(x,y);
				nir = processor[3].getPixelValue(x,y);
				np = panip.getPixelValue(x, y);
				
				/* Fast IHS
				punto = FIHS.FusionFIHS(nr, ng, nb, np);
				r[k] = punto[0];
				g[k] = punto[1];
				b[k] = punto[2];
				*/
				
				double n = (nr + ng + nb + nir) / 4;
				// Gamma resultante de la respuesta espacial
				double gamma = 0.8162230774206065;
				double delta = gamma*np-(1./4.)*(nr+ng+nb+nir);
				r[k] = nr + delta*nr/n;
				g[k] = ng + delta*ng/n;
				b[k] = nb + delta*nb/n;
				ir[k] = nir + delta*nir/n;
			}
			// Actualiza la barra de progreso
			IJ.showProgress(k, max);
		}
		
		
		// Crea la pila de imagenes resultantes
		//ImageStack pcastack = new ImageStack(width, height, panip.getColorModel());
		ImageStack stack = new ImageStack(width, height);


		// Creamos la pila con las bandas finales
		switch (nbits)
		{
		case 8: // En caso de ser una imagen de 8 bits
			byte[] puntosh_b = new byte[npixels];
			byte[] puntoss_b = new byte[npixels];
			byte[] puntosi_b = new byte[npixels];
			byte[] puntosir_b = new byte[npixels];
			
			for (int k=0; k<npixels; k++) {
				puntosh_b[k] = (byte) r[k];
				puntoss_b[k] = (byte) g[k];
				puntosi_b[k] = (byte) b[k];
				puntosir_b[k] = (byte) ir[k];
			}
			stack.addSlice("Banda1", puntosh_b);
			stack.addSlice("Banda2", puntoss_b);
			stack.addSlice("Banda3", puntosi_b);
			stack.addSlice("Banda4", puntosir_b);
			break;
		case 16: // En caso de ser una imagen de 16 bits
			short[] puntosh_s = new short[npixels];
			short[] puntoss_s = new short[npixels];
			short[] puntosi_s = new short[npixels];
			short[] puntosir_s = new short[npixels];
			
			for (int k=0; k<npixels; k++) {
				puntosh_s[k] = (short) r[k];
				puntoss_s[k] = (short) g[k];
				puntosi_s[k] = (short) b[k];
				puntosir_s[k] = (short) ir[k];
			}
			
			stack.addSlice("Banda1", puntosh_s);
			stack.addSlice("Banda2", puntoss_s);
			stack.addSlice("Banda3", puntosi_s);
			stack.addSlice("Banda4", puntosir_s);
			break;
		case 32: // En caso de ser una imagen de 32 bits
			
            int[] puntosh_i = new int[npixels];
			int[] puntoss_i = new int[npixels];
			int[] puntosi_i = new int[npixels];
			int[] puntosir_i = new int[npixels];

			for (int k=0; k<npixels; k++) {
				puntosh_i[k] = (int) r[k];
				puntoss_i[k] = (int) g[k];
				puntosi_i[k] = (int) b[k];
				puntosir_i[k] = (int) ir[k];
			}

			stack.addSlice("Banda1", puntosh_i);
			stack.addSlice("Banda2", puntoss_i);
			stack.addSlice("Banda3", puntosi_i);
			stack.addSlice("Banda4", puntosir_i);
			//IJ.showMessage("32 bits!");
			break;
		}
		ImageProcessor [] resultado = new ImageProcessor[nbands];
		for (int i = 0; i < nbands; i++)
                {
                    resultado[i] = stack.getProcessor(i+1);
                }
		// Creamos el contenedor de bandas y lo mostramos
		return resultado;
		//newim.getProcessor().setMinAndMax(rango.min(), rango.max());
		
		
	}
	
}
