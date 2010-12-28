package com.vse.segments.types;

public class FloatingDecimalField extends DecimalField {
	private double defaultValue;
	private double exp = 1;

	public FloatingDecimalField() {
	}

	public String getAcessorModifierName() {
		return "";
	}

	public Class<?> getType() {
		return Double.TYPE;
	}

	public String getFieldPackerName() {
		return "IntFieldPacker";
	}

	public void setDefault(double defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDefault(long defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDecimalPositions(int positions) {
		exp = Math.pow(10, -positions);
	}

	public double get(char[] buffer, int start) throws FieldException {
		return getLong(buffer, start) * exp;
	}

	public void set(char[] buffer, int start, double value)
			throws FieldException {
		setLong(buffer, start, (long) Math.round(value / exp));
	}

	public void initialize(char[] buffer, int start) {
		try {
			set(buffer, start, defaultValue);
		} catch (FieldException e) {
		}
	}

}