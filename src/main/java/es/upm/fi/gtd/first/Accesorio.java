package es.upm.fi.gtd.first;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Implementa el accesorio personalizado que se le agrega al JFileChooser global
 * de la aplicación
 */

public class Accesorio extends JComponent
	{
		/**
		 *  Versión de la clase para serializar
		 */
		private static final long serialVersionUID = 1L;
                /**
                 *  Componente que permite elegir una profundidad de bits
                 */
		private ElegirProfundidad ep;
                /**
                 *  Componente que muestra una imagen en miniatura
                 */
		private FilePreviewer fp;
                /**
                 *  Componente que permite cargar las imágenes multibanda de varias maneras
                 */
		private JCheckBox jcb;
          
    /**
     * Constructor de la clase
     * @param fc El JFileChooser al que se va a añadir el Accesorio
     * @param t El JFrame principal de la aplicación
     * @param ic 
     */
		public Accesorio(JFileChooser fc, JFrame t, ParseXML ic )
		{
                    
			// Es un BoxLayout en el que se crea el previsualizador y el grupo de checkboxes
			this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
			ep = new ElegirProfundidad(t, ic);
			fp = new FilePreviewer(fc);
			
			jcb = new JCheckBox(ic.buscar(65)); // Tag 65
			this.add(fp);
			this.add(Box.createRigidArea(new Dimension(0,1)));
			this.add(ep);			
			this.add(jcb);
			jcb.setVisible(false);

			// Leer multibanda como bandas separadas?
			
			
		}
    /**
     *  Muestra el componente de carga de multibanda si es un diálogo de apertura
     */
		public void esDialogoAbrir ()
		{
			jcb.setVisible(true);
		}
    /**
     *  Devuelve el valor del marcador de carga de bandas múltiples separadas
     * @return true si el marcador está seleccionado
     */
		public boolean getSeparadas ()
		{
			return jcb.isSelected();
		}
    /**
     *  Muestra o no el componente que permite seleccionar la profundidad
     * @param caso true si el JFileChooser es SAVE_DIALOG
     */
		public void setProfundidad(boolean caso)
		{
			if (caso == true)
			{
				ep.setVisible(true);
			}
			else
			{
				ep.setVisible(false);
			}
		}
    /**
     * Devuelve el componente de selección de profundidad
     * @return El componente de selección de profundidad
     */
		public ElegirProfundidad getEP (){
			return ep;
		}
	}