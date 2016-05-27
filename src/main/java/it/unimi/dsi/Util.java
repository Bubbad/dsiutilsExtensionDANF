package it.unimi.dsi;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2002-2016 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.longs.LongBigArrays;
import it.unimi.dsi.util.XorShift128PlusRandomGenerator;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;

/** All-purpose static-method container class.
 * 
 * @author Sebastiano Vigna
 * @since 0.1 
 */

public final class Util {
	private Util() {}

	/** A reasonable format for real numbers. Shared by all format methods. */
	private static final NumberFormat FORMAT_DOUBLE = NumberFormat.getInstance( Locale.US );
	static {
		if ( FORMAT_DOUBLE instanceof DecimalFormat ) ((DecimalFormat)FORMAT_DOUBLE).applyPattern( "#,##0.00" );
	}
	/** A reasonable format for integers. Shared by all format methods. */
	private static final NumberFormat FORMAT_LONG = NumberFormat.getInstance( Locale.US );
	static {
		if ( FORMAT_DOUBLE instanceof DecimalFormat ) ((DecimalFormat)FORMAT_LONG).applyPattern( "#,###" );
	}

	private static final FieldPosition UNUSED_FIELD_POSITION = new java.text.FieldPosition( 0 );
	
	/** Formats a number.
	 *
	 * <P>This method formats a double separating thousands and printing just two fractional digits. 
	 * <p>Note that the method is synchronized, as it uses a static {@link NumberFormat}.
	 * @param d a number.
	 * @return a string containing a pretty print of the number.
	 */
	public synchronized static String format( final double d ) {
		return FORMAT_DOUBLE.format( d, new StringBuffer(), UNUSED_FIELD_POSITION ).toString();
	}
	
	/** Formats a number.
	 *
	 * <P>This method formats a long separating thousands.
	 * <p>Note that the method is synchronized, as it uses a static {@link NumberFormat}.
	 * @param l a number.
	 * @return a string containing a pretty print of the number.
	 */
	public synchronized static String format( final long l ) {
		return FORMAT_LONG.format( l, new StringBuffer(), UNUSED_FIELD_POSITION ).toString();
	}

	/** Formats a number using a specified {@link NumberFormat}.
	 *
	 * @param d a number.
	 * @param format a format.
	 * @return a string containing a pretty print of the number.
	 */
	public static String format( final double d, final NumberFormat format ) {
		final StringBuffer s = new StringBuffer();
		return format.format( d, s, UNUSED_FIELD_POSITION ).toString();
	}
	
	
	/** Formats a number using a specified {@link NumberFormat}.
	 * 
	 * @param l a number.
	 * @param format a format.
	 * @return a string containing a pretty print of the number.
	 */
	public static String format( final long l, final NumberFormat format ) {
		final StringBuffer s = new StringBuffer();
		return format.format( l, s, UNUSED_FIELD_POSITION ).toString();
	}

	/** Formats a size.
	 *
	 * <P>This method formats a long using suitable unit multipliers (e.g., <code>K</code>, <code>M</code>, <code>G</code>, and <code>T</code>)
	 * and printing just two fractional digits.
	 * <p>Note that the method is synchronized, as it uses a static {@link NumberFormat}.
	 * @param l a number, representing a size (e.g., memory).
	 * @return a string containing a pretty print of the number using unit multipliers.
	 */
	public static String formatSize( final long l ) {
		if ( l >= 1000000000000L ) return format( l / 1000000000000.0 ) + "T";
		if ( l >= 1000000000L ) return format( l / 1000000000.0 ) + "G";
		if ( l >= 1000000L ) return format( l / 1000000.0 ) + "M";
		if ( l >= 1000L ) return format( l / 1000.0 ) + "K";
		return Long.toString( l );
	}

	/** Formats a binary size.
	 *
	 * <P>This method formats a long using suitable unit binary multipliers (e.g., <code>Ki</code>, <code>Mi</code>, <code>Gi</code>, and <code>Ti</code>)
	 * and printing <em>no</em> fractional digits. The argument must be a power of 2.
	 * <p>Note that the method is synchronized, as it uses a static {@link NumberFormat}.
	 * @param l a number, representing a binary size (e.g., memory); must be a power of 2.
	 * @return a string containing a pretty print of the number using binary unit multipliers.
	 */
	public static String formatBinarySize( final long l ) {
		if ( ( l & -l ) != l ) throw new IllegalArgumentException( "Not a power of 2: " + l );
		if ( l >= ( 1L << 40 ) ) return format( l >> 40 ) + "Ti";
		if ( l >= ( 1L << 30 ) ) return format( l >> 30 ) + "Gi";
		if ( l >= ( 1L << 20 ) ) return format( l >> 20 ) + "Mi";
		if ( l >= ( 1L << 10 ) ) return format( l >> 10 ) + "Ki";
		return Long.toString( l );
	}

