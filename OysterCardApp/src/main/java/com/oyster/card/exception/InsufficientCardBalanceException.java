package com.oyster.card.exception;

/**
 * 
 * @author Shahnawaz
 *
 */
public class InsufficientCardBalanceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3696374343487290819L;

	public InsufficientCardBalanceException(String message) {
		super(message);
	}

}
