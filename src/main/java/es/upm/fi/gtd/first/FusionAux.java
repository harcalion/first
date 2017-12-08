/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first;

import ij.ImagePlus;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.util.HashMap;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;

/**
 *
 * @author Alvar
 */
public class FusionAux {

    
        /**
     * Convierte rápidamente (usando JAI) una imagen de un tipo de datos a tipo
     * int
     * @param img Imagen de entrada
     * @return Imagen cuyos datos han sido pasados a int
     */
    public static GeoImg expandirRango(GeoImg img) {
//	Pasamos el número de niveles de gris de 8/16 bits a 32 bits
	Raster ras = img.getImage().getData();
	int tipo = ras.getTransferType();
	int width = img.getImage().getWidth();
	int height = img.getImage().getHeight();	

	if (false)
	{// Haciendo un cambio de Buffer usando JAI
		ParameterBlock pbTipo = new ParameterBlock();
		pbTipo.addSource(img.getImage());
		pbTipo.add(DataBuffer.TYPE_INT);
		img.setImage(JAI.create("format",pbTipo));
	}
	else
	{ // A lo cutre

		if(tipo== DataBuffer.TYPE_BYTE)
		{
			byte[] pixels8 = new byte [width*height];
			ras.getDataElements(0,0,width,height,pixels8);
			int[] pixels32 = new int[width * height];
			for (int i=0; i<width*height; i++) {

				pixels32[i] = (pixels8[i]&0xff);
				//System.out.println("pixel:"+pixels32[i]);
			}
			DataBufferInt dbuffer = new DataBufferInt(pixels32,pixels32.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, width, height, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,width, height, 0, 0, samplemod, colormod);		
			tiled.setData(raster);
			return new GeoImg(img.getData(),tiled,img.getFile(),img.getStreamMetadata(),img.getImageMetadata());
		}
		if (tipo == DataBuffer.TYPE_USHORT) 
		{
			short[] pixels16 = new short [width*height];
			ras.getDataElements(0,0,width,height,pixels16);
			int[] pixels32 = new int[width * height];
			for (int i=0; i<width*height; i++) {

				pixels32[i] = (pixels16[i]&0xffff);
				//System.out.println("pixel:"+pixels32[i]);
			}
			DataBufferInt dbuffer = new DataBufferInt(pixels32,pixels32.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, width, height, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,width, height, 0, 0, samplemod, colormod);

			tiled.setData(raster);
			return new GeoImg(img.getData(),tiled,img.getFile(),img.getStreamMetadata(),img.getImageMetadata());
		}
	}
	return img;
}
    