	/** Formats a size.
	 *
	 * <P>This method formats a long using suitable binary
	 * unit multipliers (e.g., <code>Ki</code>, <code>Mi</code>, <code>Gi</code>, and <code>Ti</code>)
	 * and printing just two fractional digits.
	 * <p>Note that the method is synchronized, as it uses a static {@link NumberFormat}.
	 * @param l a number, representing a size (e.g., memory).
	 * @return a string containing a pretty print of the number using binary unit multipliers.
	 */
	public static String formatSize2( final long l ) {
		if ( l >= 1L << 40 ) return format( (double)l / ( 1L << 40 ) ) + "Ti";
		if ( l >= 1L << 30 ) return format( (double)l / ( 1L << 30 ) ) + "Gi";
		if ( l >= 1L << 20 ) return format( (double)l / ( 1L << 20 ) ) + "Mi";
		if ( l >= 1L << 10 ) return format( (double)l / ( 1L << 10 ) ) + "Ki";
		return Long.toString( l );
	}

	/** Formats a size using a specified {@link NumberFormat}.
	 *
	 * <P>This method formats a long using suitable unit multipliers (e.g., <code>K</code>, <code>M</code>, <code>G</code>, and <code>T</code>)
	 * and the given {@link NumberFormat} for the digits.
	 * @param l a number, representing a size (e.g., memory).
	 * @param format a format.
	 * @return a string containing a pretty print of the number using unit multipliers.
	 */
	public static String formatSize( final long l, final NumberFormat format ) {
		if ( l >= 1000000000000L ) return format( l / 1000000000000.0 ) + "T";
		if ( l >= 1000000000L ) return format( l / 1000000000.0 ) + "G";
		if ( l >= 1000000L ) return format( l / 1000000.0 ) + "M";
		if ( l >= 1000L ) return format( l / 1000.0 ) + "K";
		return Long.toString( l );
	}

	/** Formats a size using a specified {@link NumberFormat}.
	 *
	 * <P>This method formats a long using suitable unit binary multipliers (e.g., <code>Ki</code>, <code>Mi</code>, <code>Gi</code>, and <code>Ti</code>)
 	 * and the given {@link NumberFormat} for the digits. The argument must be a power of 2.
	 * @param l a number, representing a binary size (e.g., memory); must be a power of 2.
	 * @param format a format.
	 * @return a string containing a pretty print of the number using binary unit multipliers.
	 */
	public static String formatBinarySize( final long l, final NumberFormat format ) {
		if ( ( l & -l ) != l ) throw new IllegalArgumentException( "Not a power of 2: " + l );
		if ( l >= ( 1L << 40 ) ) return format( l >> 40 ) + "Ti";
		if ( l >= ( 1L << 30 ) ) return format( l >> 30 ) + "Gi";
		if ( l >= ( 1L << 20 ) ) return format( l >> 20 ) + "Mi";
		if ( l >= ( 1L << 10 ) ) return format( l >> 10 ) + "Ki";
		return Long.toString( l );
	}

	/** Formats a size using a specified {@link NumberFormat} and binary unit multipliers.
	 *
	 * <P>This method formats a long using suitable binary
	 * unit multipliers (e.g., <code>Ki</code>, <code>Mi</code>, <code>Gi</code>, and <code>Ti</code>)
	 * and the given {@link NumberFormat} for the digits.
	 * @param l a number, representing a size (e.g., memory).
	 * @param format a format.
	 * @return a string containing a pretty print of the number using binary unit multipliers.
	 */
	public static String formatSize2( final long l, final NumberFormat format ) {
		if ( l >= 1L << 40 ) return format( (double)l / ( 1L << 40 ) ) + "Ti";
		if ( l >= 1L << 30 ) return format( (double)l / ( 1L << 30 ) ) + "Gi";
		if ( l >= 1L << 20 ) return format( (double)l / ( 1L << 20 ) ) + "Mi";
		if ( l >= 1L << 10 ) return format( (double)l / ( 1L << 10 ) ) + "Ki";
		return Long.toString( l );
	}
	
