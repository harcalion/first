package es.upm.fi.gtd.first;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * Clase auxiliar que crea los diálogos de entrada de datos para los algoritmos
 * @author Alvar
 */
public class DialogosAux {
	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_FUSIONAR = 0;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_ERGAS = 1;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_DEGRADAR = 2;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_POND = 3;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_ATROUS = 4;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_BIL = 5;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_MUESTRAS = 6;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_ERGASYAFUS = 7;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_PEDIR_LH = 8;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_FUSIONAR = 9;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_ERGAS = 10;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_DEGRADAR = 11;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_POND = 12;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_ATROUS = 13;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_BIL = 14;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_MUESTRAS = 15;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_ERGASYAFUS = 16;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_PEDIR_LH = 17;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_PONDNOSEP = 18;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_PONDNOSEP = 19;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_PONDSEP = 20;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_PONDSEP = 21;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int DIAG_VARIO = 22;
        	/**
         * Constante para identificar los distintos tipos de diálogo
         */
	public static final int OPT_VARIO = 23;
	/**
     * Constante para identificar los distintos tipos de diálogo
     */
	public static final int DIAG_ESCALA = 24;
        	/**
         * Objeto principal
         */
	private static Atrous principal;
			
        private ParseXML ic;
	
        /**
         * Constructor de la clase
         * @param princ Objeto principal de estado y GUI de la aplicación
         */
        public DialogosAux(Atrous princ)
	{
		principal = princ;
		ic = principal.getIC();

	}
	
