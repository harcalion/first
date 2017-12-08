package es.upm.fi.gtd.first.algorithms;

/**
 * Clase para la transformación de un punto RGB a HSI y viceversa
 * @author Francisco Javier Merino Guardiola
 * @see <i>"Digital Image Processing"</i>(Gonzalez, Woods, Eddings) Capítulo 6.2.5 - <i>"The HSI Color Space"<i>
 */
public class RGBHSI {
	// EPSILON = el menor de los numeros representables
	private static final double EPSILON = Double.MIN_VALUE;
	
	
	public static double Normalizar(double p, int nbits) {
		return p/(Math.pow(2,nbits)-1); 
	}
	
	public static double Desnormalizar(double p, int nbits) {
		return p*(Math.pow(2,nbits)-1); 
	}
	
	/**
	 * Normaliza un pixel dada su profundidad ([0,2^nbits-1] -> [0,1])
	 * @param rgb array de puntos (double)
	 * @param nbits profundidad en bits del punto original
	 * @return array del pixel normalizado
	 */
	public static double[] Normalizar(double[] rgb, int nbits) {
		return Normalizar(rgb[0],rgb[1],rgb[2],nbits); 
	}

	/**
	 * Normaliza un pixel dada su profundidad ([0,2^nbits-1] -> [0,1])
	 * @param r valor double del pixel r 
	 * @param g valor double del pixel g
	 * @param b valor double del pixel b
	 * @param nbits profundidad en bits del punto original
	 * @return array del pixel normalizado
	 */
	public static double[] Normalizar(double r, double g, double b, int nbits) {
		double[] normalizado = new double[3];
		normalizado[0] = r/(Math.pow(2,nbits)-1);
		normalizado[1] = g/(Math.pow(2,nbits)-1);
		normalizado[2] = b/(Math.pow(2,nbits)-1);
		return normalizado;
	}

	
	/**
	 * Normaliza un pixel dada su profundidad ([0,1] -> [0,2^nbits-1])
	 * @param rgb array de puntos (double)
	 * @param nbits profundidad en bits del punto original
	 * @return array del pixel desnormalizado
	 */
	public static double[] Desnormalizar(double[] rgb, int nbits) {
		return Desnormalizar(rgb[0],rgb[1],rgb[2],nbits); 
	}
	
	/**
	 * 
	 * @param r valor double del pixel r
	 * @param g valor double del pixel g
	 * @param b valor double del pixel b
	 * @param nbits profundidad en bits del punto original
	 * @return array del pixel desnormalizado
	 */
	public static double[] Desnormalizar(double r, double g, double b, int nbits) {
		double[] desnormalizado = new double[3];
		desnormalizado[0] = r*(Math.pow(2,nbits)-1);
		desnormalizado[1] = g*(Math.pow(2,nbits)-1);
		desnormalizado[2] = b*(Math.pow(2,nbits)-1);
		return desnormalizado;
	}
	
	
	/**
	 * Transforma un pixel rgb (red green blue) a hsi (hue saturation intensity)
	 * @param rgb valores: red green blue
	 * @return valores hue-saturation-intensity
	 */
	public static double[] toHSI(double[] rgb) {
		return toHSI(rgb[0],rgb[1],rgb[2]);
	}
	
