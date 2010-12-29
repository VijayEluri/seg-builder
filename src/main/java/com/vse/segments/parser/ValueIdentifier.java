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
