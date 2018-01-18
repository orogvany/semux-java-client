package com.semuxpool.client.api;

/**
 */
public class TransactionLimits
{
    private Long minTransactionFee;
    private Long minDelegateBurnAmount;
    private Integer maxTransactionDataSize;

    public Long getMinTransactionFee()
    {
        return minTransactionFee;
    }

    public void setMinTransactionFee(Long minTransactionFee)
    {
        this.minTransactionFee = minTransactionFee;
    }

    public Long getMinDelegateBurnAmount()
    {
        return minDelegateBurnAmount;
    }

    public void setMinDelegateBurnAmount(Long minDelegateBurnAmount)
    {
        this.minDelegateBurnAmount = minDelegateBurnAmount;
    }

    public Integer getMaxTransactionDataSize()
    {
        return maxTransactionDataSize;
    }

    public void setMaxTransactionDataSize(Integer maxTransactionDataSize)
    {
        this.maxTransactionDataSize = maxTransactionDataSize;
    }
}
