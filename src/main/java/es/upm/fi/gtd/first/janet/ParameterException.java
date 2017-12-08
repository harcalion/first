package es.upm.fi.gtd.first.janet;
/**
 * ParameterException class represents exceptions having to do
 * with parameter settings being wrong or inconsistent.
 * This exception should not be thrown, 
 * rather a concrete descriptive subclass exception should be used.
 * @author Carsten Knudsen
 * @version 1.0
 */
public class ParameterException extends JanetException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParameterException( String s ) { super(s); }
} // ParameterException


