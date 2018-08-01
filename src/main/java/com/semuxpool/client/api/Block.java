package com.semuxpool.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block
{
    private String hash;
    private Long number;
    private Long view;
    private String coinbase;
    private String parentHash;
    private long timestamp;
    private String date;
    private String transactionsRoot;
    private String resultsRoot;
    private String stateRoot;
    private String data;
    private List<Transaction> transactions;

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public Long getNumber()
    {
        return number;
    }

    public void setNumber(Long number)
    {
        this.number = number;
    }

    public Long getView()
    {
        return view;
    }

    public void setView(Long view)
    {
        this.view = view;
    }

    public String getCoinbase()
    {
        return coinbase;
    }

    public void setCoinbase(String coinbase)
    {
        this.coinbase = coinbase;
    }

    public String getParentHash()
    {
        return parentHash;
    }

    public void setParentHash(String parentHash)
    {
        this.parentHash = parentHash;
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

    public String getTransactionsRoot()
    {
        return transactionsRoot;
    }

    public void setTransactionsRoot(String transactionsRoot)
    {
        this.transactionsRoot = transactionsRoot;
    }

    public String getResultsRoot()
    {
        return resultsRoot;
    }

    public void setResultsRoot(String resultsRoot)
    {
        this.resultsRoot = resultsRoot;
    }

    public String getStateRoot()
    {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot)
    {
        this.stateRoot = stateRoot;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public List<Transaction> getTransactions()
    {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions)
    {
        this.transactions = transactions;
    }
}
