package com.vse.segments.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class SegmentBuilder {

	protected final String name;
	protected final SegmentBuilder baseBuilder;
	protected final List<FieldBuilder> fields = new ArrayList<FieldBuilder>();
	protected final Map<String, FieldBuilder> fieldsByName = new HashMap<String, FieldBuilder>();
	protected final String classAttributes;

	interface FieldVisitor {
		void visit(BuildContext context, FieldBuilder field, int offset)
				throws BuildException;
	};

	public SegmentBuilder(String name, SegmentBuilder baseBuilder,
			String classAttributes) {
		this.name = name;
		this.baseBuilder = baseBuilder;
		this.classAttributes = classAttributes;
	}

	public String getName() {
		return name;
	}

	public void addField(String fieldName, FieldType fieldType, Map<String, ValueIdentifier> modifiers)
			throws ParseException {
		if (fieldsByName.containsKey(fieldName))
			throw new ParseException("Duplicate field name: " + fieldName);

		FieldBuilder field = fieldType.createBuilder(fieldName, modifiers);
		fieldsByName.put(fieldName, field);
		fields.add(field);
	}

	public void addField(String fieldName, FieldType fieldType)
			throws ParseException {
		addField(fieldName, fieldType, new HashMap<String, ValueIdentifier>());
	}

	public void buildDefinition(BuildContext context, int offset)
			throws BuildException {
		processFields(context, offset, new FieldVisitor() {
			public void visit(BuildContext c, FieldBuilder field, int offs)
					throws BuildException {
				field.buildDefinition(c, offs);
			}
		});
	}

	public void buildInitialization(BuildContext context, int offset)
			throws BuildException {
		processFields(context, offset, new FieldVisitor() {
			public void visit(BuildContext c, FieldBuilder field, int offs)
					throws BuildException {
				field.buildInitialization(c, offs);
			}
		});
	}

	public void buildConstruction(BuildContext context, int offset)
			throws BuildException {
		processFields(context, offset, new FieldVisitor() {
			public void visit(BuildContext c, FieldBuilder field, int offs)
					throws BuildException {
				field.buildConstruction(c, offs);
			}
		});
	}

	public void buildStaticConstruction(BuildContext context, int offset)
			throws BuildException {
		processFields(context, offset, new FieldVisitor() {
			public void visit(BuildContext c, FieldBuilder field, int offs)
					throws BuildException {
				field.buildStaticConstruction(c, offs);
			}
		});
	}

	public void buildMethods(BuildContext context, int offset)
			throws BuildException {
		processFields(context, offset, new FieldVisitor() {
			public void visit(BuildContext c, FieldBuilder field, int offs)
					throws BuildException {
				field.buildMethods(c, offs);
			}
		});
	}

	public void buildToString(BuildContext context, int offset)
			throws BuildException {
		if (baseBuilder != null)
			context.out.println("ss.append (\" \"+super.toString ());");

		processFields(context, offset, new FieldVisitor() {
			public void visit(BuildContext c, FieldBuilder field, int offs)
					throws BuildException {
				field.buildToString(c, offs);
			}
		});
	}

	public void buildCpp(BuildContext context, int offset)
			throws BuildException {
		processFields(context, offset, new FieldVisitor() {
			public void visit(BuildContext c, FieldBuilder field, int offs)
					throws BuildException {
				field.buildCpp(c, offs);
			}
		});
	}

	public void buildCpp(BuildContext context) throws BuildException {
		context.out.println();
		context.out.println("{");
		context.out.indent();
		context.out.println("VVector<FieldTemplate *> fields;");

		buildCpp(context, 0);

		context.out
				.println("FixedSegmentTemplate *segTemplate = new FixedSegmentTemplate (fields);");
		context.out.println("theTemplateMap.put (\"" + name
				+ "\", segTemplate);");
		context.out.unindent();
		context.out.println("}");
		context.out.println();
	}

	public void build(BuildContext context) throws BuildException {
		String className = Utils.prepareIdentifier("-" + name);

		context.out.printlnf();
		context.out.println((classAttributes == null ? "" : classAttributes
				+ " ")
				+ "public class "
				+ className
				+ (baseBuilder != null ? " extends "
						+ Utils.prepareIdentifier(baseBuilder.name) + " {"
						: " extends SegmentBase {"));

		context.out.indent();
		context.out.println();

		context.out.println();
		context.out.println("public static final int LENGTH = " + getLength()
				+ ";");
		context.out.println();

		buildDefinition(context, 0);

		context.out.println();
		context.out.println("static {");
		context.out.indent();
		buildStaticConstruction(context, 0);
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		buildMethods(context, 0);

		context.out.println();
		context.out.println("public " + className + " (char[] buffer)");
		context.out.println("{");
		context.out.indent();
		context.out.println("this (buffer, 0);");
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		context.out.println();
		context.out.println("public " + className
				+ " (char[] buffer, int offset)");
		context.out.println("{");
		context.out.indent();
		context.out.println("super (buffer, offset);");
		context.out.println();
		context.out.println("if (buffer.length-offset < LENGTH)");
		context.out.indent();
		context.out
				.println("throw new IllegalArgumentException (\"Segment buffer too small\");");
		context.out.unindent();
		context.out.println();
		buildConstruction(context, 0);
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		context.out.println();
		context.out.println("public " + className + " ()");
		context.out.println("{");
		context.out.indent();
		context.out.println("this (LENGTH);");
		context.out.println("reset ();");
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		context.out.println();
		context.out.println("protected " + className + " (int length)");
		context.out.println("{");
		context.out.indent();
		context.out.println("super (length);");
		context.out.println();
		buildConstruction(context, 0);
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		context.out.println();
		context.out.println("protected void reset ()");
		context.out.println("{");
		context.out.indent();
		buildInitialization(context, 0);
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		context.out.println();
		context.out.println("public String toString ()");
		context.out.println("{");
		context.out.indent();
		context.out.println("StringBuffer ss = new StringBuffer ();");
		context.out.println("ss.append (\"" + className + " {\");");
		buildToString(context, 0);
		context.out.println("ss.append (\"}\");");
		context.out.println("return ss.toString ();");
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		context.out.println();
		context.out.println("public int getLength ()");
		context.out.println("{");
		context.out.indent();
		context.out.println("return LENGTH;");
		context.out.unindent();
		context.out.println("}");
		context.out.println();

		context.out.unindent();
		context.out.println("}");
		context.out.println();
	}

	protected void processFields(BuildContext context, int offset,
			FieldVisitor visitor) throws BuildException {
		if (baseBuilder != null)
			offset += baseBuilder.getLength();

		Iterator<FieldBuilder> fieldList = fields.iterator();
		while (fieldList.hasNext()) {
			FieldBuilder field = fieldList.next();
			visitor.visit(context, field, offset);
			offset += field.getLength();
		}
	}

	public int getLength() throws BuildException {
		int length = 0;
		if (baseBuilder != null)
			length += baseBuilder.getLength();

		Iterator<FieldBuilder> fieldList = fields.iterator();
		while (fieldList.hasNext()) {
			FieldBuilder field = fieldList.next();
			length += field.getLength();
		}

		return length;
	}

}
