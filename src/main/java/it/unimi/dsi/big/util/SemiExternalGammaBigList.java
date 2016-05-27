package it.unimi.dsi.big.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2007-2016 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.longs.AbstractLongBigList;
import it.unimi.dsi.fastutil.longs.LongBigList;
import it.unimi.dsi.io.InputBitStream;
import it.unimi.dsi.io.OutputBitStream;

import java.io.EOFException;
import java.io.IOException;

/** Provides semi-external random access to a {@linkplain LongBigList big list} 
 * of {@linkplain OutputBitStream#writeGamma(int) &gamma;-encoded} integers. 
 * 
 * <p>This class is a semi-external {@link it.unimi.dsi.fastutil.longs.LongBigList} that
 * MG4J uses to access files containing frequencies.
 *  
 * <p>Instead, this class accesses frequencies in their
 * compressed forms, and provides entry points for random access to each long. At construction
 * time, entry points are computed with a certain <em>step</em>, which is the number of longs
 * accessible from each entry point, or, equivalently, the maximum number of longs that will
 * be necessary to read to access a given long.
 *
 * <p><strong>Warning:</strong> This class is not thread safe, and needs to be synchronised to be used in a
 * multithreaded environment. 
 *
 * @author Fabien Campagne
 * @author Sebastiano Vigna
 * @since 2.0
 */
public class SemiExternalGammaBigList extends AbstractLongBigList {
	public final static int DEFAULT_STEP = 128;
	
	/** Position in the offset stream for each random access entry point (one each {@link #step} elements). */
	private final long[] position;
	/** Stream over the compressed offset information. */
	private final InputBitStream ibs;
	/** Maximum number of longs to skip. */
	private final int step;
	/** The number of longs. */
	private final long numLongs;

	/** Creates a new semi-external list.
	 * 
	 * @param longs a bit stream containing &gamma;-encoded longs.
	 * @param step the step used to build random-access entry points, or -1 to get {@link #DEFAULT_STEP}; note that
	 * a step causing more than 2<sup>31</sup> slots will be silently increased.
	 * @param numLongs the overall number of offsets (i.e., the number of terms).
	 */

	public SemiExternalGammaBigList( final InputBitStream longs, final int step, final long numLongs ) throws IOException {
		// We guarantee that the default step is such that we cannot cause problems.
		this.step = Math.max( step == -1 ? DEFAULT_STEP : step, (int)( numLongs / ( 1L << 31 ) ) );
		int slots = (int)( ( numLongs + this.step - 1 ) / this.step );
		this.position = new long[ slots ];
		this.numLongs = numLongs;
		this.ibs = longs;
		ibs.position( 0 );
		ibs.readBits( 0 );
		final int lastSlot = position.length - 1;
		for ( int i = 0; i <= lastSlot; i++ ) {
			position[ i ] = ibs.readBits();
			if ( i != lastSlot ) ibs.skipGammas( this.step );
		}
	}

	
	/** Creates a new semi-external list.
	 * 
	 * <p>This quick-and-dirty constructor estimates the number of longs by checking
	 * for an {@link EOFException}.
	 * 
	 * @param longs a bit stream containing &gamma;-encoded longs.
	 */
	
	public SemiExternalGammaBigList( final InputBitStream longs ) throws IOException {
		this( longs, DEFAULT_STEP, estimateNumberOfLongs( longs ) );
	}
		
	private static int estimateNumberOfLongs( final InputBitStream longs ) {
		int numLongs = 0;
		try {
			longs.position( 0 );
			for(;;) {
				longs.readLongGamma();
				numLongs++;
			}
		}
		catch( EOFException e ) {
			return numLongs;
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}
	
	public final long getLong( final long index ) {
		if ( index < 0 || index >= numLongs ) throw new IndexOutOfBoundsException( Long.toString( index ) );
		final int slotNumber = (int)( index / step );
		final int k = (int)( index % step );
		try {
			ibs.position( position[ slotNumber ] );
			ibs.skipGammas( k );
			return ibs.readLongGamma();
		}
		catch( IOException e ) {
			throw new RuntimeException( e );
		}
	}
	
	public long size64() {
		return numLongs;
	}
}