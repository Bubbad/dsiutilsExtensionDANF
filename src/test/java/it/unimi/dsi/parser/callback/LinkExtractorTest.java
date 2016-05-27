package it.unimi.dsi.parser.callback;

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
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.parser.BulletParser;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.common.base.Charsets;

public class LinkExtractorTest {

	@Test
	public void testExtractor() throws IOException {
		char[] text = IOUtils.toCharArray( this.getClass().getResourceAsStream( "LinkExtractorTest.data" ), Charsets.UTF_8 );

		BulletParser parser = new BulletParser();
		LinkExtractor linkExtractor = new LinkExtractor();
		parser.setCallback( linkExtractor );
		parser.parse( text );

		testExtractorResults( linkExtractor );
	}

	private void testExtractorResults( final LinkExtractor linkExtractor ) {
		assertEquals( new ObjectLinkedOpenHashSet<String>( new String[] { "manual.css", "http://link.com/", "http://anchor.com/", "http://badanchor.com/" } ), linkExtractor.urls );
		assertEquals( "http://base.com/", linkExtractor.base() );
		assertEquals( "http://refresh.com/", linkExtractor.metaRefresh() );
		assertEquals( "http://location.com/", linkExtractor.metaLocation() );
	}
}
