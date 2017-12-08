/*
 * Estadisticas.java
 *
 * Created on 11 de noviembre de 2007, 1:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.upm.fi.gtd.ij_fusion.fi.upm.util;


/**
 *
 * @author Francisco Javier Merino Guardiola
 */
public class Estadisticas {
    public final static int NOP=0, MEDIA=1, DESVIACION_ESTANDAR=2;
    private float media;
    private boolean media_calculada = false;
    private float desviacion_tipica;
    
    
    private void calcularMedia(float[]pixels) {
        // Calculamos la media
        float acumulador = 0.0f;
        for (int i=0;i<pixels.length;i++)
            acumulador += pixels[i];
        this.media = acumulador / pixels.length;
        this.media_calculada = true;
    }
    
    
    private void calcularDesviacionTipica(float[]pixels) {
        // Comprobamos si la media fue calculada anteriormente
        if (!this.media_calculada) calcularMedia(pixels);
        // Calculamos la desviacion tipica
        float acumulador = 0.0f;
        for (int i=0;i<pixels.length;i++)
            acumulador += (float)java.lang.Math.pow(pixels[i] - this.media, 2);
        acumulador /= pixels.length - 1;
        this.desviacion_tipica = (float)java.lang.Math.pow(acumulador, 0.5);
    }
    
    /**
     * Constructor de estadisticas, por defecto calcula todas las estadísticas
     * @param pixels 
     */
    public Estadisticas(float []pixels) {
        calcularMedia(pixels);
        calcularDesviacionTipica(pixels);
    }
    
    public Estadisticas(float []pixels,int operador) {
        switch(operador) {
            case MEDIA: calcularMedia(pixels); break;
            case DESVIACION_ESTANDAR: calcularDesviacionTipica(pixels); break;
            case NOP:
            default: break;
        }
    }
    
    public float getMedia() {
        return this.media;
    }
    
    public float getDesviacionTipica() {
        return this.desviacion_tipica;
    }
    
}
