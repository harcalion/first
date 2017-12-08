package es.upm.fi.gtd.first;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.Enumeration;

import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import com.sun.media.jai.codecimpl.util.DataBufferDouble;

/**
 *Componente que se añade a un JFileChooser que permite elegir la profundidad
 *al guardar un fichero de imagen
 */
public class ElegirProfundidad extends JComponent
	{
    /**
     * JRadioButton con las distintas profundidades de color
     */
		private JRadioButton ochoBPP,dieciBPP, veintiBPP, treintaBPP, cuarentaBPP, sesentaBPP;
                /**
                 * Título del componente
                 */
		JLabel titulo;
                /**
                 * 8 bits de profundidad
                 */
		public static final int OCHO = 8;
                /**
                 * 16 bits de profundidad
                 */
		public static final int DIECI = 16;
                /**
                 * 24 bits de profundidad. 3 bandas de 8 bits/banda
                 */
		public static final int VEINTI = 24;
                /**
                 * 32 bits de profundidad
                 */
		public static final int TREINTA = 32;
                /**
                 * 48 bits de profundidad. 3 bandas de 16 bits/banda
                 */
		public static final int CUARENTA = 48;
                /**
                 * 64 bits de profundidad (Imagen de double?)
                 */
		public static final int SESENTA = 64;
                /**
                 * Agrupación para los JRadioButton con las profundidades
                 */
		private ButtonGroup bg;
		/**
                 * JFrame principal de la aplicación
                 */
		private JFrame t;
		
		/**
		 * Versión para serialización
		 */
		private static final long serialVersionUID = 1L;
    /**
     * Constructor de la clase
     * @param t JFrame principal de la aplicación
     */
		ElegirProfundidad (JFrame t, ParseXML ic)
		{
                     
			this.t = t;
			this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
			ochoBPP = new JRadioButton(ic.buscar(140)); // Tag 140
			ochoBPP.setMnemonic(OCHO);			
			dieciBPP = new JRadioButton(ic.buscar(141)); // Tag 141
			dieciBPP.setMnemonic(DIECI);
			veintiBPP = new JRadioButton(ic.buscar(142)); // Tag 142
			veintiBPP.setMnemonic(VEINTI);
			treintaBPP = new JRadioButton(ic.buscar(143)); // Tag 143
			treintaBPP.setMnemonic(TREINTA);
			cuarentaBPP = new JRadioButton(ic.buscar(144)); // Tag 144
			cuarentaBPP.setMnemonic(CUARENTA);
			sesentaBPP = new JRadioButton(ic.buscar(145)); // Tag 145
			sesentaBPP.setMnemonic(SESENTA);
			bg = new ButtonGroup();
			bg.add(ochoBPP);
			bg.add(dieciBPP);
			bg.add(veintiBPP);
			bg.add(treintaBPP);
			bg.add(cuarentaBPP);
			bg.add(sesentaBPP);
			titulo = new JLabel(ic.buscar(146)); // Tag 146
			
			
			// 32 bits es la predeterminada
			treintaBPP.setSelected(true);
			this.add(titulo);
			this.add(ochoBPP);
			this.add(dieciBPP);
			this.add(veintiBPP);
			this.add(treintaBPP);
			this.add(cuarentaBPP);
			this.add(sesentaBPP);
			this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			
			
		}
    /**
     * Obtiene la profundidad marcada en el componente
     * @return Un entero que especifica la profundidad
     */
		int getMarcada ()
		{

			Enumeration<AbstractButton> en =   bg.getElements();
			int mnem = -1;
			while(en.hasMoreElements())
			{
				AbstractButton but = en.nextElement();
				if (but.isSelected())
				{
					mnem = but.getMnemonic();
				}
			}
			
			switch(mnem)
			{
			case OCHO: return OCHO;
			case DIECI: return DIECI;
			case VEINTI: return VEINTI;
			case TREINTA: return TREINTA;
			case CUARENTA: return CUARENTA;
			case SESENTA: return SESENTA;
			default: return -1;
			}
			
		}
    /**
     * Reduce la profundidad de píxeles de la imagen para guardarla a la 
     * indicada
     * @param ipixel_o Los valores (double) de pixel originales 
     * @param profundidad La profundidad indicada
     * @param anchura Anchura de la imagen
     * @param altura Altura de la imagen
     * @return Imagen reducida
     */
		public static TiledImage crearProfundidadReducida(double[] ipixel_o,int profundidad,int anchura, int altura)
		{
			TiledImage tiled = null;
			if (profundidad == DIECI)
			{// Tengo que pasarlo a 16 bits
				short [] pixeles16 = new short[ipixel_o.length];

				for (int i = 0; i < ipixel_o.length; i ++)
				{

					if (ipixel_o[i]>Short.MAX_VALUE)
					{
						pixeles16[i] = Short.MAX_VALUE;
					}
					else
					{
						pixeles16[i] = (short) ipixel_o [i];
					}
				}
				DataBufferUShort dbuffer = new DataBufferUShort(pixeles16,pixeles16.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_USHORT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			else if (profundidad == OCHO)
			{// Tengo que pasarlo a 8 bits
				byte [] pixeles8 = new byte[ipixel_o.length];

				for (int i = 0; i < ipixel_o.length; i ++)
				{
					if (ipixel_o[i]>Byte.MAX_VALUE)
					{
						pixeles8[i] = Byte.MAX_VALUE;
					}
					else
					{
						pixeles8[i] = (byte) ipixel_o [i];
					}
				}
				DataBufferByte dbuffer = new DataBufferByte(pixeles8,pixeles8.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
				
			}
			else if (profundidad == TREINTA)
			{
				// Tengo que pasarlo a entero 32 bits
				int [] pixeles32 = new int[ipixel_o.length];

				for (int i = 0; i < ipixel_o.length; i ++)
				{
					if (ipixel_o[i]>Integer.MAX_VALUE)
					{
						pixeles32[i] = Integer.MAX_VALUE;
					}
					else
					{
						System.out.println(ipixel_o[i]);
						pixeles32[i] = (int) ipixel_o [i];
					}
				}
				
				DataBufferInt dbuffer = new DataBufferInt(pixeles32,pixeles32.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			else
			{
				DataBufferDouble dbuffer = new DataBufferDouble(ipixel_o,ipixel_o.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
				
			}
			return tiled;
		}
		
    /**
     * Calcula el máximo y el mínimo de un array de int
     * @param lista Array de enteros
     * @return Array con max en 0 y mín en 1
     */
		public static int [] extremos (int[] lista)
		{
			int max =0 ,min = 0;
			for (int i:lista)
			{
				if (i > max){
					max = i;
				}
				if (i < min){
					min = i;
				}
			}
			int [] result = {max,min};
			return result;
		}
		    /**
     * Reduce la profundidad de píxeles de la imagen para guardarla a la 
     * indicada
     * @param ipixel_o Los valores (int) de pixel originales 
     * @param profundidad La profundidad indicada
     * @param anchura Anchura de la imagen
     * @param altura Altura de la imagen
     * @return Imagen reducida
     */
		public static TiledImage crearProfundidadReducida(int[] ipixel_o,int profundidad,int anchura, int altura)
		{
			TiledImage tiled = null;
			if (profundidad == DIECI)
			{// Tengo que pasarlo a 16 bits
				short [] pixeles16 = new short[ipixel_o.length];
				int [] ext = extremos (ipixel_o);
				int max = ext[0];
				int min = ext[1];
				
				for (int i = 0; i < ipixel_o.length; i ++)
				{

					short temp = (short) (ipixel_o[i]-min);
					//pixeles16[i] = (short) ((temp*Short.MAX_VALUE)/(max-min));
                                        pixeles16[i] = (short) ((temp*max)/(max-min));
				}
				DataBufferUShort dbuffer = new DataBufferUShort(pixeles16,pixeles16.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_USHORT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			else if (profundidad == OCHO)
			{// Tengo que pasarlo a 8 bits
				byte [] pixeles8 = new byte[ipixel_o.length];
				int [] ext = extremos (ipixel_o);
				int max = ext[0];
				int min = ext[1];
				
				for (int i=0; i<ipixel_o.length; i++) {
					int temp = (short) (ipixel_o[i]-min);
					pixeles8[i] = (byte) ((temp*255)/(max-min));
					//System.out.println("pixel:"+pixels32[i]);
				}
				
				DataBufferByte dbuffer = new DataBufferByte(pixeles8,pixeles8.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
				
			}
			else
			{
				
				DataBufferInt dbuffer = new DataBufferInt(ipixel_o,ipixel_o.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			return tiled;
		}
		    /**
     * Reduce la profundidad de píxeles de la imagen para guardarla a la 
     * indicada
     * @param ipixel_o Los valores (short) de pixel originales 
     * @param profundidad La profundidad indicada
     * @param anchura Anchura de la imagen
     * @param altura Altura de la imagen
     * @return Imagen reducida
     */
		public static TiledImage crearProfundidadReducida(short[] ipixel_o, int profundidad, int anchura, int altura) {
			TiledImage tiled = null;
			if (profundidad == OCHO)
			{// Tengo que pasarlo a 8 bits
				byte [] pixeles8 = new byte[ipixel_o.length];

				for (int i = 0; i < ipixel_o.length; i ++)
				{
					if (ipixel_o[i]>Byte.MAX_VALUE)
					{
						pixeles8[i] = Byte.MAX_VALUE;
					}
					else
					{
						pixeles8[i] = (byte) ipixel_o [i];
					}
				}
				DataBufferByte dbuffer = new DataBufferByte(pixeles8,pixeles8.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
				
			}
			else if (profundidad == DIECI)
			{
				
				DataBufferUShort dbuffer = new DataBufferUShort(ipixel_o,ipixel_o.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_USHORT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			else
			{
				int [] pixeles32 = new int[ipixel_o.length];

				for (int i = 0; i < ipixel_o.length; i ++)
				{
						pixeles32[i] = ipixel_o [i];
				}
				DataBufferInt dbuffer = new DataBufferInt(pixeles32,pixeles32.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			return tiled;
			
		}
		    /**
     * Reduce la profundidad de píxeles de la imagen para guardarla a la 
     * indicada
     * @param ipixel_o Los valores (byte) de pixel originales 
     * @param profundidad La profundidad indicada
     * @param anchura Anchura de la imagen
     * @param altura Altura de la imagen
     * @return Imagen reducida
     */
		public static TiledImage crearProfundidadReducida(byte[] ipixel_o, int profundidad, int anchura, int altura) {
			TiledImage tiled = null;
			if (profundidad == DIECI)
			{// Tengo que pasarlo a 16 bits
				short [] pixeles16 = new short[ipixel_o.length];

				for (int i = 0; i < ipixel_o.length; i ++)
				{
					pixeles16[i] = (short) ipixel_o[i];
				}
				DataBufferUShort dbuffer = new DataBufferUShort(pixeles16,pixeles16.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_USHORT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
				
			}
			else if (profundidad == TREINTA)
			{// Tengo que pasarlo a 32
				int [] pixeles32 = new int[ipixel_o.length];

				for (int i = 0; i < ipixel_o.length; i ++)
				{
						pixeles32[i] = ipixel_o [i];
				}
				DataBufferInt dbuffer = new DataBufferInt(pixeles32,pixeles32.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			else
			{
				
				DataBufferByte dbuffer = new DataBufferByte(ipixel_o,ipixel_o.length);
				SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, anchura, altura, 1);
				ColorModel colormod =  PlanarImage.createColorModel(samplemod);
				Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
				tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
				tiled.setData(raster);
			}
			return tiled;
		}

    static RenderedImage crearProfundidadReducida(RenderedImage renderedImage, int profundidad, int anchura, int altura) {
        Raster ras = renderedImage.getData();
       
        int type = ras.getTransferType();
        switch (type)
        {
            case DataBuffer.TYPE_BYTE:
            {
                byte [] pixeles = new byte[anchura*altura];
                ras.getDataElements(0,0,anchura, altura, pixeles);
                return crearProfundidadReducida(pixeles,profundidad, anchura, altura);
            }
            case DataBuffer.TYPE_USHORT:
            {
                short [] pixeles = new short[anchura*altura];
                ras.getDataElements(0,0,anchura, altura, pixeles);
                return crearProfundidadReducida(pixeles,profundidad, anchura, altura);
            }
            case DataBuffer.TYPE_INT:
            {
                int [] pixeles = new int[anchura*altura];
                ras.getDataElements(0,0,anchura, altura, pixeles);
                return crearProfundidadReducida(pixeles,profundidad, anchura, altura);
            }
            case DataBuffer.TYPE_DOUBLE:
            {
                double [] pixeles = new double[anchura*altura];
                ras.getDataElements(0,0,anchura, altura, pixeles);
                return crearProfundidadReducida(pixeles,profundidad, anchura, altura);
            }
            default: return null;
            
        }
    }

		
	
	}