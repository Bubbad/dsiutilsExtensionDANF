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

import org.junit.Test;

public class ByteArrayTransformationStrategyTest {

	@Test
	public void testGetLong() {
		byte[] a = new byte[] { 0x55, (byte)0xFF };
		assertEquals( 16, TransformationStrategies.byteArray().toBitVector( a ).length() );
		assertEquals( 0xFFAAL, TransformationStrategies.byteArray().toBitVector( a ).getLong( 0, 16 ) );

		a = new byte[] { 1, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, 0 };
		assertFalse( TransformationStrategies.byteArray().toBitVector( a ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.byteArray().toBitVector( a ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.byteArray().toBitVector( a ).getBoolean( 7 ) );
		assertTrue( TransformationStrategies.byteArray().toBitVector( a ).getBoolean( 64 ) );
		assertEquals( 128, TransformationStrategies.byteArray().toBitVector( a ).getLong( 0, 56 ) );
		assertEquals( 128, TransformationStrategies.byteArray().toBitVector( a ).getLong( 0, 64 ) );
		assertEquals( 0xF000000000000008L, TransformationStrategies.byteArray().toBitVector( a ).getLong( 4, 68 ) );
		assertEquals( -1L, TransformationStrategies.byteArray().toBitVector( a ).getLong( 64, 128 ) );

		for( int i = 1; i < 7; i++ )
			assertEquals( 0, TransformationStrategies.byteArray().toBitVector( a ).getLong( 0, i ) );

		for( int i = 8; i < 63; i++ )
			assertEquals( 128, TransformationStrategies.byteArray().toBitVector( a ).getLong( 0, i ) );
	}
}
