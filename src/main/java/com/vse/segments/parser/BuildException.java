package com.vse.segments.parser;

public class BuildException extends Exception {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	public BuildException(String message) {
		super(message);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}
}
