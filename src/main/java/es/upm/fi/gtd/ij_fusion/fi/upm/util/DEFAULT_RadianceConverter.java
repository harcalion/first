/*
 * DEFAULT_RadianceConverter.java
 *
 * Created on 13 de noviembre de 2007
 *
 */

package es.upm.fi.gtd.ij_fusion.fi.upm.util;

/**
 * Default digital/radiance value converter.
 * This kind of conversions should be used if the conversion betwen
 * randiances and digitals values depends on a simple coefficient.
 * Example: Quickbird
 *
 * @author Francisco Javier Merino Guardiola
 */
public class DEFAULT_RadianceConverter extends RadianceConverter {
    /** Default QuickBird coefficient for converter */
    final public float DEFAULT_COEFFICIENT = 1.0f;
    protected int nbands;
    /** Coefficient to be used in the conversion */
    protected float[] coefs;
    
    /** Conversor with default coefficient value */
    public DEFAULT_RadianceConverter(int nbands) {
        this.nbands = nbands;
        coefs = new float[nbands];
        for(int i=0;i<nbands;i++)
            coefs[i]=DEFAULT_COEFFICIENT;
    }
    
    /** Conversor using the specified coefficient value */
    public DEFAULT_RadianceConverter(float []coefficients) {
        this.coefs = coefficients;
    }
    
    
    public float toDigital(float radianceValue, int nband) {
        return radianceValue / this.coefs[nband];
    }
    
    
    public float[] toDigital(float[] radianceValues, int nband) {
        float[] res = new float[radianceValues.length];
        for(int i=0;i<radianceValues.length;i++)
            res[i] = radianceValues[i] / this.coefs[nband];
        return res;
    }
    
    
    public void toDigitalNA(float[] radianceValues, int nband) {
        for(int i=0;i<radianceValues.length;i++)
            radianceValues[i] = radianceValues[i] / this.coefs[nband];
    }
    
    
    
    public float toRadiance(float digitalValue, int nband) {
        return digitalValue * this.coefs[nband];
    }
    
    
    public float[] toRadiance(float[] digitalValues, int nband) {
        float[] res = new float[digitalValues.length];
        for(int i=0;i<digitalValues.length;i++)
            res[i] = digitalValues[i] * this.coefs[nband];
        return res;
    }
    
    
    public void toRadianceNA(float[] digitalValues, int nband) {
        for(int i=0;i<digitalValues.length;i++)
            digitalValues[i] = digitalValues[i] * this.coefs[nband];
    }
    
}
