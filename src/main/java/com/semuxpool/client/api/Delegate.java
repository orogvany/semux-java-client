package com.semuxpool.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delegate
{
    private String address;
    private String name;
    private Long registeredAt;
    private Long votes;
    private Long blocksForged;
    private Long turnsHit;
    private Long turnsMissed;
    private Boolean validator;

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getRegisteredAt()
    {
        return registeredAt;
    }

    public void setRegisteredAt(Long registeredAt)
    {
        this.registeredAt = registeredAt;
    }

    public Long getVotes()
    {
        return votes;
    }

    public void setVotes(Long votes)
    {
        this.votes = votes;
    }

    public Long getBlocksForged()
    {
        return blocksForged;
    }

    public void setBlocksForged(Long blocksForged)
    {
        this.blocksForged = blocksForged;
    }

    public Long getTurnsHit()
    {
        return turnsHit;
    }

    public void setTurnsHit(Long turnsHit)
    {
        this.turnsHit = turnsHit;
    }

    public Long getTurnsMissed()
    {
        return turnsMissed;
    }

    public void setTurnsMissed(Long turnsMissed)
    {
        this.turnsMissed = turnsMissed;
    }

    public Boolean getValidator() {
        return validator;
    }

    public void setValidator(Boolean validator) {
        this.validator = validator;
    }
}
