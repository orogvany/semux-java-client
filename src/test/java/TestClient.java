import com.semuxpool.client.SemuxClient;
import com.semuxpool.client.api.SemuxException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TestClient {

    public static void main(String[] args) throws IOException, SemuxException {

        String address = "0x541365fe0818ea0d2d7ab7f7bc79f719f5f72227";
        String transaction = "0xab48e1a200eba7435d68f262186f3d4b85298d8ff29f27af564015dbc968ec06";
        String blockHash = "0x5c04ca3ebc06fba7419484db376722c17d94609077dc27f42b7558902d193557";
        SemuxClient client = new SemuxClient("localhost",5171,"user","pass");
        List<String> accounts = client.getAllAccounts();
        for (Iterator<String> iterator = accounts.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            System.out.println(next);
        }

        client.getBlock(blockHash);
//        client.getBlock(121000);
        client.getAccountTransactions(address,1,100);
        client.getAllAccounts();
        client.getDelegate(address);
        client.getInfo();
        client.getLatestBlock();
        client.getLatestBlockNumber();
        client.getPeers();
        client.getPendingTransactions();
        client.getTransaction(transaction);
    }
}
