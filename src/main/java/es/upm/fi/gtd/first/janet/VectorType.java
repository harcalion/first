package es.upm.fi.gtd.first.janet;

import java.io.Serializable;

public interface VectorType 
extends Serializable, XMLExpressible {

/**
 * The method should return a new VectorType
 * with length <CODE>n</CODE>.
 * @param n the length of the VectorType.
 * @return a VectorType with length <CODE>n</CODE>.
 **/
public VectorType getVectorType( int n );

/**
 * The method should return a new VectorType
 * with length <CODE>n</CODE> and whose elements are
 * all initialized to the value <CODE>x</CODE>.
 **/
public VectorType getVectorType( int n, double x );

/**
 * The method should return a new VectorType
 * with length <CODE>v.length</CODE> whose
 * elements are identical to those of <CODE>v</CODE>.
 **/
public VectorType getVectorType( double[] v );

/**
 * The method should return a new VectorType
 * which is a copy of <CODE>v</CODE>.
 **/
public VectorType getVectorType( VectorType v );

/**
 * Returns an array containing the elements of
 * the VectorType.
 * The <CODE>i</CODE>th element of the VectorType
 * should be the <CODE>i-1</CODE>st element of the double array.
 * @return a double array containing the elements of the VectorType. 
 **/
public double[] getArray();

/**
 * The method should return a new instance of a {@link Janet.MatrixType}.
 * There is no requirement about the dimensions of the {@link Janet.MatrixType}.
 * @return an instance of a {@link Janet.MatrixType}.
 **/
public MatrixType getMatrixType();

/**
 * Returns the length of the VectorType.
 * @return the length of the VectorType. 
 **/
public int length();

/**
 * The method should set the <CODE>i</CODE>th element of the VectorType to <CODE>value</CODE>.
 * @param i     is the index of the VectorType element to set.
 * @param value is the new value of the VectorType element.
 **/
public void set( int i, double value );

/**
 * The method should return the <CODE>i</CODE>th element of the VectorType.
 * @param i is the index of the VectorType element requested.
 * @return the value of the <CODE>i</CODE>th element of the VectorType.
 **/
public double get( int i );

/**
 * The method should compute the sum <CODE>x+y</CODE> of the two VectorType arguments and
 * return the result in a new VectorType.
 * @param x a VectorType. 
 * @param y a VectorType. 
 * @return a VectorType containing the sum of the argument VectorTypes.
 **/
public VectorType add( VectorType x, VectorType y );

/**
 * The method should compute the difference <CODE>x-y</CODE> of the two VectorType arguments and
 * return the result in a new VectorType.
 * @param x a VectorType.
 * @param y a VectorType.
 * @return a VectorType containing the difference of the argument VectorTypes.
 **/
public VectorType sub( VectorType x, VectorType y );

/**
 * The method should perform an elementwise multiplication of the double <CODE>c</CODE>
 * onto the VectorType <CODE>x</CODE>
 * and return the result in a new VectorType.
 * @param x a VectorType.
 * @param c a scalar variable.
 * @return a VectorType containing the elementwise multiplication of <CODE>c</CODE> and <CODE>x</CODE>.
 **/
public VectorType mul( VectorType x, double c );

/**
 * The method should return a VectorType whose elements are the absolute value
 * of the elements of the argument VectorType.
 * @param x a VectorType.
 * @return a VectorType with same lenght as the argument of
 * the mehtod whose elements have the absolute of the
 * argument VectorTypes.
 **/
public VectorType abs( VectorType x );

/**
 * The method should compute the inner or dot product of the two VectorType arguments and
 * return the result as a double.
 * @param x a VectorType. 
 * @param y a VectorType. 
 * @return a double containing the inner or dot product.
 **/
public double dot( VectorType x, VectorType y );

/**
 * The method should compute the usual Euclidean norm of the VectorType and
 * return the result as a double.
 * @return a double containing the norm of the VectorType.
 **/
public double norm();

/**
 * The method should return a String representation of the VectorType.
 * @return a String representation of the VectorType.
 **/
    @Override
public String toString();

}
