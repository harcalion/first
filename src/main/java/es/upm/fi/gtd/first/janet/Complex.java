package es.upm.fi.gtd.first.janet;

/**
 *The <b>Complex</b> class implements a representation of complex numbers
 *for use in calculations.
 *<p>
 *The class possess various constructors for complex numbers, methods for 
 *applying arithmetic operations on the members of the class, and 
 *implementation of some of the most common elementary functions defined for 
 *complex numbers.
 *<p>
 *Notice that the standard arithmetics and some functions exist both as class 
 *and instance methods. In general the method arguments are left unchanged,
 *but with the instance methods the calling instance may be modified.<br>
 *E.g. the <code>*=</code> operator is implemented as an instance method,
 *whereas the mere <code>*</code> operator is a class method.
 *<p>
 *Halfway internal stuff:<br>
 *The class operates internally with both rectangular and polar coordinates. 
 *If <code>abs(z)</code> and <code>arg(z)</code> are to be called repeatedly,
 *it will be favorable to force the <b>Complex</b> to remember its modulus and 
 *argument through a call to <code>toPolar()</code>.<br>
 *Methods are provided that allow the user to specify whether a multiplication 
 *or division should be performed in rectangular or polar coordinates - as 
 *default if just one of the two instances contain polar coordinates, the
 *calculation is returned as an instance containing polar coordinates.<br>
 *About the phase, the class does not force it to be within the principal 
 *branch ]-<i>pi;pi</i>]. If multipliing e.g. a cyclic time variable many times, 
 *the phase may become very large, and it should be considered to force the
 *instance into ]-<i>pi;pi</i>] with a call to <code>z.toPrincipal()</code>.
 *<p>
 *Example of use:<br>
 *Construct new complex number with real part 1 and imaginary part 2<br>
 *<code>
 *  Complex x = new <a href="Complex.html#Complex(double, double)">Complex</a>( 1, 2 );
 *</code><p>
 *Add complex numbers <code>x</code> and <code>y</code>, and store in new 
 *<code>Complex</code> object <code>z</code><br>
 *<code>
 *  Complex z = Complex.<a href="Complex.html#add(Complex, Complex)">add</a>( x, y );
 *</code><p>
 *Evaluate the complex sine function of the product of the imaginary unit
 *<code><a href="Complex.html#I">I</a></code> and <code>z</code><br>
 *<code>
 *  y = Complex.<a href="Complex.html#sin(Complex)">sin</a>( 
 *Complex.<a href="Complex.html#mul(Complex, Complex)">mul</a>( Complex.I, z ) );
 *</code><p>
 *Evaluate the complex logarithm function with the argument function defined by some
 *<a href="Domain.html">Domain</a> <code>D</code><br>
 *<code>
 *  y = Complex.<a href="Complex.html#log(Complex, Domain)">log</a>( z, D );
 *</code><p>
 *Check if <code>z</code> is zero<br>
 *<code>
 *  if( z.<a href="Complex.html#equals(Complex)">equals</a>( 
 *Complex.<a href="Complex.html#ZERO">ZERO</a> ) ){ <i>then</i> }
 *</code><p>
 *
 */
public class Complex
{
	
    /***** Instance fields **********************************************/
	
    /**
     *Real part of the complex number represented by this instance.
     */
    private double re;
	
    /**
     *Imaginary part of the complex number represented by this instance.
     */
    private double im;
	
    /**
     *Determines whether the rectangular coordinates specified by <i>re</i> 
     *and <i>im</i> are valid.
     */
    private boolean rect;
	
    /**
     *Modulus of the complex number represented by this instance.
     */
    private double mod;
	
    /**
     *Argument or angle of the complex number represented by this instance.
     */
    private double theta;
	
    /**
     *Determines whether the polar coordinates specified by <i>r</i> 
     *and <i>theta</i> are valid.
     */
    private boolean polar;
	
	
    /***** Class fields *************************************************/
	
    /**
     *The imaginary unit, <i>Complex</i>( 0, 1 ).
     */
    public static final Complex I = new Complex( 0, 1 ).toPolar();
	
    /**
     *The real unit, <i>Complex</i>( 1, 0 ).
     */
    public static final Complex ONE = new Complex( 1 );
	
    /**
     *The zero element, <i>Complex</i>( 0, 0 ).
     */
    public static final Complex ZERO = new Complex();
			
