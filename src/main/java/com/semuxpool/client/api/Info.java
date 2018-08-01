package com.semuxpool.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info
{
    private int activePeers;
    private String clientId;
    private String coinbase;
    private String latestBlockHash;
    private int latestBlockNumber;
    private int pendingTransactions;

    public int getActivePeers()
    {
        return activePeers;
    }

    public void setActivePeers(int activePeers)
    {
        this.activePeers = activePeers;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getCoinbase()
    {
        return coinbase;
    }

    public void setCoinbase(String coinbase)
    {
        this.coinbase = coinbase;
    }

    public String getLatestBlockHash()
    {
        return latestBlockHash;
    }

    public void setLatestBlockHash(String latestBlockHash)
    {
        this.latestBlockHash = latestBlockHash;
    }

    public int getLatestBlockNumber()
    {
        return latestBlockNumber;
    }

    public void setLatestBlockNumber(int latestBlockNumber)
    {
        this.latestBlockNumber = latestBlockNumber;
    }

    public int getPendingTransactions()
    {
        return pendingTransactions;
    }

    public void setPendingTransactions(int pendingTransactions)
    {
        this.pendingTransactions = pendingTransactions;
    }
}
