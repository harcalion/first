package es.upm.fi.gtd.first.janet;

/**
 * IllegalDimensionException is thrown when a method is passed an array of illegal
 * dimensions.
 * @author Carsten Knudsen
 * @version 1.0
 */
public class IllegalDimensionException extends ParameterException { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalDimensionException( String s ) { super( s ); }
} // IllegalDimensionException
