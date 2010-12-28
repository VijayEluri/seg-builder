package com.vse.segments.parser;

class IdValueIdentifier extends ValueIdentifier {
	protected String name;

	public IdValueIdentifier(String name, Object value) {
		super(value);
		this.name = name;
	}

	public String getAsText(Class<?> expectedType) {
		return name;
	}

}
