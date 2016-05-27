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

import org.junit.Test;

public class Utf32TransformationStrategyTest {

	@Test
	public void testGetLong() {
		String s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 96, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).length() );
		assertEquals( 0x4000000080000000L, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x0000000040000000L, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 32, 96 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 128, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).length() );
		assertEquals( 0x80000000L, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 0, 48 ) );
		assertEquals( 0x4000000080000000L, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 0, 64 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 160, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).length() );
		assertEquals( 0, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 128, 160 ) );
		//System.err.println( Long.toHexString( TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 16, 80 ) ));
		assertEquals( 0xC000000040000000L, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 32, 96 ) );
		s = new String( new char[] { '\uD800', '\uDC00' } );
		assertEquals( 64, TransformationStrategies.prefixFreeUtf32().length( s ) );
		assertEquals( 0x8000, TransformationStrategies.prefixFreeUtf32().toBitVector( s ).getLong( 0, 64 ) );

	
		s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 64, TransformationStrategies.utf32().toBitVector( s ).length() );
		assertEquals( 0x4000000080000000L, TransformationStrategies.utf32().toBitVector( s ).getLong( 0, 64 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 96, TransformationStrategies.utf32().toBitVector( s ).length() );
		assertEquals( 0xC000000040000000L, TransformationStrategies.utf32().toBitVector( s ).getLong( 32, 96 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 128, TransformationStrategies.utf32().toBitVector( s ).length() );
		assertEquals( 0x4000000080000000L, TransformationStrategies.utf32().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x20000000C0000000L, TransformationStrategies.utf32().toBitVector( s ).getLong( 64, 128 ) );

		s = new String( new char[] { '\uD800', '\uDC00' } );
		assertEquals( 32, TransformationStrategies.utf32().length( s ) );
		assertEquals( 0x8000, TransformationStrategies.utf32().toBitVector( s ).getLong( 0, 32 ) );
	}
	
}
