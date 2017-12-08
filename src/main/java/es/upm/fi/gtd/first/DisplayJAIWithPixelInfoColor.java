package es.upm.fi.gtd.first;


import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.text.DecimalFormat;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.measure.quantity.Length;
import javax.measure.unit.Unit;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.jscience.geography.coordinates.crs.ReferenceEllipsoid;

import com.sun.media.jai.codec.JPEGEncodeParam;
import java.awt.Rectangle;
/**
 * Clase hija de DisplayJAIWithPixelInfo que implementa varios arrays de double como
 * almacenamiento de los datos de imagen
 * @author Alvar
 */
public class DisplayJAIWithPixelInfoColor extends DisplayJAIWithPixelInfo implements MouseListener,ActionListener{





    /**
     * Versión para serialización
     */
    private static final long serialVersionUID = 1L;
    /**
     * the pixel information (formatted in a StringBuffer).
     */
    protected StringBuffer pixelInfo;
    /**
     *the pixel information as an array of doubles.
     */
    protected double[] dpixel;
    /**
     *the pixel information as an array of integers.
     */
    protected int[] ipixel;
    /**
     * Los valores de pixel en el caso de que sean de coma flotante
     */
    protected double[] dpixel_o;
    /**
     * Los valores de pixel en el caso de que sean enteros
     */
    protected int[] ipixel_o;
    /**
     * indicates which of the above arrays we will use
     */
    protected boolean isDoubleType;
    /**
     * a RandomIter that allow us to get the data on a single pixel.
     */
    protected RandomIter readIterator;
    /**
     *true if the image has a indexed color model.
     */
    protected boolean isIndexed;
    /**
     * will contain the look-up table data if isIndexed is true.
     */
    protected short[][] lutData;
    /**
     * JPopupMenu que se muestra al hacer clic-derecho
     */
    protected JPopupMenu popup;
    /**
     * Datos geográficos de la imagen contenida en este objeto
     */
    protected GeoData gdata;
    /**
     * Unidades lineales de los datos geográficos
     */
    protected String linearunits;
    /**
     * Zona geográfica donde se enmarcan los datos geográficos
     */
    protected String geozone;
    /**
     * Puntos fijos para la corrección de los datos geográficos
     */
    //protected double[][] tiepoints;
    /**
     * Escala espacial de los pixeles
     */
    protected double[] scales;
    /**
     * Unidades lineales en formato Java
     */
    Unit<Length> unidad;
    /**
     * Valor numérico de la zona UTM
     */
    int geozona;
    /**
     * Valor N/S de la zona UTM
     */
    char georef;
    /**
     * El elipsoide con el que se han proyectado los datos geográficos
     */
    ReferenceEllipsoid elipsoide;
    /**
     * El JFrame principal de la aplicación
     */
    
    protected JFrame t;
    /**
     * El objeto principal de la aplicación
     */
    protected Atrous principal;
    /**
     * Anchura de la imagen contenida en este objeto
     */
    protected int width;
    /**
     * Altura de la imagen contenida en este objeto
     */
    protected int height;
    /**
     * Valor mínimo de pixel de la imagen contenida en este objeto
     */
    protected double minValue;
    /**
     * Valor mínimo de pixel de la imagen contenida en este objeto
     */
    protected double maxValue;

    /**
     * Imagen original (distinta de la imagen mostrada)
     */
    protected PlanarImage imagen;
	  //protected PlanarImage surrogateImage;

