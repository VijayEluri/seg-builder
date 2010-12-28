package com.vse.segments.parser;

abstract class FieldBuilder {
	protected String name;

	FieldBuilder(String name) {
		this.name = name;
	}
 
	public String getName() {
		return name;
	}
 
	public abstract int getLength () throws BuildException;
	public abstract void buildCpp (BuildContext context, int offset) throws BuildException;
	public abstract void buildDefinition (BuildContext context, int offset) throws BuildException;
	public abstract void buildConstruction (BuildContext context, int offset) throws BuildException;
	public abstract void buildStaticConstruction (BuildContext context, int offset) throws BuildException;
	public abstract void buildInitialization (BuildContext context, int offset) throws BuildException;
	public abstract void buildMethods (BuildContext context, int offset) throws BuildException;
	public abstract void buildToString (BuildContext context, int offset) throws BuildException;
}
