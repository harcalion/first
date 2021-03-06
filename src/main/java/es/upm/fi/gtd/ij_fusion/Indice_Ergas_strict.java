package es.upm.fi.gtd.ij_fusion;

import es.upm.fi.gtd.ij_fusion.fi.upm.util.DEFAULT_RadianceConverter;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.SPOT_RadianceConverter;
import ij.*;
import ij.ImagePlus;
import ij.process.*;
import ij.gui.GenericDialog;
import ij.io.OpenDialog;
import ij.plugin.filter.PlugInFilter;

import es.upm.fi.gtd.ij_fusion.fi.upm.util.Estadisticas;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.QB_RadianceConverter;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.RadianceConverter;



public class Indice_Ergas_strict implements PlugInFilter {
    private boolean _DEBUG_ = false;
    // Sensor types
    final private int UNKNOWN=0, LANDSAT=1, QUICKBIRD=2, SPOT=3, IKONOS=4;
    final private String[] SENSOR_TYPES = {"DEFECTO","LANDSAT","QUICKBIRD","SPOT","IKONOS"};
    // Tipo de ERGAS a calcular
    private String tipo_ergas;
    // Indica el ratio entre pixeles (h/l)
    float escala;
    // Indica si se utilizara interpolacion
    boolean interpolar;
    // Indica si se va a usar una imagen multibanda
    boolean esmultibanda;
    // Tipo de sensor
    int tiposensor;
    // Contenedores de las imagenes
    ImagePlus fusionada;
    ImagePlus pancromatica;
    ImagePlus multibanda;
    // Conversor de radiancias para el sensor
    RadianceConverter rc;
    
    
    /**
     * Inicializacion del plugin
     */
    public int setup(String cmd, ImagePlus imp) {
        this.tipo_ergas = cmd;
        this.fusionada = imp;
        return DOES_ALL+ STACK_REQUIRED + NO_CHANGES;
    }
    
    
    /**
     * Implementación del plugin
     */
    public void run(ImageProcessor ip) {
        // Muestra el diálogo de opciones
        if (!dialogoOpcionesERGAS()) return;
        
        double ergas;
        // Comprueba si ha de realizar el ERGAS espacial
        if (tipo_ergas.equals("ESPACIAL")) {
            // Convertimos los procesadores a floats
            float[] p = (float[]) pancromatica.getStack().getProcessor(1).convertToFloat().getPixels();
            rc.toRadianceNA(p,0);
            float[][] f = new float[fusionada.getStackSize()][];
            for (int z=0;z<fusionada.getStackSize();z++) {
                f[z] = (float[]) fusionada.getStack().getProcessor(z+1).convertToFloat().getPixels();
                rc.toRadianceNA(f[z],z+1);
            }
            IJ.showMessage("Indice ERGAS Espacial: " + ERGAS_Espacial(p, f, escala));
        }
        // Comprueba si ha de realizar el ERGAS espectral
        else if (tipo_ergas.equals("ESPECTRAL")) {
            float[][] m = new float[multibanda.getStackSize()][];
            float[][] f = new float[fusionada.getStackSize()][];
            for (int z=0;z<fusionada.getStackSize();z++) {
                f[z] = (float[]) fusionada.getStack().getProcessor(z+1).convertToFloat().getPixels();
                rc.toRadianceNA(f[z],z+1);
                multibanda.getStack().getProcessor(z+1).setInterpolate(interpolar);
                m[z] = (float[]) multibanda.getStack().getProcessor(z+1).resize(fusionada.getWidth(),fusionada.getHeight()).convertToFloat().getPixels();
                rc.toRadianceNA(m[z],z+1);
            }
            FloatProcessor[] mp = new FloatProcessor[multibanda.getStackSize()];
            FloatProcessor[] fp = new FloatProcessor[fusionada.getStackSize()];
            for(int z=0; z<mp.length; z++) {
                mp[z] = new FloatProcessor(multibanda.getWidth(), multibanda.getHeight());
                mp[z].setPixels(m[z]);
                fp[z] = new FloatProcessor(fusionada.getWidth(), fusionada.getHeight());
                fp[z].setPixels(f[z]);
            }
            ergas = ERGAS_Espectral(mp, fp, escala);
            IJ.showMessage("Indice ERGAS Espectral: " + ergas);
        }        
    }
    
    
    
    
    public boolean dialogoOpcionesERGAS() {
        GenericDialog gd = new GenericDialog("Opciones de índice ERGAS "+tipo_ergas);
        gd.addChoice("Tipo de sensor", SENSOR_TYPES, SENSOR_TYPES[0]);
        gd.addNumericField("Factor de escala", 0.25, 3);
        if (tipo_ergas.equals("ESPECTRAL")) {
            gd.addMessage("Seleccione la siguiente casilla en caso de que desee\n" +
                          "utilizar interpolacion lineal.");
            gd.addCheckbox("Interpolación lineal", true);
        
            gd.addMessage("Seleccione la siguiente casilla si no dispone de una\n" +
                          "imagen TIFF multibanda y rellene el campo de número\n" +
                          "de bandas a procesar (#bandas).");
            gd.addCheckbox("Leer un fichero por cada banda:", false);
            gd.addNumericField("\t",4,0,2,"bandas.");
        }
        gd.addCheckbox("Modo depuración", false);
        gd.showDialog();
        if (gd.wasCanceled()) return false;
        
        
        tiposensor = gd.getNextChoiceIndex();
        
        // Lee el valore del factor de escala del pixel
        this.escala = (float)gd.getNextNumber();
        
        // Lee si ha de interpolar, pero solo en caso de ser un ergas espectral
        if (tipo_ergas.equals("ESPECTRAL"))
            interpolar = gd.getNextBoolean();
        
        // Creamos el conversor a radiancias adecuado
        OpenDialog od;
        String s;
        switch(tiposensor) {
            case UNKNOWN: rc = new DEFAULT_RadianceConverter(10); break;
            case LANDSAT: rc = new DEFAULT_RadianceConverter(8); break;
            case QUICKBIRD:
                // Obtenemos el nombre del fichero IMD de la multi
                od = new OpenDialog("Seleccione fichero IMD MULTI",null);
                s = od.getFileName();
                if (s==null) return false;  
                String multiimd = od.getDirectory()+od.getFileName();
                // Obtenemos el nombre del fichero IMD de la pan
                od = new OpenDialog("Seleccione fichero IMD PAN",null);
                s = od.getFileName();
                if (s==null) return false;  
                String panimd = od.getDirectory()+od.getFileName();
                rc = new QB_RadianceConverter(multiimd,panimd);
                break;
            case SPOT:
                // Obtenemos el nombre del fichero DIM de la multi
                od = new OpenDialog("Seleccione fichero DIM MULTI",null);
                s = od.getFileName();
                if (s==null) return false;  
                String multidim = od.getDirectory()+od.getFileName();
                // Obtenemos el nombre del fichero DIM de la pan
                od = new OpenDialog("Seleccione fichero DIM PAN",null);
                s = od.getFileName();
                if (s==null) return false;  
                String pandim = od.getDirectory()+od.getFileName();
                rc = new SPOT_RadianceConverter(multidim,pandim); break;
            case IKONOS: rc = new DEFAULT_RadianceConverter(5); break;
            default: IJ.showMessage("ERROR","Tipo de sensor no valido!"); return false;
        }
        
        // Permite abrir las imagenes necesarias
        Multi_Lector imagen;
        
        // En caso de ser ERGAS Espectral abriremos la/s imagen/bandas necesaria/s
        if (tipo_ergas.equals("ESPECTRAL")) {
            esmultibanda = !gd.getNextBoolean();        
            // Comprueba si las bandas se encuentran en un TIFF multibanda...
            if (esmultibanda) {
                imagen = new Multi_Lector();
                multibanda = imagen.LeerArchivo("Seleccione la imagen multibanda...");
            }
            // ...o si tiene que solicitar una a una las imagenes y crear una pila.
            else {
                int nbandas = (int)gd.getNextNumber();
                ImageStack stack = null;
                for(int z=0;z<nbandas;z++) {
                    imagen = new Multi_Lector();
                    ImageProcessor banda = imagen.LeerArchivo("Seleccione la banda número #"+(z+1)+"...").getStack().getProcessor(1);
                    if (stack == null) stack = new ImageStack(banda.getWidth(),banda.getHeight());
                    stack.addSlice("Banda "+(z+1), banda);
                }
                multibanda = new ImagePlus("multibanda", stack);
            }
        }
        
        
        if (tipo_ergas.equals("ESPACIAL")) {
            // Abrimos la imagen pancromática
            imagen = new Multi_Lector();
            pancromatica = imagen.LeerArchivo("Seleccione el archivo de la pancromática...");
        }
        
        // Comprueba si se ha seleccionado el modo depuración
        this._DEBUG_ = gd.getNextBoolean();
        return true;
    }
    
    
    
    
    
