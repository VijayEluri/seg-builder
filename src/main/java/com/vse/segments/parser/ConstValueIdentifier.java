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
