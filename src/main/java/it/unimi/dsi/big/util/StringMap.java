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


import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.objects.Object2LongFunction;
import it.unimi.dsi.fastutil.objects.ObjectBigList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.io.Serializable;

/** A map from strings to numbers (and possibly <i>vice versa</i>).
 * 
 * <p>String maps represent mappings from strings (actually, any subclass of {@link CharSequence})
 * to numbers; they can support {@linkplain #list() reverse
 * mapping}, too. The latter has usually sense only if the map is minimal and perfect (e.g., a bijection of a set
 * of string with an initial segment of the natural numbers of the same size). String maps are useful for
 * terms of an <a href="http://mg4j.dsi.unimi.it/">MG4J</a>
 * inverted index, URLs of a <a href="http://webgraph.dsi.unimi.it/">WebGraph</a>-compressed
 * web snapshot, and so on.
 * 
 * <p><strong>Warning</strong>: the return value of {@link #list()} is a <code>fastutil</code> {@link ObjectList}.
 * This in principle is not sensible, as string maps return longs (they extend
 * {@link Object2LongFunction}), and {@link ObjectList} has only integer index
 * support. At some point in the future, this problem will be addressed with a full-blown hierarchy
 * of long lists (and corresponding iterators)
 * in <code><a href="http://fastutil.dsi.unimi.it/">fastutil</a></code>.
 *  
 * @author Sebastiano Vigna 
 * @since 2.0
 */

public interface StringMap<S extends CharSequence> extends Object2LongFunction<CharSequence>, Size64, Serializable {
	
	/** Returns a list view of the domain of this string map (optional operation). 
	 * 
	 * <p>Note that the list view acts as an inverse of the mapping implemented by this map.
	 * 
	 * @return a list view of the domain of this string map, or {@code null} if this map does
	 * not support this operation.
	 */
	
	public ObjectBigList<? extends S> list();
}
