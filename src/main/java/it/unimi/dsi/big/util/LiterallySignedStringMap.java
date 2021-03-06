package it.unimi.dsi.big.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2009-2016 Sebastiano Vigna 
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


import it.unimi.dsi.big.io.FileLinesCollection;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.objects.AbstractObject2LongFunction;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.ObjectBigList;
import it.unimi.dsi.lang.MutableString;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.stringparsers.ForNameStringParser;

/** A string map based on a function signed using the original list of strings. 
 * 
 * <p>A minimal perfect hash function maps a set of string to an initial segment of the natural
 * numbers, but will actually map <em>any</em> string to that segment. We can check that
 * a string is part of the key set by hashing it to a value <var>h</var>, and checking that the <var>h</var>-th 
 * string of the original list does coincide. Since, moreover, this class implements {@link StringMap},
 * and thus {@linkplain #list() exposes the original list}, we have a two-way
 * dictionary. In other words, this is a <em>full</em> {@link StringMap} implementation.
 * 
 * <p>Note that some care must be exercised: {@link CharSequence}'s contract does not
 * prescribe equality by content, so if your function behaves badly on some implementations of
 * {@link CharSequence} you might make the checks fail. To avoid difficulties, the
 * constructor checks that every string in the list is hashed correctly.
 * 
 * <p>For the same reason, this class implements <code>StringMap&lt;MutableString&gt;</code>, and
 * requires that the list of strings provided at construction time is actually a list of 
 * {@linkplain MutableString mutable strings}.
 * 
 * <!-- <p>A typical usage of this class pairs a {@link it.unimi.dsi.util.FrontCodedStringList} with some kind
 * of succinct structure from <a href="http://sux4j.dsi.unimi.it/">Sux4J</a>. -->
 * 
 * @author Sebastiano Vigna
 * @since 2.0
 */



public class LiterallySignedStringMap extends AbstractObject2LongFunction<CharSequence> implements StringMap<MutableString>, Serializable, Size64 {
	private static final long serialVersionUID = 0L;

	/** The underlying map. */
	protected final Object2LongFunction<? extends CharSequence> function;
	/** The underlying list. */
	protected final ObjectBigList<? extends MutableString> list;
	/** The size of {@link #list}. */
	protected final long size;

	/** Creates a new shift-add-xor signed string map using a given hash map.
	 * 
	 * @param function a function mapping each string in <code>list</code> to its ordinal position.
	 * @param list a list of strings.
	 */
	
	public LiterallySignedStringMap( final Object2LongFunction<? extends CharSequence> function, final ObjectBigList<? extends MutableString> list ) {
		this.function = function;
		this.list = list;
		size = list.size64();
		for( long i = 0; i < size; i++ ) if ( function.getLong( list.get( i ) ) != i ) throw new IllegalArgumentException( "Function and list do not agree" );
		defRetValue = -1;
	}

	public long getLong( final Object o ) {
		final CharSequence s = (CharSequence)o;
		final long index = function.getLong( s );
		return index >= 0 && index < size && list.get( (int)index ).equals( s ) ? index : defRetValue;
	}

	public Long get( final Object o ) {
		final CharSequence s = (CharSequence)o;
		final long index = function.getLong( s );
		return index >= 0 && index < size && list.get( (int)index ).equals( s ) ? Long.valueOf( index ) : null;
	}

	public int size() {
		return function.size();
	}

	public long size64() {
		return list.size64();
	}

	public boolean containsKey( final Object o ) {
		return getLong( o ) != -1;
	}
	
	public ObjectBigList<? extends MutableString> list() {
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main( final String[] arg ) throws IOException, JSAPException, ClassNotFoundException, SecurityException, NoSuchMethodException {

		final SimpleJSAP jsap = new SimpleJSAP( LiterallySignedStringMap.class.getName(), "Builds a shift-add-xor signed string map by reading a newline-separated list of strings and a function built on the same list of strings.",
				new Parameter[] {
			new FlaggedOption( "encoding", ForNameStringParser.getParser( Charset.class ), "UTF-8", JSAP.NOT_REQUIRED, 'e', "encoding", "The string file encoding." ),
			new Switch( "zipped", 'z', "zipped", "The string list is compressed in gzip format." ),
			new Switch( "text", 't', "text", "The string list actually a text file, with one string per line." ),
			new UnflaggedOption( "function", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, JSAP.NOT_GREEDY, "The filename of the function to be signed." ),
			new UnflaggedOption( "list", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, JSAP.NOT_GREEDY, "The filename of the serialised list of strings, or of a text file containing a list of strings, if -t is specified." ),
			new UnflaggedOption( "map", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, JSAP.NOT_GREEDY, "The filename of the resulting map." ),
		});

		JSAPResult jsapResult = jsap.parse( arg );
		if ( jsap.messagePrinted() ) return;

		final String functionName = jsapResult.getString( "function" );
		final String listName = jsapResult.getString( "list" );
		final String mapName = jsapResult.getString( "map" );

		
		final Charset encoding = (Charset)jsapResult.getObject( "encoding" );
		final boolean zipped = jsapResult.getBoolean( "zipped" );
		final boolean text = jsapResult.getBoolean( "text" );
		
		final ObjectBigList<MutableString> list = text ? new FileLinesCollection( listName, encoding.toString(), zipped ).allLines() : (ObjectBigList)BinIO.loadObject( listName );
		
		final Logger logger = LoggerFactory.getLogger( LiterallySignedStringMap.class );
		logger.info( "Signing..." );
		BinIO.storeObject( new LiterallySignedStringMap( (Object2LongFunction)BinIO.loadObject( functionName ), list ), mapName );
		logger.info( "Completed." );
	}
}
