package it.unimi.dsi.bits;

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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstractBitVectorTest {

	private final static class MinimalAlternatingBitVector extends AbstractBitVector {
		private long length = 129;

		@Override
		public boolean getBoolean( long index ) { return index % 2 != 0; }
		@Override
		public long length() { return length; }
		
		@Override
		public MinimalAlternatingBitVector length( long newLength ) {
			this.length  = newLength;
			return this;
		}
	}

	@Test
	public void testUnsupported() {
		final BitVector v = new MinimalAlternatingBitVector();

		v.getBoolean( 0 );
		v.length();
		
		boolean ok = false;
		try {
			v.removeBoolean( 0 );
		}
		catch( UnsupportedOperationException e ) {
			ok = true;
		}
		
		assertTrue( ok );
		
		ok = false;
		try {
			v.set( 0, 0 );
		}
		catch( UnsupportedOperationException e ) {
			ok = true;
		}
		
		assertTrue( ok );
		
		ok = false;
		try {
			v.add( 0, 0 );
		}
		catch( UnsupportedOperationException e ) {
			ok = true;
		}
		
		assertTrue( ok );

		v.length( 1L<<32 );

		ok = false;
		try {
			v.size();
		}
		catch( IllegalStateException e ) {
			ok = true;
		}
		
		assertTrue( ok );

		ok = false;
		try {
			v.asLongBigList( 1 ).size();
		}
		catch( IllegalStateException e ) {
			ok = true;
		}
		
		assertTrue( ok );
	}
	
	@Test
	public void testCopy() {
		assertEquals( new MinimalAlternatingBitVector(), new MinimalAlternatingBitVector().copy() );
		assertEquals( new MinimalAlternatingBitVector().subVector( 2, 20 ), new MinimalAlternatingBitVector().subVector( 2, 20 ).copy() );
		assertEquals( new MinimalAlternatingBitVector().subVector( 5, 12 ), new MinimalAlternatingBitVector().subVector( 2, 20 ).subVector( 3, 10 ) );
		assertEquals( new MinimalAlternatingBitVector().subVector( 5, 12 ), new MinimalAlternatingBitVector().subVector( 2, 20 ).subVector( 3, 10 ).copy() );
		assertEquals( new MinimalAlternatingBitVector().subList( 2, 20 ), new MinimalAlternatingBitVector().subList( 2, 20 ).copy() );
		assertEquals( new MinimalAlternatingBitVector().subList( 5, 12 ), new MinimalAlternatingBitVector().subList( 2, 20 ).subList( 3, 10 ) );
		assertEquals( new MinimalAlternatingBitVector().subList( 5, 12 ), new MinimalAlternatingBitVector().subList( 2, 20 ).subList( 3, 10 ).copy() );
	}
	
	@Test
	public void testCount() {
		MinimalAlternatingBitVector v = new MinimalAlternatingBitVector();
		assertEquals( v.length() / 2, v.count() );
	}
	
	@Test
	public void testRemove() {
		BitVectorTestCase.testRemove( new AbstractBitVector.SubBitVector( BooleanListBitVector.getInstance().length( 1000 ), 10, 100 ) );
	}

	@Test
	public void testAdd() {
		BitVectorTestCase.testAdd( new AbstractBitVector.SubBitVector( BooleanListBitVector.getInstance().length( 1000 ), 10, 100 ) );
	}

	@Test
	public void testCompareTo() {
		MinimalAlternatingBitVector v = new MinimalAlternatingBitVector();
		LongArrayBitVector w = LongArrayBitVector.copy( v );
		assertEquals( 0, w.compareTo( v ) );
		assertEquals( 0, v.compareTo( w ) );
		w.set( 100 );
		assertEquals( 1, w.compareTo( v ) );
		assertEquals( -1, v.compareTo( w ) );
		w = LongArrayBitVector.ofLength( 10 );
		assertEquals( -1, w.compareTo( v ) );
		assertEquals( 1, v.compareTo( w ) );
		w = LongArrayBitVector.of( 1 );
		assertEquals( 1, w.compareTo( v ) );
		assertEquals( -1, v.compareTo( w ) );
	}
}