 /**
  * The constructor of the class, which creates the arrays and instances needed
  * to obtain the image data and registers the class to listen to mouse motion
  * events.
  * @param image a RenderedImage for display
  * @param field JTextField para mostrar los datos geográficos
  * @param topitopi JFrame principal de la aplicación
  * @param princ Objeto principal de la aplicación
  * @param dpane true si el objeto está contenido en un JDesktopPane 
  */
	  public DisplayJAIWithPixelInfoColor(GeoImg image, JTextField field,
			JFrame topitopi, Atrous princ, boolean dpane) {

		super(image.getImage(), field, topitopi, princ, image.getData(), image
				.getStreamMetadata(), image.getImageMetadata(), dpane, image.getFile()); 
		imagen = image.getImage();
	    gdata = image.getData();
	    if (gdata!=null)
	    {

	    	scales = gdata.getScales();
	    	tiepoints = gdata.getTiePoints();
	    	geozone = gdata.getPCSType();
                if (geozone.equalsIgnoreCase("PCS no estándar")||geozone.equalsIgnoreCase("PCS no reconocido"))
                {}
                else
                {
	    	linearunits = gdata.getUnidadLineal();
			unidad = GeoInfo.obtenerGeounit(linearunits);
			geozona = GeoInfo.obtenerZona(geozone);
			georef = GeoInfo.obtenerGeoref(geozone);
			elipsoide = GeoInfo.obtenerGeoproj(geozone);
                }
	    }
	    else
	    {
	    	System.out.println("WARNUNG!: No hay datos geográficos");
	    }
	    t = topitopi;
	    principal = princ;
	    popup = new JPopupMenu();
	    JMenuItem menuItem = new JMenuItem(ic.buscar(38)); // Tag 38
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(49)); // Tag 49
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(125)); // Tag 125
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(126)); // Tag 126
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(127)); // Tag 127
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(128)); // Tag 128
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(129)); // Tag 129
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(130)); // Tag 130
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(131)); // Tag 131
            menuItem.setEnabled(false);
	    menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem(ic.buscar(132)); // Tag 132
            menuItem.setEnabled(false);
	    menuItem.addActionListener(this);
	    popup.add(menuItem);

	    //Add listener to components that can bring up popup menus.
	    this.addMouseListener(this);

	    readIterator = RandomIterFactory.create(image.getImage(), null); // creates the iterator.
	    // Get some data about the image
	    width = image.getImage().getWidth();
	    height = image.getImage().getHeight();
	    int dataType = image.getImage().getSampleModel().getDataType(); // gets the data type
	    switch(dataType)
	      {
	      case DataBuffer.TYPE_BYTE:
	      case DataBuffer.TYPE_SHORT:
	      case DataBuffer.TYPE_USHORT:
	      case DataBuffer.TYPE_INT: isDoubleType = false; break;
	      case DataBuffer.TYPE_FLOAT:
	      case DataBuffer.TYPE_DOUBLE: isDoubleType = true; break;
	      }
	    // Depending on the image data type, allocate the double or the int array.
	    Info.mostrarInfo(PlanarImage.wrapRenderedImage(image.getImage()));

	    if (isDoubleType) 
	    	{dpixel = new double[image.getImage().getSampleModel().getNumBands()];
	    		Raster ras = image.getImage().getData();
	    		dpixel_o = new double[width*height*dpixel.length];
	    		ras.getPixels(0, 0, width, height, dpixel_o);
	    	}
	    else 
	    	{
	    	ipixel = new int[image.getImage().getSampleModel().getNumBands()];
	    	Raster ras = image.getImage().getData();
	    	ipixel_o = new int[width*height*ipixel.length];
			ras.getPixels(0, 0, width, height, ipixel_o);
	    	
	    	}
	    