	/**
	 * Transforma un pixel rgb (red green blue) a hsi (hue saturation intensity)
	 * @param r valor red
	 * @param g valor green
	 * @param b valor blue
	 * @return valores hue-saturation-intensity
	 */
	public static double[] toHSI(double r, double g, double b) {
		double h, s, i;
		double numerador, denominador, theta;
		double[] hsi = new double[3];
		
		// Calculo de h
		numerador = 0.5*((r-g)+(r-b));
		denominador = Math.pow(Math.pow((r-g),2)+(r-b)*(g-b), 0.5);
		theta = Math.acos( numerador  / (denominador + EPSILON) );
		h = (b <= g) ? theta : 2*Math.PI - theta;
		h /= Math.PI*2;
		// Calculo de s
		numerador = 3*Math.min(Math.min(r,g), b);
		denominador = r+g+b;
		denominador = denominador == 0 ? EPSILON : denominador;
		s = 1 - (numerador / denominador);
		// Calculo de i
		i = (r+g+b)/3;
		
		// Si S = 0 entonces H tambien lo sera:
		if (s == 0) h = 0;
		
		// Resultado
		hsi[0] = h; hsi[1] = s; hsi[2] = i;
		return hsi;
	}
	
	
	/**
	 * Transforma un pixel hsi (hue saturation intensity) a rgb (red green blue) 
	 * @param hsi valores: hue-saturation-intensity
	 * @return valores red-green-blue
	 */
	public static double[] toRGB(double[] hsi) {
		return toRGB(hsi[0],hsi[1],hsi[2]);
	}
	
	
	/**
	 * Transforma un pixel hsi (hue saturation intensity) a rgb (red green blue)
	 * @param h valor hue
	 * @param s valor saturation
	 * @param i valor intensity
	 * @return valores red-green-blue
	 */
	public static double[] toRGB(double h, double s, double i) {
		double r, g, b;
		double[] rgb = new double[3];

		// Desnormalizamos h
		h = h * Math.PI*2;

		// Calculamos r,g,b en funcion del angulo de h
		if ((0 <= h) && (h<2*Math.PI/3)) {                // [0 - 120)
			b = i*(1-s);
			r = i*(1+((s*Math.cos(h))/Math.cos(Math.PI/3-h)));
			g = 3*i - (r+b);
		}
		else if ((2*Math.PI/3 <= h) && (h<4*Math.PI/3)) { // [120 - 240)
			h -= 2*Math.PI/3;
			r = i*(1-s);
			g = i*(1+((s*Math.cos(h))/Math.cos(Math.PI/3-h)));
			b = 3*i - (r+g);				
		}
		else {                                            // [240 - 360]
			h -= 4*Math.PI/3;
			g = i*(1-s);
			b = i*(1+((s*Math.cos(h))/Math.cos(Math.PI/3-h)));
			r = 3*i - (g+b);
		}
		
		// Evita desbordamientos

		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;
		if (r > 1) r = 1;
		if (g > 1) g = 1;
		if (b > 1) b = 1;
		
		// Resultado
		rgb[0] = r; rgb[1] = g; rgb[2] = b;
		return rgb;
	}
	

	
	
	public static void main(String[] args) {
		/*
		double r = 255, g = 20, b = 0;
		double[] rgb = new double[3];
		double[] hsi = new double[3];
		rgb = RGBHSI.Normalizar(r, g, b, 8);
		System.out.println("Normalizados:" + java.util.Arrays.toString(rgb));
		hsi = RGBHSI.toHSI(rgb);
		System.out.println("En HSI:" + java.util.Arrays.toString(hsi));
		rgb = RGBHSI.toRGB(hsi);
		System.out.println("De vuelta a RGB:" + java.util.Arrays.toString(rgb));
		System.out.println("----------");
		double h = 2, s = 255, i = 91;
		hsi = RGBHSI.Normalizar(h, s, i, 8);
		System.out.println("Normalizados:" + java.util.Arrays.toString(hsi));
		rgb = RGBHSI.toRGB(hsi);
		System.out.println("De vuelta a RGB:" + java.util.Arrays.toString(rgb));
		rgb = RGBHSI.Desnormalizar(rgb, 8);
		System.out.println("RGB desnormalizado:" + java.util.Arrays.toString(rgb));
		*/
		double r = 165, g = 165, b = 165;
		double[] rgb = new double[3];
		double[] hsi = new double[3];
		rgb = RGBHSI.Normalizar(r, g, b, 8);
		System.out.println("Normalizados:" + java.util.Arrays.toString(rgb));
		hsi = RGBHSI.toHSI(rgb);
		System.out.println("En HSI:" + java.util.Arrays.toString(hsi));
		rgb = RGBHSI.toRGB(hsi);
		System.out.println("De vuelta a RGB:" + java.util.Arrays.toString(rgb));
		System.out.println("----------");
	}

	
	
}