package com.semuxpool.client;

import com.semuxpool.client.api.Account;
import com.semuxpool.client.api.Block;
import com.semuxpool.client.api.Delegate;
import com.semuxpool.client.api.Info;
import com.semuxpool.client.api.Peer;
import com.semuxpool.client.api.SemuxException;
import com.semuxpool.client.api.Transaction;
import com.semuxpool.client.api.TransactionLimits;
import com.semuxpool.client.api.response.SignMessageResponse;
import com.semuxpool.client.api.response.SignRawTransactionResponse;
import com.semuxpool.client.api.response.VerifyMessageResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 */
public interface ISemuxClient
{
    Info getInfo() throws IOException, SemuxException;

    List<Peer> getPeers() throws IOException, SemuxException;

    void addNode(String node, int port) throws IOException, SemuxException;

    void addToBlacklist(String ipAddress) throws IOException, SemuxException;

    void addToWhitelist(String ipAddress) throws IOException, SemuxException;

    Long getLatestBlockNumber() throws IOException, SemuxException;

    Block getLatestBlock() throws IOException, SemuxException;

    Block getBlock(long blockNum) throws IOException, SemuxException;

    Block getBlock(String hash) throws IOException, SemuxException;

    List<Transaction> getPendingTransactions() throws IOException, SemuxException;

    List<Transaction> getAccountTransactions(String address, int from, int to) throws IOException, SemuxException;

    Transaction getTransaction(String hash) throws IOException, SemuxException;

    String signRawTransaction(String address, String raw) throws IOException, SemuxException;

    String signMessage(String address, String message) throws IOException, SemuxException;

    String verifyMessage(String address, String message, String signature) throws IOException, SemuxException;

    void composeRawTransaction(String network, String type, long fee, int nonce, String to, long value, long timestamp, byte[] data) throws IOException, SemuxException;

    void sendTransaction(String raw) throws IOException, SemuxException;

    Account getAccount(String address) throws IOException, SemuxException;

    Delegate getDelegate(String delegate) throws IOException, SemuxException;

    List<Delegate> getDelegates() throws IOException, SemuxException;

    List<String> getValidators() throws IOException, SemuxException;

    Long getVotes(String delegate, String voterAddress) throws IOException, SemuxException;

    Map<String, Long> getVotes(String delegate) throws IOException, SemuxException;

    List<String> listAccounts() throws IOException, SemuxException;

    String createAccount() throws IOException, SemuxException;

    String transfer(long amountToSend, String from, String to, Long fee, byte[] data) throws IOException, SemuxException;

    String registerDelegate(String fromAddress, long fee, String delegateName) throws IOException, SemuxException;

    String vote(String from, String to, long value, long fee) throws IOException, SemuxException;

    String unvote(String from, String to, long value, long fee) throws IOException, SemuxException;

    List<String> getAllAccounts() throws IOException, SemuxException;

    Map<String, Long> getVotesForBlock(String delegate, long blockNum) throws IOException, SemuxException;

    TransactionLimits getTransactionLimits(String type) throws IOException, SemuxException;

}
