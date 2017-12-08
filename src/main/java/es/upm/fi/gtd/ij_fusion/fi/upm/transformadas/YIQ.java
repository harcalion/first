/*
 * NTSC.java
 *
 * Created on 10 de diciembre de 2007, 12:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.upm.fi.gtd.ij_fusion.fi.upm.transformadas;

/**
 * Tranformada YIQ tambien conocida como NTSC
 * @author Francisco Javier Merino Guardiola
 */
public class YIQ {
    // Matriz de conversion representada como vector
    // para mejorar la eficiencia
    private static final float[] YIQ_MATRIX =
    {0.299f, 0.587f, 0.114f,
     0.596f, -0.274f, -0.322f,
     0.211f, -0.523f, 0.312f};
    // Matriz de cambio inversa
    private static final float[] YIQ_INVMATRIX =
    {1.0f, 0.956f, 0.621f,
     1.0f, -0.272f, -0.647f,
     1.0f, -1.105f, 1.702f};
    
    
    public static float[] RGBtoYIQ(float r, float g, float b) {
        float[] yiq = new float[3];
        yiq[0] = YIQ_MATRIX[0]*r + YIQ_MATRIX[1]*g + YIQ_MATRIX[2]*b;
        yiq[1] = YIQ_MATRIX[3]*r + YIQ_MATRIX[4]*g + YIQ_MATRIX[5]*b;
        yiq[2] = YIQ_MATRIX[6]*r + YIQ_MATRIX[7]*g + YIQ_MATRIX[8]*b;
        return yiq;
    }
    
    public static float[] RGBtoYIQ(float[] rgb) {
        return RGBtoYIQ(rgb[0],rgb[1],rgb[2]);
    }
    
    
    /** Converts all the pixels from r,g&b to yiq/ntsc */
    public static void RGBtoYIQ(float[] r, float[] g, float[] b) {
        float[] pixel;
        for(int i=0;i<r.length;i++) {
            pixel = RGBtoYIQ(r[i], g[i], b[i]);
            r[i] = pixel[0];
            g[i] = pixel[1];
            b[i] = pixel[2];
        }
    }
    

    public static float[] YIQtoRGB(float y, float i, float q) {
        float[] rgb = new float[3];
        rgb[0] = YIQ_INVMATRIX[0]*y + YIQ_INVMATRIX[1]*i + YIQ_INVMATRIX[2]*q;
        rgb[1] = YIQ_INVMATRIX[3]*y + YIQ_INVMATRIX[4]*i + YIQ_INVMATRIX[5]*q;
        rgb[2] = YIQ_INVMATRIX[6]*y + YIQ_INVMATRIX[7]*i + YIQ_INVMATRIX[8]*q;
        return rgb;
    }
    
    
    public static float[] YIQtoRGB(float[] yiq) {
        return YIQtoRGB(yiq[0],yiq[1],yiq[2]);
    }

    /** Converts all the pixels from r,g&b to yiq/ntsc */
    public static void YIQtoRGB(float[] y, float[] i, float[] q) {
        float[] pixel;
        for(int j=0;j<y.length;j++) {
            pixel = YIQtoRGB(y[j], i[j], q[j]);
            y[j] = pixel[0];
            i[j] = pixel[1];
            q[j] = pixel[2];
        }
    }


}
