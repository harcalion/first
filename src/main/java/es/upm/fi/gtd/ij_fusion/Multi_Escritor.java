package es.upm.fi.gtd.ij_fusion;



import ij.*;
import ij.io.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;

 


/**
 * Plugin para la escritura de archivos TIFF, más concretamente para los archivos
 * TIFF multibanda.
 * @author Francisco Javier Merino Guardiola
 *
 */
public class Multi_Escritor implements PlugInFilter {
	private int width;
	private int height;
	private int nbands;		// Numero de bandas leidas
	private int depth;

	ImageProcessor[] processor;
	

	/**
	 * Inicializacion del plugin
	 */
	public int setup(String arg, ImagePlus imp) {
		this.width = imp.getWidth();
		this.height = imp.getHeight();
		this.nbands = imp.getStackSize();
		this.depth = imp.getBitDepth();
		processor = new ImageProcessor[nbands];
		for (int i=0;i<nbands;i++)
			processor[i] = imp.getStack().getProcessor(i+1);
		return DOES_ALL+NO_CHANGES;
	}
	
	
	/**
	 * Ejecucion del plugin ImageJ.
	 */
	
	public void run(ImageProcessor ip) {
		EscribirArchivo("Seleccionar nombre de la imagen ...");
	}

	
	
	/**
	 * Solicita al usuario el nombre del fichero
	 * @param mensaje Mensaje a mostrar al usuario
	 */
	public void EscribirArchivo(String mensaje) {
		SaveDialog sd = new SaveDialog(mensaje, null,".tif");
		String directory = sd.getDirectory();
		String fileName = sd.getFileName();
		if (fileName==null) return;

		// Intentamos cargar el archivo
		try {
			write(directory, fileName);
		}
		catch (Exception e) {
			e.printStackTrace();
			IJ.showMessage("EXCEPCION: "+e.getMessage());
		}
	}

	
	/**
	 * Escritura de los pixeles haciendo uso de libreria <b>JAI</b>.
	 * @param directory Directorio en el que se almacenara el fichero
	 * @param fileName Nombre del fichero
	 */
	protected void write(String directory, String fileName) {

		int[][] array = new int[nbands][width*height];
		for(int x=0,k=0;x<height;x++)
			for(int y=0;y<width;y++,k++)
				for(int z=0;z<nbands;z++)
                                    // He cambiado y estaba a (y,x)
					array[z][k] = processor[z].getPixel(y, x);
		
		DataBufferInt[] dbuffer = new DataBufferInt[nbands];
		for(int z=0;z<nbands;z++)
			dbuffer[z]=new DataBufferInt(array[z], width*height);
		
		
		SampleModel sm;
                
                
                int profundidad;
                if(depth==8)
                    profundidad = java.awt.image.DataBuffer.TYPE_BYTE;
                else
                    profundidad = java.awt.image.DataBuffer.TYPE_USHORT;


                
                sm = RasterFactory.createBandedSampleModel(profundidad, width, height, 1);
                
		ColorModel cm = PlanarImage.createColorModel(sm);
		
		Raster[] raster = new Raster[nbands];
		TiledImage[] tiledImage = new TiledImage[nbands];
		for(int z=0;z<nbands;z++) {
			raster[z] = RasterFactory.createWritableRaster(sm,dbuffer[z],null);
			tiledImage[z] = new TiledImage(0, 0, width, height, 0, 0, sm, cm);
			tiledImage[z].setData(raster[z]);
		}

	    ParameterBlock pb = new ParameterBlock();

            for(int z=0;z<nbands;z++)
                pb.setSource(tiledImage[z], z);


            PlanarImage result = JAI.create("bandmerge",pb,null);
            JAI.create("filestore",result,directory+fileName,"TIFF");

            
	    
	    
	}

}
