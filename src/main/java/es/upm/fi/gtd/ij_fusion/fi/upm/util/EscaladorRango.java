package es.upm.fi.gtd.ij_fusion.fi.upm.util;


/**
 * Escalador de valores de un valores de pixeles a un rango especificado
 * haciendo uso de la media y la desviacion tipica.  
 * @author Francisco Javier Merino Guardiola
 *
 */
public class EscaladorRango {
	private double media_destino;
	private double desviacion_destino;
	private double media_origen;
	private double desviacion_origen;
	

	/*
	public EscaladorRango(double mean_origen, double desviacion_orgen, double mean) {
		media = mean;
		desviacion = desv;
	}
	*/
	
	
	
	/**
	 * Crea un escalador a partir de los pixeles de una imagen que contiene un rango de
	 * referencia (pixel_rangodestino) a partir de una imagen que se desea convertir a
	 * dicho rango (imagen pixels_rangoorigen).
	 */
	public EscaladorRango(double []pixels_rangodestino, double []pixels_rangoorigen) {
		media_destino = Media(pixels_rangodestino);
		desviacion_destino = DesviacionTipica(pixels_rangodestino, media_destino);
		media_origen = Media(pixels_rangoorigen);
		desviacion_origen = DesviacionTipica(pixels_rangoorigen, media_origen);
		//System.out.println("Media origen: " + media_origen);
		//System.out.println("Desviacion origen: " + desviacion_origen);
		//System.out.println("Media destino: " + media_destino);
		//System.out.println("Desviacion destino: " + desviacion_destino);
	}

	
	public double EscalarPixel(double pixel)
	{
		double a =  desviacion_destino / desviacion_origen;
		double b = media_destino - a * media_origen;
		//System.out.println("a: " + a);
		//System.out.println("b: " + b);
		return a*pixel + b;
	}
	
	public double EscalarPixelConMedia(double pixel) {
		return pixel - media_origen + media_destino;
	}
	
	/**
	 * Calcula la media aritmetica un array de pixeles dobles
	 * @param pixeles 
	 * @return valor de la media
	 */
	public static double Media(double []pixels) {
		double acumulador = 0.0;
		for (int i=0;i<pixels.length;i++)
			acumulador += pixels[i];
		return acumulador/pixels.length;
	}
	
	
	/**
	 * Calcula la desviacion tipica de un array de pixeles dobles
	 * @param pixels
	 * @param mean valor de la media
	 * @return valor de la desviacion tipica
	 */
	public static double DesviacionTipica(double []pixels, double mean) {
		double acumulador = 0.0;
		for (int i=0;i<pixels.length;i++)
			acumulador = java.lang.Math.pow(pixels[i] - mean, 2);
		acumulador /= pixels.length - 1;
		return java.lang.Math.pow(acumulador, 0.5);
	}

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double[] rangodestino = {0.3, 0.7, 0.2};
		double[] img1 = {1000, 2000, 3000};

		EscaladorRango esc = new EscaladorRango(rangodestino, img1);
		
		for (int i=0; i<img1.length; i++)
			System.out.println(esc.EscalarPixel(img1[i]));
	}

}
