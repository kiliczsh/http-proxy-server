import java.net.ServerSocket;
import java.net.Socket;

public class ProxyMain {

    public static void main(String args[]) throws Exception {
        int port = 8888;
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
