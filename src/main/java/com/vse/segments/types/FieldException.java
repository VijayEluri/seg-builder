package com.vse.segments.types;

public class FieldException extends Exception {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	public FieldException(String message) {
		super(message);
	}

	public FieldException(Throwable cause) {
		super(cause);
	}

	public FieldException(String message, Throwable cause) {
		super(message, cause);
	}
}