    /**
 * Halla el mínimo y el máximo de los valores de pixel de una imagen
 * @param image Imagen de entrada
 * @return El mínimo en el índice 0 y el máximo en el índice 1
 */

public static double [] hallarExtremos (PlanarImage image)
{
	ParameterBlock pbMaxMin = new ParameterBlock();
	pbMaxMin.addSource(image);

	RenderedOp extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    double[] allMins = (double[])extrema.getProperty("minimum");
    double[] allMaxs = (double[])extrema.getProperty("maximum");
    double minValueFus = allMins[0];
    System.out.println("Val min fus: "+minValueFus);
    double maxValueFus = allMaxs[0];
    System.out.println("Val max fus: "+maxValueFus);
    for(int v=1;v<allMins.length;v++)
      {
      if (allMins[v] < minValueFus) minValueFus = allMins[v];
      if (allMaxs[v] > maxValueFus) maxValueFus = allMaxs[v];
      }
    
    double [] res = new double[2];
    res[0] = minValueFus;
    res[1] = maxValueFus;
    return res;
}

/**
 * Traslada el rango de valores de una imagen a otra y produce una imagen Integer
 * @param fus Imagen origen
 * @param comp Imagen destino
 * @return Imagen con valores int cuyo rango es el de comp
 */
public static PlanarImage trasladarRangoInt (PlanarImage fus, PlanarImage comp)
{
	Raster ras = fus.getData();

	int width = fus.getWidth();
	int height = fus.getHeight();
	boolean reducir = true;
	ParameterBlock pbMaxMin = new ParameterBlock();

    pbMaxMin.addSource(fus);

	RenderedOp extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    double[] allMins = (double[])extrema.getProperty("minimum");
    double[] allMaxs = (double[])extrema.getProperty("maximum");
    double minValueFus = allMins[0];
    System.out.println("Val min fus: "+minValueFus);
    double maxValueFus = allMaxs[0];
    System.out.println("Val max fus: "+maxValueFus);
    for(int v=1;v<allMins.length;v++)
      {
      if (allMins[v] < minValueFus) minValueFus = allMins[v];
      if (allMaxs[v] > maxValueFus) maxValueFus = allMaxs[v];
      }
    pbMaxMin = new ParameterBlock();
    pbMaxMin.addSource(comp);

	extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    allMins = (double[])extrema.getProperty("minimum");
    allMaxs = (double[])extrema.getProperty("maximum");
    double minValueComp = allMins[0];
    System.out.println("Val min comp: "+minValueComp);
    double maxValueComp = allMaxs[0];
    System.out.println("Val max comp: "+maxValueComp);
    for(int v=1;v<allMins.length;v++)
      {
      if (allMins[v] < minValueComp) minValueComp = allMins[v];
      if (allMaxs[v] > maxValueComp) maxValueComp = allMaxs[v];
      }
	if (reducir) {

			int[] pixels32 = new int[width * height];

			ras.getDataElements(0, 0, width, height, pixels32);
			int[] pixels16 = new int[width * height];
			for (int i = 0; i < width * height; i++) {
				int temp = pixels32[i] - (int) minValueFus;
				
				pixels16[i] = (int) (temp * (maxValueComp-minValueComp) / (maxValueFus - minValueFus));
				pixels16[i] += minValueComp;
				/*if (pixels32[i]<0)
				{
					System.out.println("pixel original:"+pixels32[i]);
					System.out.println("pixel transformado:"+pixels16[i]);
				}*/
			}
			DataBufferInt dbuffer = new DataBufferInt(pixels32, pixels32.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(
					DataBuffer.TYPE_INT, width, height, 1);
			ColorModel colormod = PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod,
					dbuffer, new Point(0, 0));
			TiledImage tiled = new TiledImage(0, 0, width, height, 0, 0,
					samplemod, colormod);
			tiled.setData(raster);
			return tiled;
		}
	else 
	{
		return fus;
	}
	
}