    /**
     *
     * @param pan Valores de <b>radiancia</b> de la imagen pacrom√°tica
     * @param fus Valores de <b>radiancia</b> de cada banda de la imagen funsionada.
     * @param scale Valor de escala del pixel (debería ser menor o igual a 1)
     * @return Valor del ergas espacial
     */
    public double ERGAS_Espacial(float[] pan, float[][] fus, float scale) {
        // Numero de bandas y de pixeles
        int nbandas = fus.length;
        int npixels = fus[0].length;
        // Valores de la pancrom√°tica ecualizada
        float[][]panecu = new float[nbandas][npixels];
        // Diferencias entre la imagen pancromatica y la fusionada
        
        // Calculamos las estadísticas de las imagenes as√≠ como
        // de la pancromática ecualizada y la diferencia entre esta y
        // la imagen fusionada.
        Estadisticas est_pan = new Estadisticas(pan, Estadisticas.MEDIA);
        Estadisticas []est_fus = new Estadisticas[nbandas];
        Estadisticas []est_panecu = new Estadisticas[nbandas];
        Estadisticas []est_diferencia = new Estadisticas[nbandas];
        for(int z=0;z<nbandas;z++) {
            est_fus[z] = new Estadisticas(fus[z], Estadisticas.MEDIA);
            float delta = est_fus[z].getMedia() - est_pan.getMedia();
            // Recorremos para crear una de las bandas correspondiente
            // a la pancromática ecualizada
            for(int k=0;k<npixels;k++)
                panecu[z][k] = pan[k]+delta;
            // Y calculamos las estadisticas de la pancromática ecualizada
            est_panecu[z] = new Estadisticas(panecu[z], Estadisticas.MEDIA);
            // Calculamos la diferencia entre la pancromatica ecualizada y
            // la pancromatica, calculamos la desviacion y liberamos memoria
            for(int k=0;k<npixels;k++)
                panecu[z][k] = panecu[z][k] - fus[z][k];
            est_diferencia[z] = new Estadisticas(panecu[z], Estadisticas.DESVIACION_ESTANDAR);
            panecu[z] = null;
        }
        
        // Calculamos el ERGAS a partir de las estadisticas obtenidas
        double RAZON = 0.0;
        
        for(int z=0;z<nbandas;z++) {
            double RMSE = Math.pow((double)(est_panecu[z].getMedia() - est_fus[z].getMedia()),2) +
                    Math.pow((double)est_diferencia[z].getDesviacionTipica(),2);
            // FIXED?? panecu o pan?
            //RAZON += RMSE / Math.pow((double)est_diferencia[z].getDesviacionTipica(),2);
            RAZON += RMSE / Math.pow((double)est_panecu[z].getMedia(),2);
        }
        RAZON /= nbandas;
        
        return 100 * Math.pow(scale,2) * Math.pow(RAZON,0.5);
    }
    
    
    
    
    
