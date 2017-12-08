package es.upm.fi.gtd.first;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import es.upm.fi.gtd.first.janet.CBSpline;
import es.upm.fi.gtd.first.janet.IllegalDimensionException;
import es.upm.fi.gtd.first.janet.Vector;




/**
 * Clase que extiende un JPanel para permitir el dibujo de curvas en un gráfico 2D con ejes
 * @author  Alvar García del Río
 */

public class Panelon {
	/**
	 * Identificador de versión para serialización
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * Valores de los puntos en el eje X
	 */
	private static Vector xpoints=null;
	/**
	 * Valores de los puntos de ERGAS espacial en el eje Y
	 */
	private static Vector ypointsEspacial=null;
	/**
	 * Valores de los puntos de ERGAS espectral en el eje Y
	 */
	private static Vector ypointsEspectral=null;
	/**
	 * Valor mínimo de la curva interpolada para ERGAS espacial
	 */
	private static double minimo = 0.0d;
        /**
         * Valores de la curva de ERGAS espacial
         */
	private double [] valoresEspacial = null;
        /**
         * Valores de la curva de ERGAS espectral
         */
	private double [] valoresEspectral = null;
        /**
         * Valores de X de las curvas
         */
	private double [] valoresX = null;
	/**
         * Menú contextual
         */
	private JPopupMenu popup;




	
    /**
     * Constructor
     * @param principal2 Objeto principal de la aplicación
     */
	Panelon(Atrous principal2)
	{
            ParseXML ic = principal2.getIC();
		popup = new JPopupMenu();

	    JMenuItem menuItem = new JMenuItem(ic.buscar(38)); // Tag 38

	    popup.add(menuItem);
	    


	}
	
	/**
	 * Modifica los valores de algunos atributos de una instancia de esta clase 
	 * @param xp Valores de los puntos en X
	 * @param yp Valores de los puntos de ERGAS espacial en Y
	 * @param zp Valores de los puntos de ERGAS espectral en Y
	 */

	public void setData (double[] xp, double [] yp, double [] zp)
	{

		xpoints = new Vector(xp);
		ypointsEspacial = new Vector(yp);
		ypointsEspectral = new Vector(zp);

								
	}
        
    /**
     * Obtiene el punto donde las curvas se han intersecado
     * @return Punto de intersección
     */
    public double getInter()
        {
           
            double interseccion = 0.0d;
            double valortral = 0.0d, valorcial = 0.0d;
            boolean found = false;
            
            // Crear los splines (global)
          /*  if ((csbEspacial==null) || (csbEspectral == null))
            {
                try
		{
			csbEspacial = new CBSpline(xpoints,ypointsEspacial);
			csbEspectral = new CBSpline(xpoints,ypointsEspectral);
		}
		catch (IllegalDimensionException ide){ Dialogos.informarExcepcion(ide);}
            }*/
            int i = 0;
            while (!found && (i < valoresEspacial.length))
            //for (int i = 0; i < valoresEspacial.length; i++)
            {
                // Obtener los valores para el paso actual
                valorcial = valoresEspacial[i];
		valortral = valoresEspectral[i];
                
                if (valortral > valorcial)
			{ // Se ha encontrado la intersección
				found = true;
				double x1,y1a,y1b,x2,y2a,y2b,ma,mb,na,nb;
                                if (i!=0)
                                {
                                    x1 = valoresX[i-1];
                                }
                                else
                                {
                                    x1 = valoresX[0];
                                }
				x2 = valoresX[i];
                                if (i!=0)
                                {
                                    y1a = valoresEspacial[i-1];
                                    y1b = valoresEspectral[i-1];
                                }
                                else
                                {
                                    y1a = valoresEspacial[0];
                                    y1b = valoresEspectral[0];
                                }
				y2a = valorcial;
				y2b = valortral;				
				ma = (y2a-y1a)/(x2-x1);						
				mb = (y2b-y1b)/(x2-x1);									
				na = y1a-ma*x1;
				nb = y1b-mb*x1;
                                if (i!=0)                                
                                    interseccion = (nb-na)/(ma-mb);    
                                
				

			}
                i++;
            }
            return interseccion;
            
        }
	
	/**
	 * Obtiene la intersección de las curvas de ERGAS espacial y ERGAS espectral utilizando
	 * interpolación con splines cúbicos
	 */
	