 /**
     * Traslada el rango de valores de una imagen a otra y produce una imagen short
     * @param fus Imagen origen
     * @param comp Imagen destino
     * @return Imagen cuyo rango y tipo de datos son iguales a los de comp
     */
public static PlanarImage trasladarRangoShort (PlanarImage fus, PlanarImage comp)
{
	Raster ras = fus.getData();
	int tipoFus = ras.getTransferType();
	int tipoComp = comp.getData().getTransferType();
	int width = fus.getWidth();
	int height = fus.getHeight();
	boolean reducir = false;
	ParameterBlock pbMaxMin = new ParameterBlock();
	if ((tipoFus == DataBuffer.TYPE_INT) && (tipoComp == DataBuffer.TYPE_USHORT))
	{
		reducir = true;
	}
    pbMaxMin.addSource(fus);

	RenderedOp extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    double[] allMins = (double[])extrema.getProperty("minimum");
    double[] allMaxs = (double[])extrema.getProperty("maximum");
    double minValueFus = allMins[0];
    System.out.println("Val min fus: "+minValueFus);
    double maxValueFus = allMaxs[0];
    System.out.println("Val max fus: "+maxValueFus);
    for(int v=1;v<allMins.length;v++)
      {
      if (allMins[v] < minValueFus) minValueFus = allMins[v];
      if (allMaxs[v] > maxValueFus) maxValueFus = allMaxs[v];
      }
    pbMaxMin = new ParameterBlock();
    pbMaxMin.addSource(comp);

	extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    allMins = (double[])extrema.getProperty("minimum");
    allMaxs = (double[])extrema.getProperty("maximum");
    double minValueComp = allMins[0];
    System.out.println("Val min comp: "+minValueComp);
    double maxValueComp = allMaxs[0];
    System.out.println("Val max comp: "+maxValueComp);
    for(int v=1;v<allMins.length;v++)
      {
      if (allMins[v] < minValueComp) minValueComp = allMins[v];
      if (allMaxs[v] > maxValueComp) maxValueComp = allMaxs[v];
      }
	if (reducir) {

			int[] pixels32 = new int[width * height];

			ras.getDataElements(0, 0, width, height, pixels32);
			short[] pixels16 = new short[width * height];
			for (int i = 0; i < width * height; i++) {
				int temp = (short) (pixels32[i] - minValueFus);
				
				pixels16[i] = 
                                        (short) ((temp * (maxValueComp-minValueComp) / (maxValueFus - minValueFus)));
				pixels16[i] += minValueComp;
				/*if (pixels32[i]<0)
				{
					System.out.println("pixel original:"+pixels32[i]);
					System.out.println("pixel transformado:"+pixels16[i]);
				}*/
			}
			DataBufferUShort dbuffer = new DataBufferUShort(pixels16, pixels16.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(
					DataBuffer.TYPE_USHORT, width, height, 1);
			ColorModel colormod = PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod,
					dbuffer, new Point(0, 0));
			TiledImage tiled = new TiledImage(0, 0, width, height, 0, 0,
					samplemod, colormod);
			tiled.setData(raster);
			return tiled;
		}
	else 
	{
		return fus;
	}
	
}
/**
 * Transforma una imagen para adaptarse al rango de otra. Devuelve una imagen de bytes.
 * @param fus Imagen origen
 * @param comp Imagen destino
 * @return Imagen con valores byte cuyo rango es el de comp
 */

public static PlanarImage trasladarRangoByte (PlanarImage fus, PlanarImage comp)
{
	Raster ras = fus.getData();
	int tipoFus = ras.getTransferType();
	int tipoComp = comp.getData().getTransferType();
	int width = fus.getWidth();
	int height = fus.getHeight();
	boolean reducir = false;
	ParameterBlock pbMaxMin = new ParameterBlock();
	if ((tipoFus == DataBuffer.TYPE_INT) && (tipoComp == DataBuffer.TYPE_BYTE))
	{
		reducir = true;
	}
    pbMaxMin.addSource(fus);

	RenderedOp extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    double[] allMins = (double[])extrema.getProperty("minimum");
    double[] allMaxs = (double[])extrema.getProperty("maximum");
    double minValueFus = allMins[0];
    System.out.println("Val min fus: "+minValueFus);
    double maxValueFus = allMaxs[0];
    System.out.println("Val max fus: "+maxValueFus);
    for(int v=1;v<allMins.length;v++)
      {
      if (allMins[v] < minValueFus) minValueFus = allMins[v];
      if (allMaxs[v] > maxValueFus) maxValueFus = allMaxs[v];
      }
    pbMaxMin = new ParameterBlock();
    pbMaxMin.addSource(comp);

	extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    allMins = (double[])extrema.getProperty("minimum");
    allMaxs = (double[])extrema.getProperty("maximum");
    double minValueComp = allMins[0];
    System.out.println("Val min comp: "+minValueComp);
    double maxValueComp = allMaxs[0];
    System.out.println("Val max comp: "+maxValueComp);
    for(int v=1;v<allMins.length;v++)
      {
      if (allMins[v] < minValueComp) minValueComp = allMins[v];
      if (allMaxs[v] > maxValueComp) maxValueComp = allMaxs[v];
      }
    if (reducir) {
    	TiledImage tiled = null;


    	int[] pixels32 = new int[width * height];

    	ras.getDataElements(0, 0, width, height, pixels32);
    	byte[] pixels8 = new byte[width * height];
    	for (int i = 0; i < width * height; i++) {
    		int temp = (short) (pixels32[i] - minValueFus);

    		pixels8[i] = (byte) ((temp * (maxValueComp-minValueComp) / (maxValueFus - minValueFus)));
    		pixels8[i] += minValueComp;

    	}
    	DataBufferByte dbuffer = new DataBufferByte(pixels8, pixels8.length);
    	SampleModel samplemod = RasterFactory.createBandedSampleModel(
    			DataBuffer.TYPE_BYTE, width, height, 1);
    	ColorModel colormod = PlanarImage.createColorModel(samplemod);
    	Raster raster = RasterFactory.createWritableRaster(samplemod,
    			dbuffer, new Point(0, 0));
    	tiled = new TiledImage(0, 0, width, height, 0, 0,
    			samplemod, colormod);
    	tiled.setData(raster);


    	return tiled;
    }
    else 
    {
    	return fus;
    }
	
}

