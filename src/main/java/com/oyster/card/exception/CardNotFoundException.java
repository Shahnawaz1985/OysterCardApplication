package com.oyster.card.exception;

/**
 * @author Shahnawaz
 */
public class CardNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8385919572723901772L;

	/**
	 * @param message message
	 */
	public CardNotFoundException(String message) {
		super(message);
	}

}
