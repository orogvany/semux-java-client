package com.semuxpool.client.api;

/**
 */
public class Account
{
    private String address;
    private Long available;
    private Long locked;
    private long nonce;

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Long getAvailable()
    {
        return available;
    }

    public void setAvailable(Long available)
    {
        this.available = available;
    }

    public Long getLocked()
    {
        return locked;
    }

    public void setLocked(Long locked)
    {
        this.locked = locked;
    }

    public long getNonce()
    {
        return nonce;
    }

    public void setNonce(long nonce)
    {
        this.nonce = nonce;
    }
}
