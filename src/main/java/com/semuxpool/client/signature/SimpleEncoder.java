/**
 * Copyright (c) 2017-2018 The Semux Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.semuxpool.client.signature;

import com.semuxpool.client.api.SemuxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SimpleEncoder
{
    private final ByteArrayOutputStream out;

    public SimpleEncoder(byte[] toAppend) throws SemuxException
    {
        this.out = new ByteArrayOutputStream();
        try
        {
            out.write(toAppend);
        } catch (IOException e)
        {
            throw new SemuxException(e);
        }
    }

    public SimpleEncoder() throws SemuxException
    {
        this(Bytes.EMPTY_BYTES);
    }

    public SimpleEncoder(int size)
    {
        out = new ByteArrayOutputStream(size);
    }

    public void writeBoolean(boolean b)
    {
        out.write(b ? 1 : 0);
    }

    public void writeByte(byte b)
    {
        out.write(b);
    }

    public void writeShort(short s)
    {
        out.write(0xFF & (s >>> 8));
        out.write(0xFF & s);
    }

    public void writeInt(int i)
    {
        out.write(0xFF & (i >>> 24));
        out.write(0xFF & (i >>> 16));
        out.write(0xFF & (i >>> 8));
        out.write(0xFF & i);
    }

    public void writeLong(long l)
    {
        int i1 = (int) (l >>> 32);
        int i2 = (int) l;

        writeInt(i1);
        writeInt(i2);
    }

    /**
     * Encode a byte array.
     *
     * @param bytes the byte array to encode
     * @param vlq   should always be true unless we're providing pre-mainnet support.
     */
    public void writeBytes(byte[] bytes, boolean vlq) throws SemuxException
    {
        if (vlq)
        {
            writeSize(bytes.length);
        } else
        {
            writeInt(bytes.length);
        }

        try
        {
            out.write(bytes);
        } catch (IOException e)
        {
            throw new SemuxException(e);
        }
    }

    public void writeBytes(byte[] bytes) throws SemuxException
    {
        writeBytes(bytes, true);
    }

    public void writeString(String s) throws SemuxException
    {
        writeBytes(Bytes.of(s));
    }

    public byte[] toBytes()
    {
        return out.toByteArray();
    }

    public int getWriteIndex()
    {
        return out.size();
    }

    /**
     * Writes a size into the output byte array.
     *
     * @param size
     * @throws IllegalArgumentException when the input size is negative
     */
    protected void writeSize(int size)
    {
        if (size < 0)
        {
            throw new IllegalArgumentException("Size can't be negative: " + size);
        } else if (size > 0x0FFFFFFF)
        {
            throw new IllegalArgumentException("Size can't be larger than 0x0FFFFFFF: " + size);
        }

        int[] buf = new int[4];
        int i = buf.length;
        do
        {
            buf[--i] = size & 0x7f;
            size >>>= 7;
        } while (size > 0);

        while (i < buf.length)
        {
            if (i != buf.length - 1)
            {
                out.write((byte) (buf[i++] | 0x80));
            } else
            {
                out.write((byte) buf[i++]);
            }
        }
    }
}
