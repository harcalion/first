/**
 * Paquete que engloba toda la pr�ctica de Sistemas Inform�ticos. Implementa
 * los algoritmos de Mallat, � trous normal y ponderado y realiza varias tareas
 * de apertura y guardado de im�genes.
 * @author Alvar Garc�a del R�o
 * @version 1.0
 * 
 */
package es.upm.fi.gtd.first;

import com.sun.media.imageio.plugins.tiff.GeoTIFFTagSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.metadata.IIOMetadata;
import javax.media.jai.BorderExtender;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBicubic2;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;

import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.JPEGEncodeParam;
import es.upm.fi.gtd.first.algorithms.FFIHS;
import es.upm.fi.gtd.first.algorithms.IHS;
import es.upm.fi.gtd.first.algorithms.KMeansAdapter;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Locale;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;









/**
 * Clase que contiene toda la interfaz del paquete Atrous, los atributos para la
 * interfaz, los manejadores de eventos y alg�n M�todo de conveniencia
 * 
 * @author Alvar Garc�a del R�o
 */
public class Atrous {

    /**
     * Men� principal de la aplicaci�n
     */
    private static JMenuBar menu;
    /**
     * Objeto que escuchar� las acciones del men�
     */
    private static Dialogos diag;
    /**
     * JFrame principal de la aplicaci�n
     */
    public static JFrame t;

    /**
     * JTabbedPane global de la aplicaci�n
     */
    private JTabbedPane tpanel = null;
    /**
     * JDesktopPane global de la aplicaci�n
     */
    private AreaTrabajo dpanel = null;
    /**
     * JTextField donde se muestra la informaci�n de pixel
     */
    private static JTextField status;
    /**
     * Lista de nombres de archivo que se han elegido en Archivo -> Abrir o como
     * PAN en fusi�n, etc...
     */
    private GeoImg[] files_pan = null;
    /**
     * Lista de nombres de archivo que se han elegido como MULTI en fusi�n,
     * etc...
     */
    private File[] files_multi = null;
    /**
     * Lista de im�genes que se est�n mostrando
     */
    // private PlanarImage [] imgs = null;
    // private ImagenMostrada [] imgs = null;
    private List<GeoImg> imgs;
    /**
     * Lista de im�genes obtenidas de un TIFF multip�gina o de un archivo ASCII
     * BIL
     */
    // private ImagenMostrada [] bandasBIL = null;
    private List<GeoImg> images_pan = null;
    /**
     * Lista de im�genes obtenidas de un TIFF multip�gina o de un archivo ASCII
     * BIL
     */
    private List<GeoImg> images_multi = null;
    /**
     * Variables para manejo de archivos BIL
     */
    private int bandas = 0;
    /**
     * Variables para manejo de archivos BIL
     */
    private int lineas = 0;
    /**
     * Variables para manejo de archivos BIL
     */
    private int columnas = 0;
    /**
     * Variables para manejo de archivos BIL
     */
    private int celdas = 0;
    /**
     * Variables que se eligen para efectuar las fusiones
     */
    private int niv_PAN = 0;
    /**
     * Variables que se eligen para efectuar las fusiones
     */
    private int niv_MULTI = 0;
    /**
     * Variables que se eligen para efectuar las fusiones
     */
    private int precis = 0;
    /**
     * Variables que se eligen para efectuar las fusiones
     */
    private int wvm = 0;
    /**
     * Variables que se eligen para efectuar las fusiones
     */
    private int interpol = 0;
    /**
     * Variables que se eligen para efectuar las fusiones
     */
    private int muestras = 0;
    /**
     * Valor de ponderaci�n para la fusi�n � trous ponderada
     */
    private double ponderacion = 0.0;
    /**
     * Para permitir obtener los valores promedio y por banda en c�lculo de
     * ERGAS y de alfa
     */
    private boolean separado = false;
    /**
     * Manejadores de eventos ComboBox
     */
    public ActionListener wvt;
    /**
     * Manejadores de eventos ComboBox
     */
    public ActionListener interp_lis;
    /**
     * ComboBoxes para elegir la wavelet y la interpolaci�n deseadas
     */
    private JComboBox cbwavelet;
    /**
     * ComboBoxes para elegir la wavelet y la interpolaci�n deseadas
     */
    private JComboBox interp;
    /**
     * Definici�n del objeto Ergas para permitir la comunicaci�n entre Atrous y
     * Ergas y reutilizar atrousLis
     */
    private Ergas erg;
    /**
     * Manejador de eventos utilizado en el c�lculo de Ergas, el c�lculo del
     * valor �ptimo de ponderaci�n y la fusi�n � trous
     */
    public ActionListener atrousLis;
    /**
     * JFileChooser global (para que recuerde el �ltimo directorio de trabajo)
     */
    private static JFileChooser fglobal;
    /**
     * Fichero a guardar
     */
    private File guardar;
    /**
     * N�mero de niveles a degradar
     */
    private int niveles;
    /**
     * Filtro de archivos para usar en fglobal
     */
    private ExampleFileFilter pep;
    /**
     * Profundidad de bits para guardar im�genes
     */
    private int profundidad;
    /**
     * Auxiliar para crear los componentes de los di�logos
     */
    private DialogosAux da;
    /**
     * Marcador para leer los ficheros multibanda como bandas separadas
     */
    private boolean leerSeparadas;
    /**
     * Barra de progreso para las operaciones de la aplicaci�n
     */
    private static JProgressBar barraprogreso;
    /**
     * Componente donde se ubican la barra de progreso y la barra de estado
     */
    private static JPanel sur;
    /**
     * Valor inicial de la barra de progreso
     */
    public static int valorbarra = 0;
    /**
     * Thread independiente que permite actualizar la barra de progreso 
     */
    public static RunnableFuture updateAComponent;

    /**
     * Idioma del interfaz de la aplicaci�n
     */
    public Locale id;
    
    /**
     * Tenemos el XML en memoria para conseguir un acceso m�s r�pido
     */
    public static ParseXML ic;
    
    private static JMenu archivo;
    private static JMenu visual;
    private static JMenu fus;
    private static JMenu indices;
    private static JMenu util;
    private static JMenu idioma;
    private static JMenu ventana;
    private static JMenu sobre;
    
    private static JMenuItem open;

        private static JMenuItem save;
        

        private static JMenuItem saveAll;

        private static JMenuItem exit;
        private static JMenuItem show;
        

        private static JMenuItem show2;
        

        private static JMenuItem close;
        

        private static JMenuItem combinar;
        
        private static ButtonGroup bg;

        private static JMenuItem fusionar;
        

        private static JMenuItem atrous;

        
        //Meri
        private static JMenuItem fihs;
        private static JMenuItem ihs;
        private static JMenuItem degradar;
        private static JMenuItem fourier;
        private static JMenuItem pca;
        private static JMenuItem brovey;
        private static JMenuItem respuesta;
        private static JMenuItem mdmr;
        private static JMenuItem zhou;

        private static JMenuItem boxcount;
        
        private static JMenuItem ergasespayafus;
        

        private static JMenuItem ergasespeyafus;

        private static JMenuItem pond;
        
        private static JMenuItem fusionporbanda;

        private static JMenuItem escala;
        private static JMenuItem and;

        
        private static JMenuItem prueba;

        private static JMenuItem recortar;
        
        private static JMenuItem kmedias;
        private static JMenuItem acerca;
        
        private static JMenuItem actualizar;
        
        private static JMenuItem cascada;
        private static JMenuItem mosaico;
        
        
        
