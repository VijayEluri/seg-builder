package com.vse.segments.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

class BuildWriter {
	public int indent = 0;
	private boolean linefeedDisabled = false;
	private boolean linefeedNeeded = false;
	private final PrintWriter out;

	public BuildWriter(File path, String moduleName) throws IOException {
		out = new PrintWriter(new FileOutputStream(new File(path, moduleName)));
	}

	public void close() {
		out.close();
	}

	public void indent() {
		if (!linefeedDisabled && linefeedNeeded)
			out.print('\n');

		indent += 2;
		linefeedDisabled = true;
	}

	public void unindent() {
		linefeedDisabled = false;
		linefeedNeeded = false;
		indent -= 2;
	}

	public void println(String s) {
		if (!linefeedDisabled && linefeedNeeded)
			out.print('\n');

		for (int i = 0; i < indent; i++)
			out.print(' ');

		out.print(s);
		out.print('\n');

		linefeedNeeded = false;
		linefeedDisabled = false;
	}

	public void printlnf() {
		linefeedNeeded = true;
		linefeedDisabled = false;
	}

	public void println() {
		linefeedNeeded = true;
	}

}
