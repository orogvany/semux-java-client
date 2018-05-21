import com.semuxpool.client.SemuxClient;
import com.semuxpool.client.api.SemuxException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TestClient {

    public static void main(String[] args) throws IOException, SemuxException {

        SemuxClient client = new SemuxClient("localhost",5171,"user","pass");
        List<String> accounts = client.getAllAccounts();
        for (Iterator<String> iterator = accounts.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}
