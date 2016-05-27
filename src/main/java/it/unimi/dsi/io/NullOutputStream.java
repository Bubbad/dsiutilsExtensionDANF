package it.unimi.dsi.io;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2003-2016 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.io.RepositionableStream;

import java.io.IOException;
import java.io.OutputStream;

/** Throw-it-away output stream.
 *
 * <P>This stream discards whatever is written into it. Its usefulness is in
 * previewing the length of some coding by wrapping it in an {@link
 * OutputBitStream} (it is a good idea, in this case, {@linkplain
 * OutputBitStream#OutputBitStream(java.io.OutputStream,int) to specify a 0-length buffer}).
 *
 * <P>This class is a singleton. You cannot create a null output stream,
 * but you can obtain an instance of this class using {@link #getInstance()}.
 *
 * @author Sebastiano Vigna
 * @since 0.6
 */

public class NullOutputStream extends OutputStream implements RepositionableStream {

	private final static NullOutputStream SINGLETON = new NullOutputStream();

	private NullOutputStream() {}
	 
	public void write( final int discarded ) {}

	/** Returns the only instance of this class. */
	public static NullOutputStream getInstance() {
		return SINGLETON;
	}

	public long position() throws IOException {
		return 0;
	}

	public void position( long newPosition ) throws IOException {}
}
