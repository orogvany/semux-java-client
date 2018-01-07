package com.semuxpool.client.api.response;

/**
 */
public class SemuxResponse<T extends Object>
{
    private boolean success;
    private String message;
    private T result;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getResult()
    {
        return result;
    }

    public void setResult(T result)
    {
        this.result = result;
    }
}
