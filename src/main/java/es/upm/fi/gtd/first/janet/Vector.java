package es.upm.fi.gtd.first.janet;

import java.util.StringTokenizer;

/**
 * The <b>Vector</b> class provides an implementation for <b>real</b>
 * vectors with no distinction between <i>row</i> and <i>column</i>
 * vectors.
 * <p>
 * Indexing of the elements goes from 1 to the length of the vector. 
 * The class provides methods for
 * <i>array</i> arithmetics on the elements, together with <i>norm</i>
 * and <i>dot-</i> and <i>cross-product</i> and various other methods.
 *
 * @author Laurits H&oslash;jgaard Olesen
 * @author Paul M. Diderichsen
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.1
**/
public class Vector implements VectorType {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***** Instance Fields *********************************/
    
    private double[] v;

    private static Matrix matrix = new Matrix( 1, 1 );
        
    /***** Constructors ************************************/
    
    /**
     * Constructs a new <b>Vector</b> of the elements specified by
     * double array <i>d</i>.
     *
     * @param d   double array of the future elements of this vector
     **/
    public Vector( double[] d ) {
	v = new double[ d.length ];
	for(int i = 0; i < d.length; i++)
	    v[ i ] = d[ i ];
    }
    
    /**
     * Constructs a new <b>Vector</b> of length <i>n</i> with elements
     * initialized to zero.
     *
     * @param n length of the vector
     **/
    public Vector( int n ) {
	this( n, 0.0 );
    }
    
    /**
     * Constructs a new <b>Vector</b> of length <i>n</i> with all elements 
     * initialized to <i>d</i>.
     *
     * @param n   length of the vector
     * @param d   initial value of the elements of this vector
     **/
    public Vector( int n, double d ) {
	v = new double[ n ];
	for (int i = 0; i < n; i++)
	    v[ i ] = d;
    }
    
    /**
     * Constructs a new <b>Vector</b> containing a copy of <i>w</i>.
     *
     * @param w   vector to be copied
     **/
    public Vector( Vector w ) {
	v = new double[ w.v.length ];
	for (int i = 0; i < v.length; i++)
	    v[ i ] = w.v[ i ];
    }
    
    /**
     * Constructs a new <b>Vector</b> of the elements specified by <i>s</i>, 
     * with <i>s</i> assumed to be a string containing <i>comma-</i>, 
     * <i>semi colon-</i> or <i>space-</i> separated doubles. Any brackets are 
     * ignored.
     * <p>
     * Example of use:<br>
     * <pre>
     * Vector a = new Vector( "1,2.5,-4.1,0,0" );
     * Vector b = new Vector( "( 1; 2.5; -4.1; 0; 0; )" );
     * Vector c = new Vector( " 1 2.5 -4.1 0 0 " );
     *
     * Vector d = new Vector( "[(1];, ()2.5,,,, -4.1, 0 0 (" );
     * </pre>
     * Vector <i>d</i> is an example of a constructor that does the job, but
     * the notation is not in general adviced.
     *
     * @param s   string of comma-, semi colon- or space- separated doubles
     **/
    public Vector( String s ) {
	StringTokenizer tokenizer = new StringTokenizer( s, "()[]{},; ", false );
	v = new double[ tokenizer.countTokens() ];
	
	for (int i = 0; i < v.length; i++)
	    v[ i ] = Double.parseDouble( tokenizer.nextToken() );
    }
    
    
    /***** Instance Methods ********************************/

    //
    // Implementation of VectorType interface
    //
    
    /**
     * Get the element at index <i>i</i>.
     *
     * @param i   index in range 1 to length of this vector
     *
     * @return   element at <i>i</i>
     **/
    public double get( int i ) {
	return v[ i - 1 ];
    }
    
    /**
     * Set the element at index <i>i</i> to <i>d</i>.
     *
     * @param i   index in range 1 to length of this vector
     * @param d   value to be set
     **/
    public void set( int i, double d ) {
	v[ i - 1 ] = d;
    }
    
