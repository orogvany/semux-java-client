package com.semuxpool.client;

import com.semuxpool.client.api.*;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
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

    String signMessageLocal(String privateKey, String message) throws SemuxException, InvalidKeySpecException;

    String verifyMessage(String address, String message, String signature) throws IOException, SemuxException;

    String composeRawTransaction(String network, String type, long fee, long nonce, String to, long value, long timestamp, byte[] data) throws IOException, SemuxException;

    String sendTransaction(String raw) throws IOException, SemuxException;

    String sendTransaction(String raw, String signature) throws IOException, SemuxException;

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

    String create(String from, long gasPrice, long gas, byte[] data) throws IOException, SemuxException;

    String call(String from, String to, long gasPrice, long gas, byte[] data, boolean local) throws IOException, SemuxException;
}
