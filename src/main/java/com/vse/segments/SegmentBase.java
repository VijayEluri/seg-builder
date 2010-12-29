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

public abstract class SegmentBase {
	protected final char[] buffer;
	protected final int offset;

	public abstract int getLength();

	public SegmentBase(char[] buffer) {
		this(buffer, 0);
	}

	public SegmentBase(char[] buffer, int offset) {
		this.buffer = buffer;
		this.offset = offset;
	}

	protected SegmentBase(int length) {
		offset = 0;
		buffer = new char[length];
		for (int i = 0; i < length; i++)
			buffer[i] = ' ';
	}

	public char[] getBuffer() {
		return buffer;
	}

	public int getOffset() {
		return offset;
	}

	protected void resetBufferSection(int start, int length, char padChar) {
		for (int i = 0; i < length; i++)
			buffer[start + i] = padChar;
	}

}
