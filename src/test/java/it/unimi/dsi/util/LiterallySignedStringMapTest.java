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
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.lang.MutableString;

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
			
			LiterallySignedStringMap map = new LiterallySignedStringMap( mph, fcl );

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
}
