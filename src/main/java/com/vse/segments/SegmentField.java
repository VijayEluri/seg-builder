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
