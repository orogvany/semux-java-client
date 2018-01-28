package com.semuxpool.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semuxpool.client.api.Account;
import com.semuxpool.client.api.Block;
import com.semuxpool.client.api.Delegate;
import com.semuxpool.client.api.Info;
import com.semuxpool.client.api.Peer;
import com.semuxpool.client.api.SemuxException;
import com.semuxpool.client.api.Transaction;
import com.semuxpool.client.api.TransactionLimits;
import com.semuxpool.client.api.response.AccountResponse;
import com.semuxpool.client.api.response.BlockResponse;
import com.semuxpool.client.api.response.DelegateResponse;
import com.semuxpool.client.api.response.DelegatesResponse;
import com.semuxpool.client.api.response.InfoResponse;
import com.semuxpool.client.api.response.LongResponse;
import com.semuxpool.client.api.response.PeersResponse;
import com.semuxpool.client.api.response.SemuxResponse;
import com.semuxpool.client.api.response.StringResponse;
import com.semuxpool.client.api.response.StringsResponse;
import com.semuxpool.client.api.response.TransactionLimitsResponse;
import com.semuxpool.client.api.response.TransactionResponse;
import com.semuxpool.client.api.response.TransactionsResponse;
import com.semuxpool.client.api.response.VotesResponse;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class SemuxClient implements ISemuxClient
{
    private static final Logger logger = LoggerFactory.getLogger(SemuxClient.class);
    private String host;
    private boolean mockPayments = false;
    private int port;
    private CloseableHttpClient httpclient;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SemuxClient(String host, int port, String user, String password)
    {
        this.host = host;
        this.port = port;

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
        provider.setCredentials(AuthScope.ANY, credentials);

        httpclient = HttpClientBuilder.create()
            .setDefaultCredentialsProvider(provider)
            .build();
    }

    @Override
    public List<Transaction> getAccountTransactions(String address, int from, int to) throws IOException, SemuxException
    {
        return makeRequest("get_account_transactions?address=" + address + "&from=" + from + "&to=" + to, TransactionsResponse.class);
    }

    @Override
    public Transaction getTransaction(String hash) throws IOException, SemuxException
    {
        return makeRequest("get_transaction?hash=" + hash, TransactionResponse.class);
    }

    @Override
    public void sendTransaction(String raw) throws IOException, SemuxException
    {
        makeRequest("send_transaction?raw=" + raw, null);
    }

    @Override
    public Map<String, Long> getVotes(String delegate) throws IOException, SemuxException
    {
        return makeRequest("get_votes?delegate=" + delegate, VotesResponse.class);
    }

    @Override
    public List<String> listAccounts() throws IOException, SemuxException
    {
        return makeRequest("list_accounts", StringsResponse.class);
    }

    @Override
    public String createAccount() throws IOException, SemuxException
    {
        return makeRequest("create_account", StringResponse.class);
    }

    @Override
    public Account getAccount(String address) throws IOException, SemuxException
    {
        return makeRequest("get_account?address=" + address, AccountResponse.class);
    }

    @Override
    public Delegate getDelegate(String delegate) throws IOException, SemuxException
    {
        return makeRequest("get_delegate?address=" + delegate, DelegateResponse.class);
    }

    @Override
    public List<Delegate> getDelegates() throws IOException, SemuxException
    {
        return makeRequest("get_delegates", DelegatesResponse.class);
    }

    @Override
    public List<String> getValidators() throws IOException, SemuxException
    {
        return makeRequest("get_validators", StringsResponse.class);
    }

    @Override
    public Long getVotes(String delegate, String voterAddress) throws IOException, SemuxException
    {
        return makeRequest("get_vote?delegate=" + delegate + "&voter=" + voterAddress, LongResponse.class);
    }

    @Override
    public Info getInfo() throws IOException, SemuxException
    {
        return makeRequest("get_info", InfoResponse.class);
    }

    @Override
    public List<Peer> getPeers() throws IOException, SemuxException
    {
        return makeRequest("get_peers", PeersResponse.class);
    }

    @Override
    public void addNode(String node, int port) throws IOException, SemuxException
    {
        makeRequest("add_node?node=" + node + ":" + port, null);
    }

    @Override
    public void addToBlacklist(String ipAddress) throws IOException, SemuxException
    {
        makeRequest("add_to_blacklist?ip=" + ipAddress, null);
    }

    @Override
    public void addToWhitelist(String ipAddress) throws IOException, SemuxException
    {
        makeRequest("add_to_whitelist?ip=" + ipAddress, null);
    }

    @Override
    public Long getLatestBlockNumber() throws IOException, SemuxException
    {
        return makeRequest("get_latest_block_number", LongResponse.class);
    }

    @Override
    public Block getLatestBlock() throws IOException, SemuxException
    {
        return makeRequest("get_latest_block", BlockResponse.class);
    }

    @Override
    public Block getBlock(long blockNum) throws IOException, SemuxException
    {
        return makeRequest("get_block?number=" + blockNum, BlockResponse.class);
    }

    @Override
    public Block getBlock(String hash) throws IOException, SemuxException
    {
        return makeRequest("get_block?hash=" + hash, BlockResponse.class);
    }

    @Override
    public List<Transaction> getPendingTransactions() throws IOException, SemuxException
    {
        return makeRequest("get_pending_transactions", TransactionsResponse.class);
    }

    @Override
    public String transfer(long amountToSend, String from, String to, long fee, byte[] data) throws IOException, SemuxException
    {
        if (mockPayments)
        {
            logger.info("Skipping payment to " + to + " as we are in debug mode");
            return "mockPayment";
        }
        else
        {
            String url = "transfer?from=" + from + "&to=" + to + "&fee=" + fee + "&value=" + amountToSend;
            if (data != null)
            {
                url += "&data=" + Hex.encodeHexString(data);
            }
            return makeRequest(url, StringResponse.class);
        }
    }

    @Override
    public String registerDelegate(String address, long fee, String delegateName) throws IOException, SemuxException
    {
        String hexDelegateName = Hex.encodeHexString(delegateName.getBytes());
        return makeRequest("delegate?from=" + address + "&fee=" + fee + "&data=" + hexDelegateName, StringResponse.class);
    }

    @Override
    public String vote(String from, String to, long value, long fee) throws IOException, SemuxException
    {
        return makeRequest("vote?from=" + from + "&fee=" + fee + "&to=" + to + "&value=" + value, StringResponse.class);
    }

    @Override
    public String unvote(String from, String to, long value, long fee) throws IOException, SemuxException
    {
        return makeRequest("unvote?from=" + from + "&fee=" + fee + "&to=" + to + "&value=" + value, StringResponse.class);
    }

    @Override
    public List<String> getAllAccounts() throws IOException, SemuxException
    {
        return makeRequest("list_accounts", StringsResponse.class);
    }

    @Override
    public Map<String, Long> getVotesForBlock(String delegate, long blockNum) throws IOException, SemuxException
    {
        List<Transaction> transactions = getAccountTransactions(delegate, 0, Integer.MAX_VALUE);
        //tally them up to blockNum
        Block block = getBlock(blockNum);

        Map<String, Long> votes = new HashMap<>();
        for (Transaction transaction : transactions)
        {
            if (transaction.getTimestamp() > block.getTimestamp())
            {
                break;
            }
            long valueToAdd = 0;
            if (transaction.getType().equals("VOTE"))
            {
                valueToAdd = transaction.getValue();
            }
            else if (transaction.getType().equals("UNVOTE"))
            {
                valueToAdd = 0 - transaction.getValue();
            }
            if (valueToAdd != 0)
            {
                Long currentVal = votes.get(transaction.getFrom());
                if (currentVal == null)
                {
                    currentVal = 0l;
                }
                currentVal += valueToAdd;
                if (currentVal < 0)
                {
                    logger.error("Negative vote amount from " + transaction.getFrom());
                }
                votes.put(transaction.getFrom(), currentVal);
            }
        }
        return votes;
    }

    @Override
    public TransactionLimits getTransactionLimits(String type) throws IOException, SemuxException
    {
        return makeRequest("get_transaction_limits?type=" + type, TransactionLimitsResponse.class);
    }

    private class SimpleResponseHandler implements ResponseHandler<String>
    {
        @Override
        public String handleResponse(
            final HttpResponse response) throws IOException
        {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300)
            {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
            else
            {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }
    }

    public boolean isMockPayments()
    {
        return mockPayments;
    }

    /**
     * allow for non-breaking API calls. won't send or vote
     *
     * @param mockPayments mockPayments
     */
    public void setMockPayments(boolean mockPayments)
    {
        this.mockPayments = mockPayments;
    }

    private <T> T makeRequest(String path, Class<? extends SemuxResponse<T>> responseClass) throws IOException, SemuxException
    {
        HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/" + path);

        ResponseHandler<String> responseHandler = new SimpleResponseHandler();
        String responseString = httpclient.execute(httpGet, responseHandler);

        //handle unknown type
        if (responseClass == null)
        {
            SemuxResponse response = objectMapper.readValue(responseString, SemuxResponse.class);
            if (!response.isSuccess())
            {
                throw new SemuxException(response.getMessage());
            }
            else
            {
                return null;
            }
        }
        else
        {
            SemuxResponse<T> response = objectMapper.readValue(responseString, responseClass);
            if (response.isSuccess())
            {
                return response.getResult();
            }
            else
            {
                throw new SemuxException(response.getMessage());
            }
        }
    }
}
