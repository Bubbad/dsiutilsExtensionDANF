package it.unimi.dsi.io;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2011-2016 Sebastiano Vigna 
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
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import it.unimi.dsi.util.SplitMix64Random;

import java.io.IOException;

import org.junit.Test;

@SuppressWarnings("resource")
public class OutputBitStreamTest {

	@Test
	public void testPositionWrapped() throws IOException {
		final byte[] a = new byte[ 2 ];
		OutputBitStream obs = new OutputBitStream( a );
		obs.position( 8 );
		obs.writeInt( 1, 8 );
		assertArrayEquals( new byte[] { 0, 1 }, a );
		obs.position( 0 );
		obs.writeInt( 1, 1 );
		obs.flush();
		assertArrayEquals( new byte[] { -128, 1 }, a );
	}

	@Test(expected=IllegalArgumentException.class)
	public void testPositionUnaligned() throws IOException {
		final byte[] a = new byte[ 2 ];
		OutputBitStream obs = new OutputBitStream( a );
		obs.position( 1 );
	}
	
	@Test
	public void testWriteGammas() throws IOException {
		final FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		final OutputBitStream obs = new OutputBitStream( fbaos );
		SplitMix64Random random = new SplitMix64Random( 0 );
		for( int i = 0; i < 100; i++ ) {
			final int l = random.nextInt( 20 );
			final int[] a = new int[ l + 5 ];
			for( int p = 0; p < l; p++ ) a[ p ] = random.nextInt( 10000 );
			final long writtenBits = obs.writtenBits();
			final long length = obs.writeGammas( a, l );
			assertEquals( obs.writtenBits() - writtenBits, length );
		}
		
		obs.flush();
		final InputBitStream ibs = new InputBitStream( fbaos.array );
		random = new SplitMix64Random( 0 );
		for( int i = 0; i < 100; i++ ) {
			final int l = random.nextInt( 20 );
			final int[] a = new int[ l ];
			ibs.readGammas( a, l );
			for( int p = 0; p < l; p++ ) assertEquals( random.nextInt( 10000 ), a[ p ] );
		}
	}

	@Test
	public void testWriteShiftedGammas() throws IOException {
		final FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		final OutputBitStream obs = new OutputBitStream( fbaos );
		SplitMix64Random random = new SplitMix64Random( 0 );
		for( int i = 0; i < 100; i++ ) {
			final int l = random.nextInt( 20 );
			final int[] a = new int[ l + 5 ];
			for( int p = 0; p < l; p++ ) a[ p ] = random.nextInt( 10000 );
			final long writtenBits = obs.writtenBits();
			final long length = obs.writeShiftedGammas( a, l );
			assertEquals( obs.writtenBits() - writtenBits, length );
		}
		
		obs.flush();
		final InputBitStream ibs = new InputBitStream( fbaos.array );
		random = new SplitMix64Random( 0 );
		for( int i = 0; i < 100; i++ ) {
			final int l = random.nextInt( 20 );
			final int[] a = new int[ l ];
			ibs.readShiftedGammas( a, l );
			for( int p = 0; p < l; p++ ) assertEquals( random.nextInt( 10000 ), a[ p ] );
		}
	}

	@Test
	public void testWriteDeltas() throws IOException {
		final FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		final OutputBitStream obs = new OutputBitStream( fbaos );
		SplitMix64Random random = new SplitMix64Random( 0 );
		for( int i = 0; i < 100; i++ ) {
			final int l = random.nextInt( 20 );
			final int[] a = new int[ l + 5 ];
			for( int p = 0; p < l; p++ ) a[ p ] = random.nextInt( 10000 );
			final long writtenBits = obs.writtenBits();
			final long length = obs.writeDeltas( a, l );
			assertEquals( obs.writtenBits() - writtenBits, length );
		}
		
		obs.flush();
		final InputBitStream ibs = new InputBitStream( fbaos.array );
		random = new SplitMix64Random( 0 );
		for( int i = 0; i < 100; i++ ) {
			final int l = random.nextInt( 20 );
			final int[] a = new int[ l ];
			ibs.readDeltas( a, l );
			for( int p = 0; p < l; p++ ) assertEquals( random.nextInt( 10000 ), a[ p ] );
		}
	}
}
