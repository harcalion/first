/* * @(#)IconJAI.java 5.2 99/06/08 * * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved. * * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use, * modify and redistribute this software in source and binary code form, * provided that i) this copyright notice and license appear on all copies of * the software; and ii) Licensee does not utilize the software in a manner * which is disparaging to Sun. * * This software is provided "AS IS," without a warranty of any kind. ALL * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE * POSSIBILITY OF SUCH DAMAGES. * * This software is not designed or intended for use in on-line control of * aircraft, air traffic, aircraft navigation or aircraft communications; or in * the design, construction, operation or maintenance of any nuclear * facility. Licensee represents and warrants that it will not use or * redistribute the Software for such purposes. */
package es.upm.fi.gtd.first;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.swing.Icon;
/**
 * Clase que implementa un Icon de AWT en el que se pueden mostrar imágenes de JAI
 * como PlanarImage y TiledImage
 * @author Alvar
 */
public class IconJAI implements Icon {
    /** The source RenderedImage. */
    protected RenderedImage im;
    /** The image's SampleModel. */
    protected SampleModel sampleModel;
    /** The image's ColorModel or one we supply. */
    protected ColorModel colorModel;
    /**
     * Tamaño mínimo horizontal del componente
     */
    protected int minX;
    /**
     * Tamaño mínimo vertical del componente
     */
    protected int minY;
    /**
     * Anchura
     */
    protected int width;
    /**
     * Altura
     */
    protected int height;
    /**
     * Dimensiones de la imagen
     */
    protected Rectangle imageBounds;
    /** The image's min X tile. */
    protected int minTileX;
    /** The image's max X tile. */
    protected int maxTileX;
    /** The image's min Y tile. */
    protected int minTileY;
    /** The image's max Y tile. */
    protected int maxTileY;
    /** The image's tile width. */
    protected int tileWidth;
    /** The image's tile height. */
    protected int tileHeight;
    /** The image's tile grid X offset. */
    protected int tileGridXOffset;
    /** The image's tile grid Y offset. */
    protected int tileGridYOffset;
    /** The pixel to display in the upper left corner or the canvas. */
    protected int originX;
    /** The pixel to display in the upper left corner or the canvas. */
    protected int originY;
    
    private Color backgroundColor = Color.black;
    static final boolean debug=false;
    /**
     * Constructor
     * @param im Imagen de entrada
     */
    public IconJAI(RenderedImage im) {
        int nbands = im.getSampleModel().getNumBands();
        if (nbands!=1) {
            ParameterBlockJAI selectPb = new ParameterBlockJAI( "BandSelect" );
            selectPb.addSource( im );
            selectPb.setParameter( "bandIndices", new int[] { 0 } );
            
            im = JAI.create( "BandSelect", selectPb );
        }
        int mx = im.getMinX();
        int my = im.getMinY();
        if ((mx < 0) || (my < 0)) {
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(im);
            pb.add((float)Math.max(-mx, 0));
            pb.add((float)Math.max(-my, 0));
            pb.add(new InterpolationNearest());
            im = JAI.create("translate", pb, null);
        }
        this.im = im;
        this.sampleModel = im.getSampleModel();
        this.colorModel = im.getColorModel();
        if (this.colorModel == null) {
            this.colorModel = PlanarImage.createColorModel(im.getSampleModel());
        }
        if (this.colorModel == null) {
            throw new IllegalArgumentException( "IconJAI is unable to display supplied RenderedImage.");
        }
 /*if (this.colorModel.getTransparency() != Transparency.OPAQUE) {
 Object col = im.getProperty("background_color");
 if (col != null) {
 backgroundColor = (Color)col;
 }
 else {
 backgroundColor = new Color(0, 0, 0);
 }
 } */
        minX = im.getMinX();
        minY = im.getMinY();
        width = im.getWidth();
        height = im.getHeight();
        imageBounds = new Rectangle(minX, minY, width, height);
        minTileX = im.getMinTileX();
        maxTileX = im.getMinTileX() + im.getNumXTiles() - 1;
        minTileY = im.getMinTileY();
        maxTileY = im.getMinTileY() + im.getNumYTiles() - 1;
        tileWidth = im.getTileWidth();
        tileHeight = im.getTileHeight();
        tileGridXOffset = im.getTileGridXOffset();
        tileGridYOffset = im.getTileGridYOffset();
        originX = originY = 0;
    }
    /**
     * Devuelve la anchura del icono
     * @return La anchura del icono
     */
    @Override
    public int getIconWidth() {
        return im.getWidth();
    }
    