    /**
     *
     **/
    public VectorType getVectorType( int n ) {
	return new Vector( n );
    }
    
    /**
     *
     **/
    public VectorType getVectorType( int n, double v ) {
	return new Vector( n, v );
    }
    
    /**
     *
     **/
    public VectorType getVectorType( double[] v ) {
	return new Vector( v );
    }
    
    /**
     *
     **/
    public VectorType getVectorType( VectorType v ) {
	return new Vector( v.getArray() );
    }

    /**
     *
     **/
    public double[] getArray() {
	return v;
    }
    
    /**
     *
     **/
    public MatrixType getMatrixType() {
	return matrix;
    }

    /**
     *
     **/
    public VectorType add( VectorType x, VectorType y ) throws ArithmeticException {
	return Vector.add( (Vector)getVectorType( x ), (Vector)getVectorType( y ) );
    }

    /**
     *
     **/
    public VectorType sub( VectorType x, VectorType y ) throws ArithmeticException {
	return Vector.sub( (Vector)getVectorType( x ), (Vector)getVectorType( y ) );
    }

    /**
     *
     **/
    public VectorType mul( VectorType x, double c ) {
	return ((Vector)getVectorType( x )).mul( c );
    }

    /**
     *
     **/
    public VectorType abs( VectorType x ) {
	VectorType y = x.getVectorType( x.length() );
	for (int i = 1; i <= x.length(); i++)
	    y.set( i, Math.abs( x.get( i ) ) );
	return y;
    }

    /**
     *
     **/
    public double dot( VectorType x, VectorType y ) {
	return Vector.dot( (Vector)getVectorType( x ), (Vector)getVectorType( y ) );
    }

    //
    // End of implementation of VectorType interface
    //


    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    /**
     * True if all elements are equal.
     *
     * @param a   <b>Vector</b> to be compared with
     **/
    public boolean equals(Vector a) {
	if ( v.length != a.v.length )
	    return false;
	for (int i = 0; i < v.length; i++)
	    {
		if ( v[ i ] != a.v[ i ] )
		    return false;
	    }
	return true;
    }
    
    /**
     * Sum of elements.
     *
     * @return   sum of all elements of this vector
     **/
    public double sum() {
	double sum = v[0];
	for (int i=1; i<v.length; i++)
	    sum += v[i];
	return sum;
    }
    
    /**
     * The 2-norm, i.e. ||<b>x</b>|| = (<i>x<sub>1</sub><sup>2</sup> + 
     * x<sub>2</sub><sup>2</sup> + x<sub>3</sub><sup>2</sup> + ... + 
     * x<sub>n</sub><sup>2</sup></i>)<i><sup>1/2</sup></i>.
     *
     * @return   the 2-norm of this vector
     **/
    // NOTICE COULD BE OPTIMIZED BY FACTORING OUT THE LARGEST ELEMENT OF THE VECTOR - SEE Complex.abs()
    public double norm() {
	double norm = v[ 0 ] * v[ 0 ];
	for (int i = 1; i < v.length; i++)
	    norm += v[ i ] * v[ i ];
	return Math.sqrt( norm );
    }
    
    /**
     * The 2-norm squared, i.e. ||<b>x</b>||<sup>2</sup> = (<i>x<sub>1</sub><sup>2</sup> +
     * x<sub>2</sub><sup>2</sup> + x<sub>3</sub><sup>2</sup> + ... +
     * x<sub>n</sub><sup>2</sup></i>).
     *
     * @return   the squared 2-norm of this vector
     **/
    public double sqrNorm() {
	double norm2 = v[ 0 ] * v[ 0 ];
	for (int i = 1; i < v.length; i++)
	    norm2 += v[ i ] * v[ i ];
	return norm2;
    }
    
    /**
     * Number of elements.
     *
     * @return   the number of elements of this vector
     **/
    public int length() {
	return v.length;
    }
    
