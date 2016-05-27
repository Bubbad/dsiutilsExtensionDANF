package it.unimi.dsi.big.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2008-2016 Sebastiano Vigna 
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
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunctions;
import it.unimi.dsi.fastutil.objects.ObjectBigList;
import it.unimi.dsi.fastutil.objects.ObjectBigLists;
import it.unimi.dsi.util.Interval;
import it.unimi.dsi.util.LongInterval;

import java.io.Serializable;

/** A class providing static methods and objects that do useful things with {@linkplain StringMap string maps}
 * and {@linkplain PrefixMap prefix maps}.
 * 
 * @see StringMap
 * @see PrefixMap
 * @author Sebastiano Vigna
 * @since 2.0
 */

public class StringMaps {
	private StringMaps() {}
	
	protected static class SynchronizedStringMap<S extends CharSequence> implements StringMap<S>, Serializable {
		private static final long serialVersionUID = 1L;
		protected final StringMap<S> stringMap;
		protected ObjectBigList<? extends S> list;

		public SynchronizedStringMap( final StringMap<S> stringMap ) {
			this.stringMap = stringMap;
		}

		@Deprecated
		public synchronized int size() {
			return stringMap.size();
		}

		public synchronized long size64() {
			return stringMap.size64();
		}

		public synchronized ObjectBigList<? extends S> list() {
			if ( list == null ) {
				list = stringMap.list();
				if( list != null ) list = ObjectBigLists.synchronize( list, this ); 
			}
			return list;
		}

		public synchronized long getLong( Object s ) {
			return stringMap.getLong( s );
		}	

		public synchronized Long get( Object key ) {
			return stringMap.get( key );
		}

		public synchronized long put( CharSequence key, long value ) {
			return stringMap.put(  key, value );
		}

		public synchronized Long put( CharSequence key, Long value ) {
			return stringMap.put( key, value );
		}

		public synchronized Long remove( Object key ) {
			return stringMap.remove( key );
		}

		public synchronized long removeLong( Object key ) {
			return stringMap.removeLong( key );
		}
		
		public synchronized void clear() {
			stringMap.clear();
		}

		public synchronized boolean containsKey( Object key ) {
			return stringMap.containsKey( key );
		}

		public synchronized long defaultReturnValue() {
			return stringMap.defaultReturnValue();
		}

		public synchronized void defaultReturnValue( long rv ) {
			stringMap.defaultReturnValue( rv );
		}
	}
	

	protected static class SynchronizedPrefixMap<S extends CharSequence> extends SynchronizedStringMap<S> implements PrefixMap<S>, Serializable {
		private static final long serialVersionUID = 1L;
		protected final PrefixMap<S> map;
		protected Object2ObjectFunction<LongInterval, S> prefixMap;
		protected Object2ObjectFunction<CharSequence, LongInterval> rangeMap;

		public SynchronizedPrefixMap( final PrefixMap<S> map ) {
			super( map );
			this.map = map;
		}

		public synchronized Object2ObjectFunction<LongInterval, S> prefixMap() {
			if ( prefixMap == null ) {
				prefixMap = map.prefixMap();
				if ( prefixMap != null ) prefixMap = Object2ObjectFunctions.synchronize( prefixMap, this );
			}
			return prefixMap;
		}

		public synchronized Object2ObjectFunction<CharSequence, LongInterval> rangeMap() {
			if ( rangeMap == null ) {
				rangeMap = map.rangeMap();
				if ( rangeMap != null ) rangeMap = Object2ObjectFunctions.synchronize( rangeMap, this );
			}
			return rangeMap;
		}


	}
	
	/** Returns a synchronized string map backed by the given string map.
     *
     * @param stringMap the string map to be wrapped in a synchronized map.
     * @return a synchronized view of the specified string map.
     */
	public static <T extends CharSequence> StringMap<T> synchronize( final StringMap<T> stringMap ) {
		return stringMap instanceof PrefixMap ? new SynchronizedPrefixMap<T>( (PrefixMap<T>)stringMap ) : new SynchronizedStringMap<T>( stringMap );
	}

	/** Returns a synchronized prefix map backed by the given prefix map.
    *
    * @param prefixMap the prefix map to be wrapped in a synchronized map.
    * @return a synchronized view of the specified prefix map.
    */
	public static <T extends CharSequence> PrefixMap<T> synchronize( final PrefixMap<T> prefixMap ) {
		return new SynchronizedPrefixMap<T>( prefixMap );
	}

	protected static class StringMapWrapper<T extends CharSequence> extends AbstractObject2LongFunction<CharSequence> implements StringMap<T> {
		private static final long serialVersionUID = 1L;
		private final it.unimi.dsi.util.StringMap<T> stringMap;

		public StringMapWrapper( final it.unimi.dsi.util.StringMap<T> stringMap ) {
			this.stringMap = stringMap;
		}

		@Override
		public long getLong( final Object key ) {
			return stringMap.getLong( key );
		}

		@Override
		public boolean containsKey( final Object key ) {
			return stringMap.containsKey( key );
		}

		@Override
		public int size() {
			return stringMap.size();
		}

		@Override
		public long size64() {
			return stringMap.size();
		}

		@Override
		public ObjectBigList<? extends T> list() {
			return ObjectBigLists.asBigList( stringMap.list() );
		}
	}
	
	/** Returns an immutable (big) {@link StringMap} view of a standard {@link it.unimi.dsi.util.StringMap}.
	 * 
	 * @param stringMap a string map.
	 * @return a {@link StringMap} view of {@code stringMap}.
	 */
	
	public static <T extends CharSequence> StringMap<T> wrap( final it.unimi.dsi.util.StringMap<T> stringMap ) {
		return new StringMapWrapper<T>( stringMap );
	}

	protected static class PrefixMapWrapper<T extends CharSequence> extends StringMapWrapper<T> implements PrefixMap<T> {
		private static final long serialVersionUID = 1L;
		private final Object2ObjectFunction<CharSequence, LongInterval> rangeMap;

		public PrefixMapWrapper( final it.unimi.dsi.util.PrefixMap<T> prefixMap ) {
			super( prefixMap );
			rangeMap = new AbstractObject2ObjectFunction<CharSequence, LongInterval>() {
				private static final long serialVersionUID = 1L;
				private final Object2ObjectFunction<CharSequence, Interval> prefixMapRangeMap = prefixMap.rangeMap();

				@Override
				public LongInterval get( final Object key ) {
					final Interval interval = prefixMapRangeMap.get( key );
					return LongInterval.valueOf( interval.left, interval.right );
				}

				@Override
				public boolean containsKey( final Object key ) {
					return prefixMapRangeMap.containsKey( key );
				}

				@Override
				public int size() {
					return prefixMapRangeMap.size();
				}
			};
		}

		@Override
		public Object2ObjectFunction<CharSequence, LongInterval> rangeMap() {
			return rangeMap;
		}

		@Override
		public Object2ObjectFunction<LongInterval, T> prefixMap() {
			return null;
		}
	}
	
	/** Returns an immutable (big) {@link PrefixMap} view of a standard {@link it.unimi.dsi.util.PrefixMap}. Note that
	 * the returned prefix map does not implement {@link PrefixMap#prefixMap()}.
	 * 
	 * @param prefixMap a prefix map.
	 * @return a {@link PrefixMap} view of {@code prefixMap}.
	 */

	public static <T extends CharSequence> PrefixMap<T> wrap( final it.unimi.dsi.util.PrefixMap<T> prefixMap ) {
		return new PrefixMapWrapper<T>( prefixMap );
	}
}
