/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first.algorithms;

/**
 *
 * @author Meri
 */
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
            double max = maximo, min = minimo;
            if (maximo > Byte.MAX_VALUE)
            {
                max = Byte.MAX_VALUE-1;
            }
            if (minimo < Byte.MIN_VALUE)
            {
                min = Byte.MIN_VALUE -1;
            }
		return pixel * (max - min) + min;
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
