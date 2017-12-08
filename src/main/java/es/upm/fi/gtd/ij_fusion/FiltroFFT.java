package es.upm.fi.gtd.ij_fusion;


/**
 * Modified version of the FFTCustomFilter (ImageJ 1.39)
 * @author ImageJ 1.39
 * @author Francisco Javier Merino Guardiola
 */

import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.measure.*;


/** This class implements the Process/FFT/Custom Filter command. */
public class FiltroFFT implements Measurements {
    
    private ImageProcessor ip;
    int bits;
    private ImageProcessor filter;
    private boolean padded;
    private int originalWidth;
    private int originalHeight;
    
        
    public FiltroFFT(ImageProcessor ip, int nbits) {
        this.bits = nbits;
        this.ip = ip;
    }
    
    
    public void aplicarFiltro(byte[] pixels, int width, int height) {
        ByteProcessor f = new ByteProcessor(width, height);
        f.setPixels(pixels);
        aplicarFiltro(f);
    }
    
    public void aplicarFiltro(ByteProcessor filter) {
        FHT fht = newFHT(ip);
        this.filter = padFilter(filter,fht.getWidth());
        (fht).transform();
        customFilter(fht);
        doInverseTransform(fht, ip);
        ip.resetMinAndMax();
    }
    
    private void doInverseTransform(FHT fht, ImageProcessor ip) {
        fht.inverseTransform();
        if (fht.quadrantSwapNeeded)
            fht.swapQuadrants();
        fht.resetMinAndMax();
        ImageProcessor ip2 = fht;
        if (fht.originalWidth>0) {
            fht.setRoi(0, 0, fht.originalWidth, fht.originalHeight);
            ip2 = fht.crop();
        }
        int bitDepth = fht.originalBitDepth>0?fht.originalBitDepth:this.bits;
        switch (bitDepth) {
            case 8: ip2 = ip2.convertToByte(true); break;
            case 16: ip2 = ip2.convertToShort(true); break;
            case 24:
                fht.rgb.setBrightness((FloatProcessor)ip2);
                ip2 = fht.rgb;
                fht.rgb = null;
                break;
            case 32: break;
        }
        ip.insert(ip2, 0, 0);
    }
    
    private FHT newFHT(ImageProcessor ip) {
        FHT fht;
        if (ip instanceof ColorProcessor) {
            ImageProcessor ip2 = ((ColorProcessor)ip).getBrightness();
            fht = new FHT(pad(ip2));
            fht.rgb = (ColorProcessor)ip.duplicate(); // save so we can later update the brightness
        } else
            fht = new FHT(pad(ip));
        if (padded) {
            fht.originalWidth = originalWidth;
            fht.originalHeight = originalHeight;
        }
        fht.originalBitDepth = this.bits;
        return fht;
    }
    
    private ImageProcessor pad(ImageProcessor ip) {
        originalWidth = ip.getWidth();
        originalHeight = ip.getHeight();
        int maxN = Math.max(originalWidth, originalHeight);
        int i = 2;
        while(i<maxN) i *= 2;
        if (i==maxN && originalWidth==originalHeight) {
            padded = false;
            return ip;
        }
        maxN = i;
        ImageStatistics stats = ImageStatistics.getStatistics(ip, MEAN, null);
        ImageProcessor ip2 = ip.createProcessor(maxN, maxN);
        ip2.setValue(stats.mean);
        ip2.fill();
        ip2.insert(ip, 0, 0);
        padded = true;
        Undo.reset();
        return ip2;
    }
    
    
    
    void customFilter(FHT fht) {
        int size = fht.getWidth();
        fht.swapQuadrants(filter);
        float[] fhtPixels = (float[])fht.getPixels();
        byte[] filterPixels = (byte[])filter.getPixels();
        for (int i=0; i<fhtPixels.length; i++)
            fhtPixels[i] = (float)(fhtPixels[i]*(filterPixels[i]&255)/255.0);
        fht.swapQuadrants(filter);
    }
    
    
    ImageProcessor padFilter(ImageProcessor ip, int maxN) {
        int width = ip.getWidth();
        int height = ip.getHeight();
        if (width==maxN && height==maxN)
            return ip;
        ImageStatistics stats = ImageStatistics.getStatistics(ip, MEAN, null);
        ImageProcessor ip2 = ip.createProcessor(maxN, maxN);
        ip2.setValue(stats.mean);
        ip2.fill();
        ip2.insert(ip, (maxN-width)/2, (maxN-height)/2);
        return ip2;
    }
    
    
}

