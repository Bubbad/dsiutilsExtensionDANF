package it.unimi.dsi.util;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2013-2016 Sebastiano Vigna 
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
import it.unimi.dsi.logging.ProgressLogger;

import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;

/** A fast, top-quality {@linkplain Random pseudorandom number generator} that
 * returns the sum of consecutive outputs of a handcrafted linear generator with 128 bits of state. It improves 
 * upon {@link XorShift128PlusRandom xorshift128+} 
 * under every respect: it is faster and has stronger statistical properties.  
 * More details can be found on the <a href="http://xorshift.di.unimi.it/"><code>xoroshiro+</code>/<code>xorshift*</code>/<code>xorshift+</code> 
 * generators and the PRNG shootout</a> page.
 *
 * <p>Note that this is
 * <strong>not</strong> a cryptographic-strength pseudorandom number generator, but its quality is
 * preposterously higher than {@link Random}'s, and its cycle length is
 * 2<sup>128</sup>&nbsp;&minus;&nbsp;1, which is more than enough for any single-thread application.
 * 
 * <p>By using the supplied {@link #jump()} method it is possible to generate non-overlapping long sequences
 * for parallel computations. This class provides also a {@link #split()} method to support recursive parallel computations, in the spirit of 
 * Java 8's <a href="http://docs.oracle.com/javase/8/docs/api/java/util/SplittableRandom.html"><code>SplittableRandom</code></a>.
 * 
 * @see it.unimi.dsi.util
 * @see RandomGenerator
 */

@SuppressWarnings("javadoc")
public class XoRoShiRo128PlusRandom extends Random {
	private static final long serialVersionUID = 1L;
	/** The internal state of the algorithm. */
	private long s0, s1;
	
	/** Creates a new generator seeded using {@link Util#randomSeed()}. */
	public XoRoShiRo128PlusRandom() {
		this( Util.randomSeed() );
	}

	/** Creates a new generator using a given seed.
	 * 
	 * @param seed a nonzero seed for the generator (if zero, the generator will be seeded with -1).
	 */
	public XoRoShiRo128PlusRandom( final long seed ) {
		setSeed( seed );
	}

	@Override
	public long nextLong() {
		final long s0 = this.s0;
		long s1 = this.s1;
		final long result = s0 + s1;
		s1 ^= s0;
		this.s0 = Long.rotateLeft(s0, 55) ^ s1 ^ s1 << 14;
		this.s1 = Long.rotateLeft(s1, 36);
		return result;
	}

	@Override
	public int nextInt() {
		return (int)nextLong();
	}
	
	@Override
	public int nextInt( final int n ) {
		return (int)nextLong( n );
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
	public long nextLong(final long n) {
		if (n <= 0) throw new IllegalArgumentException("illegal bound " + n + " (must be positive)");
		long t = nextLong();
		final long nMinus1 = n - 1;
		// Shortcut for powers of two
		if ((n & nMinus1) == 0) return t & nMinus1;
		// Rejection-based algorithm to get uniform integers in the general case
		for (long u = t >>> 1; u + nMinus1 - (t = u % n) < 0; u = nextLong() >>> 1);
		return t;
	}
	
	@Override
	public double nextDouble() {
		return Double.longBitsToDouble( nextLong() >>> 12 | 0x3FFL << 52 ) - 1.0;
	}

	@Override
	public float nextFloat() {
		return Float.intBitsToFloat( (int)( nextLong() >>> 41 ) | 0x3F8 << 20 ) - 1.0f;
	}

	@Override
	public boolean nextBoolean() {
		return nextLong() < 0;
	}
	
	@Override
	public void nextBytes( final byte[] bytes ) {
		int i = bytes.length, n = 0;
		while( i != 0 ) {
			n = Math.min( i, 8 );
			for ( long bits = nextLong(); n-- != 0; bits >>= 8 ) bytes[ --i ] = (byte)bits;
		}
	}

	private static final long JUMP[] = 	{ 0xbeac0467eba5facbL, 0xd86b048b86aa9922L };

	/** The the jump function for this generator. It is equivalent to 2<sup>64</sup> 
	 * calls to {@link #nextLong()}; it can be used to generate 2<sup>64</sup> 
	 * non-overlapping subsequences for parallel computations. */

	public void jump() {
		long s0 = 0;
		long s1 = 0;
		for(int i = 0; i < JUMP.length; i++)
			for(int b = 0; b < 64; b++) {
				if ( ( JUMP[i] & 1L << b ) != 0 ) {
					s0 ^= this.s0;
					s1 ^= this.s1;
				}
				nextLong();
			}

		this.s0 = s0;
		this.s1 = s1;
	}

	/**
     * Returns a new instance that shares no mutable state 
     * with this instance. The sequence generated by the new instance
     * depends deterministically from the state of this instance,
     * but the probability that the sequence generated by this
     * instance and by the new instance overlap is negligible.
     *
     * @return the new instance.
     */
	public XoRoShiRo128PlusRandom split() {
		final XoRoShiRo128PlusRandom split = new XoRoShiRo128PlusRandom();
		split.s0 = HashCommon.murmurHash3( s0 );
		split.s1 = HashCommon.murmurHash3( s1 );
		return split;
	}

	/** Sets the seed of this generator.
	 * 
	 * <p>The argument will be used to seed a {@link SplitMix64RandomGenerator}, whose output
	 * will in turn be used to seed this generator. This approach makes &ldquo;warmup&rdquo; unnecessary,
	 * and makes the probability of starting from a state 
	 * with a large fraction of bits set to zero astronomically small.
	 * 
	 * @param seed a nonzero seed for this generator.
	 */
	@Override
	public void setSeed( final long seed ) {
		final SplitMix64RandomGenerator r = new SplitMix64RandomGenerator( seed );
		s0 = r.nextLong();
		s1 = r.nextLong();
	}


	/** Sets the state of this generator.
	 * 
	 * <p>The internal state of the generator will be reset, and the state array filled with the provided array.
	 * 
	 * @param state an array of 2 longs; at least one must be nonzero.
	 */
	public void setState( final long[] state ) {
		if ( state.length != 2 ) throw new IllegalArgumentException( "The argument array contains " + state.length + " longs instead of " + 2 );
		s0 = state[ 0 ];
		s1 = state[ 1 ];
	}

	public static void main( String[] arg ) {
		long n = Long.parseLong( arg[ 0 ] );
		long x = 0;
		ProgressLogger pl = new ProgressLogger();
		XoRoShiRo128PlusRandom r = new XoRoShiRo128PlusRandom(0);
		for( int k = 10; k-- != 0; ) {
			pl.start( "Measuring..." );
			for ( long i = n; i-- != 0; )
				x ^= r.nextLong();
			pl.done( n );
			if ( x == 0 ) System.out.println( x );
		}
	}
}