	/** A static reference to {@link Runtime#getRuntime()}. */
	public final static Runtime RUNTIME = Runtime.getRuntime();
	
	/** Returns true if less then 5% of the available memory is free.
	 * 
	 * @return true if less then 5% of the available memory is free.
	 */
	public static boolean memoryIsLow() {
		return availableMemory() * 100 < RUNTIME.totalMemory() * 5; 
	}

	/** Returns the amount of available memory (free memory plus never allocated memory).
	 * 
	 * @return the amount of available memory, in bytes.
	 */
	public static long availableMemory() {
		return RUNTIME.freeMemory() + ( RUNTIME.maxMemory() - RUNTIME.totalMemory() ); 
	}

	/** Returns the percentage of available memory (free memory plus never allocated memory).
	 * 
	 * @return the percentage of available memory.
	 */
	public static int percAvailableMemory() {
		return (int)( ( Util.availableMemory() * 100 ) / Runtime.getRuntime().maxMemory() ); 
	}

	/** Tries to compact memory as much as possible by forcing garbage collection.
	 */
	public static void compactMemory() {
		try {
			final byte[][] unused = new byte[ 128 ][]; 
			for( int i = unused.length; i-- != 0; ) unused[ i ] = new byte[ 2000000000 ];
		}
		catch ( OutOfMemoryError itsWhatWeWanted ) {}
		System.gc();
	}
	
	private static final XorShift128PlusRandomGenerator seedUniquifier = new XorShift128PlusRandomGenerator( System.nanoTime() );	

	/** Returns a random seed generated by taking the output of a {@link XorShift128PlusRandomGenerator}
	 * (seeded at startup with {@link System#nanoTime()}) and xoring it with {@link System#nanoTime()}.
	 * 
	 * @return a reasonably good random seed. 
	 */
	public static long randomSeed() {
		final long x;
		synchronized( seedUniquifier ) {
			x = seedUniquifier.nextLong();
		}
		return x ^ System.nanoTime();
	}

	/** Returns a random seed generated by {@link #randomSeed()} under the form of an array of eight bytes.
	 * 
	 * @return a reasonably good random seed.
	 */
	public static byte[] randomSeedBytes() {
		final long seed = Util.randomSeed();
		final byte[] s = new byte[ 8 ];
		for( int i = Long.SIZE / Byte.SIZE; i-- != 0; ) s[ i ] = (byte)( seed >>> i );
		return s;
	}

	/** Computes in place the inverse of a permutation expressed
	 * as an array of <var>n</var> distinct integers in [0&nbsp;..&nbsp;<var>n</var>).
	 * 
	 * <p><strong>Warning</strong>: if <code>perm</code> is not a permutation,
	 * essentially anything can happen.
	 * 
	 * @param perm the permutation to be inverted.
	 * @return <code>perm</code>.
	 */
	
	public static int[] invertPermutationInPlace( int[] perm ) {
		for( int n = perm.length; n-- != 0; ) {
			int i = perm[ n ];
			if ( i < 0 ) perm[ n ] = -i - 1;
			else if ( i != n ) {
				int j, k = n;
				
				for(;;) {
					j = perm[ i ];
					perm[ i ] = -k - 1;
					if ( j == n ) {
						perm[ n ] = i;
						break;
					}
					k = i;
					i = j;
				}
			}
		}
		
		return perm;
	}
	
	/** Computes the inverse of a permutation expressed
	 * as an array of <var>n</var> distinct integers in [0&nbsp;..&nbsp;<var>n</var>).
	 * 
	 * <p><strong>Warning</strong>: if <code>perm</code> is not a permutation,
	 * essentially anything can happen.
	 * 
	 * @param perm the permutation to be inverted.
	 * @param inv the array storing the inverse. 
	 * @return <code>inv</code>.
	 */
	
	public static int[] invertPermutation( int[] perm, int[] inv ) {
		for( int i = perm.length; i-- != 0; ) inv[ perm[ i ] ]  = i;
		return inv;
	}
	
