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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;

import java.io.IOException;

import org.junit.Test;

public class MutableStringTest {
	@Test
	public void testSqueezeSpace() {
		MutableString s = new MutableString( new char[] { 32, 13, 10, 32, 32, 32, 13, 10, 32, 32, 32, 13, 10, 32, 32, 32, 32, 32 } );
				
		assertEquals( new MutableString( " \r\n \r\n \r\n " ), s.squeezeSpace() );
		assertEquals( new MutableString( " " ), s.squeezeWhitespace() );
	}
	
	@Test
	public void testSubsequence() {
		MutableString s = new MutableString( "abc" );
		CharSequence ss = s.subSequence( 1, 3 );
		assertEquals( new MutableString( "bc" ), ss );
		assertEquals( 1, ss.subSequence( 1, 2 ).length() );
	}
	
	@Test
	public void testSkipSelfDelimUTF8() throws IOException {
		final FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
		new MutableString( "a" ).writeSelfDelimUTF8( fastByteArrayOutputStream );
		new MutableString( "b" ).writeSelfDelimUTF8( fastByteArrayOutputStream );
		new MutableString( "\u221E" ).writeSelfDelimUTF8( fastByteArrayOutputStream );
		new MutableString( "c" ).writeSelfDelimUTF8( fastByteArrayOutputStream );
		fastByteArrayOutputStream.flush();
		final FastByteArrayInputStream fastByteArrayInputStream = new FastByteArrayInputStream( fastByteArrayOutputStream.array );
		assertEquals( "a", new MutableString().readSelfDelimUTF8( fastByteArrayInputStream ).toString() );
		assertEquals( "b", new MutableString().readSelfDelimUTF8( fastByteArrayInputStream ).toString() );
		assertEquals( 1, MutableString.skipSelfDelimUTF8( fastByteArrayInputStream ) );
		assertEquals( "c", new MutableString().readSelfDelimUTF8( fastByteArrayInputStream ).toString() );
		fastByteArrayInputStream.position( 0 );
		assertEquals( "a", new MutableString().readSelfDelimUTF8( fastByteArrayInputStream ).toString() );
		assertEquals( 1, MutableString.skipSelfDelimUTF8( fastByteArrayInputStream ) );
		assertEquals( "\uu221E", new MutableString().readSelfDelimUTF8( fastByteArrayInputStream ).toString() );
		assertEquals( "c", new MutableString().readSelfDelimUTF8( fastByteArrayInputStream ).toString() );
	}
	
	@Test
	public void testIsEmpty() {
		assertTrue( new MutableString().compact().isEmpty() );
		assertTrue( new MutableString().loose().isEmpty() );
		assertFalse( new MutableString( " " ).compact().isEmpty() );
		assertFalse( new MutableString( " " ).loose().isEmpty() );
	}
}
