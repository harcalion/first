package es.upm.fi.gtd.ij_fusion.fi.upm.util;

public class Convolucion {
    int ancho, alto;
    int offset;
    // Imagen original
    double[][] im;
    double[][] kernel;
    
    final static public int CONVOLUCION_CEROS = 0;
    
    
    public Convolucion(double[][] img, int tipo) {
        this.alto = img.length;
        this.ancho = img[0].length;
        this.im = img;
        this.offset = 1;
        
        switch(tipo) {
            case 0:
                im = padding_convolucion(0);
                break;
            default: 
                im = padding_convolucion(0);
                break;
        }
    }
    
    
    private double[][] padding_convolucion(int valor) {
        int offset = 1;
        double[][]img = new double[alto+offset*2][ancho+offset*2];
        
        
        for(int i=0;i<offset;i++)
            for(int j=0;j<offset;j++) {
            img[i][j] = 0.0;
            img[alto+i][ancho+j] = 0.0;
            }
        
        for(int i=0;i<alto;i++)
            for(int j=0;j<ancho;j++)
                img[i+offset][j+offset] = im[i][j];
        
        return img;
    }
    
    
    public double[][] convolucionar(double[][] k) {
        // Imagen convolucionada
        double[][] conv = new double[alto][ancho];

        
        for (int i=offset;i<(alto+offset);i++) {
            for(int j=offset;j<(ancho+offset);j++) {
//                System.out.println("i="+i+" j="+j);
//                System.out.println("\t"+im[i-1][j-1]+"*"+k[0][0]+"\t"+im[i-1][j]+"*"+k[0][1]+"\t"+im[i-1][j+1]+"*"+k[0][2]);
//                System.out.println("\t"+im[i][j-1]+"*"+k[1][0]+"\t"+im[i][j]+"*"+k[1][1]+"\t"+im[i][j+1]+"*"+k[1][2]);
//                System.out.println("\t"+im[i+1][j-1]+"*"+k[2][0]+"\t"+im[i+1][j]+"*"+k[2][1]+"\t"+im[i+1][j+1]+"*"+k[2][2]);
                // convoluciona
                conv[i-offset][j-offset] = (im[i-1][j-1]*k[0][0] + im[i-1][j]*k[0][1] + im[i-1][j+1]*k[0][2]) + (im[i][j-1]*k[1][0] + im[i][j]*k[1][1] + im[i][j+1]*k[1][2]) + (im[i+1][j-1]*k[2][0] + im[i+1][j]*k[2][1] + im[i+1][j+1]*k[2][2]);
            }
        }
        return conv;
    }
    
    
    
    public static void main(String args[]) {
    	double[][]im = {{1,2,3},{6,5,4},{8,8,8}};
        //double[][]im = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20},{21,22,23,24,25}};
        double[][]ker = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
        
        for (int i=0;i<im.length;i++)
            System.out.println(java.util.Arrays.toString(im[i]));
        
        System.out.println("[Convolucionando]");
        Convolucion c = new Convolucion(im,CONVOLUCION_CEROS);
        
        im = c.convolucionar(ker);
        for (int i=0;i<im.length;i++)
            System.out.println(java.util.Arrays.toString(im[i]));
    }
    
}
