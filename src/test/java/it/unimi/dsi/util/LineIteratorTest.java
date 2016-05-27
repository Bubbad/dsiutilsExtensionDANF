package it.unimi.dsi.util;

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
import it.unimi.dsi.io.FastBufferedReader;
import it.unimi.dsi.io.LineIterator;
import it.unimi.dsi.logging.ProgressLogger;

import java.io.StringReader;

import org.junit.Test;


public class LineIteratorTest {

	private static final String TEXT = "0\n1\n2\n3";
	private static final CharSequence[] LINES = TEXT.split( "\n" );
	
	@Test
	public void testLineIteratorProgressLogger() {
		testLineIterator( new ProgressLogger() );
	}

	@Test
	public void testLineIterator() {
		testLineIterator( null );
	}

	public void testLineIterator( ProgressLogger pl ) {
		final LineIterator lineIterator = new LineIterator( new FastBufferedReader( new StringReader( TEXT ) ), pl );
		int i = 0;
		while( lineIterator.hasNext() )
			assertEquals( LINES[ i++ ].toString(), lineIterator.next().toString() );

		assertEquals( i, LINES.length );
	}
	
}
