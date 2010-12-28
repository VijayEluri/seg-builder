package com.vse.segments.types;

import com.vse.segments.SegmentField;

public class PackedDecimalField implements SegmentField {
	private boolean signed;
	private int length;
	private long defaultValue;

	public PackedDecimalField() {
	}

	private boolean longRequired() {
		int length = getLength() * 2 - 1;
		return Integer.toString(Integer.MAX_VALUE).length() - 1 < length;
	}

	public String getAcessorModifierName() {
		return longRequired() ? "Long" : "Int";
	}

	public String getFieldPackerName() {
		return "IntFieldPacker";
	}

	public Class<?> getType() {
		return longRequired() ? Long.TYPE : Integer.TYPE;
	}

	public void setDefault(long defaultValue) {
		this.defaultValue = defaultValue;
	}

	public long getLong(char[] buffer, int start) throws FieldException {
		long value = 0;
		int v = 0;

		for (int i = 0; i < length; i++) {
			v = (short) buffer[i + start];

			value = value * 10 + (v >> 4);
			v &= 0x0F;

			if (i + 1 < length)
				value = value * 10 + v;
		}

		switch (v) {
		case 0xC:
			if (!signed)
				throw new FieldException("Expecting unsigned number");
			break;

		case 0xF:
			if (signed)
				throw new FieldException("Expecting signed number");
			break;

		case 0xD:
			if (!signed)
				throw new FieldException("Expecting unsigned number");
			value = -value;
			break;

		default:
			throw new FieldException("Invalid sign signature");
		}

		return value;
	}

	public void setLong(char[] buffer, int start, long value)
			throws FieldException {
		int v = signed ? (value < 0 ? 0xD : 0xC) : 0xF;
		value = Math.abs(value);

		for (int i = length - 1; i >= 0; i--) {
			if (v == 0) {
				v = (int) (value % 10);
				value /= 10;
			}

			v |= (value % 10) << 4;
			value /= 10;

			buffer[i + start] = (char) v;
			v = 0;
		}

		if (value != 0)
			throw new FieldException("Value image too long");
	}

	public int getInt(char[] buffer, int start) throws FieldException {
		return (int) getLong(buffer, start);
	}

	public void setInt(char[] buffer, int start, int value)
			throws FieldException {
		setLong(buffer, start, value);
	}

	public void initialize(char[] buffer, int start) {
		try {
			setLong(buffer, start, defaultValue);
		} catch (FieldException e) {
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

}
