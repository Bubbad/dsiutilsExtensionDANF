package it.unimi.dsi.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2003-2016 Paolo Boldi and Sebastiano Vigna 
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

import java.util.Comparator;

                                                                                                                                                             
/** A class providing static methods and objects that do useful things with intervals. 
 * 
 * @see LongInterval
 */

public class LongIntervals {

	private LongIntervals() {}

	public final static LongInterval[] EMPTY_ARRAY = {};

	/** An empty (singleton) interval. */
	public final static LongInterval EMPTY_INTERVAL = new LongInterval( 1, 0 );

	/** A singleton located at &minus;&#8734;. */
	public final static LongInterval MINUS_INFINITY = new LongInterval( Integer.MIN_VALUE, Integer.MIN_VALUE );

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval starts <em>after</em> the second one, that is, 
	 * iff <var>a</var>' &lt; <var>a</var>.
	 */
	public final static Comparator<LongInterval> STARTS_AFTER = new Comparator<LongInterval>() {
		public int compare( final LongInterval i1, final LongInterval i2 ) {
			if ( i1.left != i2.left ) return i2.left < i1.left ? -1 : 1;
			return 0;
		}
	};                                                                                                                    

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval starts <em>before</em> the second one, that is, 
	 * iff <var>a</var> &lt; <var>a</var>'.
	 */
	public final static Comparator<LongInterval> STARTS_BEFORE = new Comparator<LongInterval>() {
		public int compare( final LongInterval i1, final LongInterval i2 ) {
			if ( i1.left != i2.left ) return i2.left < i1.left ? 1 : -1;
			return 0;
		}
	};                                                                                                                    

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval ends <em>after</em> the second one, that is, 
	 * iff <var>b</var>' &lt; <var>b</var>.
	 */
	public final static Comparator<LongInterval> ENDS_AFTER = new Comparator<LongInterval>() {
		public int compare( final LongInterval i1, final LongInterval i2 ) {
			if ( i1.right != i2.right ) return i2.right < i1.right ? -1 : 1;
			return 0;
		}
	};                                                                                                                    

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval ends <em>before</em> the second one, that is, 
	 * iff <var>b</var> &lt; <var>b</var>'.
	 */
	public final static Comparator<LongInterval> ENDS_BEFORE = new Comparator<LongInterval>() {
		public int compare( final LongInterval i1, final LongInterval i2 ) {
			if ( i1.right != i2.right ) return i2.right < i1.right ? 1 : -1;
			return 0;
		}
	};                                                                                                                    

	/** A comparator between intervals based on their length. */
	public final static Comparator<LongInterval> LENGTH_COMPARATOR = new Comparator<LongInterval>() {
		public int compare( final LongInterval i1, final LongInterval i2 ) {
			return (int)Math.signum( i1.length() - i2.length() );
		}
	};                                                                                                                    
}
                                                                                                                                                        