	/*public double getInter ()
	{
		int npp= 10*xpoints.length();
		double espacioX = 1/(2*(double)npp);
		double inter = 0;
		double valortral = 0, valorcial = 0;
		boolean found = false;
		CBSpline csbEspacial = null, csbEspectral = null;

		try
		{
			csbEspacial = new CBSpline(xpoints,ypointsEspacial);
			csbEspectral = new CBSpline(xpoints,ypointsEspectral);
		}
		catch (IllegalDimensionException ide){ Dialogos.informarExcepcion(ide);}
		int i = 0;
		while(!found && (i < npp*4))
		{
			valorcial = csbEspacial.value(i*espacioX);
			valortral = csbEspectral.value(i*espacioX);
			//System.out.println("Valor en X: "+(i*espacioX)+"Valor Espacial: "+valorcial+" Valor Espectral: "+valortral);
			if (valortral > valorcial)
			{ // Se ha encontrado la intersección
				found = true;
				double x1,y1a,y1b,x2,y2a,y2b,ma,mb,na,nb;
				x1 = (i-1)*espacioX;						
				x2 = i*espacioX;
				//System.out.println("La intersección está entre (X) "+x1+" y "+x2);
				y1a = csbEspacial.value((i-1)*espacioX);
				y1b = csbEspectral.value((i-1)*espacioX);
				y2a = valorcial;
				y2b = valortral;
				//System.out.println("Y entre los valores espectrales (Y) "+y1b+" y "+y2b);
				//System.out.println("Y entre los valores espaciales (Y) "+y1a+" y "+y2a);
				ma = (y2a-y1a)/(x2-x1);						
				mb = (y2b-y1b)/(x2-x1);
				//System.out.println("Pendiente Espacial "+ma+" y Pendiente Espectral "+mb);						
				na = y1a-ma*x1;
				nb = y1b-mb*x1;
				//System.out.println("Corte en el origen Espacial "+na+" y Corte en el origen Espectral "+nb);
				inter = (nb-na)/(ma-mb);

			}
			i++;
			
		}
		return inter;
	}*/
	
	/**
	 * Obtiene el mínimo de la curva de ERGAS espacial utilizando una spline cúbica
	 * @return  Mínimo de la curva
	 * @uml.property  name="minimo"
	 */
	
	public double getMinimo ()
	{
		int npp = 10*xpoints.length(); 	// Número de nodos intermedios                
                 double paso = ((xpoints.get(xpoints.length())-xpoints.get(1)) / npp); // Distancia entre puntos intermedios
		System.out.println("npp es "+npp+" y paso es "+paso);	
		double vale,vala;
		
		valoresEspacial = new double[npp];
		valoresEspectral = new double[npp];
		valoresX = new double[npp];
               
            CBSpline csbEspectral = null, csbEspacial = null;
		try
		{
                    csbEspacial = new CBSpline(xpoints,ypointsEspacial);
                    csbEspectral = new CBSpline(xpoints,ypointsEspectral);
		}
		catch (IllegalDimensionException ide){ide.printStackTrace();}
                System.out.println("Comprobamos que los splines se han creado bien. Máximo Espacial: "+csbEspacial.maxFX()+" Máximo Espectral: "+csbEspectral.maxFX());
		for (int i = 0; i < npp; i++)
		{	
                    //System.out.println("Mínimo: Valor de paso: "+paso+", Nº de paso: "+i+", Valor en X"+(minPond+i*paso));
			valoresX[i] = xpoints.get(1)+i*paso;
                        System.out.println("Mínimo: Valor X:"+valoresX[i] );
			vala = csbEspectral.value(valoresX[i]);
			valoresEspectral[i] = vala;
                        System.out.println("Mínimo: Valor espectral:"+valoresEspectral[i] );
			vale = csbEspacial.value(valoresX[i]);
			valoresEspacial[i] = vale;
                        System.out.println("Mínimo: Valor espacial:"+valoresEspacial[i] );
			// Acumulo el mínimo
			if (vale < csbEspacial.value(minimo))
			{
				minimo = valoresX[i];
			}
		}
                // Aquí tenemos todos los valores, hallar la intersección se puede hacer recorriendo los arrays...
                System.out.println("El mínimo es "+minimo);
		return minimo;
                
	}
	
    /**
     * Devuelve los valores de la curva ERGAS espacial
     * @return Valores de ERGAS espacial
     */
	public double [] getValoresEspacial ()
	{
		return valoresEspacial;
	}
	    /**
     * Devuelve los valores de la curva ERGAS espectral
     * @return Valores de ERGAS espectral
     */
	public double [] getValoresEspectral ()
	{
		return valoresEspectral;
	}
   /**
     * Devuelve los valores de X de la curva
     * @return Valores de X
     */
	public double [] getValoresX ()
	{
		return valoresX;
	}
	

	
}
