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
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.chars.CharArrayList;

import org.junit.Test;

public class TextPatternTest {
	@Test
	public void testSingleCharacterSearch() {
		byte[] b = new byte[] { 1, (byte)'A', 2 };
		String s = " A ";
		TextPattern pattern = new TextPattern( "A" );

		assertEquals( -1, pattern.search( b, 0, 1 ) );	
		assertEquals( -1, pattern.search( s, 0, 1 ) );	
		assertEquals( -1, pattern.search( s.toCharArray(), 0, 1 ) );	
		assertEquals( -1, pattern.search( CharArrayList.wrap( s.toCharArray() ), 0, 1 ) );	

		assertEquals( 1, pattern.search( b ) );	
		assertEquals( 1, pattern.search( s ) );	
		assertEquals( 1, pattern.search( s.toCharArray() ) );	
		assertEquals( 1, pattern.search( CharArrayList.wrap( s.toCharArray() ) ) );	
	}
	
	@Test
	public void testSearch() {
		byte[] b = new byte[] { 1, (byte)'A', 'B', 2 };
		String s = " AB ";
		TextPattern pattern = new TextPattern( "AB" );
		
		assertEquals( -1, pattern.search( b, 0, 2 ) );	
		assertEquals( -1, pattern.search( s, 0, 2 ) );	
		assertEquals( -1, pattern.search( s.toCharArray(), 0, 2 ) );	
		assertEquals( -1, pattern.search( CharArrayList.wrap( s.toCharArray() ), 0, 2 ) );	
		
		assertEquals( 1, pattern.search( b ) );	
		assertEquals( 1, pattern.search( s ) );	
		assertEquals( 1, pattern.search( s.toCharArray() ) );	
		assertEquals( 1, pattern.search( CharArrayList.wrap( s.toCharArray() ) ) );	

		TextPattern patternMeta = new TextPattern( "<meta", TextPattern.CASE_INSENSITIVE );
		assertTrue( patternMeta.search( documentMetaIsutf_8.getBytes() ) != -1 );
		patternMeta = new TextPattern( "<META", TextPattern.CASE_INSENSITIVE );
		assertTrue( patternMeta.search( documentMetaIsutf_8.getBytes() ) != -1 );
		
	}

	private static final String documentMetaIsutf_8 = 
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Strict//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n" +
		"\n" +
		"<html>\n" +
		"<head>\n" + 
		"<style type=\"text/css\">\n" +
		"@import \"/css/content.php\";\n" +
		"@import \"/css/layout.php\";\n" +
		"</style>" +
		"<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" >" +
		"<title id=\"mamma\" special-type=\"li turchi\">Sebastiano Vigna</title>\n" +  
		"</HEAD>\n" +
		"<boDY>\n" +	
		"<div id=header>:::Sebastiano Vigna</div>" +
		"<div id=left>\n" +
		"<ul id=\"left-nav\">" +
		"<br>Bye bye baby\n" +
		"<img SRc=\"but I'm ignoring this one\"> and not this one\n" +
		"\n\n even whitespace counts \n\n" +
		"<frame SRC=\"http://www.GOOGLE.com/\">The frame source counts</frame>\n" +
		"<iframe SRC=\"http://www.GOOGLE.com/\">And so does the iframe source</iframe>\n" +
		"</body>\n" +
		"</html>";


}
