package es.upm.fi.gtd.ij_fusion;


import ij.plugin.PlugIn;
import ij.gui.*;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;



public class Filtro_Elipsoidal implements PlugIn  {
    private double a = 1.0;
    private double b = 1.0;
    private int nangles = 0;
    private static final int DEFAULT_FILTER_SIZE = 127;
    
    public void run(String string) {
        if (!dialogoParametros())
            return;
        
        double angulo = 0.00;
        double paso_angulo = 0.0;
        if (nangles>1)
            paso_angulo = Math.PI / ((double)this.nangles);
        
        ImageStack stack = new ImageStack(DEFAULT_FILTER_SIZE,DEFAULT_FILTER_SIZE);
        for(int i=0;i<this.nangles;i++) {
            byte[] pixels = generarFiltroElipsoidal(DEFAULT_FILTER_SIZE,this.a,this.b,angulo);
            ByteProcessor ip = new ByteProcessor(DEFAULT_FILTER_SIZE,DEFAULT_FILTER_SIZE);
            ip.setPixels(pixels);
            stack.addSlice("Ángulo("+angulo+")",ip);
            angulo+=paso_angulo;
        }
        ImagePlus imp = new ImagePlus("Filtro Elipsoidal a("+this.a+") b("+this.b+")",stack);
        imp.show();
    }
    
    
    public boolean dialogoParametros() {
        GenericDialog gd = new GenericDialog("ConfiguraciÃ³n de filtro elipsoidal");
        gd.addNumericField("a:",1.0,2);
        gd.addNumericField("b:",1.0,2);
        gd.addNumericField("Ángulos:",4,0);
        gd.showDialog();
        
        if(gd.wasCanceled()) return false;
        
        this.a = gd.getNextNumber();
        this.b = gd.getNextNumber();
        this.nangles = (int)gd.getNextNumber();        
        return true;
    }
    
    
    
    
    
    
    // size = npixels x npixels (es cuadrado)
    public static byte[] generarFiltroElipsoidal(int size, double a, double b, double theta) {
        double []u = new double[size];
        double []v = new double[size];
        double []H1 = new double[u.length];
        double []H2 = new double[v.length];
        double [][]H = new double[v.length][u.length];

        // Calculamos la frecuencia de muestreo
        double paso = 2.0/size;
        double init = -1.0;
        // Generamos los vectores de muestreo
        for(int i=0;i<u.length;i++) {
            u[i]= init;
            v[i]= init;
            init+=paso;
        }
        
        
        double alpha = (Math.pow(a,2)-Math.pow(b,2))*Math.sin(2*theta)/(Math.pow(a,2)*Math.pow(b,2));
        //IJ.showMessage("Alfa:" + alpha);
        double max=Double.MIN_VALUE;
        double min=Double.MAX_VALUE;
        // H1(u)
        for(int i=0;i<u.length;i++) {
            H1[i] = Math.exp((-Math.pow(u[i],2)) * (Math.pow(Math.cos(theta),2)/Math.pow(a,2) + Math.pow(Math.sin(theta),2)/Math.pow(b,2)));
            // H2(v)
            for(int j=0;j<v.length;j++) {
                H2[j] = Math.exp((-Math.pow(v[j],2)) * (Math.pow(Math.cos(theta),2)/Math.pow(b,2) + Math.pow(Math.sin(theta),2)/Math.pow(a,2)));

                //H[j][i] = H1[i]*H2[j] - alpha*H1[i]*H2[j];
                H[j][i] = H1[i]*H2[j] - alpha*u[i]*H1[i]*v[j]*H2[j];
                max = (H[j][i]>max) ? H[j][i] : max;
                min = (H[j][i]<min) ? H[j][i] : min;
            }
        }
        
        // Recalculamos en el rango 0..255
        byte []pixels = new byte[v.length*u.length];
        for(int y=0,k=0;y<v.length;y++)
            for(int x=0;x<u.length;x++,k++)
                pixels[k]=(byte)((H[y][x]-min)*(255.0/(max-min)));
        
        
        /* DEBUG: muestra el filtro como una imagen float
        double []fpix = new double[v.length*u.length];
        for(int y=0,k=0;y<v.length;y++)
            for(int x=0;x<u.length;x++,k++)
                fpix[k]=H[y][x];
        FloatProcessor fp = new FloatProcessor(u.length,v.length,fpix);
        ImagePlus fip = new ImagePlus("Filtro float", fp);
        fip.show();
        */
        
        
        return pixels;
    }

    
}
