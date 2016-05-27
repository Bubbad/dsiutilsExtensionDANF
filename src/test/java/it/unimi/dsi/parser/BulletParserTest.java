package it.unimi.dsi.parser;

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
import it.unimi.dsi.parser.callback.Callback;
import it.unimi.dsi.parser.callback.DefaultCallback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.common.base.Charsets;

public class BulletParserTest {

	@Test
	public void testParser() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, FileNotFoundException, IOException {
		char[] text = IOUtils.toCharArray( this.getClass().getResourceAsStream( "test.data" ), Charsets.UTF_8 );

		final Callback mockCallback = (Callback)Proxy.getProxyClass( Callback.class.getClassLoader(), Callback.class ).getConstructor( InvocationHandler.class )
				.newInstance( new Object[] { new InvocationHandler() {
					int call = 0;

					String[] methods = { "configure", "startDocument", "endDocument" };

					@Override
					public Object invoke( final Object proxy, final Method method, final Object[] args ) throws Throwable {
						if ( call < methods.length )
							assertEquals( method.getName(), methods[ call++ ] );
						return Boolean.TRUE;
					}
				} } );

		new BulletParser().setCallback( mockCallback ).parse( text, 0, text.length );
	}
	
	private final static class VisibleBulletParser extends BulletParser {
		@Override
		public int scanEntity( char[] a, int offset, int length, boolean loose, MutableString entity ) {
			return super.scanEntity( a, offset, length, loose, entity );
		}
	}
	
	@Test
	public void testScanEntityAtEndOfArray() {
		VisibleBulletParser parser = new VisibleBulletParser();
		
		char[] test = "&test".toCharArray();
		assertEquals( -1, parser.scanEntity( test, 0, test.length, false, new MutableString() ) );
		assertEquals( -1, parser.scanEntity( test, 0, test.length, true, new MutableString() ) );
		test = "&apos".toCharArray();
		assertEquals( -1, parser.scanEntity( test, 0, test.length, false, new MutableString() ) );
		assertEquals( 5, parser.scanEntity( test, 0, test.length, true, new MutableString() ) );
	}

	@Test
	public void testCdata() {
		final BulletParser parser = new BulletParser();
		final Callback callback = new DefaultCallback() {
			@Override
			public boolean cdata( Element element, char[] text, int offset, int length) {
				assertEquals( "Test > 0", new String(  text, offset, length ) );
				return true;
			}
			
		};
		parser.setCallback( callback );
		parser.parseCDATA( true );
		parser.parse( "<tag><![CDATA[Test > 0]]></tag>".toCharArray() );
		parser.parse( "<tag><![CDATA[Test > 0".toCharArray() );
	}
}