    /***** Constructors ***************************************************/
	
    /**
     *Constructs a complex number with zero real an imaginary part.
     */
    public Complex()
    {
	re = 0;
	im = 0;
	rect = true;
		
	mod = 0;
	theta = 0;
	polar = true;
    }
	
    /**
     *Constructs a complex number with real part <i>x</i> and zero
     *imaginary part, <i> z = x + 0 &middot; i</i>.
     *
     *@param x   the real part of this new little <b>Complex</b>
     */
    public Complex( double x )
    {
	re = x;
	im = 0;
	rect = true;
		
	mod = Math.abs(x);
	if( x >= 0 )
	    theta = 0;
	else
	    theta = Math.PI;
		
	polar = true;
    }
	
    /**
     *Constructs a complex number with real part <i>x</i> and imaginary
     *part <i>y</i>,
     *<i> z = x + y &middot; i</i>.
     *
     *@param x    real part
     *@param y    imaginary part of this new <b>Complex</b>
     */
    public Complex( double x, double y )
    {
	re = x;
	im = y;
	rect = true;
		
	polar = false;
    }
	
    /**
     *Constructs a complex number with <i>either</i> real part <i>x</i>
     *and imaginary part <i>y</i> if <i>rect</i> is <i>true, or</i>
     *modulus <i>x</i> and argument <i>y</i> if <i>rect</i> is <i>false</i>.
     *
     *@param x    real part
     *@param y    imaginary part of this new <b>Complex</b>
     *@param rectangular    specifies whether <i>x,y</i> are rectangular or 
     *polar coordinates
     */
    // NOTICE COULD BE PRIVATE ???
    public Complex( double x, double y, boolean rectangular )
    {
	if( rectangular )
	    {
		re = x;
		im = y;
		rect = true;
		polar = false;
	    }
	else
	    {
		mod = x;
		theta = y;
		polar = true;
		rect = false;
	    }
    }

    /**
     *Constructs a copy of the complex number <i>z</i>.
     *
     *@param z    <b>Complex</b> to be copied
     */
    public Complex( Complex z )
    {
	if( z.rect )
	    {
		re = z.re;
		im = z.im;
		rect = true;
	    }
	else
	    rect = false;
		
	if( z.polar )
	    {
		mod = z.mod;
		theta = z.theta;
		polar = true;
	    }
	else
	    polar = false;

    }

    /**
     *Assumes <i>s</i> is a string containing a complex number on the
     *form <i>2+0i</i> or <i>2-3i</i><br>
     *AS YET NOT IMPLEMENTED
     */
    public Complex( String s )
    {
	throw new ArithmeticException("Sorry - the method is not implemented as yet!");
    }


    /***** Instance methods ********************************************/

    /**
     *Real part of <i>z</i>.
     *<p>
     *NOTICE: If you are to call <i>real</i>() and <i>imag</i>() repeatedly
     *you may consider calling
     *<a href="Complex.html#toRect()"><code>toRect()</code></a>.
     *
     *@return real part of <b>Complex</b> <i>z</i>
     */
    public double real()
    {
	return this.rect ? this.re : this.mod*Math.cos( this.theta );
    }

    /**
     *Imaginary part of <i>z</i>.
     *<p>
     *NOTICE: If you are to call <i>real</i>() and <i>imag</i>() repeatedly
     *you may consider calling
     *<a href="Complex.html#toRect()"><code>toRect()</code></a>.
     *
     *@return imaginary part of <b>Complex</b> <i>z</i>
     */
    public double imag()
    {
	return this.rect ? this.im : this.mod*Math.sin( this.theta );
    }

    /**
     *Returns the modulus of this complex number.
     *
     *if you are to call <i>abs</i>() and <i>arg</i>() repeatedly
     *you may consider calling
     *<a href=Complex.html#toPolar><code>toPolar()</code></a>.
     */
    public double abs( )
    {
	if( this.polar )
	    {
		return this.mod;
	    }
	else
	    {
		// avoid loosing precision...
		double x = Math.abs( this.re );
		double y = Math.abs( this.im );

		if( x == 0 )
		    return y;
		if( y == 0 )
		    return x;

		if( x > y )
		    {
			y /= x;
			return x*Math.sqrt( 1 + y*y );
		    }
		else
		    {
			x /= y;
			return y*Math.sqrt( 1 + x*x );
		    }
	    }
    }