    /**
     * Crea el mensaje (message[]) del diálogo en cuestión
     * @param tipo Tipo de diálogo a crear
     * @return El array con los objetos creados
     */
	public Object [] crearMensaje (int tipo)
	{
		Object[] message = null;
	
		switch (tipo)
		{
			case DIAG_FUSIONAR:
			{
				message = new Object[6];	
    			JTextPane eti = new JTextPane ();
    			eti.setEditable(false);
    			eti.setText(ic.buscar(105)); // Tag 105
    			message[0] = eti;
    			JComboBox cbwavelet = new JComboBox();	    			
    			cbwavelet.addItem("db1");
    			cbwavelet.addItem("db2");
    			cbwavelet.addItem("db3");
    			cbwavelet.addItem("db4");
    			cbwavelet.addItem("db5");
    			cbwavelet.addItem("db6");
    			cbwavelet.addActionListener(principal.wvt);
    			message[1] = cbwavelet;		    			
    			JTextPane eti2 = new JTextPane ();
    			eti2.setEditable(false);
    			eti2.setText(ic.buscar(106)); // Tag 106
    			message[2] = eti2;		    			
    			JTextField niveles_PAN = new JTextField ("");
    			message[3] = niveles_PAN;
    			JTextPane eti3 = new JTextPane();
    			eti3.setEditable(false);
    			eti3.setText(ic.buscar(108)); // Tag 108
    			
    			message[4] = eti3;
    			JTextField niveles_MULTI = new JTextField("");
    			message[5] = niveles_MULTI;
    			break;
			}
			case DIAG_DEGRADAR:
			{
				message = new Object[5];
				JLabel eti = new JLabel (ic.buscar(107)); // Tag 107
				message[0] = eti;
				JTextField nivtex = new JTextField ("");
				message[1] = nivtex;
				JLabel eti2 = new JLabel (ic.buscar(105)); // Tag 105
				message[2] = eti2;
				JComboBox cbwavelet = new JComboBox();	    			
				cbwavelet.addItem("db1");
				cbwavelet.addItem("db2");
				cbwavelet.addItem("db3");
				cbwavelet.addItem("db4");
				cbwavelet.addItem("db5");
				cbwavelet.addItem("db6");
				cbwavelet.addActionListener(principal.wvt);
				message[3] = cbwavelet;
                                JCheckBox soloFinales = new JCheckBox(ic.buscar(177));
                                message[4] = soloFinales;
				break;
			}
			case DIAG_ERGAS:
			{
				message = new Object[12];	
    			JTextPane eti = new JTextPane ();
    			eti.setEditable(false);
    			eti.setText(ic.buscar(105)); // Tag 105
    			message[0] = eti;
    			JComboBox cbwavelet = new JComboBox();	    			
    			cbwavelet.addItem("b3spline");
    			cbwavelet.addActionListener(principal.wvt);
    			message[1] = cbwavelet;		    			
    			JTextPane eti2 = new JTextPane ();
    			eti2.setEditable(false);
    			eti2.setText(ic.buscar(106)); // Tag 106
    			message[2] = eti2;		    			
    			JTextField niveles_PAN = new JTextField ("");
    			message[3] = niveles_PAN;
    			JTextPane eti3 = new JTextPane();
    			eti3.setEditable(false);
    			eti3.setText(ic.buscar(108)); // Tag 108			    			
    			message[4] = eti3;
    			JTextField niveles_MULTI = new JTextField("");
    			message[5] = niveles_MULTI;
    			JTextPane eti4 = new JTextPane();
    			eti4.setEditable(false);
    			eti4.setText(ic.buscar(109)); // Tag 109
    			message[6] = eti4;
    			JComboBox interp = new JComboBox();
    			interp.addItem(ic.buscar(75)); // Tag 75
    			interp.addItem(ic.buscar(76)); // Tag 76
    			interp.addItem(ic.buscar(77)); // Tag 77
    			interp.addActionListener(principal.interp_lis);
    			message[7] = interp;
    			JTextField precision = new JTextField("8");
    			message[8] = precision;
    			JTextPane eti5 = new JTextPane();
    			eti5.setEditable(false);
    			eti5.setText(ic.buscar(110)); // Tag 110
    			message[9] = eti5;
    			JTextField ponder = new JTextField("");
    			message[10] = ponder;
    			JCheckBox sep = new JCheckBox(ic.buscar(111)); // Tag 111
                        sep.setSelected(true);
    			message[11] = sep;
    			break;
			}
			case DIAG_POND:
			{
				message = new Object[14];	
    			JTextPane eti = new JTextPane ();
    			eti.setEditable(false);
    			eti.setText(ic.buscar(105)); // Tag 105
    			message[0] = eti;
    			JComboBox cbwavelet = new JComboBox();	    			
    			cbwavelet.addItem("b3spline");
    			cbwavelet.addActionListener(principal.wvt);
    			message[1] = cbwavelet;		    			
    			JTextPane eti2 = new JTextPane ();
    			eti2.setEditable(false);
    			eti2.setText(ic.buscar(106)); // Tag 106
    			message[2] = eti2;		    			
    			JTextField niveles_PAN = new JTextField ("");
    			message[3] = niveles_PAN;
    			JTextPane eti3 = new JTextPane();
    			eti3.setEditable(false);
    			eti3.setText(ic.buscar(108)); // Tag 108		    			
    			message[4] = eti3;
    			JTextField niveles_MULTI = new JTextField("");
    			message[5] = niveles_MULTI;
    			JTextPane eti4 = new JTextPane();
    			eti4.setEditable(false);
    			eti4.setText(ic.buscar(109)); // Tag 109
    			message[6] = eti4;
    			JComboBox interp = new JComboBox();
    			interp.addItem(ic.buscar(75)); // Tag 75
    			interp.addItem(ic.buscar(76)); // Tag 76
    			interp.addItem(ic.buscar(77)); // Tag 77
                        interp.addItem(ic.buscar(78)); // Tag 78
                        interp.addItem(ic.buscar(79)); // Tag 79
    			interp.addActionListener(principal.interp_lis);
    			message[7] = interp;
    			JTextField precision = new JTextField("8");
    			message[8] = precision;
                        JTextPane eti5 = new JTextPane();
                        eti5.setEditable(false);
                        eti5.setText(ic.buscar(112)); // Tag 112
                        JTextField min = new JTextField("0.0");
                        JTextField max = new JTextField("5.0");
                        message[9] = eti5;
                        message[10] = min;
                        message[11] = max;
                        JTextPane eti6 = new JTextPane();
                        eti6.setEditable(false);
                        eti6.setText(ic.buscar(113)); // Tag 113
                        JTextField muestras = new JTextField("10");
                        message[12] = eti6;
                        message[13] = muestras;
    			break;
			}
			case DIAG_ATROUS:
			{
				message = new Object[11];	
    			JTextPane eti = new JTextPane ();
    			eti.setEditable(false);
    			eti.setText(ic.buscar(105)); // Tag 105
    			message[0] = eti;
    			JComboBox cbwavelet = new JComboBox();	    			
    			cbwavelet.addItem("b3spline");
    			cbwavelet.addActionListener(principal.wvt);
    			message[1] = cbwavelet;		    			
    			JTextPane eti2 = new JTextPane ();
    			eti2.setEditable(false);
    			eti2.setText(ic.buscar(106)); // Tag 106
    			message[2] = eti2;		    			
    			JTextField niveles_PAN = new JTextField ("");
    			message[3] = niveles_PAN;
    			JTextPane eti3 = new JTextPane();
    			eti3.setEditable(false);
    			eti3.setText(ic.buscar(108)); // Tag 108			    			
    			message[4] = eti3;
    			JTextField niveles_MULTI = new JTextField("");
    			message[5] = niveles_MULTI;
    			JTextPane eti4 = new JTextPane();
    			eti4.setEditable(false);
    			eti4.setText(ic.buscar(109)); // Tag 109
    			message[6] = eti4;
    			JComboBox interp = new JComboBox();
    			interp.addItem(ic.buscar(75)); // Tag 75
    			interp.addItem(ic.buscar(76)); // Tag 76
    			interp.addItem(ic.buscar(77)); // Tag 77
    			interp.addItem(ic.buscar(78)); // Tag 78
    			interp.addItem(ic.buscar(79)); // Tag 79
    			interp.addActionListener(principal.interp_lis);
    			message[7] = interp;
    			JTextField precision = new JTextField("8");
    			message[8] = precision;
    			JTextPane eti5 = new JTextPane();
    			eti5.setEditable(false);
    			eti5.setText(ic.buscar(110)); // Tag 110
    			message[9] = eti5;
    			JTextField ponder = new JTextField("");
    			message[10] = ponder;
    			break;
			}
			case DIAG_BIL:
			{
				message = new Object[8];
				JLabel banlab = new JLabel (ic.buscar(114)); // Tag 114
				JLabel linlab = new JLabel (ic.buscar(115)); // Tag 115
				JLabel collab = new JLabel (ic.buscar(116)); // Tag 116
				JLabel cellab = new JLabel (ic.buscar(117)); // Tag 117
				JTextField bantex = new JTextField ("");
				JTextField lintex = new JTextField ("");
				JTextField coltex = new JTextField ("");
				JTextField celtex = new JTextField ("");
				message[0] = banlab;
				message[1] = bantex;
				message[2] = linlab;
				message[3] = lintex;
				message[4] = cellab;
				message[5] = celtex;
				message[6] = collab;
				message[7] = coltex;
				break;
			}
			case DIAG_MUESTRAS:
			{
				message = new Object[2];
				JLabel mueslab = new JLabel (ic.buscar(118)); // Tag 118
				JTextField muestex = new JTextField ("");
				message[0] = mueslab;
				message[1] = muestex;
				break;
			}
			case DIAG_PEDIR_LH:
			{
				message = new Object[2];
				JLabel mueslab = new JLabel (ic.buscar(119)); // Tag 119
				JTextField muestex = new JTextField ("");
				message[0] = mueslab;
				message[1] = muestex;
				break;
			}
			case DIAG_ERGASYAFUS:
			{
				break;
			}
			case DIAG_PONDNOSEP:
			{
				message = new Object[1];
				JTextPane mensaje = new JTextPane ();
				mensaje.setEditable(false);						    					
				message[0] = mensaje;
				break;
			}
			case DIAG_PONDSEP:
			{
				message = new Object[1];
				JTextPane muestpane = new JTextPane ();				
				muestpane.setEditable(false);
				message[0] = muestpane;
				break;
			}
			case DIAG_VARIO:
			{
				message = new Object[1];
				/*JLabel mueslab = new JLabel ("Muestras");
				JLabel distlab = new JLabel ("Distancia máxima");
				JLabel tollab = new JLabel ("Tolerancia");
				JTextField muestex = new JTextField ("");
				JTextField disttex = new JTextField ("");
				JTextField toltex = new JTextField ("");
				message[0] = mueslab;
				message[1] = muestex;
				message[2] = distlab;
				message[3] = disttex;
				message[4] = tollab;

				message[5] = toltex;*/
                                JCheckBox metbox = new JCheckBox (ic.buscar(120)); // Tag 120
                                metbox.setSelected(true);
                                message[0] = metbox;
				break;
			}
			case DIAG_ESCALA:
			{			
				message = new Object[11];	
				JCheckBox cb = new JCheckBox(ic.buscar(68)); // Tag 68
				
				JCheckBox cb2 = new JCheckBox(ic.buscar(72)); // Tag 72
    			JTextPane eti4 = new JTextPane();
    			eti4.setEditable(false);
    			eti4.setText(ic.buscar(109)); // Tag 109
    			message[6] = eti4;
    			JComboBox interp = new JComboBox();
    			interp.addItem(ic.buscar(75)); // Tag 75
    			interp.addItem(ic.buscar(76)); // Tag 76
    			interp.addItem(ic.buscar(77)); // Tag 77
    			interp.addItem(ic.buscar(78)); // Tag 78
    			interp.addItem(ic.buscar(79)); // Tag 79
    			interp.addActionListener(principal.interp_lis);
    			message[7] = interp;
    			JTextField precision = new JTextField("8");
    			message[8] = precision;
    			JTextPane eti5 = new JTextPane();
    			eti5.setEditable(false);
    			eti5.setText(ic.buscar(110)); // Tag 110
    			message[9] = eti5;
    			JTextField ponder = new JTextField("");
    			message[10] = ponder;
    			break;
			}
			
		}
		return message;
		
		
	}
    /**
     * Crea las opciones (options[]) del diálogo en cuestión
     * @param tipo Tipo de diálogo a crear
     * @return Array de String con las opciones creadas
     */
	public String [] crearOpciones(int tipo)
	{
		String [] options = null;
		switch (tipo)
		{
			case OPT_FUSIONAR:
			{
				options = new String[2];
    			options [0] = ic.buscar(23); // Tag 23
    			options [1] = ic.buscar(24); // Tag 24
    			break;
			}
			case OPT_DEGRADAR:
			{
				options = new String[2];
    			options [0] = ic.buscar(23); // Tag 23
    			options [1] = ic.buscar(24); // Tag 24
    			break;
			}
			case OPT_ERGAS:
			{
				options = new String[2];
    			options [0] = ic.buscar(23); // Tag 23
    			options [1] = ic.buscar(24); // Tag 24
    			break;
			}
			case OPT_ATROUS:
			{
				options = new String[2];
    			options [0] = ic.buscar(23); // Tag 23
    			options [1] = ic.buscar(24); // Tag 24
    			break;
			}
			case OPT_MUESTRAS:
			{
				options = new String[2];
    			options [0] = ic.buscar(23); // Tag 23
    			options [1] = ic.buscar(24); // Tag 24
    			break;
			}
			case OPT_BIL:
			{
				options = new String[2];
    			options [0] = ic.buscar(23); // Tag 23
    			options [1] = ic.buscar(24); // Tag 24
    			break;
			}
			case OPT_PONDNOSEP:
			{
				options = new String[3];
				options [0] = ic.buscar(23); // Tag 23
				options [1] = ic.buscar(121); // Tag 121
				options [2] = ic.buscar(24); // Tag 24
				break;
			}
			case OPT_PONDSEP:
			{
				options = new String[3];
				options [0] = ic.buscar(23); // Tag 23
				options [1] = ic.buscar(122); // Tag 122
				options [2] = ic.buscar(24); // Tag 24
			}
			
		}
		return options;
	}

}
