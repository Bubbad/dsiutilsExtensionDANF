package it.unimi.dsi.lang;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2010-2016 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses/>.
 * 
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.martiansoftware.jsap.ParseException;

public class EnumParserTest {
	public enum TestEnum {
		A,
		b,
		C
	};
	
	@Test
	public void test() throws Exception {
		EnumStringParser<TestEnum> enumStringParser = EnumStringParser.getParser(TestEnum.class);
		assertEquals(TestEnum.A, enumStringParser.parse("A"));
		assertEquals(TestEnum.b, enumStringParser.parse("b"));
		assertEquals(TestEnum.C, enumStringParser.parse("C"));
	}

	@Test(expected=ParseException.class)
	public void testNoMatchBecauseOfCase() throws Exception {
		EnumStringParser<TestEnum> enumStringParser = EnumStringParser.getParser(TestEnum.class);
		enumStringParser.parse("a");
	}

	@Test(expected=ParseException.class)
	public void testNoMatchBecauseWrong() throws Exception {
		EnumStringParser<TestEnum> enumStringParser = EnumStringParser.getParser(TestEnum.class);
		enumStringParser.parse("D");
	}

	@Test
	public void testNorm() throws Exception {
		EnumStringParser<TestEnum> enumStringParser = EnumStringParser.getParser(TestEnum.class, true);
		assertEquals(TestEnum.A, enumStringParser.parse("a"));
		assertEquals(TestEnum.C, enumStringParser.parse("c"));
	}
}
