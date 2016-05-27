package it.unimi.dsi.lang;

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

public class TwoStrings {
	private final String a;
	private final String b;
	private final Object context;
	public void test() {}
	
	public TwoStrings( String a, String b ) {
		this( null, a, b );
	}

	public TwoStrings( String... a ) {
		this( null, a );
	}
	
	public static TwoStrings getInstance( String a ) {
		return new TwoStrings( a, a );
	}
	
	public static TwoStrings getInstance( String... a ) {
		return getInstance( Integer.toString( a.length ) );
	}
	
	public TwoStrings( Object context, String a, String b ) {
		this.a = a;
		this.b = b;
		this.context = context;
	}

	public TwoStrings( Object context, String... a ) {
		this.a = a[ 0 ];
		this.b = Integer.toString( a.length );
		this.context = context;
	}
	
	public static TwoStrings getInstance( Object context, String a ) {
		return new TwoStrings( context, a, a );
	}
	
	public static TwoStrings getInstance( Object context, String... a ) {
		return getInstance( context, Integer.toString( a.length ) );
	}
	
	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) return true;
		if ( obj == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		final TwoStrings other = (TwoStrings)obj;
		if ( a == null ) {
			if ( other.a != null ) return false;
		}
		else if ( !a.equals( other.a ) ) return false;
		if ( b == null ) {
			if ( other.b != null ) return false;
		}
		else if ( !b.equals( other.b ) ) return false;
		if ( context == null ) {
			if ( other.context != null ) return false;
		}
		else if ( !context.equals( other.context ) ) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "(" + context + ", " + a + ", " + b + ")";
	}
}
