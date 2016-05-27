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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class BooleanListBitVectorTest {

	@Test
	public void testSetClearFlip() {
		BooleanListBitVector v = BooleanListBitVector.getInstance();
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
		BooleanListBitVector v = BooleanListBitVector.getInstance();
		v.size( 100 );
		BitVectorTestCase.testFillFlip( v );
		BitVectorTestCase.testFillFlip( v.subVector( 0, 90 ) );
		BitVectorTestCase.testFillFlip( v.subVector( 5, 90 ) );
	}
	
	@Test
	public void testRemove() {
		BitVectorTestCase.testRemove( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testAdd() {
		BitVectorTestCase.testAdd( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testCopy() {
		BitVectorTestCase.testCopy( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testEquals2() {
		BooleanListBitVector v = BooleanListBitVector.getInstance();
		v.clear();
		v.size( 100 );
		v.fill( 5, 80, true );
		BooleanListBitVector w = v.copy();
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
		BitVectorTestCase.testBits( BooleanListBitVector.getInstance() );
	}
		
	@Test
	public void testLongBigListView() {
		BitVectorTestCase.testLongBigListView( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testLongSetView() {
		BitVectorTestCase.testLongSetView( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testFirstLast() {
		BitVectorTestCase.testFirstLastPrefix( BooleanListBitVector.getInstance() );
	}
	
	@Test
	public void testLogicOperators() {
		BitVectorTestCase.testLogicOperators( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testCount() {
		BitVectorTestCase.testCount( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		BitVectorTestCase.testSerialisation( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testReplace() {
		BitVectorTestCase.testReplace( BooleanListBitVector.getInstance() );
	}

	@Test
	public void testAppend() {
		BitVectorTestCase.testAppend( BooleanListBitVector.getInstance() );
	}	
}
