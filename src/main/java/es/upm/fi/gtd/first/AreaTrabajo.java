package es.upm.fi.gtd.first;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import es.upm.fi.gtd.first.ListaImagenes.PanelImagenes;

/*
 * Extiende JDesktopPane para añadir la funcionalidad de una lista de imágenes 
 * abiertas flotante
 */
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JViewport;
import javax.swing.event.ChangeListener;


/**
 * AreaTrabajo extiende la clase básica de Swing JDesktopPane añadiendo elementos MDI como ordenación en mosaico, en cascada y una lista acoplable de imágenes abiertas
 * @author Alvar
 */
public class AreaTrabajo extends JDesktopPane implements ActionListener, InternalFrameListener, ComponentListener, ChangeListener{
//public class AreaTrabajo extends JDesktopPane implements ActionListener, InternalFrameListener, ComponentListener, ChangeListener{
    private static int FRAME_OFFSET=20;
	/**
	 * Lista de imágenes flotante
	 */
	ListaImagenes lista;
        /**
         * Lista de imágenes fija
         */
	PanelImagenes pi;
        /**
         * Versión de serialización
         */
	private static final long serialVersionUID = 1L;
        
        Image img;
        
        MDIDesktopManager manager;
        
        Atrous principal;
        
        ParseXML ic;
        /**
         * Constructor que llama al constructor de JDesktopPane
         * @param principal GUI y estado de la aplicación
         */
	public AreaTrabajo(Atrous principal)
	{
		super();
                //this.putClientProperty("JDesktopPane.dragMode", "faster");
                //this.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
                this.ic = principal.getIC();
                this.principal = principal;
                principal.getT().addComponentListener(this);
        try {
            img = javax.imageio.ImageIO.read(new File("resources/logo3.png"));
        } catch (IOException ex) {
            Logger.getLogger(AreaTrabajo.class.getName()).log(Level.SEVERE, null, ex);
        }
               // DefaultDesktopManager man = new DefaultDesktopManager();
                //setDesktopManager(man);
                manager=new MDIDesktopManager(this);
            setDesktopManager(manager);
            setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
	}
        

        
    /**
     * Añade una nueva ventana interior al Área de Trabajo
     * @param frame La nueva ventana interior
     * @return El componente que devuelve la clase padre al añadir la ventana
     */
    public Component add(JComponent frame)
        {
            if (frame instanceof JInternalFrame)
            {
                JInternalFrame f = (JInternalFrame) frame;
                 System.out.println("Entro y añado imagen");
        JInternalFrame[] array = getAllFrames();
        Point p;
        int w;
        int h;

        Component retval=super.add(f);
        checkDesktopSize();
        if (array.length > 0) {
            p = array[0].getLocation();
            p.x = p.x + FRAME_OFFSET;
            p.y = p.y + FRAME_OFFSET;
        }
        else {
            p = new Point(0, 0);
        }
        f.setLocation(p.x, p.y);
        if (f.isResizable()) {
            w = getWidth() - (getWidth()/3);
            h = getHeight() - (getHeight()/3);
            if (w < f.getMinimumSize().getWidth()) w = (int)f.getMinimumSize().getWidth();
            if (h < f.getMinimumSize().getHeight()) h = (int)f.getMinimumSize().getHeight();
            f.setSize(w, h);
        }
        moveToFront(f);
        f.setVisible(true);
        try {
            f.setSelected(true);
        } catch (PropertyVetoException e) {
            f.toBack();
        }
        return retval;
            }
            else
            {
                Component retval = super.add(frame);
                return retval;
            }
        }
  
