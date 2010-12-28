package com.vse.segments.parser;

class ConstValueIdentifier extends ValueIdentifier {

	public ConstValueIdentifier(Object value) {
		super(value);
	}

	public String getAsText(Class<?> c) throws BuildException {
		if (String.class.equals(c)) {
			return "\"" + value + "\"";
		}

		if (Character.class.equals(c) || Character.TYPE.equals(c)) {
			if (((String) value).length() != 1)
				throw new BuildException("Expecting Character");

			return "'" + value + "'";
		}

		return value.toString();
	}

	public Object getValue(Class<?> c) throws BuildException {
		if (value instanceof String) {
			if (Character.class.equals(c) || Character.TYPE.equals(c))
				return new Character(((String) value).charAt(0));
		}

		return super.getValue(c);
	}

}
