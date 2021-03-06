package it.unimi.dsi.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2015-2016 Sebastiano Vigna 
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


import it.unimi.dsi.Util;
import it.unimi.dsi.fastutil.HashCommon;

import java.util.Random;

/** A fast, top-quality, non-splittable version of the <span style="font-variant: small-caps">SplitMix</span> 
 * pseudorandom number generator used by Java 8's 
 * <a href="http://docs.oracle.com/javase/8/docs/api/java/util/SplittableRandom.html"><code>SplittableRandom</code></a>. Due to 
 * the fixed increment constant and to different strategies in generating finite ranges, the methods of this generator
 * are usually faster than those of <code>SplittableRandom</code>.
 * 
 * <p>Note that this generator has a relatively short period (2<sup>64</sup>) so it should
 * not be used to generate very long sequences (the rule of thumb to have a period 
 * greater than the square of the length of the sequence you want to generate).
 *
 * @see it.unimi.dsi.util
 * @see Random
 * @see SplitMix64RandomGenerator
 */
public class SplitMix64Random extends Random {
	private static final long serialVersionUID = 1L;
	/** 2<sup>64</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
	private static final long PHI = 0x9E3779B97F4A7C15L;

	/** The internal state of the algorithm (a Weyl generator using the {@link #PHI} as increment). */
	private long x;

	/** Creates a new generator seeded using {@link Util#randomSeed()}. */
	public SplitMix64Random() {
		this( Util.randomSeed() );
	}

	/** Creates a new generator using a given seed.
	 * 
	 * @param seed a nonzero seed for the generator (if zero, the generator will be seeded with -1).
	 */
	public SplitMix64Random( final long seed ) {
		setSeed( seed );
	}

	/* David Stafford's (http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html)
     * "Mix13" variant of the 64-bit finalizer in Austin Appleby's MurmurHash3 algorithm. */
	private static long staffordMix13( long z ) {
		z = ( z ^ ( z >>> 30 ) ) * 0xBF58476D1CE4E5B9L; 
		z = ( z ^ ( z >>> 27 ) ) * 0x94D049BB133111EBL;
		return z ^ ( z >>> 31 );
	}

	/* David Stafford's (http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html)
     * "Mix4" variant of the 64-bit finalizer in Austin Appleby's MurmurHash3 algorithm. */
	private static int staffordMix4Upper32( long z ) {
		z = ( z ^ ( z >>> 33 ) ) * 0x62A9D9ED799705F5L;
		return (int)( ( ( z ^ ( z >>> 28 ) ) * 0xCB24D0A5C88C35B3L ) >>> 32 );
	}

	@Override
	public long nextLong() {
		return staffordMix13( x += PHI );
	}

	@Override
	public int nextInt() {
		return staffordMix4Upper32( x += PHI );
	}
	
	@Override
	public int nextInt( final int n ) {
		return (int)nextLong(n);
	}
	
	/** Returns a pseudorandom uniformly distributed {@code long} value
     * between 0 (inclusive) and the specified value (exclusive), drawn from
     * this random number generator's sequence. The algorithm used to generate
     * the value guarantees that the result is uniform, provided that the
     * sequence of 64-bit values produced by this generator is. 
     * 
     * @param n the positive bound on the random number to be returned.
     * @return the next pseudorandom {@code long} value between {@code 0} (inclusive) and {@code n} (exclusive).
     */
	public long nextLong( final long n ) {
		if (n <= 0) throw new IllegalArgumentException("illegal bound " + n + " (must be positive)");
		long t = staffordMix13(x += PHI);
		final long nMinus1 = n - 1;
		// Shortcut for powers of two
		if ((n & nMinus1) == 0) return t & nMinus1;
		// Rejection-based algorithm to get uniform integers in the general case
		for (long u = t >>> 1; u + nMinus1 - (t = u % n) < 0; u = staffordMix13(x += PHI) >>> 1);
		return t;
	}	
	
	@Override
	public double nextDouble() {
		return Double.longBitsToDouble( staffordMix13( x += PHI ) >>> 12 | 0x3FFL << 52 ) - 1.0;
	}

	@Override
	public float nextFloat() {
		return Float.intBitsToFloat( staffordMix4Upper32( x += PHI ) >>> 41 | 0x3F8 << 20 ) - 1.0f;
	}

	@Override
	public boolean nextBoolean() {
		return staffordMix4Upper32( x += PHI ) < 0;
	}
	
	@Override
	public void nextBytes( final byte[] bytes ) {
		int i = bytes.length, n = 0;
		while( i != 0 ) {
			n = Math.min( i, 8 );
			for ( long bits = staffordMix13( x += PHI ); n-- != 0; bits >>= 8 ) bytes[ --i ] = (byte)bits;
		}
	}


	/** Sets the seed of this generator.
	 * 
	 * <p>The seed will be passed through {@link HashCommon#murmurHash3(long)}.
	 * 
	 * @param seed a seed for this generator.
	 */
	@Override
	public void setSeed( final long seed ) {
		x = HashCommon.murmurHash3( seed );
	}


	/** Sets the state of this generator.
	 * 
	 * @param state the new state for this generator (must be nonzero).
	 */
	public void setState( final long state ) {
		x = state;
	}
}
