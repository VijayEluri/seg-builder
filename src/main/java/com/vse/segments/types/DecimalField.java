package com.vse.segments.types;

public class DecimalField extends PaddedField {
	private long defaultValue;
	private boolean signed;

	public DecimalField() {
		setAlignment(Alignment.RIGHT);
		setPadChar('0');
	}

	private boolean longRequired() {
		int length = getLength();
		if (signed) {
			length--;
		}

		return Integer.toString(Integer.MAX_VALUE).length() - 1 < length;
	}

	public String getAcessorModifierName() {
		return longRequired() ? "Long" : "Int";
	}

	public String getFieldPackerName() {
		return signed ? "SignedIntFieldPacker" : "IntFieldPacker";
	}

	public Class<?> getType() {
		return longRequired() ? Long.TYPE : Integer.TYPE;
	}

	public void setLength(int length) {
		if (signed)
			length++;
		super.setLength(length);
	}

	public void setSigned(boolean signed) {
		if (this.signed && !signed)
			setLength(getLength() - 1);
		if (!this.signed && signed)
			setLength(getLength() + 1);

		this.signed = signed;
	}

	public void setDefault(long defaultValue) {
		this.defaultValue = defaultValue;
	}

	public long getLong(char[] buffer, int start) throws FieldException {
		try {
			String image = signed ? getString(buffer, start + 1,
					getLength() - 1, true) : getString(buffer, start, true);
			if (image.length() == 0)
				return 0;

			long value = Long.parseLong(image);

			if (signed)
				switch (buffer[start]) {
				case '+':
					break;

				case '-':
					value = -value;
					break;

				default:
					throw new FieldException("Expecting +/-");
				}

			return value;

		} catch (NumberFormatException exc) {
			throw new FieldException(exc.toString() + "'"
					+ getString(buffer, start, false) + "'");
		}
	}

	public void setLong(char[] buffer, int start, long value)
			throws FieldException {
		setString(buffer, start, Long.toString(value));

		if (signed) {
			if (buffer[start] != getPadChar())
				throw new FieldException("Value image too long");

			buffer[start] = value > 0 ? '+' : '-';
		}
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
}