    /**
     *Returns the argument of this complex number in <i>some</i> branch
     *of the <i>arg</i> function.
     *<p>
     *if you are to call <i>abs</i>() and <i>arg</i>() repeatedly
     *you may consider calling
     *<a href=Complex.html#toPolar><code>toPolar()</code></a>.
     */
    public double arg()
    {
	return this.polar ? this.theta : Math.atan2( this.im, this.re );
    }

    /**
     *<code>z.equals(a)</code> returns true if <i>z</i> and
     *<i>a</i> have identical real and imaginary parts, false otherwise.
     */
    public boolean equals(Complex a)
    {
	if(rect && a.rect)
	    return (re == a.re && im == a.im) ? true : false;
	if(polar && a.polar)
	    return ( mod == a.mod && ( theta - a.theta ) % ( 2*Math.PI ) == 0 ) ? true : false;

	toRect();
	a.toRect();
	return ( re == a.re && im == a.im ) ? true : false;
    }

    /**
     *Turns this complex number into it's complex conjugate, and returns it.
     *
     *@return   this <b>Complex</b> after complex conjugation
     */
    public Complex conj()
    {
	if( rect )
	    im = -im;
	if( polar )
	    theta = -theta;
	return this;
    }
	
	
    /**
     *<code>z.add(a)</code> operates like the <code>+=</code> operator.
     *That is: writes <i>z+a</i> to <i>z</i> and returns this;
     *
     *@param a   <b>Complex</b> to be added
     *
     *@return   this <b>Complex</b> after addition of <i>a</i>
     */
    // returns rectangular
    public Complex add( Complex a )
    {
	toRect();
	a.toRect();
	re = re + a.re;
	im = im + a.im;
	polar = false;
	return this;
    }
	
    /**
     *<code>z.sub(a)</code> operates like the <code>-=</code> operator. 
     *That is: writes <i>z-a</i> to <i>z</i> and returns this;
     *
     *@param a   <b>Complex</b> to be subtracted
     *
     *@return   this <b>Complex</b> after subtraction of <i>a</i>
     */
    // returns rectangular
    public Complex sub( Complex a )
    {
	toRect();
	a.toRect();
	re = re - a.re;
	im = im - a.im;
	polar = false;
	return this;
    }
	
    /**
     *<code>z.mul(a)</code> operates like the <code>*=</code> operator. 
     *That is: writes <i>z*a</i> to <i>z</i> and returns this;
     *
     *@param a   <b>Complex</b> to be multiplied
     *
     *@return   this <b>Complex</b> after Multiplication with <i>a</i>
     */
    // returns rectangular on rect*=rect, polar otherwise
    public Complex mul( Complex a )
    {
	if( polar || a.polar )
	    {
		return this.mul( a, false );
	    }
	else
	    {
		re = re*a.re - im*a.im;
		im = im*a.re + re*a.im;
		return this;
	    }
    }
	
    /**
     *<code>z.mul(a)</code> operates like the <code>*=</code> operator. 
     *That is: writes <i>z*a</i> to <i>z</i> and returns this;<br>
     *Forces the answer to be either in rectangular or polar coordinates.
     *
     *@param a   <b>Complex</b> to be multiplied
     *
     *@return   this <b>Complex</b> after multiplication with <i>a</i>
     */
    public Complex mul( Complex a, boolean rectangular )
    {
	if( rectangular )
	    {
		this.toRect();
		a.toRect();
			
		re = re*a.re - im*a.im;
		im = im*a.re + re*a.im;
			
		polar = false;
		return this;
	    }
	else
	    {
		this.toPolar();
		a.toPolar();
			
		mod *= a.mod;
		theta = theta + a.theta;
			
		rect = false;
		return this;
	    }
    }
	
