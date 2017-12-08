package es.upm.fi.gtd.first.janet;

/**
 * Class for creating <i>non</i>-clamped cubic B-splines based upon a
 * dataset of <i>x</i>-values and the corresponding <i>y</i>-values:
 * <i>f(x)</i>. The fitted spline can return a value on the spline,
 * the first derivative or the second derivative at any point. The
 * corresponding methods are {@link #value}, {@link #firstDeriv}
 * and {@link #secondDeriv}.
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen (since v1.1)
 * @version 1.1 (250101)
 **/
public class CBSpline implements C2Function {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VectorType x, a, b, c, d, h, alpha, l, mu, z;
    private int n;

    int lastEvalIndex = -1;
    double lastEvalValue = Double.NEGATIVE_INFINITY;
    double xdiff1, xdiff2, xdiff3;

    /**
     * Returns the the minimum value of the given <i>x</i>'s.
     * @since 1.1
     **/
    public double minX() {
	return x.get(1);
    } // minX

    /**
     * Returns the the maximum value of the given <i>x</i>'s.
     * @since 1.1
     **/
    public double maxX() {
	return x.get( x.length() );
    } // maxX

    /**
     * Returns the the minimum value of the given <i>fx</i>'s.
     * @since 1.1
     **/
    public double minFX() {
	double minVal = Double.POSITIVE_INFINITY;
	for ( int i = 1; i <= a.length(); i++ ) {
	    minVal = ( minVal<a.get(i) ? minVal : a.get(i) );
	} // for
	return minVal;
    } // minFX

    /**
     * Returns the the maximum value of the given <i>fx</i>'s.
     * @since 1.1
     **/
    public double maxFX() {
	double maxVal = Double.NEGATIVE_INFINITY;
	for ( int i = 1; i <= a.length(); i++ ) {
	    maxVal = ( maxVal>a.get(i) ? maxVal : a.get(i) );
	} // for
	return maxVal;
    } // maxFX
    
    /**
     * Returns the interval in which the given x-value is
     * positioned. The interval counting starts from 0 and so forth.
     **/
    public int intervalForValue( double xVal ) {

	if ( ( xVal < x.get(1) ) || ( xVal > x.get(n+1) ) ) {
	    throw new RuntimeException( "The x-value has to be in the interval [ " + x.get(1) + " ; " + x.get(n+1) + " ]!" );
	} // if

	int retIndex = 0;

	for ( int i = 0; i<=n-1; i++ ) {
	    if ( xVal >= x.get(i+1) ) {
		retIndex = i;
	    } // if
	    else {
		break;
	    } // else	    
	} // for

	return retIndex;
    } // intervalForValue

    /**
     * Updates repetition values: index and powers of
     * (xVal-x_index). But only if necessary.
     **/
    private void updateRepValues( double xVal ) {
	if ( xVal != lastEvalValue ) {
	    lastEvalIndex = intervalForValue( xVal );
	    xdiff1 = ( xVal - x.get( lastEvalIndex+1 ) );
	    xdiff2 = xdiff1*xdiff1;
	    xdiff3 = xdiff1*xdiff2;
	} // if
    } // updateRepValues

    /**
     * Evaluates the B-spline at a given <i>x</i>-value. 
     **/
    public double value( double xVal ) {
	updateRepValues( xVal );
	return 
	    a.get( lastEvalIndex+1 ) + 
	    b.get( lastEvalIndex+1 )*xdiff1 +
	    c.get( lastEvalIndex+1 )*xdiff2 +
	    d.get( lastEvalIndex+1 )*xdiff3;
    } // eval
    
    /**
     * Returns the first derivative of the B-spline at a given
     * <i>x</i>-value.
     **/
    public double firstDeriv( double xVal ) {
	updateRepValues( xVal );
	return
	    b.get( lastEvalIndex+1 ) +
	    2*c.get( lastEvalIndex+1 )*xdiff1 +
	    3*d.get( lastEvalIndex+1 )*xdiff2;
    } // firstDeriv

    /**
     * Returns the second derivative of the B-spline at a given
     * <i>x</i>-value. 
     **/
    public double secondDeriv( double xVal ) {
	updateRepValues( xVal );
	return
	    2*c.get( lastEvalIndex+1 ) +
	    6*d.get( lastEvalIndex+1 )*xdiff1;
    } // secondDeriv

    /**
     * Creates a <i>non</i>-clamped cubic B-spline based upon the data
     * given as arguments. If there is dimension-mismatch between the
     * arguments a <@link Janet.IllegalDimensionException> is
     * thrown. Further, it is also thrown if there is less than two
     * point-sets in the given dataset.
     * 
     * @param x the x-values for the data-set, ordered in ascending
     * order.
     * @param fx the corresponding f(x)-values.
     * @since 1.0
     **/
    public CBSpline( VectorType x, VectorType fx ) 
	throws IllegalDimensionException {
	n = x.length() - 1; // n is one less that amount of points.

	if ( n != fx.length() - 1 ) {
	    throw new IllegalDimensionException( "The length of x must equal that of fx!");
	} // if,
	else if ( n < 1 ) {
	    throw new IllegalDimensionException( "There must be at least 2 points in the given dataset!");
	} // else if
	
	// *** x ***
	this.x = x.getVectorType( x );

	// *** a ***
	a = x.getVectorType( fx );

	// *** h ***
	h = x.getVectorType( n );
	for ( int i = 0; i <= n-1; i++ ) {
	    h.set( i+1, x.get(i+2) - x.get(i+1) );
	} // for

	// *** alpha ***
	alpha = x.getVectorType( n ); // alpha(1) unused
	double adiff3 = 3*( a.get(2) - a.get(1) ) / h.get(1);
	double adiff3New;
	for ( int i = 1; i <= n-1; i++ ) {
	    adiff3New = 3*( a.get(i+2)-a.get(i+1) ) / h.get(i+1);
	    alpha.set( i+1, adiff3New - adiff3 );
	    adiff3 = adiff3New;
	} // for
	
	// *** l, mu, z ***
	l = x.getVectorType( n+1 );
	mu = x.getVectorType( n );
	z = x.getVectorType( n+1 );
	
	l.set( 1, 1.);
	mu.set( 1, 0.);
	z.set( 1, 0.);

	for ( int i = 1; i <= n-1; i++ ) {
	    l.set( i+1, 2*( x.get(i+2) - x.get(i) ) -
		   h.get(i)*mu.get(i) );
	    mu.set( i+1, h.get(i+1)/l.get(i+1) );
	    z.set( i+1, ( alpha.get(i+1) - h.get(i)*z.get(i)
			  )/l.get(i+1) );
	} // for
	
	l.set( n+1, 1.);
	z.set( n+1, 0.);

	// *** b, c, d ***
	c = x.getVectorType( n+1 );
	b = x.getVectorType( n+1 );
	d = x.getVectorType( n+1 );

	c.set( n+1, 0.);

	for ( int j = n-1; j >= 0; j-- ) {
	    c.set( j+1, z.get(j+1) - mu.get(j+1)*c.get(j+2) );
	    b.set( j+1, (a.get(j+2)-a.get(j+1))/h.get(j+1) -
		   h.get(j+1)/3 * ( c.get(j+2)+2*c.get(j+1) ) );
	    d.set( j+1, ( c.get(j+2) - c.get(j+1) )/(3*h.get(j+1)) );
	} // for
    } // contr.

} // CBSpline
