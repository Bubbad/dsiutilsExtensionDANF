package it.unimi.dsi.parser.callback;

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

import static org.junit.Assert.assertTrue;
import it.unimi.dsi.parser.BulletParser;

import org.junit.Test;

public class TextExtractorTest {

	@Test
	public void testBRBreaksFlow() {
		char a[] = "ciao<BR>mamma<BR>".toCharArray();
		BulletParser bulletParser = new BulletParser();
		TextExtractor textExtractor = new TextExtractor();
		bulletParser.setCallback( textExtractor );
		bulletParser.parse( a );
		assertTrue( textExtractor.text.toString(), textExtractor.text.indexOf( ' ' ) != -1 );
	}

}
