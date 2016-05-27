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

import org.junit.Test;

public class SummaryStatsTest {
	@Test
	public void test() {
		SummaryStats summaryStats = new SummaryStats();
		summaryStats.add( 0 );
		assertEquals( 0, summaryStats.sum(), 0 );
		assertEquals( 0, summaryStats.mean(), 0 );
		assertEquals( 0, summaryStats.variance(), 0 );
		assertEquals( 0, summaryStats.min(), 0 );
		assertEquals( 0, summaryStats.max(), 0 );
		assertEquals( 1, summaryStats.size64() );
		
		summaryStats.add( 1 );
		assertEquals( 1, summaryStats.sum(), 0 );
		assertEquals( .5, summaryStats.mean(), 0 );
		assertEquals( .25, summaryStats.variance(), 0 );
		assertEquals( 0, summaryStats.min(), 0 );
		assertEquals( 1, summaryStats.max(), 0 );
		assertEquals( 2, summaryStats.size64() );
	}
}
