package es.upm.fi.gtd.first;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

import javax.media.jai.DataBufferFloat;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;

/**
 * Clase que implementa una convolución discreta sobre un conjunto de datos matriciales obtenidos
 * de una imagen. Esta clase no se usa, utilizándose en su lugar el operador convolución de JAI
 * 
 * @author Alvar García del Río
 * @see javax.media.jai.JAI
 */

public class Convolucion {
	
	/**
	 * Convoluciona horizontalmente usando un filtro unidimensional horizontal.
	 * Tiene tolerancia a distintos tipos de píxeles en la imagen.
	 * 
	 * @param entrada La imagen a convolucionar
	 * @param filtro Filtro usado (obtenido de la clase Filtros)
	 * @return La imagen convolucionada
	 */
	PlanarImage convHor(PlanarImage entrada, double[] filtro)
	{
		
		Raster input = entrada.getData();
		int transfer = input.getTransferType();
		int cols = entrada.getWidth();
		int lins = entrada.getHeight();
		double[] hVar = filtro.clone();
		int maxFil=filtro.length;
		int maxPad=(int) Math.floor(filtro.length/2);

		

		
		// Tipo INT
		if (transfer == DataBuffer.TYPE_INT)
		{
			int [] pixeles = new int[cols*lins];
			int [] salida = new int[cols*lins];
		
		input.getPixels(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;
			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = maxPad-(pos-(cols-1));
				for (int p=maxFil;p>maxFil-(pos-1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					int pixel = pixeles[i+(j-maxPad)];
					//int pixint = pixel & 0xff;
					
					
					salida[i]+=hVar[j]*pixel;
					if (Math.abs(salida[i])>127)
					{
						//System.out.println("ENTRADA:"+pixel+" SALIDA: "+salida[i]);
					}
				}
				//System.out.println("ENTRADA:"+(pixeles[i] & 0xff));
			}

			//System.out.println("ENTRADA:"+(pixeles[i] & 0xff)+" SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}
		DataBufferInt dbuffer = new DataBufferInt(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_BYTE)
		{
			byte [] pixeles = new byte[cols*lins];
			byte [] salida = new byte[cols*lins];
		
		input.getDataElements(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;

			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = pos-(cols-1)+maxPad;
				for (int p=maxFil-1;p>maxFil-(pos+1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0.0D)
				{
					salida[i]+=0;						     
				}
				else
				{
					//System.out.println("i: "+i+"j: "+j);
					byte pixel = pixeles[i+(j-maxPad)];
					//int pixint = pixel & 0xff;
					
					
					salida[i]+=hVar[j]*pixel;
					if (Math.abs(salida[i])>127)
					{
						//System.out.println("ENTRADA:"+pixel+" SALIDA: "+salida[i]);
					}
				}
				//System.out.println("ENTRADA:"+(pixeles[i] & 0xff));
			}

			//System.out.println("ENTRADA:"+(pixeles[i] & 0xff)+" SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}
		DataBufferByte dbuffer = new DataBufferByte(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_FLOAT)
		{
			float [] pixeles = new float[cols*lins];
			float [] salida = new float[cols*lins];
		
		input.getDataElements(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;
			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = maxPad-(pos-(cols-1));
				for (int p=maxFil;p>maxFil-(pos-1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					float pixel = pixeles[i+(j-filtro.length/2)];
					//int pixint = pixel & 0xff;
					
					
					salida[i]+=hVar[j]*pixel;
					if (Math.abs(salida[i])>127)
					{
						//System.out.println("ENTRADA:"+pixel+" SALIDA: "+salida[i]);
					}
				}
				//System.out.println("ENTRADA:"+(pixeles[i] & 0xff));
			}

			//System.out.println("ENTRADA:"+(pixeles[i] & 0xff)+" SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}
		DataBufferFloat dbuffer = new DataBufferFloat(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_DOUBLE)
		{
			double [] pixeles = new double[cols*lins];
			double [] salida = new double[cols*lins];
		
		input.getDataElements(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;
			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = maxPad-(pos-(cols-1));
				for (int p=maxFil;p>maxFil-(pos-1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					double pixel = pixeles[i+(j-filtro.length/2)];
					//int pixint = pixel & 0xff;
					
					
					salida[i]+=hVar[j]*pixel;
					if (Math.abs(salida[i])>127)
					{
						//System.out.println("ENTRADA:"+pixel+" SALIDA: "+salida[i]);
					}
				}
				//System.out.println("ENTRADA:"+(pixeles[i] & 0xff));
			}

			//System.out.println("ENTRADA:"+(pixeles[i] & 0xff)+" SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}
		DataBufferDouble dbuffer = new DataBufferDouble(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		return null;
	}
	
	/**
	 * Convoluciona verticalmente usando un filtro unidimensional vertical
	 * @param entrada La imagen a convolucionar
	 * @param filtro El filtro utilizado (obtenido de la clase Filtros)
	 * @return La imagen convolucionada
	 */
	
	PlanarImage convVert(PlanarImage entrada, double[] filtro)
	{
//		 Degradamos con filtro las COLUMNAS
		Raster input = entrada.getData();
		int cols = entrada.getWidth();
		int lins = entrada.getHeight();
		int transfer = input.getTransferType();
		int maxFil=filtro.length;
		int maxPad=(int) Math.floor(filtro.length/2);
		if (transfer == DataBuffer.TYPE_BYTE)
		{
		byte[] pixeles = new byte[cols*lins];
		byte[] salida = new byte[cols*lins];	
		input.getDataElements(0,0,cols,lins,pixeles);
		double [] hVar = filtro.clone();

		
		for (int i = 0; i < cols*lins ;i++)
		{

			// Está en el borde superior
			if ((i >= 0) && (i <= (maxPad*cols-1)))
			{			
				int fil=(int) Math.floor(i/cols);
				fil = maxPad - fil;
				for (int f=0; f < fil; f++)
				{
					hVar[f]=0.0D;
				}
			}
			// Está en el borde inferior
			if ((i >= ((lins-maxPad)*cols)) && (i <= (lins*cols)-1))
			{
				int fil=(int) Math.floor(i/cols);
				fil++;
				fil = maxPad - (lins-fil);
				for (int f=maxFil-1; f > (maxFil-fil)-1; f--)
				{
					hVar[f]= 0.0D;
				}
			}

			salida[i]=0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					//System.out.println("i: "+i+" j: "+j);
					salida[i]+=hVar[j]*pixeles[i+(j-maxPad)*cols];
				}
				//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
			}
			
			//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}	
		
		//	Creamos la imagen de salida 2ª convol
		DataBufferByte dbuffer = new DataBufferByte(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_INT)
		{
		int[] pixeles = new int[cols*lins];
		int[] salida = new int[cols*lins];
		
		input.getPixels(0,0,cols,lins,pixeles);
		double [] hVar = filtro.clone();
		for (int i = 0; i < cols*lins ;i++)
		{

			// Está en el borde superior
			if ((i >= 0) && (i <= maxPad*(cols-1)))
			{
				int fil=i/cols;
				fil = maxPad - fil;
				for (int f=0; f < fil; f++)
				{
					hVar[f]=0.0D;
				}
			}
			// Está en el borde inferior
			if ((i >= (lins-maxPad)*cols) && (i <= (lins*cols)-1))
			{
				int fil=i/cols;
				fil = maxPad - (lins-fil);
				for (int f=maxFil; f > maxFil-(fil-1); f--)
				{
					hVar[f]= 0.0D;
				}
			}
			salida[i]=0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					salida[i]+=hVar[j]*pixeles[i+(j-filtro.length/2)*cols];
				}
				//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
			}
			
			//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}	
		
		//	Creamos la imagen de salida 2ª convol
		DataBufferInt dbuffer = new DataBufferInt(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_FLOAT)
		{
		float[] pixeles = new float[cols*lins];
		float[] salida = new float[cols*lins];
		//input.getPixels(0,0,cols,lins,pixeles);
		input.getDataElements(0,0,cols,lins,pixeles);
		double [] hVar = filtro.clone();
		for (int i = 0; i < cols*lins ;i++)
		{

			// Está en el borde superior
			if ((i >= 0) && (i <= maxPad*(cols-1)))
			{
				int fil=i/cols;
				fil = maxPad - fil;
				for (int f=0; f < fil; f++)
				{
					hVar[f]=0.0D;
				}
			}
			// Está en el borde inferior
			if ((i >= (lins-maxPad)*cols) && (i <= (lins*cols)-1))
			{
				int fil=i/cols;
				fil = maxPad - (lins-fil);
				for (int f=maxFil; f > maxFil-(fil-1); f--)
				{
					hVar[f]= 0.0D;
				}
			}
			salida[i]=0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					salida[i]+=hVar[j]*pixeles[i+(j-filtro.length/2)*cols];
				}
				//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
			}
			
			//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}	
		
		//	Creamos la imagen de salida 2ª convol
		DataBufferFloat dbuffer = new DataBufferFloat(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_DOUBLE)
		{
		double[] pixeles = new double[cols*lins];
		double[] salida = new double[cols*lins];
		//input.getPixels(0,0,cols,lins,pixeles);
		input.getDataElements(0,0,cols,lins,pixeles);
		double [] hVar = filtro.clone();
		for (int i = 0; i < cols*lins ;i++)
		{

			// Está en el borde superior
			if ((i >= 0) && (i <= maxPad*(cols-1)))
			{
				int fil=i/cols;
				fil = maxPad - fil;
				for (int f=0; f < fil; f++)
				{
					hVar[f]=0.0D;
				}
			}
			// Está en el borde inferior
			if ((i >= (lins-maxPad)*cols) && (i <= (lins*cols)-1))
			{
				int fil=i/cols;
				fil = maxPad - (lins-fil);
				for (int f=maxFil; f > maxFil-(fil-1); f--)
				{
					hVar[f]= 0.0D;
				}
			}
			salida[i]=0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					salida[i]+=hVar[j]*pixeles[i+(j-filtro.length/2)*cols];
				}
				//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
			}
			
			//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}	
		
		//	Creamos la imagen de salida 2ª convol
		DataBufferDouble dbuffer = new DataBufferDouble(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		return null;
	}

	/**
	 * Convoluciona horizontamente usando un filtro unidimensional horizontal. El algoritmo 
	 * fue obtenido de la documentación de MATLAB (<a href="http://www.mathworks.com">http://www.mathworks.com</a>)
	 * @param entrada La imagen a convolucionar
	 * @param filtro El filtro utilizado (Debe obtenerse de la clase Filtros)
	 * @return La imagen convolucionada
	 * 
	 */
	PlanarImage convHor2(PlanarImage entrada, double[] filtro)
	{
		//Este nuevo procedimiento de convolución emplea el método descrito en MATLAB
		//(Los filtros son simétricos con respecto al punto medio)
		Raster input = entrada.getData();
		int transfer = input.getTransferType();
		int cols = entrada.getWidth();
		int lins = entrada.getHeight();
		double[] hVar = filtro.clone();
		int maxFil=filtro.length;
		int maxPad=(int) Math.floor(filtro.length/2);

		

		
		// Tipo INT
		if (transfer == DataBuffer.TYPE_INT)
		{
			int [] pixeles = new int[cols*lins];
			int [] salida = new int[cols*lins];
		
		input.getPixels(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;
			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = maxPad-(pos-(cols-1));
				for (int p=maxFil;p>maxFil-(pos-1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < maxFil; j++)
			{// Recorre la ventana sobre pixeles
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					int pin = i-(maxPad-j); // Posición original en pixeles
					int pout = i-(maxPad-j); //Posición original en salida
					for (int k = j; k > -1; k--)
					{
						
						salida[pout]+=(int) (pixeles[pin]*filtro[k]); //Multiplicamos por el filtro
						/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
						 * igual hasta que avance j 
						 */
						pin++;
					}
					
				}
			} /* Fin for \/ elemento del filtro */
			hVar=filtro.clone();
		} /* Fin for \/ pixel */
		DataBufferInt dbuffer = new DataBufferInt(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		} /* Fin tipo INT */
		if (transfer == DataBuffer.TYPE_BYTE)
		{
			byte [] pixeles = new byte[cols*lins];
			byte [] salida = new byte[cols*lins];
		
		input.getDataElements(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;

			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = pos-(cols-1)+maxPad;
				for (int p=maxFil-1;p>maxFil-(pos+1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0.0D)
				{
					salida[i]+=0;						     
				}
				else
				{
					int pin = i-(maxPad-j); // Posición original en pixeles
					int pout = i-(maxPad-j); //Posición original en salida
					for (int k = j; k > -1; k--)
					{
						//System.out.println("Pin: "+pin+" Pout: "+pout+" i: "+i);
						if (hVar[k]==0.0D)
						{
							salida[pout]+=0;
						}
						else
						{
							if(pin<(cols*lins-1))
							{
								salida[pout]+=(byte) (pixeles[pin]*filtro[k]);
							}
						} //Multiplicamos por el filtro
						/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
						 * igual hasta que avance j 
						 */
						pin++;
						
					}
				}
				//System.out.println("ENTRADA:"+(pixeles[i] & 0xff));
			}

			//System.out.println("ENTRADA:"+(pixeles[i] & 0xff)+" SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}
		DataBufferByte dbuffer = new DataBufferByte(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_FLOAT)
		{
			float [] pixeles = new float[cols*lins];
			float [] salida = new float[cols*lins];
		
		input.getDataElements(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;
			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = maxPad-(pos-(cols-1));
				for (int p=maxFil;p>maxFil-(pos-1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					int pin = i-(maxPad-j); // Posición original en pixeles
					int pout = i-(maxPad-j); //Posición original en salida
					for (int k = j; k > -1; k--)
					{
						
						salida[pout]+=(float) (pixeles[pin]*filtro[k]); //Multiplicamos por el filtro
						/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
						 * igual hasta que avance j 
						 */
						pin++;
					}
				}
				//System.out.println("ENTRADA:"+(pixeles[i] & 0xff));
			}

			//System.out.println("ENTRADA:"+(pixeles[i] & 0xff)+" SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}
		DataBufferFloat dbuffer = new DataBufferFloat(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		if (transfer == DataBuffer.TYPE_DOUBLE)
		{
			double [] pixeles = new double[cols*lins];
			double [] salida = new double[cols*lins];
		
		input.getDataElements(0,0,cols,lins,pixeles);
		for (int i = 0; i < cols*lins ;i++)
		{
			int pos = i % cols;
			// Está en el borde izquierdo
			if (pos < maxPad)
			{
				pos = maxPad-pos;
				
				for (int p=0;p<pos;p++)
				{
					hVar[p]=0.0D;
				}

			}
			// Está en el borde derecho
			if (pos > ((cols-1)-maxPad))
			{
				pos = maxPad-(pos-(cols-1));
				for (int p=maxFil;p>maxFil-(pos-1);p--)
				{
					hVar[p]=0.0D;
				}	
			}
			salida[i]= 0;
			for (int j = 0;j < filtro.length; j++)
			{
				
				if (hVar[j]==0)
				{
					salida[i]+=0;						     
				}
				else
				{
					int pin = i-(maxPad-j); // Posición original en pixeles
					int pout = i-(maxPad-j); //Posición original en salida
					for (int k = j; k > -1; k--)
					{
						
						salida[pout]+= (pixeles[pin]*filtro[k]); //Multiplicamos por el filtro
						/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
						 * igual hasta que avance j 
						 */
						pin++;
					}
				}
				//System.out.println("ENTRADA:"+(pixeles[i] & 0xff));
			}

			//System.out.println("ENTRADA:"+(pixeles[i] & 0xff)+" SALIDA: "+salida[i]);
			hVar = filtro.clone();
		}
		DataBufferDouble dbuffer = new DataBufferDouble(salida,cols*lins);

		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		}
		return null;
	}

	/**
	 * Convoluciona verticalmente usando un filtro unidimensional vertical. El algoritmo 
	 * fue obtenido de la documentación de MATLAB (<a href="http://www.mathworks.com">http://www.mathworks.com</a>)
	 * 
	 * @param entrada La imagen a convolucionar
	 * @param filtro El filtro utilizado (obtenido de la clase Filtros)
	 * @return La imagen convolucionada
	 */
	PlanarImage convVert2(PlanarImage entrada, double[] filtro)
	{
		 //Degradamos con filtro las COLUMNAS
			Raster input = entrada.getData();
			int cols = entrada.getWidth();
			int lins = entrada.getHeight();
			int transfer = input.getTransferType();
			int maxFil=filtro.length;
			int maxPad=(int) Math.floor(filtro.length/2);
			if (transfer == DataBuffer.TYPE_BYTE)
			{
			byte[] pixeles = new byte[cols*lins];
			byte[] salida = new byte[cols*lins];	
			input.getDataElements(0,0,cols,lins,pixeles);
			double [] hVar = filtro.clone();

			
			for (int i = 0; i < cols*lins ;i++)
			{

				// Está en el borde superior
				if ((i >= 0) && (i <= (maxPad*cols-1)))
				{			
					int fil=(int) Math.floor(i/cols);
					fil = maxPad - fil;
					for (int f=0; f < fil; f++)
					{
						hVar[f]=0.0D;
					}
				}
				// Está en el borde inferior
				if ((i >= ((lins-maxPad)*cols)) && (i <= (lins*cols)-1))
				{
					int fil=(int) Math.floor(i/cols);
					fil++;
					fil = maxPad - (lins-fil);
					for (int f=maxFil-1; f > (maxFil-fil)-1; f--)
					{
						hVar[f]= 0.0D;
					}
				}

				salida[i]=0;
				for (int j = 0;j < filtro.length; j++)
				{
					
					if (hVar[j]==0)
					{
						salida[i]+=0;						     
					}
					else
					{
						//System.out.println("i: "+i+" j: "+j);
						int pin = i-(maxPad-j)*cols; // Posición original en pixeles
						int pout = i-(maxPad-j)*cols; //Posición original en salida
						for (int k = j; k > -1; k--)
						{
							//System.out.println("Pin: "+pin+" Pout: "+pout+" i: "+i);
							if (hVar[k]==0.0D)
							{
								salida[pout]+=0;
							}
							else
							{
								if (pin < (cols*lins-1))
								{
									salida[pout]+=(byte) (pixeles[pin]*filtro[k]);
								}
							}//Multiplicamos por el filtro
							/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
							 * igual hasta que avance j 
							 */
							pin+=cols;
						}
						//salida[i]+=hVar[j]*pixeles[i+(j-maxPad)*cols];
					}
					//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
				}
				
				//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
				hVar = filtro.clone();
			}	
			
			//	Creamos la imagen de salida 2ª convol
			DataBufferByte dbuffer = new DataBufferByte(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return tiled;
			}
			if (transfer == DataBuffer.TYPE_INT)
			{
			int[] pixeles = new int[cols*lins];
			int[] salida = new int[cols*lins];
			
			input.getPixels(0,0,cols,lins,pixeles);
			double [] hVar = filtro.clone();
			for (int i = 0; i < cols*lins ;i++)
			{

				// Está en el borde superior
				if ((i >= 0) && (i <= maxPad*(cols-1)))
				{
					int fil=i/cols;
					fil = maxPad - fil;
					for (int f=0; f < fil; f++)
					{
						hVar[f]=0.0D;
					}
				}
				// Está en el borde inferior
				if ((i >= (lins-maxPad)*cols) && (i <= (lins*cols)-1))
				{
					int fil=i/cols;
					fil = maxPad - (lins-fil);
					for (int f=maxFil; f > maxFil-(fil-1); f--)
					{
						hVar[f]= 0.0D;
					}
				}
				salida[i]=0;
				for (int j = 0;j < filtro.length; j++)
				{
					
					if (hVar[j]==0)
					{
						salida[i]+=0;						     
					}
					else
					{
						int pin = i-(maxPad-j)*cols; // Posición original en pixeles
						int pout = i-(maxPad-j)*cols; //Posición original en salida
						for (int k = j; k > -1; k--)
						{
							
							salida[pout]+=(int) (pixeles[pin]*filtro[k]); //Multiplicamos por el filtro
							/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
							 * igual hasta que avance j 
							 */
							pin+=cols;
						}
					}
					//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
				}
				
				//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
				hVar = filtro.clone();
			}	
			
			//	Creamos la imagen de salida 2ª convol
			DataBufferInt dbuffer = new DataBufferInt(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return tiled;
			}
			if (transfer == DataBuffer.TYPE_FLOAT)
			{
			float[] pixeles = new float[cols*lins];
			float[] salida = new float[cols*lins];
			//input.getPixels(0,0,cols,lins,pixeles);
			input.getDataElements(0,0,cols,lins,pixeles);
			double [] hVar = filtro.clone();
			for (int i = 0; i < cols*lins ;i++)
			{

				// Está en el borde superior
				if ((i >= 0) && (i <= maxPad*(cols-1)))
				{
					int fil=i/cols;
					fil = maxPad - fil;
					for (int f=0; f < fil; f++)
					{
						hVar[f]=0.0D;
					}
				}
				// Está en el borde inferior
				if ((i >= (lins-maxPad)*cols) && (i <= (lins*cols)-1))
				{
					int fil=i/cols;
					fil = maxPad - (lins-fil);
					for (int f=maxFil; f > maxFil-(fil-1); f--)
					{
						hVar[f]= 0.0D;
					}
				}
				salida[i]=0;
				for (int j = 0;j < filtro.length; j++)
				{
					
					if (hVar[j]==0)
					{
						salida[i]+=0;						     
					}
					else
					{
						int pin = i-(maxPad-j)*cols; // Posición original en pixeles
						int pout = i-(maxPad-j)*cols; //Posición original en salida
						for (int k = j; k > -1; k--)
						{
							
							salida[pout]+=(float) (pixeles[pin]*filtro[k]); //Multiplicamos por el filtro
							/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
							 * igual hasta que avance j 
							 */
							pin+=cols;
						}
					}
					//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
				}
				
				//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
				hVar = filtro.clone();
			}	
			
			//	Creamos la imagen de salida 2ª convol
			DataBufferFloat dbuffer = new DataBufferFloat(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return tiled;
			}
			if (transfer == DataBuffer.TYPE_DOUBLE)
			{
			double[] pixeles = new double[cols*lins];
			double[] salida = new double[cols*lins];
			//input.getPixels(0,0,cols,lins,pixeles);
			input.getDataElements(0,0,cols,lins,pixeles);
			double [] hVar = filtro.clone();
			for (int i = 0; i < cols*lins ;i++)
			{

				// Está en el borde superior
				if ((i >= 0) && (i <= maxPad*(cols-1)))
				{
					int fil=i/cols;
					fil = maxPad - fil;
					for (int f=0; f < fil; f++)
					{
						hVar[f]=0.0D;
					}
				}
				// Está en el borde inferior
				if ((i >= (lins-maxPad)*cols) && (i <= (lins*cols)-1))
				{
					int fil=i/cols;
					fil = maxPad - (lins-fil);
					for (int f=maxFil; f > maxFil-(fil-1); f--)
					{
						hVar[f]= 0.0D;
					}
				}
				salida[i]=0;
				for (int j = 0;j < filtro.length; j++)
				{
					
					if (hVar[j]==0)
					{
						salida[i]+=0;						     
					}
					else
					{
						int pin = i-(maxPad-j)*cols; // Posición original en pixeles
						int pout = i-(maxPad-j)*cols; //Posición original en salida
						for (int k = j; k > -1; k--)
						{
							
							salida[pout]+= (pixeles[pin]*filtro[k]); //Multiplicamos por el filtro
							/* Voy avanzando en la entrada, retrocediendo en el filtro y la salida permanece
							 * igual hasta que avance j 
							 */
							pin+=cols;
						}
					}
					//System.out.println("I: "+i+" ENTRADA:"+pixeles[i]+" SALIDA: "+salida[i]+ "FILTRO: "+hVar[j]);
				}
				
				//System.out.println("ENTRADA: "+pixeles[i]+"SALIDA: "+salida[i]);
				hVar = filtro.clone();
			}	
			
			//	Creamos la imagen de salida 2ª convol
			DataBufferDouble dbuffer = new DataBufferDouble(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return tiled;
			}
		return null;
	}
}