//	  Which are the max and min of the image ? We need to know to create the
	    // surrogate image.
	    // Let's use the extrema operator to get them.
	    ParameterBlock pbMaxMin = new ParameterBlock();
	    pbMaxMin.addSource(image.getImage());
	    RenderedOp extrema = JAI.create("extrema", pbMaxMin);
	    // Must get the extrema of all bands !
	    double[] allMins = (double[])extrema.getProperty("minimum");
	    double[] allMaxs = (double[])extrema.getProperty("maximum");
	    minValue = allMins[0];
	    System.out.println("Val min surr: "+minValue);
	    maxValue = allMaxs[0];
	    System.out.println("Val max surr: "+maxValue);
	    for(int v=1;v<allMins.length;v++)
	      {
	      if (allMins[v] < minValue) minValue = allMins[v];
	      if (allMaxs[v] > maxValue) maxValue = allMaxs[v];
	      }
	    surrogateImage = FusionAux.reducirRango(PlanarImage.wrapRenderedImage(image.getImage()), minValue, maxValue);
	    set(surrogateImage);
	    super.setSurrogate(surrogateImage);
	    
	    
	    // Is the image color model indexed ?
	    isIndexed = (image.getImage().getColorModel() instanceof IndexColorModel);
	    if (isIndexed)
	      {
	      // Retrieve the index color model of the image.
	      IndexColorModel icm = (IndexColorModel)image.getImage().getColorModel();
	      // Get the number of elements in each band of the colormap.
	      int mapSize = icm.getMapSize();
	      // Allocate an array for the lookup table data.
	      byte[][] templutData = new byte[3][mapSize];
	      // Load the lookup table data from the IndexColorModel.
	      icm.getReds(templutData[0]);
	      icm.getGreens(templutData[1]);
	      icm.getBlues(templutData[2]);
	      // Load the lookup table data into a short array to avoid negative numbers.
	      lutData = new short[3][mapSize];
	      for(int entry=0;entry<mapSize;entry++)
	        {
	        lutData[0][entry] = templutData[0][entry] > 0 ?
	              templutData[0][entry] : (short)(templutData[0][entry]+256);
	        lutData[1][entry] = templutData[1][entry] > 0 ?
	              templutData[1][entry] : (short)(templutData[1][entry]+256);
	        lutData[2][entry] = templutData[2][entry] > 0 ?
	              templutData[2][entry] : (short)(templutData[2][entry]+256);
	        }
	      } // end if indexed
	    // Registers the mouse motion listener.
	    addMouseMotionListener(this);
	    // Create the StringBuffer instance for the pixel information.
	    pixelInfo = new StringBuffer(50);
	    }
          
      /**
       * Establece en los metadatos las nuevas medidas de una imagen recortada
       * @param imagen Imagen
       * @param rect Nuevas medidas
       */
      public void setNuevaImagen(PlanarImage imagen, Rectangle rect)
        {
            /* En el rectángulo viene definido el nuevo tamaño de la imagen y, 
             * por tanto, porqué queremos actualizar los metadatos*/
            
            /* Parámetros a actualizar: 
             * Altura: Tag 257 (SHORT/LONG)
             * Anchura: Tag 256 (SHORT/LONG)
             * ModelPixelScaleTag: 33550 (DOUBLE)
             * ModelTiepointTag: 22922 (DOUBLE) 
             */
            
            GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter (imageData);
            ti.setTiffField(257, String.valueOf(rect.height));
            ti.setTiffField(256, String.valueOf(rect.width));
            //ti.setTiffFields(33550, tiepoints);
            
        }
	 
	 /**
	  * This method will be called when the mouse is moved over the image being
	  * displayed.
	  * @param me the mouse event that caused the execution of this method.
	  */
    @Override
	  public void mouseMoved(MouseEvent me)
	    {
                double facEscala = 1.0;
        if (escala >= 0)
        {
            facEscala = escala+1;
        }
        else
        {
            facEscala = Math.pow(2,escala);
        }
		  this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                            if (escala!=0)
          {
              readIterator = RandomIterFactory.create(this.source, null); // creates the iterator.
          }
                            else
                            {
                      readIterator = RandomIterFactory.create(imagen, null); // creates the iterator.
                            }
	    pixelInfo.setLength(0); // clear the StringBuffer
	    int x = me.getX();
	    int y = me.getY();
	    if ((x >= width*facEscala) || (y >= height*facEscala))
	      {
	      pixelInfo.append("No data!");
	      return;
	      }
	    if (isDoubleType) // process the pixel as an array of double values
	      {
	    	readIterator.getPixel(me.getX(),me.getY(),dpixel); // read the pixel
	        for(int b=0;b<dpixel.length;b++)
	        {
	        	if (gdata!=null)
	        	{
	        	double geoX = x*facEscala*gdata.getXScale()+gdata.getXTiePoint(1);
	        	double geoY = y*facEscala*gdata.getYScale()+gdata.getYTiePoint(1);
	        	principal.getStatus().setText(
                                ic.buscar(133)
                                +Double.toString(dpixel[b])
                                +"("
                                +x
                                +", "
                                +y
                                +")"
                                +
                                ic.buscar(135)
                                +geoX
                                +", "
                                +geoY
                                +ic.buscar(136)
                                +gdata.getPCSType()); // Tag 133, 135, 136
	        	}
	        	else
	        	{
	        		principal.getStatus().setText(
                                        ic.buscar(133)
                                        +Double.toString(dpixel[b])
                                        +"("
                                        +x
                                        +", "
                                        +y
                                        +ic.buscar(138)); // Tag 133, 138
	        	}
	        }	     
	      }
	    else // pixel type is not floating point, will be processed as integers.
	      {
	      if (isIndexed) // if color model is indexed
	        {
	        //pixelInfo.append("(integer data with colormap) ");
	        readIterator.getPixel(me.getX(),me.getY(),ipixel); // read the pixel
	        // Assume ipixel.length = 1
	        //pixelInfo.append("Index: "+ipixel[0]);
	        // Add also the RGB entry from the LUT.
	        //pixelInfo.append(" RGB:"+lutData[0][ipixel[0]]+","+
	                            //     lutData[1][ipixel[0]]+","+
	                              //   lutData[2][ipixel[0]]);
	        for(int b=0;b<ipixel.length;b++)
	        {
	        	principal.getStatus().setText(" RGB:"+lutData[0][ipixel[0]]+","+
	                    lutData[1][ipixel[0]]+","+
	                    lutData[2][ipixel[0]]+"("+x+", "+y+")");
	        }
	        }
	      else // pixels are of integer type, but not indexed
	        {
	        //pixelInfo.append("(integer data) ");
	        readIterator.getPixel(me.getX(),me.getY(),ipixel); // read the pixel
	        String valpixel = new String(ic.buscar(133)); // Tag 133
	    	  for(int b=0;b<ipixel.length;b++)
	    	  {
	    		  valpixel = valpixel.concat(
                                  ic.buscar(134)
                                  +b
                                  +": "
                                  +ipixel[b]
                                  +", "); // Tag 134
	    	  }
	    	  if (gdata!=null)
	    	  {
	    		  double geoX=0, geoY=0;
	    		  if (tiepoints[0][0]==0 && tiepoints[0][1]==0)
	    		  {
	    			  geoX = x*facEscala*scales[0]+tiepoints[0][3];
	    			  geoY = y*facEscala*scales[1]+tiepoints[0][4];
	    		  }
	    		  else
	    		  {
	    			  double despx = x-tiepoints[0][0];
	    			  double despy = y-tiepoints[0][1];
	    			  geoX = Math.abs(despx)*facEscala*scales[0]+(Math.signum(despx)*tiepoints[0][3]);
	    			  geoY = Math.abs(despy)*facEscala*scales[1]+(Math.signum(despy)*tiepoints[0][4]);
	    		  }
	    		  
	    		  DecimalFormat df = new DecimalFormat("0.000"); //No me gusta poner punto
	      		  String geoXs = df.format(geoX);
	      		  String geoYs = df.format(geoY);
	      		String [] latlong = GeoInfo.convertirUTM(geozona, georef, geoX, geoY, unidad, elipsoide, ic);
	  		  principal.getStatus().setText(
                                  valpixel
                                  +"("
                                  +x
                                  +", "
                                  +y
                                  +")"
                                  +ic.buscar(135)
                                  +geoXs
                                  +" "
                                  +linearunits
                                  +", "
                                  +geoYs
                                  +" "
                                  +linearunits
                                  +") | Lat: "
                                  +latlong[0]
                                  +" Long: "
                                  +latlong[1]
                                  +ic.buscar(136)
                                  +geozone); // Tag 135, 136
	    	  }
	    	  else
	    	  {
	    		  principal.getStatus().setText(
                                  valpixel
                                  +"("
                                  +x
                                  +", "
                                  +y
                                  +ic.buscar(138)); // Tag 138
	    	  }
	        	
	        }
	      } // pixel is integer type
	    } // end of method mouseMoved

              /**
     * Obtiene el foco de la ventana al hacer clic sobre ella (necesario para
     * procesar KeyEvents). Si es el botón de menú contextual, lo muestra.
     * @param e Evento recibido
     */
    @Override
	  public void mousePressed(MouseEvent e) {
          super.mousePressed(e);
		  this.requestFocusInWindow(true);
	      maybeShowPopup(e);
	  }
    /**
     * Si soltar el botón es la acción para el menú contextual en la plataforma
     * lo muestra.
     * @param e Evento recibido
     */
    @Override
	  public void mouseReleased(MouseEvent e) {
	      maybeShowPopup(e);
	  }
	    /**
     * Muestra el menú de clic-derecho si en la plataforma actual se ha pulsado
     * efectivamente el botón de menú contextual
     * @param e Evento recibido
     */  
	  private void maybeShowPopup(MouseEvent e) {
	      if (e.isPopupTrigger()) {
	          popup.show(e.getComponent(),
	                     e.getX(), e.getY());

	      }
	  }
	  
	 /**
	 * This method allows external classes access to the pixel info which was obtained in the mouseMoved method.
	 * @return  the pixel information, formatted as a string
	 * @uml.property  name="pixelInfo"
	 */
	  public String getPixelInfo()
	    {
	    return pixelInfo.toString();
	    }
    /**
     * Elige la acción adecuada entre las opciones posibles en el menú de clic-
     * derecho
     * @param e El evento recibido
     */
    @Override
	  public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().compareTo(ic.buscar(38))==0) // Tag 38
			{
				guardar();
			}
			else if (e.getActionCommand().compareTo(ic.buscar(49))==0) // Tag 49
			{
				degradar();
			}
			else if (e.getActionCommand().compareTo(ic.buscar(125))==0) // Tag 125
			{
				fusionar1();
			}
			else if (e.getActionCommand().compareTo(ic.buscar(126))==0) // Tag 126
			{
				fusionar2();		
			}
			else if (e.getActionCommand().compareTo(ic.buscar(127))==0) // Tag 127
			{
				atrous1();
			}
			else if (e.getActionCommand().compareTo(ic.buscar(128))==0) // Tag 128
			{
				atrous2();		
			}
			else if (e.getActionCommand().compareTo(ic.buscar(129))==0) // Tag 129
			{
				pond1();		
			}
			else if (e.getActionCommand().compareTo(ic.buscar(130))==0) // Tag 130
			{
				pond2();
			}
			else if (e.getActionCommand().compareTo(ic.buscar(131))==0) // Tag 131
			{
				//ergas1();
			}
			else if (e.getActionCommand().compareTo(ic.buscar(132))==0) // Tag 132
			{
				//ergas2();
			}
			
		}

	      /**
     * Salva la imagen contenida en este objeto a un fichero de imagen con la
     * profundidad de bits elegida por el usuario
     */
	  
	  private void guardar() {
			
			principal.getFglobal().setDialogTitle(ic.buscar(38)); // Tag 38

				principal.getFglobal().setDialogType(JFileChooser.SAVE_DIALOG);

			Accesorio pep = new Accesorio(principal.getFglobal(),t, principal.getIC());
			principal.getFglobal().setAccessory(pep);
			pep.setProfundidad(true);

			
			                principal.getFglobal().resetChoosableFileFilters();
                
                ExampleFileFilter filtroT = new ExampleFileFilter (
                            new String [] {"tiff",
                                "tif",
                                },
                                ic.buscar(3)); // Tag 3
                

                
			
                        ExampleFileFilter filtroJ =  new ExampleFileFilter (
                            new String [] {"jpeg",
                                "jpg"
                                },
                                ic.buscar(4)); // Tag 4
                        principal.getFglobal().addChoosableFileFilter(filtroT);
                        principal.getFglobal().addChoosableFileFilter(filtroJ);
		
		
		
			
			
			int retval = principal.getFglobal().showDialog(t, null);
			if (retval == JFileChooser.APPROVE_OPTION) 
			{		
				File guardar = principal.getFglobal().getSelectedFile();
				if (	principal.getFglobal().getFileFilter().getDescription().equals(filtroJ.getDescription()))
				{// Guardar como JPEG
					JAI.create("filestore",this.source,guardar.getPath(),"JPEG", new JPEGEncodeParam());
				}
				else if (principal.getFglobal().getFileFilter().getDescription().equals(filtroT.getDescription()))
				{// Guardar como TIFF
					int profundidad = pep.getEP().getMarcada();
					System.out.println(ic.buscar(139)+profundidad); // Tag 139
					TiledImage tiled =null;
					if (profundidad == ElegirProfundidad.VEINTI || profundidad == ElegirProfundidad.CUARENTA)
					{

					 		GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter (imageData);
					 		ti.setTiffField(258, String.valueOf(profundidad));
					 		String format = imageData.getNativeMetadataFormatName();
					 		IIOMetadataNode root = ti.getRootNode();
					 		try {
								imageData.setFromTree(format, root);
							} catch (IIOInvalidTreeException e1) {

								e1.printStackTrace();
							}
							ParameterBlock pb = new ParameterBlock();
					 		switch (profundidad)
					 		{
					 		case ElegirProfundidad.VEINTI:
					 		{	
					 			if ((imagen.getNumBands() == 3) && (imagen.getData().getTransferType() == DataBuffer.TYPE_BYTE))
					 			{
					 				pb.addSource(imagen);			 						 			
					 				break;
					 			}
					 			else
					 			{
					 				pb.addSource(super.surrogateImage);
					 				break;
					 			}
					 		}
					 		case ElegirProfundidad.CUARENTA:
					 		{
					 			if ((imagen.getNumBands() == 3) && (imagen.getData().getTransferType() == DataBuffer.TYPE_USHORT))
					 			{
					 				pb.addSource(imagen);
					 			}
					 		}
					 		}
					 		pb.add(guardar);
				 			pb.add("TIFF");
				 			pb.add(false); //UseProperties
				 			pb.add(false); //Transcode
				 			pb.add(true); //VerifyOutput
				 			pb.add(false); //AllowPixelReplacement
				 			pb.add(null); //TileSize
				 			pb.add(streamData);
				 			pb.add(imageData);
				 			JAI.create("imagewrite", pb);
					 		
					}
					else
					{// Intenta guardarlo a partir de los datos a granel
						if (ipixel_o!=null)
						{
							tiled = ElegirProfundidad.crearProfundidadReducida(ipixel_o, profundidad, this.source.getWidth(), this.source.getHeight());
						}
						else if (dpixel_o!=null)
						{
							tiled = ElegirProfundidad.crearProfundidadReducida(dpixel_o,profundidad, this.source.getWidth(), this.source.getHeight());
						}

						JAI.create("imagewrite",tiled,guardar.getPath(),"TIFF");
					}
				}
				else
				{// No tiene extensión o es una extensión extraña
					JAI.create("imagewrite",this.source,guardar.getPath()+".tif","TIFF");
				}
				
			}			    
			else 
			{
				dibujarMensajesError(retval);
			} 
			
			
		}
	  
    /**
     * Devuelve los valores de pixel de la imagen contenida en este objeto
     * @return Un double[] con los valores de pixel
     */
		public double[] getDataDouble() {
			
			if (isDoubleType)
			{
				return dpixel_o;
			}
			else
			{
				return null;
			}

		}
   /**
     * Devuelve los valores de pixel de la imagen contenida en este objeto
     * @return Un byte[] con los valores de pixel
     */
		public int[] getDataInteger() {
			
			if (!isDoubleType)
			{
				return ipixel_o;
			}
			else
			{
				return null;
			}

		}
                
        /**
         * Obtiene la imagen original
         * @return Imagen original
         */
        @Override
	  public RenderedImage getSource()
          {
            return imagen;
          }

    /**
     * Obtiene los metadatos de stream (fichero)
     * @return Metadatos Stream
     */
    @Override
    public IIOMetadata getStreamData() {
        return super.streamData;
    }

    /**
     * Obtiene los metadatos de imagen
     * @return Metadatos Image
     */
    @Override
    public IIOMetadata getImageData() {
        return super.imageData;
    }

    
    /**
     * Obtiene los puntos fijos de la información geográfica
     * @return Los puntos fijos como un array de arrays de double
     */
    @Override
    public double[][] getTiePoints() {
        return tiepoints;
    }

}
