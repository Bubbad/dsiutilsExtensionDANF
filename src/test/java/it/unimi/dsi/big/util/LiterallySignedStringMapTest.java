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
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.objects.AbstractObject2LongFunction;
import it.unimi.dsi.fastutil.objects.AbstractObjectBigList;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectBigLists;
import it.unimi.dsi.lang.MutableString;
import it.unimi.dsi.util.FrontCodedStringList;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class LiterallySignedStringMapTest {
	
	private final static class CharSequenceStrategy implements Hash.Strategy<CharSequence>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean equals( CharSequence a, CharSequence b ) {
			if ( a == null ) return b == null;
			if ( b == null ) return false;
			return a.toString().equals( b.toString() );
		}

		@Override
		public int hashCode( CharSequence o ) {
			return o.toString().hashCode();
		}
	}

	@Test
	public void testNumbers() throws IOException, ClassNotFoundException {
		for( int n = 10; n < 10000; n *= 10 ) {
			String[] s = new String[ n ];
			for( int i = s.length; i-- != 0; ) s[ i ] = Integer.toString( i );
			Collections.shuffle( Arrays.asList( s ) );

			FrontCodedStringList fcl = new FrontCodedStringList( Arrays.asList( s ), 8, true );
			// Test with mph
			Object2LongOpenCustomHashMap<CharSequence> mph = new Object2LongOpenCustomHashMap<CharSequence>( new CharSequenceStrategy());
			mph.defaultReturnValue( -1 );
			for( int i = 0; i < s.length; i++ ) mph.put( new MutableString( s[ i ] ),  i );
			
			LiterallySignedStringMap map = new LiterallySignedStringMap( mph, ObjectBigLists.asBigList( fcl ) );

			for( int i = s.length; i-- != 0; ) assertEquals( i, map.getLong( s[ i ] ) );
			for( int i = s.length + n; i-- != s.length; ) assertEquals( -1, map.getLong( Integer.toString( i ) ) );

			File temp = File.createTempFile( getClass().getSimpleName(), "test" );
			temp.deleteOnExit();
			BinIO.storeObject( map, temp );
			map = (LiterallySignedStringMap)BinIO.loadObject( temp );

			for( int i = s.length; i-- != 0; ) assertEquals( i, map.getLong( s[ i ] ) );
			for( int i = s.length + n; i-- != s.length; ) assertEquals( -1, map.getLong( Integer.toString( i ) ) );
		}
	}

	private final class LargeFunction extends AbstractObject2LongFunction<String> implements Size64 {
		private static final long serialVersionUID = 1L;

		@Override
		public long getLong( Object key ) {
			try {
				final long l = Long.parseLong( key.toString() );
				return l < 1L << 31 ? l : -1;
			}
			catch( Exception e ) {
				return -1;
			}
		}

		@Override
		public boolean containsKey( Object key ) {
			try {
				final long l = Long.parseLong( key.toString() );
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
		new LiterallySignedStringMap( new LargeFunction(), new AbstractObjectBigList<MutableString>() {

			@Override
			public MutableString get( long index ) {
				return new MutableString( Long.toString( index ) );
			}

			@Override
			public long size64() {
				return 1L << 31;
			}
		} );
	}
}
