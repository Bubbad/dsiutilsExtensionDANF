package it.unimi.dsi.compression;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2007-2016 Sebastiano Vigna 
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
import it.unimi.dsi.bits.BitVector;
import it.unimi.dsi.bits.LongArrayBitVector;
import it.unimi.dsi.util.SplitMix64Random;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

public class HuffmanCodecTest extends CodecTestCase {
	@Test
	public void testZeroSymbols() {
		HuffmanCodec codec = new HuffmanCodec( new int[ 0 ] );
		assertEquals( 0, codec.codeWords().length );
		long seed = System.currentTimeMillis();
		System.err.println( seed );
	}
	
	@Test
	public void testOneSymbol() throws IOException {
		HuffmanCodec codec = new HuffmanCodec( new int[] { 1 } );
		assertEquals( 1, codec.codeWords().length );
		assertEquals( LongArrayBitVector.ofLength( 0 ), codec.codeWords()[ 0 ] );
		long seed = System.currentTimeMillis();
		System.err.println( seed );
		Random r = new SplitMix64Random( seed );
		checkPrefixCodec( codec, r );
	}

	@Test
	public void testTwoEquiprobableSymbols() throws IOException {
		HuffmanCodec codec = new HuffmanCodec( new int[] { 1, 1 } );
		assertEquals( 2, codec.codeWords().length );
		BitVector v = LongArrayBitVector.ofLength( 1 );
		assertEquals( v, codec.codeWords()[ 0 ] );
		v.set( 0 );
		assertEquals( v, codec.codeWords()[ 1 ] );
		long seed = System.currentTimeMillis();
		System.err.println( seed );
		Random r = new SplitMix64Random( seed );
		checkPrefixCodec( codec, r );
	}

	@Test
	public void testThreeNonequiprobableSymbols() throws IOException {
		HuffmanCodec codec = new HuffmanCodec( new int[] { 1, 2, 4 } );
		assertEquals( 3, codec.codeWords().length );
		BitVector v = LongArrayBitVector.ofLength( 2 );
		v.set( 0 );
		assertEquals( v, codec.codeWords()[ 1 ] );
		v.set( 1 );
		assertEquals( v, codec.codeWords()[ 0 ] );
		assertEquals( LongArrayBitVector.ofLength( 1 ), codec.codeWords()[ 2 ] );
		long seed = System.currentTimeMillis();
		System.err.println( seed );
		Random r = new SplitMix64Random( seed );
		checkPrefixCodec( codec, r );
	}

	@Test
	public void testJumpInLengths() throws IOException {
		HuffmanCodec codec = new HuffmanCodec( new int[] { 10, 1, 1, 1, 1 } );
		long seed = System.currentTimeMillis();
		System.err.println( seed );
		Random r = new SplitMix64Random( seed );
		checkPrefixCodec( codec, r );
	}


	@Test
	public void testRandomFrequencies() throws IOException {
		long seed = System.currentTimeMillis();
		System.err.println( seed );
		Random r = new SplitMix64Random( seed );
		int[] frequency = new int[ 100 ];
		for( int i = 0; i < frequency.length; i++ ) frequency[ i ] = r.nextInt( 1000 ); 
		HuffmanCodec codec = new HuffmanCodec( frequency );
		checkPrefixCodec( codec, r );
	}

	@Test
	public void testRandomCodeLengths() throws IOException {
		int[] frequency = { 805, 1335, 6401, 7156, 7333, 10613, 10951, 11708, 12710, 12948, 13237, 13976, 20355, 20909, 22398, 26303, 26400, 28380, 28865, 30152, 31693, };
		int[] codeLength = { 7, 7, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 3, 3 };
		HuffmanCodec codec = new HuffmanCodec( frequency );
		checkLengths( frequency, codeLength, codec.codeWords() );
		checkPrefixCodec( codec, new SplitMix64Random( 1 ) );
	}

	@Test
	public void testExponentialCodeLengths() throws IOException {
		int[] frequency = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824 };
		int[] codeLength = { 30, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
		HuffmanCodec codec = new HuffmanCodec( frequency );
		checkLengths( frequency, codeLength, codec.codeWords() );
		checkPrefixCodec( codec, new SplitMix64Random( 1 ) );
	}

	@Test
	public void testFibonacciCodeLengths() throws IOException {
		int[] frequency = new int[ 44 ];
		frequency[ 0 ] = frequency[ 1 ] = 1;
		for( int i = 2; i < frequency.length; i++ ) frequency[ i ] = frequency[ i - 1 ] + frequency[ i - 2 ];
		int[] codeLength = { 43, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
		HuffmanCodec codec = new HuffmanCodec( frequency );
		checkLengths( frequency, codeLength, codec.codeWords() );
		checkPrefixCodec( codec, new SplitMix64Random( 1 ) );
	}
}
