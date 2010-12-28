package com.vse.segments.parser;

class ParseContext {
	private BuildRegistry registry;

	public ParseContext(BuildRegistry reg) {
		registry = reg;
	}

	public void setPackageName(String name) {
		registry.setPackageName(name);
	}

	public void registerType(String typeName, String className)
			throws ParseException {
		registry.registerType(typeName, className);
	}

	public void registerType(String typeName, FieldType type)
			throws ParseException {
		registry.registerType(typeName, type);
	}

	public FieldType getType(String typeName) throws ParseException {
		return registry.getType(typeName);
	}

	public void registerSegment(SegmentBuilder s) throws ParseException {
		registry.registerSegment(s);
	}

	public SegmentBuilder getSegment(String segmentName) throws ParseException {
		return registry.getSegment(segmentName);
	}

	public ValueIdentifier resolveIdentifierFor(String currentType, String modifierName, String modifierValue)
			throws ParseException {
		return registry.resolveIdentifierFor(currentType, modifierName, modifierValue);
	}

}
