/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Alvar
 */
public class CargaAux {
    
    /**
     * Obtiene los datos necesarios para poder cargar un fichero BIL
     * @param da Objeto auxiliar para mostrar el diálogo de entrada de datos
     * @param t Cuadro principal de la aplicación
     * @param ic Objeto de datos de traducción actual
     * @return Array de enteros con los datos BIL
     */
    public static int [] manejoBIL(DialogosAux da, JFrame t, ParseXML ic)
    {
        

//		 Mostrar la cajita que pedirá los valores
        Object[] message = da.crearMensaje(DialogosAux.DIAG_BIL);
        String[] options = da.crearOpciones(DialogosAux.OPT_BIL);

        int result = JOptionPane.showOptionDialog(t,
                message,
                ic.buscar(1),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]); // Tag 1
        if (result == 0) {
            int bandas = Integer.parseInt(((JTextField) message[1]).getText());
            int lineas = Integer.parseInt(((JTextField) message[3]).getText());
            int columnas = Integer.parseInt(((JTextField) message[5]).getText());
            int celdas = Integer.parseInt(((JTextField) message[7]).getText());
            int [] res = {bandas, lineas, columnas, celdas};
            return res;

        } else {
            JOptionPane.showMessageDialog(t, ic.buscar(2)); // Tag 2
        }
        return null;
    
    }
    
    /**
     * Carga un archivo como imagen
     * @param file Fichero
     * @param da Objeto auxiliar para mostrar un diálogo en caso de ser un
     * fichero BIL
     * @param t Cuadro principal de la aplicación
     * @param ic Objeto de datos de traducción
     * @return Lista de ficheros GeoImg resultantes del archivo
     */
    public static List<GeoImg> cargarArchivo(File file, DialogosAux da, JFrame t, ParseXML ic) {

        List<GeoImg> resultado = new ArrayList<GeoImg>();
        if (file.getName().endsWith("bil") || file.getName().endsWith("BIL")) {// Es un archivo BIL


            int[] res = manejoBIL(da, t, ic);
            AbrirBIL ab = new AbrirBIL();
            PlanarImage[] bandasB = new PlanarImage[res[0]];

            try {
                bandasB = ab.transBIL(file, res[0], res[1], res[2], res[3]);
                for (int i = 0; i < res[0]; i++) {
                    resultado.add(new GeoImg(null, bandasB[i], file, null, null));
                }
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(t,
                        ic.buscar(6),
                        ic.buscar(7),
                        JOptionPane.ERROR_MESSAGE); // Tag 6, 7
            }

        } else {
            ImageInfo ii = new ImageInfo();
            GeoTiffIIOMetadataAdapter ti = null;
            try {
                ii.setInput((InputStream) new FileInputStream(file));

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            ii.check();
            FileSeekableStream stream;
            if (ii.getFormat() == ImageInfo.FORMAT_TIFF) {	// Leyendo TIFF
                try {

                    ti = new GeoTiffIIOMetadataAdapter(file);
                    stream = new FileSeekableStream(file);
                    ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, null);


                    
                    
                        RenderedImage imagen2 = JAI.create("imageread", file.getPath());
                        int nbands = imagen2.getSampleModel().getNumBands();
                        if (nbands != 1) {
                        for (int j = 0; j < nbands; j++) {
                            ParameterBlockJAI selectPb = new ParameterBlockJAI("BandSelect");
                            selectPb.addSource(imagen2);
                            selectPb.setParameter("bandIndices", new int[]{j});

                            RenderedImage singleBandImage = JAI.create("BandSelect", selectPb);

                            resultado.add(
                                    j,
                                    new GeoImg(
                                    Info.crearGeoData(ti),
                                    PlanarImage.wrapRenderedImage(singleBandImage),
                                    new File(file.getName() + " - Banda " + (j + 1)),
                                    Info.crearStreamData(ti),
                                    Info.crearImageData(ti)));
                        }
                    }
                    else {

                        int paginas = 0;
                        paginas = dec.getNumPages();


                        for (int j = 0; j < paginas; j++) {
                            RenderedImage ri = null;
                            ri = dec.decodeAsRenderedImage(j);

                            resultado.add(
                                    new GeoImg(
                                    Info.crearGeoData(ti),
                                    PlanarImage.wrapRenderedImage(ri),
                                    file,
                                    Info.crearStreamData(ti),
                                    Info.crearImageData(ti)));
                        }
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(CargaAux.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (ii.getFormat() == ImageInfo.FORMAT_JPEG)
            {
                PlanarImage image = JAI.create("imageread",file);
		resultado.add(new GeoImg(
                                         null,
                                         image,
                                         file,
                                         null,
                                         null));
            }
        }
        return resultado;

    }
}