        private static List<Locale> idiomas;
    /**
     * Constructor para la clase b�sica Atrous,crea los objetos necesarios y los
     * manejadores de acciones del usuario.
     * 
     * @see Main#main
     * 
     */
    public Atrous() {
        // Crear el objeto que hace parsing de los ficheros de idioma
        id = new Locale("es", "ES");
        ic = new ParseXML(id.getLanguage()+"_"+id.getCountry()+".xml");
        erg = new Ergas(this);

        diag = new Dialogos(this, t, fglobal);
        da = new DialogosAux(this);

        // Action Listeners
        interp_lis = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JComboBox interpbox = (JComboBox) e.getSource();
                interpol = interpbox.getSelectedIndex();
                interpbox.setSelectedIndex(interpol);


            }
        };
        wvt = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getSource() == cbwavelet) {
                    wvm = cbwavelet.getSelectedIndex();

                    if (cbwavelet.getSelectedItem().toString().equals(
                            "b3spline")) {

                        wvm = 6;
                    } else {
                        cbwavelet.setSelectedIndex(wvm);
                    }

                }
            }
        };

    }
    
    /**
     * Obtiene el idioma seleccionado en la herramienta
     * @return El idioma seleccionado
     */
    public Locale getSelectedLocale()
    {
        Enumeration<AbstractButton> e =  bg.getElements();
        int i = 0;
        while (e.hasMoreElements())
        {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.nextElement();
           if (item.isSelected())
           {
               System.out.println(idiomas.get(i).getDisplayCountry());
               return idiomas.get(i);
               
           }
                   i++;
        }

        return null;
    }
    
    /**
     * Obtiene el objeto que contiene las traducciones actualmente cargado
     * @return El objeto que contiene las traducciones
     */
    public ParseXML getIC()
    {
        return ic;
    }
    
    /**
     * Actualiza todos los componentes permanentes de la aplicaci�n que contienen
     * texto localizable
     * @param loc Idioma actual
     */
    public void actualizarIdioma(Locale loc)
    {
        ic = new ParseXML(loc.getLanguage()+"_"+loc.getCountry()+".xml");
        archivo.setText(ic.buscar(32));
        visual.setText(ic.buscar(33));
        fus.setText(ic.buscar(34));
        indices.setText(ic.buscar(184));
        util.setText(ic.buscar(35));
        idioma.setText(ic.buscar(166));
        ventana.setText(ic.buscar(178));
        sobre.setText(ic.buscar(36));
        
         open.setText(ic.buscar(37));

         save.setText(ic.buscar(38));

         saveAll.setText(ic.buscar(39));

         exit.setText(ic.buscar(40));

         show.setText(ic.buscar(41));
         show2.setText(ic.buscar(42));

         close.setText(ic.buscar(43));

         combinar.setText(ic.buscar(44));

         fusionar.setText(ic.buscar(45));

         atrous.setText(ic.buscar(46));

         fihs.setText(ic.buscar(47));
         ihs.setText(ic.buscar(48));
         degradar.setText(ic.buscar(49));
         pca.setText(ic.buscar(169));
         brovey.setText(ic.buscar(170));
         respuesta.setText(ic.buscar(171));
         mdmr.setText(ic.buscar(172));
         fourier.setText(ic.buscar(85));
         boxcount.setText(ic.buscar(174));

         zhou.setText(ic.buscar(173));
         ergasespayafus.setText(ic.buscar(50));
         ergasespeyafus.setText(ic.buscar(51));
         fusionporbanda.setText(ic.buscar(167));
         pond.setText(ic.buscar(52));

         escala.setText(ic.buscar(53));

        and.setText(ic.buscar(54));

         prueba.setText(ic.buscar(55));

         recortar.setText(ic.buscar(56));
        
        kmedias.setText(ic.buscar(57));

        mosaico.setText(ic.buscar(179));
        cascada.setText(ic.buscar(180));


        actualizar.setText(ic.buscar(175));
        acerca.setText(ic.buscar(58));
        if (dpanel!=null)
        {
            dpanel.getListaImagenes().getPanelImagenes().getTitulo().setText(ic.buscar(161));
        }
        diag.actualizarIdioma(loc);
    }

    /**
     * M�todos que se encargar�n de actualizar los atributos de la clase
     * principal desde las clases donde se dibujan di�logos.
     * @param nb N�mero de bandas
     */
    public void setBandas(int nb) {
        bandas = nb;

    }

    /**
     * Establece el n�mero de l�neas del fichero BIL
     * @param nl N�mero de l�neas
     */
    public void setLineas(int nl) {
        lineas = nl;
    }

    /**
     * Establece el n�mero de columnas del fichero BIL
     * @param nc N�mero de columnas
     */
    public void setColumnas(int nc) {
        columnas = nc;
    }

    /**
     * Establece el n�mero de celdas del fichero BIL
     * @param ncel N�mero de celdas
     */
    public void setCeldas(int ncel) {
        celdas = ncel;
    }


    /**
     * Establece los archivos cargados como MULTI
     * @param extfiles Archivos
     */
    public void setFilesMulti(File[] extfiles) {
        files_multi = extfiles;
    }

    /**
     * Establece el archivo en el que se almacenar� el resultado
     * @param fguardar Archivo de destino
     */
    public void setGuardar(File fguardar) {
        guardar = fguardar;
    }

    /**
     * M�todo que se encarga de pedir los valores para la conversi�n de un
     * archivo multibanda ASCII BIL en una colecci�n de im�genes seg�n los
     * par�metros introducidos
     * 
     * @author Alvar Garc�a del R�o
     */
    public void manejoBIL() {

        // Mostrar la cajita que pedir� los valores
        Object[] message = da.crearMensaje(DialogosAux.DIAG_BIL);
        String[] options = da.crearOpciones(DialogosAux.OPT_BIL);

        int result = JOptionPane.showOptionDialog(t, message,
                "Introduzca valores BIL", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]); //Tag 1
        if (result == 0) {
            bandas = Integer.parseInt(((JTextField) message[1]).getText());
            lineas = Integer.parseInt(((JTextField) message[3]).getText());
            columnas = Integer.parseInt(((JTextField) message[5]).getText());
            celdas = Integer.parseInt(((JTextField) message[7]).getText());

        } else {
            JOptionPane.showMessageDialog(t, ic.buscar(2)); // Tag 2
        }
    }

    /**
     * Muestra im�genes, que se le pasan como un array de GeoImg, especificando
     * si mostrarlas como ventanas o como pesta�as
     * @param imgs_nuevas Las im�genes a mostrar
     * @param tipo 0 si ventanas, 1 si pesta�as
     */
    public void manejoIMGS(GeoImg[] imgs_nuevas, int tipo) {

        manejoIMGS(Arrays.asList(imgs_nuevas), tipo);
    }

    /**
     * Muestra im�genes, que se le pasan como una lista de GeoImg, especificando
     * si mostrarlas como ventanas o como pesta�as
     * @param imgs_nuevas las im�genes a mostrar
     * @param tipo 0 si ventanas, 1 si pesta�as
     */
    public void manejoIMGS(List<GeoImg> imgs_nuevas, int tipo) {
        // Recibe las im�genes nuevas y tiene en cuenta si ya hay mostradas
        if (imgs != null) {
            imgs.addAll(imgs_nuevas);

            Mostrar.cerrarImagen(t, dpanel, tpanel, status);
            if (tipo == 0) { // En DesktopPane
                dpanel = Mostrar.mostrarImagen2(imgs, dpanel, t, status, this);
            } else {
                tpanel = Mostrar.mostrarImagen(imgs, t, tpanel, status, this);
            }

        } else {
            imgs = new ArrayList<GeoImg>();
            imgs.addAll(imgs_nuevas);

            Mostrar.cerrarImagen(t, dpanel, tpanel, status);
            if (tipo == 0) { // En DesktopPane
                dpanel = Mostrar.mostrarImagen2(imgs, dpanel, t, status, this);                
            } else {
                tpanel = Mostrar.mostrarImagen(imgs, t, tpanel, status, this);
            }

        }
        files_pan = null;
        files_multi = null;
    }

    /**
     * Establece el n�mero de niveles que se degradar� la imagen PAN
     * @param niv_PAN2 N�mero de niveles
     */
    public void setNivPan(int niv_PAN2) {

        niv_PAN = niv_PAN2;
    }

    /**
     * Establece el n�mero de niveles que se degradar�n las im�genes MULTI
     * @param niv_MULTI2 N�mero de niveles
     */
    public void setNivMulti(int niv_MULTI2) {

        niv_MULTI = niv_MULTI2;
    }

    /**
     * Establece el n�mero de niveles de degradaci�n
     * @param num_niveles N�mero de niveles
     */
    public void setNiveles(int num_niveles) {

        niveles = num_niveles;
    }

    /**
     * Establece si los resultados de la operaci�n se har�n banda a banda
     * @param b true si banda a banda, false si conjuntamente
     */
    public void setSeparado(boolean b) {

        separado = b;
    }

    /**
     * Establece el valor de la ponderaci�n
     * @param d Ponderaci�n
     */
    public void setPonderacion(double d) {

        ponderacion = d;
    }

    /**
     * Obtiene qu� interpolaci�n se est� utilizando
     * @return 0 si vecino m�s pr�ximo, 1 si bilineal, 2 si bic�bica, 3 si
     * bic�bica 2, 4 si Lanczos(3)
     */
    public int getInterpol() {

        return interpol;
    }

    /**
     * Establece la precisi�n en bits de submuestra para el algoritmo de
     * interpolaci�n bic�bica
     * @param precis2 N�mero de bits
     */
    public void setPrecision(int precis2) {

        precis = precis2;
    }

    /**
     * Establece qu� tipo de funci�n wavelet se utilizar� en el algoritmo
     * @param i 0 si db1, 1 si db2, 2 si db3, 3 si db4, 4 si db5, 5 si db6,
     * 6 si b3spline
     */
    public void setWavelet(int i) {

        wvm = i;
    }

    /**
     * Obtiene la ventana interior que tiene actualmente el foco
     * @return Ventana interior con foco
     */
    public DisplayJAIWithPixelInfo getMarcoActivado() {

        DisplayJAIWithPixelInfo resultado = null;
        if (tpanel != null) {
            if (tpanel.isShowing()) {
                JScrollPane jsptemp = (JScrollPane) tpanel.getSelectedComponent();
                JViewport jvtemp = jsptemp.getViewport();
                resultado = (DisplayJAIWithPixelInfo) jvtemp.getView();

            }
        }
        if (dpanel != null) {
            if (dpanel.isShowing()) {
                JScrollPane jsptemp;
                if (dpanel.getAllFrames().length == 1) {
                    JInternalFrame[] jiftemp = dpanel.getAllFrames();

                    jsptemp = (JScrollPane) jiftemp[0].getContentPane().getComponent(0);

                } else {
                    JInternalFrame jiftemp =  dpanel.getSelectedFrame();
                    jsptemp = (JScrollPane) jiftemp.getContentPane().getComponent(0);
                }
                JViewport jvtemp = jsptemp.getViewport();
                // DisplayJAIWithPixelInfoInt djai =
                // (DisplayJAIWithPixelInfoInt) jvtemp.getComponent(0);
                resultado = (DisplayJAIWithPixelInfo) jvtemp.getComponent(0);

            }
        }
        return resultado;
    }

    /**
     * Guarda la imagen contenida en la ventana interior o pesta�a seleccionada
     * en un fichero
     * @param tipo Dialogos.FORMATO_JPEG si JPEG, Dialogos.FORMATO_TIFF si TIFF
     */
    public void guardar(int tipo) {

        RenderedImage imgGuardar = null;
        if (tpanel != null) {
            if (tpanel.isShowing()) {
                JScrollPane jsptemp = (JScrollPane) tpanel.getSelectedComponent();
                JViewport jvtemp = jsptemp.getViewport();
                DisplayJAIWithPixelInfoInt djai = (DisplayJAIWithPixelInfoInt) jvtemp.getView();
                imgGuardar = djai.getSource();
            }
        }
        if (dpanel != null) {
            if (dpanel.isShowing()) {
                JScrollPane jsptemp;
                if (dpanel.getAllFrames().length == 1) {
                    JInternalFrame[] jiftemp = dpanel.getAllFrames();

                    jsptemp = (JScrollPane) jiftemp[0].getContentPane().getComponent(0);

                } else {
                    JInternalFrame jiftemp =  dpanel.getSelectedFrame();
                    jsptemp = (JScrollPane) jiftemp.getContentPane().getComponent(0);
                }
                JViewport jvtemp = jsptemp.getViewport();
                // DisplayJAIWithPixelInfoInt djai =
                // (DisplayJAIWithPixelInfoInt) jvtemp.getComponent(0);
                TiledImage tiled = null;
                
                if (jvtemp.getComponent(0) instanceof DisplayJAIWithPixelInfoColor)
                {
                    DisplayJAIWithPixelInfoColor djai = (DisplayJAIWithPixelInfoColor) jvtemp.getComponent(0);
                    tiled = ElegirProfundidad.crearProfundidadReducida(
                        djai.getDataInteger(), profundidad, djai.getSource().getWidth(), djai.getSource().getHeight());
                }
                else if (jvtemp.getComponent(0) instanceof DisplayJAIWithPixelInfoByte)
                {
                    DisplayJAIWithPixelInfoByte djai = (DisplayJAIWithPixelInfoByte) jvtemp.getComponent(0);
                    tiled = ElegirProfundidad.crearProfundidadReducida(
                        djai.getDataByte(), profundidad, djai.getSource().getWidth(), djai.getSource().getHeight());
                }
                else if (jvtemp.getComponent(0) instanceof DisplayJAIWithPixelInfoFloat)
                {
                    DisplayJAIWithPixelInfoFloat djai = (DisplayJAIWithPixelInfoFloat) jvtemp.getComponent(0);
                    tiled = ElegirProfundidad.crearProfundidadReducida(
                        djai.getDataDouble(), profundidad, djai.getSource().getWidth(), djai.getSource().getHeight());
                }
                else if (jvtemp.getComponent(0) instanceof DisplayJAIWithPixelInfoInt)
                {
                    DisplayJAIWithPixelInfoInt djai = (DisplayJAIWithPixelInfoInt) jvtemp.getComponent(0);
                    tiled = ElegirProfundidad.crearProfundidadReducida(
                        djai.getDataInteger(), profundidad, djai.getSource().getWidth(), djai.getSource().getHeight());
                }
                else if (jvtemp.getComponent(0) instanceof DisplayJAIWithPixelInfoShort)
                {
                    DisplayJAIWithPixelInfoShort djai = (DisplayJAIWithPixelInfoShort) jvtemp.getComponent(0);
                    tiled = ElegirProfundidad.crearProfundidadReducida(
                        djai.getDataShort(), profundidad, djai.getSource().getWidth(), djai.getSource().getHeight());
                }
                

                

                imgGuardar = tiled;
            }
        }
        
        if (tipo == Dialogos.FORMATO_JPEG) {// Guardar como JPEG
            JAI.create("filestore", imgGuardar, guardar.getPath(), "JPEG",
                    new JPEGEncodeParam());
        }
        if (tipo == Dialogos.FORMATO_TIFF) {// Guardar como TIFF
            JAI.create("filestore", imgGuardar, guardar.getPath(), "TIFF");
        } else {// No tiene extensi�n o es una extensi�n extra�a
            JAI.create("filestore", imgGuardar, guardar.getPath() + ".tif",
                    "TIFF");
        }
    }

    /**
     * Establece el filtro de ficheros para el di�logo de selecci�n de ficheros
     * @param pep2 filtro de ficheros
     */
    public void setPep(ExampleFileFilter pep2) {

        pep = pep2;
    }

    /**
     * Guarda todas las im�genes abiertas en la herramienta en ficheros
     * @param profundidad Profundidad de colores en la que se guardar�n
     */
    public void guardarTodo(int profundidad) {

        RenderedImage[] imgGuardar = new RenderedImage[imgs.size()];
        IIOMetadata[] streamData = new IIOMetadata[imgs.size()];
        IIOMetadata[] imageData = new IIOMetadata[imgs.size()];
        if (tpanel != null) {
            if (tpanel.isShowing()) {
                for (int i = 0; i < imgGuardar.length; i++) {
                    JScrollPane jsptemp = (JScrollPane) tpanel.getComponent(i);
                    JViewport jvtemp = jsptemp.getViewport();
                    DisplayJAIWithPixelInfoInt djai = (DisplayJAIWithPixelInfoInt) jvtemp.getView();
                    imgGuardar[i] = djai.getSource();
                }
            }
        }
        if (dpanel != null) {

            if (dpanel.isShowing()) {

                JInternalFrame[] jiftemp = dpanel.getAllFrames();

                for (int i = 0; i < imgGuardar.length; i++) {

                    JScrollPane jsptemp = (JScrollPane) jiftemp[i].getContentPane().getComponent(0);
                    JViewport jvtemp = jsptemp.getViewport();
                    DisplayJAIWithPixelInfo djai = null;
                    System.out.println("Clase:" + jvtemp.getComponent(0).getClass().getName());
                    if (jvtemp.getComponent(0).getClass().getName().compareTo("es.upm.fi.gtd.first.DisplayJAIWithPixelInfoByte") == 0) {
                        djai = (DisplayJAIWithPixelInfoByte) jvtemp.getComponent(0);
                    // La imagen original es igual a la imagen mostrada(tener en cuenta el zoom)

                    } else if (jvtemp.getComponent(0).getClass().getName().compareTo("es.upm.fi.gtd.first.DisplayJAIWithPixelInfoColor") == 0) {
                        djai = (DisplayJAIWithPixelInfoColor) jvtemp.getComponent(0);
                    } else if (jvtemp.getComponent(0).getClass().getName().compareTo("es.upm.fi.gtd.first.DisplayJAIWithPixelInfoFloat") == 0) {
                        djai = (DisplayJAIWithPixelInfoFloat) jvtemp.getComponent(0);
                    } else if (jvtemp.getComponent(0).getClass().getName().compareTo("es.upm.fi.gtd.first.DisplayJAIWithPixelInfoInt") == 0) {
                        djai = (DisplayJAIWithPixelInfoInt) jvtemp.getComponent(0);
                    } else if (jvtemp.getComponent(0).getClass().getName().compareTo("es.upm.fi.gtd.first.DisplayJAIWithPixelInfoShort") == 0) {
                        djai = (DisplayJAIWithPixelInfoShort) jvtemp.getComponent(0);
                    }


                    imgGuardar[i] = djai.getSource();
                    // Pasar a la profundidad deseada
                    imgGuardar[i] = ElegirProfundidad.crearProfundidadReducida(imgGuardar[i], profundidad, imgGuardar[i].getWidth(), imgGuardar[i].getHeight());
                    streamData[i] = djai.getStreamData();
                    imageData[i] = djai.getImageData();

                }
            }
        }
        pep.setExtensionListInDescription(false);

        if (pep.getDescription().compareTo("Im�genes TIFF")==0) {//Tag 3 - Guardar como TIFF

            for (int i = 0; i < imgGuardar.length; i++) {
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(imgGuardar[i]);
                pb.add(guardar.getParent() + File.separator + guardar.getName() + (i + 1) + ".tif");
                pb.add("TIFF");
                pb.add(false); //UseProperties
                pb.add(false); //Transcode
                pb.add(true); //VerifyOutput
                pb.add(false); //AllowPixelReplacement
                pb.add(null); //TileSize
                pb.add(streamData[i]);
                pb.add(imageData[i]);
                JAI.create("imagewrite", pb);
            //JAI.create("imagewrite", imgGuardar[i], guardar.getPath()
            //+ File.separator + "Imagen" + i + ".tif", "TIFF");
            }
        }
        if (pep.getDescription().equals("Im�genes JPEG")) {//Tag 4 - Guardar como JPEG

            for (int i = 0; i < imgGuardar.length; i++) {

                JAI.create("imagewrite", imgGuardar[i], guardar.getPath() + File.separator + "Imagen" + i + ".jpg", "JPEG",
                        new JPEGEncodeParam());
            }
        }
        if (pep.getDescription().equals("Archivo ASCII BIL")) {//Tag 5 - Guardar como
            // BIL

            AbrirBIL ab = new AbrirBIL();
            PlanarImage[] pimgGuardar = new PlanarImage[imgGuardar.length];
            for (int i = 0; i < imgGuardar.length; i++) {
                pimgGuardar[i] = PlanarImage.wrapRenderedImage(imgGuardar[i]);
            }
            ab.guardarBIL(pimgGuardar, guardar);

        }
    }

    /**
     * Fusiona las im�genes previamente seleccionadas mediante Mallat
     */
    public void fusionar() {
        Fusion a = new Fusion(ic);

        if (files_multi.length == 1) {
            tratarImagenesMulti();
        } else {
            cargarImagenesMulti();
        }
        tratarImagenesPan();

        GeoImg[] melt = new GeoImg[images_multi.size()];

        for (int i = 0; i < images_multi.size(); i++) {
            if (false) {
                melt[i] = a.fusionar(images_pan.get(0), images_multi.get(i), wvm,
                        niv_PAN, niv_MULTI);
            } else {
                melt[i] = a.fusionarEscalando(images_pan.get(0), images_multi.get(i), wvm,
                        niv_PAN, niv_MULTI);
            }
        }

        manejoIMGS(melt, 0);

    }

    /**
     * Carga las im�genes seleccionadas como multiespectrales, teniendo en cuenta
     * la posibilidad de multibanda, multip�gina y distintos formatos de imagen
     */
    public void cargarImagenesMulti() {

        if (images_multi == null) {
            images_multi = new ArrayList<GeoImg>();
        }
        images_multi.clear();
        for (int i = 0; i < files_multi.length; i++) {
            GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(
                    files_multi[i]);
            images_multi.add(new GeoImg(Info.crearGeoData(ti), JAI.create(
                    "imageread", files_multi[i].getPath()), files_multi[i],
                    Info.crearStreamData(ti), Info.crearImageData(ti)));
        }

    }

    /**
     * Procesa los ficheros cargados como MULTI, teniendo en cuenta que pueden
     * ser ficheros multibanda/multip�gina
     */
    public void tratarImagenesMulti() {

        if (images_multi != null) {
            images_multi.clear();
        } else {
            images_multi = new ArrayList<GeoImg>();
        }

        PlanarImage[] bandasB = null;
        // Usar ImageInfo
        ImageInfo ii = new ImageInfo();

        if (files_multi[0].getName().endsWith("bil") || files_multi[0].getName().endsWith("BIL")) {// Es un archivo
            // BIL

            manejoBIL();
            AbrirBIL ab = new AbrirBIL();
            bandasB = new PlanarImage[bandas];
            images_multi = new ArrayList<GeoImg>();
            try {
                bandasB = ab.transBIL(files_multi[0], bandas, lineas, columnas,
                        celdas);
                for (int i = 0; i < bandas; i++) {
                    images_multi.add(new GeoImg(null, bandasB[i],
                            files_multi[0], null, null));
                }
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(t,
                        ic.buscar(6),
                        ic.buscar(7), JOptionPane.ERROR_MESSAGE); // Tag 6,7 
            }

        }
        
            for (int i = 0; i < files_multi.length; i++) {

                try {



                    FileSeekableStream stream = new FileSeekableStream(
                            files_multi[i]);
                    ii.setInput((InputStream) stream);
                    ii.check();
                    if (ii.getFormat() == ImageInfo.FORMAT_TIFF) {
                        RenderedImage imagen2 = JAI.create("imageread", files_multi[0].getPath());
                        int nbands = imagen2.getSampleModel().getNumBands();
                        GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(
                                files_multi[0]);
                        if (nbands != 1) {// TIFF multibanda

                            for (int j = 0; j < nbands; j++) {
                                ParameterBlockJAI selectPb = new ParameterBlockJAI(
                                        "BandSelect");
                                selectPb.addSource(imagen2);
                                selectPb.setParameter("bandIndices", new int[]{j});

                                RenderedImage singleBandImage = JAI.create(
                                        "BandSelect", selectPb);

                                images_multi.add(new GeoImg(Info.crearGeoData(ti),
                                        PlanarImage.wrapRenderedImage(singleBandImage),
                                        new File(files_multi[i].getName() + ic.buscar(8) + j), Info.crearStreamData(ti), Info.crearImageData(ti))); // Tag 8

                            }

                        } else {
                            // Una banda


                            // A lo brasileiro
                            com.sun.media.jai.codec.ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, null);
                            int paginas = dec.getNumPages();

                            for (int j = 0; j < paginas; j++) {
                                RenderedImage ri = null;
                                ri = dec.decodeAsRenderedImage(j);


                                images_multi.add(new GeoImg(Info.crearGeoData(ti),
                                        PlanarImage.wrapRenderedImage(ri),
                                        files_multi[i], Info.crearStreamData(ti), Info.crearImageData(ti)));
                            }

                        }
                    }

                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(
                            t,
                            "Ha ocurrido un error de E/S al decodificar el archivo TIFF multip�gina.",
                            "Error sin consecuencias",
                            JOptionPane.ERROR_MESSAGE); //Tag 9,7
                }
            

        }

    }

    /**
     * Procesa las im�genes cargadas como PAN, teniendo en cuenta las operaciones
     * en las que se cargan los ficheros de esta manera y si hay im�genes
     * multibanda
     */
    public void tratarImagenesPan() {
        if (images_pan != null) {
            images_pan.clear();
        } else {
            images_pan = new ArrayList<GeoImg>();
        }
        if (files_pan.length == 1 || files_pan.length == 3) {// Es un solo
            // archivo o
            // combinar
            ImageInfo ii = new ImageInfo();
            for (int i = 0; i < files_pan.length; i++) {

                try {
                    ii.setInput((InputStream) new FileSeekableStream(
                            files_pan[i].getFile()));
                } catch (IOException ioe) {
                    Dialogos.informarExcepcion(ioe);
                }
                ii.check();

                if (files_pan[i].getFile().getName().endsWith("bil") || files_pan[i].getFile().getName().endsWith("BIL")) {// Es
                    // un
                    // archivo
                    // BIL

                    JOptionPane.showMessageDialog(t,
                            ic.buscar(10),
                            ic.buscar(7),
                            JOptionPane.ERROR_MESSAGE); // Tag 10, 7
                    return;
                }
                if (ii.getFormat() == ImageInfo.FORMAT_TIFF) {// Es un archivo
                    // TIFF
                    try {
                        RenderedImage imagen2 = JAI.create("imageread",
                                files_pan[i].getFile().getPath());

                        int nbands = imagen2.getSampleModel().getNumBands();

                        if (nbands != 1) {// TIFF multibanda

                            JOptionPane.showMessageDialog(t,
                                    ic.buscar(10),
                                    ic.buscar(7),
                                    JOptionPane.ERROR_MESSAGE); // Tag 10,7
                            return;
                        } else {
                            FileSeekableStream stream = new FileSeekableStream(
                                    files_pan[i].getFile());

                            // A lo brasileiro
                            com.sun.media.jai.codec.ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, null);
                            int paginas = dec.getNumPages();
                            if (paginas != 1) {

                                JOptionPane.showMessageDialog(
                                        t,
                                        ic.buscar(10),
                                        ic.buscar(7),
                                        JOptionPane.ERROR_MESSAGE); // Tag 10,7
                                return;
                            } else if (paginas == 1) {

                                // if (files_pan[i].getData()!=null &&
                                // files_pan[i].getStreamMetadata()!=null &&
                                // files_pan[i].getImageMetadata()!=null)
                                // {

                                images_pan.add(new GeoImg(files_pan[i].getData(), PlanarImage.wrapRenderedImage(imagen2),
                                        files_pan[i].getFile(), files_pan[i].getStreamMetadata(),
                                        files_pan[i].getImageMetadata()));

                            // }
                            // else
                            // {

                            // GeoTiffIIOMetadataAdapter ti = new
                            // GeoTiffIIOMetadataAdapter(files_pan[i].getFile());
                            // images_pan.add(new
                            // GeoImg(Info.crearGeoData(ti),
                            // PlanarImage.wrapRenderedImage(imagen2),files_pan[i].getFile(),files_pan[i].getStreamMetadata(),files_pan[i].getImageMetadata()));
                            // }
                            }
                        }
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(
                                t,
                                ic.buscar(9),
                                ic.buscar(7),
                                JOptionPane.ERROR_MESSAGE); // Tag 9,7
                    }
                }

            }
        }

    }

    /**
     * Obtiene los objetos GeoImg que est�n cargados como PAN
     * @return Los objetos GeoImg como un array
     */
    public GeoImg[] getFilesPan() {

        return files_pan;
    }

    /**
     * Obtiene los archivos que est�n cargados como MULTI
     * @return Un array de ficheros cargados
     */
    public File[] getFilesMulti() {
        return files_multi;
    }

    /**
     * Degrada las im�genes cargadas mediante la transformada wavelet
     * @param soloFinales true si s�lo se desea el resultado de la transformada
     * false si se desean obtener las im�genes de aproximaci�n y detalle
     * intermedias
     */
    public void degradar(boolean soloFinales) {
        tratarImagenesPan();
        if (files_multi != null) {
            if (files_multi.length == 1) {
                tratarImagenesMulti();
            } else {
                cargarImagenesMulti();
            }
        }
        Fusion a = new Fusion(ic);
        GeoImg[] img = a.degradar(wvm, niveles, images_pan.get(0).getFile(),soloFinales);
        manejoIMGS(img, 0);
        return;

    }

    /**
     * Calcula el �ndice ERGAS entre las im�genes fusionadas y las originales
     * que han sido previamente cargadas
     * @param b true si son im�genes ya fusionadas, false si hay que fusionarlas
     * y obtener el �ndice ERGAS de esa fusi�n
     * @param espacial true si es el �ndice ERGAS espacial, false si es el �ndice
     * ERGAS espectral
     */
    public void ergas(boolean b, boolean espacial) {

        muestras = 1;
        JTextPane message = new JTextPane();
        message.setEditable(false);
        if (files_multi.length == 1) {
            tratarImagenesMulti();
        } else {
            cargarImagenesMulti();
        }
        tratarImagenesPan();

        // Aqu� tengo que pasarle todas las bandas obtenidas de BIL o TIFF y
        // modificarlo tambi�n en muestreoErgas
        double l = 0, h = 0;
        if (images_pan.get(0) != null) {
            if (images_pan.get(0).getData() != null)
            {
                l = images_pan.get(0).getData().getXScale();
                h = images_multi.get(0).getData().getXScale();
            }
        }
        double ratio = 0;
        if ((l == 0.0) || (h == 0.0) || b) {
            ratio = pedirLH();
        } else {
            ratio = l / h;
        }

        erg.setAll(wvm, niv_PAN, niv_MULTI, interpol, precis, images_pan,
                images_multi, ponderacion, separado, b, ratio);
        double[] res = erg.muestreoErgas(muestras, t, false);
        if (res == null) {
            return;
        }
        if (separado) {
            String mensaje = new String("");
            for (int i = 0; i < res.length / 2; i++) {
                if (espacial) {
                    mensaje = mensaje.concat(ic.buscar(11) + 
                            (i + 1) + ": " + res[2 * i] + "\n"); // Tag 11
                } else {
                    mensaje = mensaje.concat(ic.buscar(12) + 
                            (i + 1) + ": " + res[2 * i + 1] + "\n"); // Tag 12
                }
            /*mensaje = mensaje.concat("ERGAS espacial de la banda "
            + (i + 1) + ": " + res[2 * i] + " ERGAS espectral: "
            + res[2 * i + 1] + "\n");*/
            }
            message.setText(mensaje);
            JOptionPane.showMessageDialog(t, message, ic.buscar(13),
                    JOptionPane.PLAIN_MESSAGE); // Tag 13
        } else {

            message.setText(ic.buscar(14) + res[0] + "\n" 
                    + ic.buscar(15) + res[1] + "\n" 
                    + ic.buscar(16) + res[2] + "\n" 
                    + ic.buscar(17) + res[3] + "\n" 
                    + ic.buscar(18) + res[4] + "\n" 
                    + ic.buscar(19) + Math.abs(res[0] - res[2]));
                        // Tag 14, 15, 16, 17, 18, 19
            JOptionPane.showMessageDialog(t, message, ic.buscar(13),
                    JOptionPane.PLAIN_MESSAGE); // Tag 13
        }

    // ParseXML.crearOActualizar(files,res,ParseXML.ERGAS,erg,wvm,niv_PAN,niv_MULTI,interpol,precis,muestras,ponderacion);

    }

    void fihs() {
        if (files_multi.length == 1) {
            tratarImagenesMulti();
        } else {
            cargarImagenesMulti();
        }
        tratarImagenesPan();
        GeoImg[] res = {FFIHS.fusionFIHS(images_pan, images_multi, id)};
        manejoIMGS(res, 0);
    }

    @SuppressWarnings("unchecked")
    void ihs() {
        if (files_multi.length == 1) {
            tratarImagenesMulti();
        } else {
            cargarImagenesMulti();
        }
        tratarImagenesPan();
        GeoImg [] res = {IHS.fusionIHS(images_pan, images_multi, id)};
        
        manejoIMGS(res, 0);
    }

    void kmedias() {
        if (files_pan != null)
        {
            tratarImagenesPan();
        }
        KMeansAdapter kma = new KMeansAdapter(images_pan, dpanel, t, id, this);
        dpanel = kma.getDpanel();
    }

    void recortar() {
        DisplayJAIWithPixelInfo marco = getMarcoActivado();
        if (marco == null) {
            JOptionPane.showMessageDialog(t, 
                    ic.buscar(20), 
                    ic.buscar(21), 
                    JOptionPane.WARNING_MESSAGE); // Tag 20, 21
        } else {
            Rectangle rectSeleccion = marco.getSelectionRect();
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(marco.getSource());
            pb.add((float) rectSeleccion.x);
            pb.add((float) rectSeleccion.y);
            pb.add((float) rectSeleccion.width);
            pb.add((float) rectSeleccion.height);
            PlanarImage output = JAI.create("crop", pb, null);
            pb = new ParameterBlock();
            pb.addSource(output);
            pb.add((float) -rectSeleccion.x);
            pb.add((float) -rectSeleccion.y);
            // Create the output image by translating itself.
            output = JAI.create("translate", pb, null);
            // Tengo dos opciones


            /* En el rect�ngulo viene definido el nuevo tama�o de la imagen y, 
             * por tanto, porqu� queremos actualizar los metadatos*/

            /* Par�metros a actualizar: 
             * Altura: Tag 257 (SHORT/LONG)
             * Anchura: Tag 256 (SHORT/LONG)
             * ModelPixelScaleTag: 33550 (DOUBLE)
             * ModelTiepointTag: 22922 (DOUBLE) 
             */

            IIOMetadata imageDataMod = marco.getImageData(), streamDataMod = marco.getStreamData();
            GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(imageDataMod);
            ti.setTiffField(257, String.valueOf(rectSeleccion.height));
            ti.setTiffField(256, String.valueOf(rectSeleccion.width));
            // Aplanar tiepoints
            double[] tiepoints = ti.getModelTiePoints();
            // NOTA!!!! S�lo considero el caso de un tiepoint en 0,0

            tiepoints[3] = tiepoints[3] + rectSeleccion.x;
            tiepoints[4] = tiepoints[4] + rectSeleccion.y;
            for (int i = 0; i < tiepoints.length; i++) {

                System.out.println("Valores de tiepoints originales: " + tiepoints[i]);
                tiepoints[i] = tiepoints[i] + 2;
            }


            ti.setTiffDoubles(GeoTIFFTagSet.TAG_MODEL_TIE_POINT, tiepoints);

            String format = imageDataMod.getNativeMetadataFormatName();
            IIOMetadataNode root = ti.getRootNode();
            try {
                imageDataMod.setFromTree(format, root);
            } catch (IIOInvalidTreeException e1) {

                e1.printStackTrace();
            }

            double[] tiep = ti.getModelTiePoints();
            IIOMetadataNode nodo = ti.getTiffField(257);
            String alto = nodo.getAttributes().getNamedItem(GeoTiffIIOMetadataAdapter.VALUE_ATTR).getNodeValue();
            System.out.println("El valor de la altura es: " + alto);
            for (double d : tiep) {
                System.out.println("Valores de tiepoints modificados: " + d);
            }
            GeoData gd = Info.crearGeoData(ti);

            GeoImg[] gi = {new GeoImg(gd, 
                    output, 
                    new File(marco.archivo.getName() + " - Recortada"), 
                    streamDataMod, imageDataMod)}; // Tag 22

            manejoIMGS(gi, 0);




        }
    

    }

    /**
     * Obtiene las im�genes seleccionadas en la lista de im�genes y las carga en
     * unas variables intermedias para poder operar sobre ellas
     * @return true si hay im�genes seleccionadas, false en caso contrario
     */
    public boolean comprobarListaImagenes() {
        if (dpanel != null) {


            List<GeoImg> lim = dpanel.getListaImagenes().getImagenesMarcadas();
            if (lim.size() != 0) {
                if (images_pan == null) {
                    // Tengo que rellenar también files_pan
                    files_pan = new GeoImg[lim.size()];
                    lim.toArray(files_pan);
                    images_pan = lim;
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    void setImgs(List<GeoImg> imgs) {
    this.imgs = imgs;
    }

    private double pedirLH() {
        // Pedimos a manita la relaci�n L/H
        //DialogosAux da = new DialogosAux(this);
        Object[] message = da.crearMensaje(DialogosAux.DIAG_PEDIR_LH);
        String[] options = new String[2];
        options[0] = ic.buscar(23); // Tag 23
        options[1] = ic.buscar(24); // Tag 24
        boolean correcto = false;
        double ratio = 0;
        int resultado = 0;
        while (!correcto) {
            resultado = JOptionPane.showOptionDialog(t, message,
                    ic.buscar(25), 
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, options, options[0]); // Tag 25
            if (!((JTextField) message[1]).getText().equals("")) {
                correcto = true;
            }
            if (resultado == 1) {
                correcto = true;
            }
            if (correcto != true) {
                JOptionPane.showMessageDialog(t,
                        ic.buscar(26),
                        ic.buscar(21), 
                        JOptionPane.WARNING_MESSAGE); // Tag 26, 21
            }
        }
        if (resultado == 0) {

            ratio = Double.parseDouble(((JTextField) message[1]).getText());
        }
        return ratio;
    }

    /**
     * Establece el n�mero de muestras en que se dividir� en intervalo de
     * ponderaci�n a la hora de hallar la ponderaci�n �ptima
     * @param muestras2 N�mero de muestras
     */
    public void setMuestras(int muestras2) {

        muestras = muestras2;
    }

    /**
     * Halla la ponderaci�n �ptima para un conjunto de im�genes determinado
     * @param min Valor m�nimo de p�xel de las im�genes
     * @param max Valor m�ximo de p�xel de las im�genes
     * @param fast true para usar un algoritmo con menor procesamiento, false
     * en caso contrario
     */
    public void hallarPond(double min, double max, boolean fast) {
        Object[] message;
        String[] options;
        if (files_multi.length == 1) {
            tratarImagenesMulti();
        } else {
            cargarImagenesMulti();
        }
        tratarImagenesPan();
        int result;
        // Aqu� tengo que pasarle todas las bandas obtenidas de BIL o TIFF
        double l = images_pan.get(0).getData().getXScale();
        double h = images_multi.get(0).getData().getXScale();
        //if ((l == 0.0) || (h == 0.0)) {
            pedirLH();
        //}

        double ratio = l / h;
        erg.setAll(wvm, niv_PAN, niv_MULTI, interpol, precis, images_pan,
                images_multi, ponderacion, false, false, ratio);
        erg.setMinMax(min, max);
        double[] res = erg.muestreoErgas(muestras, t, fast);
        if (res == null) {
            return;
        }

        // Por separado (devuelve un array de alfas)
        String mensaje = new String("");
        for (int i = 0; i < res.length; i++) {
            mensaje = mensaje.concat(ic.buscar(27) 
                    + (i + 1) + " : " + res[i] + "\n"); // Tag 27
        }
        System.out.println(mensaje);
        message = da.crearMensaje(DialogosAux.DIAG_PONDSEP);
        ((JTextPane) message[0]).setText(mensaje);
        options = da.crearOpciones(DialogosAux.OPT_PONDSEP);
        result = JOptionPane.showOptionDialog(t, message, ic.buscar(28),
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]); // Tag 28
        switch (result) {
            case 0: {
                break;
            }
            case 1: {
                Fusion f = new Fusion(ic);
                GeoImg[] fusoptima = new GeoImg[images_multi.size()];

                for (int i = 0; i < images_multi.size(); i++) {

                    fusoptima[i] = f.atrous(images_pan.get(0), images_multi.get(i),
                            wvm, niv_PAN, niv_MULTI, interpol, precis, res[i]);
                }

                cerrarImagen();
                manejoIMGS(fusoptima, 0);

                break;
            }
            case 2: {
                JOptionPane.showMessageDialog(t, 
                        ic.buscar(29)); // Tag 29
                break;
            }

        }

    // ParseXML.crearOActualizar(files,res,ParseXML.POND,erg,wvm,niv_PAN,niv_MULTI,interpol,precis,muestras,ponderacion);

    }

    /**
     * Prepara las im�genes para la fusi�n � trous 
     */
    public void atrous() {

        if (files_multi.length == 1) {
            tratarImagenesMulti();
        } else {
            cargarImagenesMulti();
        }
        tratarImagenesPan();

        GeoImg[] melt = null;
        Fusion a = new Fusion(ic);

        melt = new GeoImg[images_multi.size()];

        for (int i = 0; i < images_multi.size(); i++) {

            melt[i] = a.atrous(images_pan.get(0), images_multi.get(i), wvm,
                    niv_PAN, niv_MULTI, interpol, precis, ponderacion);

            if (i == images_multi.size() - 1) {
                valorbarra = 100;
            } else {
                valorbarra = (i + 1) * (100 / files_multi.length);
            }
            updateAComponent.run();

        }

        manejoIMGS(melt, 0); // En DesktopPane
    }

    /**
     * Obtiene el selector de ficheros global (recuerda el �ltimo directorio)
     * @return Selector de ficheros global
     */
    public JFileChooser getFglobal() {

        return fglobal;
    }

    /**
     * Establece el selector de ficheros global (recuerda el �ltimo directorio)
     * @param jfc Selector de ficheros global
     */
    public static void setFglobal(JFileChooser jfc) {
        fglobal = jfc;
    }

    /**
     * Obtiene el objeto persistente Ergas
     * @return Ergas
     */
    public Ergas getErgasTocho() {

        return erg;
    }

    /**
     * Establece el objeto persistente Ergas
     * @param erg2 Ergas
     */
    public void setErgasTocho(Ergas erg2) {

        erg = erg2;
    }

    /**
     * Limpia la pantalla y elimina las referencias a las im�genes anteriormente
     * mostradas.
     */
    public void cerrarImagen() {

        Mostrar.cerrarImagen(t, dpanel, tpanel, status);

    }

    /**
     * Obtiene el marco principal de la aplicaci�n
     * @return El marco principal
     */
    public JFrame getT() {

        return t;
    }

    /**
     * Combina 3 im�genes para obtener una imagen RGB
     * @param vieneDeLista true si las im�genes se han seleccionado en la lista
     * de im�genes abiertas, false si se han seleccionado utilizando el di�logo de selecci�n
     * de ficheros
     */
    public void combinar(boolean vieneDeLista) {

    
        if (!vieneDeLista) {
            tratarImagenesPan();
        }

        int tam = 0;
        for (int i = 0; i < images_pan.size(); i++) {
            if (i == 0) {
                tam = images_pan.get(0).getImage().getWidth();
            } else {
                if (images_pan.get(i).getImage().getWidth() != tam) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(30)); // Tag 30
                    return;
                }

            }
        }
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(images_pan.get(0).getImage());
        pb.addSource(images_pan.get(1).getImage());
        pb.addSource(images_pan.get(2).getImage());

        GeoImg[] resultado = {new GeoImg(images_pan.get(0).getData(), 
                JAI.create("bandmerge", pb), 
                    new File("Combi" 
                    + images_pan.get(0).getFile().getName() 
                    + "&" + images_pan.get(1).getFile().getName() 
                    + "&" + images_pan.get(2).getFile().getName()),
                images_pan.get(0).getStreamMetadata(), 
                images_pan.get(0).getImageMetadata())}; 
        manejoIMGS(resultado, 0);

    }

    /**
     * Muestra los valores del �ndice ERGAS para estas im�genes, calculados
     * previamente y almacenados en un fichero XML
     * @param res Valores de ERGAS
     */
    public void mostrarErgasAlmacenado(double[] res) {
        JTextPane message = new JTextPane();
        String mensaje = new String("");
        for (int i = 0; i < res.length / 2; i++) {
            mensaje = mensaje.concat(ic.buscar(11) 
                    + (i + 1) + ": " + res[2 * i] 
                    + ic.buscar(12)
                    + res[2 * i + 1] + "\n"); // Tag 11, 12
        }
        message.setText(mensaje);
        JOptionPane.showMessageDialog(t, message,
                ic.buscar(31), 
                JOptionPane.PLAIN_MESSAGE); // Tag 31

    }

    /**
     * Obtiene el componente gr�fico de la barra de estado
     * @return La barra de estado
     */
    public JTextField getStatus() {
        return status;
    }

    /**
     * Muestra los valores de ponderaci�n �ptima para estas im�genes, calculados
     * previamente y almacenados en un fichero XML
     * @param pond_opt Valores de ponderaci�n �ptima
     */
    public void mostrarPondAlmacenada(double[] pond_opt) {
        JTextPane message = new JTextPane();
        String mensaje = new String("");
        for (int i = 0; i < pond_opt.length; i++) {
            mensaje = mensaje.concat(ic.buscar(27) 
                    + (i + 1) + ": " + pond_opt[i] + "\n"); // Tag 27
        }
        message.setText(mensaje);
        JOptionPane.showMessageDialog(t, message,
                ic.buscar(31), 
                JOptionPane.PLAIN_MESSAGE); // Tag 31

    }

    /**
     * Actualiza el valor interno de la barra de progreso con un porcentaje
     * @param i Porcentaje actual de progreso
     */
    public void setProgreso(int i) {
        valorbarra = i;

    }

    /**
     * Creaci�n de la interfaz de usuario
     */
    public static void createAndShowGUI() {


        // LOOK AND FEEL
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
            Dialogos.informarExcepcion(e);
            e.printStackTrace();
        }

        //UIManager.setLookAndFeel(Clase con otro look and feel);
        t = new JFrame("FIRST");
        Image imgIcon = null;
        try {
            // Poner un icono a la ventana
            imgIcon = javax.imageio.ImageIO.read(new File("resources/logoVentana.png"));
        } catch (IOException ex) {
            Logger.getLogger(Atrous.class.getName()).log(Level.SEVERE, null, ex);
        }
        t.setIconImage(imgIcon);
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu = new JMenuBar();

         archivo = new JMenu(ic.buscar(32)); // Tag 32
         visual = new JMenu(ic.buscar(33)); // Tag 33
         fus = new JMenu(ic.buscar(34)); // Tag 34
         indices = new JMenu(ic.buscar(184)); // Tag 184
         util = new JMenu(ic.buscar(35)); // Tag 35
         idioma = new JMenu(ic.buscar(166)); // Tag 166
         ventana = new JMenu(ic.buscar(178)); // Tag 178
         sobre = new JMenu(ic.buscar(36)); // Tag 36

         open = archivo.add(new JMenuItem(ic.buscar(37))); // Tag 37
        open.addActionListener(diag);

         save = archivo.add(new JMenuItem(ic.buscar(38))); // Tag 38
        save.addActionListener(diag);

         saveAll = archivo.add(new JMenuItem(ic.buscar(39))); // Tag 39
        saveAll.addActionListener(diag);

         exit = archivo.add(new JMenuItem(ic.buscar(40))); // Tag 40
        exit.addActionListener(diag);

         show = visual.add(new JMenuItem(ic.buscar(41))); // Tag 41
        show.addActionListener(diag);

         show2 = visual.add(new JMenuItem(ic.buscar(42))); // Tag 42
        show2.addActionListener(diag);

         close = visual.add(new JMenuItem(ic.buscar(43))); // Tag 43
        close.addActionListener(diag);

         combinar = visual.add(new JMenuItem(ic.buscar(44))); // Tag 44
        combinar.addActionListener(diag);
        // Men� Transformaci�n

         fusionar = fus.add(new JMenuItem(ic.buscar(45))); // Tag 45
        fusionar.addActionListener(diag);

         atrous = fus.add(new JMenuItem(ic.buscar(46))); // Tag 46
        atrous.addActionListener(diag);

        
        
        fihs = new JMenuItem(ic.buscar(47));
        Font defFont = fihs.getFont();
         fus.add(fihs); // Tag 47
        fihs.addActionListener(diag);
        fihs.setFont( new Font(defFont.getFamily(), Font.ITALIC, defFont.getSize() ));

        ihs = new JMenuItem(ic.buscar(48));
        fus.add(ihs); // Tag 48
        ihs.addActionListener(diag);
        ihs.setFont( new Font(defFont.getFamily(), Font.ITALIC, defFont.getSize() ));
        
        pca = new JMenuItem(ic.buscar(169));
        fus.add(pca);  // Tag 169
        pca.addActionListener(diag);
        pca.setFont( new Font(defFont.getFamily(), Font.ITALIC, defFont.getSize() ));
        
        brovey = new JMenuItem(ic.buscar(170));        
        fus.add(brovey);  // Tag 170
        brovey.addActionListener(diag);
        brovey.setFont( new Font(defFont.getFamily(), Font.ITALIC, defFont.getSize() ));
        
        respuesta = new JMenuItem(ic.buscar(171)); // Tag 171
        fus.add(respuesta);        
        respuesta.addActionListener(diag);
        respuesta.setFont( new Font(defFont.getFamily(), Font.ITALIC, defFont.getSize() ));
        
        mdmr = new JMenuItem(ic.buscar(172));
        fus.add(mdmr); // Tag 172
        mdmr.addActionListener(diag);
        mdmr.setFont( new Font(defFont.getFamily(), Font.ITALIC, defFont.getSize() ));
        
        zhou = new JMenuItem(ic.buscar(173));
        indices.add(zhou); // Tag 173
        zhou.addActionListener(diag);
        zhou.setFont( new Font(defFont.getFamily(), Font.ITALIC, defFont.getSize() ));

        boxcount = new JMenuItem(ic.buscar(174));
        util.add(boxcount); // Tag 174
        boxcount.addActionListener(diag);
        
         degradar = util.add(new JMenuItem(ic.buscar(49))); // Tag 49
        degradar.addActionListener(diag);

        //ergas = trans.add(new JMenuItem("C�lculo de ERGAS"));
        //ergas.addActionListener(diag);

         ergasespayafus = indices.add(new JMenuItem(
                ic.buscar(50))); // Tag 50
        ergasespayafus.addActionListener(diag);

         ergasespeyafus = indices.add(new JMenuItem(
                ic.buscar(51))); // Tag 51
        ergasespeyafus.addActionListener(diag);

         pond = indices.add(new JMenuItem(ic.buscar(52))); // Tag 52
        pond.addActionListener(diag);
        
        /*JMenuItem pond2 = fus.add(new JMenuItem("Calcular Pond R�pida"));
        pond2.addActionListener(diag);*/
        
        fusionporbanda = fus.add(new JMenuItem(ic.buscar(167))); // Tag 167
        fusionporbanda.addActionListener(diag);

         escala = util.add(new JMenuItem(ic.buscar(53))); // Tag 53
        escala.addActionListener(diag);

         and = util.add(new JMenuItem(ic.buscar(54))); // Tag 54
        and.addActionListener(diag);

        fourier = util.add(new JMenuItem(ic.buscar(85)));
        fourier.addActionListener(diag);
        /*invfourier = trans
        .add(new JMenuItem("Transformada inversa de Fourier"));
        invfourier.addActionListener(diag);*/

            actualizar = sobre.add(new JMenuItem(ic.buscar(175))); // Tag 175
            actualizar.addActionListener(diag);
        
         prueba = util.add(new JMenuItem(ic.buscar(55))); // Tag 55
        prueba.addActionListener(diag);

         recortar = util.add(new JMenuItem(ic.buscar(56))); // Tag 56
        recortar.addActionListener(diag);
        
         kmedias = util.add(new JMenuItem(ic.buscar(57))); // Tag 57
        kmedias.addActionListener(diag);
        
        // Men� idioma
        // Comprobar los ficheros de idioma en el directorio
        // Obtenemos los ficheros XML
        File curPath = new File(".");
        FilenameFilter ff = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("XML")||name.endsWith("xml"))
                {
                    return true;
                }
                else return false;
            };
        };
        String [] archivosXML = curPath.list(ff);
        bg = new ButtonGroup();
        idiomas = new ArrayList<Locale>();
        for (String arch: archivosXML)
        {
            System.out.println("Archivos XML: "+arch);
            if (Arrays.binarySearch(Locale.getISOLanguages(),arch.substring(0,2))>=0)
            { // Considerar v�lido y comprobar pa�s
                if (Arrays.binarySearch(Locale.getISOCountries(),arch.substring(3,5))>=0)
                { // Crear el men� de Idiomas
                    Locale lenguaje = new Locale (arch.substring(0,2),arch.substring(3,5));
                    idiomas.add(lenguaje);
                    JCheckBoxMenuItem item = new JCheckBoxMenuItem(lenguaje.getDisplayName());
                    item.setActionCommand("LANGUAGE_MENU");
                    item.addActionListener(diag);
                    bg.add(item);
                    
                    idioma.add(item);
                }
            }
            
        }
        
        mosaico = new JMenuItem(ic.buscar(179)); // Tag 179
        ventana.add(mosaico);
        mosaico.addActionListener(diag);
        
        cascada = new JMenuItem(ic.buscar(180)); // Tag 180
        ventana.add(cascada);
        cascada.addActionListener(diag);

        JMenuItem manual = new JMenuItem(ic.buscar(176)); // Tag 176
        sobre.add(manual);
        manual.addActionListener(diag);
        
        acerca = sobre.add(new JMenuItem(ic.buscar(58))); // Tag 58
        acerca.addActionListener(diag);

        menu.add(archivo);
        menu.add(visual);
        menu.add(fus);
        menu.add(util);
        menu.add(indices);
        menu.add(idioma);
        menu.add(ventana);
        menu.add(sobre);
        sur = new JPanel();
        sur.setLayout(new BoxLayout(sur, BoxLayout.LINE_AXIS));
        status = new JTextField("");
        status.setEditable(false);
        status.setSize(new Dimension(300, 30));
        // status.setVisible(true);
        sur.add(status);
        barraprogreso = new JProgressBar(0, 100);
        //barraprogreso.setStringPainted(true);
        barraprogreso.setString("0%");
        barraprogreso.setValue(0);
        sur.add(barraprogreso);

        updateAComponent = new RunnableFuture() {

            @Override
            public void run() {
                barraprogreso.setValue(valorbarra);
                barraprogreso.setStringPainted(true);
                barraprogreso.setString(String.valueOf(valorbarra) + "%");
                Graphics g = barraprogreso.getGraphics();
                barraprogreso.update(g);
            }

            @Override
            public boolean cancel(boolean arg0) {
                return false;
            }

            @Override
            public Object get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Object get(long arg0, TimeUnit arg1)
                    throws InterruptedException, ExecutionException,
                    TimeoutException {

                return null;
            }

            @Override
            public boolean isCancelled() {

                return false;
            }

            @Override
            public boolean isDone() {
                if (barraprogreso.getValue() == valorbarra) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        t.getContentPane().add(sur, BorderLayout.PAGE_END);
        t.setJMenuBar(menu);
        menu.setVisible(true);
        t.setResizable(true);
        t.pack();
        t.setLocation(0, 0);
        t.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {

                if (valorbarra != 0) {
                    barraprogreso.setIndeterminate(false);
                }
                barraprogreso.setValue(valorbarra);
            }
        });
        timer.start();
    }

    /**
     * Muestra las im�genes previamente cargadas en modo de pesta�as
     */
    public void show() {

        if (files_pan != null || files_multi != null) {

            GeoImg[] nuevas = null;
            if (bandas != 0) {
                try {
                    nuevas = Mostrar.mostrarImagen2(files_pan, bandas, lineas,
                            columnas, celdas);
                } catch (IOException ioe) {
                    Dialogos.informarExcepcion(ioe);
                }
            } else {
                nuevas = Mostrar.mostrarImagen(files_pan, leerSeparadas);
            }
            manejoIMGS(Arrays.asList(nuevas), 1);

        } else if (imgs != null) { // Cambiar de modo
            List<GeoImg> cambio = imgs;
            imgs = null;
            manejoIMGS(cambio, 1);
        } else {
            JOptionPane.showMessageDialog(t,
                    ic.buscar(59),
                    ic.buscar(21), JOptionPane.WARNING_MESSAGE); // Tag 59, 21
        }
    }

    /**
     * Muestra las im�genes previamente cargadas en modo de ventanas
     */
    public void show2() {

        if (files_pan != null || files_multi != null) {

            GeoImg[] nuevas = null;
            if (bandas != 0) {
                try {

                    nuevas = Mostrar.mostrarImagen2(files_pan, bandas, lineas,
                            columnas, celdas);
                } catch (IOException ioe) {
                    Dialogos.informarExcepcion(ioe);
                }
            } else {
                nuevas = Mostrar.mostrarImagen(files_pan, leerSeparadas);
            }
            manejoIMGS(Arrays.asList(nuevas), 0);


        } else if (imgs != null) { // Cambiar de modo
            List<GeoImg> cambio = imgs;
            imgs = null;
            manejoIMGS(cambio, 0);
        } else {
            JOptionPane.showMessageDialog(t,
                    ic.buscar(59),
                    ic.buscar(21), JOptionPane.WARNING_MESSAGE); // Tag 59, 21
        }

    }

    /**
     * Cierra todas las ventanas interiores y limpia todas las im�genes cargadas
     */
    public void close() {
        if (imgs != null) {
            imgs.clear();
            imgs = null;
        }
        if (images_pan != null) {
            images_pan.clear();
            images_pan = null;
        }
        if (images_multi != null) {
            images_multi.clear();
            images_multi = null;
        }
        //dpanel = null;
        tpanel = null;
        System.gc();
        Mostrar.cerrarImagen(t, dpanel, tpanel, status);


    }

    /**
     * Sale de la aplicaci�n
     */
    public void exit() {

        System.exit(0);

    }

    /**
     * Establece la profundidad de color a la que guardar la imagen
     * @param marcada Profundidad de color deseada
     */
    public void setProfundidad(int marcada) {
        profundidad = marcada;

    }

    /**
     * Obtener el thread actualizable que modifica concurrentemente los elementos
     * de la interfaz
     * @return Thread actualizable
     */
    public RunnableFuture getUpdate() {

        return updateAComponent;
    }

    /**
     * Calcula la transformada de Fourier de una imagen cargada
     */
    public void fourier() {
        tratarImagenesPan();
        Contour c = new Contour(this);
        GeoImg[] resultado = {c.fourier(images_pan)};
        setProgreso(100);
        manejoIMGS(resultado, 0);

    }

    /**
     * Calcula la transformada inversa de Fourier de una imagen cargada
     */
    public void invfourier() {
        tratarImagenesPan();
        GeoImg[] resultado = {Contour.invfourier(images_pan)};
        manejoIMGS(resultado, 0);

    }

    /**
     * Establece las im�genes cargadas como PAN en un array de GeoImg
     * @param imagesPan Im�genes cargadas
     */
    public void setFilesPan(GeoImg[] imagesPan) {
        files_pan = imagesPan;
    }

    /**
     * Establece que las im�genes multibanda se lean como im�genes separadas
     * @param separadas true si se carga cada banda en una imagen independiente,
     * false si se carga el fichero multibanda en una �nica imagen
     */
    public void setLeerSeparadas(boolean separadas) {
        leerSeparadas = separadas;

    }

    /**
     * Obtiene las im�genes cargadas como una lista de GeoImg
     * @return Im�genes cargadas
     */
    public List<GeoImg> getImagesPan() {
        return images_pan;
    }

    /**
     * Calcula el variograma de una imagen cargada
     * @param muestras N�mero de pasos en los que dividir la distancia m�xima
     * @param dist Distancia m�xima
     * @param tol Tolerancia Permitida
     * @param metros true para calcular el variograma en metros, false para
     * calcularlo en p�xeles
     */
    public void variograma(int muestras, double dist, double tol, boolean metros) {
        tratarImagenesPan();
        GeoImg imagen = images_pan.get(0);

        if (dpanel != null) {
            System.out.println("El n�mero de ventanas en el JDP es " + dpanel.getAllFrames().length);
        }

        double[][] datos = Variograma.crearVariograma(this, imagen, muestras, dist, tol);
        DefaultXYDataset ysds = new DefaultXYDataset();

        JFreeChart jfc = null;
        if ((imagen.getData() != null) && metros) {
            double facEscala = imagen.getData().getScales()[0];
            double[] valoresLagPixel = datos[0];
            double[] valoresLagMetros = new double[valoresLagPixel.length];
            for (int i = 0; i < valoresLagMetros.length; i++) {
                valoresLagMetros[i] = valoresLagPixel[i] * facEscala;
            }
            double[][] datosMetros = {valoresLagMetros, datos[1]};
            ysds.addSeries(ic.buscar(60), datosMetros); // Tag 60
            jfc = ChartFactory.createXYLineChart(ic.buscar(60), 
                    ic.buscar(61), 
                    ic.buscar(60), 
                    ysds, 
                    PlotOrientation.VERTICAL, 
                    true, 
                    true, 
                    false); // Tag 60, 61, 60
        } else {
            ysds.addSeries(ic.buscar(60), datos);
            jfc = ChartFactory.createXYLineChart(ic.buscar(60), 
                    ic.buscar(62), 
                    ic.buscar(60), 
                    ysds, 
                    PlotOrientation.VERTICAL, 
                    true, 
                    true, 
                    false); // Tag 60, 62, 60
        }

        ChartPanel cp = new ChartPanel(jfc);


        JInternalFrame jif = new JInternalFrame(
                ic.buscar(63) + imagen.getFile().getName(), 
                true, 
                true, 
                true, 
                true); // Tag 63
        jif.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        jif.getContentPane().add(cp);
        if (dpanel == null) {
            dpanel = new AreaTrabajo(this);


        }
        jif.addInternalFrameListener(dpanel);

        dpanel.add(jif);
        jif.setVisible(true);
        int desp = dpanel.getAllFrames().length;
        jif.setBounds(20 + desp * 10, 20 + desp * 10, 500, 600);
        jif.pack();


        dpanel.setVisible(true);
        // Queremos Scroll
        JScrollPane jsp = new JScrollPane(dpanel);
        
        t.add(jsp, BorderLayout.CENTER);
        t.setExtendedState(t.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        t.setVisible(true);
        if (dpanel.hayLista()) {
            dpanel.addToLista(jif.getTitle());
        } else {
            dpanel.addLista(null);
        }
    //t.repaint();
    }

    /**
     * Obtiene el AreaTrabajo principal de la aplicaci�n
     * @return AreaTrabajo principal
     */
    public AreaTrabajo getDPane() {

        return dpanel;
    }

    /**
     * Cambia de tama�o una imagen
     * @param img Imagen de entrada
     * @param b true si el cambio de tama�o es absoluto, false si es relativo
     * @param p Las dimensiones de la imagen si el cambio es absoluto, el factor
     * proporcional si es relativo
     * @param prec N�mero de bits de submuestra para los algoritmos de
     * interpolaci�n bic�bicos
     */
    public void escala(GeoImg img, boolean b, Point2D p, int prec) {

        if (img == null) { // se ha cargado
            tratarImagenesPan();
        } else { // ya est� cargada
            if (images_pan != null) {
                images_pan.clear();
            } else {
                images_pan = new ArrayList<GeoImg>();
            }
            images_pan.add(img);
        }
        float factorEscalaX, factorEscalaY;
        if (b) { // Absoluto
            factorEscalaX = (float) p.getX() / images_pan.get(0).getImage().getWidth();
            factorEscalaY = (float) p.getY() / images_pan.get(0).getImage().getHeight();
        } else {
            factorEscalaX = (float) p.getX();
            factorEscalaY = (float) p.getY();
        }
        GeoImg imgNueva = null;
        //Hay que corregir los datos geográficos
        IIOMetadata imageDataMod = images_pan.get(0).getImageMetadata(), streamDataMod = images_pan.get(0).getStreamMetadata();
        GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(imageDataMod);
        // Seguramente esto sea un poco innecesario
        ti.setTiffField(257, String.valueOf(factorEscalaX * images_pan.get(0).getImage().getWidth()));
        ti.setTiffField(256, String.valueOf(factorEscalaY * images_pan.get(0).getImage().getHeight()));
        // Hay que cambiar las Model Scales
        double[] escalas = images_pan.get(0).getData().getScales();

        double factorEscalaEscalasX = 1 / factorEscalaX;
        double factorEscalaEscalasY = 1 / factorEscalaY;


        escalas[0] = escalas[0] * factorEscalaEscalasX;
        escalas[1] = escalas[1] * factorEscalaEscalasY;

        ti.setTiffDoubles(GeoTIFFTagSet.TAG_MODEL_PIXEL_SCALE, escalas);

        String format = imageDataMod.getNativeMetadataFormatName();
        IIOMetadataNode root = ti.getRootNode();
        try {
            imageDataMod.setFromTree(format, root);
        } catch (IIOInvalidTreeException e1) {

            e1.printStackTrace();
        }
        double[] scales = ti.getModelPixelScales();
        for (double s : scales) {
            System.out.println("Las escalas modificadas son: " + s);
        }
        if (interpol != Fusion.LANCZOS) {
            RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
                    BorderExtender.createInstance(BorderExtender.BORDER_COPY));
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(images_pan.get(0).getImage());
            pb.add(factorEscalaX);
            pb.add(factorEscalaY);
            pb.add(0.0F);
            pb.add(0.0F);
            System.out.println("La interpolaci�n que se va a usar es " + interpol);
            switch (interpol) {
                case (Fusion.NEAR): {
                    pb.add(new InterpolationNearest());
                    break;
                }
                case (Fusion.BILIN): {
                    pb.add(new InterpolationBilinear());
                    break;
                }
                case (Fusion.BICUB): {
                    pb.add(new InterpolationBicubic(prec));
                    break;
                }
                case (Fusion.BICUB2): {
                    pb.add(new InterpolationBicubic2(prec));
                }
                }

            imgNueva = new GeoImg(images_pan.get(0).getData(), 
                    JAI.create("scale", pb, rh), 
                    new File(
                        images_pan.get(0).getFile().getName() + ic.buscar(64)), 
                        images_pan.get(0).getStreamMetadata(), 
                        images_pan.get(0).getImageMetadata()); // Tag 64


        } else {   //Interpolaci�n Lanczos
            imgNueva = new GeoImg(images_pan.get(0).getData(), 
                InterpolationLanczos.interpolar(images_pan.get(0).getImage(), 
                (int) (images_pan.get(0).getImage().getWidth() * factorEscalaX), 
                (int) (images_pan.get(0).getImage().getHeight() * factorEscalaY)), 
                images_pan.get(0).getFile(), 
                images_pan.get(0).getStreamMetadata(), 
                images_pan.get(0).getImageMetadata());
        }
        manejoIMGS(new GeoImg[]{imgNueva}, 0);

    }

    void calculator(int op) {
        if (files_multi.length == 1) {
            tratarImagenesMulti();
        } else {
            cargarImagenesMulti();
        }
        tratarImagenesPan();
        int tam = images_pan.get(0).getImage().getWidth();
        ParameterBlock pb = new ParameterBlock();
        for (int i = 0; i < images_multi.size(); i++) {
            if (images_multi.get(i).getImage().getWidth() != tam) {
                JOptionPane.showMessageDialog(t,
                        ic.buscar(30)); // Tag 30
                return;
            }
            pb.addSource(images_multi.get(i).getImage());
            double[] factor = {255.0};
            pb.add(factor);
            images_multi.get(i).setImage(JAI.create("dividebyconst", pb));
        }
        //Las im�genes est�n cargadas y son del mismo tama�o
        int cols = images_pan.get(0).getImage().getWidth();
        int lins = images_pan.get(0).getImage().getHeight();
        Raster data = images_pan.get(0).getImage().getData();
        int transfer = data.getTransferType();
        byte pixeles_byte[] = null;
        short pixeles_short[] = null;
        int pixeles_int[] = null;


        if (transfer == DataBuffer.TYPE_BYTE) {
            pixeles_byte = new byte[cols * lins];
            data.getDataElements(0, 0, cols, lins, pixeles_byte);
        // Las m�scaras siempre ser�n de byte, la cargo

        } else if (transfer == DataBuffer.TYPE_USHORT) {
            pixeles_short = new short[cols * lins];
            data.getDataElements(0, 0, cols, lins, pixeles_short);

        } else if (transfer == DataBuffer.TYPE_INT) {
            pixeles_int = new int[cols * lins];
            data.getDataElements(0, 0, cols, lins, pixeles_int);
        }
        GeoImg[] resultados = new GeoImg[images_multi.size()];
        for (int i = 0; i < images_multi.size(); i++) {
            byte pixeles_mascara[] = new byte[cols * lins];

            Raster dataMask = images_multi.get(i).getImage().getData();

            dataMask.getDataElements(0, 0, cols, lins, pixeles_mascara);

            switch (op) {
                case Dialogos.OP_AND: {
                    /* Recorremos el array de la m�scara y vamos poniendo en 
                     * el array original un 0 si hay 0 en la m�scara o dejamos
                     * el contenido e.o.c. */
                    for (int j = 0; j < cols * lins; j++) {
                        if (transfer == DataBuffer.TYPE_BYTE) {
                            if (pixeles_mascara[j] == (byte) 0) {
                                pixeles_byte[j] = (byte) 0;
                            }
                        } else if (transfer == DataBuffer.TYPE_USHORT) {
                            if (pixeles_mascara[j] == (byte) 0) {
                                pixeles_short[j] = (short) 0;
                            }

                        } else if (transfer == DataBuffer.TYPE_INT) {
                            if (pixeles_mascara[j] == (byte) 0) {
                                pixeles_int[j] =  0;
                            }
                        }
                    }
                    TiledImage tiled = null;
                    if (transfer == DataBuffer.TYPE_BYTE) {
                        DataBufferByte dbuffer = new DataBufferByte(pixeles_byte, pixeles_byte.length);
                        SampleModel samplemod = RasterFactory.createBandedSampleModel(
                                DataBuffer.TYPE_BYTE, cols, lins, 1);
                        ColorModel colormod = PlanarImage.createColorModel(samplemod);
                        Raster raster = RasterFactory.createWritableRaster(samplemod,
                                dbuffer, new Point(0, 0));
                        tiled = new TiledImage(0, 0, cols, lins, 0, 0,
                                samplemod, colormod);
                        tiled.setData(raster);

                    } else if (transfer == DataBuffer.TYPE_USHORT) {
                        DataBufferUShort dbuffer = new DataBufferUShort(pixeles_short, pixeles_short.length);
                        SampleModel samplemod = RasterFactory.createBandedSampleModel(
                                DataBuffer.TYPE_USHORT, cols, lins, 1);
                        ColorModel colormod = PlanarImage.createColorModel(samplemod);
                        Raster raster = RasterFactory.createWritableRaster(samplemod,
                                dbuffer, new Point(0, 0));
                        tiled = new TiledImage(0, 0, cols, lins, 0, 0,
                                samplemod, colormod);
                        tiled.setData(raster);

                    } else if (transfer == DataBuffer.TYPE_INT) {
                        DataBufferInt dbuffer = new DataBufferInt(pixeles_int, pixeles_int.length);
                        SampleModel samplemod = RasterFactory.createBandedSampleModel(
                                DataBuffer.TYPE_INT, cols, lins, 1);
                        ColorModel colormod = PlanarImage.createColorModel(samplemod);
                        Raster raster = RasterFactory.createWritableRaster(samplemod,
                                dbuffer, new Point(0, 0));
                        tiled = new TiledImage(0, 0, cols, lins, 0, 0,
                                samplemod, colormod);
                        tiled.setData(raster);
                    }
                    resultados[i] = new GeoImg(images_pan.get(0).getData(), tiled, images_pan.get(0).getFile(), images_pan.get(0).getStreamMetadata(), images_pan.get(0).getImageMetadata());
                }
                }
        }
        //

        /* TODELETE pb = new ParameterBlock();
        for (int i = 0; i < images_multi.size(); i++)
        {
        pb.addSource(images_pan.get(0).getImage());
        pb.addSource(images_multi.get(i).getImage());
        resultados[i] = new GeoImg(images_pan.get(0).getData(),JAI.create("multiply",pb),images_pan.get(0).getFile(),images_pan.get(0).getStreamMetadata(), images_pan.get(0).getImageMetadata());
        }*/
        manejoIMGS(resultados, 0);
    }
}
/**
 * 
 * Clase principal de la aplicaci�n
 * @author Alvar Garc�a del R�o
 *
 */
