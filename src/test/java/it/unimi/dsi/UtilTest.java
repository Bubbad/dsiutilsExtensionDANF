package it.unimi.dsi;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2002-2016 Sebastiano Vigna 
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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.longs.LongBigArrays;
import it.unimi.dsi.util.SplitMix64Random;

import java.util.Collections;

import org.junit.Test;

public class UtilTest {
	
	@Test
	public void testFormatBinarySize() {
		assertEquals( "1", Util.formatBinarySize( 1 ) );
		assertEquals( "2", Util.formatBinarySize( 2 ) );
		boolean ok = false;
		try {
			Util.formatBinarySize( 6 );
		}
		catch( IllegalArgumentException e ) {
			ok = true;
		}
		assertTrue( ok );
		assertEquals( "128", Util.formatBinarySize( 128 ) );
		assertEquals( "1Ki", Util.formatBinarySize( 1024 ) );
		assertEquals( "2Ki", Util.formatBinarySize( 2048 ) );
		assertEquals( "1Mi", Util.formatBinarySize( 1024 * 1024 ) );
		assertEquals( "2Mi", Util.formatBinarySize( 2 * 1024 * 1024 ) );
		assertEquals( "1Gi", Util.formatBinarySize( 1024 * 1024 * 1024 ) );
		assertEquals( "2Gi", Util.formatBinarySize( 2L * 1024 * 1024 * 1024 ) );
		assertEquals( "1Ti", Util.formatBinarySize( 1024L * 1024 * 1024 * 1024 ) );
		assertEquals( "2Ti", Util.formatBinarySize( 2L * 1024 * 1024 * 1024 * 1024 ) );
	}

	@Test
	public void testFormatSize() {
		assertEquals( "1", Util.formatSize( 1 ) );
		assertEquals( "2", Util.formatSize( 2 ) );
		assertEquals( "128", Util.formatSize( 128 ) );
		assertEquals( "1.00K", Util.formatSize( 1000 ) );
		assertEquals( "2.00K", Util.formatSize( 2000 ) );
		assertEquals( "2.50K", Util.formatSize( 2500 ) );
		assertEquals( "1.00M", Util.formatSize( 1000 * 1000 ) );
		assertEquals( "2.00M", Util.formatSize( 2 * 1000 * 1000 ) );
		assertEquals( "1.00G", Util.formatSize( 1000 * 1000 * 1000 ) );
		assertEquals( "2.00G", Util.formatSize( 2L * 1000 * 1000 * 1000 ) );
		assertEquals( "1.00T", Util.formatSize( 1000L * 1000 * 1000 * 1000 ) );
		assertEquals( "2.00T", Util.formatSize( 2L * 1000 * 1000 * 1000 * 1000 ) );
	}
	

	@Test
	public void testInvertPermutation() {
		for( int k = 10; k-- != 0; ) {
			final int[] p = Util.identity( k * 10 );
			IntArrays.shuffle( p, new SplitMix64Random( 0 ) );
			int[] q = Util.invertPermutation( p );
			q = Util.invertPermutation( q );
			assertArrayEquals( q, p );
		}
	}
	
	@Test
	public void testInvertPermutationInPlace() {
		assertArrayEquals( new int[] { 0, 1, 2 }, Util.invertPermutationInPlace( new int[] { 0, 1, 2 } ) );
		assertArrayEquals( new int[] { 1, 0 }, Util.invertPermutationInPlace( new int[] { 1, 0 } ) );
		assertArrayEquals( new int[] { 0, 2, 1 }, Util.invertPermutationInPlace( new int[] { 0, 2, 1 } ) );
		assertArrayEquals( new int[] { 3, 0, 1, 2 }, Util.invertPermutationInPlace( new int[] { 1, 2, 3, 0 } ) );
		
		for( int k = 10; k-- != 0; ) {
			final int[] p = Util.identity( k * 10 );
			Collections.shuffle( IntArrayList.wrap( p ) );
			final int[] q = Util.invertPermutation( p );
			Util.invertPermutationInPlace( p );
			assertArrayEquals( q, p );
		}
	}
	
	@Test
	public void testInvertBigPermutation() {
		for( int k = 10; k-- != 0; ) {
			final long[][] p = Util.identity( k * 10L );
			LongBigArrays.shuffle( p, new SplitMix64Random( 0 ) );
			long[][] q = Util.invertPermutation( p );
			q = Util.invertPermutation( q );
			assertArrayEquals( q, p );
		}
	}
	
	@Test
	public void testBigInvertPermutationInPlace() {
		assertArrayEquals( new long[][] { { 0, 1, 2 } }, Util.invertPermutationInPlace( new long[][] { { 0, 1, 2 } } ) );
		assertArrayEquals( new long[][] { { 1, 0 } }, Util.invertPermutationInPlace( new long[][] { { 1, 0 } } ) );
		assertArrayEquals( new long[][] { { 0, 2, 1 } }, Util.invertPermutationInPlace( new long[][] { { 0, 2, 1 } } ) );
		assertArrayEquals( new long[][] { { 3, 0, 1, 2 } }, Util.invertPermutationInPlace( new long[][] { { 1, 2, 3, 0 } } ) );
		
		for( int k = 10; k-- != 0; ) {
			final long[][] p = Util.identity( k * 10L );
			LongBigArrays.shuffle( p, new SplitMix64Random( 0 ) );
			final long[][] q = Util.invertPermutation( p );
			Util.invertPermutationInPlace( p );
			assertArrayEquals( q, p );
		}
	}
}
