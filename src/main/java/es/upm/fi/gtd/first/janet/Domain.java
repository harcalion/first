package es.upm.fi.gtd.first.janet;


/**
 *A domain of the complex logarithm function is a <i>simply connected</i> set 
 *in the complex plane not containing origo. In such a domain the argument or 
 *angle arg(<i>z</i>) is continous - and the complex logarithm 
 *function log(<i>z</i>) is defined and analytical.
 *<p>
 *The <code>Domain</code> class is the abstract superclass of any such domain.<br>
 *The class provides methods for determining whether a complex number is 
 *in the domain, and for determining it's argument.
 *<br>
 *<br>
 *Notice that arg(<i>z</i>) is ambigous since arg(<i>z</i>) + 
 *2<font face=symbol>p</font> is also a valid argument function.
 */
public abstract class Domain
{
	/**
	 *Should return true if <code>z</code> is part of the <code>Domain</code>, 
	 *false otherwise.
	 */
	public abstract boolean contains(Complex z);
	
	/**
	 *Should return the argument or angle of <code>z</code> in the 
	 *<code>Domain</code>.
	 *<p>
	 *The <code>Domain</code> should pick a specific branch of the argument 
	 *function to return in <code>arg(Complex z)</code>.
	 *<p>
	 *The method expects the argument <code>z</code> to be contained 
	 *in the <code>Domain</code>, and the user should expect an error or an 
	 *erroneous result, if <code>arg</code> is called with a <code>z</code> 
	 *not contained in the <code>Domain</code>.
	 */
	public abstract double arg(Complex z);
}
