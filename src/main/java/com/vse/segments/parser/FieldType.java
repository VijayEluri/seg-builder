package com.vse.segments.parser;

import java.util.Map;

abstract class FieldType {

	public abstract FieldBuilder createBuilder(String name, Map<String, ValueIdentifier> modifiers) throws ParseException;

	public abstract ValueIdentifier resolveIdentifier(String name);

}