    /**
     *<code>z.mul(d)</code> operates like the <code>*=</code> operator.
     *That is: writes <i>z*d</i> to <i>z</i> and returns this;
     *
     *@param d   double to be multiplied
     *
     *@return   this <b>Complex</b> after multiplication with <i>d</i>
     */
    // returns polar on polar*=double, rectangular on rect*=double
    public Complex mul( double d )
    {
	if( rect )
	    {
		re *= d;
		im *= d;
	    }
	if( polar )
	    {
		if( d > 0 )
		    {
			mod *= d;
			return this;
		    }
		else if( d == 0 )
		    {
			re = 0;
			im = 0;
			rect = true;
				
			mod = 0;
			theta = 0;
			return this;
		    }
		else
		    {
			mod *= -d;
			theta += Math.PI;
			/* if( theta > 0 ) theta -= Math.PI; else theta += Math.PI; */
			return this;
		    }
	    }
	return this;
    }
	
    /**
     *<code>z.div(a)</code> operates like the <code>/=</code> operator. 
     *That is: writes <i>z/a</i> to <i>z</i> and returns this.
     */
    // returns rect on rect/=rect, polar otherwise
    public Complex div( Complex a )
    {
	if(polar || a.polar)
	    return this.div( a, false );
	else
	    return this.div( a, true );
    }
	
    /**
     *<code>z.div(a)</code> operates like the <code>/=</code> operator. 
     *That is: writes <i>z/a</i> to <i>z</i> and returns this.<br>
     *Forces the answer to be either in rectangular or polar coordinates.
     */
    public Complex div( Complex a, boolean rectangular )
    {
	if( a.equals( ZERO ) )
	    throw new ArithmeticException( "Divide by zero" );
		
	if( rectangular )
	    {
		this.toRect();
		a.toRect();
			
		// avoid loss of precision in this curious way...
		if( Math.abs( a.re ) >= Math.abs( a.im ) )
		    {
				// 'polar' is false on return, so we COULD overwrite the fields
			double fact = a.im/a.re;
			double denom = a.re + fact*a.im;
				
			re = ( re + im*fact )/denom;
			im = ( im - re*fact )/denom;
				
			polar = false;
			return this;
		    }
		else
		    {
				// 'polar' is false on return, so we COULD overwrite the fields
			double fact = a.re/a.im;
			double denom = fact*a.re + a.im;
				
			re = ( re*fact + im )/denom;
			im = ( im*fact - re )/denom;
				
			polar = false;
			return this;
		    }
	    }
	else
	    {
		this.toPolar();
		a.toPolar();
		mod /= a.mod;
		/* theta = ( theta - a.theta ) % ( 2*Math.PI ); if( theta > Math.PI ) theta -= 2*Math.PI; */
		theta = theta - a.theta;
			
		rect = false;
		return this;
	    }
    }
		
    /**
     *<code>z.div(d)</code> operates like the <code>/=</code> operator.
     *That is: writes <i>z/d</i> to <i>z</i> and returns this;
     */
    // returns polar on polar/=double and rect on rect/=double
    public Complex div( double d )
    {
	if( d == 0 )
	    throw new ArithmeticException( "Division by zero" );
		
	if( rect )
	    {
		re /= d;
		im /= d;
	    }
	if( polar )
	    {
		if( d > 0 )
		    {
			mod /= d;
			return this;
		    }
		else
		    {
			mod /= -d;
			theta += Math.PI;
				/*
				  if( theta > 0 )
				  theta -= Math.PI;
				  else
				  theta += Math.PI;
				*/
			return this;
		    }
	    }
	return this;
    }
	
    /**
     *Forces this <b>Complex</b> contain rectangular coordinates.
     */
    // NOTICE: WOULD THIS BE MORE CONVENIENT AS A VOID METHOD?? 
    public Complex toRect()
    {
	if( rect )
	    {
		return this;
	    }
	else
	    {
		if( mod == 0 )
		    {
			re = 0;
			im = 0;
			rect = true;
			return this;
		    }
		else
		    {
			re = mod*Math.cos( theta );
			im = mod*Math.sin( theta );
			rect = true;
			return this;
		    }
	    }
    }
	
