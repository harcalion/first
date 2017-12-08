/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first.algorithms;

import es.upm.fi.gtd.first.FusionAux;
import es.upm.fi.gtd.first.GeoImg;
import es.upm.fi.gtd.first.ParseXML;
import es.upm.fi.gtd.ij_fusion.fi.upm.transformadas.HSI;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
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
public class IHS {
public static GeoImg fusionIHS(List<GeoImg> pan, List<GeoImg> multi, Locale loc)
{
    ParseXML ic = new ParseXML(loc.getLanguage()+"_"+loc.getCountry()+".xml");
    // Variables referidas a la imagen final
    int width = pan.get(0).getImage().getWidth();		// Ancho final es el de la pancromatica
    int height = pan.get(0).getImage().getHeight();		// Altura final es la de la pancromatica
    int npixels = width * height;     	// numero de pixeles
    int nbands = multi.size(); 	// numero de bandas

    GeoImg resultado = null;
    TiledImage[] multiEsc = new TiledImage[nbands];
    // Redimensiona las bandas de la multi para que tengan el tamaño de la pan
    // Calcular el factor para cambiar de tamaño la imagen
    float factorEscalaX = width / (multi.get(0).getImage().getWidth());
    System.out.println("Factor X: " + factorEscalaX + "width: " + width);

    float factorEscalaY = height / (multi.get(0).getImage().getHeight());
    System.out.println("Factor Y: " + factorEscalaY + "height: " + height);
    RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
            BorderExtender.createInstance(BorderExtender.BORDER_COPY));
    ParameterBlock pb = null;


    //ip.setInterpolate(true);
    for (int i = 0; i < nbands; i++) {
        pb = new ParameterBlock();
        pb.addSource(multi.get(i).getImage());
        pb.add(factorEscalaX);
        pb.add(factorEscalaY);
        pb.add(0.0F);
        pb.add(0.0F);
        // Qué interpolación se usa en esta fusión?
        pb.add(new InterpolationNearest());
        multiEsc[i] = new TiledImage(JAI.create("scale", pb, rh), width, height);
    //multi.get(i).getImage().dispose();

    }

    // Crea variables utilizadas para la transformada
    double[] punto;
    double[] h = new double[npixels];
    double[] s = new double[npixels];
    double[] i = new double[npixels];
    double r, g, b, p;

    ParameterBlock pbMaxMin = new ParameterBlock();

    pbMaxMin.addSource(pan.get(0).getImage());
    RenderedOp extrema = JAI.create("extrema", pbMaxMin);
    // Must get the extrema of all bands !
    double[] allMins = (double[]) extrema.getProperty("minimum");
    double[] allMaxs = (double[]) extrema.getProperty("maximum");
    double smin = allMins[0];
    System.out.println("Val min pan: " + smin);
    double smax = allMaxs[0];
    System.out.println("Val max pan: " + smax);
    for (int v = 1; v < allMins.length; v++) {
        if (allMins[v] < smin) {
            smin = allMins[v];
        }
        if (allMaxs[v] > smax) {
            smax = allMaxs[v];
        }
    }

Raster rojo = multiEsc[2].getData();
Raster verde = multiEsc[1].getData();
Raster azul = multiEsc[0].getData();
Raster rPan = pan.get(0).getImage().getData();


    // Transformada a HSI
    Normalizador rango = new Normalizador(smin, smax); // Escala imagen pancromatica

