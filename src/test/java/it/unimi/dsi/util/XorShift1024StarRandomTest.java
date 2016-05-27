package it.unimi.dsi.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2013-2016 Sebastiano Vigna 
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


public class XorShift1024StarRandomTest {
	private final static long seeds[] = { 0, 1, 1024, 0x5555555555555555L };

	@Test
	public void testNextFloat() {
		for ( long seed : seeds ) {
			XorShift1024StarRandom xorShiftRandom = new XorShift1024StarRandom( seed );
			double avg = 0;
			for ( int i = 1000000; i-- != 0; ) {
				float d = xorShiftRandom.nextFloat();
				assertTrue( d < 1 );
				assertTrue( d >= 0 );
				avg += d;
			}

			assertEquals( 500000, avg, 500 );
		}
	}

	@Test
	public void testNextDouble() {
		for ( long seed : seeds ) {
			XorShift1024StarRandom xorShiftRandom = new XorShift1024StarRandom( seed );
			double avg = 0;
			for ( int i = 1000000; i-- != 0; ) {
				double d = xorShiftRandom.nextDouble();
				assertTrue( d < 1 );
				assertTrue( d >= 0 );
				avg += d;
			}

			assertEquals( 500000, avg, 500 );
		}
	}

	@Test
	public void testNextInt() {
		for ( long seed : seeds ) {
			XorShift1024StarRandom xorShiftRandom = new XorShift1024StarRandom( seed );
			double avg = 0;
			for ( int i = 100000000; i-- != 0; ) {
				int d = xorShiftRandom.nextInt( 101 );
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
			XorShift1024StarRandom xorShiftRandom = new XorShift1024StarRandom( seed );
			final int[] count = new int[ 32 ];
			long change = 0;
			int prev = 0;
			for ( int i = 1000000; i-- != 0; ) {
				int d = xorShiftRandom.nextInt();
				change += Long.bitCount( d ^ prev );
				for ( int b = 32; b-- != 0; )
					if ( ( d & ( 1 << b ) ) != 0 ) count[ b ]++;
				prev = d;
			}

			assertEquals( 32 * 1000000L, change, 60000 );
			for ( int b = 32; b-- != 0; ) assertEquals( 500000, count[ b ], 1600 );
		}
	}

	@Test
	public void testNextLong() {
		for ( long seed : seeds ) {
			XorShift1024StarRandom xorShiftRandom = new XorShift1024StarRandom( seed );
			final int[] count = new int[ 64 ];
			long change = 0;
			long prev = 0;
			for ( int i = 1000000; i-- != 0; ) {
				long d = xorShiftRandom.nextLong();
				change += Long.bitCount( d ^ prev );
				for ( int b = 64; b-- != 0; )
					if ( ( d & ( 1L << b ) ) != 0 ) count[ b ]++;
				prev = d;
			}

			assertEquals( 32 * 1000000L, change, 10000 );
			for ( int b = 64; b-- != 0; ) assertEquals( 500000, count[ b ], 1800 );
		}
	}
	
	@Test
	public void testJump() {
		XorShift1024StarRandom xorShiftRandom0 = new XorShift1024StarRandom( 0 ), xorShiftRandom1 = new XorShift1024StarRandom( 0 );
		xorShiftRandom0.nextLong();
		xorShiftRandom0.jump();
		xorShiftRandom1.jump();
		xorShiftRandom1.nextLong();
		assertEquals( xorShiftRandom0.nextLong(), xorShiftRandom1.nextLong() );
	}
}
