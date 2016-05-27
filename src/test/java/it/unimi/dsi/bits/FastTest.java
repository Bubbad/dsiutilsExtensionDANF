package it.unimi.dsi.bits;

import static org.junit.Assert.assertEquals;
import it.unimi.dsi.util.SplitMix64RandomGenerator;

import org.junit.Test;

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

public class FastTest {
	
	@Test
	public void testCeilLog2() {
		for( int i = 1; i < 1000; i++ ) assertEquals( (int)Math.ceil( Math.log( i ) / Math.log( 2 ) ), Fast.ceilLog2( i ) );
	}

	@Test
	public void testLength() {
		assertEquals( 1, Fast.length( 0 ) );
		assertEquals( 1, Fast.length( 0L ) );
		for( int i = 1; i < 100; i++ ) assertEquals( Fast.mostSignificantBit( i ) + 1, Fast.length( i ) ); 
		for( long i = 1; i < 100; i++ ) assertEquals( Fast.mostSignificantBit( i ) + 1, Fast.length( i ) ); 
	}
	
	@Test
	public void testMostSignificantBitInt() {
		assertEquals( -1, Fast.mostSignificantBit( 0 ) );
		for( int i = 0; i < 32; i++ ) {
			assertEquals( i, Fast.mostSignificantBit( 1L << i ) );
			assertEquals( i, Fast.mostSignificantBit( 1L << i | 1 ) );
			assertEquals( i, Fast.mostSignificantBit( 1L << i ) );
			assertEquals( i, Fast.mostSignificantBit( 1L << i | 1 ) );
		}
	}

	@Test
	public void testMostSignificantBitLong() {
		assertEquals( -1, Fast.mostSignificantBit( 0 ) );
		for( int i = 0; i < 64; i++ ) {
			assertEquals( i, Fast.mostSignificantBit( 1L << i ) );
			assertEquals( i, Fast.mostSignificantBit( 1L << i | 1 ) );
			assertEquals( i, Fast.mostSignificantBit( 1L << i ) );
			assertEquals( i, Fast.mostSignificantBit( 1L << i | 1 ) );
		}
	}

	
	@Test
	public void testSelect2() {
		assertEquals( 0, Fast.select( 1, 0 ) );
		for( int i = 0; i < 64; i++ ) assertEquals( i, Fast.select( 0xFFFFFFFFFFFFFFFFL, i ) );
		for( int i = 1; i < 32; i++ ) assertEquals( 2 * i + 1, Fast.select( 0xAAAAAAAAAAAAAAAAL, i ) );
		SplitMix64RandomGenerator r = new SplitMix64RandomGenerator( 0 );
		for( int i = 100000; i-- != 0; ) {
			final long x = r.nextLong();
			final int count = Long.bitCount( x );
			for( int rank = count; rank-- != 0; ) assertEquals( Fast.select( x, rank ), Fast.select( x, rank ) );
		}
	}
	


	@Test
	public void testInt2Nat() {
		assertEquals( Integer.MAX_VALUE, Fast.int2nat( Integer.MIN_VALUE / 2 ) );
		assertEquals( Integer.MAX_VALUE - 2, Fast.int2nat( Integer.MIN_VALUE / 2 + 1 ) );
		assertEquals( Integer.MAX_VALUE - 1, Fast.int2nat( Integer.MAX_VALUE / 2 ) );

		for( int i = 0; i < 16; i++ ) assertEquals( 2*i, Fast.int2nat( i ) );
		for( int i = -16; i < 0; i++ ) assertEquals( -2*i-1, Fast.int2nat( i ) );

		assertEquals( Long.MAX_VALUE, Fast.int2nat( Long.MIN_VALUE / 2 ) );
		assertEquals( Long.MAX_VALUE - 2, Fast.int2nat( Long.MIN_VALUE / 2 + 1 ) );
		assertEquals( Long.MAX_VALUE - 1, Fast.int2nat( Long.MAX_VALUE / 2 ) );

		for( int i = 0; i < 16; i++ ) assertEquals( 2L*i, Fast.int2nat( i ) );
		for( int i = -16; i < 0; i++ ) assertEquals( -2L*i-1, Fast.int2nat( i ) );
	}

	@Test
	public void testNat2Int() {
		assertEquals( Integer.MIN_VALUE / 2, Fast.nat2int( Integer.MAX_VALUE ) );
		assertEquals( Integer.MIN_VALUE / 2 + 1, Fast.nat2int( Integer.MAX_VALUE - 2 ) );
		assertEquals( Integer.MAX_VALUE / 2, Fast.nat2int( Integer.MAX_VALUE - 1 ) );

		for( int i = 0; i < 16; i++ ) assertEquals( i, Fast.nat2int( 2*i ) );
		for( int i = -16; i < 0; i++ ) assertEquals( i, Fast.nat2int( -2*i-1 ) );

		assertEquals( Long.MIN_VALUE / 2 + 1, Fast.nat2int( Long.MAX_VALUE - 2 ) );
		assertEquals( Long.MIN_VALUE / 2, Fast.nat2int( Long.MAX_VALUE ) );
		assertEquals( Long.MAX_VALUE / 2, Fast.nat2int( Long.MAX_VALUE - 1 ) );

		for( int i = 0; i < 16; i++ ) assertEquals( i, Fast.nat2int( 2L*i ) );
		for( int i = -16; i < 0; i++ ) assertEquals( i, Fast.nat2int( -2L*i-1 ) );

	}
}
