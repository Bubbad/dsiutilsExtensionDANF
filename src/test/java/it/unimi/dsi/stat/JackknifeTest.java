package it.unimi.dsi.stat;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2011-2016 Sebastiano Vigna 
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;


public class JackknifeTest {
	@Test
	public void test() {
		ArrayList<double[]> samples = new ArrayList<double[]>();
		samples.add( new double[] { 1 } );
		samples.add( new double[] { 2 } );
		samples.add( new double[] { 3 } );
		// Linear statistics must pass through the jackknife without bias.
		Jackknife average = Jackknife.compute( samples, Jackknife.IDENTITY );
		assertEquals( 2, average.estimate[ 0 ], 1E-30 );
		assertEquals( Math.sqrt( ( ( 1 - 2 ) * ( 1 - 2 ) + ( 3 - 2 ) * ( 3 - 2 ) ) / 6. ), average.standardError[ 0 ], 1E-30 );
	}
}
