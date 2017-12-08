package es.upm.fi.gtd.first;

import java.awt.Transparency;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PackedColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.media.jai.PlanarImage;

import com.sun.media.imageio.plugins.tiff.EXIFTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.GeoTIFFTagSet;

/**
 * Esta clase obtiene un conjunto de valores informativos sobre una imagen.
 * 
 * @author Rafael Santos
 * @author Alvar García del Río
 *
 */

public class Info {

	    /**
     * Obtiene los metadatos de imagen
     * @param ti Adaptador de GeoTIFF
     * @return Los metadatos de fichero
     */
	public static IIOMetadata crearStreamData (GeoTiffIIOMetadataAdapter ti)
	{
		return ti.getStreamMetadata();
	}
	
    /**
     * Obtiene los metadatos de imagen
     * @param ti Adaptador de GeoTIFF
     * @return Los metadatos de imagen
     */
	public static IIOMetadata crearImageData (GeoTiffIIOMetadataAdapter ti)
	{
		return ti.getImageMetadata();
	}

    /**
     * Obtiene los datos geográficos de una imagen
     * @param ti Adaptador creado a partir del nombre de fichero
     * @return Los datos geográficos
     */
	public static GeoData crearGeoData(GeoTiffIIOMetadataAdapter ti)
	{
		
		
		IIOMetadataNode geoKeyDir = ti.getTiffField(
	            GeoTIFFTagSet.TAG_GEO_KEY_DIRECTORY);
		if (geoKeyDir==null)
		{
			return null;
		}
		
		Map<Integer,String> codigos = new HashMap<Integer,String>();
		for (int i = 0; i < ti.getNumGeoKeys(); i++)
		{
			int result = ti.getTiffShort(geoKeyDir, 4+4*i);
			String key = ti.getGeoKey(result);
			if (key != null)
			{
				codigos.put(result, key);
			}
			//System.out.println("GeoKey contenidas en el Directorio:"+result);
		}
		
									
		double [] pixelscale = ti.getModelPixelScales();
		double [] tiep = ti.getModelTiePoints();
		
		GeoData gd = new GeoData(pixelscale, tiep, codigos);
		return gd;
	}
	
