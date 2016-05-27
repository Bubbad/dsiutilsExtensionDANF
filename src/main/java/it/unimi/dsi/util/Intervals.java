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
 * @see Interval
 */

public class Intervals {

	private Intervals() {}

	public final static Interval[] EMPTY_ARRAY = {};

	/** An empty (singleton) interval. */
	public final static Interval EMPTY_INTERVAL = new Interval( 1, 0 );

	/** A singleton located at &minus;&#8734;. */
	public final static Interval MINUS_INFINITY = new Interval( Integer.MIN_VALUE, Integer.MIN_VALUE );

	/** A comparator between intervals defined as follows: 
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>'] iff
	 * the first interval starts before or prolongs the second one, that is,  
	 * iff <var>a</var> &lt; <var>a</var>' or <var>a</var>=<var>a</var>' and <var>b</var>' &lt; <var>b</var>.
	 */
	public final static Comparator<Interval> STARTS_BEFORE_OR_PROLONGS = new Comparator<Interval>() {
		public int compare( final Interval i1, final Interval i2 ) {
			final int t = i1.left - i2.left;
			if ( t != 0 ) return t;
			return i2.right - i1.right;
		}
	};

	/** A comparator between intervals defined as follows: 
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>'] iff
	 * the first interval ends before or is a suffix of the second one, that is,  
	 * iff <var>b</var> &lt; <var>b</var>' or <var>b</var>=<var>b</var>' and <var>a</var>' &lt; <var>a</var>.
	 */
	public final static Comparator<Interval> ENDS_BEFORE_OR_IS_SUFFIX = new Comparator<Interval>() {
		public int compare( final Interval i1, final Interval i2 ) {
			final int t = i1.right - i2.right;
			if ( t != 0 ) return t;
			return i2.left - i1.left;
		}
	};

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval starts <em>after</em> the second one, that is, 
	 * iff <var>a</var>' &lt; <var>a</var>.
	 */
	public final static Comparator<Interval> STARTS_AFTER = new Comparator<Interval>() {
		public int compare( final Interval i1, final Interval i2 ) {
			return i2.left - i1.left;
		}
	};                                                                                                                    

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval starts <em>before</em> the second one, that is, 
	 * iff <var>a</var> &lt; <var>a</var>'.
	 */
	public final static Comparator<Interval> STARTS_BEFORE = new Comparator<Interval>() {
		public int compare( final Interval i1, final Interval i2 ) {
			return i1.left - i2.left;
		}
	};                                                                                                                    

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval ends <em>after</em> the second one, that is, 
	 * iff <var>b</var>' &lt; <var>b</var>.
	 */
	public final static Comparator<Interval> ENDS_AFTER = new Comparator<Interval>() {
		public int compare( final Interval i1, final Interval i2 ) {
			return i2.right - i1.right;
		}
	};                                                                                                                    

	/** A comparator between intervals defined as follows:
	 * [<var>a</var>..<var>b</var>] is less than [<var>a</var>'..<var>b</var>']
	 * iff the first interval ends <em>before</em> the second one, that is, 
	 * iff <var>b</var> &lt; <var>b</var>'.
	 */
	public final static Comparator<Interval> ENDS_BEFORE = new Comparator<Interval>() {
		public int compare( final Interval i1, final Interval i2 ) {
			return i1.right - i2.right;
		}
	};                                                                                                                    

	/** A comparator between intervals based on their length. */
	public final static Comparator<Interval> LENGTH_COMPARATOR = new Comparator<Interval>() {
		public int compare( final Interval i1, final Interval i2 ) {
			return i1.length() - i2.length();
		}
	};                                                                                                                    
}
                                                                                                                                                        