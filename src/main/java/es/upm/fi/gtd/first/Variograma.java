package es.upm.fi.gtd.first;

import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import es.upm.fi.gtd.first.jama.Matrix;
import java.util.concurrent.RunnableFuture;


/**
 * Implementa el cálculo y dibujo por pantalla de un variograma que represente
 * la variabilidad espacial/espectral de una imagen
 */


public class Variograma {

		
	

	
    /**
     * Constructor
     * @param principal
     * @param image Imagen a partir de la cual construir el variograma
     * @param muestras Número de pasos en los que dividir la distancia máxima
     * @param dist Distancia máxima
     * @param tol Tolerancia permitida
     * @return Valores del variograma como un array de arrays de double
     */
	public static double [][] crearVariograma (Atrous principal, GeoImg image,int muestras, double dist, double tol)
	{
		// Pasar la imagen a una matriz
                RunnableFuture update = principal.getUpdate();
		int anchura = image.getImage().getWidth();
		int altura = image.getImage().getHeight();
		Raster ras = image.getImage().getData();
		
		double [] pixelesDouble = null;
		if (ras.getTransferType() == DataBuffer.TYPE_INT)
		{
			int [] pixeles = new int[anchura*altura];
			ras.getDataElements(0, 0, anchura, altura, pixeles);
			pixelesDouble = new double[anchura*altura];
			for (int i = 0; i < pixeles.length; i ++)
			{
				pixelesDouble[i]=pixeles[i];
			}
			
		}
		else if (ras.getTransferType() == DataBuffer.TYPE_USHORT)
		{
			short [] pixeles = new short[anchura*altura];
			ras.getDataElements(0, 0, anchura, altura, pixeles);
			
			pixelesDouble = new double[anchura*altura];
			for (int i = 0; i < pixeles.length; i ++)
			{
				pixelesDouble[i]=pixeles[i];
			}
			
			
		}
		else if (ras.getTransferType() == DataBuffer.TYPE_BYTE)
		{
			byte [] pixeles = new byte[anchura*altura];
			ras.getDataElements(0, 0, anchura, altura, pixeles);
			pixelesDouble = new double[anchura*altura];
			for (int i = 0; i < pixeles.length; i ++)
			{
				pixelesDouble[i]=pixeles[i];
			}
		}
		
		Matrix mat = new Matrix(pixelesDouble,anchura);
		// Por cada valor de distancia en función de las muestras
		//double [] valoresLag = new double[muestras+1];
                double [] valoresLag = {0,1,Math.sqrt(2),2,Math.sqrt(5), Math.sqrt(8),
                                        3, Math.sqrt(10),Math.sqrt(13),Math.sqrt(18)};
		double [] valoresVario = new double[valoresLag.length];
		int contadorValores = 0;
		for (double m :valoresLag)
		{
			valoresLag[contadorValores] = m;
			if (m != 0)
			{
				// Llamo al método para la matriz completa
                                double valorDistancia = calcularYSumarPuntos(m, tol, mat);
				//Set<ParDePuntos> resultado  = calcularPuntos(m, tol, mat);
				//System.out.println("El número de pares para m = "+m+" es "+resultado.size());
				//double valorDistancia = sumarValorDistancia (resultado,mat);
				valoresVario[contadorValores] = valorDistancia;
			}
			else
			{
				valoresVario[contadorValores] = 0.0;
			}
			principal.setProgreso(10+contadorValores*10);
                        update.run();
			contadorValores++;
		}
                	principal.setProgreso(100);
                        update.run();
		for (double d:valoresLag)
		{
			System.out.println("Los valores en valoresLag son "+d);
		}
		for (double d:valoresVario)
		{
			System.out.println("Los valores en valoresVario son "+d);
		}
		double [][] datos = {valoresLag,valoresVario};
		return datos;
	}

    /**
     * Suma de todos los valores de distancia al cuadrado de un conjunto de pares
     * de puntos
     * @param resultado Conjunto de pares de puntos
     * @param mat Matriz con los valores de pixel
     * @return Suma total
     */
	private static double sumarValorDistancia(Set<ParDePuntos> resultado, Matrix mat) {
		// Recorro el conjunto
		double suma = 0.0;
		for (Iterator<ParDePuntos> it = resultado.iterator(); it.hasNext();)
		{
			ParDePuntos actual = it.next();
			double valPunto1 = mat.get(actual.punto1.x, actual.punto1.y);
			double valPunto2 = mat.get(actual.punto2.x, actual.punto2.y);
			double resta = Math.abs(valPunto1-valPunto2);
			double cuadrado = Math.pow(resta,2);
			suma += cuadrado;
		}
                return suma;
		//return suma/resultado.size();
	}

    /**
     * Calcula todos los pares de puntos de la matriz mat que están a una 
     * distancia m entre sí, considerando una determinada tolerancia
     *
     * @param m Distancia
     * @param tol Tolerancia
     * @param mat Matriz con valores de píxeles
     * @return Conjunto de pares de puntos
     */
	private static Set<ParDePuntos> calcularPuntos(double m, double tol, Matrix mat) {
		// Recorremos la matriz y vamos calculando los pares que están
		// a una determinada distancia
		Set<ParDePuntos> resultado = new HashSet<ParDePuntos>();
		for (int i = 0; i < mat.getRowDimension(); i++)
		{
			for (int j = 0; j < mat.getColumnDimension(); j++)
			{
				resultado.addAll(calcularPuntos(new Point(i,j),m,tol,mat));
				
			}
		}
		return resultado;
	}
        
