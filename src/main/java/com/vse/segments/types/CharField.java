package com.vse.segments.types;

public class CharField extends PaddedField {
	private String defaultValue = "";

	public CharField() {
		setAlignment(Alignment.LEFT);
		setPadChar(' ');
	}

	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getAcessorModifierName() {
		return "";
	}

	public String getFieldPackerName() {
		return "StringFieldPacker";
	}

	public Class<?> getType() {
		return String.class;
	}

	public String get(char[] buffer, int start) {
		return getString(buffer, start, true);
	}

	public void set(char[] buffer, int start, String value)
			throws FieldException {
		setString(buffer, start, value);
	}

	public void initialize(char[] buffer, int start) {
		try {
			set(buffer, start, defaultValue);
		} catch (FieldException e) {
		}
	}

}
