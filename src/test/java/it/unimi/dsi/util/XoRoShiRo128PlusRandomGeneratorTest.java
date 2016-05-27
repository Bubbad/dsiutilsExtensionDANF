package it.unimi.dsi.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2014-2016 Sebastiano Vigna 
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

import org.junit.Test;


public class XoRoShiRo128PlusRandomGeneratorTest {
	private final static long seeds[] = { 0, 1, 1024, 0x5555555555555555L };

	@Test
	public void testNextFloat() {
		for ( long seed : seeds ) {
			XoRoShiRo128PlusRandomGenerator xorShift = new XoRoShiRo128PlusRandomGenerator( seed );
			double avg = 0;
			for ( int i = 1000000; i-- != 0; ) {
				float d = xorShift.nextFloat();
				assertTrue( Float.toString( d ), d < 1 );
				assertTrue( Float.toString( d ), d >= 0 );
				avg += d;
			}

			assertEquals( 500000, avg, 1000 );
		}
	}

	@Test
	public void testNextDouble() {
		for ( long seed : seeds ) {
			XoRoShiRo128PlusRandomGenerator xorShift = new XoRoShiRo128PlusRandomGenerator( seed );
			double avg = 0;
			for ( int i = 1000000; i-- != 0; ) {
				double d = xorShift.nextDouble();
				assertTrue( d < 1 );
				assertTrue( d >= 0 );
				avg += d;
			}

			assertEquals( 500000, avg, 1000 );
		}
	}

	@Test
	public void testNextInt() {
		for ( long seed : seeds ) {
			XoRoShiRo128PlusRandomGenerator xorShift = new XoRoShiRo128PlusRandomGenerator( seed );
			double avg = 0;
			for ( int i = 100000000; i-- != 0; ) {
				int d = xorShift.nextInt( 101 );
				assertTrue( d <= 100 );
				assertTrue( d >= 0 );
				avg += d;
			}

			assertEquals( 5000000000L, avg, 1000000 );
		}
	}

	@Test
	public void testNextInt2() {
		for ( long seed : seeds ) {
			XoRoShiRo128PlusRandomGenerator xorShift = new XoRoShiRo128PlusRandomGenerator( seed );
			final int[] count = new int[ 32 ];
			long change = 0;
			int prev = 0;
			for ( int i = 1000000; i-- != 0; ) {
				int d = xorShift.nextInt();
				change += Long.bitCount( d ^ prev );
				for ( int b = 32; b-- != 0; )
					if ( ( d & ( 1 << b ) ) != 0 ) count[ b ]++;
				prev = d;
			}

			assertEquals( 32 * 1000000L, change, 33000 );
			for ( int b = 32; b-- != 0; ) assertEquals( 500000, count[ b ], 2000 );
		}
	}

	@Test
	public void testNextLong() {
		for ( long seed : seeds ) {
			XoRoShiRo128PlusRandomGenerator xorShift = new XoRoShiRo128PlusRandomGenerator( seed );
			final int[] count = new int[ 64 ];
			long change = 0;
			long prev = 0;
			for ( int i = 1000000; i-- != 0; ) {
				long d = xorShift.nextLong();
				change += Long.bitCount( d ^ prev );
				for ( int b = 64; b-- != 0; )
					if ( ( d & ( 1L << b ) ) != 0 ) count[ b ]++;
				prev = d;
			}

			assertEquals( 32 * 1000000L, change, 7000 );
			for ( int b = 64; b-- != 0; ) assertEquals( 500000, count[ b ], 2000 );
		}
	}
	
	@Test
	public void testSameAsRandom() {
		XoRoShiRo128PlusRandom xorShiftStarRandom = new XoRoShiRo128PlusRandom( 0 );
		XoRoShiRo128PlusRandomGenerator xorShiftStar = new XoRoShiRo128PlusRandomGenerator( 0 );
		for( int i = 1000000; i-- != 0; ) {
			assertEquals( xorShiftStar.nextLong(), xorShiftStarRandom.nextLong() );
			assertEquals( 0, xorShiftStar.nextDouble(), xorShiftStarRandom.nextDouble() );
			assertEquals( xorShiftStar.nextInt(), xorShiftStarRandom.nextInt() );
			assertEquals( xorShiftStar.nextInt( 99 ), xorShiftStarRandom.nextInt( 99 ) );
			assertEquals( xorShiftStar.nextInt( 128 ), xorShiftStarRandom.nextInt( 128 ) );
		}
	}
}
