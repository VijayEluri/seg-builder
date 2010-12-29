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