    /**
     * Devuelve la altura del icono
     * @return La altura del icono
     */
    @Override
    public int getIconHeight() {
        return im.getHeight();
    }
    /**
     * Devuelve la distancia entre un punto horizontal y su tesela correspondiente
     * @param x El punto horizontal
     * @return Valor de distancia
     */
    private int XtoTileX(int x) {
        return (int) Math.floor((double) (x - tileGridXOffset)/tileWidth);
    }
    /**
     * Devuelve la distancia entre un punto vertical y su tesela correspondiente
     * @param y El punto horizontal
     * @return Valor de distancia
     */
    private int YtoTileY(int y) {
        return (int) Math.floor((double) (y - tileGridYOffset)/tileHeight);
    }
    /**
     * Devuelve la distancia entre dos teselas horizontales
     * @param tx Número de teselas
     * @return Valor de distancia
     */
    private int TileXtoX(int tx) {
        return tx*tileWidth + tileGridXOffset;
    }
    /**
     * Devuelve la distancia entre dos teselas verticales
     * @param ty Número de teselas
     * @return Valor de distancia
     */
    private int TileYtoY(int ty) {
        return ty*tileHeight + tileGridYOffset;
    }
    /**
     * Dibuja el icono sobre el componente c
     * @param c Componente sobre el que se va a dibujar
     * @param g Adaptador de gráficos para el componente
     * @param x Anchura de la zona a pintar
     * @param y Altura de la zona a pintar
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if( debug ) System.out.println( "In " + getClass() + " method paintIcon(Component c, Graphics g, int x, int y)" );
        if( debug ) {
            System.out.println( "translation x: " + x ); System.out.println( "translation y: " + y );
        }
        Graphics2D g2D = null;
        if (g instanceof Graphics2D) {
            g2D = (Graphics2D)g;
        } else {
            System.err.println("IconJAI: not a Graphics2D.");
            return;
        }
// Get the clipping rectangle and translate it into image coordinates.
        Rectangle clipBounds = g.getClipBounds();
        int transX = x;
        int transY = y;
// Determine the extent of the clipping region in tile coordinates.
        int txmin, txmax, tymin, tymax;
        int ti, tj;
// Constrain drawing to the active image area
        Rectangle imageRect = new Rectangle(minX + transX, minY + transY, width, height);
        if (clipBounds != null) {
            txmin = XtoTileX(clipBounds.x);
            txmin = Math.max(txmin, minTileX);
            txmin = Math.min(txmin, maxTileX);
            txmax = XtoTileX(clipBounds.x + clipBounds.width - 1);
            txmax = Math.max(txmax, minTileX);
            txmax = Math.min(txmax, maxTileX);
            tymin = YtoTileY(clipBounds.y);
            tymin = Math.max(tymin, minTileY);
            tymin = Math.min(tymin, maxTileY);
            tymax = YtoTileY(clipBounds.y + clipBounds.height - 1);
            tymax = Math.max(tymax, minTileY);
            tymax = Math.min(tymax, maxTileY);
            g2D.clip(imageRect);
        } else {
            txmin = minTileX;
            txmax = maxTileX;
            tymin = minTileY;
            tymax = maxTileY;
            g2D.setClip(imageRect);
        }
        if (backgroundColor != null) {
            g2D.setColor(backgroundColor);
        }
// Loop over tiles within the clipping region
        for (tj = tymin; tj <= tymax; tj++) {
            for (ti = txmin; ti <= txmax; ti++) {
                int tx = TileXtoX(ti);
                int ty = TileYtoY(tj);
                if( debug ) {
                    System.out.println( "Rendering tile i,j: (" + ti + ", " + tj + ") at x,y: (" + tx + ", " + ty + ")" );
                }
                
                Raster tile = im.getTile(ti, tj);
                DataBuffer dataBuffer = tile.getDataBuffer();
                Point origin = new Point(0, 0);
                WritableRaster wr = Raster.createWritableRaster(sampleModel, dataBuffer, origin);
                BufferedImage bi = new BufferedImage(colorModel, wr, false, null);
                AffineTransform transform = AffineTransform.getTranslateInstance(tx + transX, ty + transY);
                if (backgroundColor != null) {
                    g2D.fillRect(tx + transX, ty + transY, tileWidth, tileHeight);
                }
                g2D.drawImage(bi, transform, null);
            }
        }
        if( debug ) System.out.println( "Out " + getClass() + " method paintIcon(Component c, Graphics g, int x, int y)" );
    }
}