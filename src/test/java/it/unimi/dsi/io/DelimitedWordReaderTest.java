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

import org.junit.Test;

@SuppressWarnings("resource")
public class DelimitedWordReaderTest {

	@Test
	public void testToSpec() {
		String className = DelimitedWordReader.class.getName();
		assertEquals( className + "(\"_\")", new DelimitedWordReader( "_" ).toSpec() );
		assertEquals( className + "(100,\"_\")", new DelimitedWordReader( "100", "_" ).toSpec() );
	}
}
