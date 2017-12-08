package es.upm.fi.gtd.ij_fusion;

import ij.*;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.gui.*;

/**
 * Plugin para ImageJ para la fusion de imagenes mediante Atrous
 * @ author Francisco Javier Merino Guardiola
 **/
public class Fusion_mediante_Atrous implements PlugInFilter {
    private boolean _DEBUG_ = false;
    ImagePlus imp;
    
    /**
     * Inicializacion del plugin
     */
    public int setup(String arg, ImagePlus imp) {
        this.imp = imp;
        return DOES_ALL + NO_CHANGES;
    }
    
    
    /**
     * Implementacion del plugin
     */
    public void run(ImageProcessor ip) {
        // Lectura de la pancromatica 
        Multi_Lector imagen = new Multi_Lector();
        ImageProcessor pancromatica = imagen.LeerArchivo("Seleccione el archivo de la pancromatica...").getStack().getProcessor(1).convertToFloat();

        // Lectura de los procesadores de la multibanda
        IJ.showStatus("Reescalando y convirtiendo multi...");
        ImageProcessor []multibanda = new ImageProcessor[imp.getStackSize()];
        for (int i=0;i<imp.getStackSize();i++) {
            multibanda[i] = imp.getStack().getProcessor(i+1).convertToFloat();
            multibanda[i].setInterpolate(true);
            multibanda[i] = multibanda[i].resize(pancromatica.getWidth(),pancromatica.getHeight());
        }
        
        // Interfaz de seleccion de ponderaciones
        GenericDialog gd = new GenericDialog("Seleccione ponderaciones por banda");
        int niveles_multi = 2;
        int niveles_pan = 2;
        float []ponderacion = new float[imp.getStackSize()];
        gd.addNumericField("Degradación MULTI:",2,0,2,"niveles");
        gd.addNumericField("Degradación PAN:",2,0,2,"niveles");
        for (int i=0;i<imp.getStackSize();i++)
            gd.addNumericField("Ponderacion banda"+i+":",1.0,4,6,"");
        gd.addCheckbox("Modo depuracion",false);
	gd.showDialog();
        // Comprueba si ha de usar los valores por defecto...
	if(gd.wasCanceled()) for (int i=0;i<imp.getStackSize();i++) ponderacion[i] = 1.0f;
        // ...o si los debe leer del cuadro de dialogo.
        else {
            niveles_multi = (int) gd.getNextNumber();
            niveles_pan = (int) gd.getNextNumber();
            for (int i=0;i<imp.getStackSize();i++) ponderacion[i] = (float) gd.getNextNumber();
            _DEBUG_ = gd.getNextBoolean();
        }
        
        
        Atrous(multibanda, pancromatica, niveles_multi, niveles_pan, ponderacion);
    }
    
    
    /** Matriz de convolucion para degradar */
    final public static float [][]WAVELET =
    {{ 1/256f,  4/256f,  6/256f,  4/256f,  1/256f},
     { 4/256f, 16/256f, 24/256f, 16/256f,  4/256f},
     { 6/256f, 24/256f, 36/256f, 24/256f,  6/256f},
     { 4/256f, 16/256f, 24/256f, 16/256f,  4/256f},
     { 1/256f,  4/256f,  6/256f,  4/256f,  1/256f}};
    
    
    /** 
     * Dado un array de procesadores (multibanda) y un procesador de pancromatica
     * realiza la fusion mediante Atrous
     */
    public void Atrous(ImageProcessor[] multi, ImageProcessor pan, int multiLev, int panLev, float[] alpha) {
        int nbands = multi.length;
        int width = pan.getWidth();
        int height = pan.getHeight();
        
        // Procesadores de la multibanda en la actual iteracion
        ImageProcessor []multi_actual = multi;
        // Procesadores de la multibanda degradada
        ImageProcessor []multi_degrad = null;
        // Procesador de la pancromatica en la actual iteracion
        ImageProcessor pan_actual = pan;
        // Procesador de la pancromatica de la anterior iteracion
        ImageProcessor pan_anterior = null;
        // Acumulador de coeficientes de la pancromatica
        float []pan_coef = new float[width*height];
        for (int k=0;k<pan_coef.length;k++) pan_coef[k] = 0;
        // Procesadores de la fusionada
        ImageProcessor []fusionada = null;
        
        // Procedemos a degradar la multi en 'i' niveles
        // (no guardo los coeficientes)
        IJ.showStatus("Degradando multi...");
        for (int i=0;i<multiLev;i++) {
            // Creo el kernel correspondiente al nivel
            float []kernel = degradarFiltro(WAVELET,i);
            for(int z=0;z<nbands;z++)
                multi_actual[z].convolve(kernel,(int)Math.sqrt(kernel.length),(int)Math.sqrt(kernel.length));
            
            if(_DEBUG_) {
                ImageStack stack = new ImageStack(width,height);
                for(int z=0;z<nbands;z++)
                    stack.addSlice("Banda "+z+" degradado nivel "+i,multi_actual[z]);
                ImagePlus imagen = new ImagePlus("Multi Degradada Nivel " + (i+1),stack);
                imagen.show();
            }
        }
        multi_degrad = multi_actual;
        
        
        // Procedemos a degradar la pancromática en 'j' niveles
        IJ.showStatus("Degradando pan y calculando coeficientes...");
        for (int j=0;j<panLev;j++) {
            // Creo el kernel correspondiente al nivel y convoluciono
            float []kernel = degradarFiltro(WAVELET,j);
            pan_anterior = pan_actual.duplicate();
            pan_actual.convolve(kernel,(int)Math.sqrt(kernel.length),(int)Math.sqrt(kernel.length));
            
            if(_DEBUG_) {
                ImagePlus imagen = new ImagePlus("Pan Degradada Nivel "+(j+1), pan_actual);
                imagen.show();
            }
            
            // Calculo los coeficientes
            float []pixels_anterior = (float [])pan_anterior.getPixels();
            float []pixels_actual = (float [])pan_actual.getPixels();
            for (int p=0;p<pixels_actual.length;p++) {
                pixels_anterior[p] = pixels_anterior[p] - pixels_actual[p];
            }
            // actualizo el acumulador de coeficientes
            for (int k=0;k<pan_coef.length;k++) {
                pan_coef[k] += pixels_anterior[k];
            }
            
            if(_DEBUG_) {
                FloatProcessor fp = new FloatProcessor(width,height);
                fp.setPixels(pixels_anterior);
                ImagePlus imagen = new ImagePlus("Coeficientes pan "+(j+1), fp);
                imagen.show();
            }
            
        } // DEGRADACION DE LA PAN
        
        
        // Calculamos la fusionada mediante: MULTIi + [PANc1 + ... + PANcj]

        IJ.showStatus("Generando imagen fusionada...");
        ImageStack stack = new ImageStack(width,height);
        for(int z=0;z<nbands;z++) {
            float []pixels_fusionada = new float[width*height];
            float []pixels_degradada = (float []) multi_degrad[z].getPixels();
            for (int k=0;k<pixels_fusionada.length;k++)
                pixels_fusionada[k] = pixels_degradada[k] + alpha[z] * pan_coef[k];
            FloatProcessor fp = new FloatProcessor(width,height);
            fp.setPixels(pixels_fusionada);
            stack.addSlice("Banda fusionada "+(z+1),fp);
        }
        
        
        // La mostramos en pantalla
        ImagePlus imagen = new ImagePlus("Fusionada mediante Atrous",stack);
        imagen.show();

    }
    
    
    
