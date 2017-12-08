/*
 * SPOT_RadianceConverter.java
 *
 * Created on 13 de noviembre de 2007
 *
 */

package es.upm.fi.gtd.ij_fusion.fi.upm.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * SPOT digital/radiance value converter.
 *
 * L = X/A + B
 *  where:
 *    - L is the resulting physical value expressed in PHYSICAL_UNIT
 *    - X is the radiometric value at a given pixel location as stored in the raster file (unitless).
 *    - A is the gain (PHYSICAL_GAIN)
 *    - B is the bias (PHYSICAL_BIAS)
 *
 * @author Francisco Javier Merino Guardiola
 */




public class SPOT_RadianceConverter extends RadianceConverter {
    // Regular expressions for the XML file

    private static String NBANDS_PATTERN = "<NBANDS>\\s*(\\d+)\\s*</NBANDS>\\s*$";
    private static String BINFO_PATTERN = "\\s*<Spectral_Band_Info>\\s*$";
    private static String EINFO_PATTERN = "\\s*</Spectral_Band_Info>\\s*$";
    private static String PBIAS_PATTERN = "<PHYSICAL_BIAS>\\s*(\\d+.?\\d*)\\s*</PHYSICAL_BIAS>\\s*$";
    private static String PGAIN_PATTERN = "<PHYSICAL_GAIN>\\s*(\\d+.?\\d*)\\s*</PHYSICAL_GAIN>\\s*$";
    private static String BINDEX_PATTERN = "<BAND_INDEX>\\s*(\\d+)\\s*</BAND_INDEX>\\s*$";
    
    
    
    // Physical measure gain for each of the spectral band
    private float[] gain;
    // Physical measure bias for each of the spectral band
    private float[] bias;
    
    /** SPOT conversor using the specified gain and bias values */
    public SPOT_RadianceConverter(float[] gain, float[] bias) {
        this.bias = bias;
        this.gain = gain;
    }
    
    /** SPOT conversor reading the gain/bias values from the .DIM file */
    public SPOT_RadianceConverter(String multidimfile, String pandimfile) {
        parseMultiDIMFile(multidimfile);
        parsePanDIMFile(pandimfile);
        System.out.println(java.util.Arrays.toString(bias));
        System.out.println(java.util.Arrays.toString(gain));
    }
    
    
    public float toDigital(float radianceValue, int nband) {
        // L = X/A + B
        return radianceValue/gain[nband] + bias[nband];
    }
    
    
    public float[] toDigital(float[] radianceValues, int nband) {
        float[] res = new float[radianceValues.length];
        for(int i=0;i<radianceValues.length;i++)
            res[i] = toDigital(radianceValues[i], nband);
        return res;
    }
    
    
    public void toDigitalNA(float[] radianceValues, int nband) {
        for(int i=0;i<radianceValues.length;i++)
            radianceValues[i] = toDigital(radianceValues[i], nband);
    }
    
    
    
    public float toRadiance(float digitalValue, int nband) {
        // X = A*(L-B)
        return gain[nband]*(digitalValue-bias[nband]);
    }
    
    
    public float[] toRadiance(float[] digitalValues, int nband) {
        float[] res = new float[digitalValues.length];
        for(int i=0;i<digitalValues.length;i++)
            res[i] = toRadiance(digitalValues[i], nband);
        return res;
    }
    
    
    public void toRadianceNA(float[] digitalValues, int nband) {
        for(int i=0;i<digitalValues.length;i++)
            digitalValues[i] = toRadiance(digitalValues[i], nband);
    }
    
    
    
    
    private void parseMultiDIMFile(String file) {
        boolean section = false, bands = false;
        float gain=0.0f, bias=0.0f;
        int nbands = 0;
        int band = 0;
        int processed = 0;
        
        try {
            java.io.FileReader fr = new java.io.FileReader(file);
            java.io.BufferedReader br = new java.io.BufferedReader(fr);
            String line;
            // Parse the file line by line
            while ((line = br.readLine()) != null ){
                // While not section or nbands tags are reached
                if (!section || !bands) {
                    if(line.matches(NBANDS_PATTERN)) {
                        Pattern p = Pattern.compile(NBANDS_PATTERN);
                        Matcher m = p.matcher(line);
                        if(!m.find()) {System.err.println("Multi DIM file error #0! "+file); return;}
                        nbands = Integer.parseInt(m.group(1));
                        // Reserves memory for nbands + 1 (multi+pan)
                        this.bias = new float[nbands+1];
                        this.gain = new float[nbands+1];
                        bands = true;
                    }
                    // Spectral section of the file reached
                    if(line.matches(BINFO_PATTERN)) section = true;
                    continue;
                }
                // Reads the Physical BIAS value
                if (line.matches(PBIAS_PATTERN)) {
                    Pattern p = Pattern.compile(PBIAS_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Multi DIM file error #1! "+file); return;}
                    bias = Float.parseFloat(m.group(1));
                }
                // Reads the Physical GAIN value
                if (line.matches(PGAIN_PATTERN)) {
                    Pattern p = Pattern.compile(PGAIN_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Multi DIM file error #2! "+file); return;}
                    gain = Float.parseFloat(m.group(1));
                }
                // Reads the band index number
                if (line.matches(BINDEX_PATTERN)) {
                    Pattern p = Pattern.compile(BINDEX_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Multi DIM file error #3! "+file); return;}
                    band = Integer.parseInt(m.group(1));
                }

                // Spectral section finished
                if (line.matches(EINFO_PATTERN)) {
                    this.bias[band] = bias;
                    this.gain[band] = gain;
                    processed++;
                }
                
                // Checks if more info needs to be readed
                if (processed==nbands)
                    break;
                
            }
            // Unexpected end of file
            if (line == null) {
                System.err.println("Invalid DIM file! "+file);
                return;
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    /**
     *
     * NOTE: Require parseMultiDIM to run before! (it allocates memory)
     */
    private void parsePanDIMFile(String file) {
        boolean section = false;
        int band;
        
        try {
            java.io.FileReader fr = new java.io.FileReader(file);
            java.io.BufferedReader br = new java.io.BufferedReader(fr);
            String line;
            // Parse the file line by line
            while ((line = br.readLine()) != null ){
                // Spectral section of the file reached
                if (!section) {
                    if(line.matches(BINFO_PATTERN)) section = true;
                    continue;
                }
                // Reads the Physical BIAS value
                if (line.matches(PBIAS_PATTERN)) {
                    Pattern p = Pattern.compile(PBIAS_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Pan DIM file error #1! "+file); return;}
                    this.bias[0]=Float.parseFloat(m.group(1));
                    continue;
                }
                // Reads the Physical GAIN value
                if (line.matches(PGAIN_PATTERN)) {
                    Pattern p = Pattern.compile(PGAIN_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Pan DIM file error #2! "+file); return;}
                    this.gain[0]=Float.parseFloat(m.group(1));
                    continue;
                }
                // Spectral section finished
                if (line.matches(EINFO_PATTERN)) {
                    break;
                }
            }
            // Unexpected end of file
            if (line == null) {
                System.err.println("Invalid DIM file! "+file);
                return;
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SPOT_RadianceConverter src = new SPOT_RadianceConverter("/tmp/multi.dim","/tmp/pan.dim");
    }
    
}