//class Main {

    /**
     * Crea el objeto de interfaz
     * @param args Par�metros de l�nea de comandos que se le pueden pasar a la aplicaci�n
     */
  //  public static void main(String[] args) {
        // Cargar las DLL's, para no tener que instalarlas en el JRE

        //System.out.println("Este es el directorio actual"+System.getProperty("user.dir"));
        //String cwd = Main.class.getClassLoader().getResource(".").getPath();
        //Quitarle el slash inicial
        //cwd = cwd.substring(1);
        //Cambiar los slashes
        //cwd = cwd.replace('/','\\');
        //System.out.println(cwd);
        //System.out.println(Main.class.getClassLoader().getResource(".").getPath());
        //String s = System.getProperty("user.dir");
        //System.out.println(cwd+"clib_jiio.dll");
        //System.load(cwd+"clib_jiio.dll");
        //System.load(cwd+"clib_jiio_sse2.dll");
        //System.load(cwd+"clib_jiio_util.dll");
        //System.load(cwd+"mlib_jai.dll");
        //System.load(cwd+"mlib_jai_mmx.dll");
        //System.load(cwd+"mlib_jai_util.dll");
 //       Atrous a = new Atrous();
 //       javax.swing.SwingUtilities.invokeLater(new Runnable() {

   //         @Override
   //         public void run() {
   //             Atrous.createAndShowGUI();
  //          }
   //     });

  //  }
//}
