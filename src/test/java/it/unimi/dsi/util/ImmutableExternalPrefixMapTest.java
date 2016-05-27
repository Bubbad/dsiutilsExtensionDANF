package it.unimi.dsi.util;

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
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;


public class ImmutableExternalPrefixMapTest {

	public void testLargeSet( final int blockSize ) throws IOException {
		Collection<String> c = Arrays.asList( TernaryIntervalSearchTreeTest.WORDS );
		TernaryIntervalSearchTree t = new TernaryIntervalSearchTree( c );
		ImmutableExternalPrefixMap d = new ImmutableExternalPrefixMap( c, blockSize );
		
		for( int i = 0; i < TernaryIntervalSearchTreeTest.WORDS.length; i++ ) assertTrue( TernaryIntervalSearchTreeTest.WORDS[ i ], d.containsKey( TernaryIntervalSearchTreeTest.WORDS[ i ] ) );
		for( int i = 0; i < TernaryIntervalSearchTreeTest.WORDS.length; i++ ) assertEquals( TernaryIntervalSearchTreeTest.WORDS[ i ], d.list().get( i ).toString() );
		for( int i = 0; i < TernaryIntervalSearchTreeTest.WORDS.length; i++ ) 
			for( int j = 0; j < TernaryIntervalSearchTreeTest.WORDS[ i ].length(); j++ ) {
				String s = TernaryIntervalSearchTreeTest.WORDS[ i ].substring( 0, j + 1 );
				assertEquals( s, t.rangeMap().get( s ), d.getInterval( s ) );
				s = s + " ";
				assertEquals( s, t.rangeMap().get( s ), d.getInterval( s ) );
				s = s.substring( 0, s.length() - 1 ) + "~";
				assertEquals( s, t.rangeMap().get( s ), d.getInterval( s ) );
			}
		
		// Similar tests, using all prefixes of all strings in WORDS.
		Collection<String> p = new ObjectRBTreeSet<String>();
		for( int i = 0; i < TernaryIntervalSearchTreeTest.WORDS.length; i++ ) 
			for( int j = 0; j < TernaryIntervalSearchTreeTest.WORDS[ i ].length(); j++ )
				p.add( TernaryIntervalSearchTreeTest.WORDS[ i ].substring( 0, j + 1 ) );

		d = new ImmutableExternalPrefixMap( p, blockSize );
		t = new TernaryIntervalSearchTree( p );
				
		int j = 0;
		for( Iterator<String> i = p.iterator(); i.hasNext(); ) {
			String s = i.next();
			assertTrue( s, d.containsKey( s ) );
			assertTrue( s, d.containsKey( s ) );
			assertEquals( s, d.list().get( j++ ).toString() );
			assertEquals( s, t.rangeMap().get( s ), d.getInterval( s ) );
		}
		
		final Iterator<CharSequence> k = d.iterator();
		for( final Iterator<String> i = p.iterator(); i.hasNext(); ) {
			assertTrue( i.hasNext() == k.hasNext() );
			assertEquals( i.next().toString(), k.next().toString() );
		}

		// Test negatives
		for( long i = 1000000000000L; i < 1000000002000L; i++ ) assertEquals( -1, d.getLong( Long.toBinaryString( i ) ) );

	}
	
	@Test
	public void testLargeSet64() throws IOException {
		testLargeSet( 64 );
	}
	
	@Test
	public void testLargeSet128() throws IOException {
		testLargeSet( 128 );
	}

	@Test
	public void testLargeSet256() throws IOException {
		testLargeSet( 256 );
	}

	@Test
	public void testLargeSet1024() throws IOException {
		testLargeSet( 1024 );
	}
	
	@Test
	public void testLargeSet16384() throws IOException {
		testLargeSet( 16384 );
	}
	
	@Test
	public void testPrefixes() throws IOException {
		ImmutableExternalPrefixMap d = new ImmutableExternalPrefixMap( new ObjectLinkedOpenHashSet<CharSequence>( new String[] { "ab", "ba", "bb" } ) );
		assertEquals( Interval.valueOf( 1, 2 ), d.getInterval( "b" ) );
	}
	
	@Test
	public void testLargeRootPrefixes() throws IOException {
		ImmutableExternalPrefixMap d = new ImmutableExternalPrefixMap( new ObjectLinkedOpenHashSet<CharSequence>( new String[] { "aab", "aac", "aad" } ), 2 );
		assertEquals( Interval.valueOf( 0, 2 ), d.getInterval( "" ) );
		assertEquals( Interval.valueOf( 0, 2 ), d.getInterval( "aa" ) );
		assertEquals( Interval.valueOf( 0, 2 ), d.getInterval( "aa" ) );
	}
	
	@Test
	public void testSingleton() throws IOException {
		ImmutableExternalPrefixMap d = new ImmutableExternalPrefixMap( ObjectSets.singleton( "a" ), 1024 );
		assertTrue( d.containsKey( "a" ) );
		assertFalse( d.containsKey( "b" ) );
		assertFalse( d.containsKey( "0" ) );
	}
	
	@Test
	public void testPrefixOutOfRange() throws IOException {
		ImmutableExternalPrefixMap d = new ImmutableExternalPrefixMap( new ObjectLinkedOpenHashSet<CharSequence>( new String[] { "ab", "ac" } ) );
		assertEquals( Intervals.EMPTY_INTERVAL, d.getInterval( "b" ) );
		assertEquals( Interval.valueOf( 0, 1 ), d.getInterval( "a" ) );
	}
}
