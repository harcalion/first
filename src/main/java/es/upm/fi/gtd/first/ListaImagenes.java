package es.upm.fi.gtd.first;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.text.Position.Bias;

/**
 * Clase que implementa la lista de imágenes abiertas
 * @author Alvar
 */
public class ListaImagenes  extends JInternalFrame{
    /**
     * Versión para serialización
     */
    private static final long serialVersionUID = 1L;
    /**
     * JList con las imágenes 
     */
    private JList list;
    /**
     * JScrollPane donde se coloca la lista anterior
     */
    private JScrollPane scrollPane;
    /**
     * Versión no flotante de la lista de imágenes
     */
    private PanelImagenes pi;

    /**
     * Constructor
     * @param datos Lista de imágenes
     * @param dpanel AreaTrabajo donde se colocará la lista
     * @param ic Objeto de datos de traducción
     */
public ListaImagenes(List<GeoImg> datos, AreaTrabajo dpanel, ParseXML ic)
{
    
    list = null;
    DefaultListModel lm = new DefaultListModel();
    	if (datos == null)
	{ // No son GeoImg
            JInternalFrame [] frames = dpanel.getAllFrames();
            
            
            for (int i = 0; i < frames.length; i ++)
            {
                
                lm.addElement(new GeoImg(null, null, new File(frames[i].getTitle()), null, null));
            }
		
		 list = new JList(lm);
                 
	}		
	else
	{
           
                for (Iterator it = datos.listIterator(); it.hasNext();)
                {
                    lm.addElement(it.next());
                }
                
		list = new JList(lm);
	}
    MyCellRenderer mcr = new MyCellRenderer();
    list.setCellRenderer(mcr);
    this.addInternalFrameListener(dpanel);
	
	
	
	this.setClosable(false);
	this.setResizable(false);
	this.setMaximizable(false);
	this.setIconifiable(true);
	
	this.setTitle(ic.buscar(161)); // Tag 161
	this.setVisible(true);


	scrollPane = new JScrollPane(list);
	pi = new PanelImagenes(scrollPane, list, dpanel, ic);
	this.getContentPane().add(scrollPane);
	//this.setBounds(0, 0, 200, 100);
}

    /**
     * Devuelve la versión no flotante de la lista
     * @return PanelImagenes no flotante
     */
public PanelImagenes getPanelImagenes()
{
	return pi;
}
    /**
     * Devuelve la lista de imágenes
     * @return JList con la lista de imágenes
     */
public JList getList()
{
	return list;
}
/**
 * Obtiene las imágenes seleccionadas en la lista de imágenes
 * @return Lista con las imágenes seleccionadas
 */
public List<GeoImg> getImagenesMarcadas()
{
    int [] indices = list.getSelectedIndices();
    List<GeoImg> resultado = new ArrayList<GeoImg>();
    for (int i: indices)
    {
        resultado.add((GeoImg)list.getModel().getElementAt(i)); 
    }
    return resultado;
}

    /**
     * Devuelve el panel de scroll donde está colocada la lista de imágenes
     * @return JScrollPane donde se coloca
     */
public JScrollPane getScrollPane()
{
	return scrollPane;
}

/**
 * Obtiene todas las imágenes que contiene la lista
 * @return Lista con todas las imágenes
 */
public List<GeoImg> getImagenes() {
        List<GeoImg> resultado = new ArrayList<GeoImg>();
        for (int i = 0; i < list.getModel().getSize(); i++)
        {
            resultado.add((GeoImg) list.getModel().getElementAt(i));    
        }
        return resultado;
    }
    
    void quitarCerrada (String nombre)
    {
        int i = list.getNextMatch(nombre, 0, Bias.Forward);
        DefaultListModel lm = (DefaultListModel) list.getModel();
        lm.remove(i);
        list.validate();
    }

    void setMarcada(String nombre) {
        ListModel lm = list.getModel();        
        for (int i = 0; i < lm.getSize(); i++) {

                GeoImg img = (GeoImg) lm.getElementAt(i);
                if (img.getFile().getName().equalsIgnoreCase(nombre)) {
                    list.setSelectedIndex(i);
                }
            
        }
        
        //list.repaint();

    }
    /**
     * Clase que implementa la lista de imágenes como un panel de imágenes
     * acoplado
     */
    public class PanelImagenes extends JPanel
{
	/**
	 * Versión para serialización
	 */
	private static final long serialVersionUID = 1L;
        /**
         * JScrollPane que contiene a la lista
         */
	private JScrollPane pane;
        /**
         * JList con los datos
         */
        private JList list;
        /**
         * Botón para hacer la lista flotante
         */
	private JButton but;
        /**
         * Título de la ventana
         */
	private JLabel titulo;
        /**
         * Colocación de componentes
         */
	javax.swing.GroupLayout layout;

        /**
         * Constructor
         * @param pane Componente con la lista 
         * @param dpanel AreaTrabajo donde se dibujará el componente
         */
	PanelImagenes (JScrollPane panel, JList li, AreaTrabajo dpanel, ParseXML ic)
	{

		titulo = new JLabel (ic.buscar(161)); // Tag 161
		but = new JButton("\u2191");
		but.addActionListener(dpanel);
                list = li;
		pane = panel;
		layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(but))
            .addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(but))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
            );

	}
        
    /**
     * Obtiene el título de la ventana
     * @return Título de la ventana como JLabel
     */
    public JLabel getTitulo()
        {
            return titulo;
        }
    /**
     * Obtiene la lista de imágenes
     * @return Lista de imágenes como JList
     */
    public JList getList()
	{
		return list;
	}

        /**
         * Vuelve a pintar el componente
         */
	public void refresh() {
		
		this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(but))
            .addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(but))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
            );
	}

        void setMarcada(String nombre) {
        ListModel lm = list.getModel();            
            for (int i = 0; i < lm.getSize(); i++) {
           
                GeoImg img = (GeoImg) lm.getElementAt(i);
                if (img.getFile().getName().equalsIgnoreCase(nombre)) {
                    list.setSelectedIndex(i);
                }
                
                
            
            }
            list.repaint();

        }
}
class MyCellRenderer extends JLabel implements ListCellRenderer {
   

    // This is the only method defined by ListCellRenderer.
    // We just reconfigure the JLabel each time we're called.

    /**
	 * Versión para serialización
	 */
	private static final long serialVersionUID = 1L;

        public MyCellRenderer() {
         setOpaque(true);
     }
        /**
         * Crea un renderizador de celdas
         * @param list Lista de entrada
         * @param value Valor de una celda en concreto
         * @param index Índice de la celda
         * @param isSelected Si está seleccionada la celda
         * @param cellHasFocus Si tiene foco
         * @return Renderizador
         */
	public Component getListCellRendererComponent(
      JList list,              // the list
      Object value,            // value to display
      int index,               // cell index
      boolean isSelected,      // is the cell selected
      boolean cellHasFocus)    // does the cell have focus
    {
        String s = value.toString();
        setText(s);


        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        GeoImg img = null;
        if (value.getClass().getName().compareTo("es.upm.fi.gtd.first.GeoImg")==0) {
            img = (GeoImg) value;
            if (img.getImage() == null) { // Es un diagrama, pintar diferente
                setForeground(Color.RED);
            }
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}


	
}
