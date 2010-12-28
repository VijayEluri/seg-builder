package com.vse.segments.parser;

import java.util.Iterator;
import java.util.Map;

class NestedSubSegmentType extends SubSegmentType {

	public NestedSubSegmentType(SegmentBuilder segmentDef) {
		super(segmentDef);
	}

	public FieldBuilder createBuilder(String name, Map<String, ValueIdentifier> modifiers)
			throws ParseException {
		Iterator<String> modifierList = modifiers.keySet().iterator();
		if (modifierList.hasNext())
			throw new ParseException("Unknown property: " + modifierList.next());

		return new NestedSubFieldBuilder(name);
	}

	class NestedSubFieldBuilder extends SubFieldBuilder {

		NestedSubFieldBuilder(String name) throws ParseException {
			super(name, 1);
		}

		public void buildDefinition(BuildContext context, int offset)
				throws BuildException {
			context.out.println();
			segmentDef.build(context);
			context.out.println();

			super.buildDefinition(context, offset);
		}
	}

}
