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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RawFixedLongTransformationStrategyTest {

	@Test
	public void testGetBoolean() {
		final TransformationStrategy<Long> rawFixedLong = TransformationStrategies.rawFixedLong();
		BitVector p = rawFixedLong.toBitVector( Long.valueOf( 0 ) );
		for( int i = Long.SIZE; i-- != 0; ) assertFalse( p.getBoolean( i ) );
		p = rawFixedLong.toBitVector( Long.valueOf( 0xDEADBEEFDEADF00DL ) );
		for( int i = Long.SIZE; i-- != 0; ) assertTrue( p.getBoolean( i ) == ( ( 0xDEADBEEFDEADF00DL & 1L << i ) != 0 ) );
	}

	@Test
	public void testGetLong() {
		final TransformationStrategy<Long> rawFixedLong = TransformationStrategies.rawFixedLong();
		BitVector p = rawFixedLong.toBitVector( Long.valueOf( 0xDEADBEEFDEADF00DL ) );
		for( int from = Long.SIZE; from-- != 0; ) 
			for( int to = Long.SIZE; from < to--; ) 
				assertTrue( p.getLong( from, to ) == LongArrayBitVector.wrap( new long[] { 0xDEADBEEFDEADF00DL } ).getLong( from, to ) );
	}

}