        	private static double calcularYSumarPuntos(double m, double tol, Matrix mat) {
		// Recorremos la matriz y vamos calculando los pares que están
		// a una determinada distancia
		Set<ParDePuntos> resultado = new HashSet<ParDePuntos>();
                int npares = 0;
                double res = 0;
		for (int i = 0; i < mat.getRowDimension(); i++)
		{
			for (int j = 0; j < mat.getColumnDimension(); j++)
			{
				resultado = calcularPuntos(new Point(i,j),m,tol,mat);
                                //System.out.println("Los valores para el punto ("+i+","+j+") y m ="+m+" son "+sumarValorDistancia(resultado,mat));
                                res += sumarValorDistancia(resultado,mat);
                                npares += resultado.size();
				
			}
		}
		return res/npares;
	}
	
    /**
     * Calcula todos los pares  puntos de la matriz mat que están a distancia m de la posición
     * pos y considerando una tolerancia tol
     * @param pos Posición base 
     * @param m Distancia
     * @param tol Tolerancia
     * @param mat Matriz de píxeles
     * @return Conjunto de pares de puntos a la distancia especificada
     */
	private static Set<ParDePuntos> calcularPuntos(Point pos, double m, double tol, Matrix mat) {
		// Creamos los índices del entorno en el que se buscará
		int i0 = pos.x-(int)Math.ceil(m); // Fila inicial
		if (i0 < 0)
		{
			i0 = 0;					
		}
		int i1 = pos.x+(int)Math.ceil(m); // Fila final
		if (i1 > mat.getRowDimension()-1)
		{
			i1 = mat.getRowDimension()-1;
		}
		int j0 = pos.y -(int)Math.ceil(m); // Columna inicial
		if (j0 < 0)
		{
			j0 = 0;					
		}
		int j1 = pos.y + (int)Math.ceil(m); // Columna final
		if (j1 > mat.getColumnDimension()-1)
		{
			j1 = mat.getColumnDimension()-1;
		}
		Matrix subMatriz = mat.getMatrix(i0, i1, j0, j1);
		Set<ParDePuntos> resultado = new HashSet<ParDePuntos>();
		for (int i = 0; i < subMatriz.getRowDimension(); i++)
		{
			for (int j = 0; j < subMatriz.getColumnDimension(); j++)
			{
				//if(m == 0.5)
					//System.out.println("La distancia entre el punto ("+pos.x+","+pos.y+") y el punto ("+(i0+i)+","+(j0+j)+") es "+pos.distance(new Point(i0+i,j0+j)));
				if (!pos.equals(new Point(i0+i,j0+j)))
				{
					if ((pos.distance(new Point(i0+i,j0+j))>=(m-tol))&&(pos.distance(new Point(i0+i,j0+j))<=(m+tol)))
					{// La distancia es la pedida teniendo en cuenta la tolerancia
						//if(m == 1)
							//System.out.println("La distancia entre el punto ("+pos.x+","+pos.y+") y el punto ("+(i0+i)+","+(j0+j)+") es "+pos.distance(new Point(i0+i,j0+j)));
						resultado.add(new ParDePuntos(pos,new Point(i0+i,j0+j))); // Aquí esperemos que no añada repetidos
					}
				}
			}
		}
		return resultado; // El resultado en el entorno del punto pedido
	}


	

}

class ParDePuntos extends Object
{
    /**
     *Punto 1
     */
	Point punto1;
        /**
         *Punto 2
         */
	Point punto2;
        /**
         *Hashcode del objeto
         */
	int hash;
    /**
     * Constructor
     * @param p1 Punto 1
     * @param p2 Punto 2
     */
	public ParDePuntos (Point p1, Point p2)
	{
		punto1 = p1;
		punto2 = p2;
		hash = p1.hashCode()+p2.hashCode();
	}
    /**
     * Sustituye a la operación básica e implementa que un par {(x1,y1),(x2,y2)}
     * es igual a un par {(x2,y2),(x1,y1)}
     * @param o Objeto
     * @return true si son iguales
     */
    @Override
	public boolean equals(Object o)
	{
        if (o instanceof ParDePuntos) {
            ParDePuntos temp = (ParDePuntos) o;
            if (((temp.punto1.equals(this.punto1)) && (temp.punto2.equals(this.punto2))) ||
                    ((temp.punto2.equals(this.punto1)) && (temp.punto1.equals(this.punto2)))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
	}
    /**
     * Necesario al cambiar equals. Devuelve el hash del objeto
     * @return Hash del objeto
     */
    @Override
	public int hashCode ()
	{
		return hash;
	}
    /**
     * Obtiene el punto 1
     * @return El punto 1
     */
	public Point getPunto1()
	{
		return punto1;			
	}
    /**
     * Obtiene el punto 2
     * @return El punto 2
     */
	public Point getPunto2()
	{
		return punto2;			
	}
}
