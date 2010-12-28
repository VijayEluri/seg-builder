package com.vse.segments.parser;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;


public class SegmentDefinitionParserTest {

	@Test
	public void runSegmentParser() throws Exception {
		SegmentDefinitionParser.main(new String[] {
				"-javaOut",
				"target",
				"src/test/segments/TestRecord.segmentDef"
		});
		File f = new File("target/TestRecord.java");
		assertTrue(f.exists());
		assertTrue(f.isFile());
	}
}
