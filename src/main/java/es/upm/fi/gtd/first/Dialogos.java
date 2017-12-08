/**
* Aquá se dibujan los diálogos de la aplicacián, permitiendo una mejor extensibilidad 
* y mejorando la claridad de la clase principal (Atrous)
*/
package es.upm.fi.gtd.first;

import es.upm.fi.gtd.first.dialogos.DialogoBoxCount;
import es.upm.fi.gtd.first.dialogos.DialogoFusionPorBandass;
import es.upm.fi.gtd.first.dialogos.DialogoEscala;
import es.upm.fi.gtd.first.dialogos.DialogoFusionBrovey;
import es.upm.fi.gtd.first.dialogos.DialogoFusionMDMR;
import es.upm.fi.gtd.first.dialogos.DialogoFusionPCA;
import es.upm.fi.gtd.first.dialogos.DialogoFusionRE;
import es.upm.fi.gtd.first.dialogos.DialogoIndiceZhou;
import es.upm.fi.gtd.first.dialogos.DialogoTransformadaFourier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import java.io.IOException;


/**
 * Implementa la creación de los diálogos de los elementos del menú
 * @author Alvar
 */

public class Dialogos implements ActionListener,Runnable
{
	/**
         * Principal
         */
	private Atrous principal;
        /**
         * El JFrame principal
         */
	private static JFrame t;
        /**
         * Auxiliar que crea las opciones de los diálogos
         */
	private DialogosAux da;
        /**
         * JFileChooser global
         */
	JFileChooser fglobal;
        /**
         * Imágenes no cargadas, obtenido a travás del FilePreviewer
         */
        int width;
        /**
         * Imágenes no cargadas, obtenido a travás del FilePreviewer
         */
        int height;
        /**
         * Constantes para la calculadora de imágenes
         */
        public static final int OP_AND = 1;
        
        /**
         * Constante que identifica que se va a almacenar como un fichero TIFF
         */
        public static final int FORMATO_TIFF = 2;
        
        /**
         * Constante que identifica que se va a almacenar como un fichero JPEG
         */
        public static final int FORMATO_JPEG = 3;
	
