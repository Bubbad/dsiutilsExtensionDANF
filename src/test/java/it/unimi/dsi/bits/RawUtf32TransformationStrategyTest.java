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

public class RawUtf32TransformationStrategyTest {

	@Test
	public void testGetLong() {
		String s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 64, TransformationStrategies.rawUtf32().toBitVector( s ).length() );
		assertEquals( 0x200000001L, TransformationStrategies.rawUtf32().toBitVector( s ).getLong( 0, 64 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 96, TransformationStrategies.rawUtf32().toBitVector( s ).length() );
		assertEquals( 0x300000002L, TransformationStrategies.rawUtf32().toBitVector( s ).getLong( 32, 96 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 128, TransformationStrategies.rawUtf32().toBitVector( s ).length() );
		assertEquals( 0x200000001L, TransformationStrategies.rawUtf32().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x400000003L, TransformationStrategies.rawUtf32().toBitVector( s ).getLong( 64, 128 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );

		s = new String( new char[] { '\uD800', '\uDC00' } );
		assertEquals( 32, TransformationStrategies.rawUtf32().length( s ) );
		assertEquals( 0x10000, TransformationStrategies.rawUtf32().toBitVector( s ).getLong( 0, 32) );
	}
	
}