 /**
     * Transforma y traslada el rango de la imagen original a una imagen de tipo
     * byte y con origen en el 0
     * @param image Imagen de entrada
     * @param min Mínimo de la imagen original
     * @param max Máximo de la imagen original
     * @return Imagen reducida y trasladada
     */
public static PlanarImage reducirRango (PlanarImage image, double min, double max)
{
	Raster ras = image.getData();
	int tipo = ras.getTransferType();
	int width = image.getWidth();
	int height = image.getHeight();
	int bandas = image.getSampleModel().getNumBands();
	if(tipo== DataBuffer.TYPE_INT)
	{
            
		if (bandas == 1)
		{

		int[] pixels32 = new int [width*height];

		ras.getDataElements(0,0,width,height,pixels32);
		byte[] pixels8 = new byte[width*height];
		for (int i=0; i<width*height; i++) {
			int temp = (short) (pixels32[i]-min);
			pixels8[i] = (byte) ((temp*255)/(max-min));
			//System.out.println("pixel:"+pixels32[i]);
		}
		DataBufferByte dbuffer = new DataBufferByte(pixels8,pixels8.length);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,width, height, 0, 0, samplemod, colormod);		
		tiled.setData(raster);
		return tiled;
                }
                else
		{
			PlanarImage [] bands = new PlanarImage[bandas];
			PlanarImage [] bandsred = new PlanarImage[bandas];
			for (int i = 0; i < bandas;i++)
			{

				ParameterBlockJAI selectPb = new ParameterBlockJAI( "BandSelect" );
				selectPb.addSource(image);
				selectPb.setParameter( "bandIndices", new int[] { i } );
				bands [i] = JAI.create("BandSelect", selectPb);
				double [] extremos = hallarExtremos(bands[i]);
				bandsred[i] = reducirRango(bands[i],extremos[0],extremos[1]);


			}
			ParameterBlock pb = new ParameterBlock();
			for (PlanarImage pi:bandsred)
			{
				pb.addSource(pi);
			}
			return JAI.create("bandmerge",pb);
		}
	}
	else if (tipo == DataBuffer.TYPE_FLOAT)
	{
            if (bandas == 1)
		{
		float[] pixels32 = new float [width*height];
		
		ras.getDataElements(0,0,width,height,pixels32);
		byte[] pixels8 = new byte[pixels32.length];
		for (int i=0; i<width*height; i++) {
			//int temp = (short) Math.log10(pixels32[i]-min);
			//System.out.println("Valor logaritmizado: "+temp);
			//pixels8[i] = (byte) ((temp*255)/(max-min));
			Float f = new Float(pixels32[i]);
			byte b = f.byteValue();
			pixels8[i] = (byte) (b+255);
			System.out.println("pixel:"+pixels8[i]);
		}
		DataBufferByte dbuffer = new DataBufferByte(pixels8,pixels8.length);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,width, height, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
            }
            else
		{
			PlanarImage [] bands = new PlanarImage[bandas];
			PlanarImage [] bandsred = new PlanarImage[bandas];
			for (int i = 0; i < bandas;i++)
			{

				ParameterBlockJAI selectPb = new ParameterBlockJAI( "BandSelect" );
				selectPb.addSource(image);
				selectPb.setParameter( "bandIndices", new int[] { i } );
				bands [i] = JAI.create("BandSelect", selectPb);
				double [] extremos = hallarExtremos(bands[i]);
				bandsred[i] = reducirRango(bands[i],extremos[0],extremos[1]);


			}
			ParameterBlock pb = new ParameterBlock();
			for (PlanarImage pi:bandsred)
			{
				pb.addSource(pi);
			}
			return JAI.create("bandmerge",pb);
		}
            

	}
	else if (tipo== DataBuffer.TYPE_USHORT)
	{
		
		if (bandas == 1)
		{
			short[] pixels16 = new short [width*height*bandas];

			ras.getDataElements(0,0,width,height,pixels16);
			byte[] pixels8 = new byte[pixels16.length];
			for (int i=0; i<width*height; i++) {
				short temp = (short) (pixels16[i]-min);

				pixels8[i] = (byte) ((temp*255)/(max-min));

			}
			DataBufferByte dbuffer = new DataBufferByte(pixels8,pixels8.length);
			SampleModel samplemod;
			samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, width, height, bandas);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,width, height, 0, 0, samplemod, colormod);

