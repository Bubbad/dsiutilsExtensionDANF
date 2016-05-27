/**
 * <p>Miscellaneaous utility classes.
 * 
 * <h2>Pseudorandom number generators</h2>
 * 
 * <p>We provide a number of fast, high-quality PRNGs with different features.
 * <ul>
 * <li>{@link it.unimi.dsi.util.XoRoShiRo128PlusRandom <code>xoroshiro128+</code>} is a fast, top-quality generator. 
 * It has strong statistical properties and it is the fastest generator we provide. Its period (2<sup>128</sup> &minus; 1) is sufficient 
 * for any application with a reasonable amount of parallelism. It is our suggestion for an all-purpose generator.
 * <li>{@link it.unimi.dsi.util.XorShift128PlusRandom <code>xorshift128+</code>} is a fast, high-quality generator, but it has been superseded
 * by <code>xoroshiro128+</code>, which is better under every respect. It is presently used in the JavaScript engines of
 * <a href="http://v8project.blogspot.com/2015/12/theres-mathrandom-and-then-theres.html">Chrome</a>,
 * <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=322529#c99">Firefox</a> and
 * <a href="https://bugs.webkit.org/show_bug.cgi?id=151641">Safari</a>, and it is the
 * default generator in <a href="http://erlang.org/doc/man/rand.html">Erlang</a>.
 * <li>{@link it.unimi.dsi.util.SplitMix64Random <span style='font-variant: small-caps'>SplitMix64</span>} is a fast, top-quality generator, but it has a relatively short period (2<sup>64</sup>) so it should
 * not be used to generate very long sequences (the rule of thumb to have a period greater than the square of the length of the sequence you want to generate).
 * It is a non-splittable version of Java 8's <a href="http://docs.oracle.com/javase/8/docs/api/java/util/SplittableRandom.html"><code>SplittableRandom</code></a>,
 * and thus conceptually identical to Java 8's
 * <a href="http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadLocalRandom.html"><code>ThreadLocalRandom</code></a> (note that Java 7's version
 * was identical to {@link java.util.Random}, instead). We use it to initialize the state of all other generators starting from a 64-bit seed.
 * <li>{@link it.unimi.dsi.util.XorShift1024StarRandom <code>xorshift1024*</code>} is fast, has good quality 
 * and provides a long period (2<sup>1024</sup> &minus; 1) for massive parallel computations.
 * </ul>
 * 
 * <p>{@link it.unimi.dsi.util.XoRoShiRo128PlusRandom <code>xoroshiro128+</code>}, {@link it.unimi.dsi.util.XorShift128PlusRandom <code>xorshift128+</code>} and
 * {@link it.unimi.dsi.util.XorShift1024StarRandom <code>xorshift1024*</code>} provide
 * <em>{@linkplain it.unimi.dsi.util.XoRoShiRo128PlusRandom#jump() jump functions}</em> which make it possible to generate long non-overlapping sequences,
 * and <em>{@linkplain it.unimi.dsi.util.XoRoShiRo128PlusRandom#split() split functions}</em> in the spirit of {@link java.util.SplittableRandom SplittableRandom}.
 * 
 * <p>A table summarizing timings is provided below. Note that we test several different method parameters to highlight
 * the different strategies used to generate numbers in a range, as the rejection-based algorithm used by all generators
 * can be based on integer or long inputs, and the results are quite different. For example, on modern, 64-bit CPUs Java's
 * strategy of applying rejection to 32-bit integers does not pay off (see the timings for <code>nextInt(2<sup>30</sup> + 1)</code>).
 * 
 * <p>The timings are very different from previous versions, but they
 * should be more reliable, as they are now obtained by means of 
 * <a href="http://openjdk.java.net/projects/code-tools/jmh/">JMH</a> microbenchmarks. The JMH timings were decreased by 1ns, as
 * using the low-level {@code perfasm} profiler the JMH overhead was estimated at &approx;1ns per call.
 *  
 * <CENTER><TABLE SUMMARY="Timings in nanoseconds for several generators" BORDER=1>
 * <TR><TH>
 * <TH>{@link java.util.Random Random}
 * <TH><a href="http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadLocalRandom.html"><code>ThreadLocalRandom</code></a>
 * <TH><a href="http://docs.oracle.com/javase/8/docs/api/java/util/SplittableRandom.html"><code>SplittableRandom</code></a>
 * <TH>{@link it.unimi.dsi.util.SplitMix64RandomGenerator <span style='font-variant: small-caps'>SplitMix64</span>}
 * <TH>{@link it.unimi.dsi.util.XoRoShiRo128PlusRandom <code>xoroshiro128+</code>}
 * <TH>{@link it.unimi.dsi.util.XorShift128PlusRandom <code>xorshift128+</code>}
 * <TH>{@link it.unimi.dsi.util.XorShift1024StarRandom <code>xorshift1024*</code>}
 * 
 * <TR><TH STYLE='text-align: left'>nextInt()	<TD STYLE='text-align: right'>8.272<TD STYLE='text-align: right'>1.860<TD STYLE='text-align: right'>1.999<TD STYLE='text-align: right'>1.896<TD STYLE='text-align: right'>1.936<TD STYLE='text-align: right'>2.007<TD STYLE='text-align: right'>3.127
 * <TR><TH STYLE='text-align: left'>nextLong()	<TD STYLE='text-align: right'>17.549<TD STYLE='text-align: right'>1.886<TD STYLE='text-align: right'>2.123<TD STYLE='text-align: right'>2.054<TD STYLE='text-align: right'>1.994<TD STYLE='text-align: right'>2.012<TD STYLE='text-align: right'>3.106
 * <TR><TH STYLE='text-align: left'>nextDouble()	<TD STYLE='text-align: right'>17.542<TD STYLE='text-align: right'>2.686<TD STYLE='text-align: right'>3.068<TD STYLE='text-align: right'>3.004<TD STYLE='text-align: right'>2.597<TD STYLE='text-align: right'>2.740<TD STYLE='text-align: right'>3.835
 * <TR><TH STYLE='text-align: left'>nextInt(100000)	<TD STYLE='text-align: right'>8.285<TD STYLE='text-align: right'>3.163<TD STYLE='text-align: right'>3.769<TD STYLE='text-align: right'>3.713<TD STYLE='text-align: right'>3.070<TD STYLE='text-align: right'>3.264<TD STYLE='text-align: right'>4.753
 * <TR><TH STYLE='text-align: left'>nextInt(2<sup>29</sup>+2<sup>28</sup>)	<TD STYLE='text-align: right'>12.634<TD STYLE='text-align: right'>7.939<TD STYLE='text-align: right'>8.574<TD STYLE='text-align: right'>4.130<TD STYLE='text-align: right'>3.347<TD STYLE='text-align: right'>3.586<TD STYLE='text-align: right'>5.152
 * <TR><TH STYLE='text-align: left'>nextInt(2<sup>30</sup>)	<TD STYLE='text-align: right'>8.269<TD STYLE='text-align: right'>1.886<TD STYLE='text-align: right'>2.148<TD STYLE='text-align: right'>2.226<TD STYLE='text-align: right'>1.978<TD STYLE='text-align: right'>2.580<TD STYLE='text-align: right'>3.229
 * <TR><TH STYLE='text-align: left'>nextInt(2<sup>30</sup> + 1)	<TD STYLE='text-align: right'>22.152<TD STYLE='text-align: right'>15.645<TD STYLE='text-align: right'>16.483<TD STYLE='text-align: right'>3.837<TD STYLE='text-align: right'>3.244<TD STYLE='text-align: right'>3.528<TD STYLE='text-align: right'>5.004
 * <TR><TH STYLE='text-align: left'>nextInt(2<sup>30</sup> + 2<sup>29</sup>)	<TD STYLE='text-align: right'>12.581<TD STYLE='text-align: right'>7.923<TD STYLE='text-align: right'>8.578<TD STYLE='text-align: right'>4.092<TD STYLE='text-align: right'>3.344<TD STYLE='text-align: right'>3.572<TD STYLE='text-align: right'>5.138
 * <TR><TH STYLE='text-align: left'>nextLong(1000000000000)	<TD STYLE='text-align: right'><TD STYLE='text-align: right'>3.482<TD STYLE='text-align: right'>4.060<TD STYLE='text-align: right'>3.923<TD STYLE='text-align: right'>3.304<TD STYLE='text-align: right'>3.604<TD STYLE='text-align: right'>5.064
 * <TR><TH STYLE='text-align: left'>nextLong(2<sup>62</sup> + 1)	<TD STYLE='text-align: right'><TD STYLE='text-align: right'>16.090<TD STYLE='text-align: right'>16.864<TD STYLE='text-align: right'>16.811<TD STYLE='text-align: right'>13.805<TD STYLE='text-align: right'>14.878<TD STYLE='text-align: right'>18.197   
 *
 * </TABLE></CENTER>
 * 
 * <p>The quality of all generators we provide is very high: for instance, they perform better than <code>WELL1024a</code> 
 * or <code>MT19937</code> (AKA the Mersenne Twister) in the <a href="http://www.iro.umontreal.ca/~simardr/testu01/tu01.html">TestU01</a> BigCrush test suite.
 * More details can be found on the <a href="http://xorshift.di.unimi.it/"><code>xoroshiro+</code>/<code>xorshift*</code>/<code>xorshift+</code> generators and the PRNG shootout</a> page.
 * 
 * <p>For each generator, we provide a version that extends {@link java.util.Random}, overriding (as usual) the {@link java.util.Random#next(int) next(int)} method. Nonetheless,
 * since the generators are all inherently 64-bit also {@link java.util.Random#nextInt() nextInt()}, {@link java.util.Random#nextFloat() nextFloat()},
 * {@link java.util.Random#nextLong() nextLong()}, {@link java.util.Random#nextDouble() nextDouble()}, {@link java.util.Random#nextBoolean() nextBoolean()}
 * and {@link java.util.Random#nextBytes(byte[]) nextBytes(byte[])} have been overridden for speed (preserving, of course, {@link java.util.Random}'s semantics).
 * In particular, {@link java.util.Random#nextDouble() nextDouble()} and {@link java.util.Random#nextFloat() nextFloat()} 
 * use a multiplication-free conversion.
 * 
 * <p>If you do not need an instance of {@link java.util.Random}, or if you need a {@link org.apache.commons.math3.random.RandomGenerator} to use
 * with <a href="http://commons.apache.org/math/">Commons Math</a>, there is for each generator a corresponding {@link org.apache.commons.math3.random.RandomGenerator RandomGenerator}
 * implementation, which indeed we suggest to use in general if you do not need a generator implementing {@link java.util.Random}. 
 */

package it.unimi.dsi.util;