    /**
     *Forces this <b>Complex</b> contain polar coordinates.
     */
    // NOTICE: WOULD THIS BE MORE CONVENIENT AS A VOID METHOD?? 
    public Complex toPolar()
    {
	if( polar )
	    {
		return this;
	    }
	else
	    {
		// avoid loss of precision in calculation of modulus
		double x = Math.abs( re );
		double y = Math.abs( im );
			
		if( x == 0 )
		    {
			if( y == 0 )
			    {
				mod = 0;
				theta = 0;
				polar = true;
				return this;
			    }
			mod = y;
			theta = ( im > 0 ) ? Math.PI/2 : -Math.PI/2;
			polar = true;
			return this;
		    }
		if( y == 0 )
		    {
			mod = x;
			theta = ( re > 0 ) ? 0 : Math.PI;
			polar = true;
			return this;
		    }
			
		if( x > y )
		    {
			y /= x;
			mod = x*Math.sqrt( 1 + y*y );
		    }
		else
		    {
			x /= y;
			mod = y*Math.sqrt( 1 + x*x );
		    }
		theta = Math.atan2( im, re );
		polar = true;
		return this;
	    }
    }
	
    /**
     *Forces the argument of this <b>Complex</b> to the principal domain.
     */
    // NOTICE: WOULD THIS BE MORE CONVENIENT AS A VOID METHOD?? 
    public Complex toPrincipal()
    {
	toPolar();
	theta %= 2*Math.PI;
	if( theta > Math.PI )
	    theta -= 2*Math.PI;
	return this;
    }
	
    /**
     *Returns a String representation of the Complex number on the form 
     *e.g. " <i>2+3i</i> ".
     */
    @Override
    public String toString()
    {
	toRect();
	return "" + re + ( ( im < 0 ) ? ( "" + im ) : ( "+" + im ) ) + "i";
    }

    /***** Class methods ***********************************************/


    /**
     *The complex conjugate of <i>z</i>.
     */
    public static Complex conj( Complex z )
    {
	Complex tmp = new Complex( z );
	if( z.rect )
	    tmp.im = -z.im;
		
	if( z.polar ) //&& z.theta != Math.PI )
	    tmp.theta = -z.theta;
	return tmp;
    }
	
    /**
     *The complex sum <i>a+b</i>.
     */
    //returns rectangular
    public static Complex add( Complex a, Complex b )
    {
	a.toRect();
	b.toRect();
	return new Complex( a.re + b.re, a.im + b.im );
    }
	
    /**
     *The complex difference <i>a-b</i>.
     */
    //returns rectangular
    public static Complex sub( Complex a, Complex b )
    {
	a.toRect();
	b.toRect();
	return new Complex( a.re - b.re, a.im - b.im );
    }
	
    /**
     *The complex product <i>a&middot;b</i>.
     */
    // returns rectangular on rect*rect, polar otherwise
    public static Complex mul( Complex a, Complex b )
    {
	if( a.polar || b.polar )
	    return mul( a, b, false );
	else
	    return new Complex( a.re*b.re - a.im*b.im, a.im*b.re + a.re*b.im );
    }
	
    /**
     *Returns the complex product <i>a&middot;b</i>.<br>
     *Forces the answer to be either in rectangular or polar coordinates.
     */
    public static Complex mul( Complex a, Complex b, boolean rectangular )
    {
	if( rectangular )
	    {
		a.toRect();
		b.toRect();
		return new Complex( a.re*b.re - a.im*b.im, a.im*b.re + a.re*b.im );
	    }
	else
	    {
		a.toPolar();
		b.toPolar();
		return new Complex( a.mod*b.mod, a.theta+b.theta, false );
	    }
    }

    /**
     *Returns the product <i>d&middot;z</i>.
     */
    // returns polar on double*polar, rectangular on double*rect
    public static Complex mul( double d, Complex z )
    {
	if( d == 0)
	    return new Complex();
		
	Complex tmp = new Complex( z );
	if( z.rect )
	    {
		tmp.re *= d;
		tmp.im *= d;
	    }
	if( z.polar )
	    {
		if( d > 0)
		    {
			tmp.mod *= d;
		    }
		else
		    {
			tmp.mod *= -d;
			tmp.theta += Math.PI;
		    }
	    }
	return tmp;
		
    }
	
    /**
     *Returns the product <i>z&middot;d</i>.
     */
    public static Complex mul(Complex z, double d)
    {
	return mul(d,z);
    }
	
    /**
     *Returns the complex division <i>a/b</i>.
     */
    // returns polar on polar/complex and rect on rect/complex
    public static Complex div(Complex a, Complex b)
    {
	if( a.polar )
	    return div( a, b, false );
	else
	    return div( a, b, true );
    }
	
	
	
