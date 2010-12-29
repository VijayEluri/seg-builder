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

package com.vse.segments;

public interface SegmentField {

	/**
	 * @return <TYPE>
	 */
	public Class<?> getType();

	/**
	 * @return get/set modifier to allow several getters/setters
	 */
	public String getAcessorModifierName();

	/**
	 * @return C++ field packer name
	 */
	public String getFieldPackerName();

	public int getLength();

	public void initialize(char[] buffer, int start);

	// Also get and set methods are required
	// public <TYPE> get<MODIFIER> (char[] buffer, int start) throws FieldException;
	// public void set<MODIFIER> (char[] buffer, int start, <TYPE> value) throws FieldException;
}