			tiled.setData(raster);
			return tiled;
		}
		else
		{
			PlanarImage [] bands = new PlanarImage[bandas];
			PlanarImage [] bandsred = new PlanarImage[bandas];
			for (int i = 0; i < bandas;i++)
			{

				ParameterBlockJAI selectPb = new ParameterBlockJAI( "BandSelect" );
				selectPb.addSource(image);
				selectPb.setParameter( "bandIndices", new int[] { i } );
				bands [i] = JAI.create("BandSelect", selectPb);
				double [] extremos = hallarExtremos(bands[i]);
				bandsred[i] = reducirRango(bands[i],extremos[0],extremos[1]);


			}
			ParameterBlock pb = new ParameterBlock();
			for (PlanarImage pi:bandsred)
			{
				pb.addSource(pi);
			}
			return JAI.create("bandmerge",pb);
		}

	}
	else if (tipo == DataBuffer.TYPE_BYTE)
	{

		return image;
	}
	return null;
}

/**
 * Transforma una imagen ImagePlus (ImageJ) en PlanarImage
 * @param img Imagen básica de ImagePlus
 * @param orig Imagen con los metadatos que tendrá la imagen final
 * @param tipo No usado
 * @return PlanarImage resultado
 */
public static PlanarImage transformarDesdeImagePlus(Image img, PlanarImage orig, int tipo)
{
   RenderedImage ri = JAI.create("awtimage",img);
           SampleModel sm = orig.getSampleModel();
           ComponentColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),false, false, ComponentColorModel.OPAQUE,sm.getTransferType());
           
           
           /*SampleModel nsm = RasterFactory.createBandedSampleModel(tipo,
                                             sm.getWidth(),sm.getHeight(),
                                             1);*/
           //ColorModel cm = PlanarImage.createColorModel(nsm);
           javax.media.jai.ImageLayout layout = new ImageLayout(ri);
            layout.setColorModel(cm);
            layout.setSampleModel(cm.createCompatibleSampleModel(sm.getWidth(),
                    sm.getHeight()));
            HashMap<RenderingHints.Key,ImageLayout> map = 
      new HashMap<RenderingHints.Key,ImageLayout>();
    map.put(JAI.KEY_IMAGE_LAYOUT,layout);
    RenderingHints hints = new RenderingHints(map);
    // Reformat the image using the above hints.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(ri);
    // We don't really want to change the original surrogate image...
    PlanarImage pi = JAI.create("format",pb,hints);
    return pi; 
}

/**
 * Transforma una imagen ImagePlus (ImageJ) en PlanarImage
 * @param img Imagen básica de ImagePlus
 * @param orig Imagen con los metadatos que tendrá la imagen final
 * @return PlanarImage resultado
 */
