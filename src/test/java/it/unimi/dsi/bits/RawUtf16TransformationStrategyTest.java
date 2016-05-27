package it.unimi.dsi.bits;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.lang.MutableString;

import org.junit.Test;

public class RawUtf16TransformationStrategyTest {

	@Test
	public void testCharSequence() {
		String s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 32, TransformationStrategies.rawUtf16().toBitVector( s ).length() );
		assertEquals( 0x00020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 32 ) );
		assertTrue( TransformationStrategies.rawUtf16().toBitVector( s ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.rawUtf16().toBitVector( s ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.rawUtf16().toBitVector( s ).getBoolean( 17 ) );

		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 48, TransformationStrategies.rawUtf16().toBitVector( s ).length() );
		assertEquals( 0x000300020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 48 ) );
		assertEquals( 0x00020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 32 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 64, TransformationStrategies.rawUtf16().toBitVector( s ).length() );
		assertEquals( 0x0004000300020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x000400030002L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 16, 64 ) );

		assertEquals( 0x1L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 1 ) );
		assertEquals( 0x00001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 17 ) );
		assertEquals( 0x20001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 18 ) );

		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004', '\u0005' } );
		assertEquals( 0, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 4, 4 ) );
		assertEquals( 0x005000400030002000L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 4, 68 ) );
		assertEquals( 0x000500040003000200L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 8, 72 ) );
	}
	
	@Test
	public void testMutableString() {
		MutableString s = new MutableString( new char[] { '\u0001', '\u0002' } );
		assertEquals( 32, TransformationStrategies.rawUtf16().toBitVector( s ).length() );
		assertEquals( 0x00020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 32 ) );
		assertTrue( TransformationStrategies.rawUtf16().toBitVector( s ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.rawUtf16().toBitVector( s ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.rawUtf16().toBitVector( s ).getBoolean( 17 ) );

		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 48, TransformationStrategies.rawUtf16().toBitVector( s ).length() );
		assertEquals( 0x000300020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 48 ) );
		assertEquals( 0x00020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 32 ) );
		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 64, TransformationStrategies.rawUtf16().toBitVector( s ).length() );
		assertEquals( 0x0004000300020001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x000400030002L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 16, 64 ) );

		assertEquals( 0x1L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 1 ) );
		assertEquals( 0x00001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 17 ) );
		assertEquals( 0x20001L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 0, 18 ) );

		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003', '\u0004', '\u0005' } );
		assertEquals( 0, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 4, 4 ) );
		assertEquals( 0x005000400030002000L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 4, 68 ) );
		assertEquals( 0x000500040003000200L, TransformationStrategies.rawUtf16().toBitVector( s ).getLong( 8, 72 ) );
	}
	
}
