package es.upm.fi.gtd.ij_fusion;


import ij.*;
import ij.io.*;
import ij.plugin.*;
import ij.gui.*;

import java.awt.image.RenderedImage;
import java.awt.image.DataBuffer;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ParameterBlockJAI;




/**
 * Plugin para la lectura de archivos TIFF, más concretamente para los archivos
 * TIFF multibanda.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Multi_Lector implements PlugIn {
    private int nbands;		// Numero de bandas leidas
    private ImagePlus img[];		// Lectura de bandas
    private ImagePlus readedimg; 	// Imagen leida
    
    public Multi_Lector() {
        img = null;
        nbands = 0;
    }
    
    
    /**
     * Ejecucion del plugin ImageJ.
     */
    public void run(String arg) {
        
        readedimg = VisualizarArchivo("Seleccionar archivo de imagen...");
        if (readedimg != null)
            readedimg.show();
    }
    

    

    
    
    /**
     * Implementacion de la lectura del archivo
     * @param mensaje
     * @return
     */
    public ImagePlus LeerArchivo(String msg) {
        OpenDialog od = new OpenDialog(msg, null);
        String directory = od.getDirectory();
        String fileName = od.getFileName();
        if (fileName==null) return null;
        
        // Intentamos cargar el archivo
        try {
            read(directory, fileName);
        }
        
        catch (Exception e) {
            e.printStackTrace();
            IJ.showMessage("EXCEPCIÓN: "+e.getMessage());
        }
        
        // Si no se ha podido leer no hace nada
        if (img==null) return null;

        // Creamos la pila e insertamos en ella las bandas
        ImageStack pila = img[0].createEmptyStack();
        for (int z=0; z<img.length; z++)
            pila.addSlice("Banda "+(z+1), img[z].getStack().getProcessor(1));
        
        // Creamos el contenedor de bandas a partir de la pila y lo mostramos
        ImagePlus imagen=new ImagePlus(fileName,pila);
        return imagen;
    }
    
    
    
    
    
    
    /**
     * Implementacion de la lectura del archivo
     * @param mensaje
     * @return
     */
    public ImagePlus VisualizarArchivo(String msg) {
        OpenDialog od = new OpenDialog(msg, null);
        String directory = od.getDirectory();
        String fileName = od.getFileName();
        if (fileName==null) return null;
        
        // Intentamos cargar el archivo
        try {
            read(directory, fileName);
        }
        
        catch (Exception e) {
            e.printStackTrace();
            IJ.showMessage("EXCEPCION: "+e.getMessage());
        }
        
        // Si no se ha podido leer no hace nada
        if (img==null) return null;
        
        
        // Solicita si se requiere cambiar la profundidad en pixeles
        int bits;
        GenericDialog gd = new GenericDialog("Numero de bits:");
        gd.addNumericField("Numero bits por pixel:",img[0].getBitDepth(),0,3,"bits");
        gd.showDialog();
        if(gd.wasCanceled()) bits = img[0].getBitDepth();
        else bits = (int)gd.getNextNumber();
        
        
        // Creamos la pila e insertamos en ella las bandas
        ImageStack pila = img[0].createEmptyStack();
        for (int z=0; z<img.length; z++) {
            // Establece el rango de vision (ejemplo, imagenes de 11 bits)
            img[z].getProcessor().setMinAndMax(0, Math.pow(2, bits));
            pila.addSlice("Banda "+(z+1), img[z].getStack().getProcessor(1));
        }
        
        // Creamos el contenedor de bandas a partir de la pila y lo mostramos
        ImagePlus imagen=new ImagePlus(fileName,pila);
        return imagen;
    }
    
    
    
    
    /**
     * Lectura de los pixeles haciendo uso de libreria <b>JAI</b>.
     * @param directory Directorio en el que se encuentra el fichero
     * @param fileName Nombre del fichero
     */
    protected void read(String directory, String fileName) {
        int i = 0;
        // Calculo del numero de bandas
        RenderedImage imagen = JAI.create("imageread", directory + fileName);
        
        nbands = imagen.getSampleModel().getNumBands();
        IJ.showStatus("El numero de bandas del fichero es: " + nbands);
        img = new ImagePlus[nbands];
        
        
        // Proceso cada una de las bandas del fichero
        for (i=0;i<nbands;i++) {
            // Lectura de cada una de las bandas
            ParameterBlockJAI selectPb = new ParameterBlockJAI("BandSelect");
            selectPb.addSource(imagen);
            selectPb.setParameter("bandIndices", new int[]{ i } );
            RenderedImage banda = JAI.create( "BandSelect", selectPb );
            PlanarImage pimage = PlanarImage.wrapRenderedImage(banda);
            
            // Obtiene informacion de la banda
            int width = pimage.getWidth();
            int height = pimage.getHeight();
            int bits = pimage.getColorModel().getPixelSize();
            
            
            // Crea e inicializa el 'ImagePlus'
            int [] readpixels = new int[height*width];
            pimage.getData().getPixels(0,0,width,height,readpixels);
            img[i] = NewImage.createImage("banda"+(i+1)+"-"+fileName, width,
                    height, 1, bits, NewImage.FILL_BLACK);
            int transfer = pimage.getData().getTransferType();
            
            // Dependiendo del tipo de imagen copia la informacion de pixeles
            // es decir, realiza la lectura en el contenedor img correspondiente
            switch (transfer) {
                
                // Imagen de 8 bits
                case DataBuffer.TYPE_BYTE:
                    IJ.showStatus("TYPE_BYTE");
                    byte[] writepixels_b = (byte[]) img[i].getProcessor().getPixels();
                    for(int j=0;j<width*height;j++)
                        writepixels_b[j] = (byte) readpixels[j];
                    break;

                case DataBuffer.TYPE_DOUBLE: IJ.showMessage("TYPE_DOUBLE"); break;

                case DataBuffer.TYPE_FLOAT:
                    IJ.showStatus("TYPE_FLOAT");
                    float[] writepixels_f = (float[]) img[i].getProcessor().getPixels();
                    for(int j=0;j<width*height;j++)
                        writepixels_f[j] = (float) readpixels[j];
                    break;
                    // Imagenes de 32 bits
                case DataBuffer.TYPE_INT: 
                    IJ.showStatus("TYPE_INT");
                    int[] writepixels_i = (int[]) img[i].getProcessor().getPixels();
                    for(int j=0;j<width*height;j++)
                        writepixels_i[j] = readpixels[j];
                    break;
             
                case DataBuffer.TYPE_SHORT: IJ.showMessage("TYPE_SHORT"); break;
                // En caso de no reconocer el tipo de imagen
                case DataBuffer.TYPE_UNDEFINED: IJ.showMessage("TYPE_UNDEFINED!!!"); break;
                // Imagenes de 16 bits
                case DataBuffer.TYPE_USHORT:
                    IJ.showStatus("TYPE_USHORT");
                    short[] writepixels_us = (short[]) img[i].getProcessor().getPixels();
                    for(int j=0;j<width*height;j++)
                        writepixels_us[j] = (short) readpixels[j];
                    break;
            } // switch
        } // for - bandas
    }
    
    
}
