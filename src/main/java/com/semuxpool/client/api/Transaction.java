package com.semuxpool.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction
{
    private String hash;
    private String type;
    private String from;
    private String to;
    private long value;
    private long fee;
    private long nonce;
    private long timestamp;
    private String date;
    private String data;
    private Long blockNumber;


    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public long getValue()
    {
        return value;
    }

    public void setValue(long value)
    {
        this.value = value;
    }

    public long getFee()
    {
        return fee;
    }

    public void setFee(long fee)
    {
        this.fee = fee;
    }

    public long getNonce()
    {
        return nonce;
    }

    public void setNonce(long nonce)
    {
        this.nonce = nonce;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public Long getBlockNumber()
    {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber)
    {
        this.blockNumber = blockNumber;
    }
}
