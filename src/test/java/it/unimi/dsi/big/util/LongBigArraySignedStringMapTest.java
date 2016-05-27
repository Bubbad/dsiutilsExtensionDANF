package it.unimi.dsi.big.util;

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

import static org.junit.Assert.assertEquals;
import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.longs.LongBigArrayBigList;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class LongBigArraySignedStringMapTest {
	
	@Test
	public void testNumbers() throws IOException {
		
		for( int width = 16; width <= Long.SIZE; width += 8 ) {
			String[] s = new String[ 100000 ];
			long[] v = new long[ s.length ];
			for( int i = s.length; i-- != 0; ) s[ (int)( v[ i ] = i  )] = Integer.toString( i );

			// Test with mph
			Object2LongOpenHashMap<String> mph = new Object2LongOpenHashMap<String>( s, v );
			final long[][] signatures = LongBigListSignedStringMap.sign( Arrays.asList( s ).iterator(), mph );
			
			LongBigListSignedStringMap map = new LongBigListSignedStringMap( mph, LongBigArrayBigList.wrap( signatures ) );

			for( int i = s.length; i-- != 0; ) assertEquals( i, map.getLong( Integer.toString( i ) ) );
			for( int i = s.length + 100; i-- != s.length; ) assertEquals( -1, map.getLong( Integer.toString( i ) ) );

			File temp = File.createTempFile( getClass().getSimpleName(), "test" );
			temp.deleteOnExit();

			BinIO.storeLongs( signatures, temp );
			map = new LongBigListSignedStringMap( mph, temp.toString() );

			for( int i = s.length; i-- != 0; ) assertEquals( i, map.getLong( Integer.toString( i ) ) );
			for( int i = s.length + 10000; i-- != s.length; ) assertEquals( -1, map.getLong( Integer.toString( i ) ) );
			
			temp.delete();
		
		}
	}		

	@Test
	public void testSortedNumbers() throws IOException {
		
		for( int width = 16; width <= Long.SIZE; width += 8 ) {
			String[] s = new String[ 100000 ];
			long[] v = new long[ s.length ];
			for( int i = s.length; i-- != 0; ) s[ (int)( v[ i ] = i  )] = Integer.toString( i );

			// Test with mph
			Object2LongOpenHashMap<String> mph = new Object2LongOpenHashMap<String>( s, v );

			File temp = File.createTempFile( getClass().getSimpleName(), "test" );
			temp.deleteOnExit();

			LongBigListSignedStringMap.sign( Arrays.asList( s ).iterator(), temp.toString() );
			LongBigListSignedStringMap map = new LongBigListSignedStringMap( mph, temp.toString() );

			for( int i = s.length; i-- != 0; ) assertEquals( i, map.getLong( Integer.toString( i ) ) );
			for( int i = s.length + 10000; i-- != s.length; ) assertEquals( -1, map.getLong( Integer.toString( i ) ) );
			
			temp.delete();
		
		}
	}		
}
