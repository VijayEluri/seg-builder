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


class UnionSegmentBuilder extends SegmentBuilder {
	private String unionName;
	private char padChar = '_'; // Default union class pad char

	public UnionSegmentBuilder(String name, String unionName) {
		super(name, null, null);
		this.unionName = unionName;
	}

	public void setPadChar(char padChar) {
		this.padChar = padChar;
	}

	public char getPadChar() {
		return padChar;
	}

	protected void processFields(BuildContext context, int offset,
			FieldVisitor visitor) throws BuildException {
		for (FieldBuilder field : fields) {
			visitor.visit(context, field, offset);
		}
	}

	public int getLength() throws BuildException {
		int length = 0;

		for (FieldBuilder field : fields) {
			length = Math.max(length, field.getLength());
		}

		return length;
	}

	public void buildMethods(BuildContext context, int offset)
			throws BuildException {
		super.buildMethods(context, offset);

		String name = Utils.prepareIdentifier(unionName);
		String getName = Utils.prepareIdentifier("get-" + name + "-mode");
		String setName = Utils.prepareIdentifier("set-" + name + "-mode");

		context.out.println();
		context.out.println("public void " + setName
				+ " (int mode) throws FieldException");
		context.out.println("{");
		context.out.indent();
		buildInitialization(context, offset);
		context.out.println();
		context.out.println("switch (mode) {");
		context.out.indent();

		for (FieldBuilder field : fields) {
			String caseName = field.getName().toUpperCase().replace('-', '_');

			context.out.println("case " + caseName + ":");
			context.out.println("{");
			context.out.indent();
			field.buildInitialization(context, offset);
			context.out.println("break;");
			context.out.unindent();
			context.out.println("}");
			context.out.println();
		}

		context.out.println("default:");
		context.out.indent();
		context.out
				.println("throw new FieldException (\"Invalid union mode\");");
		context.out.unindent();
		context.out.unindent();
		context.out.println("}");

		context.out.unindent();
		context.out.println("}");
		context.out.println();
	}

	public void buildDefinition(BuildContext context, int offset)
			throws BuildException {
		super.buildDefinition(context, offset);
		context.out.println();

		int id = 1;
		for (FieldBuilder field : fields) {
			String caseName = field.getName().toUpperCase().replace('-', '_');
			context.out.println("public static final int " + caseName + " = "
					+ id + ";");
			id++;
		}

		context.out.println();
	}

	public void buildInitialization(BuildContext context, int offset)
			throws BuildException {
		String offsetS = offset == 0 ? "offset" : ("offset+" + offset);
		context.out.println("resetBufferSection (" + offsetS + ", LENGTH, '"
				+ padChar + "');");
	}

	public void buildToString(BuildContext context, int offset)
			throws BuildException {
		context.out.println("ss.append (\"UNION\");");
	}

}
