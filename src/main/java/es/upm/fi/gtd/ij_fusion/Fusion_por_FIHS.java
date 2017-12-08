package es.upm.fi.gtd.ij_fusion;






import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;

import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.FIHS;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.Normalizador;


/**
 * Plugin para ImageJ que realiza fusión mediante transformación FIHS.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Fusion_por_FIHS implements PlugInFilter {
    
    private boolean _DEBUG_ = false;
    ImagePlus imp;
    ImageProcessor[] multibanda;
    ImageProcessor pancromatica;

    /**
     * Inicializacion del plugin
     */
    public int setup(String arg, ImagePlus imp) {
        this.imp = imp;
        if(imp.getStackSize() < 3) {
            IJ.showMessage("Se requiere por lo menos de 3 bandas");
            return DONE;
        }
        return DOES_ALL + STACK_REQUIRED + NO_CHANGES;
    }

    /**
     * Implementacion del plugin
     */
    public void run(ImageProcessor ip) {
        // Muestra el cuadro de diálogo de configuración y solicitud de pan
        if (!DialogoConfiguracionFIHS()) return;
        // Realizamos la fusión
        ImageProcessor[] fusionada = new ImageProcessor[3];
        fusionada = FIHS(multibanda, pancromatica);
        // Visualizamos la imagen
        ImageStack stack = new ImageStack(fusionada[0].getWidth(),fusionada[0].getHeight());
        stack.addSlice("Red", fusionada[0]);
        stack.addSlice("Green", fusionada[1]);
        stack.addSlice("Blue", fusionada[2]);
        ImagePlus resultado = new ImagePlus("Fusionada FIHS", stack);
        resultado.show();
    }

    
    
    
    public boolean DialogoConfiguracionFIHS() {
        // Abrimos la imagen pancromática
        Multi_Lector imagen = new Multi_Lector();
        ImagePlus ip = imagen.LeerArchivo("Seleccione el archivo de la pancromática...");
        if (ip==null) return false;
        
        pancromatica = ip.getStack().getProcessor(1).convertToFloat();

        // Interfaz de seleccion de ponderaciones
        GenericDialog gd = new GenericDialog("Configuración fusión FIHS");
        gd.addCheckbox("Interpolación lineal", true);
        gd.addCheckbox("Modo depuración", false);
        gd.showDialog();
        
        // Comprueba si ha de usar los valores por defecto...
        if (gd.wasCanceled())
            return false;
 
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
    public ImageProcessor[] FIHS(ImageProcessor[] multi, ImageProcessor pan)
    {
        // Ancho de la fusionada
        int width = pan.getWidth();
        // Altura de la fusionada
        int height = pan.getHeight();
        // Número de pixeles a procesar
        int npixels = width*height;

        // Leemos los array de pixeles para mayor eficiencia
        float[] red = (float[])multi[0].getPixels();
        float[] green = (float[])multi[1].getPixels();
        float[] blue = (float[])multi[2].getPixels();
        // La pancromatica será la componente intensidad
        float[] intensity = (float[])pan.getPixels();

        double[] punto;

        // Transformada a HSI de cada pixel
        IJ.showStatus("Realizando fusión IHS...");
        int max = npixels; // status bar
        
        for (int k=0; k<npixels; k++)
        {
            punto = FIHS.FusionFIHS(red[k], green[k], blue[k], intensity[k]);
                        
            red[k] = (float)punto[0];
            green[k] = (float)punto[1];
            blue[k] = (float)punto[2];
            // Actualiza la barra de progreso
            IJ.showProgress(k, max);
        }

        // Montamos el image processor (32 bits)
        ImageProcessor[] fusionada = new ImageProcessor[3];
        FloatProcessor fusred = new FloatProcessor(width,height);
        fusred.setPixels(red);
        fusionada[0] = fusred;
        FloatProcessor fusgreen = new FloatProcessor(width,height);
        fusgreen.setPixels(green);
        fusionada[1] = fusgreen;
        FloatProcessor fusblue = new FloatProcessor(width,height);
        fusblue.setPixels(blue);
        fusionada[2] = fusgreen;
        return fusionada;        
    }
}

