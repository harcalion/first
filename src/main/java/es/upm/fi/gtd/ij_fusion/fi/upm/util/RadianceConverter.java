/*
 * RadianceConverter.java
 *
 * Created on 13 de noviembre de 2007, 13:53
 *
 */

package es.upm.fi.gtd.ij_fusion.fi.upm.util;

/**
 *
 * Abstract class that must be implemented for each type of senssor (ikonos,
 * QB, SPOT, Landasat,...) in order to convert between digital and radiance
 * values.
 *
 * @author Francisco Javier Merino Guardiola
 */
public abstract class RadianceConverter {
    
    /** Converts a digital value to radiance value */
    abstract public float toRadiance(float digitalValue, int nband);
    /** Converts an array of digital values to an array of radiance values */
    abstract public float[] toRadiance(float[] digitalValues, int nband);
    /** Converts an array of digital values to an array of radiance values. The
     * difference with toRadiance is that this method doesnt allocate memory for a
     * new array. This is useful for big images in order not to waste memory */
    abstract public void toRadianceNA(float[] digitalValues, int nband);
    /** Converts a radiance value to digital value */
    abstract public float toDigital(float randiaceValue, int nband);
    /** Converts an array of radiance values to an array of digital values */
    abstract public float[] toDigital(float[] radianceValues, int nband);
    /** Converts an array of radiance values to an array of digital values. The
     * difference with toDigital is that this method doesnt allocate memory for a
     * new array. This is useful for big images in order not to waste memory */
    abstract public void toDigitalNA(float[] radianceValues, int nband);
}
