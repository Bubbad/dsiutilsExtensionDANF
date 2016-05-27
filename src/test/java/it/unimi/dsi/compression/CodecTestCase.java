package it.unimi.dsi.compression;

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
import it.unimi.dsi.bits.BitVector;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import it.unimi.dsi.io.InputBitStream;
import it.unimi.dsi.io.OutputBitStream;

import java.io.IOException;
import java.util.Random;

public abstract class CodecTestCase {
	protected static void checkPrefixCodec( PrefixCodec codec, Random r ) throws IOException {
		int[] symbol = new int[ 100 ];
		BooleanArrayList bits = new BooleanArrayList();
		for( int i = 0; i < symbol.length; i++ ) symbol[ i ] = r.nextInt( codec.size() ); 
		for( int i = 0; i < symbol.length; i++ ) {
			BitVector word = codec.codeWords()[ symbol[ i ] ];
			for( int j = 0; j < word.size(); j++ ) bits.add( word.get( j ) );
		}
	
		BooleanIterator booleanIterator = bits.iterator();
		Decoder decoder = codec.decoder();
		for( int i = 0; i < symbol.length; i++ ) {
			assertEquals( symbol[ i ], decoder.decode( booleanIterator ) );
		}
		
		FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
		@SuppressWarnings("resource")
		OutputBitStream obs = new OutputBitStream( fbaos, 0 );
		obs.write( bits.iterator() );
		obs.flush();
		InputBitStream ibs = new InputBitStream( fbaos.array );
		
		for( int i = 0; i < symbol.length; i++ ) {
			assertEquals( symbol[ i ], decoder.decode( ibs ) );
		}
	}

	protected void checkLengths( int[] frequency, int[] codeLength, BitVector[] codeWord ) {
		for( int i = 0; i < frequency.length; i++ ) 
			assertEquals( Integer.toString( i ), codeLength[ i ], codeWord[ i ].size() );
	}
}
