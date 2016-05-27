package it.unimi.dsi.compression;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2005-2016 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.io.InputBitStream;

import java.io.IOException;

/** Decoding methods for a specific compression technique. */
public interface Decoder {

	/** Decodes the next symbol from the given boolean iterator.
	 * 
	 * <P>Note that {@link InputBitStream} implements {@link BooleanIterator}.
	 * 
	 * @param iterator a boolean iterator.
	 * @return the next symbol decoded from the bits emitted by <code>i</code>
	 * @throws java.util.NoSuchElementException if <code>iterator</code> terminates before a symbol has been decoded.
	 */
	int decode( BooleanIterator iterator );

	/** Decodes the next symbol from the given input bit stream.
	 * 
	 * <P>Note that {@link InputBitStream} implements {@link BooleanIterator}.
	 * 
	 * @param ibs an input bit stream.
	 * @return the next symbol decoded from <code>ibs</code>.
	 */
	int decode( InputBitStream ibs ) throws IOException;
}