    /**
     *  Crea la lista (gráfica) de imágenes a partir de una lista de GeoImg
     * @param imgs La lista de imágenes a introducir
     */
	public void addLista (List<GeoImg> imgs)
	{
		lista = new ListaImagenes(imgs, this, ic);
		pi = lista.getPanelImagenes();
                this.add(pi);
                         JViewport jv = null;
                if (this.getParent() instanceof JViewport) {
                jv = (JViewport)this.getParent();
            
                }
        Rectangle viewRect = jv.getViewRect();
        Dimension paneRes = jv.getSize();
        pi.setBounds(paneRes.width-200+viewRect.x, paneRes.height-100+viewRect.y, 200, 100);
		pi.setVisible(true);
		pi.refresh();
                this.repaint();
     
		
	}
        
            /**
     * Cascade all internal frames
     */
    public void cascadeFrames() {
        int x = 0;
        int y = 0;
        JInternalFrame allFrames[] = getAllFrames();

        manager.setNormalSize();
        int frameHeight = (getBounds().height - 5) - allFrames.length * FRAME_OFFSET;
        int frameWidth = (getBounds().width - 5) - allFrames.length * FRAME_OFFSET;
        for (int i = allFrames.length - 1; i >= 0; i--) {
            allFrames[i].setSize(frameWidth,frameHeight);
            allFrames[i].setLocation(x,y);
            x = x + FRAME_OFFSET;
            y = y + FRAME_OFFSET;
        }
    }
        
    /**
     * Obtiene el panel de imágenes donde se muestran las imágenes abiertas
     * @return El panel de imágenes si es distinto de null o null en otro caso
     */
    public PanelImagenes getPanelImagenes()
        {
            if (hayPanel())
            {
                return pi;
            }
            else
                return null;
        }
        
	/**
	 * Devuelve la lista de imágenes abiertas (independientemente
	 * de cómo se esté mostrando)
	 * @return
	 */
	public JList getLista()
	{
		JList res = null;
		if (lista.isShowing())
		{
			res = lista.getList();
		}
		else if (pi.isShowing())
		{
			res = pi.getList();
		}
		return res;
	}
        
        
    /**
     * Devuelve la lista de imágenes (cuando está desacoplada)
     * @return La lista de imágenes
     */
    public ListaImagenes getListaImagenes()
        {
            if (hayLista())
            {
                return lista;
            }
            else
            {
                return null;
            }
            
        }
        
