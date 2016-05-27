package it.unimi.dsi.big.io;

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

import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream.LineTerminator;
import it.unimi.dsi.fastutil.objects.ObjectBigArrayBigList;
import it.unimi.dsi.fastutil.objects.ObjectBigList;
import it.unimi.dsi.io.SafelyCloseable;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;

/** A wrapper exhibiting the lines of a file as a {@link java.util.Collection} of byte arrays.
 * 
 * <P>An instance of this class allows to access the lines of a file as a
 * {@link java.util.Collection} of byte arrays. Reading is performed using
 * {@link FastBufferedInputStream#readLine(byte[], EnumSet)}, and follows the rules defined therein.
 * No decoding is performed.
 *  
 * <p>Using {@linkplain java.util.Collection#contains(java.lang.Object)
 * direct access} is strongly discouraged (it will require a full scan of the file), but
 * the {@link #iterator()} can be fruitfully used to scan the file, and can be called any
 * number of times, as it opens an independent input stream at each call. For the
 * same reason, the returned iterator type ({@link it.unimi.dsi.io.FileLinesCollection.FileLinesIterator})
 * is {@link java.io.Closeable}, and should be closed after usage.
 * 
 * <p>Using a suitable {@linkplain #FileLinesByteArrayCollection(CharSequence, boolean, EnumSet) constructor}, it is possible
 * to specify that the file is compressed in <code>gzip</code> format (in this case, it will be opened using a {@link GZIPInputStream}).
 * 
 * <P>Note that the first call to {@link #size64()} will require a full file scan.  
 * 
 * @author Sebastiano Vigna
 * @since 2.2.8
 */
public class FileLinesByteArrayCollection extends AbstractCollection<byte[]> implements Size64 {
	/** The filename upon which this file-lines collection is based. */
	private final String filename;
	/** Whether {@link #filename} is zipped. */
	private final boolean zipped;
	/** A set of terminators for the underlying {@link FastBufferedInputStream}. */
	private final EnumSet<LineTerminator> terminators;
	/** The cached size of the collection. */
	private long size = -1;

	/** Creates a byte-array file-lines collection for the specified filename, using both CR and LF as {@linkplain LineTerminator line terminators}.
	 * 
	 * @param filename a filename.
	 */		
	public FileLinesByteArrayCollection( final CharSequence filename ) {
		this( filename, false );
	}

	/** Creates a byte-array file-lines collection for the specified filename, optionally assuming
	 * that the file is compressed using <code>gzip</code> format, using both CR and LF as {@linkplain LineTerminator line terminators}.
	 * 
	 * @param filename a filename.
	 * @param zipped whether <code>filename</code> is zipped.
	 */		
	public FileLinesByteArrayCollection( final CharSequence filename, final boolean zipped ) {
		this( filename, zipped, FastBufferedInputStream.ALL_TERMINATORS );
	}

	/** Creates a byte-array file-lines collection for the specified filename, optionally assuming
	 * that the file is compressed using <code>gzip</code> format, using the specified {@linkplain LineTerminator line terminators}. 
	 * 
	 * @param filename a filename.
	 * @param zipped whether <code>filename</code> is zipped.
	 * @param terminators line terminators for the underlying {@link FastBufferedInputStream}.
	 */		
	public FileLinesByteArrayCollection( final CharSequence filename, final boolean zipped, final EnumSet<LineTerminator> terminators ) {
		this.zipped = zipped;
		this.filename = filename.toString();
		this.terminators = terminators;
	}

	
	/** An iterator over the lines of a {@link FileLinesByteArrayCollection}. 
	 * 
	 * <p>Instances of this class open an {@link java.io.InputStream}, and thus should be {@linkplain Closeable#close() closed} after
	 * usage. A &ldquo;safety-net&rdquo; finaliser tries to take care of the cases in which
	 * closing an instance is impossible. An exhausted iterator, however, will be closed automagically.
	 */
	
	public static final class FileLinesIterator implements Iterator<byte[]>, SafelyCloseable {
		private FastBufferedInputStream fbis;
		byte[] buffer = new byte[ 1024 ];

		boolean ready;
		int read;
		private EnumSet<LineTerminator> terminators;

		private FileLinesIterator( final String filename, final boolean zipped, final EnumSet<LineTerminator> terminators ) {
			this.terminators = terminators;
			try {
				fbis = new FastBufferedInputStream( zipped ? new GZIPInputStream( new FileInputStream( filename ) ) : new FileInputStream( filename ) );
			} catch (IOException e) {
				throw new RuntimeException( e );
			}
		}

		public boolean hasNext() {
			if ( ready ) return true;
			if ( fbis == null ) return false;
			ready = false;
			try {
				read = 0;
				int len;
				while( ( len = fbis.readLine( buffer, read, buffer.length - read, terminators ) ) == buffer.length - read ) {
					ready = true;
					read += len;
					buffer = ByteArrays.grow( buffer, buffer.length + 1 );
				}
				if ( len != -1 ) {
					ready = true;
					read += len;
				}
				if ( len == -1 ) close();
			} catch (IOException e) {
				throw new RuntimeException( e );
			}
			
			return ready;
		}

		public byte[] next() {
			if ( ! hasNext() ) throw new NoSuchElementException();
			ready = false;
			return Arrays.copyOf( buffer, read );
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public synchronized void close() {
			if ( fbis == null ) return;
			try {
				fbis.close();
			}
			catch ( IOException e ) {
				throw new RuntimeException( e );
			}
			finally {
				fbis = null;
			}
		}
		
		protected synchronized void finalize() throws Throwable {
			try {
				if ( fbis != null ) close();
			}
			finally {
				super.finalize();
			}
		}
	}

	public FileLinesIterator iterator() {
		return new FileLinesIterator( filename, zipped, terminators );
	}
		
	public synchronized int size() {
		return (int)Math.min( Integer.MAX_VALUE, size );
	}	

	public synchronized long size64() {
		if ( size == -1 ) {
			FileLinesIterator i = iterator();
			size = 0;
			while( i.hasNext() ) {
				size++;
				i.next();
			}
			i.close();
		}
		return size;
	}
		
	/** Returns all lines of the file wrapped by this file-lines collection.
	 * 
	 * @return all lines of the file wrapped by this file-lines collection.
	 */
	public ObjectBigList<byte[]> allLines() {
		final ObjectBigList<byte[]> result = new ObjectBigArrayBigList<byte[]>();
		for( Iterator<byte[]> i = iterator(); i.hasNext(); ) result.add( i.next() );
		return result;
	}

	@Override
	@Deprecated
	public Object[] toArray() {
		throw new UnsupportedOperationException( "Use allLines()" );
	}

	@Override
	@Deprecated
	public <T> T[] toArray( T[] a ) {
		throw new UnsupportedOperationException( "Use allLines()" );
	}

	public String toString() {
		return this.getClass().getSimpleName() + "(" + filename + "," + zipped + ")";
	}
}
