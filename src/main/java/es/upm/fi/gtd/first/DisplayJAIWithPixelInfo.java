/*
 * Created on May 22, 2005
 * @author Rafael Santos (rafael.santos@lac.inpe.br)
 * 
 * Part of the Java Advanced Imaging Stuff site
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
 * 
 * STATUS: Complete.
 * 
 * Redistribution and usage conditions must be done under the
 * Creative Commons license:
 * English: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.en
 * Portuguese: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.pt
 * More information on design and applications are on the projects' page
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI).
 */
package es.upm.fi.gtd.first;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.metadata.IIOMetadata;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.widget.DisplayJAI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;

/**
 * This class shows how one can extend the DisplayJAI class. We'll override the mouseMoved method of DisplayJAI so when the mouse is moved, some information about the pixel beneath the mouse will be stored (but not displayed).
 * @author  Rafael Santos
 * @author  Alvar García del Río
 */
abstract public class DisplayJAIWithPixelInfo extends DisplayJAI implements MouseWheelListener, KeyListener, MouseListener {

    //private double[] dpixel_o; // the pixel information as an array of doubles.
    //private int[] ipixel_o; // the pixel information as an array of integers.
    /**
     * JComboBox para elegir el tipo de wavelet
     */
    private JComboBox cbwavelet;
    /**
     * Valor de wavelet escogido
     */
    private int wvm;
    /**
     * JFrame principal de la aplicación
     */
    private JFrame t;
    /**
     * Objeto principal de la aplicación
     */
    private Atrous principal;
    /**
     * Objeto auxiliar para la creación de diálogos
     */
    private DialogosAux da;
    /**
     * Lista de imágenes elegidas como multiespectrales para fusión
     */
    protected List<GeoImg> images_multi;
    /**
     * Valor de bandas en una imagen BIL
     */
    private int bandas;
    /**
     * Valor de lineas en una imagen BIL
     */
    private int lineas;
    /**
     * Valor de columnas en una imagen BIL
     */
    private int columnas;
    /**
     * Valor de celdas en una imagen BIL
     */
    private int celdas;
    /**
     * Lista de imágenes elegidas como pancromáticas para fusión
     */
    List<GeoImg> images_pan;
    /**
     * Datos geográficos de la imagen contenida en este objeto
     */
    private GeoData gdata;
    /**
     * Metadatos correspondientes al archivo original de la imagen contenida
     * en este objeto
     */
    protected IIOMetadata streamData;
    /**
     * Metadatos correspondientes a la imagen contenida en este objeto
     */
    protected IIOMetadata imageData;
    /**
     * true si se ha pulsado la Hotkey de zoom (Ctrl)
     */
    protected boolean zooming;
    /**
     * Escala actual a la que se muestra la imagen por pantalla
     */
    protected int escala;
    /**
     * Imagen "original" adaptada a un DisplayJAI
     */
    protected PlanarImage surrogateImage;
    /**
     * Archivo del que procede la imagen contenida en este objeto o un 
     * archivo creado con el nombre que se desea que aparezca en el título
     * del JInternalFrame por pantalla
     */
    protected File archivo;
    /**
     * Imagen cargada con el cursor deseado cuando se pulsa la Hotkey de 
     * zoom (Ctrl)
     */
    protected PlanarImage imagenCursor;
    /**
     * El Cursor para el modo zoom
     */
    protected Cursor cursorZoom;
    /**
     * true si este objeto está contenido en un JDesktopPane
     * false si este objeto está contenido en un JTabbedPane
     */
    protected boolean inDPane;
    /**
     * Rectángulo que representa la selección que se usa para recortar imágenes
     */
    protected Rectangle rectSelection;
    /**
     * Tipo de línea para dibujar el rectángulo de selección
     */
    protected BasicStroke bs;
    /**
     * Color que se le aplica a la línea del rectángulo de selección
     */
    protected GradientPaint gp;
    /**
     * Coordenada X de la imagen origen
     */
    /**
     * Coordenada Y de la imagen origen
     */
    /**
     * Coordenada X de la imagen destino
     */
    /**
     * Coordenada Y de la imagen destino
     */
    protected int srcX,  srcY,  destX,  destY;
    /**
     * Puntos base de la información geográfica
     */
    protected double[][] tiepoints;
    /**
     * Objeto de datos de traducción actual
     */
    protected ParseXML ic;
    /**
     * Imagen original que se mantiene al usar una distinta para mostrar
     */
    protected RenderedImage imagen_original;

    /**
     * Constructor de la clase
     * @param image Imagen original
     * @param field JTextField donde se mostrarán datos de interés
     * @param topitopi JFrame principal de la aplicación
     * @param princ Objeto principal de la aplicación
     * @param data Datos geográficos
     * @param metadata Metadatos de archivo
     * @param metadata2 Metadatos de imagen
     * @param dPane ¿Está en un JDesktopPane?
     * @param file Archivo original de la imagen o construido
     */
    public DisplayJAIWithPixelInfo(RenderedImage image, JTextField field,
            JFrame topitopi, Atrous princ, GeoData data, IIOMetadata metadata,
            IIOMetadata metadata2, boolean dPane, File file) {
        super(image); // calls the constructor for DisplayJAI.

        principal = princ;
        ic = principal.getIC();
        da = new DialogosAux(principal);
        gdata = data;
        streamData = metadata;
        imageData = metadata2;
        inDPane = dPane;
        archivo = file;
        imagen_original = image;
        imagenCursor = JAI.create("imageread", "resources/lupitatrans.png");
        Toolkit a = Toolkit.getDefaultToolkit();
        cursorZoom = a.createCustomCursor(imagenCursor.getAsBufferedImage(), new Point(0, 0), "zoom");
        rectSelection = new Rectangle();
        bs = new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
        gp = new GradientPaint(0.0f, 0.0f, Color.red, 1.0f, 1.0f, Color.white,
                true);

        this.addMouseListener(this);
        this.addKeyListener(this);
        this.addMouseWheelListener(this);
        this.setFocusable(true);
        images_pan = new ArrayList<GeoImg>();
        //images_multi = new ArrayList<GeoImg>();
        zooming = false;
        escala = 0;

    }

