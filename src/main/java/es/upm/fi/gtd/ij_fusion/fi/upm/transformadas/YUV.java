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
 * Tranformada YIQ tambien conocida como PAL
 * @author Francisco Javier Merino Guardiola
 */
public class YUV {
    // Matriz de conversion representada como vector
    // para mejorar la eficiencia
    private static final float[] YUV_MATRIX =
    {0.299f, 0.587f, 0.114f,
     -0.147f, -0.289f, 0.437f,
     0.615f, -0.515f, -0.100f};
    // Matriz de cambio inversa
    private static final float[] YUV_INVMATRIX =
    {1.0f, 0.0f, 1.140f,
     1.0f, -0.394f, -0.581f,
     1.0f, 2.028f, 0.0f};
    
    
    public static float[] RGBtoYUV(float r, float g, float b) {
        float[] yiq = new float[3];
        yiq[0] = YUV_MATRIX[0]*r + YUV_MATRIX[1]*g + YUV_MATRIX[2]*b;
        yiq[1] = YUV_MATRIX[3]*r + YUV_MATRIX[4]*g + YUV_MATRIX[5]*b;
        yiq[2] = YUV_MATRIX[6]*r + YUV_MATRIX[7]*g + YUV_MATRIX[8]*b;
        return yiq;
    }
    
    public static float[] RGBtoYUV(float[] rgb) {
        return RGBtoYUV(rgb[0],rgb[1],rgb[2]);
    }
    
    
    /** Converts all the pixels from r,g&b to yuv/pal */
    public static void RGBtoYUV(float[] r, float[] g, float[] b) {
        float[] pixel;
        for(int i=0;i<r.length;i++) {
            pixel = RGBtoYUV(r[i], g[i], b[i]);
            r[i] = pixel[0];
            g[i] = pixel[1];
            b[i] = pixel[2];
        }
    }
    

    public static float[] YUVtoRGB(float y, float i, float q) {
        float[] rgb = new float[3];
        rgb[0] = YUV_INVMATRIX[0]*y + YUV_INVMATRIX[1]*i + YUV_INVMATRIX[2]*q;
        rgb[1] = YUV_INVMATRIX[3]*y + YUV_INVMATRIX[4]*i + YUV_INVMATRIX[5]*q;
        rgb[2] = YUV_INVMATRIX[6]*y + YUV_INVMATRIX[7]*i + YUV_INVMATRIX[8]*q;
        return rgb;
    }
    
    
    public static float[] YUVtoRGB(float[] yuv) {
        return YUVtoRGB(yuv[0],yuv[1],yuv[2]);
    }

    /** Converts all the pixels from r,g&b to yuv/pal */
    public static void YUVtoRGB(float[] y, float[] u, float[] v) {
        float[] pixel;
        for(int j=0;j<y.length;j++) {
            pixel = YUVtoRGB(y[j], u[j], v[j]);
            y[j] = pixel[0];
            u[j] = pixel[1];
            v[j] = pixel[2];
        }
    }


}
