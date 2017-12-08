/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first.algorithms;

import es.upm.fi.gtd.first.GeoImg;
import es.upm.fi.gtd.first.ParseXML;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.List;
import java.util.Locale;
import javax.media.jai.BorderExtender;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;

/**
 *
 * @author Alvar
 */
public class FFIHS {


    public static GeoImg fusionFIHS(List<GeoImg> pan, 
            List<GeoImg> multi, Locale loc)
    {
        ParseXML ic = new ParseXML(loc.getLanguage()+"_"+loc.getCountry()+".xml");
        // Variables referidas a la imagen final
		int width = pan.get(0).getImage().getWidth();		// Ancho final es el de la pancromatica
		int height = pan.get(0).getImage().getHeight();	// Altura final es la de la pancromatica
		int npixels = width*height;     	// numero de pixeles
		int nbands = multi.size(); 	// numero de bandas
		//double minv = panip.getMin();		// Minimo valor de pixel
		//double maxv = panip.getMin();		// Maximo valor de pixel
		
                GeoImg resultado = null;
                PlanarImage [] multiEsc = new PlanarImage[nbands];
		// Redimensiona las bandas de la multi para que tengan el tamaño de la pan
		// Calcular el factor para cambiar de tamaño la imagen
                float factorEscalaX = width/(multi.get(0).getImage().getWidth());
                System.out.println("Factor X: "+factorEscalaX+"width: "+width);
                
                float factorEscalaY = height/(multi.get(0).getImage().getHeight());
                System.out.println("Factor Y: "+factorEscalaY+"height: "+height);
                RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
                        BorderExtender.createInstance(BorderExtender.BORDER_COPY));
                ParameterBlock pb = null;

                
		//ip.setInterpolate(true);
		for (int i=0;i<nbands;i++) {
                    pb = new ParameterBlock();
                    pb.addSource(multi.get(i).getImage());
                    pb.add(factorEscalaX);
                    pb.add(factorEscalaY);
                    pb.add(0.0F);
                    pb.add(0.0F);
                    // Qué interpolación se usa en esta fusión?
                    pb.add(new InterpolationNearest());
                    multiEsc[i] = JAI.create("scale", pb, rh);
                    //multi.get(i).getImage().dispose();
                            
                }
		//System.gc();
		// Crea la matriz en la que se almacenaran las imagenes (una por columna)
		

            // Crea variables utilizadas para la transformada
		double[] r = new double[npixels];
		double[] g = new double[npixels];
		double[] b = new double[npixels];
		double[] ir = new double[npixels];
		double nr = 0.0, ng = 0.0 , nb = 0.0 , nir, np = 0.0;

		ParameterBlock pbMaxMin = new ParameterBlock();

    pbMaxMin.addSource(pan.get(0).getImage());
	RenderedOp extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    double[] allMins = (double[])extrema.getProperty("minimum");
    double[] allMaxs = (double[])extrema.getProperty("maximum");
    double smin = allMins[0];
    System.out.println("Val min pan: "+smin);
    double smax = allMaxs[0];
    System.out.println("Val max pan: "+smax);
    for(int v=1;v<allMins.length;v++)
      {
        if (allMins[v] < smin) smin = allMins[v];
        if (allMaxs[v] > smax) smax = allMaxs[v];
      }

    // Obtenemos los Raster para no hacer tantas llamadas a GetData()
    Raster rojo = multiEsc[2].getData();
    Raster verde = multiEsc[1].getData();
    Raster azul = multiEsc[0].getData();
    Raster rPan = pan.get(0).getImage().getData();
      
		// Fusion mediante FIHS
		for (int y=0, k=0; y<height; y++)
		{
			for(int x=0; x<width; x++,k++)
                        {
                            nr = rojo.getSampleDouble(x,y,0);
                            ng = verde.getSampleDouble(x,y,0);
                            nb = azul.getSampleDouble(x,y,0);
                            np = rPan.getSampleDouble(x,y,0);
                            

				/* Fast IHS */
                                double []punto;
				punto = FIHS.FusionFIHS(nr, ng, nb, np);
                                
                                /*
                                if (x==45 && y==189) {
                                    IJ.showMessage("r:"+nr+" g:"+ng+" b:"+nb);
                                    IJ.showMessage("r:"+punto[0]+" g:"+punto[1]+" b:"+punto[2]);
                                }
                                 */
                                
				//r[k] = punto[0]>0 ? (punto[0]<smax?punto[0]:smax) : 0;
				//g[k] = punto[1]>0 ? (punto[1]<smax?punto[1]:smax) : 0;
				//b[k] = punto[2]>0 ? (punto[2]<smax?punto[2]:smax) : 0;

                                r[k] = punto[0]>0 ? (punto[0]<255?punto[0]:255) : 0;
				g[k] = punto[1]>0 ? (punto[1]<255?punto[1]:255) : 0;
				b[k] = punto[2]>0 ? (punto[2]<255?punto[2]:255) : 0;

			}
			// Actualiza la barra de progreso
			
		
		
                }
		


