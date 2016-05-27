package it.unimi.dsi.io;

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
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.util.XorShift128PlusRandom;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

@SuppressWarnings("resource")
public class InputBitStreamTest {

	@Test
	public void testReadAligned() throws IOException {
		byte[] a = { 1 }, A = new byte[ 1 ];
		new InputBitStream( a ).read( A, 8 );
		assertTrue( Arrays.toString( a ) + " != " + Arrays.toString( A ), Arrays.equals( a, A ) );
		byte[] b = { 1, 2 }, B = new byte[ 2 ];
		new InputBitStream( b ).read( B, 16 );
		assertTrue( Arrays.toString( b ) + " != " + Arrays.toString( B ), Arrays.equals( b, B ) );
		byte[] c = { 1, 2, 3 }, C = new byte[ 3 ];
		new InputBitStream( c ).read( C, 24 );
		assertTrue( Arrays.toString( c ) + " != " + Arrays.toString( C ), Arrays.equals( c, C ) );
	}
	
	@Test
	public void testOverflow() throws IOException {
		InputBitStream ibs = new InputBitStream( new byte[ 0 ] );
		ibs.readInt( 0 );
	}
	
	@Test
	public void testPosition() throws IOException {
		InputBitStream ibs = new InputBitStream( new byte[ 100 ] );
		for( int i = 0; i < 800; i++ ) {
			ibs.position( i );
			assertEquals( i, ibs.position() );
		}
		for( int i = 800; i-- != 0; ) {
			ibs.position( i );
			assertEquals( i, ibs.position() );
		}
	}
	
	@Test
	public void readWriteLongs() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Random random = new Random( 0 );
		File tempFile = File.createTempFile( "readWriteLongs", "os" );
		tempFile.deleteOnExit();

		String[] oneArgs = { "LongGamma", "LongShiftedGamma", "LongDelta", "LongNibble" };
		String[] twoArgsLong = { "LongGolomb", "LongSkewedGolomb" };
		
		LongArrayList longs = new LongArrayList();
		for ( int i = 0; i < 10; i++ ) longs.add( 5000000000L + random.nextInt() );
		//for ( int i = 24; i < 60; i++ ) longs.add( 1L << i );
		LongArrayList extraArgLong = new LongArrayList();
		for ( int i = 0; i < 5; i++ ) extraArgLong.add( 100000 + random.nextInt( 1000000 ) );
		
		// Write
		OutputBitStream obs = new OutputBitStream( tempFile );
		
		for ( String methSuffix: oneArgs ) {
			for ( long longValue: longs )
				OutputBitStream.class.getMethod( "write" + methSuffix, long.class ).invoke( obs, Long.valueOf( longValue ) );
		}
		for ( String methSuffix: twoArgsLong ) {
			for ( long longValue: longs )
				for ( long longXValue: extraArgLong )
				OutputBitStream.class.getMethod( "write" + methSuffix, long.class, long.class ).invoke( obs, Long.valueOf( longValue ), Long.valueOf( longXValue ) );
		}
		// Special methods
		for ( long longValue: longs ) {
			obs.writeLong( longValue, Long.SIZE );
			obs.writeLongMinimalBinary( longValue, longValue + 5 );
			for ( int i = 3; i < 10; i++) obs.writeLongZeta( longValue, i );
		}
		obs.writeLongUnary( 15 + 1L << 20 );
		
		obs.close();
		
		// Read
		InputBitStream ibs = new InputBitStream( tempFile );

		for ( String methSuffix: oneArgs ) {
			for ( long longValue: longs )
				assertEquals( Long.valueOf( longValue ), InputBitStream.class.getMethod( "read" + methSuffix ).invoke( ibs ) );
		}
		for ( String methSuffix: twoArgsLong ) {
			for ( long longValue: longs )
				for ( long longXValue: extraArgLong )
					assertEquals( Long.valueOf( longValue ), InputBitStream.class.getMethod( "read" + methSuffix, long.class ).invoke( ibs, Long.valueOf( longXValue ) ) );
		}
		for ( long longValue: longs ) {
			assertEquals( longValue, ibs.readLong( Long.SIZE ) );
			assertEquals( longValue, ibs.readLongMinimalBinary( longValue + 5 ) );
			for ( int i = 3; i < 10; i++) assertEquals( longValue, ibs.readLongZeta( i ) );
		}
		assertEquals( 15 + 1L << 20, ibs.readLongUnary() );

		ibs.close();
		
	}
	
	@Test
	public void testUnary() throws IOException {
		final XorShift128PlusRandom random = new XorShift128PlusRandom( 0 );
		final FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		final OutputBitStream obs = new OutputBitStream( fbaos );
		for( int i = 0; i < 100000000; i++ ) obs.writeUnary( Long.numberOfTrailingZeros( random.nextLong() ) );
		obs.flush();
		final InputBitStream ibs = new InputBitStream( fbaos.array );
		random.setSeed( 0 );
		long start = - System.nanoTime();
		for( int i = 0; i < 100000000; i++ ) assertEquals( Long.numberOfTrailingZeros( random.nextLong() ), ibs.readUnary() );
		System.err.println( ( start + System.nanoTime() ) / 1E9 );
	}
	
	@Test
	public void testLongUnary() throws IOException {
		final XorShift128PlusRandom random = new XorShift128PlusRandom( 0 );
		final FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		final OutputBitStream obs = new OutputBitStream( fbaos );
		for( int i = 0; i < 100000000; i++ ) obs.writeLongUnary( Long.numberOfTrailingZeros( random.nextLong() ) );
		obs.flush();
		final InputBitStream ibs = new InputBitStream( fbaos.array );
		random.setSeed( 0 );
		long start = - System.nanoTime();
		for( int i = 0; i < 100000000; i++ ) assertEquals( Long.numberOfTrailingZeros( random.nextLong() ), ibs.readLongUnary() );
		System.err.println( ( start + System.nanoTime() ) / 1E9 );
	}
	
	public static void main( String arg[] ) throws IOException {
		final XorShift128PlusRandom random = new XorShift128PlusRandom( 0 );
		final FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		final OutputBitStream obs = new OutputBitStream( fbaos );
		for( int i = 0; i < 100000000; i++ ) obs.writeUnary( Long.numberOfTrailingZeros( random.nextLong() ) );
		obs.flush();
		final InputBitStream ibs = new InputBitStream( fbaos.array );
		long start = - System.nanoTime();
		for( int i = 0; i < 100000000; i++ ) ibs.readUnary();
		System.err.println( ( start + System.nanoTime() ) / 1E9 );

		ibs.position( 0 );
		start = - System.nanoTime();
		for( int i = 0; i < 100000000; i++ ) ibs.readUnary();
		System.err.println( ( start + System.nanoTime() ) / 1E9 );

		ibs.position( 0 );
		start = - System.nanoTime();
		for( int i = 0; i < 100000000; i++ ) ibs.readUnary();
		System.err.println( ( start + System.nanoTime() ) / 1E9 );
	}
}
