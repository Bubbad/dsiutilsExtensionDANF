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
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class SegmentedInputStreamTest {

	private FastByteArrayInputStream stream = new FastByteArrayInputStream(
			new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 }
	);
	
	private SegmentedInputStream sis;
	
	@Before
	public void setUp() throws IllegalArgumentException, IOException {
		sis = new SegmentedInputStream( stream );
		sis.addBlock( 0, 1, 2 );
		sis.addBlock( 2, 3, 4 );
		sis.addBlock( 6, 7, 8 );
		sis.addBlock( 8, 11, 14 );
	}
	
	@Test
	public void testResetClose() throws IOException {
		assertEquals( 0, sis.read() );
		sis.reset();
		assertEquals( 1, sis.read() );
		sis.reset();
		assertEquals( -1, sis.read() );

		sis.close();
		assertEquals( 2, sis.read() );
		sis.reset();
		assertEquals( 3, sis.read() );
		sis.reset();
		assertEquals( -1, sis.read() );

		sis.close();
		assertEquals( 6, sis.read() );
		sis.reset();
		assertEquals( 7, sis.read() );
		sis.reset();
		assertEquals( -1, sis.read() );
	}
	
	@Test
	public void testRead() throws IOException {
		final byte[] b = new byte[ 11 ];
		assertEquals( 1, sis.read( b, 0, 10 ) );
		assertEquals( 0, b[ 0 ] );
		sis.reset();
		assertEquals( 1, sis.read( b, 1, 10 ) );
		assertEquals( 1, b[ 1 ] );
		
		sis.close();
		assertEquals( 1, sis.read( b, 5, 5 ) );
		assertEquals( 2, b[ 5 ] );
	}

	@Test
	public void testSkip() throws IOException {
		assertEquals( 1, sis.skip( 1 ) );
		sis.reset();
		assertEquals( 1, sis.skip( 10 ) );
		sis.reset();
		assertEquals( 0, sis.skip( 10 ) );
		
		sis.close();
		sis.close();
		sis.close();

		assertEquals( 2, sis.skip( 2 ) );
		assertEquals( 1, sis.skip( 2 ) );
		sis.reset();
		assertEquals( 3, sis.skip( 10 ) );
		
	}
}
