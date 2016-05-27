package it.unimi.dsi.io;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2016 Sebastiano Vigna 
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

import it.unimi.dsi.big.io.FileLinesByteArrayCollection;
import it.unimi.dsi.big.io.FileLinesByteArrayCollection.FileLinesIterator;
import it.unimi.dsi.fastutil.io.BinIO;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileLinesByteArrayCollectionTest {

	@Test 
	public void test() throws IOException {
		File file = File.createTempFile( FastBufferedReaderTest.class.getSimpleName(), "tmp" );
		file.deleteOnExit();

		byte[] a = { '0', '\n', '1', '\n' };
		BinIO.storeBytes( a, file );  
		FileLinesByteArrayCollection flbac = new FileLinesByteArrayCollection( file.toString() );
		FileLinesIterator iterator = flbac.iterator();
		assertArrayEquals( new byte[] { '0' }, iterator.next() );
		assertArrayEquals( new byte[] { '1' }, iterator.next() );
		assertFalse( iterator.hasNext() );
		assertEquals( 2, flbac.size64() );

		a = new byte[] { '0', '\n', '1' };
		BinIO.storeBytes( a, file );  
		flbac = new FileLinesByteArrayCollection( file.toString() );
		assertEquals( 2, flbac.size64() );
		iterator = flbac.iterator();
		assertArrayEquals( new byte[] { '0' }, iterator.next() );
		assertTrue( iterator.hasNext() );
		assertArrayEquals( new byte[] { '1' }, iterator.next() );
		assertFalse( iterator.hasNext() );
		assertFalse( iterator.hasNext() );
		iterator.close();
		
		a = new byte[ 1000000 ];
		Arrays.fill( a, (byte)'A' );
		BinIO.storeBytes( a, file );  
		flbac = new FileLinesByteArrayCollection( file.toString() );
		assertEquals( 1, flbac.size64() );
		iterator = flbac.iterator();
		assertArrayEquals( a, iterator.next() );
		assertFalse( iterator.hasNext() );

		file.delete();
	}
}