public static PlanarImage transformarDesdeImagePlus(Image img, PlanarImage orig)
{
    RenderedImage ri = JAI.create("awtimage",img);
           SampleModel sm = orig.getSampleModel();
           ComponentColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),false, false, ComponentColorModel.OPAQUE,sm.getTransferType());
           
           
           /*SampleModel nsm = RasterFactory.createBandedSampleModel(sm.getTransferType(),
                                             sm.getWidth(),sm.getHeight(),
                                             1);*/
           //ColorModel cm = PlanarImage.createColorModel(nsm);
           javax.media.jai.ImageLayout layout = new ImageLayout(ri);
            layout.setColorModel(cm);
            layout.setSampleModel(cm.createCompatibleSampleModel(sm.getWidth(),
                    sm.getHeight()));
            HashMap<RenderingHints.Key,ImageLayout> map = 
      new HashMap<RenderingHints.Key,ImageLayout>();
    map.put(JAI.KEY_IMAGE_LAYOUT,layout);
    RenderingHints hints = new RenderingHints(map);
    // Reformat the image using the above hints.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(ri);
    // We don't really want to change the original surrogate image...
    PlanarImage pi = JAI.create("format",pb,hints);
    return pi;
}

/**
 * Transforma una imagen ImagePlus (ImageJ) en PlanarImage
 * @param img Imagen básica de ImagePlus
 * @return PlanarImage resultado
 */
public static PlanarImage transformarDesdeImagePlus(Image img)
{
    RenderedImage ri = JAI.create("awtimage",img);
           SampleModel sm = RasterFactory.createBandedSampleModel(
                                DataBuffer.TYPE_INT, ri.getWidth(), ri.getHeight(), 1);
           ComponentColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),false, false, ComponentColorModel.OPAQUE,sm.getTransferType());
           
           
           /*SampleModel nsm = RasterFactory.createBandedSampleModel(sm.getTransferType(),
                                             sm.getWidth(),sm.getHeight(),
                                             1);*/
           //ColorModel cm = PlanarImage.createColorModel(nsm);
           javax.media.jai.ImageLayout layout = new ImageLayout(ri);
            layout.setColorModel(cm);
            layout.setSampleModel(cm.createCompatibleSampleModel(sm.getWidth(),
                    sm.getHeight()));
            HashMap<RenderingHints.Key,ImageLayout> map = 
      new HashMap<RenderingHints.Key,ImageLayout>();
    map.put(JAI.KEY_IMAGE_LAYOUT,layout);
    RenderingHints hints = new RenderingHints(map);
    // Reformat the image using the above hints.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(ri);
    // We don't really want to change the original surrogate image...
    PlanarImage pi = JAI.create("format",pb,hints);
    return pi;
}

/**
 * Transforma una imagen ImagePlus (ImageJ) en PlanarImage
 * @param img Imagen básica de ImagePlus
 * @param orig Imagen con los metadatos que tendrá la imagen final
 * @return PlanarImage resultado
 */
public static PlanarImage transformarDesdeImagePlus(ImagePlus img, PlanarImage orig)
{
    RenderedImage ri = JAI.create("awtimage",img.getImage());
           SampleModel sm = orig.getSampleModel();
           ComponentColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),false, false, ComponentColorModel.OPAQUE,sm.getTransferType());
           
           
           /*SampleModel nsm = RasterFactory.createBandedSampleModel(sm.getTransferType(),
                                             sm.getWidth(),sm.getHeight(),
                                             1);*/
           //ColorModel cm = PlanarImage.createColorModel(nsm);
           javax.media.jai.ImageLayout layout = new ImageLayout(ri);
            layout.setColorModel(cm);
            layout.setSampleModel(cm.createCompatibleSampleModel(sm.getWidth(),
                    sm.getHeight()));
            HashMap<RenderingHints.Key,ImageLayout> map = 
      new HashMap<RenderingHints.Key,ImageLayout>();
    map.put(JAI.KEY_IMAGE_LAYOUT,layout);
    RenderingHints hints = new RenderingHints(map);
    // Reformat the image using the above hints.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(ri);
    // We don't really want to change the original surrogate image...
    PlanarImage pi = JAI.create("format",pb,hints);
    return pi;
}
    
}
