package es.upm.fi.gtd.first.janet;

import java.io.Serializable;

/**
 * The interface Function defines the methods that
 * must be implemented for a class to represent
 * a function of one real argument.
 * The function value must be returned by the method
 * {@link #value}.
 * <br><b>Example of use</b>:<br>
 * We assume that a file name datafile exists, that contains
 * two columns of real data in the {@link DataSet} format.
 * <pre>
 * Function f = new LinearInterpolation( new DataSet( "datafile" ) );
 * double x = 1.2;
 * double y = f.value( x );
 * </pre>
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @see DataSet
 * @see ConstantInterpolation
 * @see LinearInterpolation
 * @see CBSpline
 */
public interface Function extends Serializable {
    /**
     * The method should return the function value.
     * There is no specified behaviour if the independent value
     * is outside of its range.
     * @param x is the independent variable
     */
    public double value( double x );

  /**
     * The method should return the minimum value of the independent variable.
     */
    public double minX();

    /**
     * The method should return the maximum value of the independent variable.
     */
    public double maxX();

    /**
     * The method should return an estimate for the minimum value of the dependent variable
     * for the independent variable in the range from {@link #minX()} to {@link #maxX()}.
     */
    public double minFX();

    /**
     * The method should return an estimate for the maximum value of the dependent variable
     * for the independent variable in the range from {@link #minX()} to {@link #maxX()}.
     */
    public double maxFX();
} // Function