	ParseXML ic;
    /**
     * Constructor
     * @param atrous Principal
     * @param t2 JFrame de la aplicacián
     * @param fglobal2 JFileChooser global
     */
	public Dialogos(Atrous atrous, JFrame t2, JFileChooser fglobal2) {
		
		principal = atrous;
                ic = principal.getIC();
		t = t2;
		fglobal = fglobal2;
		if (fglobal == null)
		{
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				Dialogos.informarExcepcion(e);
				e.printStackTrace();
			}
			fglobal = new JFileChooser();
			
			Atrous.setFglobal(fglobal);
			
		}
		da = new DialogosAux(principal);

	}

    /**
     * Carga un nuevo objeto de datos de traducciones de acuerdo al idioma
     * especificado por loc
     * @param loc Idioma a cargar
     */
    public void actualizarIdioma(Locale loc)
{
   ic = new ParseXML(loc.getLanguage()+"_"+loc.getCountry()+".xml"); 
}
	
    /**
     * Elige a partir del evento recibido, quá diálogo ha de crear
     * @param e El evento recibido
     */
    @Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().compareTo(ic.buscar(37))==0) // Tag 37
		{
			dibujarDialogoAbrir();
                        principal.show2();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(38))==0) // Tag 38
		{
			dibujarDialogoGuardar();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(39))==0) // Tag 39
		{
			dibujarDialogoGuardarTodo();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(41))==0) // Tag 41
		{
			principal.show();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(42))==0) // Tag 42
		{
			principal.show2();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(43))==0) // Tag 43
		{
			principal.close();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(40))==0) // Tag 40
		{
			principal.exit();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(58))==0) // Tag 58
                {
			JOptionPane.showMessageDialog(t, 
                                ic.buscar(82),
                                ic.buscar(83),JOptionPane.INFORMATION_MESSAGE); // Tag 82, 83
		}
		else if (e.getActionCommand().compareTo(ic.buscar(45))==0) // Tag 45
		{
			dibujarDialogoFusionar();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(49))==0) // Tag 49
		{
			if (principal.getFilesPan()==null)
			{
				dibujarDialogoDegradar();
			}
			dibujarDialogoDegradar2();
			//principal.degradar();

		}
		else if (e.getActionCommand().compareTo(ic.buscar(84))==0) // Tag 84
		{
	        dibujarDialogoErgas();	       
		}
		else if (e.getActionCommand().compareTo(ic.buscar(50))==0) // Tag 50
		{
			dibujarDialogoErgasYaFus(true);
		}
		else if (e.getActionCommand().compareTo(ic.buscar(51))==0) // Tag 51
		{
			dibujarDialogoErgasYaFus(false);
		}
		else if (e.getActionCommand().compareTo(ic.buscar(52))==0) // Tag 52 
		{
			dibujarDialogoHallarPonderacion(false);
		}
                else if (e.getActionCommand().compareTo("Calcular Pond Rápida")==0)  
		{
			dibujarDialogoHallarPonderacion(true);
		}
		else if (e.getActionCommand().compareTo(ic.buscar(46))==0) // Tag 46
		{
			dibujarDialogoFusionAtrous();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(44))==0) // Tag 44
		{
                    if (principal.comprobarListaImagenes())
                    {
                        principal.combinar(true);
                    }
                    else{
			dibujarDialogoCombinar();
                    }
		}
		else if (e.getActionCommand().compareTo(ic.buscar(85))==0) // Tag 85
		{
                    DialogoTransformadaFourier dtf = new DialogoTransformadaFourier(principal.getT(), true, principal);
                     dtf.setVisible(true);
		}
		else if (e.getActionCommand().compareTo(ic.buscar(86))==0) // Tag 86
		{
			dibujarDialogoDegradar();
			principal.invfourier();
		}
		else if (e.getActionCommand().compareTo(ic.buscar(55))==0) // Tag 55
		{
			dibujarDialogoVariograma();
			
		}
		else if (e.getActionCommand().compareTo(ic.buscar(53))==0) // Tag 53
		{
			dibujarDialogoEscala();
		}
                else if (e.getActionCommand().compareTo(ic.buscar(54))==0) // Tag 54
                {
                        dibujarDialogoAND();
                        
                        
                }
                else if (e.getActionCommand().compareTo(ic.buscar(47))==0) // Tag 47
                {
                    dibujarDialogoFIHS();
                }
                 else if (e.getActionCommand().compareTo(ic.buscar(48))==0) // Tag 48
                {
                    dibujarDialogoIHS();
                }
                 else if (e.getActionCommand().compareTo(ic.buscar(56))==0) // Tag 56
                 {
                     principal.recortar();
                 }
                 else if (e.getActionCommand().compareTo(ic.buscar(57))==0) // Tag 57
                 {
                     dibujarDialogoAbrir();
                     principal.kmedias();
                 }
                 else if (e.getActionCommand().compareTo("LANGUAGE_MENU")==0)
                 {
                     principal.actualizarIdioma(principal.getSelectedLocale());
                 }
                 else if (e.getActionCommand().compareTo(ic.buscar(167))==0)  // Tag 167
                 {
                     DialogoFusionPorBandass dfpb = new DialogoFusionPorBandass (principal.getT(), true, principal);
                     dfpb.setVisible(true);
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(169))==0) // Tag 169
                 {
                     DialogoFusionPCA dfp = new DialogoFusionPCA (principal.getT(), true, principal);
                     dfp.setVisible(true);
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(170))==0) // Tag 170
                 {
                     DialogoFusionBrovey dfb = new DialogoFusionBrovey (principal.getT(), true, principal);
                     dfb.setVisible(true);
                 }
                 else if (e.getActionCommand().compareTo(ic.buscar(171))==0) // Tag 171
                 {
                     DialogoFusionRE dfb = new DialogoFusionRE (principal.getT(), true, principal);
                     dfb.setVisible(true);
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(172))==0) // Tag 172
                 {
                     DialogoFusionMDMR dfb = new DialogoFusionMDMR (principal.getT(), true, principal);
                     dfb.setVisible(true);
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(173))==0) // Tag 173
                 {
                     DialogoIndiceZhou diz = new DialogoIndiceZhou (principal.getT(), true, principal);
                     diz.setVisible(true);
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(174))==0) // Tag 174
                 {
                     DialogoBoxCount dbc = new DialogoBoxCount (principal.getT(), true, principal);
                     dbc.setVisible(true);
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(175))==0) // Tag 175
                 {
                     Actualizador ac = new Actualizador(principal);
                     ac.actualizar();
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(176))==0) // Tag 176
                 {
                     dibujarManual();
                 }
                else if (e.getActionCommand().compareTo(ic.buscar(179))==0) // Tag 179
                {
                    principal.getDPane().tileFrames();
                }
                else if (e.getActionCommand().compareTo(ic.buscar(180))==0) // Tag 180
                {
                    principal.show2();
                }

	}
	

    private void dibujarDialogoEscala() {
        // Tengo en cuenta si hay alguna imagen seleccionada en la cajita de imágenes
        // Necesito el objeto AreaTrabajo
        AreaTrabajo at = principal.getDPane();
        int res = -1;
        GeoImg imagen = null;
        if (at != null) {
            JList lista = at.getLista();
            // Obtengo lo que hay seleccionado
            if (lista.getSelectedValues() == null) {// No hay nada seleccionado, se pide abrir imagen
                res = dibujarDialogoDegradar();
                // Hay imagen ya en Atrous
                
            }
            else
            { // Hay valores seleccionados (escoger sálo uno)
                if (lista.getSelectedValues().length > 1)
                {
                    JOptionPane.showMessageDialog(t, 
                            ic.buscar(87)); // Tag 87
                }
                else if(lista.getSelectedValues().length ==1)
                {
                    imagen = (GeoImg) lista.getSelectedValues()[0];
                    res=0;
                }
                
            }
        }
        else
        { // Si at es null, no hay ninguna imagen abierta
            res = dibujarDialogoDegradar();
        }
        if (res != 0)
        {
            return;
        }
            
        // Creo el diálogo para los parámetros
        DialogoEscala de = null;
        if (imagen != null)
        {
            de = new DialogoEscala(t,true,principal.interp_lis,imagen.getImage().getWidth(),imagen.getImage().getWidth(), principal.id);
        }
        else
        {
           
            
            
            de = new DialogoEscala(t, true, principal.interp_lis, width, height, principal.id);
        }
        de.setVisible(true);
        // Como es modal, hasta que no se cierre no volvemos
        Point2D p= null;
        if (de.getAbsoluto())
        {
            p = de.getValores();
            principal.escala(imagen,true,p,de.getPrecision());
        }
        else
        {
            p = de.getValores();
            principal.escala(imagen,false,p,de.getPrecision());
        }
        
		
	}

    private void dibujarDialogoFIHS() {
        List<File> pan = new ArrayList<File>();
        List<File> multis = new ArrayList<File>();
        if (crearDialogoAbrirPanAbrirMulti(pan, multis) == false) {
            return;
        }
        File[] files = new File[pan.size()];
        pan.toArray(files);

        GeoImg[] imagesPan = new GeoImg[files.length];
        for (int i = 0; i < files.length; i++) {

            GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
            imagesPan[i] = new GeoImg(Info.crearGeoData(ti), null, files[i], Info.crearStreamData(ti), Info.crearImageData(ti));
        }
        principal.setFilesPan(imagesPan);
        files = new File[multis.size()];
        multis.toArray(files);
        principal.setFilesMulti(files);
        principal.fihs();
    }

      private void dibujarDialogoIHS() {
        List<File> pan = new ArrayList<File>();
        List<File> multis = new ArrayList<File>();
        if (crearDialogoAbrirPanAbrirMulti(pan, multis) == false) {
            return;
        }
        File[] files = new File[pan.size()];
        pan.toArray(files);

        GeoImg[] imagesPan = new GeoImg[files.length];
        for (int i = 0; i < files.length; i++) {

            GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
            imagesPan[i] = new GeoImg(Info.crearGeoData(ti), null, files[i], Info.crearStreamData(ti), Info.crearImageData(ti));
        }
        principal.setFilesPan(imagesPan);
        files = new File[multis.size()];

        multis.toArray(files);
                for(File f : files)
        {
            System.out.println("El archivo es: "+f.getName());
        }
        principal.setFilesMulti(files);
        principal.ihs();
    }


	/**
     * Crea el diálogo para calcular un Variograma
     */
	private void dibujarDialogoVariograma() {
            fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
            fglobal.setMultiSelectionEnabled(false);
            ExampleFileFilter filter =
                    new ExampleFileFilter(
                    new String[]{"tiff",
                "tif",
                "jpg",
                "jpeg",
                "jpe",
                "bil"
            },
                    ic.buscar(88)); // Tag 88
            fglobal.resetChoosableFileFilters();
            
                fglobal.addChoosableFileFilter(filter);
            
            fglobal.setAccessory(new FilePreviewer(fglobal));
            int retval = fglobal.showDialog(t, null);
            if (retval == JFileChooser.APPROVE_OPTION) {



                File[] files = new File[1];
                files[0] = fglobal.getSelectedFile();

                GeoImg[] imagesPan = new GeoImg[files.length];
                for (int i = 0; i < files.length; i++) {

                    GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
                    imagesPan[i] = new GeoImg(Info.crearGeoData(ti), null, files[i], Info.crearStreamData(ti), Info.crearImageData(ti));
                }
                principal.setFilesPan(imagesPan);
                // Otros parámetros
                Object[] message = da.crearMensaje(DialogosAux.DIAG_VARIO);
                String[] options = da.crearOpciones(DialogosAux.OPT_BIL);
                int result = 0;
                int muestras = 10;
                double dist = 5;
                double tol = 0.1;
                result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(89),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]); // Tag 89
                if (result == 0) {
                    /*if (!((JTextField) message[1]).getText().equalsIgnoreCase(""))
                    {
                    muestras = Integer.parseInt(((JTextField) message[1]).getText());
                    }
                    if (!((JTextField) message[3]).getText().equalsIgnoreCase(""))
                    {
                    dist = Double.parseDouble(((JTextField) message[3]).getText());
                    }
                    if (!((JTextField) message[5]).getText().equalsIgnoreCase(""))
                    {
                    tol = Double.parseDouble(((JTextField) message[5]).getText());
                    }*/
                    boolean metros = ((JCheckBox) message[0]).isSelected();

                    principal.variograma(muestras, dist, tol, metros);
                } else {
                    return;
                }

            } else { 
                dibujarMensajesError(ic, retval);
            } 
		
	}



    /**
     * Crea el diálogo para calcular ERGAS a partir de una imagen ya fusionada
     * @param espacial true si se quiere calcular ERGAS espacial
     */
	private void dibujarDialogoErgasYaFus(boolean espacial) {
		List <File> fus = new ArrayList <File>();
		List <File> multis = new ArrayList <File>();
		if (crearDialogoAbrirFusAbrirImagenes(fus,multis,espacial)==false) return;
		File [] files = new File[fus.size()];
		fus.toArray(files);
		GeoImg [] imagesPan = new GeoImg[files.length];
		for (int i = 0; i < files.length; i++)
		{
			//Info infobj = new Info();
			//infobj.mostrarGeoInfo(fglobal.getSelectedFiles()[i]);

			GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
			imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));

		}
		
		principal.setFilesPan(imagesPan);
		files = new File [multis.size()];
		multis.toArray(files);
		


		principal.setFilesMulti(files);
		principal.setSeparado(true);
		//Object[] message = da.crearMensaje(DialogosAux.DIAG_ERGASYAFUS);
		principal.ergas(true,espacial);
		
	}

    
        
    /**
     * Auxiliar para crear el diálogo cargar una FUS, cargar una COMP
     * @param fus Referencia a la imagen fusionada que se quiere cargar
     * @param multis Referencia a la imagen con la que se va a comparar
     * @param espacial true si viene de ERGAS espacial
     * @return true si todo ha salido bien
     */
	private boolean crearDialogoAbrirFusAbrirImagenes(List<File> fus, List<File> multis, boolean espacial) {
		boolean correcto = true;
		fglobal.setMultiSelectionEnabled(false);
		fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
    	fglobal.setDialogTitle(ic.buscar(93)); // Tag 93
    	ExampleFileFilter filter = 
                new ExampleFileFilter (
                new String [] {"tiff",
                               "tif",
                               "jpg",
                               "jpeg",
                               "jpe",
                               "bil"},
                               ic.buscar(88)); // Tag 88
    	fglobal.resetChoosableFileFilters();
    		fglobal.addChoosableFileFilter(filter);
    	
    	fglobal.setAccessory(new FilePreviewer(fglobal));
    	int retval = fglobal.showDialog(t, null);
    	
    	if (retval == JFileChooser.APPROVE_OPTION) {
    		fus.add(fglobal.getSelectedFile());
    		// Permito la seleccián de varias bandas
    		if (espacial)
    		{
    			fglobal.setDialogTitle(ic.buscar(94)); // Tag 94
    		}
    		else
    		{
    			fglobal.setDialogTitle(ic.buscar(95)); // Tag 95
    		}
    		fglobal.setMultiSelectionEnabled(true);
    		retval = fglobal.showDialog(t, null);
    		if (retval == JFileChooser.APPROVE_OPTION) {
    			multis.addAll(Arrays.asList(fglobal.getSelectedFiles()));
    		}
    		else 
	    	{
                    dibujarMensajesError(ic, retval);
	    		
	    		correcto = false;
	    	} 
	    	
    	}
    	else  
    	{
    		dibujarMensajesError(ic, retval);
    		correcto = false;
    		fus.clear();
    	} 
    	
    	return correcto;
	}



    /**
     * Crear el diálogo para combinar varias imágenes de escala de grises
     */
	private void dibujarDialogoCombinar() {
		
		
		fglobal.setMultiSelectionEnabled(false);
		fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
		fglobal.setDialogTitle(ic.buscar(96)); // Tag 96
    	ExampleFileFilter filter = 
                new ExampleFileFilter (
                    new String [] {"tiff",
                                   "tif",
                                   "jpg",
                                   "jpeg",
                                   "jpe",
                                   "bil"},
                                   ic.buscar(88)); // Tag 88
    	fglobal.resetChoosableFileFilters();
    		fglobal.addChoosableFileFilter(filter);
    	
    	fglobal.setAccessory(new FilePreviewer(fglobal));
    	int retval = fglobal.showDialog(t, null);
    	if (retval == JFileChooser.APPROVE_OPTION)
    	{
    		List <File> archivos = new ArrayList <File>();
    		archivos.add(fglobal.getSelectedFile());
    		fglobal.setDialogTitle(ic.buscar(97)); // Tag 97
    		retval = fglobal.showDialog(t, null);
    		if (retval == JFileChooser.APPROVE_OPTION)
        	{
        		
        		archivos.add(fglobal.getSelectedFile());
        		fglobal.setDialogTitle(ic.buscar(98)); // Tag 98
        		retval = fglobal.showDialog(t, null);
        		if (retval == JFileChooser.APPROVE_OPTION)
            	{
            		
            		archivos.add(fglobal.getSelectedFile());
            		File [] files =  new File[3];
            		archivos.toArray(files);
            		
            		
            		GeoImg [] imagesPan = new GeoImg[files.length];
            		for (int i = 0; i < files.length; i++)
            		{
            			GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
            			imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));
            		}
            		principal.setFilesPan(imagesPan);
            		principal.combinar(false);
                	return;
                }
                else 
                {
                            dibujarMensajesError(ic, retval);
                	
                } 
                

            }
            else 
            {
                    dibujarMensajesError(ic, retval);
            	
            } 
            

        }
        else 
        {
        	dibujarMensajesError(ic, retval);
        }         
   	}
		
	



    /**
     * Auxiliar que permite comprobar si un filtro de ficheros ya ha sido aáadido
     * a un JFileChooser
     * @param filter Filtro a comprobar
     * @param choosableFileFilters Conjunto de básqueda
     * @return true si pertenece
     */
	public static boolean pertenece(ExampleFileFilter filter, FileFilter[] choosableFileFilters) {
		List filtros = Arrays.asList(choosableFileFilters);		
		return filtros.contains(filter);
	}



	/**
	 * Método que dibuja los diálogos de elegir archivo para abrirlo
	 */
	
	public void dibujarDialogoAbrir()
	{
		fglobal.setMultiSelectionEnabled(true);
		fglobal.setDialogTitle(ic.buscar(37));  // Tag 37
		fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
    	ExampleFileFilter filter = 
                new ExampleFileFilter (
                    new String [] {"tiff",
                                   "tif",
                                   "jpg",
                                   "jpeg",
                                   "jpe",
                                   "bil"},
                                   ic.buscar(88)); // Tag 88
                             fglobal.resetChoosableFileFilters();
    		fglobal.addChoosableFileFilter(filter);

    	Accesorio acc = new Accesorio(fglobal, t, principal.getIC());
    	fglobal.setAccessory(acc);
    	acc.esDialogoAbrir();
    	acc.setProfundidad(false);
    	
        
        
    	int retval = fglobal.showDialog(t, null);
    	if (retval == JFileChooser.APPROVE_OPTION)
    	{
    		acc = (Accesorio) fglobal.getAccessory();
    		boolean separadas = acc.getSeparadas();
    		File [] files = fglobal.getSelectedFiles();
    		GeoImg [] imagesPan = new GeoImg[files.length];
                for (int i = 0; i < files.length; i++) {
                    
                    //Considerar BIL
                    if (files[i].getName().endsWith("bil")
                            ||files[i].getName().endsWith("BIL"))
                    {
                        principal.manejoBIL();
                    }
                    ImageInfo ii = new ImageInfo();
                    GeoTiffIIOMetadataAdapter ti = null;
                    try {
                        ii.setInput((InputStream) new FileInputStream(files[i]));
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    ii.check();
                    if (ii.getFormat()==ImageInfo.FORMAT_TIFF) {	// Leyendo TIFF
                        ti = new GeoTiffIIOMetadataAdapter(files[i]);
                    }
                    
                    //Info.mostrarGeoInfo(fglobal.getSelectedFiles()[i]);
                    
                    if (ti != null)
                    { // Hay datos geográficos
                        imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));
                    }
                    else
                    {
                        imagesPan [i] = new GeoImg(null, null, files[i], null, null);
                    }
                }
    		principal.setFilesPan(imagesPan);
    		principal.setLeerSeparadas(separadas);
    			
    		
    		return;
    	}
    	else  
    	{
    		dibujarMensajesError(ic, retval);
    	} 
    	
    	
		
	}
	
	/**
	 * Método que dibuja los diálogos de guardar una ánica imagen
	 */
	
	public void dibujarDialogoGuardar()
	{
		fglobal.setDialogTitle(ic.buscar(99)); // Tag 99
		fglobal.setDialogType(JFileChooser.SAVE_DIALOG);
		
		//Accesorio pep = (Accesorio) fglobal.getAccessory();
		Accesorio pep = new Accesorio(fglobal,t, principal.getIC());
		fglobal.setAccessory(pep);
		pep.setProfundidad(true);
		/*if (pep==null)
		{
			pep = new Accesorio(fglobal,t);
			fglobal.setAccessory(pep);
		}
		else
		{
			pep.setProfundidad(true);
		}
		*/
                ExampleFileFilter filtroTIFF = new ExampleFileFilter(
                                                new String [] {"tiff",
                                                              "tif"},
                                                              ic.buscar(3)); // Tag 3
                ExampleFileFilter filtroJPEG = new ExampleFileFilter(
                                                    new String [] {
                                                              "jpg",
                                                              "jpeg",
                                                              "jpe"},
                                                              ic.buscar(4)); // Tag 4
		
                fglobal.resetChoosableFileFilters();
                fglobal.addChoosableFileFilter(filtroTIFF);
                fglobal.addChoosableFileFilter(filtroJPEG);
		

		int retval = fglobal.showDialog(t, null);
		if (retval == JFileChooser.APPROVE_OPTION) 
		{		
			File guardar = fglobal.getSelectedFile();
                        int tipo = 3; // TIFF
                        if (fglobal.getFileFilter() == filtroJPEG)
                        {
                            tipo = 2;
                        }
			principal.setProfundidad(pep.getEP().getMarcada());
			principal.setGuardar(guardar);
			principal.guardar(tipo);
			
		}			    
		else 
		{
			dibujarMensajesError(ic, retval);
		} 
		
	}
	
    /**
     * Muestra los mensajes de error en un diálogo
     * @param ic Objeto de datos de traducciones actual
     * @param retval Valor devuelto por el diálogo que ha causado el error
     * @return 1 si se ha cancelado el diálogo, 2 si ha ocurrido un error, 3 en
     * otro caso
     */
    public static int dibujarMensajesError(ParseXML ic, int retval)
        {
                if (retval == JFileChooser.CANCEL_OPTION) 
		{
			JOptionPane.showMessageDialog(t, 
                                ic.buscar(90)); // Tag 90
                        return 1;
		} 
		else if (retval == JFileChooser.ERROR_OPTION) 
		{
			JOptionPane.showMessageDialog(t, ic.buscar(91),ic.buscar(7),JOptionPane.ERROR_MESSAGE); // Tag 91, 7
                        return 2;
		} 
		else 
		{
			JOptionPane.showMessageDialog(t, ic.buscar(92),ic.buscar(7),JOptionPane.ERROR_MESSAGE); // Tag 92, 7
                        return 3;
		}
               
        }
        
	/**
	 * Método que dibuja los diálogos para guardar todas las imágenes abiertas
	 */
	public void dibujarDialogoGuardarTodo()
	{
		fglobal.setDialogType(JFileChooser.SAVE_DIALOG);
    	fglobal.setDialogTitle(ic.buscar(100)); // Tag 100
                        Accesorio pepi = new Accesorio(fglobal, t, principal.getIC());
			fglobal.setAccessory(pepi);
			pepi.setProfundidad(true);
    	//fglobal.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	
    	
    	ExampleFileFilter filter_tiff = 
                new ExampleFileFilter (
                    new String [] {"tiff","tif"},ic.buscar(3)); // Tag 3
    	ExampleFileFilter filter_jpg = 
                new ExampleFileFilter(
                    new String [] {"jpg","jpeg","jpe"},ic.buscar(4)); // Tag 4
    	ExampleFileFilter filter_bil = 
                new ExampleFileFilter(
                    new String [] {"bil"},ic.buscar(5)); // Tag 5
    	fglobal.resetChoosableFileFilters();
    		fglobal.addChoosableFileFilter(filter_tiff);
    	
    		fglobal.addChoosableFileFilter(filter_jpg);
    	
    		fglobal.addChoosableFileFilter(filter_bil);
    	
    	int retval = fglobal.showDialog(t, null);
    	if (retval == JFileChooser.APPROVE_OPTION) 
    	{
            int profundidad = pepi.getEP().getMarcada();
    		File guardar = fglobal.getSelectedFile();
			principal.setGuardar(guardar);
			ExampleFileFilter pep = (ExampleFileFilter) fglobal.getFileFilter();
			principal.setPep(pep);
			principal.guardarTodo(profundidad);
			return;
    	
    	}			    
    	else  
    	{
    		dibujarMensajesError(ic, retval);
    	} 
    	
	}
	
	/**
	 * Método que dibuja los diálogos para fusionar dos (o más) imágenes por el Método de Mallat
	 */
	public void dibujarDialogoFusionar ()
	{
		List <File> pan = new ArrayList <File>();
		List <File> multis = new ArrayList <File>();
		if (crearDialogoAbrirPanAbrirMulti(pan,multis)==false) return;
    	Object[] message = da.crearMensaje(DialogosAux.DIAG_FUSIONAR);
    	String [] options = da.crearOpciones(DialogosAux.OPT_FUSIONAR);		    			
    	int result = 0;
    	boolean correcto = false;
    	while (!correcto)
    	{    				
    		result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        options, 
                        options[0]); // Tag 101
    		if (!((JTextField)message[3]).getText().equals("") && !((JTextField)message[5]).getText().equals(""))
    		{
    			correcto = true;
    		}
    		if (result == 1)
    		{
    			correcto = true; // Dejamos cancelar
    		}
    		if (correcto != true)
    		{
    			JOptionPane.showMessageDialog(t, 
                                   ic.buscar(26),
                                   ic.buscar(21),
                                   JOptionPane.WARNING_MESSAGE); // Tag 26, 21
    		}
    	}
    				
    	if (result == 0)
    	{    		
    		int niv_PAN = Integer.parseInt(((JTextField)message[3]).getText());
    		int niv_MULTI = Integer.parseInt(((JTextField)message[5]).getText());
    		principal.setNivPan(niv_PAN);
    		principal.setNivMulti(niv_MULTI);
    		File [] files = new File[pan.size()];
    		pan.toArray(files);
    		
    		GeoImg [] imagesPan = new GeoImg[files.length];
    		for (int i = 0; i < files.length; i++)
    		{

    			GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
    			imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));
    		}
    		principal.setFilesPan(imagesPan);
    		files = new File [multis.size()];
    		multis.toArray(files);
    		principal.setFilesMulti(files); 
    		principal.fusionar();
    				
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
    	}    			    			    		    	
	}
	/**
	 * Método que dibuja los diálogos necesarios para degradar una imagen (Elegir los ficheros)
     *
     * @return 0 en caso de que el diálogo de selección devuelva JFileChooser.APPROVE_OPTION,
     * el resultado de dibujarMensajesError en otro caso
     */
	public int dibujarDialogoDegradar()
	{
		fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
		fglobal.setMultiSelectionEnabled(false);
			ExampleFileFilter filter = 
                                new ExampleFileFilter (
                                    new String [] {"tiff",
                                    "tif",
                                    "jpg",
                                    "jpeg",
                                    "jpe",
                                    "bil"},ic.buscar(88)); // Tag 88
	    	fglobal.resetChoosableFileFilters();
	    		fglobal.addChoosableFileFilter(filter);
	    	
                        FilePreviewer fp = new FilePreviewer(fglobal);
			fglobal.setAccessory(fp);
			int retval = fglobal.showDialog(t, null);
			if (retval == JFileChooser.APPROVE_OPTION) {
    	 
    		
    		
				File [] files = new File[1];
				files[0] = fglobal.getSelectedFile();
				
				GeoImg [] imagesPan = new GeoImg[files.length];
	    		for (int i = 0; i < files.length; i++)
	    		{

	    			GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
        			imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));
	    		}
	    		principal.setFilesPan(imagesPan);
                        width = fp.getImageWidth();
                        height = fp.getImageHeight();
				return 0;
	    	
			}
	    
			else 
			{
				return dibujarMensajesError(ic, retval);
			} 
			

		
	};
	/**
	 * Método que dibuja el diálogo de seleccián de la wavelet madre y el námero de niveles para degradar
	 */
	public void dibujarDialogoDegradar2()
	{
		Object[] message = da.crearMensaje(DialogosAux.DIAG_DEGRADAR);
		int num_niveles = 0;
		String [] options = da.crearOpciones(DialogosAux.OPT_DEGRADAR);	    				    		
		int result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(101),
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        options, 
                        options[0]); // Tag 101
		if (result == 0)
		{	    					
			num_niveles = Integer.parseInt(((JTextField)message[1]).getText());
                        boolean soloFinales = ((JCheckBox)message[4]).isSelected();
			principal.setNiveles(num_niveles);
			principal.degradar(soloFinales);
		}
		else
		{
			JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
			return;
		}
	}
	/**
	 * Método que dibuja los diálogos necesarios para calcular el ERGAS de una fusián á trous
	 * Será llamado desde la clase Ergas
	 */
	public void dibujarDialogoErgas ()
	{
		List <File> pan = new ArrayList <File>();
		List <File> multis = new ArrayList <File>();
		if (crearDialogoAbrirPanAbrirMulti(pan,multis)==false) return;
		Object[] message = da.crearMensaje(DialogosAux.DIAG_ERGAS);    			    			    			
		String [] options = da.crearOpciones(DialogosAux.OPT_ERGAS);			    			
		int result = 0;

		boolean correcto = false;
		while (!correcto)
		{

			result = JOptionPane.showOptionDialog(t,
                                message,
                                ic.buscar(101),
                                JOptionPane.DEFAULT_OPTION, 
                                JOptionPane.INFORMATION_MESSAGE, 
                                null, 
                                options, 
                                options[0]); // Tag 101
			if (!((JTextField)message[10]).getText().equals("") && !((JTextField)message[3]).getText().equals("") && !((JTextField)message[5]).getText().equals(""))
			{// Está todo rellenado
				correcto = true;
			}			    				
			if (result == 1)
			{
				correcto = true;
			}
			if (correcto != true)
			{
				JOptionPane.showMessageDialog(t, 
                                        ic.buscar(26),
                                        ic.buscar(21),
                                        JOptionPane.WARNING_MESSAGE); // Tag 26, 21
			}
		}
		if (result == 0)
		{	 
			//Ahora llamo al nuevo procedimiento de Fusion, llamado fusionar
			//Ya que pido los niveles, tengo que pasárselos a fusionar			    							    				
			principal.setNivPan(Integer.parseInt(((JTextField)message[3]).getText()));
			principal.setNivMulti(Integer.parseInt(((JTextField)message[5]).getText()));
			principal.setSeparado(((JCheckBox)message[11]).isSelected());
			principal.setPonderacion(Double.parseDouble(((JTextField)message[10]).getText()));			    				   					
			File [] files = new File[pan.size()];
			pan.toArray(files);
					
			GeoImg [] imagesPan = new GeoImg[files.length];
    		for (int i = 0; i < files.length; i++)
    		{

    			GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
    			imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));
    		}
    		principal.setFilesPan(imagesPan);
			files = new File [multis.size()];
			multis.toArray(files);
			principal.setFilesMulti(files); 


			int precis = 0;
			int interpol = principal.getInterpol();
			if (interpol==2)
			{
				precis = Integer.parseInt(((JTextField)message[8]).getText());
				principal.setPrecision(precis);    						
			}    				
			if(((JComboBox)message[1]).getSelectedItem().toString().equals("b3spline"))
			{
				principal.setWavelet(6);

			}
			principal.ergas(false,false);    				    				
		}
		else
		{
			JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
		}    			    			    		

	}

	/**
	 * Método que dibuja los diálogos cuando se quiere calcular la ponderacián áptima para una fusián ï¿½ trous
	 * Será llamado desde la clase Ergas
     *
     * @param fast true si se usa el algoritmo con menos operaciones, false en
     * caso contrario
     */
	public void dibujarDialogoHallarPonderacion (boolean fast)
	{
		
		/*// Mostrar la cajita que pedirá los valores
		Object[] message = da.crearMensaje(DialogosAux.DIAG_MUESTRAS);
		
*/
            String [] options = da.crearOpciones(DialogosAux.OPT_MUESTRAS);
		int result = 0;
		int muestras = 0;
                double min,max;
		boolean correcto = false;
                /*
		while (!correcto)
		{
			result = JOptionPane.showOptionDialog(t,message,"Muestras sobre ERGAS",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			if (!((JTextField)message[1]).getText().equals(""))
			{
				correcto = true;		    					
			}
			if (result == 1)
			{
				correcto = true;		    					
			}
			if (correcto != true)
			{
				JOptionPane.showMessageDialog(t, "Hay campos necesarios en blanco","Aviso sin consecuencias",JOptionPane.WARNING_MESSAGE);
			}
		}
		if (result == 0)
		{
			muestras = Integer.parseInt(((JTextField)message[1]).getText());
		}
		else
		{
			JOptionPane.showMessageDialog(t, "Se ha cancelado la operacián","Aviso sin consecuencias",JOptionPane.WARNING_MESSAGE);
			return;
		}
		principal.setMuestras(muestras);
                */
		List <File> pan = new ArrayList <File>();
		List <File> multis = new ArrayList <File>();
		if (crearDialogoAbrirPanAbrirMulti(pan,multis)==false) return;
		Object[] message = da.crearMensaje(DialogosAux.DIAG_POND);    			    			    								    			
		result = 0;

		correcto = false;
		while (!correcto)
		{

			result = JOptionPane.showOptionDialog(t,
                                message,
                                ic.buscar(101),
                                JOptionPane.DEFAULT_OPTION, 
                                JOptionPane.INFORMATION_MESSAGE, 
                                null, 
                                options, 
                                options[0]); // Tag 101
			if (!((JTextField)message[3]).getText().equals("") && !((JTextField)message[5]).getText().equals(""))
			{// Está todo rellenado
				correcto = true;
			}			    				
			if (result == 1)
			{
				correcto = true;
			}
			if (correcto != true)
			{
				JOptionPane.showMessageDialog(t, 
                                        ic.buscar(26),
                                        ic.buscar(21),
                                        JOptionPane.WARNING_MESSAGE); // Tag 26, 21
			}
		}
		if (result == 0)
		{	 
			//Ahora llamo al nuevo procedimiento de Fusion, llamado fusionar
			//Ya que pido los niveles, tengo que pasárselos a fusionar			    							    				
			principal.setNivPan(Integer.parseInt(((JTextField)message[3]).getText()));
			principal.setNivMulti(Integer.parseInt(((JTextField)message[5]).getText()));
			principal.setSeparado(true);
                        // Obtengo los valores del rango
                        min = Double.parseDouble(((JTextField)message[10]).getText());
                        max = Double.parseDouble(((JTextField)message[11]).getText());
                        muestras = Integer.parseInt(((JTextField)message[13]).getText());
                        principal.setMuestras(muestras);
			File [] files = new File[pan.size()];
			pan.toArray(files);
			
			GeoImg [] imagesPan = new GeoImg[files.length];
    		for (int i = 0; i < files.length; i++)
    		{

    			GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
    			imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));
    		}
    		principal.setFilesPan(imagesPan);
			files = new File [multis.size()];
			multis.toArray(files);
			principal.setFilesMulti(files);     						    				   					    				    				    				
			int precis = 0;
			int interpol = principal.getInterpol();
			if (interpol==2)
			{
				precis = Integer.parseInt(((JTextField)message[8]).getText());
				principal.setPrecision(precis);    						
			}    				
			if(((JComboBox)message[1]).getSelectedItem().toString().equals("b3spline"))
			{
				principal.setWavelet(6);    					
			}    				
			principal.hallarPond(min, max, fast);
		}
		else
		{
			JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
		}    			    			    		
    		
	}
	/**
	 * Método que dibuja los diálogos necesarios para fusionar dos (o más) imágenes por el algoritmo á trous
	 * Será llamado desde Atrous
	 */
	public void dibujarDialogoFusionAtrous ()
	{
		List <File> pan = new ArrayList <File>();
		List <File> multis = new ArrayList <File>();
		if (crearDialogoAbrirPanAbrirMulti(pan,multis)==false) return;
		 
    			// Pido la wavelet que se utilizará
    			Object[] message = da.crearMensaje(DialogosAux.DIAG_ATROUS);
    			String [] options = da.crearOpciones(DialogosAux.OPT_ATROUS);			    			
    			int result = 0;
    			
    			boolean correcto = false;
    			while (!correcto)
    			{    				
    				result = JOptionPane.showOptionDialog(t,
                                        message,
                                        ic.buscar(101),
                                        JOptionPane.DEFAULT_OPTION, 
                                        JOptionPane.INFORMATION_MESSAGE, 
                                        null, 
                                        options, 
                                        options[0]); // Tag 101
    				if (!((JTextField)message[10]).getText().equals("") && !((JTextField)message[3]).getText().equals("") && !((JTextField)message[5]).getText().equals(""))
    				{// Está todo rellenado
    					correcto = true;
    				}			    				
    				if (result == 1)
    				{
    					correcto = true;
    				}
    				if (correcto != true)
    				{
    					JOptionPane.showMessageDialog(t, 
                                              ic.buscar(26),
                                              ic.buscar(21),
                                              JOptionPane.WARNING_MESSAGE); // Tag 26, 21
    				}
    			}
    			if (result == 0)
    			{	 
    				//Ahora llamo al nuevo procedimiento de Fusion, llamado fusionar
    				//Ya que pido los niveles, tengo que pasárselos a fusionar			    							    				
    				principal.setNivPan(Integer.parseInt(((JTextField)message[3]).getText()));
    				principal.setNivMulti(Integer.parseInt(((JTextField)message[5]).getText()));    				
    				principal.setPonderacion(Double.parseDouble(((JTextField)message[10]).getText()));
    				File [] files = new File[pan.size()];
    				pan.toArray(files);
    				
    				GeoImg [] imagesPan = new GeoImg[files.length];
    	    		for (int i = 0; i < files.length; i++)
    	    		{
    	    			
    	    			GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[i]);
    	    			
            			imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, files[i],Info.crearStreamData(ti),Info.crearImageData(ti));
            			
            			
    	    		}
    	    		principal.setFilesPan(imagesPan);
    				files = new File [multis.size()];
    				multis.toArray(files);
    				principal.setFilesMulti(files);    				    				
    				int precis = 0;
    				int interpol = principal.getInterpol();
    				System.out.println(ic.buscar(102)+interpol); // Tag 102
    				if (interpol==2)
    				{
    					precis = Integer.parseInt(((JTextField)message[8]).getText());
    					principal.setPrecision(precis);    						
    				}    				
    				if(((JComboBox)message[1]).getSelectedItem().toString().equals("b3spline"))
    				{
    					principal.setWavelet(6);
    				}    				
    				principal.atrous();
    			}
    			else
    			{
    				JOptionPane.showMessageDialog(t, ic.buscar(29)); // Tag 29
    			}    			    			    		
    		
}
	
    /**
     * Auxiliar que implementa la creacián del diálogo abrir una PAN, abrir
     * varias MULTI
     * @param pan Referencia a la imagen PAN cargada
     * @param multis Referencia a las imágenes MULTI cargadas
     * @return true si todo ha ido bien
     */
	private boolean crearDialogoAbrirPanAbrirMulti(final List<File> pan, final List<File> multis) {
		boolean correcto = true;
		fglobal.setMultiSelectionEnabled(false);
		fglobal.setDialogType(JFileChooser.OPEN_DIALOG);
    	fglobal.setDialogTitle(ic.buscar(103)); // Tag 103
    	ExampleFileFilter filter = 
                new ExampleFileFilter (
                    new String [] {"tiff",
                                   "tif",
                                   "jpg",
                                   "jpeg",
                                   "jpe",
                                   "bil"},ic.buscar(88)); // Tag 88
                                   fglobal.resetChoosableFileFilters();
    		fglobal.addChoosableFileFilter(filter);
    	
    	fglobal.setAccessory(new FilePreviewer(fglobal));
    	int retval = fglobal.showDialog(t, null);
    	
    	if (retval == JFileChooser.APPROVE_OPTION) {
    		pan.add(fglobal.getSelectedFile());
    		// Permito la seleccián de varias bandas
    		fglobal.setDialogTitle(ic.buscar(104)); // Tag 104
    		fglobal.setMultiSelectionEnabled(true);
    		retval = fglobal.showDialog(t, null);
    		if (retval == JFileChooser.APPROVE_OPTION) {
    			multis.addAll(Arrays.asList(fglobal.getSelectedFiles()));
    		}
    		else  
	    	{
	    		dibujarMensajesError(ic, retval);
	    		correcto = false;
	    	} 
	    	
    	}
    	else  
    	{
            dibujarMensajesError(ic, retval);
    		
    		correcto = false;
    		pan.clear();
    	} 
    	
    	return correcto;
		
	}



    /**
     * Crea el diálogo que permite importar imágenes BIL
     * @param principal Objeto principal
     * @param t JFrame principal
     */
	public void manejoBIL (Atrous principal, JFrame t)
	{
//		 Mostrar la cajita que pedirá los valores
		Object[] message = da.crearMensaje(DialogosAux.DIAG_BIL);
		String [] options = da.crearOpciones(DialogosAux.OPT_BIL);
		
		int result = JOptionPane.showOptionDialog(t,
                        message,
                        ic.buscar(1),
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        options, 
                        options[0]); // Tag 1
		if (result == 0)
		{
			principal.setBandas(Integer.parseInt(((JTextField)message[1]).getText()));
			principal.setLineas(Integer.parseInt(((JTextField)message[3]).getText()));
			principal.setColumnas(Integer.parseInt(((JTextField)message[5]).getText()));
			principal.setCeldas(Integer.parseInt(((JTextField)message[7]).getText()));

		}
		else
		{
			JOptionPane.showMessageDialog(t,
                                ic.buscar(2)); // Tag 2
		}	
	}
    /**
     * Auxiliar para mostrar excepciones por pantalla
     * @param e 
     */
	public static void informarExcepcion (Exception e)
	{
		JOptionPane.showMessageDialog(t,e.getMessage());
		e.printStackTrace();
		
	}

    /**
     * Auxiliar para mostrar un mensaje de error por pantalla
     * @param mensaje 
     */
	public static void informarError (String mensaje)
	{
		JOptionPane.showMessageDialog(t,mensaje);
		
	}



    @Override
	public void run() {
		
		
	}

    private void dibujarDialogoAND() {
        List <File> pan = new ArrayList <File>();
        List <File> multis = new ArrayList <File>();
        if (crearDialogoAbrirPanAbrirMulti(pan,multis)==false) return;
        GeoImg [] imagesPan = new GeoImg[pan.size()];
        for (int i = 0; i < pan.size(); i++) {
            
            GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(pan.get(i));
            
            imagesPan [i] = new GeoImg(Info.crearGeoData(ti), null, pan.get(i), Info.crearStreamData(ti),Info.crearImageData(ti));
            
            
        }
        principal.setFilesPan(imagesPan);
        File[] files = new File [multis.size()];
        multis.toArray(files);
        principal.setFilesMulti(files);
        principal.calculator(OP_AND);
                
    }

    private void dibujarManual() {
        
        es.upm.fi.gtd.pdfviewer.PDFViewer pv = new es.upm.fi.gtd.pdfviewer.PDFViewer(true);
        pv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
            pv.openFile(new File("manual.pdf"));
        } catch (IOException ex) {
            Logger.getLogger(Dialogos.class.getName()).log(Level.SEVERE, null, ex);
        }
        pv.setVisible(true);
        

    }
	
	
	
}