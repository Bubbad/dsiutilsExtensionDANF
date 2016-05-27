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

public class Utf16TransformationStrategyTest {

	@Test
	public void testCharacterSequence() {
		String s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 48, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).length() );
		assertEquals( 0x40008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 32 ) );
		assertEquals( 0x40008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 48 ) );
		assertFalse( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 15 ) );
		assertFalse( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 33 ) );

		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 64, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).length() );
		assertEquals( 0xC00040008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 48 ) );
		assertEquals( 0xC00040008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 64 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 80, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).length() );
		assertEquals( 0x2000C00040008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 64, 80 ) );
		//System.err.println( Long.toHexString( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 16, 80 ) ));
		assertEquals( 0x2000C0004000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 16, 80 ) );

	
		s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 32, TransformationStrategies.utf16().toBitVector( s ).length() );
		assertEquals( 0x40008000L, TransformationStrategies.utf16().toBitVector( s ).getLong( 0, 32 ) );
		assertFalse( TransformationStrategies.utf16().toBitVector( s ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.utf16().toBitVector( s ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.utf16().toBitVector( s ).getBoolean( 15 ) );

		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 48, TransformationStrategies.utf16().toBitVector( s ).length() );
		assertEquals( 0xC00040008000L, TransformationStrategies.utf16().toBitVector( s ).getLong( 0, 48 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 64, TransformationStrategies.utf16().toBitVector( s ).length() );
		assertEquals( 0x2000C00040008000L, TransformationStrategies.utf16().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x2000C000400080L, TransformationStrategies.utf16().toBitVector( s ).getLong( 8, 62 ) );
		assertEquals( 0x000C000400080L, TransformationStrategies.utf16().toBitVector( s ).getLong( 8, 61 ) );
	}
	
	@Test
	public void testMutableString() {
		MutableString s = new MutableString( new char[] { '\u0001', '\u0002' } );
		assertEquals( 48, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).length() );
		assertEquals( 0x40008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 32 ) );
		assertEquals( 0x40008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 48 ) );
		assertFalse( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 15 ) );
		assertFalse( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getBoolean( 33 ) );

		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 64, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).length() );
		assertEquals( 0xC00040008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 48 ) );
		assertEquals( 0xC00040008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 64 ) );
		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 80, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).length() );
		assertEquals( 0x2000C00040008000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 64, 80 ) );
		//System.err.println( Long.toHexMutableString( TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 16, 80 ) ));
		assertEquals( 0x2000C0004000L, TransformationStrategies.prefixFreeUtf16().toBitVector( s ).getLong( 16, 80 ) );

	
		s = new MutableString( new char[] { '\u0001', '\u0002' } );
		assertEquals( 32, TransformationStrategies.utf16().toBitVector( s ).length() );
		assertEquals( 0x40008000L, TransformationStrategies.utf16().toBitVector( s ).getLong( 0, 32 ) );
		assertFalse( TransformationStrategies.utf16().toBitVector( s ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.utf16().toBitVector( s ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.utf16().toBitVector( s ).getBoolean( 15 ) );

		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 48, TransformationStrategies.utf16().toBitVector( s ).length() );
		assertEquals( 0xC00040008000L, TransformationStrategies.utf16().toBitVector( s ).getLong( 0, 48 ) );
		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 64, TransformationStrategies.utf16().toBitVector( s ).length() );
		assertEquals( 0x2000C00040008000L, TransformationStrategies.utf16().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x2000C000400080L, TransformationStrategies.utf16().toBitVector( s ).getLong( 8, 62 ) );
		assertEquals( 0x000C000400080L, TransformationStrategies.utf16().toBitVector( s ).getLong( 8, 61 ) );
	}
}