    for (int y = 0,  k = 0; y < height; y++) {
        for (int x = 0; x < width; x++, k++) {
            r = rojo.getSampleDouble(x, y, 0);
            g = verde.getSampleDouble(x, y, 0);
            b = azul.getSampleDouble(x, y, 0);
            p = rPan.getSampleDouble(x, y, 0);
            //System.out.println("Valores originales: r: "+r+" g: "+g+" b: "+b+" p: "+p); 
            punto = HSI.FusionIHS(r, g, b, p);
            //System.out.println("Valores fusión 1: r: "+punto[0]+" g: "+punto[1]+" b: "+punto[2]); 
            h[k] = punto[0];
            s[k] = punto[1];
            i[k] = punto[2];
            //if (h[k]>255 || s[k]>255 || i[k]>255)
            //System.out.println("Valores fusión 2: r: "+h[k]+" g: "+s[k]+" b: "+i[k]); 
            
            //punto = RGBHSI.toHSI(r, g, b);
            /*h[k] = punto[0];
            s[k] = punto[1];*/
            // No guardo I porque es la componente que voy a reemplazar
            //i[k] = punto[2];
        }

    // Actualiza la barra de progreso
    // Cambia la componente I por la pancromatica
    }
   /* for (int y = 0,  k = 0; y < height; y++) {
        for (int x = 0; x < width; x++, k++) {
            i[k] = rango.Normalizar(rPan.getSampleDouble(x, y, 0));
            punto = RGBHSI.toRGB(h[k], s[k], i[k]);
            h[k] = rango.Escalar(punto[0]);
            s[k] = rango.Escalar(punto[1]);
            i[k] = rango.Escalar(punto[2]);
        }

    }*/
    // Creamos la pila con las bandas finales


            short[] puntosh_b = new short[npixels];
            short[] puntoss_b = new short[npixels];
            short[] puntosi_b = new short[npixels];

            for (int k = 0; k < npixels; k++) {
                puntosh_b[k] = (short) h[k];
                puntoss_b[k] = (short) s[k];
                puntosi_b[k] = (short) i[k];
            }
            DataBufferUShort dbuffer = new DataBufferUShort(puntosh_b, puntosh_b.length);
            SampleModel samplemod = RasterFactory.createBandedSampleModel(
                    DataBuffer.TYPE_USHORT, width, height, 1);
            ColorModel colormod = PlanarImage.createColorModel(samplemod);
            Raster raster = RasterFactory.createWritableRaster(samplemod,
                    dbuffer, new Point(0, 0));
            TiledImage tiledh = new TiledImage(0, 0, width, height, 0, 0,
                    samplemod, colormod);
            tiledh.setData(raster);

            dbuffer = new DataBufferUShort(puntoss_b, puntoss_b.length);
            samplemod = RasterFactory.createBandedSampleModel(
                    DataBuffer.TYPE_USHORT, width, height, 1);
            colormod = PlanarImage.createColorModel(samplemod);
            raster = RasterFactory.createWritableRaster(samplemod,
                    dbuffer, new Point(0, 0));
            TiledImage tileds = new TiledImage(0, 0, width, height, 0, 0,
                    samplemod, colormod);
            tileds.setData(raster);

            dbuffer = new DataBufferUShort(puntosi_b, puntosi_b.length);
            samplemod = RasterFactory.createBandedSampleModel(
                    DataBuffer.TYPE_USHORT, width, height, 1);
            colormod = PlanarImage.createColorModel(samplemod);
            raster = RasterFactory.createWritableRaster(samplemod,
                    dbuffer, new Point(0, 0));
            TiledImage tiledi = new TiledImage(0, 0, width, height, 0, 0,
                    samplemod, colormod);
            tiledi.setData(raster);
            
            PlanarImage planh = FusionAux.trasladarRangoShort(tiledh, pan.get(0).getImage());
            PlanarImage plans = FusionAux.trasladarRangoShort(tileds, pan.get(0).getImage());
            PlanarImage plani = FusionAux.trasladarRangoShort(tiledi, pan.get(0).getImage());
            
            pb = new ParameterBlock();
            pb.addSource(planh);
            pb.addSource(plans);
            pb.addSource(plani);

            resultado = new GeoImg(
                    pan.get(0).getData(), 
                    JAI.create("bandmerge", pb), 
                    new File(
                        ic.buscar(164)
                        +pan.get(0).getFile().getName()
                        +"&"
                        +multi.get(0).getFile().getName()),
                    pan.get(0).getStreamMetadata(), 
                    pan.get(0).getImageMetadata()); // Tag 164
      
    h = null;
    s = null;
    i = null;



    return resultado;
    
}
}
