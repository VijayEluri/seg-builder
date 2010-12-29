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