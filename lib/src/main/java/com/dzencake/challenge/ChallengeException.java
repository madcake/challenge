package com.dzencake.challenge;

/**
 * Task exception.
 */
public class ChallengeException extends Exception {
    public final Err err;

	/**
	 *
	 * @param err error description
	 * @param ex the cause of this exception
	 */
	public ChallengeException(Err err, Exception ex) {
		super(ex);
		this.err = err;
	}

	/**
	 *
	 * @param code error code
	 * @param message error message
	 */
	public ChallengeException(int code, String message) {
		super();
		this.err = new Err(code, message);
	}

	/**
	 *
	 * @param code error code
	 * @param message error message
	 * @param ex the cause of this exception.
	 */
	public ChallengeException(int code, String message, Exception ex) {
		super(ex);
		this.err = new Err(code, message);
	}
}
