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
import it.unimi.dsi.lang.MutableString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FrontCodedStringListTest {
	
	@Test
	public void testLargeSet() {
		List<String> c = Arrays.asList( TernaryIntervalSearchTreeTest.WORDS.clone() );
		MutableString s = new MutableString();
		for( int p = 0; p < 2; p++ ) {
			for( boolean utf8: new boolean[] { false, true } )
				for( int ratio = 1; ratio < 8; ratio++ ) {
					final FrontCodedStringList fcl = new FrontCodedStringList( c.iterator(), ratio, utf8 );
					for( int i = 0; i < fcl.size(); i++ ) {
						assertEquals( Integer.toString( i ), c.get( i ), fcl.get( i ).toString() );
						fcl.get( i, s );
						assertEquals( Integer.toString( i ), c.get( i ), s.toString() );
					}
				}
			
			Collections.sort( c );
		}
	}

	@Test
	public void testSurrogatePairs() {
		List<String> c = Arrays.asList( new String[] { "a", "AB\uE000AB", "\uD800\uDF02", "\uD800\uDF03", "b" } );
		for( boolean utf8: new boolean[] { false, true } )
			for( int ratio = 1; ratio < 8; ratio++ ) {
				final FrontCodedStringList fcl = new FrontCodedStringList( c.iterator(), ratio, utf8 );
				for( int i = 0; i < fcl.size(); i++ ) {
					assertEquals( Integer.toString( i ), c.get( i ), fcl.get( i ).toString() );
				}
			}
	}


}
