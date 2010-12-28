package com.vse.segments.parser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import com.vse.segments.SegmentField;

class SimpleFieldType extends FieldType {
	// changed from 'private final Class fieldClass;'
	// since javac under Solaris refuses to understand initialization in
	// constructor
	private Class<? extends SegmentField> fieldClass;

	public SimpleFieldType(Class<? extends SegmentField> c) {
		fieldClass = c;
	}

	private SegmentField createProcessor(Map<String, ValueIdentifier> modifiers)
			throws BuildException {
		try {
			SegmentField processor = fieldClass.newInstance();

			Iterator<String> modifierList = modifiers.keySet().iterator();
			while (modifierList.hasNext()) {
				String modifierName = modifierList.next();
				ValueIdentifier value = modifiers.get(modifierName);

				Method m = Utils.findSetPropertyMethodFor(fieldClass,
						Utils.prepareIdentifier(modifierName),
						value.getValueClass());

				Class<?> c = m.getParameterTypes()[0];
				try {
					m.invoke(processor, new Object[] { value.getValue(c) });
				} catch (InvocationTargetException e) {
				}
			}

			return processor;

		} catch (InstantiationException e) {
			throw new BuildException("Could not load field type: " + e);
		} catch (IllegalAccessException e) {
			throw new BuildException("Could not load field type: " + e);
		}
	}

	public FieldBuilder createBuilder(String name, Map<String, ValueIdentifier> modifiers) {
		return new SimpleFieldBuilder(name, modifiers);
	}

	public ValueIdentifier resolveIdentifier(String name) {
		Class<?> type = fieldClass;
		if (type != null)
			try {
				Field field = type.getField(name);
				return new IdValueIdentifier(type.getName() + "." + name,
						field.get(null));
			} catch (Exception e) {
			}

		return null;
	}

	class SimpleFieldBuilder extends FieldBuilder {
		private SegmentField processor;
		private Map<String, ValueIdentifier> modifiers;

		SimpleFieldBuilder(String name, Map<String, ValueIdentifier> modifiers) {
			super(name);
			this.modifiers = modifiers;
		}

		private SegmentField getProcessor() throws BuildException {
			if (processor == null)
				processor = createProcessor(modifiers);

			return processor;
		}

		public int getLength() throws BuildException {
			return getProcessor().getLength();
		}

		public void buildCpp(BuildContext context, int offset)
				throws BuildException {
			String fieldCType = getProcessor().getFieldPackerName();
			context.out.println("fields.add (new FixedFieldTemplate<"
					+ fieldCType + ">" + " (\"" + this.name + "\", " + offset
					+ ", " + getLength() + "));");
		}

		public void buildDefinition(BuildContext context, int offset)
				throws BuildException {
			String name = Utils.prepareIdentifier(this.name);
			String typeName = fieldClass.getName();

			context.out.println("private static final " + typeName + " " + name
					+ " = new " + typeName + " ();");
		}

		public void buildInitialization(BuildContext context, int offset)
				throws BuildException {
			String name = Utils.prepareIdentifier(this.name);
			String offsetS = offset == 0 ? "offset" : ("offset+" + offset);

			context.out
					.println(name + ".initialize (buffer, " + offsetS + ");");
		}

		public void buildConstruction(BuildContext context, int offset)
				throws BuildException {
		}

		public void buildStaticConstruction(BuildContext context, int offset)
				throws BuildException {
			Iterator<String> modifierList = modifiers.keySet().iterator();
			while (modifierList.hasNext()) {
				String modifierName = modifierList.next();
				ValueIdentifier value = modifiers
						.get(modifierName);

				Method m = Utils.findSetPropertyMethodFor(fieldClass,
						Utils.prepareIdentifier(modifierName),
						value.getValueClass());

				String valueText = value.getAsText(m.getParameterTypes()[0]);
				context.out.println(Utils.prepareIdentifier(name) + "."
						+ m.getName() + " (" + valueText + ");");
			}
		}

		public void buildToString(BuildContext context, int offset)
				throws BuildException {
			String getName = Utils.prepareIdentifier("get-" + name);

			context.out.println();
			context.out.println("ss.append(\" " + name + "\");");
			context.out.println("try {");
			context.out.indent();
			context.out
					.println("if (false) throw new FieldException(\"DUMMY\");");
			context.out.println("ss.append(\"='\"+" + getName + "()+\"'\");");
			context.out.unindent();
			context.out.println("} catch (FieldException exception)");
			context.out.println("  { ss.append(\" INVALID\"); }");
			context.out.println();
		}

		public void buildMethods(BuildContext context, int offset)
				throws BuildException {
			String name = Utils.prepareIdentifier(this.name);
			String valType = getProcessor().getType().getName();
			String getMod = getProcessor().getAcessorModifierName();
			String getName = Utils.prepareIdentifier("get-" + name);
			String setName = Utils.prepareIdentifier("set-" + name);
			String getAName = Utils.prepareIdentifier("get-" + getMod);
			String setAName = Utils.prepareIdentifier("set-" + getMod);
			String offsetS = offset == 0 ? "offset" : ("offset+" + offset);

			context.out.println("public " + valType + " " + getName
					+ "() throws FieldException {");
			context.out.indent();
			context.out.println("return " + name + "." + getAName
					+ "(buffer, " + offsetS + ");");
			context.out.unindent();
			context.out.println("}");
			context.out.println();
			context.out.println("public void " + setName + "(" + valType
					+ " value) throws FieldException {");
			context.out.indent();
			context.out.println(name + "." + setAName + "(buffer, " + offsetS
					+ ", value);");
			context.out.unindent();
			context.out.println("}");
			context.out.println();
		}
	}

}
