package it.unimi.dsi.util;

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
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.hash.Funnels;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;

public class BloomFilterTest {
	
	@Test
	public void testAdd() {
		BloomFilter<Void> bloomFilter = BloomFilter.create( 10, 20 ); // High precision 
		assertTrue( bloomFilter.add( "test" ) );
		assertFalse( bloomFilter.add( "test" ) );
		assertTrue( bloomFilter.add( "foo" ) );
		assertTrue( bloomFilter.add( "bar" ) );
		assertEquals( 3, bloomFilter.size64() );

		assertTrue( bloomFilter.contains( "test" ) );
		assertTrue( bloomFilter.contains( "foo" ) );
		assertTrue( bloomFilter.contains( "bar" ) );

		assertFalse( bloomFilter.contains( "42" ) );
		assertFalse( bloomFilter.contains( "42" ) );

		bloomFilter.clear();
		assertTrue( bloomFilter.add( new byte[] { 0, 1 } ) );
		assertFalse( bloomFilter.add( new byte[] { 0, 1 } ) );
		assertTrue( bloomFilter.add( new byte[] { 1, 2 } ) );
		assertTrue( bloomFilter.add( new byte[] { 1, 0 } ) );
		assertEquals( 3, bloomFilter.size64() );
	}

	@Test
	public void testConflictsStrings() {
		BloomFilter<Void> bloomFilter = BloomFilter.create( 1000, 8 ); 
		LongOpenHashSet longs = new LongOpenHashSet();
		SplitMix64RandomGenerator random = new SplitMix64RandomGenerator( 1 );
		
		for( int i = 1000; i-- != 0; ) {
			final long l = random.nextLong();
			longs.add( l );
			final String s = Long.toBinaryString( l );
			bloomFilter.add( s );
			assertTrue( bloomFilter.contains( s ) );
		}
		
		assertEquals( longs.size(), bloomFilter.size64() );
	}

	@Test
	public void testConflictsLongStrings() {
		BloomFilter<Void> bloomFilter = BloomFilter.create( 1000, 8 );
		ObjectOpenHashSet<String> strings = new ObjectOpenHashSet<String>();
		SplitMix64RandomGenerator random = new SplitMix64RandomGenerator( 2 );
		
		for( int i = 1000; i-- != 0; ) {
			StringBuilder s = new StringBuilder();
			for( int j = 0; j < 100; j++ ) s.append( Long.toBinaryString( random.nextLong() ) );
			strings.add( s.toString() );
			bloomFilter.add( s );
			assertTrue( bloomFilter.contains( s ) );
		}
		
		assertEquals( strings.size(), bloomFilter.size64() );
	}

	@Test
	public void testConflictsLongs() {
		BloomFilter<Long> bloomFilter = new BloomFilter<Long>( 1000, 8, Funnels.longFunnel() ); 
		LongOpenHashSet longs = new LongOpenHashSet();
		SplitMix64RandomGenerator random = new SplitMix64RandomGenerator( 2 );
		
		for( int i = 1000; i-- != 0; ) {
			final long l = random.nextLong();
			longs.add( l );
			final Long o = Long.valueOf( l );
			bloomFilter.add( o );
			assertTrue( bloomFilter.contains( o ) );
		}
		
		assertEquals( longs.size(), bloomFilter.size64() );
	}

	@Test
	public void testConflictsByteArrays() {
		BloomFilter<Void> bloomFilter = BloomFilter.create( 1000, 8 ); 
		LongOpenHashSet longs = new LongOpenHashSet();
		SplitMix64RandomGenerator random = new SplitMix64RandomGenerator( 4 );
		
		for( int i = 1000; i-- != 0; ) {
			final long l = random.nextLong();
			longs.add( l );
			final byte[] o = Longs.toByteArray( l );
			bloomFilter.add( o );
			assertTrue( bloomFilter.contains( o ) );
		}
		
		assertEquals( longs.size(), bloomFilter.size64() );
	}

	@Test
	public void testConflictsHashes() {
		BloomFilter<Void> bloomFilter = BloomFilter.create( 1000, 8 ); 
		LongOpenHashSet longs = new LongOpenHashSet();
		SplitMix64RandomGenerator random = new SplitMix64RandomGenerator( 1 );
		
		for( int i = 1000; i-- != 0; ) {
			final long l = random.nextLong();
			final long m = random.nextLong();
			longs.add( l ^ m );
			final byte[] o = Bytes.concat( Longs.toByteArray( l ), Longs.toByteArray( m ) );
			bloomFilter.addHash( o );
			assertTrue( bloomFilter.containsHash( o ) );
		}
		
		assertEquals( longs.size(), bloomFilter.size64() );
	}

	@Test
	public void testNegativeSeed() {
		BloomFilter<Long> bloomFilter = new BloomFilter<Long>( 1000, 8, Funnels.longFunnel() );
		assertTrue( bloomFilter.add( "test" ) );
		assertFalse( bloomFilter.add( "test" ) );
	}

	@Ignore
	@Test
	public void testConflictsBig() {
		BloomFilter<Long> bloomFilter = new BloomFilter<Long>( 1000000000, 30, Funnels.longFunnel() );
		LongOpenHashSet longs = new LongOpenHashSet();
		SplitMix64RandomGenerator random = new SplitMix64RandomGenerator( 5 );
		
		for( int i = 10000000; i-- != 0; ) {
			final long l = random.nextLong();
			longs.add( l );
			final Long o = Long.valueOf( l );
			bloomFilter.add( o );
				}
		
		assertEquals( longs.size(), bloomFilter.size64() );
	}

	@Test
	public void testZeroFunctions() {
		BloomFilter<Void> bloomFilter = BloomFilter.create( 10, 0 ); 
		assertFalse( bloomFilter.add( "test" ) );
		assertEquals( 0, bloomFilter.size64() );
	}
}
