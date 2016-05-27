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
import it.unimi.dsi.util.SplitMix64Random;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Random;

import org.junit.Test;

/** Note: this test has little meaning unless you change ByteBufferInputStream.FIRST_SHIFT to 16.
 */

@SuppressWarnings("resource")
public class ByteBufferInputStreamTest {

	private static final boolean DEBUG = false;

	@Test
	public void testStream() throws FileNotFoundException, IOException {
		File f = File.createTempFile( ByteBufferInputStreamTest.class.getName(), "tmp" );
		f.deleteOnExit();
		final int l = 100000;
		long seed = System.currentTimeMillis();
		if ( DEBUG ) System.err.println( "Seed: " + seed );
		Random random = new SplitMix64Random( seed );

		for( int n = 1; n < 8; n++ ) {
			final FileOutputStream fos = new FileOutputStream( f );
			for( int i = 0; i < l * n; i++ ) fos.write( random.nextInt() & 0xFF );
			fos.close();

			final FileChannel channel = new FileInputStream( f ).getChannel();
			ByteBufferInputStream bis = ByteBufferInputStream.map( channel, MapMode.READ_ONLY );
			if ( n % 2 == 0 ) bis = bis.copy();
			
			FileInputStream test = new FileInputStream( f );
			FileChannel fc = test.getChannel();
			int a1, a2, off, len, pos;
			byte b1[] = new byte[ 32768 ];
			byte b2[] = new byte[ 32768 ];

			for( int k = 0; k < l / 10; k++ ) {

				switch ( random.nextInt( 6 ) ) {

				case 0:
					if ( DEBUG ) System.err.println( "read()" );
					a1 = bis.read();
					a2 = test.read();
					assertEquals( a2, a1 );
					break;

				case 1:
					off = random.nextInt( b1.length );
					len = random.nextInt( b1.length - off + 1 );
					a1 = bis.read( b1, off, len );
					a2 = test.read( b2, off, len );
					if ( DEBUG ) System.err.println( "read(b, " + off + ", " + len + ")" );

					assertEquals( a2, a1 );

					for ( int i = off; i < off + len; i++ )
						assertEquals( b2[ i ], b1[ i ] );
					break;

				case 2:
					if ( DEBUG ) System.err.println( "available()" );
					assertEquals( test.available(), bis.available() );
					break;

				case 3:
					pos = (int)bis.position();
					if ( DEBUG ) System.err.println( "position()=" + pos );
					assertEquals( (int)fc.position(), pos );
					break;

				case 4:
					pos = random.nextInt( l * n );
					bis.position( pos );
					if ( DEBUG ) System.err.println( "position(" + pos + ")" );
					( test = new FileInputStream( f ) ).skip( pos );
					fc = test.getChannel();
					break;

				case 5:
					pos = random.nextInt( (int)( l * n - bis.position() + 1 ) );
					if ( DEBUG ) System.err.println( "skip(" + pos + ")" );
					a1 = (int)bis.skip( pos );
					a2 = (int)test.skip( pos );
					assertEquals( a2, a1 );
					break;
				}
			}
			
			test.close();
			bis = null;
			System.gc(); // Try to get rid of mapped buffers.
			channel.close();
		}
	}
}