    /**
     * String representation, e.g. <code>(1.3,-5,2.1,6)</code>
     *
     * @return   this vector as a string of comma separated doubles 
     * enclosed in rounded brackets.
     **/
    @Override
    public String toString() {
	String s = "(" + v[ 0 ];
	for (int i = 1; i < v.length; i++)
	    s += "," + v[ i ];
	return s + ")";
    }
    
    /**
     * Returns an XML version of the Vector.
     **/
    public String toXML() {
	StringBuffer buffer = new StringBuffer( 1024 );
	buffer.append( "<vector>" );
	buffer.append( StandardValues.CR );
	buffer.append( "<dimension>" );
	buffer.append( v.length );
	buffer.append( "</dimension>" );
	buffer.append( StandardValues.CR );
	for (int i = 0; i < v.length; i++) {
	    buffer.append( "<real>" );
	    buffer.append( v[ i ] );
	    buffer.append( "</real>" );
	    buffer.append( StandardValues.CR );
	} // for
	buffer.append( "</vector>" );
	buffer.append( StandardValues.CR );
	return buffer.toString();
    } // toXML
    
    /**
     * Index of maximum element.
     *
     * @return   index of first occurence of maximum element of this vector 
     * in the range 1 to the length of this vector
     **/
    public int imax() {
	int imax = 0;
	for (int i = 1; i < v.length; i++) {
	    if ( v[ i ] > v[ imax ] )
		imax = i;
	}
	return imax + 1;
    }
    
    /**
     * Index of minimum element.
     *
     * @return   index of first occurence of minumum element of this vector
     * in the range 1 to the length of this vector
     **/
    public int imin() {
	int imin = 0;
	for (int i = 1; i < v.length; i++) {
	    if ( v[i] < v[ imin ] )
		imin = i;
	}
	return imin+1;
    }
    
    /**
     * Absolute value instance method.
     **/
    public Vector abs() {
	for (int i = 0; i < v.length; i++)
	    v[ i ] = Math.abs( v[ i ] );
	return this;
    }
    
    /**
     * <b>+=</b> operator. Set this vector to the sum of this and <i>a</i>, 
     * and returns the result.
     * <p>
     * <i>a</i> must be of the same length as this.
     *
     * @param a  vector to be added
     *
     * @return   this after addition of <i>a</i>
     *
     * @throws ArithmeticException    if <i>this</i> and <i>a</i> are of unequal length
     **/
    public Vector add( Vector a ) throws ArithmeticException {
	if ( v.length != a.v.length )
	    throw new ArithmeticException( "using += operator, length of Vectors don't match: "
					   + v.length + "!=" + a.v.length );
	
	for (int i = 0; i < v.length; i++)
	    v[ i ] += a.v[ i ];
	return this;
    }

    
    /**
     * <b>+=</b> operator. Increses every element in this vector by scalar
     * <i>d</i>,
     * and returns the result.
     * <p>
     *
     * @param d  scalar to be added
     *
     * @return   this after addition of <i>d</i>
     **/
    public Vector add( double d ) {
	for (int i = 0; i < v.length; i++)
	    v[ i ] += d;
	return this;
    }
    
    /**
     * <b>-=</b> operator. Set this vector to the difference of this and
     * <i>a</i>, and returns the result.
     * <p>
     * <i>a</i> must be of the same length as this.
     *
     * @param a  vector to be subtracted
     *
     * @return   this after subtraction of <i>a</i>
     *
     * @throws ArithmeticException    if <i>this</i> and <i>a</i> are of unequal length
     **/
    public Vector sub(Vector a) throws ArithmeticException {
	if ( v.length != a.v.length )
	    throw new ArithmeticException( "using -= operator, length of Vectors don't match: "
					   + v.length + "!=" + a.v.length );
	
	for (int i = 0; i < v.length; i++)
	    v[ i ] -= a.v[ i ];
	return this;
    }
    
