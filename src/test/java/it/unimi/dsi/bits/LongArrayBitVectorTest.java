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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.longs.LongBigList;
import it.unimi.dsi.util.SplitMix64Random;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

public class LongArrayBitVectorTest {

	@Test
	public void testSetClearFlip() {
		LongArrayBitVector v = LongArrayBitVector.getInstance();
		v.size( 1 );
		BitVectorTestCase.testSetClearFlip( v );
		v.size( 64 );
		BitVectorTestCase.testSetClearFlip( v );
		v.size( 80 );
		BitVectorTestCase.testSetClearFlip( v );
		v.size( 150 );
		BitVectorTestCase.testSetClearFlip( v );
		
		BitVectorTestCase.testSetClearFlip( v.subVector( 0, 90 ) );
		BitVectorTestCase.testSetClearFlip( v.subVector( 5, 90 ) );
	}

	@Test
	public void testFillFlip() {
		LongArrayBitVector v = LongArrayBitVector.getInstance();
		v.size( 100 );
		BitVectorTestCase.testFillFlip( v );
		BitVectorTestCase.testFillFlip( v.subVector( 0, 90 ) );
		BitVectorTestCase.testFillFlip( v.subVector( 5, 90 ) );
	}
	
	@Test
	public void testRemove() {
		BitVectorTestCase.testRemove( LongArrayBitVector.getInstance() );
		LongArrayBitVector v = LongArrayBitVector.getInstance();
		
		v.clear();
		v.size( 65 );
		v.set( 64 );
		v.removeBoolean( 0 );
		assertEquals( 0, v.bits()[ 1 ] );
		v.clear();
	}

	@Test
	public void testAdd() {
		BitVectorTestCase.testAdd( LongArrayBitVector.getInstance() );
	}

	@Test
	public void testCopy() {
		BitVectorTestCase.testCopy( LongArrayBitVector.getInstance() );
	}

	@Test
	public void testEquals2() {
		LongArrayBitVector v = LongArrayBitVector.getInstance();
		v.clear();
		v.size( 100 );
		v.fill( 5, 80, true );
		LongArrayBitVector w = v.copy();
		assertTrue( w.equals( v, 0, 100 ) );
		assertTrue( w.equals( v, 0, 64 ) );
		assertTrue( w.equals( v, 64, 100 ) );

		v.clear();
		v.size( 1000 );
		v.fill( 5, 800, true );
		w.replace( v );
		assertTrue( w.equals( v, 0, 1000 ) );
		assertTrue( w.equals( v, 0, 64 ) );
		assertTrue( w.equals( v, 0, 500 ) );
		assertTrue( w.equals( v, 128, 900 ) );

		v.clear();
		v.size( 100 );
		v.fill( 5, 80, true );
		w = v.copy();
		w.clear( 30 );
		w.clear( 70 );
		w.set( 90 );
		assertFalse( w.equals( v, 0, 100 ) );
		assertFalse( w.equals( v, 0, 64 ) );
		assertFalse( w.equals( v, 64, 100 ) );
		assertFalse( w.equals( v, 65, 100 ) );

		v.clear();
		v.size( 1000 );
		v.fill( 5, 800, true );
		w.replace( v );
		w.clear( 63 );
		w.clear( 128 );
		w.clear( 500 );
		assertFalse( w.equals( v, 0, 1000 ) );
		assertFalse( w.equals( v, 0, 64 ) );
		assertFalse( w.equals( v, 0, 500 ) );
		assertFalse( w.equals( v, 128, 900 ) );
		assertFalse( w.equals( v, 129, 900 ) );
		assertFalse( w.equals( v, 129, 511 ) );
}

	@Test
	public void testBits() {
		BitVectorTestCase.testBits( LongArrayBitVector.getInstance() );
	}
		
	@Test
	public void testLongBigListView() {
		BitVectorTestCase.testLongBigListView( LongArrayBitVector.getInstance() );
	}
	
	@Test
	public void testLongSetView() {
		BitVectorTestCase.testLongSetView( LongArrayBitVector.getInstance() );
	}
	
	@Test
	public void testFirstLastPrefix() {
		BitVectorTestCase.testFirstLastPrefix( LongArrayBitVector.getInstance() );
	}
	
	@Test
	public void testLogicOperators() {
		BitVectorTestCase.testLogicOperators( LongArrayBitVector.getInstance() );
	}

