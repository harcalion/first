package es.upm.fi.gtd.ij;


import es.upm.fi.gtd.ij_fusion.*;
import ij.*;
import ij.ImagePlus;

import ij.plugin.filter.PlugInFilter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.gui.*;


/**
 * Plugin para ImageJ para la fusion de imagenes mediante MDMR
 * @ author Francisco Javier Merino Guardiola
 **/
public class Fusion_mediante_MDMRMod implements PlugInFilter {

    private boolean _DEBUG_ = false;
    ImagePlus imp;
    // Parametros de la fusion
    private int niveles_multi = 2;
    private int niveles_pan = 2;
    public ImageProcessor pancromatica;
    public ImageProcessor[] multibanda;
    //Filtro_Elipsoidal fe;
    double[] a; // a[0] = pancromatica
    double[] b; // b[0] = pancromatica

    
    public Fusion_mediante_MDMRMod (ImageProcessor pan, ImageProcessor [] multi)
    {
        pancromatica = pan.convertToFloat();
         multibanda = new ImageProcessor[multi.length];
        for (int i = 0; i < multi.length; i++) {

                multibanda[i] = multi[i].convertToFloat();
                multibanda[i].setInterpolate(true);
                 multibanda[i] = multibanda[i].resize(pancromatica.getWidth(), pancromatica.getHeight());
            
        }
        
    }
    
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
        if (!DialogoConfiguracionMDMR()) {
            return;
        }
        MDMR(multibanda, pancromatica, niveles_multi, niveles_pan, a, b);
    }

    public boolean DialogoConfiguracionMDMR() {
        // Lectura de la pancromatica
        Multi_Lector imagen = new Multi_Lector();
        pancromatica = imagen.LeerArchivo("Seleccione el archivo de la pancrom谩tica...").getStack().getProcessor(1).convertToFloat();

        // Lectura de los procesadores de la multibanda
        IJ.showStatus("Reescalando y convirtiendo multi...");
        multibanda = new ImageProcessor[imp.getStackSize()];
        for (int i = 0; i < imp.getStackSize(); i++) {
            multibanda[i] = imp.getStack().getProcessor(i + 1).convertToFloat();
            multibanda[i].setInterpolate(true);
            multibanda[i] = multibanda[i].resize(pancromatica.getWidth(), pancromatica.getHeight());
        }
        // Interfaz de seleccion de ponderaciones
        GenericDialog gd = new GenericDialog("Configuraci贸n fusi贸n MDMR");
        gd.addNumericField("Degradaci贸n MULTI:", 2, 0, 2, "羘gulos");
        gd.addNumericField("Degradaci贸n PAN:", 2, 0, 2, "羘gulos");

        for (int i = 0; i < imp.getStackSize(); i++) {
            gd.addMessage("Filtro Banda #" + (i + 1));
            gd.addNumericField(" a:", 1, 2);
            gd.addNumericField(" b:", 1, 2);
        }
        gd.addCheckbox("Modo depuraci贸n", false);
        gd.showDialog();
        // Comprueba si ha de usar los valores por defecto...
        if (gd.wasCanceled()) {
            return false;
        }
        this.niveles_multi = (int) gd.getNextNumber();
        this.niveles_pan = (int) gd.getNextNumber();
        // Lee los parametros de los filtro de la multibanda
        a = new double[imp.getStackSize()];
        b = new double[imp.getStackSize()];
        for (int i = 0; i < imp.getStackSize(); i++) {
            this.a[i] = gd.getNextNumber();
            this.b[i] = gd.getNextNumber();
        }

        // Comprueba si se ha seleccionado el modo depuracion
        this._DEBUG_ = gd.getNextBoolean();

        //fe = new Filtro_Elipsoidal();
        //return fe.dialogoParametros();
        return true;
    }

    /**
     * Dado un array de procesadores (multibanda) y un procesador de pancromatica
     * realiza la fusion mediante Atrous
     */
    public ImageProcessor [] MDMR(ImageProcessor[] multi, ImageProcessor pan, int multiLev, int panLev, double[] a, double[] b) {
        int nbands = multi.length;
        int width = pan.getWidth();
        int height = pan.getHeight();

        // Procesadores de la multibanda en la actual iteracion
        ImageProcessor[] multi_actual = multi;
        // Procesadores de la multibanda degradada
        ImageProcessor[] multi_degrad = null;
        // Procesador de la pancromatica en la actual iteracion
        ImageProcessor pan_actual = pan;
        // Procesador de la pancromatica de la anterior iteracion
        ImageProcessor pan_anterior = null;
        // Acumulador de coeficientes de la pancromatica
        float[][] pan_coef = new float[nbands][width * height];
        for (int z = 0; z < nbands; z++) {
            for (int k = 0; k < pan_coef[z].length; k++) {
                pan_coef[z][k] = 0;
            }
        }
        // Procesadores de la fusionada
        ImageProcessor[] fusionada = null;

        // Procedemos a degradar la multi en 'i' niveles
        // (no guardo los coeficientes)
        IJ.showStatus("Degradando multi...");
        // Calculamos los angulos para el filtro
        double angulo = 0.00;
        double paso_angulo = 0.0;
        if (multiLev > 1) {
            paso_angulo = Math.PI / (double) multiLev;
        }

        for (int i = 0; i < multiLev; i++) {
            // Aplicamos el filtro elipsoidal
            for (int z = 0; z < nbands; z++) {
                IJ.showStatus("Aplicando filtro elipsoidal(" + (i + 1) + ") a Banda " + (z + 1));
                byte[] pixels_filtro = Filtro_Elipsoidal.generarFiltroElipsoidal(128, a[z], b[z], angulo);
                FiltroFFT filtro = new FiltroFFT(multi_actual[z], 32);
                filtro.aplicarFiltro(pixels_filtro, 128, 128);
            }
            angulo += paso_angulo;

            if (_DEBUG_) {
                ImageStack stack = new ImageStack(width, height);
                for (int z = 0; z < nbands; z++) {
                    stack.addSlice("Banda " + z + " filtro nivel " + i, multi_actual[z]);
                }
                ImagePlus imagen = new ImagePlus("Multi Filtrada Nivel " + (i + 1), stack);
                imagen.show();
            }
        }
        multi_degrad = multi_actual;

        // Para cada una de las bandas calcularemos los respectivos coeficientes de la pan
        for (int z = 0; z < nbands; z++) {
            // Procedemos a degradar la pancrom谩tica en 'j' niveles
            pan_actual = pan.duplicate();
            IJ.showStatus("Degradando pan y calculando coeficientes banda #"+(z+1));
            // Calculamos los angulos para el filtro
            angulo = 0.00;
            paso_angulo = 0.0;
            if (panLev > 1) {
                paso_angulo = Math.PI / (double) panLev;
            }
            for (int j = 0; j < panLev; j++) {
                // Aplicamos el filtro elipsoidal
                IJ.showStatus("Aplicando filtro elipsoidal(" + (j + 1) + ") a pancrom谩tica");
                pan_anterior = pan_actual.duplicate();
                byte[] pixels_filtro = Filtro_Elipsoidal.generarFiltroElipsoidal(128, a[z], b[z], angulo);
                FiltroFFT filtro = new FiltroFFT(pan_actual, 32);
                filtro.aplicarFiltro(pixels_filtro, 128, 128);
                angulo += paso_angulo;

                if (_DEBUG_) {
                    ImagePlus imagen = new ImagePlus("Pan filtrada Nivel " + (j + 1), pan_actual);
                    imagen.show();
                }

                // Calculo los coeficientes
                float[] pixels_anterior = (float[]) pan_anterior.getPixels();
                float[] pixels_actual = (float[]) pan_actual.getPixels();
                for (int p = 0; p < pixels_actual.length; p++) {
                    pixels_anterior[p] = pixels_anterior[p] - pixels_actual[p];
                }
                // actualizo el acumulador de coeficientes
                for (int k = 0; k < pan_coef[z].length; k++) {
                    pan_coef[z][k] += pixels_anterior[k];
                }

                if (_DEBUG_) {
                    FloatProcessor fp = new FloatProcessor(width, height);
                    fp.setPixels(pixels_anterior);
                    ImagePlus imagen = new ImagePlus("Coeficientes pan " + (j + 1), fp);
                    imagen.show();
                }

            } // DEGRADACION DE LA PAN
        }

        // Calculamos la fusionada mediante: MULTIi + [PANc1 + ... + PANcj]
        IJ.showStatus("Generando imagen fusionada...");
        ImageStack stack = new ImageStack(width, height);
        
        for (int z = 0; z < nbands; z++) {
            float[] pixels_fusionada = new float[width * height];
            float[] pixels_degradada = (float[]) multi_degrad[z].getPixels();
            for (int k = 0; k < pixels_fusionada.length; k++) {
                pixels_fusionada[k] = pixels_degradada[k] + pan_coef[z][k];
            }
            FloatProcessor fp = new FloatProcessor(width, height);
            fp.setPixels(pixels_fusionada);
            
            stack.addSlice("Banda fusionada " + (z + 1), fp);
        }
          ImageProcessor [] resultado = new ImageProcessor[nbands];
		for (int i = 0; i < nbands; i++)
                {
                    resultado[i] = stack.getProcessor(i+1);
                }
		// Creamos el contenedor de bandas y lo mostramos
          ImagePlus imagen = new ImagePlus("Fusionada mediante MDMR", stack);
          
		return resultado;
        // La mostramos en pantalla
        
        
        
    }
}
