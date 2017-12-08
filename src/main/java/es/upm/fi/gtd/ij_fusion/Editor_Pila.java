package es.upm.fi.gtd.ij_fusion;




import ij.*;
import ij.process.*;
import ij.plugin.*;

// Formulario Swing
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// Layouts y eventos
//import java.awt.NullLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.TreeSelectionListener;



public class Editor_Pila implements PlugIn {
	
	// Variables globales
	JTree from_tree = null, to_tree = null;
	JFrame window;
	
    @Override
	public void run(String arg) {
		if (WindowManager.getImageCount() == 0) {
			IJ.showMessage("Se necesita por lo menos una imagen para utilizar el editor!");
			return;
		}
		window = new StackForm();
		
	}
	
	
	
	/**
	 * Formulario de creacion de pila de imagenes
	 * @author Francisco Javier Merino Guardiola
	 *
	 */
	private class StackForm extends JFrame {
            private static final long serialVersionUID = 1L;
		public StackForm() {
			this.setDefaultCloseOperation(StackForm.DISPOSE_ON_CLOSE);
			this.setBounds(100, 100, 570, 350);
			this.setTitle("Creacion de pila...");
			// Creamos el panel principal y lo asociamos a la ventana
			JPanel panelPrincipal = new JPanel(null);
			this.setContentPane(panelPrincipal);
			
			// Creamos el arbol de origenes y creamos un JTree
			DefaultMutableTreeNode rootf = new DefaultMutableTreeNode("ImageJ");
			createTreeNodes(rootf);
			JTree jtf = new JTree(rootf);
			jtf.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			//--->panelPrincipal.add(jtf);
                        JScrollPane scroll1 = new JScrollPane(jtf);
                        panelPrincipal.add(scroll1);
			from_tree = jtf;
			
			// Creamos los botones
			JButton b1 = new JButton(">");
			b1.addActionListener(new addAction());
			JButton b2 = new JButton("X");
			b2.addActionListener(new removeAction());
			JButton b3 = new JButton("Crear");
			b3.addActionListener(new createAction());
			panelPrincipal.add(b1); panelPrincipal.add(b2); panelPrincipal.add(b3);
			
			// Creamos el otro arbol
			DefaultMutableTreeNode roott = new DefaultMutableTreeNode("Nueva Imagen",true);
			DefaultTreeModel treeModelt = new DefaultTreeModel(roott);
			JTree jtt = new JTree(treeModelt);
			jtt.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			jtt.setEditable(true);

			jtt.addTreeSelectionListener(new nodeRenameListener());
			//--->panelPrincipal.add(jtt);
                        JScrollPane scroll2 = new JScrollPane(jtt);
                        panelPrincipal.add(scroll2);
			to_tree = jtt;
			
			
			// Bounds ;)
			//jtf.setBounds(10, 10, 200, 300);
                        scroll1.setBounds(10, 10, 200, 300);
			//jtt.setBounds(350, 10, 200, 300);
                        scroll2.setBounds(350, 10, 200, 300);
			b1.setBounds(240, 100, 75, 30);
			b2.setBounds(240, 150, 75, 30);
			b3.setBounds(240, 200, 75, 30);
                        this.setResizable(false);
			this.setVisible(true);
		}
		
	} // formulario
	
	
	
	
	/**
	 * Clase que define un nodo del arbol
	 * @author Francisco Javier Merino Guardiola
	 */
	private class TreeNode {
		public String title = null;
		public ImagePlus image = null;
		public ImageProcessor processor = null;
		
		/**
		 * Nodo raiz
		 * @param title
		 */
		public TreeNode(String title) {
			this.title = title;
		}
		
		/**
		 * Nodo contenedor de bandas
		 * @param imp Contenedor
		 */
		public TreeNode(ImagePlus imp) {
			this.image = imp;
			this.title = imp.getTitle();
		}
		
		/**
		 * Nodo hoja (un procesador)
		 * @param imp Contenedor
		 * @param n Numero de procesador
		 */
		public TreeNode(ImagePlus imp, int n) {
			this.processor = imp.getStack().getProcessor(n);			
			if(imp.getStack().getSliceLabel(n) == null)
				this.title = imp.getTitle();
			else
				this.title = imp.getStack().getSliceLabel(n);
		}
		
