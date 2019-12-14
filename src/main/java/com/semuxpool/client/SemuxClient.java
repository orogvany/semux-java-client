package com.semuxpool.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semuxpool.client.api.*;
import com.semuxpool.client.api.response.*;
import com.semuxpool.client.signature.Hash;
import com.semuxpool.client.signature.Hex;
import com.semuxpool.client.signature.Key;
import com.semuxpool.client.signature.SimpleEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
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

    private String protocolPrefix = "http://";

    public SemuxClient(String host, int port, String user, String password)
    {
        this.host = host;
        this.port = port;

        CredentialsProvider provider = new BasicCredentialsProvider();
        if (user != null && user.trim().length() > 0)
        {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
            provider.setCredentials(AuthScope.ANY, credentials);
        }
        if (port == 443)
        {
            protocolPrefix = "https://";
        }

        httpclient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();
    }

    @Override
    public List<Transaction> getAccountTransactions(String address, int from, int to) throws IOException, SemuxException
    {
        return makeRequest("account/transactions?address=" + address + "&from=" + from + "&to=" + to, TransactionsResponse.class);
    }

    @Override
    public Transaction getTransaction(String hash) throws IOException, SemuxException
    {
        return makeRequest("transaction?hash=" + hash, TransactionResponse.class);
    }

    @Override
    public String signRawTransaction(String address, String raw) throws IOException, SemuxException
    {
        return makeRequest("sign-raw-transaction?address=" + address + "&raw=" + raw, SignRawTransactionResponse.class);
    }

    @Override
    public String signMessage(String address, String message) throws IOException, SemuxException
    {
        return makePostRequest("sign-message?address=" + address + "&message=" + message, SignMessageResponse.class, null);
    }

    @Override
    public String signMessageLocal(String privateKey, String message) throws SemuxException, InvalidKeySpecException
    {
        Key key = new Key(Hex.decode0x(privateKey));
        byte[] hash = Hash.h256(Hex.decode0x(message));
        Key.Signature signedMessage = key.sign(hash);
        return Hex.encode0x(signedMessage.toBytes());
    }

    @Override
    public String verifyMessage(String address, String message, String signature) throws IOException, SemuxException
    {
        return makeRequest("verify-message?address=" + address + "&message=" + message, VerifyMessageResponse.class);
    }

    @Override
    public String composeRawTransaction(String network, String type, long fee, long nonce, String to, long value, long timestamp, byte[] data) throws IOException, SemuxException
    {
        if (data == null)
        {
            data = new byte[0];
        }
        return makeRequest("compose-raw-transaction?network=" + network + "&type=" + type + "&fee=" + fee + "&nonce=" + nonce + "&to=" + to + "&value=" + value + "&timestamp=" + timestamp + "&data=" + Hex.encode0x(data), ComposeRawTransactionResponse.class);
    }

    @Override
    public String sendTransaction(String raw) throws IOException, SemuxException
    {
        return makePostRequest("transaction/raw?raw=" + raw, SignMessageResponse.class, null);
    }

    @Override
    public String sendTransaction(String raw, String signature) throws IOException, SemuxException
    {
        SimpleEncoder enc = new SimpleEncoder();
        byte[] rawBytes = Hex.decode0x(raw);
        enc.writeBytes(Hash.h256(rawBytes));
        enc.writeBytes(rawBytes);
        enc.writeBytes(Hex.decode0x(signature));

        String fullRaw = Hex.encode0x(enc.toBytes());

        return makePostRequest("transaction/raw?raw=" + fullRaw, SignMessageResponse.class, null);
    }

    @Override
    public Map<String, Long> getVotes(String delegate) throws IOException, SemuxException
    {
        return makeRequest("votes?delegate=" + delegate, VotesResponse.class);
    }

    @Override
    public List<String> listAccounts() throws IOException, SemuxException
    {
        return makeRequest("accounts", StringsResponse.class);
    }

    @Override
    public String createAccount() throws IOException, SemuxException
    {
        return makePostRequest("account", StringResponse.class, null);
    }

    @Override
    public Account getAccount(String address) throws IOException, SemuxException
    {
        return makeRequest("account?address=" + address, AccountResponse.class);
    }

    @Override
    public Delegate getDelegate(String delegate) throws IOException, SemuxException
    {
        try
        {
            return makeRequest("delegate?address=" + delegate, DelegateResponse.class);
        } catch (SemuxException e)
        {
            //try getting all and finding
            List<Delegate> delegates = getDelegates();
            for (Delegate del : delegates)
            {
                if (del.getAddress().equalsIgnoreCase(delegate))
                {
                    return del;
                }
            }
            throw new SemuxException("Delegate not found");

        }
    }

    @Override
    public List<Delegate> getDelegates() throws IOException, SemuxException
    {
        return makeRequest("delegates", DelegatesResponse.class);
    }

    @Override
    public List<String> getValidators() throws IOException, SemuxException
    {
        return makeRequest("validators", StringsResponse.class);
    }

    @Override
    public Long getVotes(String delegate, String voterAddress) throws IOException, SemuxException
    {
        return makeRequest("vote?delegate=" + delegate + "&voter=" + voterAddress, LongResponse.class);
    }

    @Override
    public Info getInfo() throws IOException, SemuxException
    {
        return makeRequest("info", InfoResponse.class);
    }

    @Override
    public List<Peer> getPeers() throws IOException, SemuxException
    {
        return makeRequest("peers", PeersResponse.class);
    }

    @Override
    public void addNode(String node, int port) throws IOException, SemuxException
    {
        makePostRequest("node?node=" + node + ":" + port, null, null);
    }

    @Override
    public void addToBlacklist(String ipAddress) throws IOException, SemuxException
    {
        makeRequest("blacklist?ip=" + ipAddress, null);
    }

    @Override
    public void addToWhitelist(String ipAddress) throws IOException, SemuxException
    {
        makeRequest("whitelist?ip=" + ipAddress, null);
    }

    @Override
    public Long getLatestBlockNumber() throws IOException, SemuxException
    {
        return makeRequest("latest-block-number", LongResponse.class);
    }

    @Override
    public Block getLatestBlock() throws IOException, SemuxException
    {
        return makeRequest("latest-block", BlockResponse.class);
    }

    @Override
    public Block getBlock(long blockNum) throws IOException, SemuxException
    {
        return makeRequest("block-by-number?number=" + blockNum, BlockResponse.class);
    }

    @Override
    public Block getBlock(String hash) throws IOException, SemuxException
    {
        return makeRequest("block-by-hash?hash=" + hash, BlockResponse.class);
    }

    @Override
    public List<Transaction> getPendingTransactions() throws IOException, SemuxException
    {
        return makeRequest("pending-transactions", TransactionsResponse.class);
    }

    @Override
    public String transfer(long amountToSend, String from, String to, Long fee, byte[] data) throws IOException, SemuxException
    {
        if (mockPayments)
        {
            logger.info("Skipping payment to " + to + " as we are in debug mode");
            return "mockPayment";
        } else
        {
            String url = "transaction/transfer?from=" + from + "&to=" + to + "&value=" + amountToSend;
            if (fee != null)
            {
                url += "&fee=" + fee;
            }
            if (data != null)
            {
                url += "&data=" + Hex.encode0x(data);
            }
            return makePostRequest(url, StringResponse.class, null);
        }
    }

    @Override
    public String registerDelegate(String address, long fee, String delegateName) throws IOException, SemuxException
    {
        String hexDelegateName = Hex.encode0x(delegateName.getBytes());
        return makePostRequest("transaction/delegate?from=" + address + "&fee=" + fee + "&data=" + hexDelegateName, StringResponse.class, null);
    }

    @Override
    public String vote(String from, String to, long value, long fee) throws IOException, SemuxException
    {
        return makePostRequest("transaction/vote?from=" + from + "&fee=" + fee + "&to=" + to + "&value=" + value, StringResponse.class, null);
    }

    @Override
    public String unvote(String from, String to, long value, long fee) throws IOException, SemuxException
    {
        return makePostRequest("transaction/unvote?from=" + from + "&fee=" + fee + "&to=" + to + "&value=" + value, StringResponse.class, null);
    }

    @Override
    public String create(String from, long gasPrice, long gas, byte[] data) throws IOException, SemuxException
    {
        String body = "data=" + Hex.encode0x(data);
        return makePostRequest("transaction/create?from=" + from + "&value=0&gasPrice=" + gasPrice + "&gas=" + gas, StringResponse.class, body);
    }

    @Override
    public String call(String from, String to, long gasPrice, long gas, byte[] data, boolean local) throws IOException, SemuxException
    {
        String body = "data=" + Hex.encode0x(data);
        return makePostRequest("transaction/call?local=" + local + "&from=" + from + "&to=" + to + "&value=0&gasPrice=" + gasPrice + "&gas=" + gas, StringResponse.class, body);
    }

    @Override
    public List<String> getAllAccounts() throws IOException, SemuxException
    {
        return makeRequest("accounts", StringsResponse.class);
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
            } else if (transaction.getType().equals("UNVOTE"))
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
        return makeRequest("transaction-limits?type=" + type, TransactionLimitsResponse.class);
    }

    private class SimpleResponseHandler implements ResponseHandler<String>
    {
        @Override
        public String handleResponse(
                final HttpResponse response) throws IOException
        {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
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
        HttpGet httpGet = new HttpGet(protocolPrefix + host + ":" + port + "/v2.3.0/" + path);

        logger.debug(httpGet.getURI().toString());
        CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

        if (httpResponse.getStatusLine().getStatusCode() != 200)
        {
            throw new SemuxException("Unknown response code " + httpResponse.getStatusLine().getStatusCode());
        }

        //handle unknown type
        if (responseClass == null)
        {
            SemuxResponse response = objectMapper.readValue(responseString, SemuxResponse.class);
            if (!response.isSuccess())
            {
                throw new SemuxException(response.getMessage());
            } else
            {
                return null;
            }
        } else
        {
            SemuxResponse<T> response = objectMapper.readValue(responseString, responseClass);
            if (response.isSuccess())
            {
                return response.getResult();
            } else
            {
                throw new SemuxException(response.getMessage());
            }
        }
    }


    private <T> T makePostRequest(String path, Class<? extends SemuxResponse<T>> responseClass, String body) throws IOException, SemuxException
    {
        HttpPost httpPost = new HttpPost(protocolPrefix + host + ":" + port + "/v2.3.0/" + path);
        if (body != null)
        {
            HttpEntity entity = new ByteArrayEntity(body.getBytes());
            httpPost.setEntity(entity);
        }
        logger.debug(httpPost.getURI().toString());


        CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

        if (httpResponse.getStatusLine().getStatusCode() != 200)
        {
            throw new SemuxException("Unknown response code " + httpResponse.getStatusLine().getStatusCode());
        }

        //handle unknown type
        if (responseClass == null)
        {
            SemuxResponse response = objectMapper.readValue(responseString, SemuxResponse.class);
            if (!response.isSuccess())
            {
                throw new SemuxException(response.getMessage());
            } else
            {
                return null;
            }
        } else
        {
            SemuxResponse<T> response = objectMapper.readValue(responseString, responseClass);
            if (response.isSuccess())
            {
                return response.getResult();
            } else
            {
                throw new SemuxException(response.getMessage());
            }
        }
    }

}
