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