    /**
     * Controla la pulsación del ratón en el botón para hacer la lista flotante
     * @param e El evento recibido
     */
    @Override
	public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().compareTo("\u2191")==0)
            {
		pi.setVisible(false);
		this.add(lista, JDesktopPane.PALETTE_LAYER);
		lista.setVisible(true);
		JScrollPane jsp =lista.getScrollPane();
		lista.getContentPane().add(jsp);
                JViewport jv = null;
                if (this.getParent() instanceof JViewport) {
                jv = (JViewport)this.getParent();
            
                }
        
        Dimension paneRes = jv.getSize();
	Rectangle viewRect = jv.getViewRect();	
		lista.setBounds(paneRes.width-200+viewRect.x, paneRes.height-100+viewRect.y, 200, 100);
            }	
		
		
	}
    @Override
	public void internalFrameActivated(InternalFrameEvent e) {
		
            JInternalFrame objeto = e.getInternalFrame();
            objeto.requestFocusInWindow();
            String nombre = objeto.getTitle();
            if (lista.isShowing())
            {
                lista.setMarcada(nombre);
            }
            else if (pi.isShowing())
            {
                pi.setMarcada(nombre);
            }
            
            
            

	}
    @Override
	public void internalFrameClosed(InternalFrameEvent e) {
		// Borrar la imagen de la lista de imágenes en memoria
 
            
        
		
	}
    @Override
	public void internalFrameClosing(InternalFrameEvent e) {
		
		           JInternalFrame jif = e.getInternalFrame();


                    JScrollPane jsptemp = (JScrollPane) jif.getContentPane().getComponent(0);

                
                JViewport jvtemp = jsptemp.getViewport();
                // DisplayJAIWithPixelInfoInt djai =
                // (DisplayJAIWithPixelInfoInt) jvtemp.getComponent(0);
                DisplayJAIWithPixelInfo resultado = (DisplayJAIWithPixelInfo) jvtemp.getComponent(0);
                
                File f = resultado.archivo;
                lista.quitarCerrada(f.getName());
                List<GeoImg> imgs = lista.getImagenes();
                for (GeoImg g : imgs )
                {
                    if (g.getFile().equals(f))
                    {
                        imgs.remove(g);
                    }
                }
                
                principal.setImgs(imgs);
                
	}
    @Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		
		
	}
    @Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		
		
	}
    /**
     * Evita que se aplique el método que minimiza el JInternalFrame dentro
     * de JDesktopPane y lo que hace es fijar la lista al escritorio
     * @param e Evento recibido
     */
    @Override
	public void internalFrameIconified(InternalFrameEvent e) {
		lista.setVisible(false);
		try {
			lista.setIcon(false);
		} catch (PropertyVetoException e1) {
			
			e1.printStackTrace();
		}
                JViewport jv = null;
                if (this.getParent() instanceof JViewport) {
                jv = (JViewport)this.getParent();
            
                }
        Rectangle viewRect = jv.getViewRect();
        Dimension paneRes = jv.getSize();
        pi.setBounds(paneRes.width-200+viewRect.x, paneRes.height-100+viewRect.y, 200, 100);
		pi.setVisible(true);
		pi.refresh();
		
	}
    @Override
	public void internalFrameOpened(InternalFrameEvent e) {
		
		
	}
        
    @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(img != null) g.drawImage(img, (this.getWidth()/2)-180,(this.getHeight()/2)-25,364,131,this);
            else g.drawString("Image not found", (this.getWidth()/2)-180,(this.getHeight()/2)-25);
        }
        
            /**
     * Tile all internal frames
     */
    public void tileFrames() {
        java.awt.Component allFrames[] = getAllFrames();
//        manager.setNormalSize();
        int frameHeight,frameWidth = 0;
        int y = 0;
        int x = 0;
        for (int i = 0; i < allFrames.length; i++) {
            frameHeight = allFrames[i].getBounds().height;
            frameWidth = allFrames[i].getBounds().width;
            allFrames[i].setSize(frameWidth,frameHeight);
            allFrames[i].setLocation(x,y);
            if (y+frameHeight < this.getHeight())
            {
                if (x+frameWidth > this.getWidth())
                {
                    x=0;
                }
                y = y + frameHeight;
            }
            else
            {
                x = x+allFrames[i].getBounds().width;
                y = 0;
                
                
            }
        }
    }

    /**
     * Devuelve si la lista de imágenes está inicializada
     * @return true si lista es distinto de null
     */
    public boolean hayLista() {

        if (lista == null)
        {
            return false;            
        }
        else
        {
            return true;
        }
        
    }
    
    /**
     * Comprueba si se está mostrando el panel de imágenes (acoplado)
     * @return true si se está mostrando, false en caso contrario
     */
    public boolean hayPanel() {

        if (pi == null)
        {
            return false;            
        }
        else
        {
            return true;
        }
        
    }

    void addToLista(String string) {
        GeoImg image = new GeoImg (null, null, new File(string), null, null);
        JList list = lista.getList();
        DefaultListModel model = (DefaultListModel) list.getModel();
        
        model.addElement(image);
        list.setModel(model);
        
        
        
        
    }

    
    @Override
    public void componentResized(ComponentEvent e) {        
        JFrame t = (JFrame)e.getSource();
        JViewport jv = null;
        // Vuelvo a pintar el DesktopPane y la lista
        if (this.getParent() instanceof JViewport) {
            jv = (JViewport)this.getParent();
            
        }
        Dimension paneRes = null;
        if (jv == null)
        {
            if (this.getParent() instanceof JScrollPane)
            {
                JScrollPane jsp = (JScrollPane)this.getParent();
                paneRes = jsp.getSize();
            }
        }
        else
        {
            paneRes = jv.getSize();            
        }
        //this.setBounds(0,0, paneRes.width, paneRes.height);
        if (pi!=null)
        {
		pi.setBounds(paneRes.width-200, paneRes.height-100, 200, 100);
                pi.repaint();
        }
               t.repaint();
    }

    
    @Override
    public void componentMoved(ComponentEvent e) {
        JFrame t = (JFrame)e.getSource();
     t.repaint();  
    }

    @Override
    public void componentShown(ComponentEvent e) {
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        
    }
    /**
     * Redimensiona todas las ventanas interiores a un mismo tamaño
     * @param width Anchura deseada
     * @param height Altura deseada
     */
    public void setAllSize(int width, int height){
        setAllSize(new Dimension(width,height));
    }
    /**
     * Redimensiona todas las ventanas interiores a un mismo tamaño
     * @param d Dimensión deseada
     */
    public void setAllSize(Dimension d){
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
    }
    @Override
            public void remove(Component c) {
        super.remove(c);
        checkDesktopSize();
    }
    
    @Override
        public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x,y,w,h);
        checkDesktopSize();
    }
            private void checkDesktopSize() {
        if (getParent()!=null&&isVisible()) manager.resizeDesktop();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        
        if (pi != null)
        {
            
        JViewport jv = (JViewport) e.getSource();
        
        Dimension paneRes = jv.getSize();
        
        Rectangle viewRect = jv.getViewRect();
        
        
		pi.setBounds(paneRes.width-200+viewRect.x, paneRes.height-100+viewRect.y, 200, 100);
                jv.invalidate();
                jv.validate();
                
        }
    }

}
class MDIDesktopManager extends DefaultDesktopManager
{
    private static final long serialVersionUID = 1L;
    private AreaTrabajo desktop;
    