    /**
     * <b>-=</b> operator. Decreses every element in this vector by scalar
     * <i>d</i>, and returns the result.
     *
     * @param d  scalar to be subtracted
     *
     * @return   this after subtraction of <i>d</i>
     **/
    public Vector sub( double d ) {
	for (int i = 0; i < v.length; i++)
	    v[ i ] -= d;
	return this;
    }
    
    /**
     * <b>*=</b> operator. Set this vector to the product of this and scalar
     * <i>d</i>, and returns the result.
     *
     * @param d  scalar to be multiplied
     *
     * @return   this after multiplication with <i>d</i>
     **/
    public Vector mul( double d ) {
	for (int i = 0; i < v.length; i++)
	    v[ i ] *= d;
	return this;
    }
    
    /**
     * <b>*=</b> <i>array</i> product operator. Multiplies every element
     * in this vector with corresponding element in <i>a</i>, stores the
     * result in this, and returns this.
     * <p>
     * <i>a</i> must be of the same length as this.
     *
     * @param a  vector to be multiplied
     *
     * @return   this after array multiplication with <i>a</i>
     *
     * @throws ArithmeticException   if <i>this</i> and <i>a</i> are of unequal length
     **/
    public Vector mul(Vector a) throws ArithmeticException {
	if ( v.length != a.v.length )
	    throw new ArithmeticException( "using *= operator, length of Vectors don't match: "
					   + v.length + "!=" + a.v.length );
	
	for (int i = 0; i < v.length; i++)
	    v[ i ] *= a.v[ i ];
	return this;
    }
    
    /**
     * <b>/=</b> operator. Set this vector to the division of this and
     * scalar <i>d</i>, and returns the result.
     *
     * @param d  scalar with which to divide
     *
     * @return   this after division with <i>d</i>
     **/
    public Vector div( double d ) {
	for (int i = 0; i < v.length; i++)
	    v[ i ] /= d;
	return this;
    }
    
    /**
     * <b>/=</b> <i>array</i> division operator. Divides every element
     * in this vector with corresponding element in <i>a</i>, stores the
     * result in this, and returns this.
     * <p>
     * <i>a</i> must be of the same length as this.
     *
     * @param a  vector with which to divide
     *
     * @return   this after array division with <i>a</i>
     *
     * @throws ArithmeticException    if <i>this</i> and <i>a</i> are of unequal length
     **/
    public Vector div( Vector a ) throws ArithmeticException {
	if ( v.length != a.v.length )
	    throw new ArithmeticException( "using /= operator, length of Vectors don't match: "
					   + v.length + "!=" + a.v.length );
	
	for (int i = 0; i < v.length; i++)
	    v[ i ] /= a.v[ i ];
	return this;
    }
    
    /***** Class Methods ***********************************/    
    
    /**
     * Addition of <i>a</i> and <i>b</i>.
     * <p>
     * <i>a</i> and <i>b</i> must be of the same length.
     *
     * @param a  
     * @param b  vectors to be added
     *
     * @return   addition of <i>a</i> and <i>b</i>
     *
     * @throws ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     **/
    public static Vector add(Vector a, Vector b) throws ArithmeticException {
	if ( a.v.length != b.v.length )
	    throw new ArithmeticException("using + operator, length of Vectors don't match: "+a.v.length+"!="+b.v.length);
	
	Vector w = new Vector( a.v.length );
	for (int i = 0; i < w.v.length; i++)
	    w.v[ i ] = a.v[ i ] + b.v[ i ];
	return w;
    }
    
    /**
     * Addition of scalar <i>d</i> to every element in vector
     * <i>a</i>.
     *
     * @param d  double and
     * @param a  vector to be added
     *
     * @return   addition of <i>a</i> and <i>d</i>
     **/
    public static Vector add(Vector a, double d) {
	Vector w = new Vector(a.v.length);
	for (int i = 0; i < w.v.length; i++)
	    w.v[ i ] = a.v[ i ] + d;
	return w;
    }
    