		// Creamos la pila con las bandas finales

		switch (pan.get(0).getImage().getData().getTransferType())
		{
		case DataBuffer.TYPE_BYTE: // En caso de ser una imagen de 8 bits
                {
			byte[] puntosh_b = new byte[npixels];
			byte[] puntoss_b = new byte[npixels];
			byte[] puntosi_b = new byte[npixels];
			
			for (int k=0; k<npixels; k++) {
				puntosh_b[k] = (byte) r[k];
				puntoss_b[k] = (byte) g[k];
				puntosi_b[k] = (byte) b[k];
			}
                    DataBufferByte dbuffer = new DataBufferByte(puntosh_b, puntosh_b.length);
                    SampleModel samplemod = RasterFactory.createBandedSampleModel(
                            DataBuffer.TYPE_BYTE, width, height, 1);
                    ColorModel colormod = PlanarImage.createColorModel(samplemod);
                    Raster raster = RasterFactory.createWritableRaster(samplemod,
                            dbuffer, new Point(0, 0));
                    TiledImage tiledh = new TiledImage(0, 0, width, height, 0, 0,
                            samplemod, colormod);
                    tiledh.setData(raster);
                    
                    dbuffer = new DataBufferByte(puntoss_b, puntoss_b.length);
                    samplemod = RasterFactory.createBandedSampleModel(
                            DataBuffer.TYPE_BYTE, width, height, 1);
                    colormod = PlanarImage.createColorModel(samplemod);
                    raster = RasterFactory.createWritableRaster(samplemod,
                            dbuffer, new Point(0, 0));
                    TiledImage tileds = new TiledImage(0, 0, width, height, 0, 0,
                            samplemod, colormod);
                    tileds.setData(raster);
                    
                    dbuffer = new DataBufferByte(puntosi_b, puntosi_b.length);
                    samplemod = RasterFactory.createBandedSampleModel(
                            DataBuffer.TYPE_BYTE, width, height, 1);
                    colormod = PlanarImage.createColorModel(samplemod);
                    raster = RasterFactory.createWritableRaster(samplemod,
                            dbuffer, new Point(0, 0));
                    TiledImage tiledi = new TiledImage(0, 0, width, height, 0, 0,
                            samplemod, colormod);
                    tiledi.setData(raster);
        
			pb = new ParameterBlock();
		pb.addSource(tiledh);
		pb.addSource(tileds);
		pb.addSource(tiledi);

		resultado = new GeoImg(
                            pan.get(0).getData(), 
                            JAI.create("bandmerge", pb), 
                            new File(ic.buscar(163)
                                +pan.get(0).getFile().getName()
                                +"&"
                                +multi.get(0).getFile().getName()),
                            pan.get(0).getStreamMetadata(), 
                            pan.get(0).getImageMetadata()); // Tag 163
			break;
                }
		          case DataBuffer.TYPE_USHORT: // En caso de ser una imagen de 16 bits
                {
                short[] puntosh_s = new short[npixels];
                short[] puntoss_s = new short[npixels];
                short[] puntosi_s = new short[npixels];

                for (int k = 0; k < npixels; k++) {
                    puntosh_s[k] = (short) r[k];
                    puntoss_s[k] = (short) g[k];
                    puntosi_s[k] = (short) b[k];
                }

                DataBufferUShort dbuffers = new DataBufferUShort(puntosh_s, puntosh_s.length);
                SampleModel samplemod = RasterFactory.createBandedSampleModel(
                        DataBuffer.TYPE_USHORT, width, height, 1);
                ColorModel colormod = PlanarImage.createColorModel(samplemod);
                Raster raster = RasterFactory.createWritableRaster(samplemod,
                        dbuffers, new Point(0, 0));
                TiledImage tiledhs = new TiledImage(0, 0, width, height, 0, 0,
                        samplemod, colormod);
                tiledhs.setData(raster);

                dbuffers = new DataBufferUShort(puntoss_s, puntoss_s.length);
                samplemod = RasterFactory.createBandedSampleModel(
                        DataBuffer.TYPE_USHORT, width, height, 1);
                colormod = PlanarImage.createColorModel(samplemod);
                raster = RasterFactory.createWritableRaster(samplemod,
                        dbuffers, new Point(0, 0));
                TiledImage tiledss = new TiledImage(0, 0, width, height, 0, 0,
                        samplemod, colormod);
                tiledss.setData(raster);

                dbuffers = new DataBufferUShort(puntosi_s, puntosi_s.length);
                samplemod = RasterFactory.createBandedSampleModel(
                        DataBuffer.TYPE_USHORT, width, height, 1);
                colormod = PlanarImage.createColorModel(samplemod);
                raster = RasterFactory.createWritableRaster(samplemod,
                        dbuffers, new Point(0, 0));
                TiledImage tiledis = new TiledImage(0, 0, width, height, 0, 0,
                        samplemod, colormod);
                tiledis.setData(raster);

                pb = new ParameterBlock();
                pb.addSource(tiledhs);
                pb.addSource(tiledss);
                pb.addSource(tiledis);

                resultado = new GeoImg(
                        pan.get(0).getData(),
                        JAI.create("bandmerge", pb),
                        new File(
                        ic.buscar(163) + pan.get(0).getFile().getName() + "&" + multi.get(0).getFile().getName()),
                        pan.get(0).getStreamMetadata(),
                        pan.get(0).getImageMetadata()); // Tag 163
                break;
                }
		case DataBuffer.TYPE_INT: // En caso de ser una imagen de 32 bits
                {
                 int[] puntosh_i = new int[npixels];
                int[] puntoss_i = new int[npixels];
                int[] puntosi_i = new int[npixels];

                for (int k = 0; k < npixels; k++) {
                    puntosh_i[k] = (int) r[k];
                    puntoss_i[k] = (int) g[k];
                    puntosi_i[k] = (int) b[k];
                }

                DataBufferInt dbuffers = new DataBufferInt(puntosh_i, puntosh_i.length);
                SampleModel samplemod = RasterFactory.createBandedSampleModel(
                        DataBuffer.TYPE_USHORT, width, height, 1);
                ColorModel colormod = PlanarImage.createColorModel(samplemod);
                Raster raster = RasterFactory.createWritableRaster(samplemod,
                        dbuffers, new Point(0, 0));
                TiledImage tiledhs = new TiledImage(0, 0, width, height, 0, 0,
                        samplemod, colormod);
                tiledhs.setData(raster);

                dbuffers = new DataBufferInt(puntoss_i, puntoss_i.length);
                samplemod = RasterFactory.createBandedSampleModel(
                        DataBuffer.TYPE_USHORT, width, height, 1);
                colormod = PlanarImage.createColorModel(samplemod);
                raster = RasterFactory.createWritableRaster(samplemod,
                        dbuffers, new Point(0, 0));
                TiledImage tiledss = new TiledImage(0, 0, width, height, 0, 0,
                        samplemod, colormod);
                tiledss.setData(raster);

                dbuffers = new DataBufferInt(puntosi_i, puntosi_i.length);
                samplemod = RasterFactory.createBandedSampleModel(
                        DataBuffer.TYPE_USHORT, width, height, 1);
                colormod = PlanarImage.createColorModel(samplemod);
                raster = RasterFactory.createWritableRaster(samplemod,
                        dbuffers, new Point(0, 0));
                TiledImage tiledis = new TiledImage(0, 0, width, height, 0, 0,
                        samplemod, colormod);
                tiledis.setData(raster);

                pb = new ParameterBlock();
                pb.addSource(tiledhs);
                pb.addSource(tiledss);
                pb.addSource(tiledis);

                resultado = new GeoImg(
                        pan.get(0).getData(),
                        JAI.create("bandmerge", pb),
                        new File(
                        ic.buscar(163) + pan.get(0).getFile().getName() + "&" + multi.get(0).getFile().getName()),
                        pan.get(0).getStreamMetadata(),
                        pan.get(0).getImageMetadata()); // Tag 163
			
			
			break;
		}
                }/* end switch */
                r = null;
                g = null;
                b = null;
		
		return resultado;
    }
            

}
