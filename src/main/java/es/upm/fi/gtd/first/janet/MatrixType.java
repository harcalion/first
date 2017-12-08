package es.upm.fi.gtd.first.janet;

import java.io.Serializable;


/**
 * The interface MatrixType defines methods that must
 * be implemented by a class wishing to be used
 * by Janet elements for Matrix-like operations.
 * A class implementing the MatrixType interface
 * must support a number of class and instance methods.
 * For instance, standard set and get methods
 * (see {@link #set(int,int,double)} and {@link #get(int,int)}),
 * as well as standard arithmethic operations such as
 * vector addition and subtraction 
 * (see {@link #add(MatrixType,MatrixType)} and 
 * {@link #sub(MatrixType,MatrixType)}).
 * A very important property of any MatrixType implementation is
 * that the class must know of a {@link Janet.VectorType},
 * i.e. it must be able to create instances of {@link Janet.VectorType}
 * (see {@link #getVectorType()}).
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
**/
public interface MatrixType 
    extends Serializable, XMLExpressible {

    /**
     * The method should return a new MatrixType
     * with <CODE>n</CODE> rows and <CODE>m</CODE> columns.
     * @param n the number of rows in the MatrixType.
     * @param m the number of columns in the MatrixType.
     * @return a <CODE>n</CODE>-by-<CODE>m</CODE> MatrixType.
     **/
    public MatrixType getMatrixType( int n, int m );

    /**
     * The method should return a new MatrixType
     * with dimensions identical to the double array
     * given as argument.
     * The elements in the MatrixType should be identical
     * to those in <CODE>m</CODE>.
     * The element in row <CODE>i</CODE> and column <CODE>j</CODE> of the 
     * double array     
     * should be the same as the element in the 
     * <CODE>i+1</CODE>st row and <CODE>j+1</CODE>st column
     * of the MatrixType.
     * @param m a double array.
     * @return a MatrixType.
     **/
    public MatrixType getMatrixType( double[][] m );

    /**
     * The method should return a new MatrixType
     * with same dimensions as <CODE>m</CODE>, and whose elements are
     * all initialized to the values of <CODE>m</CODE>.
     * @param m a MatrixType.
     * @return a MatrixType that is a copy of the argument <CODE>m</CODE>.
     **/
    public MatrixType getMatrixType( MatrixType m );

    /**
     * Returns a double array containing the elements of
     * the MatrixType.
     * The element in row <CODE>i</CODE> and column <CODE>j</CODE> of the MatrixType
     * should be in the <CODE>i-1</CODE>st row and <CODE>j-1</CODE>st column
     * of the double array.
     * @return a double array containing the elements of the MatrixType. 
     **/
    public double[][] getArray();

    /**
     * The method should return a new instance of a {@link Janet.VectorType}.
     * There is no requirement about the length of the {@link Janet.VectorType}.
     * @return an instance of a {@link Janet.VectorType}.
     **/
    public VectorType getVectorType();

    /**
     * The method should return the number of rows in the MatrixType.
     * @return the number of rows in the MatrixType.
     **/
    public int getRowDimension();

    /**
     * The method should return the number of columns in the MatrixType.
     * @return the number of columns in the MatrixType.
     **/
    public int getColumnDimension();

    /**
     * The method should set the element of the MatrixType at the
     * <CODE>i</CODE>th row 
     * <CODE>j</CODE>th column 
     * of the MatrixType to <CODE>value</CODE>.
     * @param i     is the row index of the MatrixType element to set.
     * @param j     is the column index of the MatrixType element to set.
     * @param value is the new value of the MatrixType element.
     **/
    public void set( int i, int j, double value );

    /**
     * The method should return the element of the MatrixType
     * in the <CODE>i</CODE>th row and <CODE>j</CODE>th column.
     * @param i is the row index of the MatrixType element requested.
     * @param j is the column index of the MatrixType element requested.
     * @return the value of the element in the 
     * <CODE>i</CODE>th row and <CODE>j</CODE>th column of the MatrixType.
     **/
    public double get( int i, int j );

    /**
     * The method should compute the sum <CODE>a+b</CODE> of the two MatrixType arguments and
     * return the result in a new MatrixType.
     * @param a a MatrixType. 
     * @param b a MatrixType. 
     * @return a MatrixType containing the sum of the argument MatrixTypes.
     **/
    public MatrixType add( MatrixType a, MatrixType b );

    /**
     * The method should compute the difference <CODE>a-b<CODE> of the two MatrixType arguments and
     * return the result in a new MatrixType.
     * @param a a MatrixType.
     * @param b a MatrixType.
     * @return a MatrixType containing the difference of the argument MatrixTypes.
     **/
    public MatrixType sub( MatrixType a, MatrixType b );

    /**
     * The method should compute the product <CODE>a*b</CODE> of the two MatrixType arguments and
     * return the result in a new MatrixType.
     * The number of columns in <CODE>a</CODE> and the
     * number of rows in <CODE>b</CODE> must coincide.
     * The returned MatrixType will have the same number of
     * rows as <CODE>a</CODE> and the same number of columns as <CODE>b</CODE>.
     * @param a a MatrixType. 
     * @param b a MatrixType. 
     * @return a MatrixType containing the product of the argument MatrixTypes.
     **/
    public MatrixType mul( MatrixType a, MatrixType b );

    /**
     * The method should perform an elementwise multiplication of the double <CODE>c</CODE>
     * onto the MatrixType <CODE>a</CODE>
     * and return the result in a new MatrixType.
     * @param a a MatrixType.
     * @param c a scalar variable.
     * @return a MatrixType containing the elementwise multiplication of <CODE>c</CODE> and <CODE>a</CODE>.
     **/
    public MatrixType mul( MatrixType a, double c );

    /**
     * The method should compute the product <CODE>a*x</CODE> of the 
     * MatrixType <CODE>a</CODE> and the {@link Janet.VectorType} <CODE>x</CODE> and
     * return the result in a new {@link Janet.VectorType}.
     * @param a a MatrixType. 
     * @param x a VectorType. 
     * @return a VectorType containing the product of the arguments.
     **/
    public VectorType mul( MatrixType a, VectorType x );

    /**
     * The method should return a n-by-n unit MatrixType.
     * @param n row and column dimension of the required unit MatrixType.
     * @return a n-by-n unit MatrixType.
     **/
    public MatrixType unit( int n );
    
    /**
     * Solve the linear system of equations <CODE>ax=v</CODE>
     * where <CODE>x</CODE> is the unknown {@link Janet.VectorType}.
     * @param a a MatrixType. 
     * @param v a VectorType. 
     * @return a VectorType containing the solution to the linear
     * system <CODE>ax=v</CODE>.
     **/
    public VectorType solve( MatrixType a, VectorType v );

    /**
     * The method should return a java.util.Vector with
     * the eigenvalues of the MatrixType.
     * The first component should contain a VectorType
     * with the real parts of the eigenvalues,
     * and the second component  should contain a VectorType 
     * with the imaginary parts of the eigenvalues.
     **/
    public java.util.Vector getEigenvalues();

    /**
     * The method should return the eigenvectors of the MatrixType
     * in a {@link java.util.Vector}.
     **/
    public java.util.Vector getEigenvectors();

    /**
     * The method should return the determinant of the MatrixType.
     * @return the determinant of the MatrixType.
     **/
    public double det( MatrixType A );

    /**
     * The method should return the inverse of the MatrixType.
     * @return the inverse of the MatrixType.
     **/
    public MatrixType inverse( MatrixType A );

    /**
     * Transposes this <B>MatrixType</B> and returns a new
     * <B>MatrixType</B>.
     **/
    public MatrixType transpose();

    /**
     * The method should return a String representation of the MatrixType.
     * @return a String representation of the MatrixType.
     **/
    @Override
    public String toString();

} // MatrixType