    /**
     *Division of <i>a</i> with <i>b</i>.<br>
     *Forces the answer to be either in rectangular or polar coordinates.
     */
    public static Complex div( Complex a, Complex b, boolean rectangular )
    {
	if( b.equals( ZERO ) )
	    throw new ArithmeticException( "Division by zero" );
		
	if( rectangular )
	    {
		a.toRect();
		b.toRect();
			
		// avoid loss of precision in this curious way...
		if( Math.abs( b.re ) >= Math.abs( b.im ) )
		    {
				// 'polar' is false on return, so we COULD overwrite the fields
			double fact = b.im/b.re;
			double denom = b.re + fact*b.im;
				
			return new Complex( ( a.re + a.im*fact )/denom, ( a.im - a.re*fact )/denom );
		    }
		else
		    {
				// 'polar' is false on return, so we COULD overwrite the fields
			double fact = b.re/b.im;
			double denom = fact*b.re + b.im;
				
			return new Complex( ( a.re*fact + a.im )/denom, ( a.im*fact - a.re )/denom );
		    }
	    }
	else
	    {
		a.toPolar();
		b.toPolar();
		return new Complex( a.mod/b.mod, a.theta - b.theta );
	    }
    }

    /**
     *Returns the division <i>z/d</i>.
     */
    // returns polar on polar/double, rectangular on rect/double
    public static Complex div( Complex z, double d )
    {
	if( d == 0)
	    throw new ArithmeticException( "Division by zero" );
		
	Complex tmp = new Complex( z );
	if( z.rect )
	    {
		tmp.re /= d;
		tmp.im /= d;
	    }
	if( z.polar )
	    {
		if(d > 0)
		    {
			tmp.mod /= d;
			return tmp;
		    }
		else
		    {
			tmp.mod /= -d;
			tmp.theta += Math.PI;
			return tmp;
		    }
	    }
	return tmp;
    }
	
    /**
     *Returns the complex division <i>d/z</i>.
     */
    // returns polar on double*polar, rectangular on double*rect
    public static Complex div(double d, Complex z)
    {
	if( z.equals( ZERO ) )
	    throw new ArithmeticException( "Division by zero" );
		
	if( z.polar )
	    {
		return new Complex( d/z.re, -z.im, false );
	    }
	else
	    {
		double denom = z.re*z.re + z.im*z.im;
		return new Complex( d*z.re/denom, -d*z.im/denom );
	    }
    }
			
    /**
     *Returns the complex exponential of <i>z</i>.
     *
     *If <i>z</i> = <i>x</i> + i <i>y</i>, then 
     *<center>
     *exp( <i>z</i> ) = e<sup><i>x</i></sup> ( cos( <i>y</i> ) + i sin( <i>y</i> ) )
     *</center>
     */
    // returns polar
    public static Complex exp( Complex z )
    {
	// exp(x+i*y) = exp(x)*exp(i*y)  ->  r = exp(x) and theta = y
	z.toRect();
	return new Complex( Math.exp( z.re ), z.im, false );
    }
	
    /**
     *Returns the complex logarithm of <i>z</i>, with 
     *the principal branch of the logarithm function.
     *
     *<center>
     *log( <i>z</i> ) = ln( ||<i>z</i>|| ) + i arg( <i>z</i> )
     *</center>
     */
    // returns rectangular
    public static Complex log( Complex z )
    {
	// log(r*exp(i*theta)) = ln(r) + i*theta
	z.toPrincipal();
	return new Complex( Math.log( z.mod ), z.theta );
    }
	
    /**
     *Returns the complex logarithm of <i>z</i>, with 
     *the branch of the logarithm function defined by <i>D</i>.
     *
     *<center>
     *log( <i>z</i> ) = ln( ||<i>z</i>|| ) + i arg( <i>z</i> )
     *</center>
     */
    // returns rectangular
    public static Complex log(Complex z, Domain D)
    {
	return new Complex( Math.log( z.abs() ), D.arg(z) );
    }
	
    /**
     *Returns the complex sine of <i>z</i>.
     *
     *<center>
     *sin( <i>z</i> ) = 
     *  ( e<sup>i <i>z</i></sup> - e<sup>-i <i>z</i></sup> ) / 2i
     *</center>
     */
    // returns rectangular
    public static Complex sin( Complex z )
    {
	// sin(z) = (exp(iz) - exp(-iz))/2i
	//  = sin(x)*(exp(y)+exp(-y))/2 + i*cos(x)*(exp(y)-exp(-y))/2
	z.toRect();
	double expy = Math.exp( z.im );
	double exp_y = Math.exp( -z.im );
	return new Complex( Math.sin( z.re )*( expy + exp_y )/2, Math.cos( z.re )*( expy - exp_y )/2 );
    }
	
