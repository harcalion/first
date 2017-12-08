package es.upm.fi.gtd.first.janet;


/**
 * The interface C1Function defines the methods that
 * must be implemented for a class to represent
 * a differentiable function of one real argument.
 * The function value, first derivative, and second derivative
 * must be returned by the methods
 * {@link #value} and
 * {@link #firstDeriv}, respectively.
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 */
public interface C1Function extends Function {
    /**
     * The method should return the value of the first derivative.
     * @param x is the independent variable
     */
    public double firstDeriv( double x );


} // C1Function
