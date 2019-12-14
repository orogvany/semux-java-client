package com.semuxpool.client.api;

/**
 */
public class SemuxException extends Exception
{
    public SemuxException(String message)
    {
        super(message);
    }

    public SemuxException(Exception e)
    {
        super(e.getMessage(), e);
    }
}
