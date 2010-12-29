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

class UnionSubSegmentType extends SubSegmentType {

	public UnionSubSegmentType(SegmentBuilder segmentDef) {
		super(segmentDef);
	}

	public FieldBuilder createBuilder(String name, Map<String, ValueIdentifier> modifiers)
			throws ParseException {
		char padChar = '_';

		Iterator<String> modifierList = modifiers.keySet().iterator();
		while (modifierList.hasNext()) {
			String modifierName = modifierList.next();
			ValueIdentifier value = modifiers.get(modifierName);

			try {

				if (modifierName.equals("pad-char")) {
					padChar = ((Character) value.getValue(Character.class))
							.charValue();
					continue;
				}

				throw new ParseException("Unknown property: " + modifierName);

			} catch (BuildException exc) {
				throw new ParseException(exc.toString());
			}
		}

		return new UnionSubFieldBuilder(name, padChar);
	}

	class UnionSubFieldBuilder extends SubFieldBuilder {
		char padChar;

		UnionSubFieldBuilder(String name, char padChar) throws ParseException {
			super(name, 1);
			this.padChar = padChar;
		}

		public void buildDefinition(BuildContext context, int offset)
				throws BuildException {
			segmentDef.buildDefinition(context, offset);
		}

		public void buildInitialization(BuildContext context, int offset)
				throws BuildException {
			UnionSegmentBuilder union = (UnionSegmentBuilder) segmentDef;
			char unionPadChar = union.getPadChar();
			union.setPadChar(padChar);

			union.buildInitialization(context, offset);

			union.setPadChar(unionPadChar);
		}

		public void buildConstruction(BuildContext context, int offset)
				throws BuildException {
			segmentDef.buildConstruction(context, offset);
		}

		public void buildStaticConstruction(BuildContext context, int offset)
				throws BuildException {
			segmentDef.buildStaticConstruction(context, offset);
		}

		public void buildToString(BuildContext context, int offset)
				throws BuildException {
		}

		public void buildMethods(BuildContext context, int offset)
				throws BuildException {
			UnionSegmentBuilder union = (UnionSegmentBuilder) segmentDef;
			char unionPadChar = union.getPadChar();
			union.setPadChar(padChar);

			segmentDef.buildMethods(context, offset);

			union.setPadChar(unionPadChar);
		}

	}

}