    /**
     * Obtiene los puntos base de la información geográfica
     * @return Los puntos base como un array de arrays de double
     */
    public abstract double[][] getTiePoints();

    /**
     * Obtiene el rectángulo de selección sobre la imagen
     * @return El rectángulo de selcción
     */
    public Rectangle getSelectionRect() {
        if (srcX != destX || srcY != destY) {
            return rectSelection;
        } else {
            return null;
        }
    }

    /*
     * EVENTOS DE TECLADO Y RATÓN 
     */
    /**
     * Inicia el modo de selección de cuadrante (borra selección anterior)
     * @param arg0 
     */
    @Override
    public void mousePressed(MouseEvent arg0) {
        //super.mousePressed(arg0);
        destX = srcX = arg0.getX();

        destY = srcY = arg0.getY();

        repaint();
    }

    /**
     * Actualiza el rectángulo de selección
     * @param arg0
     */
    @Override
    public void mouseDragged(MouseEvent arg0) {
        super.mouseDragged(arg0);
        destX = arg0.getX();
        destY = arg0.getY();


        repaint();
    }

    /**
     * Pinta el rectángulo
     * 
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (srcX != destX || srcY != destY) {
            // Compute upper-left and lower-right coordinates for selection
            // rectangle corners.


            int x1 = (srcX < destX) ? srcX : destX;
            int y1 = (srcY < destY) ? srcY : destY;


            int x2 = (srcX > destX) ? srcX : destX;
            int y2 = (srcY > destY) ? srcY : destY;


            // Establish selection rectangle origin.


            rectSelection.x = x1;
            rectSelection.y = y1;


            // Establish selection rectangle extents.


            rectSelection.width = (x2 - x1) + 1;
            rectSelection.height = (y2 - y1) + 1;


            // Draw selection rectangle.


            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(bs);
            g2d.setPaint(gp);
            g2d.draw(rectSelection);
        }
    }

    /**
     * Activa el modo zoom si se ha presionado la tecla Ctrl
     * @param arg0 Evento recibido
     */
    @Override
    public void keyPressed(KeyEvent arg0) {

        if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
            this.setCursor(cursorZoom);
            zooming = true;
        }

    }

    /**
     * Desactiva el modo zoom si se suelta la tecla Ctrl
     * @param arg0 Evento recibido
     */
    @Override
    public void keyReleased(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
            this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            zooming = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    

    }

    /**
     * Modifica el tamaño de la imagen en pantalla mediante el movimiento de la
     * rueda del ratón
     * @param arg0 Evento recibido
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
        // Intento conseguir el JInternalFrame para marcar el 
        // nivel de zoom
        JRootPane jrp = this.getRootPane();
        JInternalFrame jif = null;
        JTabbedPane jtp = null;
        JScrollPane jsp = null;
        if (inDPane) {
            jif = (JInternalFrame) jrp.getParent(); // JInternalFrame
        } else {
            JViewport jv = (JViewport) this.getParent();
            jsp = (JScrollPane) jv.getParent();
            jtp = (JTabbedPane) jsp.getParent();
        }
        if (zooming) {

            int fac_escala = -arg0.getWheelRotation();
            escala += fac_escala;
            if (inDPane) {
                if (escala >= 0) {
                    jif.setTitle(archivo.getName() + " - Zoom " + (100 + escala * 100) + "%");
                } else {
                    jif.setTitle(archivo.getName() + " - Zoom " + (Math.pow(2, escala) * 100) + "%");
                }
            } else {

                if (escala >= 0) {
                    jtp.setTitleAt(jtp.indexOfComponent(jsp), archivo.getName() + " - Zoom " + (100 + escala * 100) + "%");
                } else {
                    jtp.setTitleAt(jtp.indexOfComponent(jsp), archivo.getName() + " - Zoom " + (Math.pow(2, escala) * 100) + "%");
                }
            }
            if (escala == 0) { // hemos vuelto a la imagen original, la cargamos desde
                // imagen
                this.set(surrogateImage);
            } else {
                float factor_escala = 0;
                factor_escala = (float) Math.pow(2, fac_escala);
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(this.source);
                pb.add(factor_escala);
                pb.add(factor_escala);
                pb.add(0.0f);
                pb.add(0.0f);
                pb.add(new InterpolationBilinear());
                RenderedImage ri = JAI.create("scale", pb);
                this.set(ri);
            }
        }

    }

    /**
     * @deprecated Calcula el ERGAS fusionando esta imagen como multiespectral
     */
    @Deprecated
    protected void ergas2() {
        JFileChooser fglobal = principal.getFglobal();

        // Permito la selección de varias bandas
        fglobal.setDialogTitle(ic.buscar(123)); // Tag 123
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setMultiSelectionEnabled(false);
        int retval = fglobal.showDialog(t, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File[] files_pan = {fglobal.getSelectedFile()};
            // Pido la wavelet que se utilizará
            Object[] message = da.crearMensaje(DialogosAux.DIAG_ERGAS);
            String[] options = da.crearOpciones(DialogosAux.OPT_ERGAS);
            int result = 0;
            boolean correcto = false;
            while (!correcto) {

                result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]); // Tag 101

                if (!((JTextField) message[10]).getText().equals("") && !((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {// Está todo rellenado
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true;
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21
                }
            }
            if (result == 0) {
                //Ahora llamo al nuevo procedimiento de Fusion, llamado fusionar
                //Ya que pido los niveles, tengo que pasárselos a fusionar			    							    				
                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());
                boolean separado = ((JCheckBox) message[11]).isSelected();
                double ponderacion = Double.parseDouble(((JTextField) message[10]).getText());
                int precis = 0;
                int interpol = principal.getInterpol();
                if (interpol == 2) {
                    precis = Integer.parseInt(((JTextField) message[8]).getText());
                }
                if (((JComboBox) message[1]).getSelectedItem().toString().equals("b3spline")) {
                    wvm = 6;
                }
                Ergas erg = principal.getErgasTocho();

                images_multi.add(new GeoImg(gdata, PlanarImage.wrapRenderedImage(this.source), new File(""), streamData, imageData));
                tratarImagenesPan(files_pan);
                double l = images_pan.get(0).getData().getXScale();
                double h = images_multi.get(0).getData().getXScale();
                double ratio = 0;
                if ((l == 0.0) || (h == 0.0)) {
                    ratio = pedirLH();
                } else {
                    ratio = l / h;
                }
                erg.setAll(wvm, niv_PAN, niv_MULTI, interpol, precis, images_pan, images_multi, ponderacion, separado, false, ratio);
                double[] res = erg.muestreoErgas(1, t, false);
                JTextPane resultados = new JTextPane();
                if (separado) {
                    String mensaje = new String("");
                    for (int i = 0; i < res.length / 2; i++) {
                        mensaje = mensaje.concat(
                                ic.buscar(11) + (i + 1) + ": " + res[2 * i] + ic.buscar(12) + res[2 * i + 1] + "\n"); // Tag 11, 12
                    }
                    resultados.setText(mensaje);
                    JOptionPane.showMessageDialog(t,
                            resultados,
                            ic.buscar(13),
                            JOptionPane.PLAIN_MESSAGE); // Tag 13
                } else {
                    resultados.setText(ic.buscar(14) + res[0] + "\n" +
                            ic.buscar(15) + res[1] + "\n" +
                            ic.buscar(16) + res[2] + "\n" +
                            ic.buscar(17) + res[3] + "\n" +
                            ic.buscar(18) + res[4] + "\n" +
                            ic.buscar(19) + Math.abs(res[0] - res[2])); // Tag 14, 15, 16, 17, 18, 19
                    JOptionPane.showMessageDialog(t, resultados, ic.buscar(124), JOptionPane.PLAIN_MESSAGE); // Tag 124
                }
            } else {
                JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
            }
        }
    }

    /**
     * @deprecated Calcula el ERGAS fusionando esta imagen como pancromática
     */
    @Deprecated
    protected void ergas1() {

        JFileChooser fglobal = principal.getFglobal();

        // Permito la selección de varias bandas
        fglobal.setDialogTitle(ic.buscar(104)); // Tag 104
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setMultiSelectionEnabled(true);
        int retval = fglobal.showDialog(t, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File[] files_multi = fglobal.getSelectedFiles();
            // Pido la wavelet que se utilizará
            Object[] message = da.crearMensaje(DialogosAux.DIAG_ERGAS);
            String[] options = da.crearOpciones(DialogosAux.OPT_ERGAS);
            int result = 0;
            boolean correcto = false;
            while (!correcto) {
                result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]); // Tag 101
                if (!((JTextField) message[10]).getText().equals("") && !((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {// Está todo rellenado
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true;
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21
                }
            }
            if (result == 0) {
                //Ahora llamo al nuevo procedimiento de Fusion, llamado fusionar
                //Ya que pido los niveles, tengo que pasárselos a fusionar			    							    				
                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());
                boolean separado = ((JCheckBox) message[11]).isSelected();
                double ponderacion = Double.parseDouble(((JTextField) message[10]).getText());
                int precis = 0;
                int interpol = principal.getInterpol();
                if (interpol == 2) {
                    precis = Integer.parseInt(((JTextField) message[8]).getText());
                }
                if (((JComboBox) message[1]).getSelectedItem().toString().equals("b3spline")) {
                    wvm = 6;
                }
//					 En el caso de que se haya iniciado el cálculo desde botón derecho
				/*	File fil = new File("ergas.xml");
                if (fil.exists())
                {
                ParseXML pxml = new ParseXML("ergas.xml");
                double res [] = new double[2*(multis.length)];
                boolean noenc = true;
                int files_cont = 0;
                try {
                for (int i=0; i < res.length; i++)
                {
                if (i%2!=0)
                {						
                res[i] = Double.parseDouble(pxml.buscar(ParseXML.digest(multis[files_cont]),"ERGAS_espacial"));
                if (res[i]!=0.0)
                {
                noenc = false;
                }
                files_cont++;
                }
                else
                {
                res[i] = Double.parseDouble(pxml.buscar(ParseXML.digest(multis[files_cont]),"ERGAS_espectral"));
                if (res[i]!=0.0)
                {
                noenc = false;
                }
                }
                }
                } catch (Exception e) {
                Dialogos.informarExcepcion(e);
                }
                if (!noenc)
                {
                principal.mostrarErgasAlmacenado(res);
                return;
                }
                }
                 */
                Ergas erg = principal.getErgasTocho();
                images_pan.add(new GeoImg(gdata,
                        PlanarImage.wrapRenderedImage(this.source),
                        new File(""),
                        streamData,
                        imageData));
                if (files_multi.length == 1) {
                    tratarImagenesMulti(files_multi);
                } else {
                    cargarImagenesMulti(files_multi);
                }
                double l = images_pan.get(0).getData().getXScale();
                double h = images_multi.get(0).getData().getXScale();
                double ratio = 0;
                if ((l == 0.0) || (h == 0.0)) {
                    ratio = pedirLH();
                } else {
                    ratio = l / h;
                }
                erg.setAll(wvm, niv_PAN, niv_MULTI, interpol, precis, images_pan, images_multi, ponderacion, separado, false, ratio);
                double[] res = erg.muestreoErgas(1, t, false);
                JTextPane resultados = new JTextPane();
                if (separado) {
                    String mensaje = new String("");
                    for (int i = 0; i < res.length / 2; i++) {
                        mensaje = mensaje.concat(
                                ic.buscar(11) + (i + 1) + ": " + res[2 * i] + ic.buscar(12) + res[2 * i + 1] + "\n"); // Tag 11, 12
                    }
                    resultados.setText(mensaje);
                    JOptionPane.showMessageDialog(t,
                            resultados,
                            ic.buscar(13),
                            JOptionPane.PLAIN_MESSAGE); // Tag 13
                } else {

                    resultados.setText(ic.buscar(14) + res[0] + "\n" + ic.buscar(15) + res[1] + "\n" + ic.buscar(16) + res[2] + "\n" + ic.buscar(17) + res[3] + "\n" + ic.buscar(18) + res[4] + "\n" + ic.buscar(19) + Math.abs(res[0] - res[2])); // Tag 14, 15, 16, 17, 18, 19
                    JOptionPane.showMessageDialog(t,
                            resultados,
                            ic.buscar(13),
                            JOptionPane.PLAIN_MESSAGE); // Tag 13
                }


            } else {
                JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
            }

        }
    }

    /**
     * Calcula la ponderación (alfa) óptima de una fusión realizada con la 
     * imagen contenida en este objeto utilizada como multiespectral
     */
    protected void pond2() {


        JFileChooser fglobal = principal.getFglobal();
        fglobal.setDialogTitle(ic.buscar(123)); // Tag 123
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setMultiSelectionEnabled(false);
        int retval = fglobal.showDialog(t, null);
        int result = 0;
        if (retval == JFileChooser.APPROVE_OPTION) {
            File[] files_pan = {fglobal.getSelectedFile()};
            // Pido la wavelet que se utilizará
            Object [] message = da.crearMensaje(DialogosAux.DIAG_POND);
            String [] options = da.crearOpciones(DialogosAux.OPT_FUSIONAR);
            boolean correcto = false;
            while (!correcto) {

                result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]); // Tag 101
                if (!((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {// Está todo rellenado
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true;
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21

                }
            }
            if (result == 0) {
                //Ahora llamo al nuevo procedimiento de Fusion, llamado fusionar
                //Ya que pido los niveles, tengo que pasárselos a fusionar			    							    				
                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());


                int precis = 0;
                int interpol = principal.getInterpol();
                if (interpol == 2) {
                    precis = Integer.parseInt(((JTextField) message[8]).getText());


                }


                double minPond = Double.parseDouble (((JTextField) message[10]).getText());
                double maxPond = Double.parseDouble (((JTextField) message[11]).getText());
                
                cbwavelet = (JComboBox) message[1];
                if (cbwavelet.getSelectedItem().toString().equals("b3spline")) {
                    wvm = 6;

                }
                int muestras = Integer.parseInt(((JTextField)message[13]).getText());
                Ergas erg = new Ergas(principal);
                
                if (images_multi == null)
                {
                    images_multi = new ArrayList<GeoImg>();
                }
                images_multi.clear();
                images_multi.add(new GeoImg(gdata,
                        PlanarImage.wrapRenderedImage(imagen_original),
                        archivo,
                        streamData,
                        imageData));
                tratarImagenesPan(files_pan);
                double l = images_pan.get(0).getData().getXScale();
                double h = images_multi.get(0).getData().getXScale();
                double ratio = 0;
                if ((l == 0.0) || (h == 0.0)) {
                    ratio = pedirLH();
                } else {
                    ratio = l / h;
                }
                erg.setAll(wvm, niv_PAN, niv_MULTI, interpol, precis, images_pan, images_multi, 0.0, true, false, ratio);
                erg.setMinMax(minPond, maxPond);
                principal.cerrarImagen();
                double[] res = erg.muestreoErgas(muestras, principal.getT(), false);
                principal.setErgasTocho(erg);
                if (res == null) {
                    return;
                }
                // Por separado (devuelve un array de alfas)
                String mensaje = new String("");
                for (int i = 0; i < res.length; i++) {
                    mensaje = mensaje.concat(ic.buscar(27) + (i + 1) + " : " + res[i] + "\n"); // Tag 27
                }
                System.out.println(mensaje);
                message = da.crearMensaje(DialogosAux.DIAG_PONDSEP);
                ((JTextPane) message[0]).setText(mensaje);
                options = da.crearOpciones(DialogosAux.OPT_PONDSEP);
                result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(28),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]); // Tag 28
                switch (result) {
                    case 0: {
                        return;
                    }
                    case 1: {
                        Fusion f = new Fusion(ic);
                        GeoImg[] fusoptima = new GeoImg[1];



                        for (int i = 0; i < fusoptima.length; i++) {


                            fusoptima[i] = f.atrous(images_pan.get(0), images_multi.get(i), wvm, niv_PAN, niv_MULTI, interpol, precis, res[0]);
                        }



                        principal.manejoIMGS(fusoptima, 0);

                        break;

                    }
                    case 2: {
                        JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
                        break;
                    }

                }


            } else {
                JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
            }



        } else {
            dibujarMensajesError(retval);

        }


    }

    /**
     * Dibuja los errores que pueden haber ocurrido en el diálogo de selección
     * de ficheros
     * @param retval Resultado devuelto por el diálogo de selección
     * @return 1 en caso de Cancelar, 2 en caso de un error, 3 en otro caso
     */
    protected int dibujarMensajesError(int retval) {
        if (retval == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(t,
                    ic.buscar(90)); // Tag 90
            return 1;
        } else if (retval == JFileChooser.ERROR_OPTION) {
            JOptionPane.showMessageDialog(t, ic.buscar(91), ic.buscar(7), JOptionPane.ERROR_MESSAGE); // Tag 91, 7
            return 2;
        } else {
            JOptionPane.showMessageDialog(t, ic.buscar(91), ic.buscar(7), JOptionPane.ERROR_MESSAGE);
            return 3;
        }

    }

    /**
     * Calcula la ponderación (alfa) óptima para una fusión realizada con 
     * la imagen contenida en este objeto utilizada como pancromática
     */
    protected void pond1() {
//		 Mostrar la cajita que pedirá los valores


        JFileChooser fglobal = principal.getFglobal();
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setDialogTitle(ic.buscar(104)); // Tag 104
        fglobal.setMultiSelectionEnabled(true);
        int retval = fglobal.showDialog(t, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File[] files_multi = fglobal.getSelectedFiles();
            // Pido la wavelet que se utilizará
            Object  [] message = da.crearMensaje(DialogosAux.DIAG_POND);
            String [] options = da.crearOpciones(DialogosAux.OPT_FUSIONAR);
            int result = 0;

            boolean correcto = false;
            while (!correcto) {

                result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]); // Tag 101
                if (!((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {// Está todo rellenado
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true;
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21
                }
            }
            if (result == 0) {
                //Ahora llamo al nuevo procedimiento de Fusion, llamado fusionar
                //Ya que pido los niveles, tengo que pasárselos a fusionar			    							    				
                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());


                int precis = 0;
                int interpol = principal.getInterpol();
                if (interpol == 2) {
                    precis = Integer.parseInt(((JTextField) message[8]).getText());


                }
                double minPond = Double.parseDouble (((JTextField) message[10]).getText());
                double maxPond = Double.parseDouble (((JTextField) message[11]).getText());
                
                cbwavelet = (JComboBox) message[1];
                if (cbwavelet.getSelectedItem().toString().equals("b3spline")) {
                    wvm = 6;

                }
                int muestras = Integer.parseInt(((JTextField)message[13]).getText());
                Ergas erg = new Ergas(principal);
                //erg = principal.getErgasTocho();
                images_pan.add(new GeoImg(gdata,
                        PlanarImage.wrapRenderedImage(imagen_original),
                        archivo,
                        streamData,
                        imageData));
                if (files_multi.length == 1) {
                    tratarImagenesMulti(files_multi);
                } else {
                    cargarImagenesMulti(files_multi);
                }
                double l = images_pan.get(0).getData().getXScale();
                double h = images_multi.get(0).getData().getXScale();
                double ratio = 0;
                if ((l == 0.0) || (h == 0.0)) {
                    ratio = pedirLH();
                } else {
                    ratio = l / h;
                }
                erg.setAll(wvm, niv_PAN, niv_MULTI, interpol, precis, images_pan, images_multi, 0.0, true, false, ratio);
                erg.setMinMax(minPond, maxPond);
                principal.cerrarImagen();
                double[] res = erg.muestreoErgas(muestras, principal.getT(), false);
                principal.setErgasTocho(erg);
                if (res == null) {
                    return;
                }
                // Por separado (devuelve un array de alfas)
                String mensaje = new String("");
                for (int i = 0; i < res.length; i++) {
                    mensaje = mensaje.concat(ic.buscar(27) + (i + 1) + " : " + res[i] + "\n"); // Tag 27
                }
                System.out.println(mensaje);
                message = da.crearMensaje(DialogosAux.DIAG_PONDSEP);
                ((JTextPane) message[0]).setText(mensaje);
                options = da.crearOpciones(DialogosAux.OPT_PONDSEP);
                result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(28),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]); // Tag 28
                switch (result) {
                    case 0: {
                        return;
                    }
                    case 1: {
                        Fusion f = new Fusion(ic);
                        GeoImg[] fusoptima = new GeoImg[images_multi.size()];



                        for (int i = 0; i < fusoptima.length; i++) {

                            fusoptima[i] = f.atrous(images_pan.get(0), images_multi.get(i), wvm, niv_PAN, niv_MULTI, interpol, precis, res[0]);
                        }



                        principal.manejoIMGS(fusoptima, 0);

                        break;

                    }
                    case 2: {
                        JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
                        break;
                    }

                }


            } else {
                JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
            }



        } else {
            dibujarMensajesError(retval);
        }




    }

    /**
     * Implementa la fusión À trous utilizando la imagen contenida en este objeto
     * como multiespectral
     */
    protected void atrous2() {
        // Fusionar (À trous) como MULTI
        JFileChooser fglobal = principal.getFglobal();
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setDialogTitle(ic.buscar(123)); // Tag 123
        fglobal.setMultiSelectionEnabled(false);
        int retval = fglobal.showDialog(t, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File[] files_pan = {fglobal.getSelectedFile()};
            // Pido la wavelet que se utilizará
            Object[] message = da.crearMensaje(DialogosAux.DIAG_ATROUS);
            String[] options = da.crearOpciones(DialogosAux.OPT_ATROUS);
            int result = 0;
            GeoImg[] melt = null;
            boolean correcto = false;
            while (!correcto) {

                result = JOptionPane.showOptionDialog(t, message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, options,
                        options[0]); // Tag 101
                if (!((JTextField) message[10]).getText().equals("") && !((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {// Está
                    // todo
                    // rellenado
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true;
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21
                }
            }
            if (result == 0) {
                // Ahora llamo al nuevo procedimiento de Fusion, llamado
                // fusionar
                // Ya que pido los niveles, tengo que pasárselos a fusionar
                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());
                double ponderacion = Double.parseDouble(((JTextField) message[10]).getText());
                int precis = 0;
                int interpol = principal.getInterpol();
                if (interpol == 2) {
                    precis = Integer.parseInt(((JTextField) message[8]).getText());

                }

                if (((JComboBox) message[1]).getSelectedItem().toString().equals("b3spline")) {
                    wvm = 6;

                }


                melt = new GeoImg[1];
                Fusion a = new Fusion(ic);
                if (images_multi == null)
                {
                    images_multi = new ArrayList<GeoImg>();
                }
                images_multi.clear();
                images_multi.add(new GeoImg(gdata,
                        PlanarImage.wrapRenderedImage(imagen_original),
                        archivo,
                        streamData,
                        imageData));
                tratarImagenesPan(files_pan);
                melt[0] = a.atrous(images_pan.get(0), images_multi.get(0), wvm, niv_PAN, niv_MULTI,
                        interpol, precis, ponderacion);

                principal.manejoIMGS(melt, 0);
            } else {
                JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
            }

        } else {
            dibujarMensajesError(retval);
            return;
        }

    }

    /**
     * Implementa la fusión À trous usando la imagen contenida en este objeto
     * como pancromática
     */
    protected void atrous1() {
        // Fusionar (À trous) como PAN
        JFileChooser fglobal = principal.getFglobal();
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setDialogTitle(ic.buscar(104)); // Tag 104
        fglobal.setMultiSelectionEnabled(true);
        int retval = fglobal.showDialog(t, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File[] files_multi = fglobal.getSelectedFiles();
            // Pido la wavelet que se utilizará
            Object[] message = da.crearMensaje(DialogosAux.DIAG_ATROUS);

            String[] options = da.crearOpciones(DialogosAux.OPT_ATROUS);
            int result = 0;
            
            boolean correcto = false;
            while (!correcto) {

                result = JOptionPane.showOptionDialog(t, message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, options,
                        options[0]); // Tag 101
                if (!((JTextField) message[10]).getText().equals("") && !((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {// Está
                    // todo
                    // rellenado
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true;
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21
                }
            }
            if (result == 0) {
                // Ahora llamo al nuevo procedimiento de Fusion, llamado
                // fusionar
                // Ya que pido los niveles, tengo que pasárselos a fusionar
                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());
                double ponderacion = Double.parseDouble(((JTextField) message[10]).getText());
                int precis = 0;
                int interpol = principal.getInterpol();
                if (interpol == 2) {
                    precis = Integer.parseInt(((JTextField) message[8]).getText());

                }

                if (((JComboBox) message[1]).getSelectedItem().toString().equals("b3spline")) {
                    wvm = 6;

                }
                Fusion a = new Fusion(ic);
                images_pan.add(new GeoImg(gdata,
                        PlanarImage.wrapRenderedImage(imagen_original),
                        archivo,
                        streamData,
                        imageData));
                if (files_multi.length == 1) {
                    tratarImagenesMulti(files_multi);
                } else {
                    cargarImagenesMulti(files_multi);
                }
                GeoImg[] melt = new GeoImg[images_multi.size()];
                for (int i = 0; i < images_multi.size(); i++) {


                    melt[i] = a.atrous(images_pan.get(0), images_multi.get(i), wvm, niv_PAN, niv_MULTI,
                            interpol, precis, ponderacion);
                }
                principal.manejoIMGS(melt, 0);

            } else {
                JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
            }

        } else {
            dibujarMensajesError(retval);
        }
    }

    /**
     * Calcula la fusión de Mallat utilizando la imagen contenida en este objeto
     * como multiespectral
     */
    protected void fusionar2() {
        // Fusionar (Mallat) como MULTI
        JFileChooser fglobal = principal.getFglobal();
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setDialogTitle(ic.buscar(123)); // Tag 123
        fglobal.setMultiSelectionEnabled(false);
        int retval = fglobal.showDialog(t, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            // Permito multiselección

            File[] files_pan = {fglobal.getSelectedFile()};
            // Pido la wavelet que se utilizará
            Object[] message = da.crearMensaje(DialogosAux.DIAG_FUSIONAR);
            String[] options = da.crearOpciones(DialogosAux.OPT_FUSIONAR);
            int result = 0;
            boolean correcto = false;
            while (!correcto) {

                result = JOptionPane.showOptionDialog(t, message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, options,
                        options[0]); // Tag 101
                if (!((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true; // Dejamos cancelar
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21
                }
            }

            if (result == 0) {

                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());
                GeoImg[] melt = new GeoImg[1];
                Fusion a = new Fusion(ic);
                if (images_multi == null)
                {
                    images_multi = new ArrayList<GeoImg>();
                }
                images_multi.clear();
                images_multi.add(new GeoImg(gdata,
                        PlanarImage.wrapRenderedImage(this.source),
                        archivo,
                        streamData,
                        imageData));
                tratarImagenesPan(files_pan);


                melt[0] = a.fusionarEscalando(images_pan.get(0), images_multi.get(0), wvm, niv_PAN, niv_MULTI);

                principal.manejoIMGS(melt, 0);

            } else {
                JOptionPane.showMessageDialog(t,
                        ic.buscar(29)); // Tag 29
            }
        // Ahora llamo a nuevo procedimiento de Fusion, llamado fusion

        } else {
            dibujarMensajesError(retval);
        }
    }

    /**
     * Calcula la fusión de Malla utilizando la imagen contenida en el objeto
     * como pancromática
     */
    protected void fusionar1() {
        // Fusionar (Mallat) como PAN
        JFileChooser fglobal = principal.getFglobal();
        fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
        fglobal.setDialogTitle(ic.buscar(104)); // Tag 104
        fglobal.setMultiSelectionEnabled(true);
        int retval = fglobal.showDialog(t, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            // Permito multiselección
            File[] files_multi = fglobal.getSelectedFiles();
            // Pido la wavelet que se utilizará
            Object[] message = da.crearMensaje(DialogosAux.DIAG_FUSIONAR);
            String[] options = da.crearOpciones(DialogosAux.OPT_FUSIONAR);
            int result = 0;
            boolean correcto = false;
            while (!correcto) {

                result = JOptionPane.showOptionDialog(t, message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, options,
                        options[0]); // Tag 101
                if (!((JTextField) message[3]).getText().equals("") && !((JTextField) message[5]).getText().equals("")) {
                    correcto = true;
                }
                if (result == 1) {
                    correcto = true; // Dejamos cancelar
                }
                if (correcto != true) {
                    JOptionPane.showMessageDialog(t,
                            ic.buscar(26),
                            ic.buscar(21),
                            JOptionPane.WARNING_MESSAGE); // Tag 26, 21
                }
            }

            if (result == 0) {

                int niv_PAN = Integer.parseInt(((JTextField) message[3]).getText());
                int niv_MULTI = Integer.parseInt(((JTextField) message[5]).getText());

                Fusion a = new Fusion(ic);
                images_pan.add(new GeoImg(gdata,
                        PlanarImage.wrapRenderedImage(imagen_original),
                        new File(""),
                        streamData,
                        imageData));
                System.out.println("El tamaño de files_multi es " + files_multi.length);
                if (files_multi.length == 1) {
                    tratarImagenesMulti(files_multi);
                } else {
                    cargarImagenesMulti(files_multi);
                }
                System.out.println("El tamaño de images_multi es " + images_multi.size());
                GeoImg[] melt = new GeoImg[images_multi.size()];
                for (int i = 0; i < images_multi.size(); i++) {



                    melt[i] = a.fusionarEscalando(images_pan.get(0), images_multi.get(i), wvm, niv_PAN, niv_MULTI);
                }
                if (melt == null) {
                    System.out.println("Estoy mostrando null");
                }
                principal.manejoIMGS(melt, 0);

            } else {
                JOptionPane.showMessageDialog(t,
                        ic.buscar(29)); // Tag 29
            }
        // Ahora llamo a nuevo procedimiento de Fusion, llamado fusion

        } else {
            dibujarMensajesError(retval);
        }
    }

    /**
     * Pide la relación L/H (la relación entre los tamaños de pixel de las 
     * imágenes) si alguno de ellos falta en los metadatos de la imagen.
     * @return La relación L/H
     */
    private double pedirLH() {
//		Pedimos a manita la relación L/H
        //DialogosAux daux = new DialogosAux(principal);
        Object[] message = da.crearMensaje(DialogosAux.DIAG_PEDIR_LH);
        String[] options = new String[2];
        options[0] = ic.buscar(23); // Tag 23
        options[1] = ic.buscar(24); // Tag 24
        boolean correcto = false;
        double ratio = 0;
        int resultado = 0;
        while (!correcto) {
            resultado = JOptionPane.showOptionDialog(t,
                    message,
                    ic.buscar(124),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]); // Tag 124
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
     * Muestra el diálogo para introducir los valores de lectura de un fichero
     * BIL
     */
    public void manejoBIL() {

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
            bandas = Integer.parseInt(((JTextField) message[1]).getText());
            lineas = Integer.parseInt(((JTextField) message[3]).getText());
            columnas = Integer.parseInt(((JTextField) message[5]).getText());
            celdas = Integer.parseInt(((JTextField) message[7]).getText());

        } else {
            JOptionPane.showMessageDialog(t, ic.buscar(2)); // Tag 2
        }
    }

    /**
     * Carga las imágenes seleccionadas como multiespectrales, teniendo en cuenta
     * que pueden ser multibanda y/o multipágina
     * @param files_multi Los ficheros seleccionados
     */
    private void tratarImagenesMulti(File[] files_multi) {
        PlanarImage[] bandasB = null;
        if (images_multi != null) {
            images_multi.clear();
        } else {
            images_multi = new ArrayList<GeoImg>();
        }

        if (files_multi[0].getName().endsWith("bil") || files_multi[0].getName().endsWith("BIL")) {// Es un archivo BIL


            manejoBIL();
            AbrirBIL ab = new AbrirBIL();
            bandasB = new PlanarImage[bandas];
            images_multi = new ArrayList<GeoImg>();
            try {
                bandasB = ab.transBIL(files_multi[0], bandas, lineas, columnas, celdas);
                for (int i = 0; i < bandas; i++) {
                    images_multi.add(new GeoImg(null, bandasB[i], files_multi[0], null, null));
                }
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(t,
                        ic.buscar(6),
                        ic.buscar(7),
                        JOptionPane.ERROR_MESSAGE); // Tag 6, 7
            }

        }
        else {
       ImageInfo ii = new ImageInfo();
		FileSeekableStream stream = null;
		try{
	// UN archivo
				stream = new FileSeekableStream(files_multi[0]);
				ii.setInput((InputStream)stream);
				ii.check();
				if (ii.getFormat()==ImageInfo.FORMAT_TIFF)
				{	// Leyendo TIFF
																																
					//A lo brasileiro
					ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, null);
					GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(
                                            files_multi[0]);
					
					int paginas=0;
					paginas = dec.getNumPages();
					
					
					for (int i=0; i<paginas; i++)
					{
						RenderedImage ri=null;
						ri = dec.decodeAsRenderedImage(i);						
						
						images_multi.add( 
                                                        new GeoImg(
                                                            Info.crearGeoData(ti),
                                                            PlanarImage.wrapRenderedImage(ri),
                                                            files_multi[0],
                                                            Info.crearStreamData(ti),
                                                            Info.crearImageData(ti)));
					}
                                }
                }
                                catch (IOException ioe)
                                {
                                    
                                }
					

				}  // FIN si es TIFF
        }



    

    /**
     * Carga las imágenes seleccionadas como pancromáticas, teniendo en cuenta
     * que no pueden ser multibanda ni multipágina
     * @param files_pan 
     */
    private void tratarImagenesPan(File[] files_pan) {
        if (images_pan != null) {
            images_pan.clear();
        } else {
            images_pan = new ArrayList<GeoImg>();
        }
        ImageInfo ii = new ImageInfo();
        if (files_pan.length == 1 || files_pan.length == 3) {// Es un solo archivo o combinar
            for (int i = 0; i < files_pan.length; i++) {
                try{
                ii.setInput((InputStream) new FileSeekableStream(files_pan[i]));
                ii.check();
                }
                catch (Exception e)
                {
                    Dialogos.informarExcepcion(e);
                }
                GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files_pan[i]);

                if (files_pan[i].getName().endsWith("bil") || files_pan[i].getName().endsWith("BIL")) {// Es un archivo BIL

                    JOptionPane.showMessageDialog(t,
                            ic.buscar(10),
                            ic.buscar(7),
                            JOptionPane.ERROR_MESSAGE); // Tag 10, 7
                    return;
                }
                if (ii.getFormat() == ImageInfo.FORMAT_TIFF) {// Es un archivo TIFF
                    try {
                        RenderedImage imagen2 = JAI.create("imageread", files_pan[i].getPath());
                        int nbands = imagen2.getSampleModel().getNumBands();

                        if (nbands != 1) {// TIFF multibanda

                            JOptionPane.showMessageDialog(t,
                                    ic.buscar(10),
                                    ic.buscar(7),
                                    JOptionPane.ERROR_MESSAGE); // Tag 10, 7
                            return;
                        } else {
                            FileSeekableStream stream = new FileSeekableStream(files_pan[0]);


                            //A lo brasileiro
                            ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, null);
                            int paginas = dec.getNumPages();
                            if (paginas != 1) {

                                JOptionPane.showMessageDialog(t,
                                        ic.buscar(10),
                                        ic.buscar(7),
                                        JOptionPane.ERROR_MESSAGE); // tag 10, 7
                                return;
                            } else if (paginas == 1) {

                                PlanarImage pan = JAI.create("imageread", files_pan[i].getPath());
                                images_pan.add(new GeoImg(Info.crearGeoData(ti),
                                        pan,
                                        files_pan[i],
                                        Info.crearStreamData(ti),
                                        Info.crearImageData(ti)));

                            }
                        }
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(t,
                                ic.buscar(6),
                                ic.buscar(7), JOptionPane.ERROR_MESSAGE); // Tag 6, 7 
                    }
                }

            }
        }




    }

    /**
     * Carga las imágenes que se han seleccionado como multiespectrales (Por tanto
     * la imagen contenida en el objeto se utiliza como pancromática)
     * @param files_multi Los ficheros seleccionados
     */
    private void cargarImagenesMulti(File[] files_multi) {
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
    ActionListener wvt = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == cbwavelet) {
                wvm = cbwavelet.getSelectedIndex();

                if (cbwavelet.getSelectedItem().toString().equals("b3spline")) {

                    wvm = 6;
                } else {
                    cbwavelet.setSelectedIndex(wvm);
                }

            }
        }
    };

    /**
     * Degrada la imagen utilizando el proceso de degradación de Mallat
     */
    protected void degradar() {
        Object[] message = da.crearMensaje(DialogosAux.DIAG_DEGRADAR);
        int num_niveles = 0;
        String[] options = da.crearOpciones(DialogosAux.OPT_DEGRADAR);

        int result = JOptionPane.showOptionDialog(t, message,
                ic.buscar(101),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]); // Tag 101
        if (result == 0) {
            num_niveles = Integer.parseInt(((JTextField) message[1]).getText());
            Fusion a = new Fusion(ic);
            GeoImg[] degradadas = a.degradar(wvm,
                    num_niveles,
                    new GeoImg(gdata,
                    PlanarImage.wrapRenderedImage(this.source),
                    new File(""),
                    streamData,
                    imageData));
            principal.manejoIMGS(degradadas, 0);
            return;
        } else {
            JOptionPane.showMessageDialog(t,
                    ic.buscar(29)); // Tag 29
            return;
        }

    }

    /**
     * Permite a las clases hijas actualizar la imagen adaptada a DisplayJAI en 
     * este objeto, pues la imagen se calcula después de llamar a super()
     * @param surrogateImage2 La imagen adaptada
     */
    public void setSurrogate(PlanarImage surrogateImage2) {
        surrogateImage = surrogateImage2;

    }

    /**
     * Devuelve los metadatos de stream (fichero)
     * @return Metadatos Stream
     */
    public abstract IIOMetadata getStreamData();

    /**
     * Devuelve los metadatos de imagen
     * @return Metadatos Image
     */
    public abstract IIOMetadata getImageData();
}

