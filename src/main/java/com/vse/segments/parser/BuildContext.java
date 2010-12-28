package com.vse.segments.parser;

class BuildContext {
	public final BuildWriter out;
	private BuildRegistry registry;

	public BuildContext(BuildRegistry reg, BuildWriter w) {
		registry = reg;
		out = w;
	}

}
