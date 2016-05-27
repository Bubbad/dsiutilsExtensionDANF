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
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import it.unimi.dsi.io.OfflineIterable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

public class BitVectorsTest {
	
	@Test
	public void testReadWriteFast() throws IOException {
		final FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream( fbaos );
		final LongArrayBitVector labv = LongArrayBitVector.getInstance();
		final BitVector[] a = new BitVector[] { BitVectors.ZERO, BitVectors.ONE, BitVectors.EMPTY_VECTOR, 
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAAL }, 64 ),
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAL }, 60 ),
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAAL, 0xAAAAAAAAAAAAAAAAL }, 128 ),
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAAL, 0xAAAAAAAAAAAAAAAL }, 124 ) };
		
		for( BitVector bv: a ) { 
			BitVectors.writeFast( bv, dos );
			dos.close();
			assertEquals( bv, BitVectors.readFast( new DataInputStream( new FastByteArrayInputStream( fbaos.array ) ) ) );
			fbaos.reset();
		}
		
		for( BitVector bv: a ) { 
			BitVectors.writeFast( bv, dos );
			dos.close();
			assertEquals( bv, BitVectors.readFast( new DataInputStream( new FastByteArrayInputStream( fbaos.array ) ), labv ) );
			fbaos.reset();
		}
	}

	@Test
	public void testMakeOffline() throws IOException {
		final BitVector[] a = new BitVector[] { BitVectors.ZERO, BitVectors.ONE, BitVectors.EMPTY_VECTOR, 
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAAL }, 64 ),
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAL }, 60 ),
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAAL, 0xAAAAAAAAAAAAAAAAL }, 128 ),
				LongArrayBitVector.wrap( new long[] { 0xAAAAAAAAAAAAAAAAL, 0xAAAAAAAAAAAAAAAL }, 124 ) };

		OfflineIterable<BitVector,LongArrayBitVector> iterable = new OfflineIterable<BitVector, LongArrayBitVector>( BitVectors.OFFLINE_SERIALIZER, LongArrayBitVector.getInstance() );
		iterable.addAll( Arrays.asList( a ) );
		
		Iterator<LongArrayBitVector> iterator = iterable.iterator();
		for( int i = 0; i < a.length; i++ ) assertEquals( a[ i ], iterator.next() );
		assertFalse( iterator.hasNext() );
		iterable.close();
	}
}
