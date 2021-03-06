package com.semuxpool.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer
{
    private String ip;
    private int port;
    private int networkVersion;
    private String clientId;
    private String peerId;
    private int latestBlockNumber;
    private List<String> capabilities;

    private int latency;

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public int getNetworkVersion()
    {
        return networkVersion;
    }

    public void setNetworkVersion(int networkVersion)
    {
        this.networkVersion = networkVersion;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getPeerId()
    {
        return peerId;
    }

    public void setPeerId(String peerId)
    {
        this.peerId = peerId;
    }

    public int getLatestBlockNumber()
    {
        return latestBlockNumber;
    }

    public void setLatestBlockNumber(int latestBlockNumber)
    {
        this.latestBlockNumber = latestBlockNumber;
    }

    public int getLatency()
    {
        return latency;
    }

    public void setLatency(int latency)
    {
        this.latency = latency;
    }

    public List<String> getCapabilities()
    {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities)
    {
        this.capabilities = capabilities;
    }
}
