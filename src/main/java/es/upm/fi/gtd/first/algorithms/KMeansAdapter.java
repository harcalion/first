/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first.algorithms;

import es.upm.fi.gtd.first.AreaTrabajo;
import es.upm.fi.gtd.first.Atrous;
import es.upm.fi.gtd.first.GeoImg;
import es.upm.fi.gtd.first.ParseXML;
import es.upm.fi.gtd.first.data.ImageRegion;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.util.List;
import java.util.Locale;
import javax.media.jai.ROIShape;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 *
 * @author Alvar
 */
public class KMeansAdapter {

      private KMeansClusterDescriptorComponent[][] components;
  // The image regions' dimensions will be constant.
  private int regionWidth = 10;
  private int regionHeight = 10;
  // The number of regions.
  private int numRegionsX,numRegionsY;
  AreaTrabajo dpanel;
  
    public KMeansAdapter(List<GeoImg> images_pan, AreaTrabajo dpanel, JFrame t, Locale loc, Atrous principal) {
        ParseXML ic = new ParseXML(loc.getLanguage()+"_"+loc.getCountry()+".xml");
        this.dpanel = dpanel;
        numRegionsX = images_pan.get(0).getImage().getWidth()/regionWidth;    
    numRegionsY = images_pan.get(0).getImage().getHeight()/regionHeight;    
    // Do the cluster analysis of the regions on the image. The regions are non-overlapping
    // polygons. Create also the components that will be displayed as the result.
    components = new KMeansClusterDescriptorComponent[numRegionsX][numRegionsY];
    for(int y=0;y<numRegionsY;y++)
      for(int x=0;x<numRegionsX;x++)
        {
        // Create the bounding polygon (square).
        int[] xPoints = {x*regionWidth,x*regionWidth,(x+1)*regionWidth,(x+1)*regionWidth};
        int[] yPoints = {y*regionHeight,(y+1)*regionHeight,(y+1)*regionHeight,y*regionHeight};
        Polygon p = new Polygon(xPoints,yPoints,4); 
        // Create the image region and cluster it.
        ImageRegion r = new ImageRegion(images_pan.get(0).getImage(),new ROIShape(p));
        KMeansClusterDescriptor[] ds = KMeansRegionClusterer.clusterImageRegion(r,5,100,0.001);
        // Get the most populated cluster.        
        KMeansClusterDescriptor winner = KMeansRegionClusterer.getLargestCluster(ds);
        components[x][y] = new KMeansClusterDescriptorComponent(25,25,winner);
        }
        // Create a grid layout for these components.
    JInternalFrame jif = 
            new JInternalFrame(
                "Clasificación de "
                +images_pan.get(0).getFile().getName(), 
                true, 
                true, 
                true, 
                true); // Tag 165
    jif.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    jif.getContentPane().setLayout(new GridLayout(numRegionsY,numRegionsX));
    for(int y=0;y<numRegionsY;y++)
      for(int x=0;x<numRegionsX;x++)
        {
        jif.getContentPane().add(components[x][y]);
        }    
    // Set the closing operation so the application is finished.
    jif.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jif.pack(); // adjust the frame size using preferred dimensions.
    jif.setVisible(true); // show the frame.
    if (dpanel == null)
    {
        dpanel = new AreaTrabajo(principal);
    }
    dpanel.add(jif);
    dpanel.setVisible(true);
    t.add(dpanel,BorderLayout.CENTER);
		
		t.setExtendedState(t.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		t.setVisible(true);
		//Dimension paneRes = dpanel.getSize();
		//pi.setBounds(paneRes.width-200, paneRes.height-100, 200, 100);
		
		
		

    }

    public AreaTrabajo getDpanel() {
        return dpanel;
    }


}