    /**
     *
     * @param multi Valores de <b>radiancia</b> para cada banda de la imagen multibanda escalada.
     * @param fus Valores de <b>radiancia</b> de cada banda de la imagen fusionada.
     * @param scale Valor de escala del pixel (debería ser menor o igual a 1)
     * @return Valor del ergas espacial
     */
    public double ERGAS_Espectral(FloatProcessor[] multi, FloatProcessor[] fus, float scale) {
        // Numero de bandas y de pixeles
        int nbands = fus.length;
        int npixels = fus[0].getWidth()*fus[0].getHeight();
        
        // Reads the array pixels
        float[][] pmulti = new float[nbands][npixels];
        float[][] pfus = new float[nbands][npixels];
        double[] meanmulti = new double[nbands];
        for(int z=0; z<nbands; z++) {
            pmulti[z] = (float[]) multi[z].getPixels();
            pfus[z] = (float[]) fus[z].getPixels();
            ImageStatistics is = ImageStatistics.getStatistics(multi[z], ImageStatistics.MEAN, null);
            meanmulti[z] = is.mean;
        }
        
        if(_DEBUG_) {
            String means = "Medias:";
            for(int z=0; z<nbands; z++) {
                means += " "+meanmulti[z];
            }
            IJ.showMessage(means);
        }
        
        // Calculo
        double aux = 0;
        for (int z=0; z<nbands; z++) {
            // Calculo el RMSE
            double rmse = 0;
            for (int k=0; k<npixels; k++)
                rmse += java.lang.Math.pow(pmulti[z][k] - pfus[z][k], 2);
            if(_DEBUG_) IJ.showMessage("Sumatorio: "+rmse);
            rmse = java.lang.Math.sqrt(rmse / npixels);
            if(_DEBUG_) IJ.showMessage("RMSE: "+rmse);
            // --
            aux += java.lang.Math.pow(rmse / meanmulti[z], 2);
        }
        double ergas = 100 * scale * java.lang.Math.sqrt(aux / nbands);
        return ergas;
    }
    
    
    
    
}
