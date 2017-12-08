package es.upm.fi.gtd.ij_fusion;


import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;
        
import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.PCA;
//import fi.upm.util.EscaladorRango;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.Escalador;


import es.upm.fi.gtd.first.jama.Matrix;
import es.upm.fi.gtd.ij_fusion.fi.upm.util.Normalizador;


/**
 * Plugin para ImageJ que realiza fusion mediante el analisis de componentes
 * principales.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Fusion_por_PCA implements PlugInFilter {
    private boolean _DEBUG_ = false;
    // Contenedor de imagenes multiespectrales
    ImagePlus imp;
    ImageProcessor[] multibanda;
    ImageProcessor pancromatica;
    // Profundidad en bits
    int nbits;
    // El resultado ha de reescalarse
    boolean rescale = false;

    /**
     * Inicializacion del plugin
     */
    public int setup(String arg, ImagePlus imp) {
        this.imp = imp;
        if(imp.getStackSize() < 2) {
            IJ.showMessage("Se requiere por lo menos de 2 bandas!");
            return DONE;
        }
        return DOES_ALL + STACK_REQUIRED + NO_CHANGES;
    }

    /**
     * Implementacion del plugin
     */
    public void run(ImageProcessor ip) {
        // Muestra el cuadro de diálogo de configuración y solicitud de pan
        if (!DialogoConfiguracionPCA()) return;
                
        // Realizamos la fusión
        ImageProcessor[] fusionada = new ImageProcessor[3];
        fusionada = PCA(multibanda, pancromatica);
        // Visualizamos la imagen
        ImageStack stack = new ImageStack(fusionada[0].getWidth(),fusionada[0].getHeight());
        for (int z=0;z<fusionada.length;z++) {
            fusionada[z].setMinAndMax(0, java.lang.Math.pow(2, nbits)-1);
            stack.addSlice("Banda #"+(z+1), fusionada[z]);
        }
        ImagePlus resultado = new ImagePlus("Fusionada PCA", stack);
        
        resultado.show();
    }

    
    
    public boolean DialogoConfiguracionPCA() {
        // Abrimos la imagen pancromática
        //Multi_Lector imagen = new Multi_Lector();
        ImagePlus ip = null;
                //imagen.LeerArchivo("Seleccione el archivo de la pancromática...");
        if (ip==null) return false;
        
        pancromatica = ip.getStack().getProcessor(1).convertToFloat();

        // Interfaz de seleccion de ponderaciones
        GenericDialog gd = new GenericDialog("Configuración fusión PCA");
        gd.addCheckbox("Rescalar visualización", true);
        gd.addNumericField("Normalización:", ip.getBitDepth(), 0, 2, "bits");
        gd.addCheckbox("Interpolación lineal", true);
        gd.addCheckbox("Modo depuración", false);
        gd.showDialog();
        
        // Comprueba si ha de usar los valores por defecto...
        if (gd.wasCanceled())
            return false;
        
        // Comprueba si ha de realizar reescalado
        this.rescale = gd.getNextBoolean();
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

        
        

    
    
    public ImageProcessor[] PCA(ImageProcessor[] multi, ImageProcessor pan)
    {
        // Variables referidas a la imagen final
        int width = pan.getWidth();		// Ancho final es el de la pancromatica
        int height = pan.getHeight();           // Altura final es la de la pancromatica
        int npixels = width*height;     	// numero de pixeles
        int nbands = imp.getStackSize(); 	// numero de bandas


        // Crea la matriz en la que se almacenaran las imagenes (una por columna)
        IJ.showStatus("Leyendo y creando matrices para la PCA...");
        int max = npixels; // status bar
        Matrix X = new Matrix(npixels, nbands);
        float[][]bands = new float[nbands][npixels];
        for (int z=0;z<nbands;z++)
            bands[z] = (float[])multi[z].getPixels();
        // Lectura de las imagenes en la matriz de Jama
        for(int k=0; k<npixels; k++) {
            for (int z=0; z<nbands; z++) {
                X.set(k, z, bands[z][k]);
            }
            // Actualiza la barra de progreso
            IJ.showProgress(k, max);
        }

        
        // Analisis de Componentes Principales
        PCA pca = new PCA(X);
        X = null; // Liberamos algo mas de memoria
        IJ.showStatus("Realizando transformada...");
        Matrix res = pca.Transformada(nbands);

        // Mostramos el resultado de la transformada PCA
        if (_DEBUG_) {
            ImageStack pcastack = new ImageStack(width, height);
            
            for (int z=0;z<nbands;z++) {
                double[] cband = res.transpose().getArray()[z];
                pcastack.addSlice("Componente #"+(z+1), new FloatProcessor(width,height,cband));
            }
            ImagePlus pcatrans = new ImagePlus("Transformada PCA", pcastack);
            pcatrans.show();
        }

        // Calcular el cambio de escala
        double[] primeracomponente = new double[npixels];
        for (int i=0, k=0; i<height; i++)
            for (int j=0; j<width; j++,k++)
                primeracomponente[k] = res.get(k, 0);

 

        // Escalamos la pancromatica al rango de la primera componente de la PCA
        ImageStatistics isp = ImageStatistics.getStatistics(new FloatProcessor(width,height,primeracomponente), ImageStatistics.MIN_MAX, null);
        Normalizador r1 = new Normalizador(isp.min, isp.max);
        Normalizador r2 = new Normalizador(0, java.lang.Math.pow(2,nbits)-1);
        float[] panc = (float[])pan.getPixels();
        
        // Cambiar la primera componente por la pancromatica
        IJ.showStatus("Cambiando la primera componente por la pancromatica...");
        for (int i=0, k=0; i<height; i++)
            for (int j=0; j<width; j++,k++)
                res.set(k, 0, r1.Escalar(r2.Normalizar(panc[k])));
        // liberamos algo de memoria
        panc = null;
        
        // Realiza la transformacion inversa
        res = pca.Transformada_Inversa(res);


        // Crea la pila de imagenes resultantes
        double[][] transformadas = res.transpose().getArray();
        ImageProcessor[] fusionada = new ImageProcessor[nbands];
        

        for (int z=0;z<nbands;z++)
            fusionada[z] = new FloatProcessor(width, height, transformadas[z]);

        // Normalizador de las bandas
        if(rescale) {
            /* Otra forma ligeramente distinta de normalizar (valor mayor/menor de todas las bandas
            double maxv = Double.MIN_VALUE;
            double minv = Double.MAX_VALUE;
            for (int z=0;z<nbands;z++) {
                ImageStatistics is = ImageStatistics.getStatistics(fusionada[z], ImageStatistics.MIN_MAX, null);
                maxv = is.max > maxv ? is.max : maxv;
                minv = is.min < minv ? is.min : minv;
            }*/
            for (int z=0;z<nbands;z++) {
                ImageStatistics is = ImageStatistics.getStatistics(fusionada[z], ImageStatistics.MIN_MAX, null);
                float[] cband = (float[])fusionada[z].getPixels();
                Normalizador range1 = new Normalizador(is.min, is.max);
                Normalizador range2 = new Normalizador(0, java.lang.Math.pow(2,nbits)-1);
                for (int k=0;k<npixels;k++)
                    cband[k] = (float)range2.Escalar(range1.Normalizar(cband[k]));
                fusionada[z].setPixels(cband);
            }
        }
        
        return fusionada;
    }

}
