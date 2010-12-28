package com.vse.segments.parser;

abstract class ValueIdentifier {
	protected Object value;

	public ValueIdentifier(Object value) {
		this.value = value;
	}

	public Class<?> getValueClass() {
		return value.getClass();
	}

	public String getAsText(Class<?> expectedType) throws BuildException {
		return value.toString();
	}

	public Object getValue(Class<?> expectedType) throws BuildException {
		if (expectedType.isAssignableFrom(value.getClass())
				|| Utils.unwrapsToPrimitiveType(value.getClass(), expectedType))
			return value;

		throw new BuildException("Invalid property type. Expected: "
				+ expectedType);
	}

}
