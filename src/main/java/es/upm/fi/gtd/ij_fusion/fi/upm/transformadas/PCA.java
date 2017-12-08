package es.upm.fi.gtd.ij_fusion.fi.upm.transformadas;

import es.upm.fi.gtd.first.jama.EigenvalueDecomposition;
import es.upm.fi.gtd.first.jama.Matrix;

/**
 * Clase para el cálculo de Análisis de Componentes Principales (PCA)
 * así de como su inversa. Requiere de la librería <b>Jama</b> para el tratamiento
 * de matrices.
 * @author Francisco Javier Merino Guardiola
 * @see <i>"Digital Image Processing"</i>(Gonzalez, Woods, Eddings) Capítulo 11.5 - <i>"Using Principal Components for Description"<i>
 *
 */
public class PCA {
    // Matrices necesarias para el calculo de la PCA
    private Matrix X;  // Matriz de imagenes
    //private Matrix Y;  // Matriz de imagenes transformada
    private Matrix A;  // Matriz de cambio
    private Matrix Mx; // Matriz extendida de medias
    // Tamaño de la matriz de las imagenes iniciales 
    //private int n;     // Numero de bandas
    private int K;     // NxM (numero de pixeles por banda)
    // Variables relacionadas con la escala de valores resultante
    private double minimo; // Valor minimo de la transformada
    private double maximo; // Valor maximo de la transformada
    private double factor; // Factor por el que se ha de multiplicar para normalizar

    public PCA(Matrix imagenes) {
        K = imagenes.getRowDimension();
        //	n = imagenes.getColumnDimension();
        X = imagenes;
    }

    /**
     * Calcula la matriz de covarianzas y el vector de vmedias de una poblacion
     * @param X Matriz con las poblaciones
     * @return Matriz = {0=>Cx, 1=>mx}
     */
    private Matrix[] covmatrix(Matrix X) {
        int K = X.getRowDimension();
        int n = X.getColumnDimension();
        Matrix C; // Covarianza
        Matrix m; // Vector media

        // Caso particular de que solo haya una fila
        if (n == 1) {
            C = null;
            m = X;
        } // En caso de haber varias filas
        else {
            double[][] media = new double[1][n];
            // Inicializamos el vector de media
            for (int i = 0; i < n; i++) {
                media[0][i] = 0;
            }
            // Calculamos el vector media
            for (int i = 0; i < K; i++) {
                for (int j = 0; j < n; j++) {
                    media[0][j] += X.get(i, j);
                }
            }
            for (int j = 0; j < n; j++) {
                media[0][j] /= K;
            }
            m = new Matrix(media).transpose();

            // Calculamos la matriz de covarianza
            //???X = X.copy();
            for (int i = 0; i < K; i++) {
                for (int j = 0; j < n; j++) {
                    X.set(i, j, X.get(i, j) - media[0][j]);
                }
            }
            C = X.transpose().times(X).times(1. / (K - 1));
        }

        Matrix[] res = new Matrix[2];
        res[0] = C;
        res[1] = m;
        return res;
    }

    /**
     * Realiza el analisis de componentes principales
     * @param q Numero de autovalores a utilizar
     * @return matriz con las proyecciones
     */
    public Matrix Transformada(int q) {
        /* Calcula la matriz de covarianzas y el vector media */
        Matrix[] cm = covmatrix(X);
        Matrix Cx = cm[0];
        Matrix mx = cm[1].transpose();
        cm = null;
        
        // Calcula los autovalores y autovectores
        EigenvalueDecomposition evd = Cx.eig();

        /* Invertimos la diagonal de autovalores así como la matriz de autovectores
         * de modo que tenga orden descendente */
        Matrix V = OrdenarAutovectores(evd.getV(), evd.getRealEigenvalues());

        // Obtenemos la matriz A
        A = V.getMatrix(0, V.getColumnDimension() - 1, 0, q - 1).transpose();

        // Calculamos Mx extendiendo las columnas
        double[][] Mx_ = new double[K][mx.getRowDimension()];
        for (int i = 0; i < K; i++) {
            Mx_[i] = mx.getRowPackedCopy();
        }
        Mx = new Matrix(Mx_);

        // Calculamos Y
        Matrix Y = A.times(X.minus(Mx).transpose());

        // Liberamos algo de memoria
        X = null;

        // Obtener los vectores reconstruidos
        //X = A.transpose().times(Y).transpose().plus(Mx);

        // Convierte Y y mx
        Y = Y.transpose();

        // Calcula el factor de escala por si se necesitase normalizar
        FactorEscala(Y);
        //mx = mx.transpose();

        // Matrix de covarianza Cy
        //Matrix Cy = A.times(Cx.times(A.transpose()));
        return Y;
    }