	@Test
	public void testCount() {
		BitVectorTestCase.testCount( LongArrayBitVector.getInstance() );
	}

	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		BitVectorTestCase.testSerialisation( LongArrayBitVector.getInstance() );
	}
	
	@Test
	public void testReplace() {
		BitVectorTestCase.testReplace( LongArrayBitVector.getInstance() );
	}
	
	@Test
	public void testGarbageInReplace() {
		LongArrayBitVector b = LongArrayBitVector.ofLength( 128 );
		b.set( 64 );
		b.replace( BooleanListBitVector.getInstance().length( 64 ) );
		assertEquals( 0, b.bits()[ 1 ] );
	}

	@Test
	public void testHashCodeConsistency() {
		LongArrayBitVector b = LongArrayBitVector.of( 0, 1, 1, 0, 0, 1 );
		assertEquals( BooleanListBitVector.getInstance().replace( b ).hashCode(), b.hashCode() );
		b = LongArrayBitVector.wrap( new long[]{ 0x234598729872983L, 0x234598729872983L, 0x234598729872983L, 0xFFFF }, 222 );
		assertEquals( BooleanListBitVector.getInstance().replace( b ).hashCode(), b.hashCode() );
		assertEquals( BitVectors.EMPTY_VECTOR.hashCode(), b.length( 0 ).hashCode() );
	}

	@Test
	public void testAppend() {
		BitVectorTestCase.testAppend( LongArrayBitVector.getInstance() );
	}
	
	@Test
	public void testTrim() {
		assertTrue( LongArrayBitVector.getInstance( 100 ).trim() );
		assertFalse( LongArrayBitVector.getInstance( 100 ).length( 65 ).trim() );
		assertFalse( LongArrayBitVector.getInstance( 0 ).trim() );
	}
	
	@Test
	public void testClone() throws CloneNotSupportedException {
		LongArrayBitVector v = LongArrayBitVector.getInstance().length( 100 );
		for( int i = 0; i < 50; i++ ) v.set( i * 2 );
		assertEquals( v, v.clone() );
	}

	@Test
	public void testEquals() {
		LongArrayBitVector v = LongArrayBitVector.getInstance().length( 100 );
		for( int i = 0; i < 50; i++ ) v.set( i * 2 );
		LongArrayBitVector w = v.copy();
		assertEquals( v, w );
		w.length( 101 );
		assertFalse( v.equals( w ) );
		w.length( 100 );
		w.set( 3 );
		assertFalse( v.equals( w ) );
	}
	
	@Test
	public void testConstructor() {
		final long bits[] = { 0, 1, 0 };
		
		boolean ok = false;
		try {
			LongArrayBitVector.wrap( bits, 64 );
		}
		catch( IllegalArgumentException e ) {
			ok = true;
		}
		
		assertTrue( ok );

		LongArrayBitVector.wrap( bits, 65 );
		LongArrayBitVector.wrap( bits, 128 );

		ok = false;
		try {
			LongArrayBitVector.wrap( bits, 193 );
		}
		catch( IllegalArgumentException e ) {
			ok = true;
		}
		
		assertTrue( ok );

		bits[ 0 ] = 10;
		bits[ 1 ] = 0;
		
		ok = false;
		try {
			LongArrayBitVector.wrap( bits, 3 );
		}
		catch( IllegalArgumentException e ) {
			ok = true;
		}
		
		assertTrue( ok );

		LongArrayBitVector.wrap( bits, 4 );
		
		bits[ 2 ] = 1;
		
		ok = false;
		try {
			LongArrayBitVector.wrap( bits, 4 );
		}
		catch( IllegalArgumentException e ) {
			ok = true;
		}
		
		assertTrue( ok );

	}
	
	@Test
	public void testLongBig() {
		LongArrayBitVector v =  LongArrayBitVector.getInstance( 16 * 1024 );
		LongBigList l = v.asLongBigList( Short.SIZE );
		l.set( 0, 511 );
		assertEquals( 511, v.bits()[ 0 ] );
	}
	
	@Test
	public void testCopyAnotherVector() {
		Random r = new SplitMix64Random( 1 );
		LongArrayBitVector bv = LongArrayBitVector.getInstance( 200 );
		for( int i = 0; i < 100; i++ ) bv.add( r.nextBoolean() );
		assertEquals( LongArrayBitVector.copy( bv ), bv );
		bv = LongArrayBitVector.getInstance( 256 );
		for( int i = 0; i < 256; i++ ) bv.add( r.nextBoolean() );
		assertEquals( LongArrayBitVector.copy( bv ), bv );
		bv = LongArrayBitVector.getInstance( 10 );
		for( int i = 0; i < 10; i++ ) bv.add( r.nextBoolean() );
		assertEquals( LongArrayBitVector.copy( bv ), bv );
		BooleanListBitVector bbv = BooleanListBitVector.getInstance( 200 );
		for( int i = 0; i < 100; i++ ) bbv.add( r.nextBoolean() );
		assertEquals( LongArrayBitVector.copy( bbv ), bbv );
		bbv = BooleanListBitVector.getInstance( 256 );
		for( int i = 0; i < 256; i++ ) bbv.add( r.nextBoolean() );
		assertEquals( LongArrayBitVector.copy( bbv ), bbv );
		bbv = BooleanListBitVector.getInstance( 10 );
		for( int i = 0; i < 10; i++ ) bbv.add( r.nextBoolean() );
		assertEquals( LongArrayBitVector.copy( bbv ), bbv );
	}
	
	@Test
	public void testReplaceLongArrayBitVector() {
		LongArrayBitVector b = LongArrayBitVector.of( 0, 1, 1 );
		assertEquals( b, LongArrayBitVector.getInstance().replace( b ) );
	}
	
	@Test
	public void testLengthClearsBits() {
		LongArrayBitVector bv = LongArrayBitVector.getInstance().length( 100 );
		bv.fill( true );
		bv.length( 0 );
		bv.append( 0, 1 );
		assertFalse( bv.getBoolean( 0 ) );
	}
}