    /**
     *Returns the complex cosine of <i>z</i>.
     *
     *<center>
     *cos( <i>z</i> ) =
     *  ( e<sup>i <i>z</i></sup> + e<sup>-i <i>z</i></sup> ) / 2
     *</center>
     */
    // returns rectangular
    public static Complex cos( Complex z )
    {
	// cos(z) = (exp(iz) + exp(-iz))/2
	//  = cos(x)*(exp(-y)+exp(y))/2 + i*sin(x)*(exp(-y)-exp(y))/2
	z.toRect();
	double expy = Math.exp( z.im );
	double exp_y = Math.exp( -z.im );
	return new Complex( Math.cos( z.re )*( exp_y + expy )/2, Math.sin( z.re )*( exp_y - expy )/2 );
    }
	
    /**
     *Returns the complex square root of <i>z</i>.
     */
    // returns polar
    public static Complex sqrt( Complex z )
    {
	z.toPolar();
	return new Complex( Math.sqrt( z.mod ), z.theta/2, false );
    }
	
    /**
     *Returns <i>a</i> raised to the power of <i>b</i>, that is
     *<i>a<sup>b</sup></i>.
     *
     *<p>
     *Calculation of the power function goes through
     *<i>a<sup>b</sup></i> = exp( <i>b</i> log( <i>a</i> ) ), with the 
     *principal branch of the logarithm function.<br>
     *Notice that 0<sup><i>b</i></sup> is not defined for complex <i>b</i>.
     */
    // returns polar
    public static Complex pow( Complex a, Complex b )
    {
	b.toRect();
	if( b.im == 0 )
	    return pow( a, b.re );
		
	if( a.equals(ZERO) )
	    throw new ArithmeticException("Attempt to raise zero to complex power");
		
	a.toPrincipal();
	// a^b = exp(b*log(a)) = exp( (br + i*bi)*(ln(|a|) + i*a.arg()) )
	double lna = Math.log( a.mod );
	double arga = a.theta;
	return new Complex( Math.exp(b.re*lna-b.im*arga), b.im*lna+b.re*arga, false );
    }
	
    /**
     *Returns <i>a</i> raised to the power of <i>b</i>, with the
     *branch of the logarithm defined by <code>D</code>.
     *
     *<p>
     *Calculation of the power function goes through
     *<i>a<sup>b</sup></i> = exp( <i>b</i> log( <i>a</i> ) ). <br>
     *Notice that 0<sup><i>b</i></sup> is not defined for complex <i>b</i>.
     */
    public static Complex pow(Complex a, Complex b, Domain D)
    {
	b.toRect();
	if( b.im == 0 )
	    return pow( a, b.re );
		
	if( a.equals(ZERO) )
	    throw new ArithmeticException("Attempt to raise zero to complex power");
		
	// a^b = exp(b*log(a)) = exp( (br + i*bi)*(ln(|a|) + i*a.arg()) )
	double lna = Math.log( a.abs() );
	double arga = D.arg( a );
	return new Complex( Math.exp(b.re*lna-b.im*arga), b.im*lna+b.re*arga, false );
    }
	
    /**
     *Returns complex <i>a</i> raised to the power of skalar <i>d</i>.
     *
     *<p>
     *Calculation of the power function goes through
     *<i>a<sup>b</sup></i> = exp( <i>b</i> log( <i>a</i> ) ). <br>
     */
    public static Complex pow(Complex a, double d)
    {
	a.toPrincipal();
		
	if(d == 0)
	    return new Complex(1);
	// NOTICE if the user wishes to overwrite the result, it will be disastrous 
	// to return the 'final static ONE'
		
	if(a.equals(ZERO))
	    return new Complex();
		
	// a^d = exp(d*log(a)) = exp(d*ln(|a|))*exp(i*d*a.arg())
	//	  = |a|^d*exp(i*d*a.arg())
	return new Complex( Math.pow(a.mod,d), d*a.theta, false );
    }
	
}
