package es.upm.fi.gtd.first;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import javax.swing.JViewport;



/**
 * Esta clase separa la construcción del interfaz (responsabilidad de la clase Atrous) del
 * manejo de las imágenes mostradas.
 * 
 * @author Alvar García del Río
 *
 */

public class Mostrar {
	
	/**
	 * Carga un conjunto de nombres de archivo
	 * @param files Nombres de archivo
	 * @param bandas Bandas (en el caso de un archivo ASCII BIL)
	 * @param lineas Líneas (en el caso de un archivo ASCII BIL)
	 * @param columnas Columnas (en el caso de un archivo ASCII BIL)
	 * @param celdas Celdas (en el caso de un archivo ASCII BIL)
	 * @return Un conjunto de imágenes renderizadas
	 * @throws IOException
	 * @see Atrous#manejoBIL()
	 */
	
	
	
	

	
	
	protected static GeoImg [] mostrarImagen2(GeoImg[] files, int bandas, int lineas, int columnas, int celdas) throws IOException {
		// Mostrando en DESKTOP PANE
		// Precondición: files != null
		
		GeoImg[] outp = null;
		ImageInfo ii = new ImageInfo();
		FileSeekableStream stream = null;
		
			if (files.length==1)
			{	// UN archivo
				stream = new FileSeekableStream(files[0].getFile());
				ii.setInput((InputStream)stream);
				ii.check();
				if (ii.getFormat()==ImageInfo.FORMAT_TIFF)
				{	// Leyendo TIFF
																																
					//A lo brasileiro
					ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, null);
					
					
					int paginas=0;
					paginas = dec.getNumPages();
					
					outp = new GeoImg[paginas];
					for (int i=0; i<paginas; i++)
					{
						RenderedImage ri=null;
						ri = dec.decodeAsRenderedImage(i);						
						
						outp[i] = 
                                                        new GeoImg(
                                                            files[0].getData(),
                                                            PlanarImage.wrapRenderedImage(ri),
                                                            files[0].getFile(),
                                                            files[0].getStreamMetadata(),
                                                            files[0].getImageMetadata());
					}									
					return outp;

				}  // FIN si es TIFF
				else
				{ 
//					 Aquí llamo al método de conversión de BIL
					if (bandas!=0)
					{
                                            System.out.println("Entro por la parte de BIL");
						AbrirBIL a = new AbrirBIL();
						TiledImage[] imgs = new TiledImage[bandas];
						imgs=a.transBIL(files[0].getFile(),bandas,lineas,columnas,celdas);						
						
						outp = new GeoImg[bandas];
						for (int i = 0; i < bandas; i++)
						{
							outp[i] = 
                                                                new GeoImg(
                                                                    files[0].getData(),
                                                                    imgs[i],
                                                                    files[0].getFile(),
                                                                    files[0].getStreamMetadata(),
                                                                    files[0].getImageMetadata());
						}
						return outp;
						
						
					}
					// NO es TIFF
					PlanarImage image = JAI.create("imageread",files[0].getFile().getPath());
					
					outp = new GeoImg[1];
					outp[0] = 
                                                new GeoImg(
                                                    files[0].getData(),
                                                    image,
                                                    files[0].getFile(),
                                                    files[0].getStreamMetadata(),
                                                    files[0].getImageMetadata());
					
					return outp;
				}
				
				
			}
			else
			{				
				// VARIOS archivos NO multibanda
				
				outp = new GeoImg[files.length];
				for (int i=0; i<files.length; i++)
				{
					PlanarImage image = JAI.create("imageread",files[i].getFile().getPath());
					
					outp[i] = 
                                                new GeoImg(
                                                    files[i].getData(),
                                                    image,
                                                    files[i].getFile(),
                                                    files[i].getStreamMetadata(),
                                                    files[i].getImageMetadata());
				}
				return outp;
			}
	
		}
	


	

	
    /**
     * Muestra las imágenes en un AreaTrabajo
     * @param imgs Imágenes a mostrar
     * @param dpanel AreaTrabajo de la aplicación
     * @param t JFrame principal de la aplicación
     * @param status JTextField donde mostrar datos de interés
     * @param principal Objeto principal de la aplicación
     * @return AreaTrabajo con las imágenes nuevas
     */
	public static AreaTrabajo mostrarImagen2(
                List <GeoImg> imgs, 
                AreaTrabajo dpanel, 
                JFrame t, 
                JTextField status, 
                Atrous principal)
	{
    ParseXML ic = principal.getIC();
			dpanel = new AreaTrabajo(principal);
			
			

			// Le añado el JInternalFrame con la lista
				//ListaImagenes lista = new ListaImagenes(imgs,principal);
				//JScrollPane pane = lista.getScrollPane();
				//PanelImagenes pi = lista.getPanelImagenes();
				//dpanel.add(lista,JDesktopPane.PALETTE_LAYER);


		Toolkit a = Toolkit.getDefaultToolkit();
		Dimension scrRes = a.getScreenSize();
		//dpanel.add(pi,JDesktopPane.PALETTE_LAYER);
		
		//pi.setVisible(true);
		
		for (int i = 0; i < imgs.size(); i++){
			JInternalFrame jif;
			
			
			if (imgs.get(i).getFile() == null)
			{

				jif = new JInternalFrame (ic.buscar(162)+i, true, true, true, true); // Tag 162
                                jif.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
			}
			else
			{
				jif = new JInternalFrame (imgs.get(i).getFile().getName(), true, true, true, true);
                                jif.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
			}

			if (imgs.get(i).getImage().getNumBands() != 1)
			{
				jif.getContentPane().add(
                                        new JScrollPane(
                                            new DisplayJAIWithPixelInfoColor(
                                                imgs.get(i),
                                                status,
                                                t,
                                                principal,
                                                true)));
			}
			else
			{
				int tipo = imgs.get(i).getImage().getSampleModel().getTransferType();

				if (tipo == DataBuffer.TYPE_INT)
				{
					jif.getContentPane().add(
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoInt(
                                                        imgs.get(i),
                                                        status,
                                                        t,
                                                        principal,
                                                        true)));
				}
				else if (tipo == DataBuffer.TYPE_FLOAT)
				{
					jif.getContentPane().add(
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoFloat(
                                                    imgs.get(i),
                                                    status,
                                                    t,
                                                    principal,
                                                    true)));
				}
				else if (tipo == DataBuffer.TYPE_USHORT)
				{
					jif.getContentPane().add(
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoShort(
                                                        imgs.get(i),
                                                        status,
                                                        t,
                                                        principal,
                                                        true)));
				}
				else
				{
					jif.getContentPane().add(
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoByte(
                                                        imgs.get(i),
                                                        status,
                                                        t,
                                                        principal,
                                                        true)));
				}
			}

			jif.addInternalFrameListener(dpanel);
			//dpanel.add(jif,i);
                        dpanel.add(jif);
			if (imgs.get(i).getImage().getWidth()>scrRes.width)
			{
				try
				{
					jif.setMaximum(true); // Maximiza el frame interno si es demasiado grande para la pantalla
				}
				catch (PropertyVetoException pve)
				{
					Dialogos.informarExcepcion(pve);
				}
			}
			jif.setVisible(true);
			// Si hay varias, separa las ventanitas para que no se solapen
			jif.setBounds(
                                20*(i%10), 
                                20*(i%10), 
                                imgs.get(i).getImage().getWidth(), 
                                imgs.get(i).getImage().getHeight()); 
			jif.pack();
			
		}
                	
		
                dpanel.setVisible(true);
                JScrollPane jsp = new JScrollPane(dpanel);
                t.add(jsp, BorderLayout.CENTER);
                t.pack();
                t.setExtendedState(t.getExtendedState()|JFrame.MAXIMIZED_BOTH);
                t.setVisible(true);
		
                JViewport jv = jsp.getViewport();
                jv.addChangeListener(dpanel);
                
                
		
		
		
                dpanel.addLista(imgs);
		
		
                
                
		return dpanel;
		
	}
	


	/**
	 * Carga las imágenes sin tener en cuenta si es un archivo BIL
	 * 
	 * @param files Nombres de archivo de las imágenes
     * @param separadas true si se desean cargar los ficheros multibanda en
     * imágenes diferentes, false si se desea cargar la imagen multibanda en una
     * sola imagen
     * @return Las imágenes renderizadas
	 */	

	
	
	protected static GeoImg[] mostrarImagen(GeoImg[] files, boolean separadas) {
		//Sólo carga y no muestra
		//Precondición files!=null
		List <GeoImg>output = new ArrayList<GeoImg>();
		
		for (int i = 0; i < files.length; i++)
		{
			RenderedImage imagen2 = JAI.create("imageread", files[i].getFile().getPath());
			// Parte que detecta si es multipágina
			FileSeekableStream stream = null;
			try {
				stream = new FileSeekableStream(files[i].getFile());
				} 
        	catch (IOException e) {
        		Dialogos.informarExcepcion(e);
        	}
        	ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, null);
        	int paginas=0;
        	ImageInfo ii = new ImageInfo();
    		ii.setInput((InputStream)stream);
    		ii.check();
        	try
        	{
        		
        		
        		if (ii.getFormat() == ImageInfo.FORMAT_TIFF)
        		{
        		   paginas = dec.getNumPages();
        		   System.out.println("No sólo es TIFF, sino que tiene "+paginas);
        		   
        		}
        	}
        	catch (IOException e) {
        		Dialogos.informarExcepcion(e);
        	}
        	// Fin de la parte de detección de multipágina
        	// Parte en la que detecta las bandas (BSQ)
        	if (ii.getFormat() == ImageInfo.FORMAT_TIFF)
			{
				int nbands = imagen2.getSampleModel().getNumBands();
				List <GeoImg>bandas;
				// Preguntamos al usuario si quiere leer la imagen como bandas separadas o no

				if (nbands > 1 && paginas == 1 && separadas)
				{// TIFF multibanda
					bandas = new ArrayList<GeoImg>();
					for ( int j = 0; j < nbands; j++ )
					{
					    ParameterBlockJAI selectPb = new ParameterBlockJAI( "BandSelect" );
					    selectPb.addSource( imagen2 );
					    selectPb.setParameter( "bandIndices", new int[] { j } );

					    RenderedImage singleBandImage = JAI.create( "BandSelect", selectPb );

					    bandas.add(
                                                    j,
                                                    new GeoImg(
                                                        files[i].getData(),
                                                        PlanarImage.wrapRenderedImage(singleBandImage),
                                                        new File(files[i].getFile().getName()+" - Banda "+(j+1)),
                                                        files[i].getStreamMetadata(),
                                                        files[i].getImageMetadata())); 
					}
					output.addAll(bandas);
				}
				else if (nbands > 1 && paginas!=1)
				{
					for (int j=0; j<paginas; j++)
					{
						RenderedImage ri=null;
						try
						{
							ri = dec.decodeAsRenderedImage(j);				
						}
						catch (IOException ioe) {
							Dialogos.informarExcepcion(ioe);
						}
						output.add(
                                                        j, 
                                                        new GeoImg(
                                                            files[i].getData(),
                                                            PlanarImage.wrapRenderedImage(ri),
                                                            new File(files[i].getFile().getName()+" - Página "+j),
                                                            files[i].getStreamMetadata(),
                                                            files[i].getImageMetadata()));
					}
				}
				else if (!separadas)
				{
					output.add(
                                                0, 
                                                new GeoImg(
                                                    files[i].getData(),
                                                    PlanarImage.wrapRenderedImage(imagen2),
                                                    files[i].getFile(),
                                                    files[i].getStreamMetadata(),
                                                    files[i].getImageMetadata()));
				}
				
			}
			else
			{								
				output.add(
                                        0, 
                                        new GeoImg(
                                            files[i].getData(),
                                            PlanarImage.wrapRenderedImage(imagen2),
                                            files[i].getFile(),
                                            files[i].getStreamMetadata(),
                                            files[i].getImageMetadata()));				
			}			
		}
		GeoImg [] outputA = new GeoImg[output.size()];
		output.toArray(outputA);
		return outputA;
	}
	


	/*public JTabbedPane mostrarImagen(PlanarImage[] suma, JFrame t, JTabbedPane tpanel,JTextField status, Atrous principal) {
		
		//JPanel panel = null;



		// Limpiar

		tpanel = new JTabbedPane();


		for (int i = 0; i < suma.length; i++)
		{

			//panel = new JPanel();
			JScrollPane panel;
			panel = new JScrollPane(new DisplayJAIWithPixelInfoInt(suma[i],status,t,principal));			
			tpanel.add("Imagen "+i,panel);

		}
		tpanel.setVisible(true);		
		t.add(tpanel,BorderLayout.CENTER);
		t.pack();
		t.setVisible(true);
		tpanel.repaint();
		return tpanel;

	}*/
	
       /**
	 * Muestra las imágenes en el JTabbedPane global de la aplicación
	 * 
	 * @param suma Imágenes a mostrar
	 * @param t JFrame de la aplicación (necesario para poder maximizarlo)
	 * @param tpanel JTabbedPane global de la aplicación
	 * @param status JTextField donde se muestra la información de pixel
        * @param principal Objeto principal de datos y GUI de la aplicación
        * @return JTabbedPane con las nuevas pestañas añadidas
	 */