	/** Computes the inverse of a permutation expressed
	 * as an array of <var>n</var> distinct integers in [0&nbsp;..&nbsp;<var>n</var>) 
	 * and stores the result in a new array.
	 * 
	 * <p><strong>Warning</strong>: if <code>perm</code> is not a permutation,
	 * essentially anything can happen.
	 * 
	 * @param perm the permutation to be inverted.
	 * @return a new array containing the inverse permutation.
	 */
	
	public static int[] invertPermutation( int[] perm ) {
		return invertPermutation( perm, new int[ perm.length ] );
	}
	
	/** Stores the identity permutation in an array.
	 * 
	 * @param perm an array of integers.
	 * @return <code>perm</code>, filled with the identity permutation.
	 */
	
	public static int[] identity( int[] perm ) {
		for( int i = perm.length; i-- != 0; ) perm[ i ] = i;
		return perm;
	}

	/** Stores the identity permutation in a new array of given length.
	 * 
	 * @param n the size of the array.
	 * @return a new array of length <code>n</code>, filled with the identity permutation.
	 */
	
	public static int[] identity( int n ) {
		return identity( new int[ n ] );
	}


	/** Computes in place the inverse of a permutation expressed
	 * as a {@linkplain BigArrays big array} of <var>n</var> distinct long integers in [0&nbsp;..&nbsp;<var>n</var>).
	 * 
	 * <p><strong>Warning</strong>: if <code>perm</code> is not a permutation,
	 * essentially anything can happen.
	 * 
	 * @param perm the permutation to be inverted.
	 * @return <code>perm</code>.
	 */
	
	public static long[][] invertPermutationInPlace( long[][] perm ) {
		for( long n = LongBigArrays.length( perm ); n-- != 0; ) {
			long i = LongBigArrays.get( perm, n );
			if ( i < 0 ) LongBigArrays.set( perm, n, -i - 1 );
			else if ( i != n ) {
				long j, k = n;
				
				for(;;) {
					j = LongBigArrays.get( perm, i );
					LongBigArrays.set( perm, i, -k - 1 );
					if ( j == n ) {
						LongBigArrays.set( perm, n, i );
						break;
					}
					k = i;
					i = j;
				}
			}
		}
		
		return perm;
	}
	
	/** Computes the inverse of a permutation expressed
	 * as a {@linkplain BigArrays big array} of <var>n</var> distinct long integers in [0&nbsp;..&nbsp;<var>n</var>).
	 * 
	 * <p><strong>Warning</strong>: if <code>perm</code> is not a permutation,
	 * essentially anything can happen.
	 * 
	 * @param perm the permutation to be inverted.
	 * @param inv the big array storing the inverse. 
	 * @return <code>inv</code>.
	 */
	
	public static long[][] invertPermutation( long[][] perm, long[][] inv ) {
		for( int i = perm.length; i-- != 0; ) {
			final long t[] = perm[ i ];
			for( int d = t.length; d-- != 0; ) LongBigArrays.set( inv, t[ d ], BigArrays.index( i, d ) );
		}
		return inv;
	}
	
	/** Computes the inverse of a permutation expressed
	 * as a {@linkplain BigArrays big array} of <var>n</var> distinct long integers in [0&nbsp;..&nbsp;<var>n</var>) 
	 * and stores the result in a new big array.
	 * 
	 * <p><strong>Warning</strong>: if <code>perm</code> is not a permutation,
	 * essentially anything can happen.
	 * 
	 * @param perm the permutation to be inverted.
	 * @return a new big array containing the inverse permutation.
	 */
	
	public static long[][] invertPermutation( long[][] perm ) {
		return invertPermutation( perm, LongBigArrays.newBigArray( LongBigArrays.length( perm ) ) );
	}
	
	/** Stores the identity permutation in a {@linkplain BigArrays big array}. 
	 * 
	 * @param perm a big array.
	 * @return <code>perm</code>, filled with the identity permutation.
	 */
	
	public static long[][] identity( long[][] perm ) {
		for( int i = perm.length; i-- != 0; ) {
			final long[] t = perm[ i ];  
			for( int d = t.length; d-- != 0; ) t[ d ] = BigArrays.index( i, d );
		}
		return perm;
	}

	/** Stores the identity permutation in a new big array of given length.
	 * 
	 * @param n the size of the array.
	 * @return a new array of length <code>n</code>, filled with the identity permutation.
	 */
	
	public static long[][] identity( long n ) {
		return identity( LongBigArrays.newBigArray( n ) );
	}
}
