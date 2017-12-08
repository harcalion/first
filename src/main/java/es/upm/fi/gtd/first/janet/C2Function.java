package es.upm.fi.gtd.first.janet;


/**
 * The interface C2Function defines the methods that
 * must be implemented for a class to represent
 * a twice differentiable function of one real argument.
 * The function value, first derivative, and second derivative
 * must be returned by the methods
 * {@link #value},
 * {@link #firstDeriv}, and
 * {@link #secondDeriv},
 * respectively.
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 */
public interface C2Function extends C1Function {

    /**
     * The method should return the value of the second derivative.
     * @param x is the independent variable
     */
    public double secondDeriv( double x );
} // C2Function
