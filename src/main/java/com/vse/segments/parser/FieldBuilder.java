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
