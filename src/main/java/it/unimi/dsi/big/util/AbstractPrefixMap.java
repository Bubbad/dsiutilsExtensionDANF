package it.unimi.dsi.big.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2007-2016 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.objects.AbstractObject2LongFunction;
import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectFunction;
import it.unimi.dsi.fastutil.objects.AbstractObjectBigList;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.ObjectBigList;
import it.unimi.dsi.lang.MutableString;
import it.unimi.dsi.util.LongInterval;
import it.unimi.dsi.util.LongIntervals;

import java.io.Serializable;

/** An abstract implementation of a prefix map.
 * 
 * <p>This class provides the full services of a {@link PrefixMap} by implementing just
 * {@link #getInterval(CharSequence)} and {@link #getTerm(long, MutableString)}
 */

public abstract class AbstractPrefixMap extends AbstractObject2LongFunction<CharSequence> implements PrefixMap<MutableString>, Serializable {
	private static final long serialVersionUID = 1L;
	protected Object2ObjectFunction<CharSequence, LongInterval> rangeMap;
	protected AbstractObject2ObjectFunction<LongInterval, MutableString> prefixMap;
	protected ObjectBigList<MutableString> list;
	
	// We must guarantee that, unless the user says otherwise, the default return value is -1.
	{
		defaultReturnValue( -1 );
	}
	/** Returns the range of strings having a given prefix.
	 * 
	 * @param prefix a prefix.
	 * @return the corresponding range of strings as an interval.
	 */
	protected abstract LongInterval getInterval( CharSequence prefix );
	/** Writes a string specified by index into a {@link MutableString}.
	 * 
	 * @param left the index of a string.
	 * @param string a mutable string.
	 * @return <code>string</code>.
	 */
	protected abstract MutableString getTerm( long left, MutableString string );
	
	public Object2ObjectFunction<CharSequence, LongInterval> rangeMap() {
		if ( rangeMap == null ) rangeMap = new AbstractObject2ObjectFunction<CharSequence, LongInterval>() {
			private static final long serialVersionUID = 1L;

			public boolean containsKey( final Object o ) {
				return get( o ) != LongIntervals.EMPTY_INTERVAL;
			}

			public int size() {
				return -1;
			}

			public LongInterval get( final Object o ) {
				return getInterval( (CharSequence)o );
			}
			
		};
		
		return rangeMap;
	}

	public Object2ObjectFunction<LongInterval, MutableString> prefixMap() {
		if ( prefixMap == null ) prefixMap = new AbstractObject2ObjectFunction<LongInterval, MutableString>() {
			private static final long serialVersionUID = 1L;

			public MutableString get( final Object o ) {
				final LongInterval interval = (LongInterval)o;
				final MutableString prefix = new MutableString();
				if ( interval == LongIntervals.EMPTY_INTERVAL || interval.left < 0 || interval.right < 0 ) throw new IllegalArgumentException();
				getTerm( interval.left, prefix );
				if ( interval.length() == 1 ) return prefix;
				final MutableString s = getTerm( interval.right, new MutableString() );
				final int l = Math.min( prefix.length(), s.length() );
				int i;
				for( i = 0; i < l; i++ ) if ( s.charAt( i ) != prefix.charAt( i ) ) break;
				return prefix.length( i );
			}

			public boolean containsKey( final Object o ) {
				LongInterval interval = (LongInterval)o;
				return interval != LongIntervals.EMPTY_INTERVAL && interval.left >= 0 && interval.right < AbstractPrefixMap.this.size64();
			}

			public int size() {
				return -1;
			}
		};
		
		return prefixMap;
	}

	public ObjectBigList<MutableString> list() {
		if ( list == null ) list = new AbstractObjectBigList<MutableString>() {
			public long size64() {
				return AbstractPrefixMap.this.size64();
			}
			public MutableString get( long index ) {
				return getTerm( index, new MutableString() );
			}
		};
		
		return list;
	}		
}
