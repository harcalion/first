/*
 * @(#)FilePreviewer.java	
 * 
 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

package es.upm.fi.gtd.first;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/**
 * Implementa la vista en miniatura para el componente personalizado del
 * JFileChooser global
 * @author  Jeff Dinkins
 * @author  Alvar García del Río
 */


public class FilePreviewer extends JComponent implements PropertyChangeListener {

    /**
     * Versión para serialización
     */
	private static final long serialVersionUID = 1629545192959875024L;
        /**
         * Imagen en miniatura de tipo normal (GIF, JPG, etc.)
         */
	ImageIcon thumbnail = null;
        /**
         * Imagen en miniatura TIFF
         */
    IconJAI dip = null;
	/**
         * Texto de información prefijo
         */
    final static String FILE_SIZE_PREFIX = "";
    /** 
     * Constante para reducir el tamaño de la información mostrada
     */
    final static long SIZE_KB = 1024;
        /** 
     * Constante para reducir el tamaño de la información mostrada
     */
    final static long SIZE_MB = SIZE_KB * 1024;
        /** 
     * Constante para reducir el tamaño de la información mostrada
     */
    final static long SIZE_GB = SIZE_MB * 1024;
        /** 
     * Constante de tamaño de la imagen en miniatura
     */
    final static int iconSizeX = 150;
            /** 
     * Constante de tamaño de la imagen en miniatura
     */
    final static int iconSizeY = 100;
    /**
     * Componente sobre el que mostrar la imagen en miniatura y el resto de 
     * información
     */
    JLabel filesizelabel = new JLabel();
        /**
     * Componente sobre el que mostrar la imagen en miniatura y el resto de 
     * información
     */
    JLabel iconlabel = new JLabel();
        /**
     * Componente sobre el que mostrar la imagen en miniatura y el resto de 
     * información
     */
    JPanel infoPanel = new JPanel();
    /**
     * Anchura de la imagen en miniatura
     */
    static int width = 0;
    /**
     * Altura de la imagen en miniatura
     */
    static int height = 0;
    int x;
    int y;
   
