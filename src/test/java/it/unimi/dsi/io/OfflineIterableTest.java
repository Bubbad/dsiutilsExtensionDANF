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
import static org.junit.Assert.assertFalse;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.lang.MutableString;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class OfflineIterableTest {
	public void doIt( String[] strings ) throws IOException {
		OfflineIterable.Serializer<MutableString,MutableString> stringSerializer = new OfflineIterable.Serializer<MutableString,MutableString>() {
			@Override
			public void read( DataInput dis, MutableString x ) throws IOException {
				x.readSelfDelimUTF8( (InputStream)dis );
			}
			@Override
			public void write( MutableString x, DataOutput dos ) throws IOException {
				x.writeSelfDelimUTF8( (OutputStream)dos );
			}
		};
		OfflineIterable<MutableString,MutableString> stringIterable = new OfflineIterable<MutableString,MutableString>( stringSerializer, new MutableString() );
		for ( String s: strings ) 
			stringIterable.add( new MutableString( s ) );
		ObjectIterator<String> shouldBe = ObjectIterators.wrap( strings );
		for ( MutableString m: stringIterable ) 
			assertEquals( new MutableString( shouldBe.next() ), m );
		assertFalse( shouldBe.hasNext() );

		// Let's do it again.
		stringIterable.clear();
		for ( String s: strings ) 
			stringIterable.add( new MutableString( s ) );
		shouldBe = ObjectIterators.wrap( strings );
		for ( MutableString m: stringIterable ) 
			assertEquals( new MutableString( shouldBe.next() ), m );
		assertFalse( shouldBe.hasNext() );
		
		stringIterable.close();
		stringIterable.close(); // Twice, to test for safety
	}
	
	@Test
	public void testSimple() throws IOException {
		doIt( new String[] { "this", "is", "a", "test" } );
	}
	
	@Test
	public void testEmpty() throws IOException {
		doIt( new String[ 0 ] );
	}	
}
