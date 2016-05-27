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

public class IsoTransformationStrategyTest {

	@Test
	public void testCharSequence() {
		String s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 24, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0x4080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 16 ) );
		assertEquals( 0x4080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 24 ) );
		assertTrue( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 7 ) );
		assertFalse( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 0 ) );
		assertTrue( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 14 ) );
		assertFalse( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 15 ) );
		assertFalse( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 16 ) );

		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 32, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0xC04080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 24 ) );
		assertEquals( 0xC04080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 32 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 40, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0x20C04080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 32 ) );
		assertEquals( 0, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 32, 40 ) );
		assertEquals( 0x20C040L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 8, 40 ) );
		assertEquals( 0x20C0408L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 4, 44 ) );
		assertEquals( 0x20C04L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 12, 44 ) );
		s = new String( new char[] { '\u0001', '\u00FF', '\u00FF', '\u00FF', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001', '\u00FF', '\u00FF', '\u00FF' } );
		assertEquals( 104, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0x8080808080FFFFFL, TransformationStrategies.iso().toBitVector( s ).getLong( 12, 72 ) );
		assertEquals( 0x080808080FFFFFF8L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 4, 68 ) );

		s = new String( new char[] { '\u0001', '\u0002' } );
		assertEquals( 16, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x4080L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 16 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 24, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0xC04080L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 24 ) );
		s = new String( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 32, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x20C04080L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 32 ) );

		s = new String( new char[] { '\u0001', '\u00FF', '\u00FF', '\u00FF', '\u0001', '\u0001', '\u0001', '\u0001' } );
		assertEquals( 64, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x808080FFFFFF80L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 56 ) );
		assertEquals( 0x80808080FFFFFF80L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x80808080FFFFFF8L, TransformationStrategies.iso().toBitVector( s ).getLong( 4, 64 ) );

		s = new String( new char[] { '\u0001', '\u00FF', '\u00FF', '\u00FF', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001', '\u00FF', '\u00FF', '\u00FF' } );
		assertEquals( 96, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x8080808080FFFFFL, TransformationStrategies.iso().toBitVector( s ).getLong( 12, 72 ) );
		assertEquals( 0x080808080FFFFFF8L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 4, 68 ) );
	}

	@Test
	public void testMutableString() {
		MutableString s = new MutableString( new char[] { '\u0001', '\u0002' } );
		assertEquals( 24, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0x4080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 16 ) );
		assertEquals( 0x4080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 24 ) );
		assertTrue( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 7 ) );
		assertFalse( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 0 ) );
		assertTrue( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 14 ) );
		assertFalse( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 15 ) );
		assertFalse( TransformationStrategies.prefixFreeIso().toBitVector( s ).getBoolean( 16 ) );

		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 32, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0xC04080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 24 ) );
		assertEquals( 0xC04080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 32 ) );
		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 40, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0x20C04080L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 0, 32 ) );
		assertEquals( 0, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 32, 40 ) );
		assertEquals( 0x20C040L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 8, 40 ) );
		assertEquals( 0x20C0408L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 4, 44 ) );
		assertEquals( 0x20C04L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 12, 44 ) );
		s = new MutableString( new char[] { '\u0001', '\u00FF', '\u00FF', '\u00FF', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001', '\u00FF', '\u00FF', '\u00FF' } );
		assertEquals( 104, TransformationStrategies.prefixFreeIso().toBitVector( s ).length() );
		assertEquals( 0x8080808080FFFFFL, TransformationStrategies.iso().toBitVector( s ).getLong( 12, 72 ) );
		assertEquals( 0x080808080FFFFFF8L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 4, 68 ) );

		s = new MutableString( new char[] { '\u0001', '\u0002' } );
		assertEquals( 16, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x4080L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 16 ) );
		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003' } );
		assertEquals( 24, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0xC04080L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 24 ) );
		s = new MutableString( new char[] { '\u0001', '\u0002', '\u0003', '\u0004' } );
		assertEquals( 32, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x20C04080L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 32 ) );

		s = new MutableString( new char[] { '\u0001', '\u00FF', '\u00FF', '\u00FF', '\u0001', '\u0001', '\u0001', '\u0001' } );
		assertEquals( 64, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x808080FFFFFF80L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 56 ) );
		assertEquals( 0x80808080FFFFFF80L, TransformationStrategies.iso().toBitVector( s ).getLong( 0, 64 ) );
		assertEquals( 0x80808080FFFFFF8L, TransformationStrategies.iso().toBitVector( s ).getLong( 4, 64 ) );

		s = new MutableString( new char[] { '\u0001', '\u00FF', '\u00FF', '\u00FF', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001', '\u00FF', '\u00FF', '\u00FF' } );
		assertEquals( 96, TransformationStrategies.iso().toBitVector( s ).length() );
		assertEquals( 0x8080808080FFFFFL, TransformationStrategies.iso().toBitVector( s ).getLong( 12, 72 ) );
		assertEquals( 0x080808080FFFFFF8L, TransformationStrategies.prefixFreeIso().toBitVector( s ).getLong( 4, 68 ) );
	}
}