        @Override
		public String toString() {return title;	}

	}
	
	
	
	
	/**
	 * Crea todos los nodos del arbol de seleccion
	 * @param root Nodo raiz
	 */
	private void createTreeNodes(DefaultMutableTreeNode root) {
		// Obtengo el numero de imagenes actualmente abiertas
		int []im = WindowManager.getIDList();
		
		// Incluye cada imagen en el arbol
		for(int i=0;i<im.length;i++) {
			// Crea un nodo para el contenedor
			ImagePlus img = WindowManager.getImage(im[i]);
			DefaultMutableTreeNode imgnode = new DefaultMutableTreeNode(new TreeNode(img));
			// Incluye cada banda del contenedor
			for(int j=1;j<=img.getStackSize();j++)
				imgnode.add(new DefaultMutableTreeNode(new TreeNode(img,j)));
			// Agrega el nodo contenedor al JTree
			root.add(imgnode);
		}
	}
	
	
	
	// Adaptadores para los botones
	
	public class addAction implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
			// Comprueba que se seleccionase algo
			if (from_tree.getLastSelectedPathComponent() == null) {
				IJ.showMessage("Primero debe seleccionar algun contenedor o banda!");
				return;
			}
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)from_tree.getLastSelectedPathComponent();
			Object obj = node.getUserObject();
			// Comprueba si es un tipo de nodo correcto
			if (!(obj instanceof TreeNode)) {
				IJ.showMessage("Por favor, seleccione un contenedor o una banda");
				return;
			}
			TreeNode elem = (TreeNode)obj; 
			
			///////////////////////////////////////
			// Agregamos al otro arbol la/s banda/s
			DefaultTreeModel modelf = (DefaultTreeModel)from_tree.getModel();
			DefaultTreeModel modelt = (DefaultTreeModel)to_tree.getModel();
			DefaultMutableTreeNode roott = (DefaultMutableTreeNode)modelt.getRoot();
			
			// Caso de una banda
			if (elem.processor != null) {
				DefaultMutableTreeNode cloned = (DefaultMutableTreeNode)node.clone();
				modelt.insertNodeInto(cloned, roott, modelt.getChildCount(roott));
				to_tree.expandPath(new TreePath(roott.getPath()));
			}

			// Caso de multiples bandas
			if (elem.image != null) {
				int n = modelf.getChildCount(node);
				for (int i=0; i<n; i++) {
					DefaultMutableTreeNode cloned = (DefaultMutableTreeNode)((DefaultMutableTreeNode)modelf.getChild(node, i)).clone();
					modelt.insertNodeInto(cloned, roott, modelt.getChildCount(roott));
				}
			}

		} // actionPerformed method
	} // ActionListener
	
	
	
	public class removeAction implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)to_tree.getLastSelectedPathComponent();
			if (!(node.getUserObject() instanceof TreeNode)) {
				IJ.showMessage("No se ha seleccionado una banda correcta");
				return;
			}
			DefaultTreeModel modelt = (DefaultTreeModel)to_tree.getModel();
			modelt.removeNodeFromParent((MutableTreeNode)to_tree.getLastSelectedPathComponent());
			
		}
	}
	
	
	
	public class createAction implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
			DefaultTreeModel modelt = (DefaultTreeModel)to_tree.getModel();
			DefaultMutableTreeNode roott = (DefaultMutableTreeNode)modelt.getRoot();

			int n = roott.getChildCount();
			
			if (n==0) {
				IJ.showMessage("No se seleccionaron bandas!");
				return;
			}
			
			int width, height, bits;
			ImageStack stack = null;
			
			// Creacion de la pila
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)roott.getChildAt(0);
			TreeNode elem = (TreeNode)node.getUserObject();
			ImageProcessor proc = elem.processor; 
			width = proc.getWidth();
			height = proc.getHeight();
			stack = new ImageStack(width, height);

			// Agrega los procesadores
			for (int i=0;i<n;i++) {
				node = (DefaultMutableTreeNode)roott.getChildAt(i);
				elem = (TreeNode)node.getUserObject();
				proc = elem.processor;
				if (proc.getWidth() != width || proc.getHeight() != height) {
					stack = null;
					IJ.showMessage("Error! todas las bandas deben coincidir en tamaño!");
					return;
				}
				stack.addSlice(elem.title, proc.duplicate());
			}
			
			
			
			ImagePlus imp = new ImagePlus("Nueva Pila",stack);
			imp.show();
			IJ.showStatus("Pila creada con exito!");
			window.dispose();
		}
	}
	
	
	private class nodeRenameListener implements TreeSelectionListener {
        @Override
		public void valueChanged(TreeSelectionEvent e) {
			/*
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)to_tree.getLastSelectedPathComponent();
			TreeNode elem = (TreeNode) node.getUserObject();
			elem.title = e.getNewLeadSelectionPath().toString();
			IJ.showMessage(elem.title);
			*/
		}
	}
	

	
		
} // class plugin