    public MDIDesktopManager(AreaTrabajo desktop) {
        this.desktop = desktop;
    }
    
    @Override
    public void endResizingFrame(JComponent f) {
        super.endResizingFrame(f);
        resizeDesktop();
    }
    
    @Override
        public void endDraggingFrame(JComponent f) {
        super.endDraggingFrame(f);
        resizeDesktop();
    }
    
   private JScrollPane getScrollPane() {
        if (desktop.getParent() instanceof JViewport) {
            JViewport viewPort = (JViewport)desktop.getParent();
            if (viewPort.getParent() instanceof JScrollPane)
                return (JScrollPane)viewPort.getParent();
        }
        return null;
    }
   
   private Insets getScrollPaneInsets() {
        JScrollPane scrollPane=getScrollPane();
        if (scrollPane==null) return new Insets(0,0,0,0);
        else return getScrollPane().getBorder().getBorderInsets(scrollPane);
    }
    
    protected void resizeDesktop() {
        int x = 0;
        int y = 0;
        JScrollPane scrollPane = getScrollPane();
        Insets scrollInsets = getScrollPaneInsets();

        if (scrollPane != null) {
            JInternalFrame allFrames[] = desktop.getAllFrames();
            for (int i = 0; i < allFrames.length; i++) {
                if (allFrames[i].getX()+allFrames[i].getWidth()>x) {
                    x = allFrames[i].getX() + allFrames[i].getWidth();
                }
                if (allFrames[i].getY()+allFrames[i].getHeight()>y) {
                    y = allFrames[i].getY() + allFrames[i].getHeight();
                }
            }
            Dimension d=scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
               d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right,
                         d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }

            if (x <= d.getWidth()) x = ((int)d.getWidth()) - 20;
            if (y <= d.getHeight()) y = ((int)d.getHeight()) - 20;
            desktop.setAllSize(x,y);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }
        public void setNormalSize() {
        JScrollPane scrollPane=getScrollPane();
        int x = 0;
        int y = 0;
        Insets scrollInsets = getScrollPaneInsets();

        if (scrollPane != null) {
            Dimension d = scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
               d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right,
                         d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }

            d.setSize(d.getWidth() - 20, d.getHeight() - 20);
            desktop.setAllSize(x,y);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }
    
}
