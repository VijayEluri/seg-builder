/*
 * This file is part of Segment-Builder.
 *
 * Segment-Builder is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Segment-Builder is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Segment-Builder. If not, see <http://www.gnu.org/licenses/>.
 */

package com.vse.segments.types;

import com.vse.segments.SegmentField;

public abstract class PaddedField implements SegmentField {
	public enum Alignment {
		LEFT, RIGHT
	};

	private int length;
	private Alignment alignment = Alignment.LEFT;
	private char padChar = ' ';

	public void initialize(char[] buffer, int start) {
		for (int i = length - 1; i >= 0; i--)
			buffer[start++] = padChar;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setAlignment(Alignment align) {
		alignment = align;
	}

	public void setPadChar(char c) {
		padChar = c;
	}

	public char getPadChar() {
		return padChar;
	}

	protected String getString(char[] buffer, int start, boolean trim) {
		return getString(buffer, start, length, trim);
	}

	protected String getString(char[] buffer, int start, int length,
			boolean trim) {
		int l = length;
		if (trim) {
			if (alignment == Alignment.RIGHT)
				while (l > 0 && buffer[start] == padChar) {
					start++;
					l--;
				}
			else
				while (l > 0 && buffer[start + l - 1] == padChar) {
					l--;
				}
		}

		return new String(buffer, start, l);
	}

	protected void setString(char[] buffer, int start, String value)
			throws FieldException {
		setString(buffer, start, length, value);
	}

	protected void setString(char[] buffer, int start, int length, String value)
			throws FieldException {
		if (value == null)
			value = "";
		int valueLength = value.length();
		if (valueLength > length)
			throw new FieldException("Value image too long");

		if (alignment == Alignment.RIGHT) {
			int padLength = length - valueLength;
			for (int i = 0; i < padLength; i++)
				buffer[start + i] = padChar;

			value.getChars(0, valueLength, buffer, start + padLength);
		} else {
			int padLength = length - valueLength;
			for (int i = 0; i < padLength; i++)
				buffer[start + valueLength + i] = padChar;

			value.getChars(0, valueLength, buffer, start);
		}
	}
}
