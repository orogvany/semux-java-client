import com.semuxpool.client.SemuxClient;
import com.semuxpool.client.api.Account;
import com.semuxpool.client.api.Info;
import com.semuxpool.client.api.SemuxException;
import com.semuxpool.client.api.TransactionLimits;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.List;

public class TestClient
{

    public static void main(String[] args) throws IOException, SemuxException, InvalidKeySpecException
    {

        boolean local = false;
        String address = "0x5c0c8a67ee6c14fa853c4595cd90e57359efe589";
        String from = "0x44f4243318f44f62da495cffb9bb5a944d686a22";
        String transaction = "0xab48e1a200eba7435d68f262186f3d4b85298d8ff29f27af564015dbc968ec06";
        String blockHash = "0x5c04ca3ebc06fba7419484db376722c17d94609077dc27f42b7558902d193557";
        SemuxClient client = new SemuxClient("localhost", 5171, "user", "pass");
        if (!local)
        {
            client = new SemuxClient("api.semux.online", 443, null, null);

        }

        //calls only available on local

        if (local)
        {
            List<String> accounts = client.getAllAccounts();
            for (Iterator<String> iterator = accounts.iterator(); iterator.hasNext(); )
            {
                String next = iterator.next();
                System.out.println(next);
            }
            client.getPeers();
        }

        client.getBlock(blockHash);
        client.getBlock(1000000);
        client.getAccountTransactions(address, 1, 100);
        client.getDelegate(address);
        Info info = client.getInfo();
        TransactionLimits limits = client.getTransactionLimits("TRANSFER");
        Account account = client.getAccount(from);
        client.getLatestBlock();
        client.getLatestBlockNumber();
        client.getPendingTransactions();
        client.getTransaction(transaction);
        client.getValidators();

        // raw transactions
        String raw = client.composeRawTransaction("MAINNET", "TRANSFER", limits.getMinTransactionFee(), account.getNonce(), address, 100000, System.currentTimeMillis(), null);
        System.out.println("Raw tx = " + raw);

//        String signed = client.signMessageLocal("PKEY HERE", raw);
//        System.out.println("Signature = " + signed);
//        client.sendTransaction(raw, signed );


    }
}
