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
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.io.InputBitStream;
import it.unimi.dsi.io.OutputBitStream;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Fabien Campagne
 * @author Sebastiano Vigna
 */
public class SemiExternalGammaBigListTest {

	private static InputBitStream buildInputStream( LongList longs ) throws IOException {
		byte[] array = new byte[ longs.size() * 4 ];
		@SuppressWarnings("resource")
		OutputBitStream streamer = new OutputBitStream( array );
		for ( int i = 0; i < longs.size(); i++ ) streamer.writeLongGamma( longs.getLong( i ) );
		int size = (int)( streamer.writtenBits() / 8 ) + ( ( streamer.writtenBits() % 8 ) == 0 ? 0 : 1 );
		byte[] smaller = new byte[ size ];
		System.arraycopy( array, 0, smaller, 0, size );

		return new InputBitStream( smaller );

	}

	@Test
    public void testSemiExternalGammaBigListGammaCoding() throws IOException {

		long[] longs = { 10, 300, 450, 650, 1000, 1290, 1699 };
		LongList listLongs = new LongArrayList( longs );

		SemiExternalGammaBigList list = new SemiExternalGammaBigList( buildInputStream( listLongs ), 1, listLongs.size() );
		for ( int i = 0; i < longs.length; ++i ) {
			assertEquals( ( "test failed for index: " + i ), longs[ i ], list.getLong( i ) );
		}

		list = new SemiExternalGammaBigList( buildInputStream( listLongs ), 2, listLongs.size() );
		for ( int i = 0; i < longs.length; ++i ) {
			assertEquals( ( "test failed for index: " + i ), longs[ i ], list.getLong( i ) );
		}

		list = new SemiExternalGammaBigList( buildInputStream( listLongs ), 4, listLongs.size() );
		for ( int i = 0; i < longs.length; ++i ) {
			assertEquals( ( "test failed for index: " + i ), longs[ i ], list.getLong( i ) );
		}

		list = new SemiExternalGammaBigList( buildInputStream( listLongs ), 7, listLongs.size() );
		for ( int i = 0; i < longs.length; ++i ) {
			assertEquals( ( "test failed for index: " + i ), longs[ i ], list.getLong( i ) );
		}
		
		list = new SemiExternalGammaBigList( buildInputStream( listLongs ), 8, listLongs.size() );
		for ( int i = 0; i < longs.length; ++i ) {
			assertEquals( ( "test failed for index: " + i ), longs[ i ], list.getLong( i ) );
		}
    }

	@Test
    public void testEmptySemiExternalGammaBigListGammaCoding() throws IOException {

		long[] longs = {  };
		LongList listOffsets = new LongArrayList( longs );

		new SemiExternalGammaBigList( buildInputStream( listOffsets ), 1, listOffsets.size() );
		assertTrue( true );
    }

}