    /**
     * Addition of scalar <i>d</i> to every element in vector
     * <i>a</i>.
     *
     * @param d  double and
     * @param a  vector to be added
     *
     * @return   addition of <i>a</i> and <i>d</i>
     **/
    public static Vector add( double d, Vector a ) {
	return add( a, d );
    }
    
    /**
     * Subtraction of <i>a</i> and <i>b</i>, <i>a - b</i>.
     * <p>
     * <i>a</i> and <i>b</i> must be of the same length.
     *
     * @param a  
     * @param b  vectors to be subtracted
     *
     * @return   subtraction of <i>b</i> from <i>a</i>
     *
     * @throws ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     **/
    public static Vector sub(Vector a, Vector b) throws ArithmeticException {
	if ( a.v.length != b.v.length )
	    throw new ArithmeticException( "using - operator, length of Vectors don't match: "
					   + a.v.length + "!=" + b.v.length );
	
	Vector w = new Vector( a.v.length );
	for (int i = 0; i < w.v.length; i++)
	    w.v[ i ] = a.v[ i ] - b.v[ i ];
	return w;
    }
    
    /**
     *Subtraction of scalar <i>d</i> from every element in vector
     *<i>a</i>, <i>a-d</i>.
     *
     *@param a  vector and
     *@param d  double to be subtracted
     *
     *@return   subtraction of <i>d</i> from <i>a</i>
     **/
    public static Vector sub(Vector a, double d)
    {
	Vector w = new Vector(a.v.length);
	for (int i=0; i<w.v.length; i++)
	    w.v[i] = a.v[i]-d;
	return w;
    }
	
    /**
     *Subtraction of every element in vector <i>a</i> from scalar
     *<i>d</i>, <i>d-a</i>.
     *
     *@param d  double and
     *@param a  vector to be subtracted
     *
     *@return   subtraction of <i>a</i> from <i>d</i>
     **/
    public static Vector sub(double d, Vector a)
    {
	Vector w = new Vector(a.v.length);
	for (int i=0; i<w.v.length; i++)
	    w.v[i] = d-a.v[i];
	return w;
    }
	
    /**
     *<i>Array</i> multiplication of <i>a</i> and <i>b</i>.
     *<p>
     *<i>a</i> and <i>b</i> must be of the same length.
     *
     *@param a  
     *@param b  vectors to be multiplied
     *
     *@return   <i>array</i> multiplication of <i>a</i> and <i>b</i>
     *
     *@throws ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     **/
    public static Vector mul(Vector a, Vector b) throws ArithmeticException
    {
	if (a.v.length != b.v.length)
	    throw new ArithmeticException("using * operator, length of Vectors don't match: "+a.v.length+"!="+b.v.length);
		
	Vector w = new Vector(a.v.length);
	for (int i=0; i<w.v.length; i++)
	    w.v[i] = a.v[i]*b.v[i];
	return w;
    }
	
    /**
     *Multiplication of scalar <i>d</i> and <i>a</i>.
     *
     *@param d  double and
     *@param a  vector to be multiplied
     *
     *@return   multiplication of <i>a</i> and <i>d</i>
     **/
    public static Vector mul(Vector a, double d)
    {
	Vector w = new Vector(a.v.length);
	for (int i=0; i<w.v.length; i++)
	    w.v[i] = a.v[i]*d;
	return w;
    }
	
    /**
     *Multiplication of scalar <i>d</i> and <i>a</i>.
     *
     *@param d  double and
     *@param a  vector to be multiplied
     *
     *@return   multiplication of <i>a</i> and <i>d</i>
     **/
    public static Vector mul(double d, Vector a)
    {
	return mul(a,d);
    }
	
