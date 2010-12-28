package com.vse.segments.parser;

import java.util.Iterator;
import java.util.Map;

class SubSegmentType extends FieldType {
	protected final SegmentBuilder segmentDef;

	public SubSegmentType(SegmentBuilder segmentDef) {
		this.segmentDef = segmentDef;
	}

	public FieldBuilder createBuilder(String name,
			Map<String, ValueIdentifier> modifiers) throws ParseException {
		int repeats = 1;

		Iterator<String> modifierList = modifiers.keySet().iterator();
		while (modifierList.hasNext()) {
			String modifierName = modifierList.next();
			ValueIdentifier value = modifiers.get(modifierName);

			try {

				if (modifierName.equals("length")) {
					repeats = ((Integer) value.getValue(Integer.class))
							.intValue();
					continue;
				}

				throw new ParseException("Unknown property: " + modifierName);

			} catch (BuildException exc) {
				throw new ParseException(exc.toString());
			}
		}

		return new SubFieldBuilder(name, repeats);
	}

	public ValueIdentifier resolveIdentifier(String name, String value) {
		return null;
	}

	class SubFieldBuilder extends FieldBuilder {
		private int repeats = 1;

		SubFieldBuilder(String name, int repeats) throws ParseException {
			super(name);
			this.repeats = repeats;
		}

		public int getLength() throws BuildException {
			return segmentDef.getLength() * repeats;
		}

		public void buildCpp(BuildContext context, int offset)
				throws BuildException {
			if (repeats != 1)
				throw new BuildException(
						"Subsegment arrays are nor supported for CPP target");

			segmentDef.buildCpp(context, offset);

			context.out.println("fields.add (new FixedFieldTemplate<StringFieldPacker>"
							+ " (\""
							+ this.name
							+ "\", "
							+ offset
							+ ", "
							+ getLength() + "));");
		}

		public void buildDefinition(BuildContext context, int offset)
				throws BuildException {
			String name = Utils.prepareIdentifier(this.name);
			String typeName = Utils.prepareIdentifier("-"
					+ segmentDef.getName());

			if (repeats == 1)
				context.out.println("public final " + typeName + " " + name
						+ ";");
			else
				context.out.println("public final " + typeName + " " + name
						+ "[];");
		}

		public void buildInitialization(BuildContext context, int offset)
				throws BuildException {
			String name = Utils.prepareIdentifier(this.name);

			if (repeats == 1) {
				context.out.println(name + ".reset ();");
			} else {
				context.out.println();
				context.out.println("for (int i=0; i<" + repeats + "; i++)");
				context.out.indent();
				context.out.println(name + "[i].reset ();");
				context.out.unindent();
				context.out.println();
			}
		}

		public void buildConstruction(BuildContext context, int offset)
				throws BuildException {
			String name = Utils.prepareIdentifier(this.name);
			String typeName = Utils.prepareIdentifier("-"
					+ segmentDef.getName());
			String offsetS = offset == 0 ? "offset" : ("offset+" + offset);

			if (repeats == 1) {
				context.out.println(name + " = new " + typeName + " (buffer, "
						+ offsetS + ");");
			} else {
				context.out.println();
				context.out.println(name + " = new " + typeName + "[" + repeats
						+ "];");
				context.out.println("for (int i=0; i<" + repeats + "; i++)");
				context.out.indent();
				context.out.println(name + "[i] = new " + typeName
						+ " (buffer, " + offsetS + "+" + segmentDef.getLength()
						+ "*i);");
				context.out.unindent();
				context.out.println();
			}
		}

		public void buildStaticConstruction(BuildContext context, int offset)
				throws BuildException {
		}

		public void buildToString(BuildContext context, int offset)
				throws BuildException {
			String name = Utils.prepareIdentifier(this.name);

			if (repeats == 1) {
				context.out.println("ss.append (\" \"+" + name
						+ ".toString());");
			} else {
				context.out.println();
				context.out.println("for (int i=0; i<" + repeats + "; i++)");
				context.out.indent();
				context.out.println("ss.append (\" \"+" + name
						+ "[i].toString());");
				context.out.unindent();
				context.out.println();
			}
		}

		public void buildMethods(BuildContext context, int offset)
				throws BuildException {
			if (repeats != 1)
				return;

			String name = Utils.prepareIdentifier(this.name);
			String typeName = Utils.prepareIdentifier("-"
					+ segmentDef.getName());
			String setName = Utils.prepareIdentifier("set-" + name);
			String offsetS = offset == 0 ? "offset" : ("offset+" + offset);

			context.out.println();
			context.out.println("public void " + setName + " (" + typeName + " value)");
			context.out.println("{");
			context.out.indent();
			context.out.println("System.arraycopy (value.getBuffer(), 0, buffer, "
							+ offsetS + ", " + segmentDef.getLength() + ");");
			context.out.unindent();
			context.out.println("}");
			context.out.println();
		}

	}

}
