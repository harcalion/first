package es.upm.fi.gtd.first.janet;

/**
 * IllegalValueException is thrown when a method tries to assign
 * an illegal value to a parameter.
 * For example if a negative value is assigned to a non-negative parameter.
 * @author Carsten Knudsen
 * @version 1.0
 */
public class IllegalValueException extends ParameterException { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalValueException( String s ) { super( s ); }
} // IllegalValueException
