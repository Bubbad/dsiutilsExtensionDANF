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
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.objects.AbstractObject2LongFunction;
import it.unimi.dsi.fastutil.objects.AbstractObjectBigList;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class ShiftAddXorSignedStringMapTest {
	
	@Test
	public void testNumbers() throws IOException, ClassNotFoundException {
		
		for( int width = 16; width <= Long.SIZE; width += 8 ) {
			String[] s = new String[ 1000 ];
			long[] v = new long[ s.length ];
			for( int i = s.length; i-- != 0; ) s[ (int)( v[ i ] = i  )] = Integer.toString( i );

			// Test with mph
			Object2LongOpenHashMap<String> mph = new Object2LongOpenHashMap<String>( s, v );
			ShiftAddXorSignedStringMap map = new ShiftAddXorSignedStringMap( Arrays.asList( s ).iterator(), mph, width );

			for( int i = s.length; i-- != 0; ) assertEquals( i, map.getLong( Integer.toString( i ) ) );
			for( int i = s.length + 100; i-- != s.length; ) assertEquals( -1, map.getLong( Integer.toString( i ) ) );

			File temp = File.createTempFile( getClass().getSimpleName(), "test" );
			temp.deleteOnExit();
			BinIO.storeObject( map, temp );
			map = (ShiftAddXorSignedStringMap)BinIO.loadObject( temp );

			for( int i = s.length; i-- != 0; ) assertEquals( i, map.getLong( Integer.toString( i ) ) );
			for( int i = s.length + 100; i-- != s.length; ) assertEquals( -1, map.getLong( Integer.toString( i ) ) );
		
		}
	}

	private final class LargeFunction extends AbstractObject2LongFunction<String> implements Size64 {
		private static final long serialVersionUID = 1L;

		@Override
		public long getLong( Object key ) {
			try {
				final long l = Long.parseLong( (String)key );
				return l < 1L << 31 ? l : -1;
			}
			catch( Exception e ) {
				return -1;
			}
		}

		@Override
		public boolean containsKey( Object key ) {
			try {
				final long l = Long.parseLong( (String)key );
				return l < 1L << 31;
			}
			catch( Exception e ) {
				return false;
			}
		}

		@Override
		public int size() {
			return Integer.MAX_VALUE;
		}
		
		public long size64() {
			return 1L << 31;
		}
	}

	@Test
	public void testLarge() {
		new ShiftAddXorSignedStringMap( new AbstractObjectBigList<String>() {

			@Override
			public String get( long index ) {
				return Long.toString( index );
			}

			@Override
			public long size64() {
				return 1L << 31;
			}
		}.iterator(), new LargeFunction(), 1 );
	}
		
}