    /**
     * Escribe por la consola los datos geográficos del fichero
     * @param datos Árbol de metadatos del fichero
     */
    public static void mostrarGeoInfo(IIOMetadata datos)
        {
            GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(datos);
            
		

		
		double [] pixelscale = ti.getModelPixelScales();
		String scales = new String("");
		if (pixelscale != null)
		{
			for (int i = 0; i < pixelscale.length; i++)
			{
				
				scales = scales.concat(Double.toString(pixelscale[i])+",");
				scales = scales.concat("\n");
			}
		}
		else
		{
			scales = scales.concat("No se han encontrado las escalas");
		}
		System.out.println("Pixel scale:" + scales);
		double [] tiep = ti.getModelTiePoints();
		String tie = new String("");
		if (tiep != null)
		{
			for (int i = 0; i < tiep.length; i++)
			{
				
				tie = tie.concat(Double.toString(tiep[i])+",");
				tie = tie.concat("\n");
			}
		}
		else
		{
			tie = tie.concat("No se han encontrado tiepoints");
		}
		System.out.println("Tiepoints:"+tie);
		
		double [] trans = ti.getModelTransformation();
		String transformacion = new String("");
		if (trans != null)
		{
			for (int i = 0; i < trans.length; i++)
			{
				
				transformacion = transformacion.concat(Double.toString(trans[i])+",");
				transformacion = transformacion.concat("\n");
			}
		}
		else
		{
			transformacion = transformacion.concat("No se ha encontrado la transformación");
		}
		System.out.println("Transformación:"+transformacion);
		
		try
		{
			
			System.out.println("Número de Geo Keys: "+ti.getNumGeoKeys());

			ParseXML modelxml = new ParseXML("modeltype.xml");			
			ParseXML rasterxml = new ParseXML("rastertype.xml");						
			// Como cada fichero puede tener GeoKeys muy diferentes, primero se escanea
			// todo el TIFF y se recogen las claves presentes
			ParseXML geoxml = new ParseXML("GeoTIFFcodes.xml");
			
			
			System.out.println("Resto de claves:");
			
			for (int i = 0; i < 65535; i++)
			{
				IIOMetadataNode key = ti.getTiffField(i);
				if (key != null)
				{
					/*
					if (i == 1024)
					{
						System.out.println("Clave con código: "+i+" cuyo nombre es "+geoxml.buscar(Integer.toString(i), new String("value"))+" tiene el valor "+key+" cuyo significado es "+modelxml.buscar(key., new String("value")));
					}
					else if (i == 1025)
					{
						System.out.println("Clave con código: "+i+" cuyo nombre es "+geoxml.buscar(Integer.toString(i), new String("value"))+" tiene el valor "+key+" cuyo significado es "+rasterxml.buscar(key, new String("value")));
					}
					else
					{
					
						System.out.println("Clave con código: "+i+" cuyo nombre es "+geoxml.buscar(Integer.toString(i), new String("value"))+" tiene el valor "+key+" cuyo significado es "+geoxml.buscar(key, new String("value")));
					
					}*/
					System.out.println("Clave con código: "+i);
				}
			}
			
			
//			 Unidades lineales
			String key = ti.getGeoKey(GeoTiffIIOMetadataAdapter.GeogLinearUnitsGeoKey);
			
			System.out.println("Unidades lineales:");
                        ParseXML pxml = new ParseXML("GeoTIFFcodes.xml");
			
			System.out.println(pxml.buscar(key, new String("value")));
			
//			 Unidades angulares
			key = ti.getGeoKey(GeoTiffIIOMetadataAdapter.GeogAngularUnitsGeoKey);
			
			System.out.println("Unidades angulares:");
			System.out.println(pxml.buscar(key, new String("value")));
			
//			Tipo de PCS
			key = ti.getGeoKey(GeoTiffIIOMetadataAdapter.ProjectedCSTypeGeoKey);
			
			System.out.println("Tipo de PCS:");
			System.out.println(pxml.buscar(key, new String("value")));
		}
		catch (UnsupportedOperationException uoe)
		{
			System.out.println("Este fichero no tiene GeoKeys");
		}
		
		

		
	
        }
    /**
     * Escribe por consola los datos geográficos que se van obteniendo del fichero
     * @param f Fichero cuya información se va a mostrar
     */
	public static void mostrarGeoInfo(File f)
	{
		GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(f);
		IIOMetadataNode node = ti.getTiffField(EXIFTIFFTagSet.TAG_DATE_TIME_ORIGINAL);
		String fecha = ti.getTiffAscii(node, 0, 20);
		System.out.println("La fecha de la imagen es "+fecha);
		
		/*System.out.println("Esto es la etiqueta donde está el puntero al  IFD EXIF "+EXIFParentTIFFTagSet.TAG_EXIF_IFD_POINTER);
		IIOMetadataNode node = ti.getTiffField(EXIFParentTIFFTagSet.TAG_EXIF_IFD_POINTER);
		if (node == null)
		{
			System.out.println("No hay un IFD EXIF");
		}
		int puntero = ti.getIntValueAttribute(node);		
		// Es esto el IFD EXIF?
		System.out.println("Esto es el puntero al IFD EXIF"+puntero);
		
		double [] pixelscale = ti.getModelPixelScales();
		String scales = new String("");
		if (pixelscale != null)
		{
			for (int i = 0; i < pixelscale.length; i++)
			{
				
				scales = scales.concat(Double.toString(pixelscale[i])+",");
				scales = scales.concat("\n");
			}
		}
		else
		{
			scales = scales.concat("No se han encontrado las escalas");
		}
		System.out.println("Pixel scale:" + scales);
		double [] tiep = ti.getModelTiePoints();
		String tie = new String("");
		if (tiep != null)
		{
			for (int i = 0; i < tiep.length; i++)
			{
				
				tie = tie.concat(Double.toString(tiep[i])+",");
				tie = tie.concat("\n");
			}
		}
		else
		{
			tie = tie.concat("No se han encontrado tiepoints");
		}
		System.out.println("Tiepoints:"+tie);
		
		double [] trans = ti.getModelTransformation();
		String transformacion = new String("");
		if (trans != null)
		{
			for (int i = 0; i < trans.length; i++)
			{
				
				transformacion = transformacion.concat(Double.toString(trans[i])+",");
				transformacion = transformacion.concat("\n");
			}
		}
		else
		{
			transformacion = transformacion.concat("No se ha encontrado la transformación");
		}
		System.out.println("Transformación:"+transformacion);
		*/
		try
		{
			/*
			System.out.println("Número de Geo Keys: "+ti.getNumGeoKeys());

			ParseXML modelxml = new ParseXML("modeltype.xml");			
			ParseXML rasterxml = new ParseXML("rastertype.xml");						
			// Como cada fichero puede tener GeoKeys muy diferentes, primero se escanea
			// todo el TIFF y se recogen las claves presentes
			ParseXML geoxml = new ParseXML("GeoTIFFcodes.xml");
			*/
			
			System.out.println("Resto de claves:");
			
			for (int i = 0; i < 65535; i++)
			{
				IIOMetadataNode key = ti.getTiffField(i);
				if (key != null)
				{
					/*
					if (i == 1024)
					{
						System.out.println("Clave con código: "+i+" cuyo nombre es "+geoxml.buscar(Integer.toString(i), new String("value"))+" tiene el valor "+key+" cuyo significado es "+modelxml.buscar(key, new String("value")));
					}
					else if (i == 1025)
					{
						System.out.println("Clave con código: "+i+" cuyo nombre es "+geoxml.buscar(Integer.toString(i), new String("value"))+" tiene el valor "+key+" cuyo significado es "+rasterxml.buscar(key, new String("value")));
					}
					else
					{
					
						System.out.println("Clave con código: "+i+" cuyo nombre es "+geoxml.buscar(Integer.toString(i), new String("value"))+" tiene el valor "+key+" cuyo significado es "+geoxml.buscar(key, new String("value")));
					
					}*/
					System.out.println("Clave con código: "+i);
				}
			}
			
			/*
//			 Unidades lineales
			key = ti.getGeoKey(GeoTiffIIOMetadataAdapter.GeogLinearUnitsGeoKey);
			geokey = Integer.valueOf(key);
			System.out.println("Unidades lineales:");
pxml = new ParseXML("GeoTIFFcodes.xml");
			
			System.out.println(pxml.buscar(Integer.toString(geokey), new String("value")));
			
//			 Unidades angulares
			key = ti.getGeoKey(GeoTiffIIOMetadataAdapter.GeogAngularUnitsGeoKey);
			geokey = Integer.valueOf(key);
			System.out.println("Unidades angulares:");
			System.out.println(pxml.buscar(Integer.toString(geokey), new String("value")));
			
//			Tipo de PCS
			key = ti.getGeoKey(GeoTiffIIOMetadataAdapter.ProjectedCSTypeGeoKey);
			geokey = Integer.valueOf(key);
			System.out.println("Tipo de PCS:");
			System.out.println(pxml.buscar(Integer.toString(geokey), new String("value")));*/
		}
		catch (UnsupportedOperationException uoe)
		{
			System.out.println("Este fichero no tiene GeoKeys");
		}
		
		

		
	}
        /**
	 * Muestra información sobre la imagen
	 * @param pi Imagen de entrada
	 */
public static void mostrarInfo (PlanarImage pi)
{
	
	SampleModel model_orig = pi.getSampleModel();

	
    System.out.println("Number of bands: "+model_orig.getNumBands());
    System.out.print("Data type: ");
    switch(model_orig.getDataType())
      {
      case DataBuffer.TYPE_BYTE:      System.out.println("byte");
                                      break;
      case DataBuffer.TYPE_SHORT:     System.out.println("short");
                                      break;
      case DataBuffer.TYPE_USHORT:    System.out.println("ushort");
                                      break;
      case DataBuffer.TYPE_INT:       System.out.println("int");
                                      break;
      case DataBuffer.TYPE_FLOAT:     System.out.println("float");
                                      break;
      case DataBuffer.TYPE_DOUBLE:    System.out.println("double");
                                      break;
      case DataBuffer.TYPE_UNDEFINED: System.out.println("undefined");
                                      break;
      }
    
    ColorModel cm = pi.getColorModel();
    if (cm != null)
      {
      System.out.print("Colormap type: ");
      if (cm instanceof DirectColorModel) 
        System.out.println("DirectColorModel");
      else if (cm instanceof PackedColorModel) 
        System.out.println("PackedColorModel");
      else if (cm instanceof IndexColorModel) 
        {
        IndexColorModel icm = (IndexColorModel)cm;
        System.out.println("IndexColorModel with "+icm.getMapSize()+" entries");
        }
      else if (cm instanceof ComponentColorModel) 
        System.out.println("ComponentColorModel");
      System.out.println("Number of color components: "+cm.getNumComponents());
      System.out.println("Bits per pixel: "+cm.getPixelSize());
      System.out.print("Transparency: ");
      switch(cm.getTransparency())
        {
        case Transparency.OPAQUE:      System.out.println("opaque");
                                       break;
        case Transparency.BITMASK:     System.out.println("bitmask");
                                       break;
        case Transparency.TRANSLUCENT: System.out.println("translucent");
                                       break;
        } // end switch
      }
    else System.out.println("No color model.");
    
    Raster input = pi.getData();
	System.out.print("Transfer type: ");
    switch(input.getTransferType())
      {
      case DataBuffer.TYPE_BYTE:      System.out.println("byte");
                                      break;
      case DataBuffer.TYPE_SHORT:     System.out.println("short");
                                      break;
      case DataBuffer.TYPE_USHORT:    System.out.println("ushort");
                                      break;
      case DataBuffer.TYPE_INT:       System.out.println("int");
                                      break;
      case DataBuffer.TYPE_FLOAT:     System.out.println("float");
                                      break;
      case DataBuffer.TYPE_DOUBLE:    System.out.println("double");
                                      break;
      case DataBuffer.TYPE_UNDEFINED: System.out.println("undefined");
                                      break;
      }
}

/**
 * Obtiene el TransferType (el tipo de los valores de los píxeles) de una imagen
 * @param pi Imagen de entrada
 * @return TransferType de la imagen (BYTE,INT,FLOAT,DOUBLE)
 */

public int getTransfer (PlanarImage pi)
{
	Raster input = pi.getData();
    return (input.getTransferType());
   
      
}
}
