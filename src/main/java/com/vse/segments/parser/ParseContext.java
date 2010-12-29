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
