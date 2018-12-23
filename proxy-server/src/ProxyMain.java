import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ProxyMain {
    static Map cache = new HashMap();
    public static void main(String args[]) throws Exception {
        int port = 8887;
        ServerSocket server = new ServerSocket(port);
        //noinspection InfiniteLoopStatement
        System.out.println("\nPort " + port+ " is listening...\n");
        try {
            while(true){

                Socket connected = server.accept();
                System.out.println("Connection Request Accepted From "+connected.getRemoteSocketAddress().toString());

                Proxy proxy = new Proxy(connected);
                //proxy.run();
                Thread thread = new Thread(proxy);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
