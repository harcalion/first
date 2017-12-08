package es.upm.fi.gtd.ij_fusion;

import ij.*;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.plugin.filter.Convolver;


public class Indice_Zhou implements PlugInFilter {
	ImagePlus fusionada;
	ImagePlus pancromatica;
	double[][] banda1;
        
	/**
	 * Inicializacion del plugin
	 */
	public int setup(String arg, ImagePlus imp) {
		this.fusionada = imp;
		return DOES_ALL+ STACK_REQUIRED + NO_CHANGES;
	}
	
	
	/**
	 * Implementacion del plugin
	 */
	public void run(ImageProcessor arg0) {
		Multi_Lector imagen = new Multi_Lector();
		pancromatica = imagen.LeerArchivo("Seleccione el archivo de la pancromatica...");
		IJ.showMessage("Indice: " + Zhou(fusionada, pancromatica));
	}

	public double Zhou(ImagePlus fus, ImagePlus pan) {
		int nbands = fus.getStackSize();
		float[] kernel_laplaciano = {-1,-1,-1,-1,8,-1,-1,-1,-1};
		int kw = 3; int kh = 3;
                Convolver conv = new Convolver();
		
		// Leemos los procesadores de la imagen fusionada
		ImageProcessor[] pfus = new ImageProcessor[fus.getStackSize()];

		for(int i=0;i<nbands;i++)
			pfus[i] = fus.getStack().getProcessor(i+1).duplicate().convertToFloat();
                

		// Leemos el procesador de la pancromatica
		ImageProcessor ppan = pan.getProcessor().convertToFloat();
                conv.convolve(ppan, kernel_laplaciano, kw, kh);
		float[] panpixels = (float[]) ppan.getPixels();
                
		// Calculamos la media de la pancromatica
		double mean_pan = 0.0;
		for (int i=0;i<panpixels.length;i++)
			mean_pan += panpixels[i];
		mean_pan /= panpixels.length;
		
		// Procesa todas las bandas y calcula los indices para cada una de ellas
		// respecto de la pancromatica
		double[] indices = new double[nbands]; 
		for (int z=0;z<nbands;z++) {
			ImageProcessor ip = pfus[z];
			conv.convolve(ip, kernel_laplaciano, kw, kh);
			
			float[] fuspixels = (float[]) ip.getPixels();
			

			//Calculamos la media de la banda actual de la fusionada
			double mean_fus = 0.0;
			for (int i=0;i<fuspixels.length;i++)
				mean_fus += fuspixels[i];
			mean_fus /= fuspixels.length;			
			
			// Calculamos el numerador
			double num = 0.0;
                            for (int i=0;i<fuspixels.length;i++)
				num += (fuspixels[i]-mean_fus)*(panpixels[i]-mean_pan);
			// Calculamos el denominador
			double a = 0.0;
			for (int i=0;i<fuspixels.length;i++)
				a += java.lang.Math.pow((fuspixels[i]-mean_fus),2);
			double b = 0.0;
			for (int i=0;i<fuspixels.length;i++)
				b += java.lang.Math.pow((panpixels[i]-mean_pan),2);
			double den = java.lang.Math.pow(a*b,0.5);
			// Calculamos el indice de la banda actual
			indices[z] = num/den;
		}


		double indice = 0.0;
		for (int z=0;z<nbands;z++)
			indice += indices[z];
		indice /= nbands;

		return indice;
	}
	
        
        public static void main(String args[]) {
            float[] fuspixels = {1,2,3,4};
            float[] panpixels = {7,8,5,6};
            
            
            double mean_pan = 0.0;
            for (int i=0;i<panpixels.length;i++)
                    mean_pan += panpixels[i];
            mean_pan /= panpixels.length;
            
            double mean_fus = 0.0;
            for (int i=0;i<fuspixels.length;i++)
                    mean_fus += fuspixels[i];
            mean_fus /= fuspixels.length;			

            // Calculamos el numerador
            double num = 0.0;
            
            for (int i=0;i<fuspixels.length;i++)
                    num += (fuspixels[i]-mean_fus)*(panpixels[i]-mean_pan);
            // Calculamos el denominador
            double a = 0.0;
            for (int i=0;i<fuspixels.length;i++)
                    a += java.lang.Math.pow((fuspixels[i]-mean_fus),2);
            double b = 0.0;
            for (int i=0;i<fuspixels.length;i++)
                    b += java.lang.Math.pow((panpixels[i]-mean_pan),2);
            double den = java.lang.Math.pow(a*b,0.5);
            // Calculamos el indice de la banda actual
            System.out.println(num/den);
        }
        
}
