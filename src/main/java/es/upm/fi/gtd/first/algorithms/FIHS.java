package es.upm.fi.gtd.first.algorithms;



/**
 * Fast IHS (Intensity Hue Saturation)
 * @author Francisco Javier Merino Guardiola
 *
 */
public class FIHS {
	double v1, v2;
	double h, s, i;
	double r, g, b;
	// Constantes de precalculos para mejorar la eficiencia
	final static double c1 = java.lang.Math.pow(2, .5)/6.; // √2/6
	final static double c2 = 1./java.lang.Math.pow(2, .5); // 1/√2
	
	
	
	
	/**
	 * Tranforma el pixel dado a su representacion IHS
	 * @param rgb
	 */
	public void toIHS(double[] rgb) {
		r = rgb[0]; g = rgb[1]; b = rgb[2];
		i = r/3. + g/3. + b/3.;
		v1 = -c1*r -c1*g + 2*c1*b;
		v2 = c2*r -c2*g;
		/*System.out.println("I: " + i);
		System.out.println("v1: " + v1);
		System.out.println("v2: " + v2);*/
	}
	
	public void toRGB() {
		r = i -c2*v1 +c2*v2;
		g = i -c2*v1 -c2*v2;
		b = i + (1/c2)*v1;
		/*System.out.println("R: " + r);
		System.out.println("G: " + g);
		System.out.println("B: " + b);*/
	}

	
	
	/**
	 * Dado un pixel r g b, realiza la fusion sustituyendo la componente
	 * intensidad por una proporcionada.
	 * @param r Componente Red
	 * @param g Componente Green
	 * @param b Componente Blue
	 * @param I Componente Intensity
	 * @return Valor RGB
	 */
	public static double[] FusionFIHS(double r, double g, double b, double I) {
		double[] rgb = new double[3];
		double i,v1,v2;
		// Calculamos v1 y v2
		i = r/3. + g/3. + b/3.;
		v1 = -c1*r -c1*g + 2*c1*b;
		v2 = c2*r -c2*g;
		// Cambiamos la componente I
		i = I;
		// Recalculamos el RGB
		rgb[0] = i -c2*v1 +c2*v2;
		rgb[1] = i -c2*v1 -c2*v2;
		rgb[2] = i + (1/c2)*v1;
		return rgb;
	}
	
	
	


}
