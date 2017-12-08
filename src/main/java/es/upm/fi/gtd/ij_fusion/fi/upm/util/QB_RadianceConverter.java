/*
 * QB_RadianceConverter.java
 *
 * Created on 13 de noviembre de 2007, 13:57
 *
 */

package es.upm.fi.gtd.ij_fusion.fi.upm.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * QuickBird digital/radiance value converter.
 * @author Francisco Javier Merino Guardiola
 */
public class QB_RadianceConverter extends DEFAULT_RadianceConverter {
    /** Number of bands of the QB Images: Pan, R, G, B, NIR */
    public final static int NBANDS = 5;
    /** Bands strings */
    public final static String []BANDS= { "BAND_P", "BAND_B", "BAND_G", "BAND_R", "BAND_N" };
    /** Panchromatic band index */
    public final static int PAN = 0;
    /** Blue band index */
    public final static int BLUE = 1;
    /** Green band index */
    public final static int GREEN = 2;
    /** Red band index */
    public final static int RED = 3;
    /** Near Infrared band index */
    public final static int NIR = 4;
    /** Limit date for using the K-Factor tables */
    public final static String LIMIT_DATE = "2003-06-06";
    
    
    
    
    /** QuickBird conversor with default coefficient value */
    public QB_RadianceConverter(float[]absCalFactor) {
        super(absCalFactor);
    }
    
    public QB_RadianceConverter(String multiimdfile, String panimdfile){
        super(NBANDS);
        parseIMDFile(multiimdfile);
        parseIMDFile(panimdfile);
        System.out.println(java.util.Arrays.toString(super.coefs));
    }
    
    
    
    /**
     * K-Factor Table for products before 2003-06-06
     * @param nbits Number of bits of the product
     * @param TDI TDI Level
     * @param bandindex Index of the band to query (PAN, BLUE, GREEN, RED, NIR)
     * @return Revised K factor
     */
    @SuppressWarnings("fallthrough")
    public static float QB_KFactor(int nbits, int TDI, int bandindex) {
        switch(nbits) {
            // 16 bit product k-factor table
            case 16:
                switch(bandindex) {
                    case PAN:
                        switch(TDI){
                            case 10: return 8.381880e-2f;
                            case 13: return 6.447600e-2f;
                            case 18: return 4.656600e-2f;
                            case 24: return 3.494440e-2f;
                            case 32: return 2.618840e-2f;
                        }
                    case BLUE: return 1.604120e-2f;
                    case GREEN: return 1.438470e-2f;
                    case RED: return 1.267350e-2f;
                    case NIR: return 1.542420e-2f;
                }
                
                
                // 8 bit product k-factor table
            case 8:
                switch(bandindex) {
                    case PAN:
                        switch(TDI){
                            case 10: return 1.02681367f;
                            case 13: return 1.02848939f;
                            case 18: return 1.02794702f;
                            case 24: return 1.02989685f;
                            case 32: return 1.02739898f;
                        }
                    case BLUE: return 1.12097834f;
                    case GREEN: return 1.37652632f;
                    case RED: return 1.30924587f;
                    case NIR: return 0.98368622f;
                }
        }
        
        // Bad params returns 0.0
        return 0.0f;
    }
    
    

    

    // Regular expressions for the IMD file
    private static String DATE_PATTERN = "\\s*generationTime\\s*=\\s*(\\d\\d\\d\\d-\\d\\d-\\d\\d).*$";
    private static String BITS_PATTERN = "\\s*bitsPerPixel\\s*=\\s*(\\d+).*$";
    private static String BGROUP_PATTERN = "\\s*BEGIN_GROUP\\s*=\\s*(\\w+)\\s*$";
    private static String ACFACTOR_PATTERN = "\\s*absCalFactor\\s*=\\s*([^;]+).*$";
    private static String TDI_PATTERN = "\\s*TDILevel\\s*=\\s*([^;]+).*$";
    
    private void parseIMDFile(String file) {
        try {
            java.io.FileReader fr = new java.io.FileReader(file);
            java.io.BufferedReader br = new java.io.BufferedReader(fr);
            String line;
            
            boolean pan = false;
            boolean date = false;
            boolean bits = false;
            boolean oldp = false;
            int nbits = 0;
            int cband = 0; // Current band
            // Parse the file line by line
            while ((line = br.readLine()) != null ){
                // Requires to read the date and the number of bits
                if (!date || !bits) {
                    if (line.matches(DATE_PATTERN)) {
                        Pattern p = Pattern.compile(DATE_PATTERN);
                        Matcher m = p.matcher(line);
                        if(!m.find()) {System.err.println("Multi IDM file error #0! "+file); return;}
                        oldp = m.group(1).compareTo(LIMIT_DATE)<0;
                        date = true;
                    } else if (line.matches(BITS_PATTERN)) {
                        Pattern p = Pattern.compile(BITS_PATTERN);
                        Matcher m = p.matcher(line);
                        if(!m.find()) {System.err.println("Multi IDM file error #1! "+file); return;}
                        nbits = Integer.parseInt(m.group(1));
                        bits = true;
                    }
                    continue;
                }
                // Searchs for bands groups
                if (line.matches(BGROUP_PATTERN)) {
                    Pattern p = Pattern.compile(BGROUP_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Multi IDM file error #2! "+file); return;}
                    for(int i=0;i<BANDS.length;i++)
                        if(BANDS[i].equalsIgnoreCase(m.group(1))) cband = i;
                    if(cband==0) pan=true;
                    continue;
                }
                // Gets the absCalcFactor
                if (line.matches(ACFACTOR_PATTERN)) {
                    Pattern p = Pattern.compile(ACFACTOR_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Multi IDM file error #3! "+file); return;}
                    super.coefs[cband]*=Float.parseFloat(m.group(1));
                    continue;
                }
                // Gets the TDI Level
                if (line.matches(TDI_PATTERN)) {
                    Pattern p = Pattern.compile(TDI_PATTERN);
                    Matcher m = p.matcher(line);
                    if(!m.find()) {System.err.println("Multi IDM file error #4! "+file); return;}
                    // For old products uses the K-Factor table
                    if(oldp) {
                        if(pan)
                            super.coefs[cband]*=QB_KFactor(nbits,Integer.parseInt(m.group(1)),cband);
                        else
                            for(cband=1;cband<NBANDS;cband++)
                                super.coefs[cband]*=QB_KFactor(nbits,Integer.parseInt(m.group(1)),cband);
                        break;
                    }
                }
                
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    public static void main(String[] args) {
        QB_RadianceConverter src = new QB_RadianceConverter("/tmp/multi.imd","/tmp/pan.imd");
    }
    
}
