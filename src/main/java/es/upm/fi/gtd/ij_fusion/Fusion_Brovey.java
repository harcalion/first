package es.upm.fi.gtd.ij_fusion;


import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.Normalizador;

/**
 * Plugin para ImageJ que realiza fusión mediante transformación FIHS.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Fusion_Brovey implements PlugInFilter {
    
    private boolean _DEBUG_ = false;
    ImagePlus imp;
    ImageProcessor[] multibanda;
    ImageProcessor pancromatica;
    int nbits;
    
    
    /**
     * Inicializacion del plugin
     */
    public int setup(String arg, ImagePlus imp) {
        this.imp = imp;
        if(imp.getStackSize() < 3) {
            IJ.showMessage("Se requiere por lo menos de 3 bandas");
            return DONE;
        } else if (imp.getStackSize() != 3) {
            IJ.showMessage("Advertencia: este alhoritmo fue diseñado para usar 3 bandas!");
        }
        return DOES_ALL + STACK_REQUIRED + NO_CHANGES;
    }

    /**
     * Implementacion del plugin
     */
    public void run(ImageProcessor ip) {
        // Muestra el cuadro de diálogo de configuración y solicitud de pan
        if (!DialogoConfiguracionBrovey()) return;
        // Realizamos la fusión
        ImageProcessor[] fusionada = new ImageProcessor[3];
        fusionada = Brovey(multibanda, pancromatica, this.nbits);
        // Visualizamos la imagen
        ImageStack stack = new ImageStack(fusionada[0].getWidth(),fusionada[0].getHeight());
        for(int z=0;z<multibanda.length;z++)
            stack.addSlice("Banda #"+(z+1), fusionada[z]);
        ImagePlus resultado = new ImagePlus("Fusionada Brovey", stack);
        resultado.show();
    }

    
    
    
    public boolean DialogoConfiguracionBrovey() {
        // Abrimos la imagen pancromática
        Multi_Lector imagen = new Multi_Lector();
        ImagePlus ip = imagen.LeerArchivo("Seleccione el archivo de la pancromática...");
        if (ip==null) return false;
        
        pancromatica = ip.getStack().getProcessor(1).convertToFloat();

        // Interfaz de seleccion de ponderaciones
        GenericDialog gd = new GenericDialog("Configuración fusión Brovey");
        gd.addNumericField("Normalización:", ip.getBitDepth(), 0, 2, "bits");
        gd.addCheckbox("Interpolación lineal", true);
        gd.addCheckbox("Modo depuración", false);
        gd.showDialog();
        
        // Comprueba si ha de usar los valores por defecto...
        if (gd.wasCanceled())
            return false;
        
        // Lee la profundidad en bits de la imagen
        this.nbits = (int)gd.getNextNumber();
 
        // Comprueba si se desea interpolación
        boolean interpolar = gd.getNextBoolean();

        // Redimensionamos la multibanda
        multibanda = new ImageProcessor[imp.getStackSize()];
        for (int z=0; z<imp.getStackSize(); z++) {
            multibanda[z] = imp.getStack().getProcessor(z+1).convertToFloat();
            multibanda[z].setInterpolate(interpolar);
            multibanda[z] = multibanda[z].resize(pancromatica.getWidth(), pancromatica.getHeight());
        }

        // Comprueba si se ha seleccionado el modo depuración
        this._DEBUG_ = gd.getNextBoolean();
        
        return true;
    }
    
    
    

    /**
     * Ejecuta el plugin de fusion por transformacion IHS
     * @param panip ImageProcessor con la imagen pancromatica
     */
    public ImageProcessor[] Brovey(ImageProcessor[] multi, ImageProcessor pan, int nbits)
    {
        // Ancho de la fusionada
        int width = pan.getWidth();
        // Altura de la fusionada
        int height = pan.getHeight();
        // Número de pixeles a procesar
        int npixels = width*height;
        // Número de bandas
        int nbands = multi.length;

        // Leemos los array de pixeles para mayor eficiencia
        float[][] ms = new float[nbands][npixels];
        for (int z=0;z<nbands;z++)
            ms[z] = (float[]) multi[z].getPixels();
        float[] p = new float[npixels];
        p = (float[]) pan.getPixels();

        
        // Creamos los array de pixeles de la fusionada
        float[][] fus = new float[nbands][npixels];

        Normalizador rango = new Normalizador(0, java.lang.Math.pow(2, nbits)-1);
        
        // Transformada a HSI de cada pixel
        IJ.showStatus("Realizando fusión Brovey...");
        int max = npixels; // status bar
        
        for (int k=0; k<npixels; k++)
        {
            float den = 0;
            for(int z=0;z<nbands;z++)
                den += ms[z][k];
                // Si quisieramos trabajar con valores normalizados
                // den += rango.Normalizar(ms[z][k]);
            
            for(int z=0;z<nbands;z++)
                fus[z][k] =(ms[z][k] / den)*p[k];
                // Si quisieramos trabajar con valores normalizados
                // fus[z][k] =(float)rango.Escalar((rango.Normalizar(ms[z][k]) / den)* rango.Normalizar(p[k]));
            
            // Actualiza la barra de progreso
            IJ.showProgress(k, max);
        }

        // Montamos el image processor (32 bits)
        ImageProcessor[] fusionada = new ImageProcessor[nbands];
        for(int z=0;z<nbands;z++) {
            FloatProcessor fp = new FloatProcessor(width,height);
            fp.setPixels(fus[z]);
            fusionada[z] = fp;
        }
        return fusionada;        
    }
}

