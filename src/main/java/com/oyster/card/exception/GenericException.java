package com.oyster.card.exception;

/**
 * @author Shahnawaz
 */
public class GenericException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1896543370199778864L;
	
	/**
	 * @param message message
	 */
	public GenericException(String message) {
		super(message);
	}

}