    /**
     * Calcula la transformada inversa dada una matriz, más concretamente
     * realiza: X = (A'*Y)'+Mx;
     * @param y Matriz a la que se le aplicara su inversa
     * @return
     */
    public Matrix Transformada_Inversa(Matrix y) {
        return A.transpose().times(y.transpose()).transpose().plus(Mx);
    }

    /**
     * Ordena una matriz de autovectores tras reordenar sus columnas segun
     * el valor de sus autovalores (ordena en orden descendiente).
     * @param M Matriz de autovectores correspondiente a sus autovalores.
     * @param v Array de autovalores correspondiente a la matriz de autovectores.
     * @return
     */
    public Matrix OrdenarAutovectores(Matrix M, double[] v) {
        // s = sorted vector
        double[] s = v.clone();
        java.util.Arrays.sort(s);
        // c = checked vector
        boolean[] c = new boolean[v.length];
        java.util.Arrays.fill(c, true);
        // o = orden de cambios
        int[] o = new int[v.length];

        // Calculo del vector de ordenacion
        for (int i = 0; i < v.length; i++) {
            double actual = v[i];
            for (int j = 0; j < v.length; j++) {
                if (s[j] == actual && c[j]) {
                    o[i] = v.length - j - 1;
                    c[j] = false;
                    break;
                }
            }
        }


        // Calcula la matriz de cambios (reordenacion de las columnas)
        double[][] a = new double[v.length][v.length];
        for (int i = 0; i < v.length; i++) {
            java.util.Arrays.fill(a[i], 0.);
            a[i][o[i]] = 1;
        }
        Matrix A = new Matrix(a);
        A = A.transpose();


        // Aplica la matriz de cambios
        return M.times(A);
    }

    /**
     * Reescalar los valores de las imagenes
     * @param M Matriz con las imagenes (una por columna)
     */
    public void FactorEscala(Matrix M) {
        minimo = Double.MAX_VALUE;
        maximo = Double.MIN_VALUE;

        for (int i = 0; i < M.getRowDimension(); i++) {
            for (int j = 0; j < M.getColumnDimension(); j++) {
                if (minimo > M.get(i, j)) {
                    minimo = M.get(i, j);
                }
                if (maximo < M.get(i, j)) {
                    maximo = M.get(i, j);
                }
            }
        }

        factor = 1. / (maximo - minimo);

//		for (int i=0; i<M.getRowDimension(); i++)
//			for (int j=0; j<M.getColumnDimension(); j++)
//				M.set(i, j, (M.get(i, j)-minimo)*factor);
    }

    public Matrix EscalarValores(Matrix M, int bits) {
        for (int i = 0; i < M.getRowDimension(); i++) {
            for (int j = 0; j < M.getColumnDimension(); j++) //M.set(i, j, (M.get(i, j)-minimo)*factor*(Math.pow(2, bits)-1));
            {
                M.set(i, j, CambiaDeRango(M.get(i, j), bits));
            }
        }
        return M;
    }

    /**
     * Escala un valor sabiendo su profundidad de modo que 
     * @param v v valor doble perteneciente a la PCA
     * @param bits rango de [0..2^bits-1] desde el que se quiere cambiar
     * @return
     */
    public double CambiaDeRangoAPCA(double valor, int bits) {
        return (valor / (factor * (Math.pow(2, bits) - 1))) + minimo;
    }

    /**
     * Escala un valor obtenido en la PCA al rango establecido por 2^bits
     * @param v valor doble perteneciente a la PCA
     * @param bits rango de [0..2^bits-1] al que se cambiara 
     * @return Devuelve el valor del pixel en el rango especificado
     */
    public double CambiaDeRango(double valor, int bits) {
        return (valor - minimo) * (factor * (Math.pow(2, bits) - 1));
    }
}
