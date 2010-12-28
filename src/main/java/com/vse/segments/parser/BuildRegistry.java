package com.vse.segments.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vse.segments.SegmentField;

class BuildRegistry {
	private String packageName = null;
	private Map<String, FieldType> types = new HashMap<String, FieldType>();
	private Map<String, SegmentBuilder> segments = new HashMap<String, SegmentBuilder>();

	public void setPackageName(String name) {
		packageName = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void registerType(String typeName, String className)
			throws ParseException {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends SegmentField> c = (Class<? extends SegmentField>) Class.forName(className); 
			registerType(typeName, new SimpleFieldType(c));
		} catch (ClassNotFoundException exc) {
			throw new ParseException("Type implementation class not found");
		}
	}

	public void registerType(String typeName, FieldType type)
			throws ParseException {
		if (types.containsKey(typeName))
			throw new ParseException("Duplicate type name: " + typeName);

		types.put(typeName, type);
	}

	public FieldType getType(String typeName) throws ParseException {
		if (!types.containsKey(typeName))
			throw new ParseException("Undefined type: " + typeName);

		return types.get(typeName);
	}

	public void registerSegment(SegmentBuilder s) throws ParseException {
		if (segments.containsKey(s.getName()))
			throw new ParseException("Duplicate segment definition: "
					+ s.getName());

		registerType(s.getName(), new SubSegmentType(s));
		segments.put(s.getName(), s);
	}

	public SegmentBuilder getSegment(String segmentName) throws ParseException {
		if (!segments.containsKey(segmentName))
			throw new ParseException("Undefined type: " + segmentName);

		return segments.get(segmentName);
	}

	public ValueIdentifier resolveIdentifierFor(String currentType, String identifierName, String identifierValue)
			throws ParseException {
		if ("true".equalsIgnoreCase(identifierValue))
			return new ConstValueIdentifier(Boolean.TRUE);

		if ("false".equalsIgnoreCase(identifierValue))
			return new ConstValueIdentifier(Boolean.FALSE);

		return getType(currentType).resolveIdentifier(identifierName, identifierValue);
	}

	public Iterator<String> getSegmentNames() {
		return segments.keySet().iterator();
	}

}