public static JTabbedPane mostrarImagen(
        List <GeoImg> suma, 
        JFrame t, 
        JTabbedPane tpanel,
        JTextField status, 
        Atrous principal) {
		
		//JPanel panel = null;



		// Limpiar

		tpanel = new JTabbedPane();


		for (int i = 0; i < suma.size(); i++)
		{

			//panel = new JPanel();
			JScrollPane panel;
			int tipo = suma.get(i).getImage().getSampleModel().getTransferType();
			if (suma.get(i).getImage().getNumBands() != 1)
			{
				panel = 
                                        new JScrollPane(
                                            new DisplayJAIWithPixelInfoColor(
                                                suma.get(i),
                                                status,
                                                t,
                                                principal,
                                                false));
			}
			else
			{
				if (tipo == DataBuffer.TYPE_INT)
				{
					panel = 
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoInt(
                                                        suma.get(i),
                                                        status,
                                                        t,
                                                        principal,false));
				}
				else if (tipo == DataBuffer.TYPE_FLOAT)
				{
					panel = 
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoFloat(
                                                        suma.get(i),
                                                        status,
                                                        t,
                                                        principal,
                                                        true));
				}
				else if (tipo == DataBuffer.TYPE_USHORT)
				{
					panel = 
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoShort(
                                                        suma.get(i),
                                                        status,
                                                        t,
                                                        principal,
                                                        false));
				}
				else
				{
					panel = 
                                                new JScrollPane(
                                                    new DisplayJAIWithPixelInfoByte(
                                                        suma.get(i),
                                                        status,
                                                        t,
                                                        principal,
                                                        false));
				}
			}
			
			if (suma.get(i).getFile() == null)
			{
				tpanel.add("Imagen "+i,panel);
			}
			else
			{
				/*Tener en cuenta las imágenes que han 
                                sido obtenidas de imágenes MultiBanda o Multipágina*/
				tpanel.add(suma.get(i).getFile().getName(),panel);
			}

		}
		tpanel.setVisible(true);		
		t.add(tpanel,BorderLayout.CENTER);
		t.pack();
		t.setVisible(true);
		tpanel.repaint();
		return tpanel;

	}
	
	/**
	 * Se ocultan los componentes gráficos globales (JDesktopPane y JTabbedPane)
	 * @param t JFrame de la aplicación
	 * @param dpanel JDesktopPane global de la aplicación
     * @param tpanel JTabbedPane global de la aplicación
     * @param status JTextField que contiene la barra de estado de la aplicación
	 */
	
	protected static void cerrarImagen(JFrame t, AreaTrabajo dpanel, 
                    JTabbedPane tpanel, JTextField status)
	{
		if (dpanel != null)
		{
                    //dpanel.removeAll();
		    dpanel.getParent().getParent().setVisible(false);                        
                        
		}
		if (tpanel != null)
		{
			tpanel.setVisible(false);
                        
		}
		
                status.setText("");
		t.pack();
                System.gc();
	}
	



}


