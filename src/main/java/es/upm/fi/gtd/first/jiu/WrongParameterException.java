/*
 * WrongParameterException
 * 
 * Copyright (c) 2001, 2002, 2003, 2004, 2005 Marco Schmidt
 * All rights reserved.
 */

package es.upm.fi.gtd.first.jiu;



/**
 * Exception class to indicate that an operation's parameter is of the wrong
 * type, does not fall into a valid interval or a similar mistake.
 *
 * @author Marco Schmidt
 * @since 0.6.0
 */
public class WrongParameterException extends OperationFailedException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongParameterException(String message)
	{
		super(message);
	}
}
