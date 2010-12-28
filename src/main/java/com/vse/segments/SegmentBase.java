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