    /**
     * Añadimos ceros en las columnas impares y en las filas impares
     */
    public static float[] degradarFiltro(float[][]filtro, int iteracion){
        float []res = new float[(filtro.length+(filtro.length-1)*iteracion)*(filtro[0].length+(filtro[0].length-1)*iteracion)];

        /** Recorremos el filtro entero */
        for(int i=0, k=0;i<filtro.length;i++) {
            for(int j=0;j<filtro[0].length;j++,k++) {
                res[k] = filtro[i][j];
                /** Si no es la ultima columna debe añadir "iteracion" ceros */
                if(j < filtro[0].length-1) {
                    for (int u=0;u<iteracion;u++)
                        res[++k] = 0;
                }
            }
            /** Si no es la ultima fila debe añadir "iteracion" filas de ceros */
            if(i < filtro.length-1) {
                for (int u=0;u<iteracion;u++) {
                    for (int v=0;v<(filtro[0].length+(filtro[0].length-1)*iteracion);v++)
                        res[k++] = 0;
                }
            }
        }
        return res;
    }
    
    public static void main(String args[]) {
        float[] prueba = degradarFiltro(WAVELET, 0);
        System.out.println(java.util.Arrays.toString(prueba));
        prueba = degradarFiltro(WAVELET, 1);
        System.out.println(java.util.Arrays.toString(prueba));
        prueba = degradarFiltro(WAVELET, 2);
        System.out.println(java.util.Arrays.toString(prueba));
        prueba = degradarFiltro(WAVELET, 3);
        System.out.println(java.util.Arrays.toString(prueba));
    }
    
}
