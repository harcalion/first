package es.upm.fi.gtd.ij_fusion;

/*
 * RGB_a_NTSC.java
 *
 * Created on 10 de diciembre de 2007, 16:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import ij.*;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.gui.*;

import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.YIQ;
import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.YUV;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.Normalizador;

/**
 * Conversor NTSC a RGB
 * @author Francisco Javier Merino Guardiola
 */
public class RGB_NTSC_PAL implements PlugInFilter {
    private final String []YIQ_COMP = {"Y (luminance)", "I (hue)", "Q (saturation)"};
    private final String []YUV_COMP = {"Y (luminance)", "U (chrominance r)", "V (chrominance b)"};    
    private final String []RGB_COMP = {"R (red)", "G (green)", "B (blue)"};
    
    // Contenedor de la imagen
    ImagePlus imp;
    // Tipo de operacion: RGB2YIQ YIQ2RGB
    String op;
    // El resultado ha de reescalarse
    boolean rescale = false;
    // Nombre de las bandas
    String[] bandnames;
    // Nombre del titulo de la nueva imagen
    String title;
    
    
    /* Inicialización del plugin */
    public int setup(String cmd, ImagePlus imp) {
        if(imp.getStackSize() < 3) {
            IJ.showMessage("Se requiere por lo menos de 3 bandas.");
            return DONE;
        }
        this.op = cmd;
        this.imp = imp;
        return DOES_ALL+STACK_REQUIRED;
    }
    
    
    /* Ejecucion del plugin */
    public void run(ImageProcessor ip) {
        // Muestra el cuadro de diálogo de configuración
        if (!ConfigDialog()) return;
        
        // Tan sólo tranforma las tres primeras bandas (RGB)
        ImageProcessor []bands = new ImageProcessor[3];
        int nbands = bands.length;
        int npixels = ip.getWidth()*ip.getHeight();
                
        for(int i=0;i<bands.length;i++)
            bands[i]=imp.getStack().getProcessor(i+1).duplicate().convertToFloat();
        
        if (op.equals("RGB2YIQ")) {
            YIQ.RGBtoYIQ((float[])bands[0].getPixels(),
                         (float[])bands[1].getPixels(),
                         (float[])bands[2].getPixels());
        }
        else if (op.equals("YIQ2RGB")) {
            YIQ.YIQtoRGB((float[])bands[0].getPixels(),
                         (float[])bands[1].getPixels(),
                         (float[])bands[2].getPixels());            
        }
        else if (op.equals("RGB2YUV")) {
            YUV.RGBtoYUV((float[])bands[0].getPixels(),
                         (float[])bands[1].getPixels(),
                         (float[])bands[2].getPixels());            
        }
        else if (op.equals("YUV2RGB")) {
            YUV.YUVtoRGB((float[])bands[0].getPixels(),
                         (float[])bands[1].getPixels(),
                         (float[])bands[2].getPixels());            
        }        
        
        // Normalizador de las bandas
        if (this.rescale) {
            for (int z=0;z<nbands;z++) {
                ImageStatistics is = ImageStatistics.getStatistics(bands[z], ImageStatistics.MIN_MAX, null);
                Normalizador range1 = new Normalizador(is.min, is.max);
                Normalizador range2 = new Normalizador(0, java.lang.Math.pow(2,imp.getBitDepth())-1);
                for (int k=0;k<npixels;k++)
                    ((float[])bands[z].getPixels())[k] = (float)range2.Escalar(range1.Normalizar(((float[])bands[z].getPixels())[k]));
            }
        }
        

        // Crea el ImagePlus para mostrar
        ImageStack stack = new ImageStack(imp.getWidth(),imp.getHeight());
        for(int i=0;i<bands.length;i++)
            stack.addSlice(bandnames[i],bands[i]);
        ImagePlus res = new ImagePlus(title,stack);
        res.show();
    }
    
    
    public boolean ConfigDialog() {
        // Interfaz de seleccion de ponderaciones
        GenericDialog gd = new GenericDialog("Configuración transformada");
        gd.addCheckbox("Rescalar visualización", false);
        gd.addMessage("Escalar los valores de visualización \n hará que el proceso sea irreversible");
        gd.showDialog();
        this.rescale = gd.getNextBoolean();
        if(op.equals("RGB2YIQ")) {
            this.bandnames = YIQ_COMP;
            this.title  = "YIQ - " + imp.getTitle();
        }
        else if(op.equals("RGB2YUV")) {
            this.bandnames = YUV_COMP;
            this.title  = "YUV - " + imp.getTitle();
        }
        else if(op.equals("YIQ2RGB") || op.equals("YUV2RGB")) {
            this.bandnames = RGB_COMP;
            this.title  = "RGB - " + imp.getTitle();
        }
        else return false;
        return true;
    }
    
    
    
    
}