    /**
     *<i>Array</i> division of <i>a</i> and <i>b</i>.
     *<p>
     *<i>a</i> and <i>b</i> must be of the same length.
     *
     *@param a  
     *@param b  vectors to be divided.
     *
     *@return   <i>array</i> division of <i>a</i> and <i>b</i>
     *
     *@throws ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     **/
    public static Vector div(Vector a, Vector b) throws ArithmeticException
    {
	if (a.v.length != b.v.length)
	    throw new ArithmeticException("using / operator, length of Vectors don't match: "+a.v.length+"!="+b.v.length);
		
	Vector w = new Vector(a.v.length);
	for (int i=0; i<w.v.length; i++)
	    w.v[i] = a.v[i]/b.v[i];
	return w;
    }
	
    /**
     *Division of vector <i>a</i> with scalar <i>d</i>.
     *
     *@param a  vector and
     *@param d  double to be divided with
     *
     *@return   division of <i>a</i> with <i>d</i>
     **/
    public static Vector div( Vector a, double d ) {
	Vector w = new Vector( a.v.length );
	for (int i = 0; i < w.v.length; i++)
	    w.v[ i ] = a.v[ i ] / d;
	return w;
    }
	
    /**
     * <i>Array</i> division of scalar <i>d</i> with vector <i>a</i>.
     *
     * @param d  double and
     * @param a  vector to be divided with
     *
     * @return   recipocal value of every element of <i>a</i> multiplied
     * by <i>d</i>
     **/
    public static Vector div( double d, Vector a )
    {
	Vector w = new Vector( a.v.length );
	for (int i = 0; i < w.v.length; i++)
	    w.v[ i ] = d / a.v[ i ];
	return w;
    }
	
    /**
     * Value of maximum element of <i>a</i>.
     *
     * @return   maximum value of <i>a</i>
     **/
    public static double max( Vector a ) {
	double max = a.v[0];
	for (int i=1; i<a.v.length; i++)
	    {
		if (a.v[i] > max)
		    max = a.v[i];
	    }
	return max;
    } // max
	
    /**
     * Value of minimum element of <i>a</i>.
     *
     * @return   minimum value of <i>a</i>
     **/
    public static double min( Vector a ) {
	double min = a.v[0];
	for (int i = 1; i < a.v.length; i++)
	    {
		if ( a.v[ i ] < min )
		    min = a.v[ i ];
	    }
	return min;
    } // min
    
    /**
     * Dot product or scalar product of <i>a</i> and <i>b</i>.
     *
     * @param a  
     * @param b  vectors to be multiplied.
     *
     * @return   scalar product of <i>a</i> and <i>b</i>
     *
     * @throws ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     **/
    public static double dot( Vector a, Vector b ) throws ArithmeticException {
	if (a.v.length != b.v.length)
	    throw new ArithmeticException( "using DOT operator, length of Vectors don't match: " 
					   + a.v.length + "!=" + b.v.length );
		
	double d = a.v[ 0 ] * b.v[ 0 ];
	for (int i = 1; i < a.v.length; i++)
	    d += a.v[ i ] * b.v[ i ];
	return d;
    } // dot
	
    /**
     * Cross product of <i>a</i> and <i>b</i>.
     * <p>
     * Only defined for <b>3-dimensional</b> vectors.
     * @param a  
     * @param b  vectors to be multiplied.
     *
     * @return   cross product of <i>a</i> and <i>b</i>
     *
     * @throws ArithmeticException    if <i>a</i> and <i>b</i> are not of length 3
     **/
    public static Vector cross( Vector a, Vector b ) throws ArithmeticException {
	if ( a.v.length != 3 || b.v.length != 3)
	    throw new ArithmeticException("Using CROSS operator - length of Vectors don't match: "
					  + a.v.length + " or " + b.v.length + " != 3" );
		
	Vector w = new Vector(3);
	w.v[ 0 ] = a.v[ 1 ] * b.v[ 2 ] - a.v[ 2 ] * b.v[ 1 ];
	w.v[ 1 ] = a.v[ 2 ] * b.v[ 0 ] - a.v[ 0 ] * b.v[ 2 ];
	w.v[ 2 ] = a.v[ 0 ] * b.v[ 1 ] - a.v[ 1 ] * b.v[ 0 ];
	return w;
    } // cross

} // Vector
