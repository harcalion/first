package es.upm.fi.gtd.first;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;

/**
 * Los dos m�todos contenidos en esta clase son utilidades para manejar los archivos BIL
 * y que por su longitud en cuanto a c�digo son molestos en la clase de interfaz
 * @author Alvar Garc�a del R�o
 * @see Atrous#manejoBIL
 * 
 */

public class AbrirBIL
{
	
/**
 * Este m�todo convierte un archivo ASCII con bandas intercaladas por l�nea (BIL) en una 
 * colecci�n de im�genes (en escala de grises, 128 niveles)
 * 
 * @param file El archivo ASCII que se va a transformar en imagen
 * @param bandas N�mero de bandas que contiene el archivo
 * @param lins N�mero de l�neas (resoluci�n vertical) de las im�genes contenidas
 * @param cols N�mero de columnas (resoluci�n horizontal) de las im�genes contenidas
 * @param celdas N�mero de p�xeles por l�nea (no se usa)
 * @return Un array de im�genes con longitud bandas
 * @throws IOException
 */
	
	public TiledImage[] transBIL(File file, int bandas, int lins, int cols, int celdas) throws IOException
	{


		// Leyendo BIL (banda, linea, celda distintas de 0)
			

				System.out.println(bandas);
				FileInputStream fis = new FileInputStream(file.getPath());
				InputStreamReader isr = new InputStreamReader(fis, "ASCII");
				

				byte [][][] data = new byte [bandas][lins][cols];
				byte a = ' ';
				FileReader stream = null;
				stream = new FileReader(file);
				// Por cada banda
				for (int i = 0; i < bandas; i++)
				{
					//Por cada l�nea
					for (int j = 0; j < lins; j++)
					{
						//Por cada celda 
						for (int k = 0; k < cols; k++)
						{
							// Hay que saltar los saltos de l�nea y eso
							a = (byte) isr.read();
							//System.out.println(a);

							if (Byte.valueOf(a).compareTo((byte)0x0a)==0)
							{
								//System.out.println("He encontrado salto de l�nea");
								
								a = (byte) isr.read();
								data[i][j][k] = a;
							}
							else
							{
								data[i][j][k] = a;
							}
							
						}		
					
						
					}
				}
				byte [][] imagenes = new byte [bandas][lins*cols];
				for (int i = 0; i < bandas; i++)
				{ int contador =0;
					for (int j = 0; j < lins; j++)
					{
						for (int k = 0; k < cols; k++)
						{
							imagenes [i][contador++]=data[i][j][k];

						} // FIN FORK

					} // FIN FORJ
				} // FIN FORI
				
				TiledImage [] output = new TiledImage[bandas];
				for (int i=0; i < bandas; i++)
				{
					DataBufferByte dbuffer = new DataBufferByte(imagenes[i],celdas*lins);
					SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, celdas, lins, 1);
					ColorModel colormod = PlanarImage.createColorModel(samplemod);
					//Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
					Raster raster = RasterFactory.createRaster(samplemod, dbuffer, new Point(0,0));
					TiledImage tiled = new TiledImage(0,0,celdas, lins, 0, 0, samplemod, colormod);
					tiled.setData(raster);
					output[i]=tiled;
				}
				stream.close();
				
		return output;		



				
}
	/**
	 * M�todo que guarda todas las im�genes actualmente mostradas en la aplicaci�n en un archivo ASCII
	 * de bandas intercaladas por l�nea (BIL)
	 * @param imagenes Im�genes actualmente mostradas
	 * @param guardar El directorio donde se almacenar� el archivo ASCII BIL
	 */
	
	
	public void guardarBIL (PlanarImage [] imagenes, File guardar)
	{
		Raster rast;
		int transfer,cont=0;
		FileWriter raf = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try{
		//raf=new FileWriter(guardar.getPath()+File.separator+"Imagen.bil");
			 fos = new FileOutputStream(guardar.getPath()+File.separator+"Imagen.bil");
			 osw= new OutputStreamWriter(fos, "ASCII");
			
		}
		catch (FileNotFoundException fnfe){ Dialogos.informarExcepcion(fnfe);}
		catch (IOException ioe) {Dialogos.informarExcepcion(ioe);}
		


		// Voy a poner 10 valores por l�nea
		for (int i = 0; i < imagenes.length; i++)
		{
			rast = imagenes[i].getData();
			transfer = rast.getTransferType();
			switch (transfer)
			{
				case (DataBuffer.TYPE_BYTE):
				{
					//CharBuffer cb = bb.asCharBuffer();
					byte [] buffer = new byte [imagenes[i].getWidth()*imagenes[i].getHeight()];
					rast.getDataElements(0,0,imagenes[i].getWidth(),imagenes[i].getHeight(),buffer);
					for (int j = 0; j < buffer.length; j++)
					{
						try
						{	
						if (cont == 9)
						{													
							osw.write(buffer[j]);
							osw.write('\n');
							cont = 0;
						}
						else
						{
							osw.write(buffer[j]);
							cont++;
						}
						}catch (IOException ioe) {Dialogos.informarExcepcion(ioe);}
						
						
					}
					
					break;
				}
				
				case (DataBuffer.TYPE_INT):
				{
					int [] buffer = new int [imagenes[i].getWidth()*imagenes[i].getHeight()];

					rast.getDataElements(0,0,imagenes[i].getWidth(),imagenes[i].getHeight(),buffer);
					for (int j = 0; j < buffer.length; j++)
					{
						try
						{
							
							//raf.write(buffer,0+i*10,10);
							raf.write('\n');
						}catch (IOException ioe) {Dialogos.informarExcepcion(ioe);}
						
					}
					break;
				}
				case (DataBuffer.TYPE_FLOAT):
				{
					float [] buffer = new float [imagenes[i].getWidth()*imagenes[i].getHeight()];
					rast.getDataElements(0,0,imagenes[i].getWidth(),imagenes[i].getHeight(),buffer);
					for (int j = 0; j < buffer.length; j++)
					{
						try
						{
						//	raf.write(buffer,0+i*10,10);
							raf.write('\n');
						}catch (IOException ioe) {Dialogos.informarExcepcion(ioe);}
						
					}
					break;
				}
				case (DataBuffer.TYPE_DOUBLE):
				{
					double [] buffer = new double [imagenes[i].getWidth()*imagenes[i].getHeight()];
					rast.getDataElements(0,0,imagenes[i].getWidth(),imagenes[i].getHeight(),buffer);
					for (int j = 0; j < buffer.length; j++)
					{
						try
						{
							
						//	raf.write(buffer,0+i*10,10);
							raf.write('\n');
						}catch (IOException ioe) {Dialogos.informarExcepcion(ioe);}
						
					}
				}
			
				break;
			}
			
		}
		
		
		try
		{

			osw.close();
			

		}
		catch (IOException ioe){Dialogos.informarExcepcion(ioe);}
		
	}
	
}
