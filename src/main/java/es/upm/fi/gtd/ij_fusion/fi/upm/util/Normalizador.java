package es.upm.fi.gtd.ij_fusion.fi.upm.util;

public class Normalizador {
	double minimo;
	double maximo;
	
	public Normalizador(double min, double max) {
		minimo = min;
		maximo = max;
	}
	
	
	/**
	 * Normaliza un pixel de la escala <b>[minimo..maximo]</b> a la escala <b>[0..1]</b>
	 * @param pixel valor del pixel a normalizar
	 * @return valor del pixel normalizado
	 */
	public double Normalizar(double pixel) {
		return (pixel-minimo) / (maximo - minimo);
	}
	
	
	/**
	 * Convierte un pixel normalizado (escala <b>[0..1]</b>) a la escala <b>[minimo..maximo]</b>
	 * @param pixel valor del pixel a escalar
	 * @return valor del pixel escalado
	 */
	public double Escalar(double pixel) {
		return pixel * (maximo - minimo) + minimo;
	}
	
	
	/**
	 * 
	 * @return Máximo valor de la escala
	 */
	public double max() {
		return maximo;
	}

	
	/**
	 * 
	 * @return Mínimo valor de la escala
	 */
	public double min() {
		return minimo;
	}
	
	
}
