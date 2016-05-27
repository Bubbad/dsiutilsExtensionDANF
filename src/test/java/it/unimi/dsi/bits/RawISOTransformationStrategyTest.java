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

public class RawISOTransformationStrategyTest {

	@Test
	public void testCharacterSequence() {
		String a = new String( new char[] { 0x55, 0xFF } );
		assertEquals( 16, TransformationStrategies.rawIso().toBitVector( a ).length() );
		System.err.println(Long.toHexString( TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, 16 )  ));
		assertEquals( 0xFF55L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, 16 ) );

		a = new String( new char[] { 1, 0, 0, 0, 0, 0, 0, 0, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, 0 } );
		assertTrue( TransformationStrategies.rawIso().toBitVector( a ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.rawIso().toBitVector( a ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.rawIso().toBitVector( a ).getBoolean( 64 ) );
		assertEquals( 0x1L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, 56 ) );
		assertEquals( 0x1L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, 64 ) );
		assertEquals( -1L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 64, 128 ) );

		for( int i = 1; i < 64; i++ )
			assertEquals( 1, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, i ) );
		for( int i = 0; i < 63; i++ )
			assertEquals( 0, TransformationStrategies.rawIso().toBitVector( a ).getLong( 1, 1 + i ) );
		for( int i = 64; i < 127; i++ )
			assertEquals( ( 1L << i - 64 ) - 1, TransformationStrategies.rawIso().toBitVector( a ).getLong( 64, i ) );

		a = new String( new char[] { 1, 0, 0, 0, 0, 0, 0, 0, 0x55 } );
		assertEquals( 0x55L << 57, TransformationStrategies.rawIso().toBitVector( a ).getLong( 7, 71 ) );
		assertEquals( 0x15L << 57, TransformationStrategies.rawIso().toBitVector( a ).getLong( 7, 70 ) );
		assertEquals( 0x15L << 57, TransformationStrategies.rawIso().toBitVector( a ).getLong( 7, 69 ) );
	
	}
	
	@Test
	public void testMutableString() {
		MutableString a = new MutableString( new char[] { 0x55, 0xFF } );
		assertEquals( 16, TransformationStrategies.rawIso().toBitVector( a ).length() );
		assertEquals( 0xFF55L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, 16 ) );

		a = new MutableString( new char[] { 1, 0, 0, 0, 0, 0, 0, 0, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, (char)-1, 0 } );
		assertTrue( TransformationStrategies.rawIso().toBitVector( a ).getBoolean( 0 ) );
		assertFalse( TransformationStrategies.rawIso().toBitVector( a ).getBoolean( 1 ) );
		assertTrue( TransformationStrategies.rawIso().toBitVector( a ).getBoolean( 64 ) );
		assertEquals( 0x1L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, 56 ) );
		assertEquals( 0x1L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, 64 ) );
		assertEquals( -1L, TransformationStrategies.rawIso().toBitVector( a ).getLong( 64, 128 ) );

		for( int i = 1; i < 64; i++ )
			assertEquals( 1, TransformationStrategies.rawIso().toBitVector( a ).getLong( 0, i ) );
		for( int i = 0; i < 63; i++ )
			assertEquals( 0, TransformationStrategies.rawIso().toBitVector( a ).getLong( 1, 1 + i ) );
		for( int i = 64; i < 127; i++ )
			assertEquals( ( 1L << i - 64 ) - 1, TransformationStrategies.rawIso().toBitVector( a ).getLong( 64, i ) );

		a = new MutableString( new char[] { 1, 0, 0, 0, 0, 0, 0, 0, 0x55 } );
		assertEquals( 0x55L << 57, TransformationStrategies.rawIso().toBitVector( a ).getLong( 7, 71 ) );
		assertEquals( 0x15L << 57, TransformationStrategies.rawIso().toBitVector( a ).getLong( 7, 70 ) );
		assertEquals( 0x15L << 57, TransformationStrategies.rawIso().toBitVector( a ).getLong( 7, 69 ) );
	
	}
}