    /**
     * Constructor
     * @param fc El JFileChooser al que añadirle esta imagen en miniatura
     */
    public FilePreviewer(JFileChooser fc) {
    	this.setLayout(new GridBagLayout());
    	filesizelabel.setHorizontalAlignment(SwingConstants.CENTER);
        filesizelabel.setHorizontalTextPosition(SwingConstants.CENTER);
        filesizelabel.setText("");
        infoPanel.setLayout(new BorderLayout());
        iconlabel.setMaximumSize(new Dimension(iconSizeX, iconSizeY));
        iconlabel.setMinimumSize(new Dimension(iconSizeX, iconSizeY));
        iconlabel.setPreferredSize(new Dimension(iconSizeX, iconSizeY));
        iconlabel.setHorizontalAlignment(SwingConstants.CENTER);

	//setPreferredSize(new Dimension(100, 50));
	fc.addPropertyChangeListener(this);
	//setBorder(new BevelBorder(BevelBorder.LOWERED));
	add(infoPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	infoPanel.add(filesizelabel,BorderLayout.CENTER);
	add(iconlabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    
	
    }
    
    /**
     * Obtiene un String con la información del tamaño de archivo a partir de su
     * valor numérico, con un formato reducido en función del tamaño del fichero
     * @param fileSize long con el valor numérico del tamaño del fichero
     * @return String con el valor del tamaño y sus unidades
     */
    private String getFileSizeString(long fileSize) {
        String fileSizeString;
        if (fileSize < SIZE_KB) {
            fileSizeString = FILE_SIZE_PREFIX + fileSize;
        } else if (fileSize < SIZE_MB) {
            fileSizeString = FILE_SIZE_PREFIX +
                    (int) ((double) fileSize / SIZE_KB + 0.5) + "KB";
        } else if (fileSize < SIZE_GB) {
            fileSizeString = FILE_SIZE_PREFIX +
                    (int) ((double) fileSize / SIZE_MB + 0.5) + "MB";
        } else {
            fileSizeString = FILE_SIZE_PREFIX +
                    (int) ((double) fileSize / SIZE_GB + 0.5) + "GB";
        }

        return fileSizeString;
    }
    
    /**
     * Carga la imagen marcada
     * @param f El fichero que está marcado en el JFileChooser
     */
    public void loadImage(File f) {

    	
    	if (f == null)
    	{
    		thumbnail = null;
    		dip = null;
    	}
    	else 
    	{
        	
        	if (f.isFile())
        	{
        		String ext = f.getName(); 
        		long filesize = f.length();
        		filesizelabel.setText(getFileSizeString(filesize));
        		filesizelabel.setVisible(true);
        		int pos = ext.indexOf(new String("."));
        		ext = ext.substring(pos+1);
        	
        		if (ext.equalsIgnoreCase("tiff") || ext.equalsIgnoreCase("tif"))
        		{
        			thumbnail = null;
        			RenderedImage image = JAI.create("imageread",f.getPath());
        			 ParameterBlock pbMaxMin = new ParameterBlock();
        			    pbMaxMin.addSource(image);
        			    RenderedOp extrema = JAI.create("extrema", pbMaxMin);
        			    // Must get the extrema of all bands !
        			    double[] allMins = (double[])extrema.getProperty("minimum");
        			    double[] allMaxs = (double[])extrema.getProperty("maximum");
        			    double minValue = allMins[0];
        			    double maxValue = allMaxs[0];
        			    for(int v=1;v<allMins.length;v++)
        			      {
        			      if (allMins[v] < minValue) minValue = allMins[v];
        			      if (allMaxs[v] > maxValue) maxValue = allMaxs[v];
        			      }
        			    // Rescale the image with the parameters
        			    double[] subtract = new double[1]; subtract[0] = minValue;
        			    double[] divide   = new double[1]; divide[0]   = 255./(maxValue-minValue);
        			    // Now we can rescale the pixels gray levels:
        			    ParameterBlock pbRescale = new ParameterBlock();
        			    pbRescale.add(divide);
        			    pbRescale.add(subtract);
        			    pbRescale.addSource(image);
        			    PlanarImage surrogateImage = (PlanarImage)JAI.create("rescale", pbRescale,null);
        			    // Let's convert the data type for displaying.
        			    ParameterBlock pbConvert = new ParameterBlock();
        			    pbConvert.addSource(surrogateImage);
        			    pbConvert.add(DataBuffer.TYPE_BYTE);
        			    surrogateImage = JAI.create("format", pbConvert);
        			/*RenderedImage image = null;
					try {
						image = ImageIO.read(f);
					} catch (IOException e) {
						Dialogos.informarExcepcion(e);
					}*/
        			x = image.getWidth();    		
        			y = image.getHeight();
        			float scalex = 90/(float)x;
        			float scaley = 90/(float)y;
        			ParameterBlock pb = new ParameterBlock();
        			pb.addSource(surrogateImage);
        			pb.add(scalex);
        			pb.add(scaley);
        			pb.add(0.0F);
        			pb.add(0.0F);
        			pb.add(new InterpolationNearest());
        			RenderedImage thumb = JAI.create("scale", pb, null);

        			String texto = filesizelabel.getText();
        			texto = texto+" ("+x+"x"+y+")";
        			filesizelabel.setText(texto);
        			dip = new IconJAI(thumb);

        				

        			
        			
        		} // Es un TIFF
        		else if (ext.equalsIgnoreCase("jpg") ||
        				ext.equalsIgnoreCase("jpeg") ||
        				ext.equalsIgnoreCase("jpe") ||
        				ext.equalsIgnoreCase("gif"))
        		{
        			dip = null;
        			ImageIcon tmpIcon = new ImageIcon(f.getPath());
        			height = tmpIcon.getIconHeight();
        			width = tmpIcon.getIconWidth();
        			String texto = filesizelabel.getText();
        			texto = texto+" ("+width+"x"+height+")";
        			filesizelabel.setText(texto);
        			if(tmpIcon.getIconWidth() > 90) 
        			{
        				thumbnail = new ImageIcon(
        						tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
        			} 
        			else 
        			{
        				thumbnail = tmpIcon;
        			}
        		} // Es otra extensión
    			else 
    			{
    	    		thumbnail = null;
    	    		dip = null;
    			} // No es una extensión reconocida (no debería llegar porque lo filtramos)
        	}
        	else
        	{
        		thumbnail = null;
	    		dip = null;
        	}
    	}
	}
    
    
    /**
     * Vuelve a pintar la imagen cuando cambia el fichero marcado en el JFileChooser
     * @param e Evento recibido
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
	String prop = e.getPropertyName();
	if(prop.compareTo(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)==0) {
	    if(isShowing()) {
                loadImage((File) e.getNewValue());
		repaint();
	    }
	}
    }
    
    /**
     * Pinta sobre el componente el IconJAI o el ImageIcon, dependiendo del 
     * tipo de la imagen
     * @param g El adaptador de gráficos del componente
     */
    @Override
    public void paint(Graphics g) {
	super.paint(g);
	filesizelabel.setVisible(true);
	//iconlabel.setVisible(true);
	if(thumbnail != null) {
		

	    iconlabel.setIcon(thumbnail);
	    //thumbnail.paintIcon(this, g, x, y);
	}
	else if (dip != null)
	{

		iconlabel.setIcon(dip);
	
	}
    }

    int getImageHeight() {
        return y;
    }

    int getImageWidth() {
        return x;
    }
}