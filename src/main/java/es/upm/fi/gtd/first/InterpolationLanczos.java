package es.upm.fi.gtd.first;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;

import es.upm.fi.gtd.first.jiu.MemoryGray32Image;
import es.upm.fi.gtd.first.jiu.MissingParameterException;
import es.upm.fi.gtd.first.jiu.Resample;
import es.upm.fi.gtd.first.jiu.WrongParameterException;

/**
 * Clase que implementa el algoritmo de remuestreo de Lanczos
 * @author Alvar
 */
public class InterpolationLanczos {

    /**
     * Interpola una imagen utilizando el filtro de Lanczos(3)
     * @param entrada Imagen de entrada
     * @param destW Anchura final
     * @param destH Altura final
     * @return Imagen interpolada
     */
public static PlanarImage interpolar (PlanarImage entrada,int destW, int destH)
{
//		 Interpolación de Lanczos
		int entradaW = entrada.getWidth();
		int entradaH = entrada.getHeight();
		MemoryGray32Image mim = new MemoryGray32Image(entradaW,entradaH);
		int [] samples = new int [entradaW*entradaH]; 
		entrada.getData().getPixels(0, 0, entradaW, entradaH, samples);
		mim.putSamples(0, 0, 0, entradaW, entradaH, samples, 0);
		Resample res = new Resample();
		res.setInputImage(mim);
		res.setSize(destW, destH);
		res.setFilter(Resample.FILTER_TYPE_LANCZOS3);
		
		try {
			res.process();
		} catch (MissingParameterException e) {
			
			e.printStackTrace();
		} catch (WrongParameterException e) {
			
			e.printStackTrace();
		}
		MemoryGray32Image mom = (MemoryGray32Image) res.getOutputImage();
		samples = new int[mom.getWidth()*mom.getHeight()];
		mom.getSamples(0, 0, 0, mom.getWidth(), mom.getHeight(), samples, 0);
		DataBufferInt dbuffer = new DataBufferInt(samples, samples.length);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(
				DataBuffer.TYPE_INT, mom.getWidth(), mom.getHeight(), 1);
		ColorModel colormod = PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod,
				dbuffer, new Point(0, 0));
		TiledImage tiled = new TiledImage(0, 0, mom.getWidth(), mom.getHeight(), 0, 0,
				samplemod, colormod);
		tiled.setData(raster);
		
		return tiled;
}
	
}
