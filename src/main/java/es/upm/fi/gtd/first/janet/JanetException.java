package es.upm.fi.gtd.first.janet;
/**
 * JanetException class is the superclass of all exceptions in
 * the Janet package.
 * This exception should not be thrown, 
 * rather a concrete descriptive subclass exception should be used.
 * @author Carsten Knudsen
 * @version 1.0
 */
public class JanetException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JanetException( String s ) { super(s); }
} // JanetException


