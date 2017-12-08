package es.upm.fi.gtd.ij_fusion.fi.upm.util;


/**
 * Dados dos rangos crea una estructura campaz de transformar un pixel del rango
 * origen, a pixel de rango destino
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Escalador {
	private double minimo_origen;
	private double maximo_origen;
	private double minimo_destino;
	private double maximo_destino;
	
	public Escalador(int min_o, int max_o, int min_d, int max_d) {
		this.minimo_origen = min_o;
		this.maximo_origen = max_o;
		this.minimo_destino = min_d;
		this.maximo_destino = max_d;
	}
	
	public Escalador(double []pixels_rangodestino, double []pixels_rangoorigen)
	{
		// Obtenemos el maximo y minimo del rango destino
		double acumulador_minimo = Double.MAX_VALUE;
		double acumulador_maximo = Double.MIN_VALUE;
		for(int i=0;i<pixels_rangodestino.length;i++) {
			acumulador_minimo = acumulador_minimo <= pixels_rangodestino[i] ? acumulador_minimo : pixels_rangodestino[i];
			acumulador_maximo = acumulador_maximo >= pixels_rangodestino[i] ? acumulador_maximo : pixels_rangodestino[i];
		}
		minimo_destino = acumulador_minimo;
		maximo_destino = acumulador_maximo;
		// Obtenemos el maximo y minimo del rango origen
		acumulador_minimo = Double.MAX_VALUE;
		acumulador_maximo = Double.MIN_VALUE;
		for(int i=0;i<pixels_rangoorigen.length;i++) {
			acumulador_minimo = acumulador_minimo <= pixels_rangoorigen[i] ? acumulador_minimo : pixels_rangoorigen[i];
			acumulador_maximo = acumulador_maximo >= pixels_rangoorigen[i] ? acumulador_maximo : pixels_rangoorigen[i];
		}
		minimo_origen = acumulador_minimo;
		maximo_origen = acumulador_maximo;		
	}
	
	
	public double EscalarPixel(double pixel)
	{
		double escalado;
		
		// Normalizamos el pixel al rango [0..1]
		escalado = (pixel - minimo_origen) / (maximo_origen-minimo_origen);

		// Tranformamos al rango destino
		escalado = escalado * (maximo_destino-minimo_destino) - minimo_destino;
		
		return escalado;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {


	}

}
